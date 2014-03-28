package com.example.chevbook.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chevbook.Class.Annonce;
import com.example.chevbook.CustomDialog.CustomDialogMap;
import com.example.chevbook.CustomDialog.CustomDialogMessage;
import com.example.chevbook.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailsAnnonceActivity extends ActionBarActivity {

    @InjectView(R.id.view_pager_detail_appartement)
    ViewPager mViewPagerDetailAppartement;
    @InjectView(R.id.indicator_view_pager_detail_appartement)
    CirclePageIndicator mIndicatorViewPagerDetailAppartement;
    @InjectView(R.id.buttonDetailAppartementLookOnMap)
    Button mButtonDetailAppartementLookOnMap;
    @InjectView(R.id.textViewDetailAnnonceTitre)
    TextView mTextViewDetailAnnonceTitre;
    @InjectView(R.id.textViewDetailAppartementDescription)
    TextView mTextViewDetailAppartementDescription;
    @InjectView(R.id.textViewDetailAppartementLoyer)
    TextView mTextViewDetailAppartementLoyer;
    @InjectView(R.id.textViewDetailAppartementQuartier)
    TextView mTextViewDetailAppartementQuartier;
    @InjectView(R.id.textViewDetailAppartementCategorie)
    TextView mTextViewDetailAppartementCategorie;
    @InjectView(R.id.textViewDetailAppartementSurface)
    TextView mTextViewDetailAppartementSurface;
    @InjectView(R.id.textViewDetailAppartementNbPiece)
    TextView mTextViewDetailAppartementNbPiece;
    @InjectView(R.id.textViewDetailAppartementUser)
    TextView mTextViewDetailAppartementUser;
    @InjectView(R.id.textViewDetailAppartementDate)
    TextView mTextViewDetailAppartementDate;
    @InjectView(R.id.textViewDetailAppartementSousCategorie)
    TextView mTextViewDetailAppartementSousCategorie;
    @InjectView(R.id.textViewDetailAppartementType)
    TextView mTextViewDetailAppartementType;
    @InjectView(R.id.textViewDetailAppartementEstMeuble)
    TextView mTextViewDetailAppartementEstMeuble;

    private static ActionBarActivity actionBarActivity;
    private static ImageLoader imageLoader;
    private Annonce mAnnonce;

    private MenuItem menuAddFavoris;
    private MenuItem menuDeleteFavoris;
    private MenuItem menuSendMessage;

    private CustomDialogMap dialogMap;
    private CustomDialogMessage dialogMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_details_annonce);
        ButterKnife.inject(this);
        imageLoader = ImageLoader.getInstance();

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        getSupportActionBar().setTitle(mNavigationTitles[2]);
        actionBarActivity = (ActionBarActivity) this;


        mAnnonce = (Annonce)getIntent().getSerializableExtra("annonce");

        initData();

        Boolean afficheGallerieImage = false;

        for(String S : mAnnonce.getUrl_images_annonces())
        {
            if(!S.equals(""))
            {
                afficheGallerieImage = true;
            }
        }

        if(afficheGallerieImage) {
            ImagePagerAdapter adapter = new ImagePagerAdapter(mAnnonce.getUrl_images_annonces());
            mViewPagerDetailAppartement.setAdapter(adapter);
            mIndicatorViewPagerDetailAppartement.setStrokeColor(Color.WHITE);
            mIndicatorViewPagerDetailAppartement.setViewPager(mViewPagerDetailAppartement);
        }else {
            mViewPagerDetailAppartement.setVisibility(View.GONE);
            mIndicatorViewPagerDetailAppartement.setVisibility(View.GONE);
        }

        dialogMap = new CustomDialogMap(DetailsAnnonceActivity.this, mAnnonce.getAdresse_annonce());
        dialogMessage = new CustomDialogMessage(DetailsAnnonceActivity.this);

        dialogMap.createDialog();
        dialogMessage.createDialog();


        mButtonDetailAppartementLookOnMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //showCustomDialogMap();
                dialogMap.showDialog();
            }
        });
    }

    private void initData(){
        mTextViewDetailAnnonceTitre.setText(mAnnonce.getTitre_annonce());
        mTextViewDetailAppartementDescription.setText(mAnnonce.getDescription_annonce());
        mTextViewDetailAppartementLoyer.setText(Double.toString(mAnnonce.getPrix_annonce()) + " €");
        mTextViewDetailAppartementQuartier.setText(mAnnonce.getQuartier_annonce());
        mTextViewDetailAppartementCategorie.setText(mAnnonce.getCategorie_annonce());
        mTextViewDetailAppartementSurface.setText(Integer.toString(mAnnonce.getSurface_annonce()) + "m²");
        mTextViewDetailAppartementNbPiece.setText(Integer.toString(mAnnonce.getNumber_room_annonce()));
        mTextViewDetailAppartementSousCategorie.setText(mAnnonce.getSousCategorie_annonce());
        mTextViewDetailAppartementType.setText(mAnnonce.getType_location_annonce());

        String est_meuble = "";

        if(mAnnonce.get_isMeuble()){est_meuble = "oui";}
        else{est_meuble = "non";}

        mTextViewDetailAppartementEstMeuble.setText(est_meuble);

        mTextViewDetailAppartementUser.setText(mAnnonce.getEmail_user_annonce());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        String DateAndTime = sdf.format(mAnnonce.getDate_create_annonce());
        mTextViewDetailAppartementDate.setText("Le " + DateAndTime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_appartement, menu);

        menuAddFavoris = menu.findItem(R.id.menu_detail_appartements_rate_not_important);
        menuDeleteFavoris = menu.findItem(R.id.menu_detail_appartements_rate_important);
        menuSendMessage = menu.findItem(R.id.menu_detail_appartements_new_message);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_detail_appartements_new_message:
                //Toast.makeText(getApplicationContext(), "Envoi d'un message", Toast.LENGTH_SHORT).show();
                //showCustomDialogMessage();

                dialogMessage.setUserName("Ugho49");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                String currentDateAndTime = sdf.format(new Date());

                dialogMessage.setDateMessage(currentDateAndTime);
                dialogMessage.setImageUserMessage("https://fbcdn-sphotos-e-a.akamaihd.net/hphotos-ak-prn2/t1/1546110_10202187125312508_9323923_n.jpg");

                dialogMessage.showDialog();

                break;

            case R.id.menu_detail_appartements_rate_important:
                //delete to favoris
                setMenuFavorisFullstar(false);
                break;

            case R.id.menu_detail_appartements_rate_not_important:
                //add to favoris
                setMenuFavorisFullstar(true);
                break;

            default:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setMenuFavorisFullstar(Boolean b) {
        //True for show full star
        //false for show empty star
        menuAddFavoris.setVisible(!b);
        menuDeleteFavoris.setVisible(b);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private ArrayList<String> mImages = new ArrayList<String>();

        //Constructor
        private ImagePagerAdapter() {
            //empty constructor
        }

        private ImagePagerAdapter(ArrayList<String> mImages) {
            //constructor
            this.mImages = mImages;
        }

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(7, 0, 7, 0);

            // int padding = context.getResources().getDimensionPixelSize(
            //       R.dimen.padding_medium);
            //imageView.setPadding(padding, padding, padding, padding);

            //imageView.setImageResource(mImages[position]);

            imageLoader.displayImage(mImages.get(position), imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intentImage = new Intent(getApplication(), FullscreenPictureActivity.class);
                    intentImage.putExtra("position", position);
                    intentImage.putExtra("ListeURL", mImages);
                    startActivity(intentImage);
                }
            });

            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
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
