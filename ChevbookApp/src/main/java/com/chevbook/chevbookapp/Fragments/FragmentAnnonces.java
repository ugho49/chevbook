package com.chevbook.chevbookapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.chevbook.chevbookapp.API.API_annonce;
import com.chevbook.chevbookapp.Activity.DeposerModifierAnnonceActivity;
import com.chevbook.chevbookapp.Activity.DetailsAnnonceActivity;
import com.chevbook.chevbookapp.Adapter.ListViewAnnonceAdapter;
import com.chevbook.chevbookapp.Class.Annonce;
import com.chevbook.chevbookapp.Class.Modele;
import com.chevbook.chevbookapp.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class FragmentAnnonces extends Fragment implements OnRefreshListener, AbsListView.OnScrollListener{

    @InjectView(R.id.editTextSearch)
    EditText mEditTextSearch;
    @InjectView(R.id.imageViewSearchMoreDetail)
    ImageView mImageViewSearchMoreDetail;
    @InjectView(R.id.imageViewSearch)
    ImageView mImageViewSearch;
    @InjectView(R.id.listViewSearch)
    ListView mListViewSearch;
    @InjectView(R.id.linearLayoutSearchAppartementLoading)
    LinearLayout mLinearLayoutSearchAppartementLoading;
    @InjectView(R.id.linearLayoutSearchAppartementNoResult)
    LinearLayout mLinearLayoutSearchAppartementNoResult;
    @InjectView(R.id.buttonNoResultRafraichirAnnonce)
    Button mButtonNoResultRafraichirAnnonce;

    public static final int CODE_RETOUR = 0;

    private int AnnonceMax = 0;
    private int AnnonceChargees = 0;

    private int AnnonceDebut = 0;
    private int AnnonceFin = 10;

    private Boolean onResume = false;

    //Custom Dialog
    private static EditText EditTextKeyWord;
    private static EditText EditTextPrixMin;
    private static EditText EditTextPrixMax;
    private static EditText mEditTextPieceMin;
    private static EditText mEditTextPieceMax;
    private static Spinner mSpinnerQuartier;
    private static Spinner mSpinnerType;
    private static CheckBox mCheckBoxMaison;
    private static CheckBox mCheckBoxAppartements;
    private static CheckBox mCheckBoxTerrain;
    private static CheckBox mCheckBoxParking;
    private static Button mButtonSearch;

    private static String [] AppartementListQuartier;
    private static String [] AppartementListType;

    private Modele vmodele;
    private ListViewAnnonceAdapter Adapter;

    private ArrayList<Annonce> mAnnonces = new ArrayList<Annonce>();

    private PullToRefreshLayout mPullToRefreshLayout;
    private static LayoutInflater mInflater;
    private ActionBarActivity actionBarActivity;
    private boolean flag_loading = false;

    private static final int CONST_CREATE = 0;
    private static final int CONST_MODIFIER = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        vmodele = new Modele();
    }

    @Override
    public void onPause() {
        super.onPause();

        onResume = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_annonce, null);
        ButterKnife.inject(this, root);
        mInflater = inflater;
        actionBarActivity = (ActionBarActivity) getActivity();
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) root.findViewById(R.id.ptr_layout_list_appartements);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        //ActionBar
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[1]);

        //click Button
        mImageViewSearch.setOnClickListener(clickListener);
        mImageViewSearchMoreDetail.setOnClickListener(clickListener);
        mButtonNoResultRafraichirAnnonce.setOnClickListener(clickListener);


        mListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intentDetailAppartement = new Intent(getActivity(), DetailsAnnonceActivity.class);
                intentDetailAppartement.putExtra("annonce", mAnnonces.get(position));
                startActivity(intentDetailAppartement);
                //startActivityForResult(intentDetailAppartement, CODE_RETOUR);
            }

        });

        mListViewSearch.setOnScrollListener(FragmentAnnonces.this);

        Adapter = new ListViewAnnonceAdapter(getActivity().getBaseContext(), mAnnonces);
        mListViewSearch.setAdapter(Adapter);

        if(onResume == false) {
            listerAnnonces(AnnonceDebut, AnnonceFin);
        }
        else {
            onResume = false;
        }

        return root;
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.imageViewSearchMoreDetail:
                    //Toast.makeText(getActivity(), "Plus de détails - Custom dialog...", Toast.LENGTH_SHORT).show();
                    custom_dialog_more_detail();
                    break;

                case R.id.imageViewSearch:
                    Toast.makeText(getActivity(), "Recherche lancée...", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.buttonNoResultRafraichirAnnonce:
                    listerAnnonces(AnnonceDebut, AnnonceFin);
                    break;

            }
        }
    };

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.list_appartements, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_appartements_menu_new_announce:
                //Toast.makeText(getActivity(), getString(R.string.action_new_announce), Toast.LENGTH_SHORT).show();
                Intent intentDetailAppartement = new Intent(getActivity(), DeposerModifierAnnonceActivity.class);
                intentDetailAppartement.putExtra("CONST", CONST_CREATE);
                //startActivity(intentDetailAppartement);
                startActivityForResult(intentDetailAppartement, CODE_RETOUR);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        int currentPage = (int) Math.floor(AnnonceChargees / 10) + 1;
        int maxPage = (int) Math.floor(AnnonceMax / 10) + 1;

        if(currentPage != maxPage){
            if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
                if (flag_loading == false){
                    flag_loading = true;

                    //Toast.makeText(getActivity(), "Bas de la listView", Toast.LENGTH_SHORT).show();
                    if(AnnonceMax > AnnonceChargees)
                    {
                        listerAnnonces(AnnonceDebut, AnnonceFin);
                    }
                }
            }
        }
    }

    public void setAnnonceMax(int annonceMax) {
        AnnonceMax = annonceMax;
    }

    private void custom_dialog_more_detail() {
        View custom_view_change_password = mInflater.inflate(R.layout.custom_dialog_search_annonce_more_detail, null);

        AppartementListQuartier = getResources().getStringArray(R.array.appartements_quartier_array);
        AppartementListType = getResources().getStringArray(R.array.appartements_type_location_array);

        mSpinnerQuartier = (Spinner)custom_view_change_password.findViewById(R.id.spinnerCustomDialogSearchAppartDetailQuartier);
        mSpinnerType = (Spinner)custom_view_change_password.findViewById(R.id.spinnerCustomDialogSearchAppartDetailType);

        ArrayAdapter<String> spinnerQuartierArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, AppartementListQuartier);
        ArrayAdapter<String> spinnerTypeArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, AppartementListType);

        mSpinnerQuartier.setAdapter(spinnerQuartierArrayAdapter);
        mSpinnerType.setAdapter(spinnerTypeArrayAdapter);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(custom_view_change_password)
                .setCancelable(false)
                .setTitle(getString(R.string.search_details))
                .setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(), "Annulation", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .create();

        dialog.show();
    }

    @Override
    public void onRefreshStarted(View view) {

        mAnnonces.clear();
        mAnnonces = new ArrayList<Annonce>();
        flag_loading = false;

        if(AnnonceChargees == 0)
        {
            listerAnnonces(AnnonceDebut, AnnonceFin);
        }
        else {

            int a = AnnonceChargees;
            AnnonceChargees = 0;
            listerAnnonces(0, a);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == 20) { //create annonce
            mAnnonces.clear();
            mAnnonces = new ArrayList<Annonce>();
            flag_loading = false;

            if(AnnonceChargees == 0)
            {
                listerAnnonces(AnnonceDebut, AnnonceFin);
            }
            else {

                int a = AnnonceChargees;
                AnnonceChargees = 0;
                listerAnnonces(0, a);
            }
        }
    }

    public void listerAnnonces(int debut, int fin)
    {
        actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
        mLinearLayoutSearchAppartementNoResult.setVisibility(View.GONE);

        if(AnnonceChargees == 0)
        {
            mListViewSearch.setVisibility(View.GONE);
            mLinearLayoutSearchAppartementLoading.setVisibility(View.VISIBLE);
        }

        String[] mesparams = {"lister_annonce", Integer.toString(debut), Integer.toString(fin)};
        new API_annonce(FragmentAnnonces.this).execute(mesparams);
    }

    public void resultListerAnnonce(Boolean result, ArrayList<Annonce> list, int debut, int annonceChargeesInThisTask){

        mListViewSearch.setVisibility(View.VISIBLE);
        mLinearLayoutSearchAppartementLoading.setVisibility(View.GONE);
        mLinearLayoutSearchAppartementNoResult.setVisibility(View.GONE);

        if (result)
        {
            for(Annonce a : list){
                mAnnonces.add(a);
            }

            Adapter.setList(mAnnonces);
            Adapter.notifyDataSetChanged();

            if(debut != 0) {
                mListViewSearch.smoothScrollToPosition(AnnonceChargees);
            }

            AnnonceChargees = AnnonceChargees + annonceChargeesInThisTask;
            int nbAnnoncesNonChargeesRestantes = AnnonceMax - AnnonceChargees;

            if(nbAnnoncesNonChargeesRestantes >= 10)
            {
                AnnonceDebut = AnnonceFin + 1;
                AnnonceFin = 10;
            }
            else {
                AnnonceDebut = AnnonceFin + 1;
                AnnonceFin = nbAnnoncesNonChargeesRestantes;
            }
        }
        else {

            mListViewSearch.setVisibility(View.GONE);
            mLinearLayoutSearchAppartementNoResult.setVisibility(View.VISIBLE);
        }

        actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
        mPullToRefreshLayout.setRefreshComplete();
        flag_loading = false;
    }
}