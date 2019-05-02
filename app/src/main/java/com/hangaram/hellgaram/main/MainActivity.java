package com.hangaram.hellgaram.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import com.hangaram.hellgaram.widget.TimetableEachProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ContentsPagerAdapter contentsPagerAdapter;

    private TitleBar titleBar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //뷰 선언하기
        titleBar = findViewById(R.id.titlebar);
        tabLayout = findViewById(R.id.bottomtablayout);
        viewPager = findViewById(R.id.viewpager);

        //탭의 그림 설정하기
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tabicon_meal));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tabicon_timetable));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tabicon_bus));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tabicon_weather));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tabicon_setting));

        //뷰 페이저 설정하기
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

        //한가람 시간표에서 호출 시 시간표 화면으로 넘어가기
        if (getIntent().getData() != null && getIntent().getData().getScheme().equals("htc"))
            viewPager.setCurrentItem(1);

        //위젯 알람 매니저 및 인텐트 설정하기
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent mIntent = new Intent(this, TimetableEachProvider.class);

        //위젯 업데이트 시간 설정
        Calendar[] mCalendarArray = new Calendar[6];

        int[][] mWidgetUpdateTimeArray = {
                {9, 15},
                {10, 40},
                {12, 5},
                {14, 25},
                {15, 50},
                {17, 10}
        };

        //위젯 알람 설정하기
        for (int period = 0; period < mCalendarArray.length; period++) {
            mCalendarArray[period] = Calendar.getInstance();

            mIntent.putExtra("period", period + 1);

            PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, 0);

            mCalendarArray[period].set(mCalendarArray[period].get(Calendar.YEAR), mCalendarArray[period].get(Calendar.MONTH), mCalendarArray[period].get(Calendar.DATE),
                    mWidgetUpdateTimeArray[period][0], mWidgetUpdateTimeArray[period][1], 0);
            mAlarmManager.set(AlarmManager.RTC, mCalendarArray[period].getTimeInMillis(), mPendingIntent);
        }
    }
}
