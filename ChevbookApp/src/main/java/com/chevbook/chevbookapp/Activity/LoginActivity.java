package com.chevbook.chevbookapp.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chevbook.chevbookapp.API.API_user;
import com.chevbook.chevbookapp.Class.Modele;
import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.GCM.GcmUtils;
import com.chevbook.chevbookapp.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;


public class LoginActivity extends ActionBarActivity {

    // Values for email and password at the time of the login attempt.
    private String mEmail;
    private String mPassword;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private Button mCreateAccountButton;
    private Button mLoginButton;
    private TextView mLoginStatusMessageView;
    private TextView mForgotPassword;

    //Modele
    private Modele vmodele;
    private User mUser;

    private GcmUtils mGcmUtils = new GcmUtils();
    private GoogleCloudMessaging gcm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);


        vmodele = new Modele();
        mUser = new User(getApplicationContext());
        if (mUser.isLoggedIn()) {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            finish();

            vmodele.setCurrentUser(mUser);
        }

        if (mGcmUtils.checkPlayServices(LoginActivity.this)) {
            gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
        } else {
            Log.i("GCM", "No valid Google Play Services APK found.");
        }

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                if (id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginButton = (Button) findViewById(R.id.sign_in_button);
        mCreateAccountButton = (Button) findViewById(R.id.create_account);
        mForgotPassword = (TextView) findViewById(R.id.login_forgot_password);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);


        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
                adb.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), getString(R.string.create_account), Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse(getString(R.string.url_web_create_account));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                adb.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.setMessage(getString(R.string.create_account_warning_message));
                adb.show();
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), getString(R.string.action_forgot_password), Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(mEmailView.getText()) && !mEmailView.getText().toString().contains("@")) {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champs d'email !!", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
                    adb.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(), getString(R.string.create_account), Toast.LENGTH_SHORT).show();
                            forgotPassword();
                        }
                    });
                    adb.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    adb.setTitle("Mot de passe oublié");
                    adb.setMessage("Voulez-vous qu'un nouveau mot de passe vous soit envoyé par mail ?");
                    adb.show();
                }
            }
        });
    }

    public GoogleCloudMessaging getGcm() {
        return gcm;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        EasyTracker.getInstance(this).activityStart(this);  // Start Google Analytics
    }

    @Override
    public void onStop() {
        super.onStop();

        EasyTracker.getInstance(this).activityStop(this);  // Stop Google Analytics
    }

    public void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);

            UserLoginTask();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

            mCreateAccountButton.setVisibility(View.VISIBLE);
            mCreateAccountButton.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mCreateAccountButton.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

            mLoginButton.setVisibility(View.VISIBLE);
            mLoginButton.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginButton.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCreateAccountButton.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginButton.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void UserLoginTask() {
        hideSoftKeyboard();

        String[] mesparams = {"identification_user", mEmail, getSha1(mPassword)};
        new API_user(LoginActivity.this).execute(mesparams);
    }

    public void resultUserLoginTask(Boolean success, String mFirstname, String mLastname, String mUrl_image) {
        showProgress(false);

        if (success) {
            //Toast.makeText(getApplicationContext(), regid, Toast.LENGTH_SHORT).show();

            mUser.loginUser(mEmail, mPassword, mFirstname, mLastname, mUrl_image);
            vmodele.setCurrentUser(mUser);

            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Erreur d'authentification", Toast.LENGTH_SHORT).show();
        }
    }

    public void forgotPassword() {
        String[] mesparams = {"password_oublie", mEmailView.getText().toString()};
        new API_user(LoginActivity.this).execute(mesparams);
    }

    public void resultForgotPassword(Boolean result) {
        if (result) {
            Toast.makeText(getApplicationContext(), "Votre nouveau mot de passe viens de vous être envoyé par mail.\nPensez à vérifier dans vos spam !!!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Adresse email invalide", Toast.LENGTH_SHORT).show();
        }
    }

    private static String getSha1(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
    }
}