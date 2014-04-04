package com.chevbook.chevbookapp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;

import com.chevbook.chevbookapp.CustomsView.CircularImageView;
import com.chevbook.chevbookapp.R;
import com.google.analytics.tracking.android.EasyTracker;

import java.io.ByteArrayOutputStream;

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
    @InjectView(R.id.editTextEmail)
    EditText mEditTextEmail;
    @InjectView(R.id.buttonChangePassword)
    Button mButtonChangePassword;
    @InjectView(R.id.buttonSaveModif)
    Button mButtonSaveModif;

    //Custom Dialog
    private EditText mCustomDialogActualPass;
    private EditText mCustomDialogNewPass;
    private EditText mCustomDialogConfirmNewPass;
    private Button mCustomDialogButtonConfirmChange;

    private static final int REQUEST_IMAGE_CAPTURE = 10;
    private static final int REQUEST_SELECT_PICTURE = 100;

    private String Base64Image = "";

    private static ActionBarActivity actionBarActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_details_account);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        getSupportActionBar().setTitle(mNavigationTitles[0]);
        actionBarActivity = (ActionBarActivity) this;

        //click Button
        mBtnChangePicture.setOnClickListener(clickListener);
        mButtonChangePassword.setOnClickListener(clickListener);
        mButtonSaveModif.setOnClickListener(clickListener);
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

                case R.id.buttonSaveModif:
                    saveData();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageViewPictureUser.setImageBitmap(imageBitmap);
                Base64Image = encodeTobase64(imageBitmap);
            }
            else if (requestCode == REQUEST_SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedPath = getPath(selectedImageUri, DetailsAccountActivity.this);
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                btmapOptions.inSampleSize = 4;
                Bitmap imageBitmap = BitmapFactory.decodeFile(selectedPath, btmapOptions);
                mImageViewPictureUser.setImageBitmap(imageBitmap);
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
        Toast.makeText(getApplicationContext(), getString(R.string.save_in_progress), Toast.LENGTH_SHORT).show();
        UpdateUserTask();
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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        image.compress(Bitmap.CompressFormat.JPEG, 70, baos); //0 meaning compress for small size, 100 meaning compress for max quality
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void alertDialogChangePassword() {
        View custom_view_change_password = getLayoutInflater().inflate(R.layout.custom_dialog_change_password, null);

        mCustomDialogActualPass = (EditText)custom_view_change_password.findViewById(R.id.editTextCustomDialogChangePasswordActualPass);
        mCustomDialogNewPass = (EditText)custom_view_change_password.findViewById(R.id.editTextCustomDialogChangePasswordNewPass);
        mCustomDialogConfirmNewPass = (EditText)custom_view_change_password.findViewById(R.id.editTextCustomDialogChangePasswordConfirmNewPass);
        mCustomDialogButtonConfirmChange = (Button)custom_view_change_password.findViewById(R.id.buttonCustomDialogConfirmChangePass);

        mCustomDialogButtonConfirmChange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "click change", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
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

        dialog.show();
    }

    public void UpdateUserTask()
    {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);

                if (success) {
                    //Toast.makeText(getApplication(), Base64Image, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplication(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
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
}
