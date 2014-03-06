package com.example.chevbook.Fragments;


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
import android.widget.ListView;

import com.example.chevbook.Activity.DeposerModifierAnnonceActivity;
import com.example.chevbook.Activity.DetailsAnnonceActivity;
import com.example.chevbook.Adapter.ListViewMyAnnoncesAdapter;
import com.example.chevbook.Class.Annonce;
import com.example.chevbook.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FragmentMyAnnonces extends Fragment {


    @InjectView(R.id.listViewMyAnnounces)
    ListView mListViewMyAnnounces;

    private ListViewMyAnnoncesAdapter Adapter;

    private static final int CONST_CREATE = 0;
    private static final int CONST_MODIFIER = 1;

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

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[4]);

        mAnnonces.add(new Annonce());
        mAnnonces.add(new Annonce());

        Adapter = new ListViewMyAnnoncesAdapter(getActivity(), getActivity().getBaseContext(), mAnnonces);
        mListViewMyAnnounces.setAdapter(Adapter);

        mListViewMyAnnounces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intentDetailAppartement = new Intent(getActivity(), DetailsAnnonceActivity.class);
                intentDetailAppartement.putExtra("annonce", mAnnonces.get(position));
                startActivity(intentDetailAppartement);
            }

        });

        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.my_announces, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_announce_menu_new_announce:
                //Toast.makeText(getActivity(), getString(R.string.action_new_announce), Toast.LENGTH_SHORT).show();
                Intent intentDetailAppartement = new Intent(getActivity(), DeposerModifierAnnonceActivity.class);
                intentDetailAppartement.putExtra("CONST", CONST_CREATE);
                startActivity(intentDetailAppartement);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
