package com.hangaram.hellgaram.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.hangaram.hellgaram.cafeteria.CafeFragment;
import com.hangaram.hellgaram.setting.SettingFragment;
import com.hangaram.hellgaram.timetable.CautionActivity;
import com.hangaram.hellgaram.timetable.TimetableFragment;
import com.hangaram.hellgaram.R;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final int REQUEST_CODE_CAUTION = 101;

    private TimetableFragment mTimeTableFragment = new TimetableFragment();
    private SettingFragment mSettingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        /*
         첫화면 지정하기
        * 한가람 시간표에서 호출할 경우에는 시간표 화면으로 넘어가기
        * 그 외에는 급식화면으로 넘어가기
        * */

        if (getIntent().getData() != null && getIntent().getData().getScheme().equals("htc")) {
            //하단바 시간표 아이콘으로 설정하기
            bottomNavigationView.setSelectedItemId(R.id.action_timetable);

            fragmentTransaction.add(R.id.main_frame_layout, mTimeTableFragment).commit();

            Intent intent = new Intent(this, CautionActivity.class);
            startActivityForResult(intent,REQUEST_CODE_CAUTION);
        }
        else
            fragmentTransaction.add(R.id.main_frame_layout, CafeFragment.getInstance()).commit();

        /*bottomNavigationView의 아이템이 선택될 때 리스너 호출*/
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId()){
                    case R.id.action_cafeteria:
                        fragmentTransaction.replace(R.id.main_frame_layout, CafeFragment.getInstance()).commit();
                        break;
                    case R.id.action_timetable:
                        fragmentTransaction.replace(R.id.main_frame_layout,mTimeTableFragment).commit();
                        break;
                    case R.id.action_settings:
                        fragmentTransaction.replace(R.id.main_frame_layout,mSettingFragment).commit();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*경고 액티비티에서 실행 신호를 보냈을 때*/
        if (requestCode == REQUEST_CODE_CAUTION){
            if (resultCode == RESULT_OK){
                try {
                    mTimeTableFragment.linkWithHangaramTimetable(new JSONArray(getIntent().getData().getQueryParameter("data")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
