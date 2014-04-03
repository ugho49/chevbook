package com.chevbook.chevbookapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chevbook.chevbookapp.Fragments.FragmentAboutTabs.FragmentAboutContactUs;
import com.chevbook.chevbookapp.Fragments.FragmentAboutTabs.FragmentAboutHelp;
import com.chevbook.chevbookapp.Fragments.FragmentAboutTabs.FragmentAboutLicences;


/**
 * Created by Ugho on 03/03/14.
 */
public class ViewPagerAboutAdapter extends FragmentPagerAdapter {

    // Declare the titles of ViewPager pages
    private String[] mTabsTitles;

    public ViewPagerAboutAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTabsTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new FragmentAboutContactUs();

            case 1:
                return new FragmentAboutLicences();

            case 2:
                return new FragmentAboutHelp();

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
