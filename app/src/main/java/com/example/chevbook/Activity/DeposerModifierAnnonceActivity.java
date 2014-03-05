package com.example.chevbook.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chevbook.R;
import com.nostra13.universalimageloader.core.ImageLoader;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                    break;

                case R.id.imageViewDeposerModifierAnnonceImage2:
                    Toast.makeText(getApplicationContext(), "Image 2", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.imageViewDeposerModifierAnnonceImage3:
                    Toast.makeText(getApplicationContext(), "Image 3", Toast.LENGTH_SHORT).show();

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

    private void remplirSpinner()
    {
        AppartementListQuartier = getResources().getStringArray(R.array.appartements_quartier_array);
        AppartementListType = getResources().getStringArray(R.array.appartements_type_array);

        ArrayAdapter<String> spinnerQuartierArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, AppartementListQuartier);
        ArrayAdapter<String> spinnerTypeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, AppartementListType);

        mSpinnerDeposerModifierAnnonceQuartier.setAdapter(spinnerQuartierArrayAdapter);
        mSpinnerDeposerModifierAnnonceCategorie.setAdapter(spinnerTypeArrayAdapter);
    }
}
