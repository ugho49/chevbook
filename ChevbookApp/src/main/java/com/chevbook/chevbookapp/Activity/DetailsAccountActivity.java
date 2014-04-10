package com.chevbook.chevbookapp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.CustomsView.CircularImageView;
import com.chevbook.chevbookapp.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailsAccountActivity extends ActionBarActivity {

    @InjectView(R.id.imageViewPictureUser)
    CircularImageView mImageViewPictureUser;
    @InjectView(R.id.btnChangePicture)
    LinearLayout mBtnChangePicture;
    @InjectView(R.id.editTextFirstName)
    EditText mEditTextFirstName;
    @InjectView(R.id.editTextLastName)
    EditText mEditTextLastName;
    @InjectView(R.id.buttonChangePassword)
    Button mButtonChangePassword;
    @InjectView(R.id.buttonSaveModif)
    Button mButtonSaveModif;
    @InjectView(R.id.buttonDeleteImage)
    Button mButtonDeleteImage;
    @InjectView(R.id.progressBarLoadImage)
    ProgressBar mProgressBarLoadImage;

    //Custom Dialog
    private EditText mCustomDialogActualPass;
    private EditText mCustomDialogNewPass;
    private EditText mCustomDialogConfirmNewPass;
    private Button mCustomDialogButtonConfirmChange;

    private static final int REQUEST_IMAGE_CAPTURE = 10;
    private static final int REQUEST_SELECT_PICTURE = 100;
    private static final int REQUEST_CROP_PICTURE = 1000;

    private ProgressDialog progress;

    private String Base64Image = ""; // "" = ne rien faire, "null" = supprimer de la base, "11ebvsrbs46..." = new picture

    private User mUser;

    private static ImageLoader imageLoader;
    private static ActionBarActivity actionBarActivity;

    private String PasswordModifieSHA1 = "";
    private String PasswordModifieSansSHA1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_details_account);
        ButterKnife.inject(this);

        imageLoader = ImageLoader.getInstance();
        mUser = new User(getApplicationContext());

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        getSupportActionBar().setTitle(mNavigationTitles[0]);
        actionBarActivity = (ActionBarActivity) this;

        //click Button
        mBtnChangePicture.setOnClickListener(clickListener);
        mButtonChangePassword.setOnClickListener(clickListener);
        mButtonSaveModif.setOnClickListener(clickListener);
        mButtonDeleteImage.setOnClickListener(clickListener);

        initData();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {

            switch(v.getId())
            {
                case R.id.btnChangePicture:
                    //Toast.makeText(getApplicationContext(), "Change Picture", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder adb = new AlertDialog.Builder(DetailsAccountActivity.this);
                    adb.setPositiveButton(getString(R.string.take_picture_from_camera), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            TakePictureIntent();
                        }
                    });
                    adb.setNegativeButton(getString(R.string.take_picture_from_gallery), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SelectPictureIntent();
                        }
                    });
                    adb.setMessage(getString(R.string.where_take_picture));
                    adb.show();
                    break;

                case R.id.buttonChangePassword:
                    //Toast.makeText(getApplicationContext(), "Change Password", Toast.LENGTH_SHORT).show();
                    alertDialogChangePassword();
                    break;

                case R.id.buttonDeleteImage:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailsAccountActivity.this);
                    alertDialog.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Base64Image = "null";
                            mImageViewPictureUser.setImageResource(R.drawable.ic_user_drag_drop);
                            mButtonDeleteImage.setVisibility(View.GONE);
                        }
                    });
                    alertDialog.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setTitle("Supprimer ?");
                    alertDialog.setMessage("Vous n'aurez plus de photo de profil sur votre compte chevbook.\nVoulez-vous la supprimer quand même ?");
                    alertDialog.show();
                    break;

                case R.id.buttonSaveModif:
                    saveData();
                    break;
            }
        }
    };

    private void initData()
    {
        String url_image = mUser.getUrlProfilPicture();
        if(!url_image.equals(""))
        {
            imageLoader.displayImage(url_image, mImageViewPictureUser);
            mButtonDeleteImage.setVisibility(View.VISIBLE);
        }
        else {
            mImageViewPictureUser.setImageResource(R.drawable.ic_user_drag_drop);
            mButtonDeleteImage.setVisibility(View.GONE);
        }

        mEditTextFirstName.setText(mUser.getFirstName());
        mEditTextLastName.setText(mUser.getLastName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageViewPictureUser.setImageBitmap(imageBitmap);
                mButtonDeleteImage.setVisibility(View.VISIBLE);
                Base64Image = encodeTobase64(imageBitmap);
            }
            else if (requestCode == REQUEST_SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedPath = getPath(selectedImageUri, DetailsAccountActivity.this);
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                btmapOptions.inSampleSize = 4;
                Bitmap imageBitmap = BitmapFactory.decodeFile(selectedPath, btmapOptions);
                mImageViewPictureUser.setImageBitmap(imageBitmap);
                mButtonDeleteImage.setVisibility(View.VISIBLE);
                Base64Image = encodeTobase64(imageBitmap);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.account_menuSave) {
            saveData();
        } else {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveData() {
        //Toast.makeText(getApplicationContext(), getString(R.string.save_in_progress), Toast.LENGTH_SHORT).show();
        String nom = mEditTextLastName.getText().toString();
        String prenom = mEditTextFirstName.getText().toString();

        if(nom.equals(mUser.getLastName()) && prenom.equals(mUser.getFirstName()) && Base64Image.equals("") && PasswordModifieSHA1.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Aucune modification.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            if(nom.equals("") || prenom.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Certains champs obligatoires ne sont pas remplis", Toast.LENGTH_SHORT).show();
            }
            else {
                UpdateUserTask();
            }
        }
    }

    private void TakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void SelectPictureIntent() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                REQUEST_SELECT_PICTURE);
    }

    @SuppressWarnings("deprecation")
    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String encodeTobase64(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 70, baos); //0 meaning compress for small size, 100 meaning compress for max quality
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void alertDialogChangePassword() {
        View custom_view_change_password = getLayoutInflater().inflate(R.layout.custom_dialog_change_password, null);

        mCustomDialogActualPass = (EditText)custom_view_change_password.findViewById(R.id.editTextCustomDialogChangePasswordActualPass);
        mCustomDialogNewPass = (EditText)custom_view_change_password.findViewById(R.id.editTextCustomDialogChangePasswordNewPass);
        mCustomDialogConfirmNewPass = (EditText)custom_view_change_password.findViewById(R.id.editTextCustomDialogChangePasswordConfirmNewPass);
        mCustomDialogButtonConfirmChange = (Button)custom_view_change_password.findViewById(R.id.buttonCustomDialogConfirmChangePass);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(custom_view_change_password)
                .setCancelable(false)
                .setTitle(getString(R.string.change_password))
                .setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(), "Annulation", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .create();

        mCustomDialogButtonConfirmChange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "click change", Toast.LENGTH_SHORT).show();
                //PasswordModifieSHA1
                String old_pass = mCustomDialogActualPass.getText().toString();
                String new_pass = mCustomDialogNewPass.getText().toString();
                String confirm_new_pass = mCustomDialogConfirmNewPass.getText().toString();

                if(old_pass.equals("") || new_pass.equals("") || confirm_new_pass.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tout les champs !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(getSha1(old_pass).equals(mUser.getPasswordSha1()))
                    {
                        if(new_pass.equals(confirm_new_pass))
                        {
                            if(new_pass.equals(old_pass))
                            {
                                Toast.makeText(getApplicationContext(), "Votre nouveau mot de passe doit être différent de l'ancien", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                PasswordModifieSHA1 = getSha1(new_pass);
                                PasswordModifieSansSHA1 = new_pass;
                                Toast.makeText(getApplicationContext(), "Votre mot de passe changera lors de l'enregistrement des modifications", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Erreur dans la confirmation de votre nouveau mot de passe", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Le mot de passe Actuel n'est pas le bon", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();
    }

    public void UpdateUserTask()
    {
        new AsyncTask<Void, Void, Boolean>() {

            String ErreurLoginTask = "Erreur";
            String AfficherJSON = null;
            String url_img ="";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
                progress = new ProgressDialog(DetailsAccountActivity.this);
                progress.setMessage("Modifications en cours ...");
                progress.setCancelable(false);
                progress.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                HttpURLConnection urlConnection = null;
                StringBuilder sb = new StringBuilder();

                try {
                    URL url = new URL(getResources().getString(R.string.URL_SERVEUR) + getResources().getString(R.string.URL_SERVEUR_MODIFICATION_USER));
                    urlConnection = (HttpURLConnection)url.openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setConnectTimeout(5000);
                    OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

                    // Création objet jsonn clé valeur
                    JSONObject jsonParam = new JSONObject();

                    // Exemple Clé valeur utiles à notre application
                    jsonParam.put("email", mUser.getEmail());
                    jsonParam.put("password", mUser.getPasswordSha1());
                    if(PasswordModifieSHA1.equals("")){
                        jsonParam.put("passwordMod", mUser.getPasswordSha1());
                    }
                    else {
                        jsonParam.put("passwordMod", PasswordModifieSHA1);
                    }
                    jsonParam.put("prenomMod", mEditTextFirstName.getText().toString());
                    jsonParam.put("nomMod", mEditTextLastName.getText().toString());
                    jsonParam.put("photoMod", Base64Image);

                    out.write(jsonParam.toString());
                    out.flush();
                    out.close();

                    // récupération du serveur
                    int HttpResult = urlConnection.getResponseCode();
                    if (HttpResult == HttpURLConnection.HTTP_OK)
                    {
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();

                        AfficherJSON = sb.toString();

                        JSONObject jsonObject = new JSONObject(sb.toString());

                        int modifOK = jsonObject.getInt("modifEffectue");

                        if(modifOK == 1){
                            url_img = jsonObject.getString("0");

                            if(url_img.equals("null"))
                            {
                                url_img = "";
                            }
                            return true;
                        } else {
                            return false;
                        }

                    }
                    else
                    {
                        return false;
                    }
                }
                catch (MalformedURLException e){
                    ErreurLoginTask = ErreurLoginTask + "URL";
                    return false; //Erreur URL
                } catch (SocketTimeoutException e) {
                    ErreurLoginTask = ErreurLoginTask + "Temps trop long";
                    return false; //Temps trop long
                } catch (IOException e) {
                    ErreurLoginTask = ErreurLoginTask + "Connexion internet lente ou inexistante";
                    return false; //Pas de connexion internet
                } catch (JSONException e) {
                    ErreurLoginTask = ErreurLoginTask + "Problème de JSON";
                    return false; //Erreur JSON
                } finally {
                    if (urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                //actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
                if (progress.isShowing()) {
                    progress.dismiss();
                }

                if (success) {
                    //todo save data in user
                    Toast.makeText(getApplication(), "Modifications enregistrés", Toast.LENGTH_SHORT).show();

                    mUser.setFirstname(mEditTextFirstName.getText().toString());
                    mUser.setLastname(mEditTextLastName.getText().toString());

                    if(!PasswordModifieSansSHA1.equals("")){
                        mUser.setPassword(PasswordModifieSansSHA1);
                    }

                    Base64Image = "";
                    PasswordModifieSHA1 = "";
                    PasswordModifieSansSHA1 = "";

                    mUser.setUrlProfilPicture(url_img);

                    setResult(2);
                    finish();
                }
                else {
                    Toast.makeText(getApplication(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                }

                /*AlertDialog.Builder adb = new AlertDialog.Builder(DetailsAccountActivity.this);
                adb.setNegativeButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.setMessage(AfficherJSON);
                adb.show();*/
            }
        }.execute();
    }

    private static String getSha1(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
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

    @Override
    public void finish() {
        String nom = mEditTextLastName.getText().toString();
        String prenom = mEditTextFirstName.getText().toString();

        if(nom.equals(mUser.getLastName()) && prenom.equals(mUser.getFirstName()) && Base64Image.equals("") && PasswordModifieSHA1.equals(""))
        {
            set_super_finish();
        }
        else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailsAccountActivity.this);
            alertDialog.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    set_super_finish();
                }
            });
            alertDialog.setNegativeButton(getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.setMessage("Des modifications ne sont pas enregistrés !\nVoulez-vous quitter quand même ?");
            alertDialog.show();
        }
    }

    private void set_super_finish(){
        super.finish();
    }
}
