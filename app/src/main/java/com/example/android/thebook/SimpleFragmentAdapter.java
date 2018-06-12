package com.example.android.thebook;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by Jianyuan on 9/30/2016.
 */

public class SimpleFragmentAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[]{"Search", "Favorites"};
    private Context context;
    private static final int COUNT = 2;

    /**
     * Create a new {@link SimpleFragmentAdapter} object.
     *
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public SimpleFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new SearchFragment();
        }
        return new FavoritesFragment();
    }

        /**
         * Return the total number of pages.
         */
        @Override
        public int getCount(){
            return COUNT;
        }

        @Override
        public CharSequence getPageTitle ( int position){
            // Generate title based on item position
            return tabTitles[position];
        }
    }
