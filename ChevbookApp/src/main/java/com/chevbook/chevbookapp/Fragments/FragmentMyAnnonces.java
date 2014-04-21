package com.chevbook.chevbookapp.Fragments;


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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.chevbook.chevbookapp.API.API_annonce;
import com.chevbook.chevbookapp.Activity.DeposerModifierAnnonceActivity;
import com.chevbook.chevbookapp.Activity.DetailsAnnonceActivity;
import com.chevbook.chevbookapp.Adapter.ListViewMyAnnoncesAdapter;
import com.chevbook.chevbookapp.Class.Annonce;
import com.chevbook.chevbookapp.Class.ConnectionDetector;
import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


public class FragmentMyAnnonces extends Fragment implements OnRefreshListener {


    @InjectView(R.id.listViewMyAnnounces)
    ListView mListViewMyAnnounces;
    @InjectView(R.id.linearLayoutMyAnnoncesLoading)
    LinearLayout mLinearLayoutMyAnnoncesLoading;
    @InjectView(R.id.buttonNoResultRafraichirMyAnnonces)
    Button mButtonNoResultRafraichirMyAnnonces;
    @InjectView(R.id.linearLayoutMyAnnoncesNoResult)
    LinearLayout mLinearLayoutMyAnnoncesNoResult;

    private PullToRefreshLayout mPullToRefreshLayout;

    private ListViewMyAnnoncesAdapter Adapter;

    private ConnectionDetector connectionDetector;

    public static final int CODE_RETOUR = 0;

    private static final int CONST_CREATE = 0;
    private static final int CONST_MODIFIER = 1;
    private Boolean onResume = false;

    private ActionBarActivity actionBarActivity;
    private User mUser;

    private ArrayList<Annonce> mAnnonces = new ArrayList<Annonce>();

    public FragmentMyAnnonces() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_annonces, container, false);
        ButterKnife.inject(this, rootView);
        actionBarActivity = (ActionBarActivity) getActivity();
        connectionDetector = new ConnectionDetector(getActivity().getApplicationContext());

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[3]);

        mUser = new User(getActivity().getApplicationContext());

        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout_list_my_appartements);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        mListViewMyAnnounces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intentDetailAppartement = new Intent(getActivity(), DetailsAnnonceActivity.class);
                intentDetailAppartement.putExtra("annonce", mAnnonces.get(position));
                startActivity(intentDetailAppartement);
                //startActivityForResult(intentDetailAppartement, CODE_RETOUR);
            }

        });

        Adapter = new ListViewMyAnnoncesAdapter(getActivity(), getActivity().getBaseContext(), mAnnonces, this);
        mListViewMyAnnounces.setAdapter(Adapter);

        if(onResume == false) {
            listerMyAnnonces();
        }
        else {
            onResume = false;
        }

        mButtonNoResultRafraichirMyAnnonces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnnonces.clear();
                mAnnonces = new ArrayList<Annonce>();

                listerMyAnnonces();
            }
        });

        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.my_announces, menu);
    }

    @Override
    public void onPause() {
        super.onPause();

        onResume = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_announce_menu_new_announce:
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == 20) { //create annonce
            mAnnonces.clear();
            mAnnonces = new ArrayList<Annonce>();

            listerMyAnnonces();
            //Toast.makeText(getActivity(), "recharger", Toast.LENGTH_SHORT).show();
        }

        if(resultCode == 40) { //update annonce
            mAnnonces.clear();
            mAnnonces = new ArrayList<Annonce>();

            listerMyAnnonces();
            //Toast.makeText(getActivity(), "recharger", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefreshStarted(View view) {
        if(connectionDetector.isConnectingToInternet()){
            mAnnonces.clear();
            mAnnonces = new ArrayList<Annonce>();

            listerMyAnnonces();
        }
        else {
            Toast.makeText(getActivity(), "Aucune connexion internet", Toast.LENGTH_SHORT).show();
            mPullToRefreshLayout.setRefreshComplete();
        }
    }

    public void listerMyAnnonces()
    {
        actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
        mLinearLayoutMyAnnoncesNoResult.setVisibility(View.GONE);
        mListViewMyAnnounces.setVisibility(View.GONE);
        mLinearLayoutMyAnnoncesLoading.setVisibility(View.VISIBLE);

        String[] mesparams = {"lister_mes_annonce"};
        new API_annonce(FragmentMyAnnonces.this).execute(mesparams);
    }

    public void resultListerMyAnnonce(Boolean result, ArrayList<Annonce> list){

        mListViewMyAnnounces.setVisibility(View.VISIBLE);
        mLinearLayoutMyAnnoncesLoading.setVisibility(View.GONE);
        mLinearLayoutMyAnnoncesNoResult.setVisibility(View.GONE);

        if (result)
        {
            //Toast.makeText(getActivity(), "reussite", Toast.LENGTH_SHORT).show();
            for(Annonce a : list){
                mAnnonces.add(a);
            }
            Adapter.setList(mAnnonces);
            Adapter.notifyDataSetChanged();
        }
        else {
            //Toast.makeText(getActivity(), "erreur", Toast.LENGTH_SHORT).show();
            mListViewMyAnnounces.setVisibility(View.GONE);
            mLinearLayoutMyAnnoncesNoResult.setVisibility(View.VISIBLE);
        }

        actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
        mPullToRefreshLayout.setRefreshComplete();
    }
}
