package com.project.admin.present_story.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.project.admin.present_story.Adapter.MainViewPagerAdapter;
import com.project.admin.present_story.Fragment.AdditionFragment;
import com.project.admin.present_story.Fragment.CalendarFragment;
import com.project.admin.present_story.R;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    String targetStory;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        targetStory = getPref("targetStory");

        toolbar = findViewById(R.id.mainactivity_toolbar);

        setToolbar();
        setTabs();
        setFab();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); //액션바에 표시되는 제목의 표시유무를 설정
        actionBar.setDisplayShowHomeEnabled(false); // 액션바 왼쪽의 <- 버튼

    }

    private void setTabs() {
        final int[] icons = {R.drawable.calendar_text, R.drawable.view_grid};
        final String[] titles = {"캘린더", "더보기"};
//        final String[] titles = {"리스트","집안일", "외출", "캘린더", "더보기"};
        final Fragment[] fragments = {new CalendarFragment(), new AdditionFragment()};

        TabLayout tabs = findViewById(R.id.mainActivity_tabLayout);
        viewPager = findViewById(R.id.mainActivity_viewpager);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }); // 탭 눌렀을때 해당 위치로 이동
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs)); // 뷰페이저 이동했을때 해당 위치로 이동
        // 어댑터
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < fragments.length; i++) {
            mainViewPagerAdapter.addFragment(icons[i], titles[i], fragments[i]);
        }
        viewPager.setAdapter(mainViewPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
                tabs.getTabAt(i).setIcon(mainViewPagerAdapter.getFragmentModel(i).getIconResId());
        }

        viewPager.setCurrentItem(0);

    }
    private void setFab() {
        FloatingActionButton fab = findViewById(R.id.mainactivity_floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.from_right, R.anim.to_left);
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent, activityOptions.toBundle());
            }
        });
    }

    String getPref(String key) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public void hideToolbar(boolean isHide) {
        if(!isHide) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }
    }
}
