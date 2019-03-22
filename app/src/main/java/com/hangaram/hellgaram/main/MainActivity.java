package com.hangaram.hellgaram.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hangaram.hellgaram.CustomView.TitleBar;
import com.hangaram.hellgaram.R;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ContentsPagerAdapter contentsPagerAdapter;

    private TitleBar titleBar;
    private TabLayout tabLayout;

    private TimeTableGeneratorItem[][] items = new TimeTableGeneratorItem[5][6];

//    Typeface typeface = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/DXRMbxB-KSCpc-EUC-H.mp3");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

//        if (getIntent().getData().getScheme().equals("htc")) {
//            try {
//                JSONArray timetable = new JSONArray(getIntent().getData().getQueryParameter("data"));
//
//                for (int i = 0; i < 5; i++) {
//                    for (int j = 0; j < 6; j++) {
//                        JSONObject jsonObject = timetable.getJSONArray(i).getJSONObject(j);
//                        items[i][j].setSubjectWithClassIdentifier(jsonObject.get("subjectWithClassIdentifier").toString());
//                        items[i][j].setTeacher(jsonObject.get("teacher").toString());
//                        items[i][j].setRoom(jsonObject.get("room").toString());
//                    }
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
