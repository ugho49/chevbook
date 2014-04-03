package com.chevbook.chevbookapp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.chevbook.chevbookapp.Class.Annonce;
import com.chevbook.chevbookapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DeposerModifierAnnonceActivity extends ActionBarActivity {

    @InjectView(R.id.textViewDeposerModifierAnnonceTitre)
    EditText mTextViewDeposerModifierAnnonceTitre;
    @InjectView(R.id.imageViewDeposerModifierAnnonceImage1)
    ImageView mImageViewDeposerModifierAnnonceImage1;
    @InjectView(R.id.imageViewDeposerModifierAnnonceImage2)
    ImageView mImageViewDeposerModifierAnnonceImage2;
    @InjectView(R.id.imageViewDeposerModifierAnnonceImage3)
    ImageView mImageViewDeposerModifierAnnonceImage3;
    @InjectView(R.id.editTextDeposerModifierAnnonceAdresse)
    EditText mEditTextDeposerModifierAnnonceAdresse;
    @InjectView(R.id.editTextDeposerModifierAnnonceCP)
    EditText mEditTextDeposerModifierAnnonceCP;
    @InjectView(R.id.editTextDeposerModifierAnnonceVille)
    EditText mEditTextDeposerModifierAnnonceVille;
    @InjectView(R.id.editTextDeposerModifierAnnonceDescription)
    EditText mEditTextDeposerModifierAnnonceDescription;
    @InjectView(R.id.spinnerDeposerModifierAnnonceQuartier)
    Spinner mSpinnerDeposerModifierAnnonceQuartier;
    @InjectView(R.id.spinnerDeposerModifierAnnonceCategorie)
    Spinner mSpinnerDeposerModifierAnnonceCategorie;
    @InjectView(R.id.editTextDeposerModifierAnnonceNbPieces)
    EditText mEditTextDeposerModifierAnnonceNbPieces;
    @InjectView(R.id.editTextDeposerModifierAnnonceLoyer)
    EditText mEditTextDeposerModifierAnnonceLoyer;
    @InjectView(R.id.buttonDeposerModifierAnnonceValider)
    Button mButtonDeposerModifierAnnonceValider;


    private static final int CONST_CREATE = 0;
    private static final int CONST_MODIFIER = 1;

    private static String [] AppartementListQuartier;
    private static String [] AppartementListType;

    private static ActionBarActivity actionBarActivity;
    private static ImageLoader imageLoader;

    private static final int REQUEST_IMAGE_CAPTURE = 10;
    private static final int REQUEST_SELECT_PICTURE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private Annonce mAnnonce = new Annonce();

    private String selectedPathImage1;
    private String selectedPathImage2;
    private String selectedPathImage3;

    private int ImageSelected = 0;

    private static Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_deposer_modifier_annonce);
        ButterKnife.inject(this);
        imageLoader = ImageLoader.getInstance();

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        getSupportActionBar().setTitle(mNavigationTitles[2]);
        actionBarActivity = (ActionBarActivity) this;

        remplirSpinner();

        Bundle b = getIntent().getExtras();
        int param = b.getInt("CONST");

        if (param == CONST_CREATE) {
            //Toast.makeText(getApplicationContext(), "CREATE", Toast.LENGTH_SHORT).show();
        } else if (param == CONST_MODIFIER) {
            //Toast.makeText(getApplicationContext(), "MODIFIER", Toast.LENGTH_SHORT).show();
            mAnnonce = (Annonce)getIntent().getSerializableExtra("annonce");
            initData();
        }

        mImageViewDeposerModifierAnnonceImage1.setOnClickListener(clickListener);
        mImageViewDeposerModifierAnnonceImage2.setOnClickListener(clickListener);
        mImageViewDeposerModifierAnnonceImage3.setOnClickListener(clickListener);
        mButtonDeposerModifierAnnonceValider.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {

            switch(v.getId())
            {
                case R.id.imageViewDeposerModifierAnnonceImage1:
                    Toast.makeText(getApplicationContext(), "Image 1", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder adb = new AlertDialog.Builder(DeposerModifierAnnonceActivity.this);
                    adb.setPositiveButton(getString(R.string.take_picture_from_camera), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ImageSelected = 1;
                            TakePictureIntent();
                        }
                    });
                    adb.setNegativeButton(getString(R.string.take_picture_from_gallery), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ImageSelected = 1;
                            SelectPictureIntent();
                        }
                    });
                    adb.setMessage(getString(R.string.where_take_picture));
                    adb.show();

                    break;

                case R.id.imageViewDeposerModifierAnnonceImage2:
                    Toast.makeText(getApplicationContext(), "Image 2", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder adb2 = new AlertDialog.Builder(DeposerModifierAnnonceActivity.this);
                    adb2.setPositiveButton(getString(R.string.take_picture_from_camera), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ImageSelected = 2;
                            TakePictureIntent();
                        }
                    });
                    adb2.setNegativeButton(getString(R.string.take_picture_from_gallery), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ImageSelected = 2;
                            SelectPictureIntent();
                        }
                    });
                    adb2.setMessage(getString(R.string.where_take_picture));
                    adb2.show();

                    break;

                case R.id.imageViewDeposerModifierAnnonceImage3:
                    Toast.makeText(getApplicationContext(), "Image 3", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder adb3 = new AlertDialog.Builder(DeposerModifierAnnonceActivity.this);
                    adb3.setPositiveButton(getString(R.string.take_picture_from_camera), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ImageSelected = 3;
                            TakePictureIntent();
                        }
                    });
                    adb3.setNegativeButton(getString(R.string.take_picture_from_gallery), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ImageSelected = 3;
                            SelectPictureIntent();
                        }
                    });
                    adb3.setMessage(getString(R.string.where_take_picture));
                    adb3.show();

                    break;

                case R.id.buttonDeposerModifierAnnonceValider:
                    Toast.makeText(getApplicationContext(), "Deposer annonce", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deposer_modifier_annonce, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        this.finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_IMAGE_CAPTURE) {

                String selectedPath = fileUri.getPath();

                switch(ImageSelected)
                {
                    case 1:
                        selectedPathImage1 = selectedPath;
                        break;

                    case 2:
                        selectedPathImage2 = selectedPath;

                        break;

                    case 3:
                        selectedPathImage3 = selectedPath;

                        break;
                }

                AfterTakingPictureTask afterTakingPictureTask = new AfterTakingPictureTask();
                afterTakingPictureTask.execute((Void) null);

            } else if (requestCode == REQUEST_SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri, DeposerModifierAnnonceActivity.this);
                ImageView mImageViewPicture = null;

                switch(ImageSelected)
                {
                    case 1:
                        selectedPathImage1 = tempPath;
                        mImageViewPicture = mImageViewDeposerModifierAnnonceImage1;
                        break;

                    case 2:
                        selectedPathImage2 = tempPath;
                        mImageViewPicture = mImageViewDeposerModifierAnnonceImage2;

                        break;

                    case 3:
                        selectedPathImage3 = tempPath;
                        mImageViewPicture = mImageViewDeposerModifierAnnonceImage3;
                        break;
                }

                //String selectedPath = tempPath;
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                mImageViewPicture.setImageBitmap(bm);
            }
        }

    }

    private void TakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        //intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, "portrait");
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
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

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "chevbook/temp");


        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("chevbook", "failed to create directory chevbook/temp");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        }

        return mediaFile;
    }

    public class AfterTakingPictureTask extends AsyncTask<Void, Void, Boolean> {

        Bitmap bm;



        @Override
        protected Boolean doInBackground(Void... params) {

            String selectedPath = null;

            switch(ImageSelected)
            {
                case 1:
                    selectedPath = selectedPathImage1;
                    break;

                case 2:
                    selectedPath = selectedPathImage2;

                    break;

                case 3:
                    selectedPath = selectedPathImage3;
                    break;
            }

            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
            bm = BitmapFactory.decodeFile(selectedPath, btmapOptions);

            File f = new File(selectedPath);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(f.getPath());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            bm = Bitmap.createBitmap(bm , 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);

            if (!success) {
                Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
            else {
                ImageView mImageViewPicture = null;

                switch(ImageSelected)
                {
                    case 1:
                        mImageViewPicture = mImageViewDeposerModifierAnnonceImage1;
                        break;

                    case 2:
                        mImageViewPicture = mImageViewDeposerModifierAnnonceImage2;

                        break;

                    case 3:
                        mImageViewPicture = mImageViewDeposerModifierAnnonceImage3;
                        break;
                }

                mImageViewPicture.setImageBitmap(bm);
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
        }
    }

    private void initData()
    {

    }

    private void remplirSpinner()
    {
        AppartementListQuartier = getResources().getStringArray(R.array.appartements_quartier_array);
        AppartementListType = getResources().getStringArray(R.array.appartements_type_location_array);

        ArrayAdapter<String> spinnerQuartierArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, AppartementListQuartier);
        ArrayAdapter<String> spinnerTypeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, AppartementListType);

        mSpinnerDeposerModifierAnnonceQuartier.setAdapter(spinnerQuartierArrayAdapter);
        mSpinnerDeposerModifierAnnonceCategorie.setAdapter(spinnerTypeArrayAdapter);
    }
}
