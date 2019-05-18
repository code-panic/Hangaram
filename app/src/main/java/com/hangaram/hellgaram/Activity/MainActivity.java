package com.hangaram.hellgaram.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.hangaram.hellgaram.Fragment.CafeMainFragment;
import com.hangaram.hellgaram.Fragment.SettingFragment;
import com.hangaram.hellgaram.Fragment.TimeTableFragment;
import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.widget.TimetableEachProvider;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //메뉴에 들어갈 Fragment 선언하기
    private CafeMainFragment mCafeteriaFragment = new CafeMainFragment();
    private TimeTableFragment mTimeTableFragment = new TimeTableFragment();
    private SettingFragment mSettingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //필요한 객체 선언하기
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //첫 화면 지정하기
        /* 한가람 시간표에서 호출할 경우에는 시간표 화면으로 넘어가기
           그 외에는 급식화면으로 넘어가기 */
        if (getIntent().getData() != null && getIntent().getData().getScheme().equals("htc"))
            fragmentTransaction.replace(R.id.frame_layout, mTimeTableFragment).commitAllowingStateLoss();
        else
            fragmentTransaction.replace(R.id.frame_layout, mCafeteriaFragment).commitAllowingStateLoss();

        //bottomNavigationView의 아이템이 선택될 때 리스너 호출
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId()){
                    case R.id.action_cafeteria:
                        fragmentTransaction.replace(R.id.frame_layout, mCafeteriaFragment).commitAllowingStateLoss();
                        break;
                    case R.id.action_timetable:
                        fragmentTransaction.replace(R.id.frame_layout,mTimeTableFragment).commitAllowingStateLoss();
                        break;
                    case R.id.action_settings:
                        fragmentTransaction.replace(R.id.frame_layout,mSettingFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });

        //위젯 알람 매니저 및 인텐트 설정하기
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent mIntent = new Intent(this, TimetableEachProvider.class);

        //위젯 업데이트 시간 설정
        int[][] mWidgetUpdateTimeArray = {
                {0, 0},
                {9, 15},
                {10, 40},
                {12, 5},
                {14, 25},
                {15, 50},
        };

        //위젯 알람 설정하기
        Calendar mCalendar = Calendar.getInstance();

        for (int period = 0; period < 6; period++) {
            mIntent.putExtra("period", period + 1);

            PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, 0);

            mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE),
                    mWidgetUpdateTimeArray[period][0], mWidgetUpdateTimeArray[period][1], 0);
            mAlarmManager.set(AlarmManager.RTC, mCalendar.getTimeInMillis(), mPendingIntent);
        }
    }
}
