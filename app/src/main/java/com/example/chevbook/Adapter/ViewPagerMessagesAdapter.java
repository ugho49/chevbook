package com.example.chevbook.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.chevbook.Fragments.FragmentMessagesTabs.FragmentMessagesReceived;
import com.example.chevbook.Fragments.FragmentMessagesTabs.FragmentMessagesSent;

/**
 * Created by Ugho on 24/02/14.
 */
public class ViewPagerMessagesAdapter extends FragmentPagerAdapter {

    // Declare the titles of ViewPager pages
    private String[] mTabsTitles;

    public ViewPagerMessagesAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTabsTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new FragmentMessagesReceived();

            case 1:
                return new FragmentMessagesSent();

        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return mTabsTitles[position];
    }

    @Override
    public int getCount() {
        return mTabsTitles.length;
    }
}
