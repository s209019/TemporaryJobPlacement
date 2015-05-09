package it.polito.mobile.temporaryjobplacement.commons.viewmanaging;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    String titles[];
    int NumbOfTabs;
    List<Fragment> fragmentList;


    public TabsPagerAdapter(FragmentManager fm, String titles[], List<Fragment> fragmentList) {
        super(fm);

        this.titles = titles;
        this.NumbOfTabs = fragmentList.size();
        this.fragmentList=fragmentList;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

       return  fragmentList.get(position);


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}