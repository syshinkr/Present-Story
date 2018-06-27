package com.project.admin.present_story.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.project.admin.present_story.Model.FragmentModel;

import java.util.ArrayList;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<FragmentModel> fragmentModels = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(int iconResId, String title, Fragment fragment) {
        FragmentModel fragmentModel = new FragmentModel(iconResId, title, fragment);
        fragmentModels.add(fragmentModel);
    }

    public FragmentModel getFragmentModel(int position) {
        return fragmentModels.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentModels.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentModels.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragmentModels.size();
    }
}
