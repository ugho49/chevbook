package com.chevbook.chevbookapp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;

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
    @InjectView(R.id.imageViewDeposerModifierAnnonceImage4)
    ImageView mImageViewDeposerModifierAnnonceImage4;
    @InjectView(R.id.imageViewDeposerModifierAnnonceImage5)
    ImageView mImageViewDeposerModifierAnnonceImage5;
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

    private Annonce mAnnonce = new Annonce();

    private int ImageSelected = 0;
    private String[] Base64Image = new String[5];

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
        mImageViewDeposerModifierAnnonceImage4.setOnClickListener(clickListener);
        mImageViewDeposerModifierAnnonceImage5.setOnClickListener(clickListener);
        mButtonDeposerModifierAnnonceValider.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {

            switch(v.getId())
            {
                case R.id.imageViewDeposerModifierAnnonceImage1:
                    ImageSelected = 1;
                    showDialogForPicture();
                    break;

                case R.id.imageViewDeposerModifierAnnonceImage2:
                    ImageSelected = 2;
                    showDialogForPicture();
                    break;

                case R.id.imageViewDeposerModifierAnnonceImage3:
                    ImageSelected = 3;
                    showDialogForPicture();
                    break;

                case R.id.imageViewDeposerModifierAnnonceImage4:
                    ImageSelected = 4;
                    showDialogForPicture();
                    break;

                case R.id.imageViewDeposerModifierAnnonceImage5:
                    ImageSelected = 5;
                    showDialogForPicture();
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
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                setBitmapAndEncodeInBase64(imageBitmap, ImageSelected);
            }
            else if (requestCode == REQUEST_SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedPath = getPath(selectedImageUri, DeposerModifierAnnonceActivity.this);
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                btmapOptions.inSampleSize = 4;
                Bitmap imageBitmap = BitmapFactory.decodeFile(selectedPath, btmapOptions);
                setBitmapAndEncodeInBase64(imageBitmap, ImageSelected);
            }
        }
    }

    private void showDialogForPicture()
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(DeposerModifierAnnonceActivity.this);
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
    }

    private void setBitmapAndEncodeInBase64(Bitmap bm, int number)
    {
        switch(number)
        {
            case 1:
                mImageViewDeposerModifierAnnonceImage1.setImageBitmap(bm);
                Base64Image[0] = encodeTobase64(bm);
                break;

            case 2:
                mImageViewDeposerModifierAnnonceImage2.setImageBitmap(bm);
                Base64Image[1] = encodeTobase64(bm);
                break;

            case 3:
                mImageViewDeposerModifierAnnonceImage3.setImageBitmap(bm);
                Base64Image[2] = encodeTobase64(bm);
                break;

            case 4:
                mImageViewDeposerModifierAnnonceImage4.setImageBitmap(bm);
                Base64Image[3] = encodeTobase64(bm);
                break;

            case 5:
                mImageViewDeposerModifierAnnonceImage5.setImageBitmap(bm);
                Base64Image[4] = encodeTobase64(bm);
                break;
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
        //return "";
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
