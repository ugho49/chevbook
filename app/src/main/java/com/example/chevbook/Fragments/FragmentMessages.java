package com.example.chevbook.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chevbook.Adapter.ViewPageMessagesAdapter;
import com.example.chevbook.R;

import java.lang.reflect.Field;


public class FragmentMessages extends Fragment {

    private String[] mTabsTitles;

    public static Fragment newInstance(Context context) {
        FragmentMessages f = new FragmentMessages();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_messages, null);
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[3]);

        mTabsTitles = getResources().getStringArray(R.array.fragment_message_tab_array);
        ViewPager mViewPager = (ViewPager) root.findViewById(R.id.viewPagerFragmentMessages);
        PagerTabStrip mPagerTabStrip =(PagerTabStrip) root.findViewById(R.id.pagerTabStripFragmentMessages);
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.green_chevbook));

        mViewPager.setAdapter(new ViewPageMessagesAdapter(getChildFragmentManager(), mTabsTitles));


        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}