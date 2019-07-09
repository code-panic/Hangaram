package com.hangaram.hellgaram.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hangaram.hellgaram.R;

public class Timetable2Activity extends Activity {
    private static final String TAG = "TimeTable2Activity";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_TIMETABLE2";

    private int appWidgetId;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private LinearLayout backgroundWidget;

    private TextView thisSubjectPeriod;
    private TextView thisSubjectName;
    private TextView thisSubjectHint;
    private TextView nextSubjectName;

    private RadioGroup radioGroup;

    private TextView transparentPercent;
    private SeekBar transparentSeekBar;

    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_timetable2);

        /*뷰 객체 가져오기*/
        getViewObjects();

        /*뷰 리스너 설정하기*/
        setViewListeners();

        /*위젯 아이디 가져오기*/
        appWidgetId = getWidgetId();

        /*임시저장할 객체 생성하기*/
        setSharedPreferences();
    }

    /*뷰 객체 초기화하기*/
    private void getViewObjects() {
        backgroundWidget = findViewById(R.id.background_timetable2);

        thisSubjectPeriod = findViewById(R.id.this_subject_period);
        thisSubjectName = findViewById(R.id.this_subject_name);
        thisSubjectHint = findViewById(R.id.this_subject_hint);
        nextSubjectName = findViewById(R.id.next_subject_name);

        radioGroup = findViewById(R.id.radio_group);

        transparentPercent = findViewById(R.id.transparent_percent_text);
        transparentSeekBar = findViewById(R.id.transparent_seek_bar);

        applyButton = findViewById(R.id.apply_button);
    }

    /*위젯 아이디 가져오기*/
    private int getWidgetId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            return extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        return 0;
    }

    /*임시저장을 위한 pref 초기화하기*/
    private void setSharedPreferences() {
        pref = getSharedPreferences("widget" + appWidgetId, 0);
        editor = pref.edit();
    }

    /*리스너 초기화하기*/
    private void setViewListeners() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.white_radio_button)
                    /* 하얀색 샘플 배경화면으로 바꾸기*/
                    changeWidgetTextColor(Color.WHITE, Color.BLACK);
                else
                    /* 검은색 샘플 배경화면으로 바꾸기*/
                    changeWidgetTextColor(Color.BLACK, Color.WHITE);
            }
        });

        transparentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int percent, boolean b) {
                /*투명도 저장하기
                 * 100% -> 0
                 * 0% -> 255*/
                editor.putInt("transparent", 255 - (int) (percent * 2.55)).apply();

                /*샘플 화면 바꾸기*/
                backgroundWidget.getBackground().setAlpha(pref.getInt("transparent", 255));
                transparentPercent.setText(percent + "%");

                Log.d(TAG, appWidgetId + "투명도: " + pref.getInt("transparent", 255));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*창 닫기*/
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*위젯 업데이트 신호를 보낸다.*/
                Intent intent = new Intent(getBaseContext(), Timetable2Provider.class);
                intent.setAction(ACTION_UPDATE);
                sendBroadcast(intent);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }

    /*샘플 위젯 색깔 바꾸*/
    void changeWidgetTextColor(int backgroundColor, int textColor) {
        backgroundWidget.setBackgroundColor(backgroundColor);
        backgroundWidget.getBackground().setAlpha(pref.getInt("transparent", 255));

        thisSubjectPeriod.setTextColor(textColor);
        thisSubjectName.setTextColor(textColor);
        thisSubjectHint.setTextColor(textColor);
        nextSubjectName.setTextColor(textColor);

        /*백그라운드 컬러 저장하기*/
        editor.putInt("backgroundColor", backgroundColor).apply();
        editor.putInt("textColor", backgroundColor).apply();
    }
}