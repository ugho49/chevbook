package com.chevbook.chevbookapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chevbook.chevbookapp.API.API_annonce;
import com.chevbook.chevbookapp.Activity.DetailsAnnonceActivity;
import com.chevbook.chevbookapp.Adapter.ListViewFavorisAdapter;
import com.chevbook.chevbookapp.Class.Annonce;
import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class FragmentFavoris extends Fragment implements OnRefreshListener {

    @InjectView(R.id.listViewFavoris)
    ListView mListViewFavoris;
    @InjectView(R.id.linearLayoutFavorisLoading)
    LinearLayout mLinearLayoutFavorisLoading;
    @InjectView(R.id.buttonNoResultRafraichirFavoris)
    Button mButtonNoResultRafraichirFavoris;
    @InjectView(R.id.linearLayoutFavorisNoResult)
    LinearLayout mLinearLayoutFavorisNoResult;

    private PullToRefreshLayout mPullToRefreshLayout;
    private ActionBarActivity actionBarActivity;

    private ListViewFavorisAdapter Adapter;
    private Boolean onResume = false;
    private User mUser;

    public static final int CODE_RETOUR = 0;

    private ArrayList<Annonce> mAnnonces = new ArrayList<Annonce>();

    public FragmentFavoris() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favoris, container, false);
        ButterKnife.inject(this, rootView);

        actionBarActivity = (ActionBarActivity) getActivity();
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[4]);

        mUser = new User(getActivity().getApplicationContext());

        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout_list_my_favoris);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        mListViewFavoris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intentDetailAppartement = new Intent(getActivity(), DetailsAnnonceActivity.class);
                intentDetailAppartement.putExtra("annonce", mAnnonces.get(position));
                intentDetailAppartement.putExtra("is_favoris", true);
                startActivityForResult(intentDetailAppartement, CODE_RETOUR);
            }

        });

        Adapter = new ListViewFavorisAdapter(getActivity(), getActivity().getBaseContext(), mAnnonces);
        mListViewFavoris.setAdapter(Adapter);

        mButtonNoResultRafraichirFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnnonces.clear();
                mAnnonces = new ArrayList<Annonce>();

                listerMyFavoris();
            }
        });


        if(onResume == false) {
            listerMyFavoris();
        }
        else {
            onResume = false;
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        onResume = true;
    }

    @Override
    public void onRefreshStarted(View view) {

        mAnnonces.clear();
        mAnnonces = new ArrayList<Annonce>();

        listerMyFavoris();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Vérifie que le résultat est OK
        if(resultCode == 2) {
            mAnnonces.clear();
            mAnnonces = new ArrayList<Annonce>();

            listerMyFavoris();
            //Toast.makeText(getActivity(), "recharger", Toast.LENGTH_SHORT).show();
        }
    }

    public void listerMyFavoris()
    {
        actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
        mLinearLayoutFavorisNoResult.setVisibility(View.GONE);
        mListViewFavoris.setVisibility(View.GONE);
        mLinearLayoutFavorisLoading.setVisibility(View.VISIBLE);

        String[] mesparams = {"lister_mes_favoris"};
        new API_annonce(FragmentFavoris.this).execute(mesparams);
    }

    public void resultListerMyFavoris(Boolean result, ArrayList<Annonce> list){

        mListViewFavoris.setVisibility(View.VISIBLE);
        mLinearLayoutFavorisLoading.setVisibility(View.GONE);
        mLinearLayoutFavorisNoResult.setVisibility(View.GONE);

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
            mListViewFavoris.setVisibility(View.GONE);
            mLinearLayoutFavorisNoResult.setVisibility(View.VISIBLE);
        }

        actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
        mPullToRefreshLayout.setRefreshComplete();
    }
}
