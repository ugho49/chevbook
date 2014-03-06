package com.example.chevbook.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chevbook.Activity.DetailsAnnonceActivity;
import com.example.chevbook.Adapter.ListViewFavorisAdapter;
import com.example.chevbook.Class.Annonce;
import com.example.chevbook.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class FragmentFavoris extends Fragment {

    @InjectView(R.id.listViewFavoris)
    ListView mListViewFavoris;

    private ListViewFavorisAdapter Adapter;

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

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[5]);

        mAnnonces.add(new Annonce());
        mAnnonces.add(new Annonce());
        mAnnonces.add(new Annonce());

        Adapter = new ListViewFavorisAdapter(getActivity(), getActivity().getBaseContext(), mAnnonces);
        mListViewFavoris.setAdapter(Adapter);

        mListViewFavoris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intentDetailAppartement = new Intent(getActivity(), DetailsAnnonceActivity.class);
                intentDetailAppartement.putExtra("annonce", mAnnonces.get(position));
                startActivity(intentDetailAppartement);
            }

        });

        return rootView;
    }


}
