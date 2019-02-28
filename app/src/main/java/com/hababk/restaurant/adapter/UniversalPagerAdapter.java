package com.hababk.appstore.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A general {@link FragmentStatePagerAdapter} with basic functions for adding and removing fragments
 */
public class UniversalPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentsList = new ArrayList<>();
    private final List<String> fragmentsTitleList = new ArrayList<>();
    private int nonPresentTitleNum = 0;
    private boolean showTitle = true;

    public UniversalPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    /**
     * This function takes fragment as an argument and adds that to this adapter along with the title provided
     *
     * @param fragment A {@link Fragment} to be added
     * @param title    A {@link String} used as a title
     */
    public void addFrag(@NonNull Fragment fragment, @NonNull String title) {
        fragmentsList.add(fragment);
        fragmentsTitleList.add(title);
    }

    /**
     * This function that removes the fragment from this adapter at the position provided.
     */
    public void removeFrag(int pos) {
        fragmentsList.remove(pos);
        fragmentsTitleList.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return showTitle ? fragmentsTitleList.get(position) : null;
    }
}
