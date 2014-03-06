package com.example.chevbook.Activity;

import android.content.Intent;
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

        ImagePagerAdapter adapter = new ImagePagerAdapter(mAnnonce.getUrl_images_annonces());
        mViewPagerDetailAppartement.setAdapter(adapter);
        mIndicatorViewPagerDetailAppartement.setViewPager(mViewPagerDetailAppartement);

        dialogMap = new CustomDialogMap(DetailsAnnonceActivity.this, mAnnonce.getAdresse_annonce() + ", " + mAnnonce.getCode_postal_annonce() + " " + mAnnonce.getVille_annonce());
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
            /*mImages.add("http://jeudepaumehotel.com/ile-saint-louis/wp-content/uploads/2010/09/jeudepaume-hotel-ile-saint-louis-appartement.jpg");
            mImages.add("http://www.ile-oleron-vacances.fr/img/appartement-ile-oleron.jpg");
            mImages.add("http://i-cms.journaldesfemmes.com/image_cms/original/511308-15-apparts-de-decorateurs.jpg");
            mImages.add("http://walk2.francenet.fr/indicateur/images/PDF/CMS/articles/12604509.jpg");
            mImages.add("http://www.mapetiteagence.com/img/referencement/vente-appartement-particulier.jpg");*/
        }

        private ImagePagerAdapter(ArrayList<String> mImages) {
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

    /*public void showCustomDialogMessage()
    {
        View custom_view_change_password = getLayoutInflater().inflate(R.layout.custom_dialog_detail_appartement_message, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(custom_view_change_password)
                .setTitle("Envoi d'un message")
                .setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(), "Annulation", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .create();

        dialog.show();
    }*/

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
