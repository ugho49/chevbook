package com.chevbook.chevbookapp.Fragments.FragmentAboutTabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chevbook.chevbookapp.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class FragmentAboutHelp extends Fragment {


    public FragmentAboutHelp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_help, container, false);
    }


}
