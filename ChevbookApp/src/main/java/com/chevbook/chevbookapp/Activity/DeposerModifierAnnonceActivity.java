package com.chevbook.chevbookapp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chevbook.chevbookapp.API.API_annonce;
import com.chevbook.chevbookapp.API.API_spinner;
import com.chevbook.chevbookapp.Class.Annonce;
import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @InjectView(R.id.imageViewDeposerModifierAnnonceImage4)
    ImageView mImageViewDeposerModifierAnnonceImage4;
    @InjectView(R.id.imageViewDeposerModifierAnnonceImage5)
    ImageView mImageViewDeposerModifierAnnonceImage5;
    @InjectView(R.id.buttonDeleteImage1)
    Button mButtonDeleteImage1;
    @InjectView(R.id.buttonDeleteImage2)
    Button mButtonDeleteImage2;
    @InjectView(R.id.buttonDeleteImage3)
    Button mButtonDeleteImage3;
    @InjectView(R.id.buttonDeleteImage4)
    Button mButtonDeleteImage4;
    @InjectView(R.id.buttonDeleteImage5)
    Button mButtonDeleteImage5;
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
    @InjectView(R.id.spinnerDeposerModifierAnnonceSousCategorie)
    Spinner mSpinnerDeposerModifierAnnonceSousCategorie;
    @InjectView(R.id.spinnerDeposerModifierAnnonceType)
    Spinner mSpinnerDeposerModifierAnnonceType;
    @InjectView(R.id.editTextDeposerModifierAnnonceNbPieces)
    EditText mEditTextDeposerModifierAnnonceNbPieces;
    @InjectView(R.id.editTextDeposerModifierAnnonceLoyer)
    EditText mEditTextDeposerModifierAnnonceLoyer;
    @InjectView(R.id.checkBoxDeposerModifierAnnonceEstMeuble)
    CheckBox mCheckBoxDeposerModifierAnnonceEstMeuble;
    @InjectView(R.id.editTextDeposerModifierAnnonceSurface)
    EditText mEditTextDeposerModifierAnnonceSurface;
    @InjectView(R.id.buttonDeposerModifierAnnonceValider)
    Button mButtonDeposerModifierAnnonceValider;
    @InjectView(R.id.textViewVersionApp)
    TextView mTextViewVersionApp;


    private static final int CONST_CREATE = 0;
    private static final int CONST_MODIFIER = 1;
    private int CONSTANTE_EN_PARAM;

    private Boolean clickDepotModif = false;
    private Boolean actionDeposerModifier = false;

    private static String [] SpinnerListQuartier;
    private static String [] SpinnerListCategorie;
    private static String [] SpinnerListSousCategorie;
    private static String [] SpinnerListType;

    private static ActionBarActivity actionBarActivity;
    private static ImageLoader imageLoader;

    private static final int REQUEST_IMAGE_CAPTURE = 10;
    private static final int REQUEST_SELECT_PICTURE = 100;

    private Annonce mAnnonce = new Annonce();
    private User mUser;

    private ProgressDialog progress;

    private int ImageSelected = 0;
    private String[] Base64Image = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_deposer_modifier_annonce);
        ButterKnife.inject(this);
        imageLoader = ImageLoader.getInstance();
        mUser = new User(getApplicationContext());

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        getSupportActionBar().setTitle(mNavigationTitles[1]);
        actionBarActivity = (ActionBarActivity) this;

        try {
            Context c = getApplicationContext();
            String versionName = c.getPackageManager()
                    .getPackageInfo(c.getPackageName(), 0).versionName;

            mTextViewVersionApp.setText("V " + versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        for(int i=0; i<=4; i++){
            Base64Image[i] = "";
        }

        Bundle b = getIntent().getExtras();
        int param = b.getInt("CONST");

        if (param == CONST_CREATE) {
            CONSTANTE_EN_PARAM = param;
            mButtonDeposerModifierAnnonceValider.setText("Déposer l'annonce");
            LoadingSpinnerTask();
        } else if (param == CONST_MODIFIER) {
            CONSTANTE_EN_PARAM = param;
            mAnnonce = (Annonce)getIntent().getSerializableExtra("annonce");
            mButtonDeposerModifierAnnonceValider.setText("Modifier l'annonce");
            LoadingSpinnerTask();
        }

        mImageViewDeposerModifierAnnonceImage1.setOnClickListener(clickListener);
        mImageViewDeposerModifierAnnonceImage2.setOnClickListener(clickListener);
        mImageViewDeposerModifierAnnonceImage3.setOnClickListener(clickListener);
        mImageViewDeposerModifierAnnonceImage4.setOnClickListener(clickListener);
        mImageViewDeposerModifierAnnonceImage5.setOnClickListener(clickListener);

        mButtonDeleteImage1.setOnClickListener(clickListener);
        mButtonDeleteImage2.setOnClickListener(clickListener);
        mButtonDeleteImage3.setOnClickListener(clickListener);
        mButtonDeleteImage4.setOnClickListener(clickListener);
        mButtonDeleteImage5.setOnClickListener(clickListener);

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

                case R.id.buttonDeleteImage1:
                    deleteImage(1);
                    break;

                case R.id.buttonDeleteImage2:
                    deleteImage(2);
                    break;

                case R.id.buttonDeleteImage3:
                    deleteImage(3);
                    break;

                case R.id.buttonDeleteImage4:
                    deleteImage(4);
                    break;

                case R.id.buttonDeleteImage5:
                    deleteImage(5);
                    break;

                case R.id.buttonDeposerModifierAnnonceValider:
                    if(!clickDepotModif) {
                        clickDepotModif = true;
                        if(verifDepotAnnonce()){
                            CreerModifierAnnonceTask();
                        }else {
                            clickDepotModif = false;
                        }
                    }
                    else {
                        if (CONSTANTE_EN_PARAM == CONST_MODIFIER) {
                            Toast.makeText(getApplicationContext(), "La modification est en cours", Toast.LENGTH_SHORT).show();
                        } else if (CONSTANTE_EN_PARAM == CONST_CREATE) {
                            Toast.makeText(getApplicationContext(), "La création est en cours", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    };

    public void setSpinnerListCategorie(String[] spinnerListCategorie) {
        SpinnerListCategorie = spinnerListCategorie;
    }

    public void setSpinnerListQuartier(String[] spinnerListQuartier) {
        SpinnerListQuartier = spinnerListQuartier;
    }

    public void setSpinnerListSousCategorie(String[] spinnerListSousCategorie) {
        SpinnerListSousCategorie = spinnerListSousCategorie;
    }

    public void setSpinnerListType(String[] spinnerListType) {
        SpinnerListType = spinnerListType;
    }

    private boolean verifDepotAnnonce()
    {
        Boolean vretour = true;
        View focusView = null;

        int cpt = 0;
        for(int i=0; i<=4; i++){
            if(!Base64Image[i].equals("")){
                cpt++;
            }
        }

        // Reset errors.
        mTextViewDeposerModifierAnnonceTitre.setError(null);
        mEditTextDeposerModifierAnnonceAdresse.setError(null);
        mEditTextDeposerModifierAnnonceCP.setError(null);
        mEditTextDeposerModifierAnnonceVille.setError(null);
        mEditTextDeposerModifierAnnonceDescription.setError(null);
        mEditTextDeposerModifierAnnonceNbPieces.setError(null);
        mEditTextDeposerModifierAnnonceSurface.setError(null);
        mEditTextDeposerModifierAnnonceLoyer.setError(null);

        if(cpt <= 0){
            Toast.makeText(getApplicationContext(), "Il est OBLIGATOIRE de mettre une photo minimum !", Toast.LENGTH_SHORT).show();
            vretour = false;
        }

        if(mEditTextDeposerModifierAnnonceLoyer.getText().toString().equals("")){
            mEditTextDeposerModifierAnnonceLoyer.setError("Champs requis");
            focusView = mEditTextDeposerModifierAnnonceLoyer;
            vretour = false;
        }

        if(mEditTextDeposerModifierAnnonceSurface.getText().toString().equals("")){
            mEditTextDeposerModifierAnnonceSurface.setError("Champs requis");
            focusView = mEditTextDeposerModifierAnnonceSurface;
            vretour = false;
        }

        if(mEditTextDeposerModifierAnnonceNbPieces.getText().toString().equals("")){
            mEditTextDeposerModifierAnnonceNbPieces.setError("Champs requis");
            focusView = mEditTextDeposerModifierAnnonceNbPieces;
            vretour = false;
        }

        if(mEditTextDeposerModifierAnnonceDescription.getText().toString().equals("")){
            mEditTextDeposerModifierAnnonceDescription.setError("Champs requis");
            focusView = mEditTextDeposerModifierAnnonceDescription;
            vretour = false;
        }

        if(CONSTANTE_EN_PARAM == CONST_CREATE){
            if(mEditTextDeposerModifierAnnonceVille.getText().toString().equals("")){
                mEditTextDeposerModifierAnnonceVille.setError("Champs requis");
                focusView = mEditTextDeposerModifierAnnonceVille;
                vretour = false;
            }

            if(mEditTextDeposerModifierAnnonceCP.getText().toString().equals("")){
                mEditTextDeposerModifierAnnonceCP.setError("Champs requis");
                focusView = mEditTextDeposerModifierAnnonceCP;
                vretour = false;
            } else if (mEditTextDeposerModifierAnnonceCP.getText().length() < 5){
                mEditTextDeposerModifierAnnonceCP.setError("Erreur de saisie");
                focusView = mEditTextDeposerModifierAnnonceCP;
                vretour = false;
            }
        }

        if(mEditTextDeposerModifierAnnonceAdresse.getText().toString().equals("")){
            mEditTextDeposerModifierAnnonceAdresse.setError("Champs requis");
            focusView = mEditTextDeposerModifierAnnonceAdresse;
            vretour = false;
        }

        if(mTextViewDeposerModifierAnnonceTitre.getText().toString().equals("")){
            mTextViewDeposerModifierAnnonceTitre.setError("Champs requis");
            focusView = mTextViewDeposerModifierAnnonceTitre;
            vretour = false;
        }

        if(!vretour){
            if (focusView != null) {
                focusView.requestFocus();
            }
        }

        return vretour;
    }

    private void deleteImage(final int pos){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeposerModifierAnnonceActivity.this);
        alertDialog.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Base64Image[pos - 1] = "";

                switch(pos)
                {
                    case 1:
                        mImageViewDeposerModifierAnnonceImage1.setImageResource(R.drawable.ic_image);
                        mButtonDeleteImage1.setVisibility(View.GONE);
                        break;

                    case 2:
                        mImageViewDeposerModifierAnnonceImage2.setImageResource(R.drawable.ic_image);
                        mButtonDeleteImage2.setVisibility(View.GONE);
                        break;

                    case 3:
                        mImageViewDeposerModifierAnnonceImage3.setImageResource(R.drawable.ic_image);
                        mButtonDeleteImage3.setVisibility(View.GONE);
                        break;

                    case 4:
                        mImageViewDeposerModifierAnnonceImage4.setImageResource(R.drawable.ic_image);
                        mButtonDeleteImage4.setVisibility(View.GONE);
                        break;

                    case 5:
                        mImageViewDeposerModifierAnnonceImage5.setImageResource(R.drawable.ic_image);
                        mButtonDeleteImage5.setVisibility(View.GONE);
                        break;
                }
            }
        });
        alertDialog.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setTitle("Supprimer ?");
        alertDialog.setMessage("Voulez-vous supprimer cette photo ?");
        alertDialog.show();
    }

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
                btmapOptions.inSampleSize = 3;
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

    private void setBitmapAndEncodeInBase64(final Bitmap bm, final int number)
    {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progress = new ProgressDialog(DeposerModifierAnnonceActivity.this);
                progress.setMessage("Compression de la photo ...");
                progress.setCancelable(false);
                progress.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                Base64Image[number - 1] = encodeTobase64(bm);

                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {

                if (progress.isShowing()) {
                    progress.dismiss();
                }

                if (success) {
                    switch(number)
                    {
                        case 1:
                            mImageViewDeposerModifierAnnonceImage1.setImageBitmap(bm);
                            mButtonDeleteImage1.setVisibility(View.VISIBLE);
                            break;

                        case 2:
                            mImageViewDeposerModifierAnnonceImage2.setImageBitmap(bm);
                            mButtonDeleteImage2.setVisibility(View.VISIBLE);
                            break;

                        case 3:
                            mImageViewDeposerModifierAnnonceImage3.setImageBitmap(bm);
                            mButtonDeleteImage3.setVisibility(View.VISIBLE);
                            break;

                        case 4:
                            mImageViewDeposerModifierAnnonceImage4.setImageBitmap(bm);
                            mButtonDeleteImage4.setVisibility(View.VISIBLE);
                            break;

                        case 5:
                            mImageViewDeposerModifierAnnonceImage5.setImageBitmap(bm);
                            mButtonDeleteImage5.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                else {
                    Base64Image[number - 1] = "";
                    Toast.makeText(getApplication(), "Erreur de compression de la photo", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();

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
        String base64 = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos); //0 meaning compress for small size, 100 meaning compress for max quality
        byte[] b = baos.toByteArray();
        base64 = "BASE64: " + Base64.encodeToString(b, Base64.DEFAULT);
        return base64;
    }

    public void LoadingSpinnerTask()
    {
        progress = new ProgressDialog(DeposerModifierAnnonceActivity.this);
        progress.setMessage(getResources().getString(R.string.loading_in_progress));
        progress.setCancelable(false);
        progress.show();

        String[] mesparams = {"lister_spinner_annonce"};
        new API_spinner(DeposerModifierAnnonceActivity.this).execute(mesparams);
    }

    public void remplirSpinner(Boolean success)
    {
        if (progress.isShowing()) {
            progress.dismiss();
        }

        if (success) {

            ArrayAdapter<String> spinnerQuartierArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, SpinnerListQuartier);
            ArrayAdapter<String> spinnerCategorieArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, SpinnerListCategorie);
            ArrayAdapter<String> spinnerSousCategorieArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, SpinnerListSousCategorie);
            ArrayAdapter<String> spinnerTypeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, SpinnerListType);

            mSpinnerDeposerModifierAnnonceQuartier.setAdapter(spinnerQuartierArrayAdapter);
            mSpinnerDeposerModifierAnnonceCategorie.setAdapter(spinnerCategorieArrayAdapter);
            mSpinnerDeposerModifierAnnonceSousCategorie.setAdapter(spinnerSousCategorieArrayAdapter);
            mSpinnerDeposerModifierAnnonceType.setAdapter(spinnerTypeArrayAdapter);

            if(CONSTANTE_EN_PARAM == CONST_MODIFIER)
            {
                initDataAnnonce();
            }
        }
        else {
            Toast.makeText(getApplication(), "Erreur de chargement des spinners", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initDataAnnonce()
    {
        //titre
        mTextViewDeposerModifierAnnonceTitre.setText(mAnnonce.getTitre_annonce());

        //Adresse
        mEditTextDeposerModifierAnnonceAdresse.setText(mAnnonce.getAdresse_annonce());
        mEditTextDeposerModifierAnnonceCP.setVisibility(View.GONE);
        mEditTextDeposerModifierAnnonceVille.setVisibility(View.GONE);

        //Description
        mEditTextDeposerModifierAnnonceDescription.setText(mAnnonce.getDescription_annonce());

        //Photos
        ArrayList<String> photos_annonce = mAnnonce.getUrl_images_annonces();
        if(photos_annonce.size() > 0)
        {
            ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
            imageViews.add(0, mImageViewDeposerModifierAnnonceImage1);
            imageViews.add(1, mImageViewDeposerModifierAnnonceImage2);
            imageViews.add(2, mImageViewDeposerModifierAnnonceImage3);
            imageViews.add(3, mImageViewDeposerModifierAnnonceImage4);
            imageViews.add(4, mImageViewDeposerModifierAnnonceImage5);

            ArrayList<Button> buttons = new ArrayList<Button>();
            buttons.add(0, mButtonDeleteImage1);
            buttons.add(1, mButtonDeleteImage2);
            buttons.add(2, mButtonDeleteImage3);
            buttons.add(3, mButtonDeleteImage4);
            buttons.add(4, mButtonDeleteImage5);

            int cpt = 0;

            for(int i=0; i<=4 && i<photos_annonce.size(); i++)
            {
                if(!photos_annonce.get(i).equals("")){
                    imageLoader.displayImage(photos_annonce.get(i), imageViews.get(i));
                    buttons.get(i).setVisibility(View.VISIBLE);
                    Base64Image[cpt] = "URL: " + photos_annonce.get(i);
                    cpt++;
                }
            }
        }

        //Spinner
        for(int i=0; i< SpinnerListType.length; i++)
        {
            if(SpinnerListType[i].equals(mAnnonce.getType_location_annonce())){
                mSpinnerDeposerModifierAnnonceType.setSelection(i);
            }
        }

        for(int i=0; i< SpinnerListQuartier.length; i++)
        {
            if(SpinnerListQuartier[i].equals(mAnnonce.getQuartier_annonce())){
                mSpinnerDeposerModifierAnnonceQuartier.setSelection(i);
            }
        }

        for(int i=0; i< SpinnerListCategorie.length; i++)
        {
            if(SpinnerListCategorie[i].equals(mAnnonce.getCategorie_annonce())){
                mSpinnerDeposerModifierAnnonceCategorie.setSelection(i);
            }
        }

        for(int i=0; i< SpinnerListSousCategorie.length; i++)
        {
            if(SpinnerListSousCategorie[i].equals(mAnnonce.getSousCategorie_annonce())){
                mSpinnerDeposerModifierAnnonceSousCategorie.setSelection(i);
            }
        }

        //Reste
        if(mAnnonce.get_isMeuble()){
            mCheckBoxDeposerModifierAnnonceEstMeuble.setChecked(true);
        }
        else {
            mCheckBoxDeposerModifierAnnonceEstMeuble.setChecked(false);
        }

        mEditTextDeposerModifierAnnonceNbPieces.setText(Integer.toString(mAnnonce.getNumber_room_annonce()));
        mEditTextDeposerModifierAnnonceSurface.setText(Integer.toString(mAnnonce.getSurface_annonce()));
        mEditTextDeposerModifierAnnonceLoyer.setText(Double.toString(mAnnonce.getPrix_annonce()));
    }

    public void CreerModifierAnnonceTask()
    {
        actionDeposerModifier = true;
        String[] mesparams;
        //actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
        progress = new ProgressDialog(DeposerModifierAnnonceActivity.this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());

        int est_meuble = 0;
        if(mCheckBoxDeposerModifierAnnonceEstMeuble.isChecked()){
            est_meuble = 1;
        }else {
            est_meuble = 0;
        }

        JSONArray jsonArrayImages = new JSONArray();
        for(int i=0; i<=4; i++){
            if (!Base64Image[i].equals("")){
                jsonArrayImages.put(Base64Image[i]);
            }
        }

        String action = "";
        String id_annonce = "";
        String adresse = "";

        if (CONSTANTE_EN_PARAM == CONST_MODIFIER) { //modifier annonce
            progress.setMessage("Modification de l'annonce en cours ...");

            action = "modifier_annonce";
            id_annonce = Integer.toString(mAnnonce.getId_annonce());
            adresse = mEditTextDeposerModifierAnnonceAdresse.getText().toString();
        }
        else { //créer annonce
            progress.setMessage("Création de l'annonce en cours ...\n\nL'upload des photos sur le serveur peut prendre un petit peu de temps ! (Prenez un café ^^)");

            action = "creer_annonce";
            id_annonce = "";
            adresse = mEditTextDeposerModifierAnnonceAdresse.getText().toString() + ", " + mEditTextDeposerModifierAnnonceCP.getText().toString() + " " + mEditTextDeposerModifierAnnonceVille.getText().toString();
        }

        progress.setCancelable(false);
        progress.show();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mesparams = new String[]{
                action,
                id_annonce,
                date.toString(),
                mTextViewDeposerModifierAnnonceTitre.getText().toString(),
                mEditTextDeposerModifierAnnonceLoyer.getText().toString(),
                mEditTextDeposerModifierAnnonceDescription.getText().toString(),
                mEditTextDeposerModifierAnnonceNbPieces.getText().toString(),
                adresse,
                mEditTextDeposerModifierAnnonceSurface.getText().toString(),
                Integer.toString(est_meuble),
                mSpinnerDeposerModifierAnnonceType.getSelectedItem().toString(),
                mSpinnerDeposerModifierAnnonceQuartier.getSelectedItem().toString(),
                mSpinnerDeposerModifierAnnonceSousCategorie.getSelectedItem().toString(),
                mSpinnerDeposerModifierAnnonceCategorie.getSelectedItem().toString(),
                jsonArrayImages.toString()};

        new API_annonce(DeposerModifierAnnonceActivity.this).execute(mesparams);
    }

    public void resultCreerModifierAnnonce(Boolean success){

        //actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        clickDepotModif = false;

        if (progress.isShowing()) {
            progress.dismiss();
        }

        if (success) {
            actionDeposerModifier = true;

            if(CONSTANTE_EN_PARAM == CONST_MODIFIER) {
                Toast.makeText(getApplicationContext(), "Votre annonce est modifié !", Toast.LENGTH_SHORT).show();
                setResult(40);
            }
            else {
                Toast.makeText(getApplicationContext(), "Votre annonce est créé avec SUCCES", Toast.LENGTH_SHORT).show();
                setResult(20);
            }

            finish();
        }
        else {
            actionDeposerModifier = false;

            if(CONSTANTE_EN_PARAM == CONST_MODIFIER) {
                Toast.makeText(getApplicationContext(), "Echec de la modification de l'annonce", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Echec de la création de l'annonce", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        if(!actionDeposerModifier) {
            if (CONSTANTE_EN_PARAM == CONST_CREATE) {
                int cpt = 0;
                for (int i = 0; i <= 4; i++) {
                    if (!Base64Image[i].equals("")) {
                        cpt++;
                    }
                }

                if (cpt > 0
                        || !mEditTextDeposerModifierAnnonceLoyer.getText().toString().equals("")
                        || !mEditTextDeposerModifierAnnonceSurface.getText().toString().equals("")
                        || !mEditTextDeposerModifierAnnonceNbPieces.getText().toString().equals("")
                        || !mEditTextDeposerModifierAnnonceDescription.getText().toString().equals("")
                        || !mEditTextDeposerModifierAnnonceVille.getText().toString().equals("")
                        || !mEditTextDeposerModifierAnnonceCP.getText().toString().equals("")
                        || !mEditTextDeposerModifierAnnonceAdresse.getText().toString().equals("")
                        || !mTextViewDeposerModifierAnnonceTitre.getText().toString().equals("")) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeposerModifierAnnonceActivity.this);
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
                    alertDialog.setMessage("Une création d'annonce est en cours !\nVoulez-vous quitter quand même ?");
                    alertDialog.show();
                } else {
                    super.finish();
                }
            } else if (CONSTANTE_EN_PARAM == CONST_MODIFIER) {
                int cpt = 0;
                int cpt1 = 0;
                for (int i = 0; i <= 4; i++) {
                    if (!Base64Image[i].equals("")) {
                        cpt++;
                    }
                }

                for (String S : mAnnonce.getUrl_images_annonces()) {
                    if (!S.equals("")) {
                        cpt1++;
                    }
                }

                if (cpt != cpt1
                        || Double.parseDouble(mEditTextDeposerModifierAnnonceLoyer.getText().toString()) != mAnnonce.getPrix_annonce()
                        || Integer.parseInt(mEditTextDeposerModifierAnnonceSurface.getText().toString()) != mAnnonce.getSurface_annonce()
                        || Integer.parseInt(mEditTextDeposerModifierAnnonceNbPieces.getText().toString()) != mAnnonce.getNumber_room_annonce()
                        || !mEditTextDeposerModifierAnnonceDescription.getText().toString().equals(mAnnonce.getDescription_annonce())
                        || !mEditTextDeposerModifierAnnonceAdresse.getText().toString().equals(mAnnonce.getAdresse_annonce())
                        || !mTextViewDeposerModifierAnnonceTitre.getText().toString().equals(mAnnonce.getTitre_annonce())) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeposerModifierAnnonceActivity.this);
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
                    alertDialog.setMessage("Une modification est en cours !\nVoulez-vous quitter quand même ?");
                    alertDialog.show();
                } else {
                    super.finish();
                }
            } else {
                super.finish();
            }
        }
        else {
            super.finish();
        }
    }

    protected void set_super_finish(){
        super.finish();
    }
}
