package com.chevbook.chevbookapp.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.chevbook.chevbookapp.Adapter.ViewPagerAboutAdapter;
import com.chevbook.chevbookapp.R;


public class FragmentAbout extends Fragment {

    private String[] mTabsTitles;

    public FragmentAbout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[6]);

        mTabsTitles = getResources().getStringArray(R.array.fragment_about_tab_array);

        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.viewPagerFragmentAbout);
        mViewPager.setAdapter(new ViewPagerAboutAdapter(getChildFragmentManager(), mTabsTitles));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.pagerTabStripFragmentAbout);
        tabs.setIndicatorColor(getResources().getColor(R.color.green_chevbook));
        tabs.setTextColor(Color.WHITE);
        tabs.setDividerColor(Color.WHITE);
        tabs.setDrawingCacheEnabled(true);
        tabs.setShouldExpand(true);
        tabs.setViewPager(mViewPager);

        return rootView;
    }


}
