package com.project.admin.present_story.Model;

import android.support.v4.app.Fragment;

public class FragmentModel {
    private int iconResId;
    private String title;
    private Fragment fragment;

    public FragmentModel(int iconResId, String title, Fragment fragment) {
        this.iconResId = iconResId;
        this.title = title;
        this.fragment = fragment;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
