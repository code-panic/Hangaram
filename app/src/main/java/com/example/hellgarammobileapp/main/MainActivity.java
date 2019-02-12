package com.example.hellgarammobileapp.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hellgarammobileapp.R;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ContentsPagerAdapter contentsPagerAdapter;
    private TabLayout tabLayout;

//    Typeface typeface = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/DXRMbxB-KSCpc-EUC-H.mp3");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.bottomtablayout);

        tabLayout.addTab(tabLayout.newTab().setText("급식"));
        tabLayout.addTab(tabLayout.newTab().setText("시간표"));
        tabLayout.addTab(tabLayout.newTab().setText("버스"));
        tabLayout.addTab(tabLayout.newTab().setText("날씨"));

        viewPager = findViewById(R.id.viewpager);
        contentsPagerAdapter = new ContentsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(contentsPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
