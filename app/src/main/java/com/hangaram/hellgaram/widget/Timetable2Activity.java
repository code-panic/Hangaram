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

        /*위젯 아이디 가져오기*/
        getWidgetId();

        /*임시저장할 객체 생성하기*/
        setSharedPreferences();

        /*뷰 리스너 설정하기*/
        setViewListeners();
    }

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

    private void getWidgetId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }

    private void setSharedPreferences() {
        pref = getSharedPreferences("widget" + appWidgetId, 0);
        editor = pref.edit();
    }

    private void setViewListeners() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.white_radio_button) {
                    /*샘플 화면 바꾸기*/
                    backgroundWidget.setBackgroundColor(Color.WHITE);
                    backgroundWidget.getBackground().setAlpha(pref.getInt("transparent", 0));
                    changeWidgetTextBlack();

                    /*백그라운드 컬러 저장하기*/
                    editor.putString("backgroundColor", "white").apply();
                    editor.putString("textColor", "black").apply();
                } else {
                    /*샘플 화면 바꾸기*/
                    backgroundWidget.setBackgroundColor(Color.BLACK);
                    backgroundWidget.getBackground().setAlpha(pref.getInt("transparent", 0));
                    changeWidgetTextWhite();

                    /*백그라운드 컬러 저장하기*/
                    editor.putString("backgroundColor", "black").apply();
                    editor.putString("textColor", "white").apply();
                }
            }
        });

        transparentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int percent, boolean b) {
                /*투명도 저장하기*/
                editor.putInt("transparent", 255 - (int) (percent * 2.55)).apply();

                Log.d(TAG, "투명도: " + pref.getInt("transparent", 0));

                /*샘플 화면 바꾸기*/
                backgroundWidget.getBackground().setAlpha(pref.getInt("transparent", 0));
                transparentPercent.setText(percent + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "widget" + appWidgetId + "activity");

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }

    void changeWidgetTextWhite() {
        thisSubjectPeriod.setTextColor(Color.WHITE);
        thisSubjectName.setTextColor(Color.WHITE);
        thisSubjectHint.setTextColor(Color.WHITE);
        nextSubjectName.setTextColor(Color.WHITE);
    }

    void changeWidgetTextBlack() {
        thisSubjectPeriod.setTextColor(Color.BLACK);
        thisSubjectName.setTextColor(Color.BLACK);
        thisSubjectHint.setTextColor(Color.BLACK);
        nextSubjectName.setTextColor(Color.BLACK);
    }
}