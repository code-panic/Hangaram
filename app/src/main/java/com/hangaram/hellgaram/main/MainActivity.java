package com.hangaram.hellgaram.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import com.hangaram.hellgaram.CustomView.TitleBar;
import com.hangaram.hellgaram.R;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ContentsPagerAdapter contentsPagerAdapter;

    private TitleBar titleBar;
    private TabLayout tabLayout;

//    Typeface typeface = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/DXRMbxB-KSCpc-EUC-H.mp3");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        titleBar = findViewById(R.id.titlebar);
        tabLayout = findViewById(R.id.bottomtablayout);

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
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        timeTableFragement.saveTimeTableData();
//    }
}
