package com.hangaram.hellgaram.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hangaram.hellgaram.CustomView.TitleBar;
import com.hangaram.hellgaram.Fragment.TimeTableFragment.TimeTableFragment;
import com.hangaram.hellgaram.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ContentsPagerAdapter contentsPagerAdapter;

    private TitleBar titleBar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleBar = findViewById(R.id.titlebar);
        tabLayout = findViewById(R.id.bottomtablayout);

        //아래 탭의 그림 설정
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tabicon_meal));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tabicon_timetable));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tabicon_bus));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tabicon_weather));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tabicon_setting));

        viewPager = findViewById(R.id.viewpager);
        contentsPagerAdapter = new ContentsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(contentsPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(titleBar);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });

        if (getIntent().getData() != null && getIntent().getData().getScheme().equals("htc")){
            viewPager.setCurrentItem(1);
        }
    }
}
