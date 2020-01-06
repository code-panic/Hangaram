package com.hangaram.hellgaram.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.hangaram.hellgaram.R;

public class StationActivity extends Activity {
    private static final String TAG = "StationActivity";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_BUS";

    private int appWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* xml 파일 로딩 */
        setContentView(R.layout.widget_station_configure);

        /* 뷰 객체 선언 및 초기화 */
        final LinearLayout backgroundLinearLayout = findViewById(R.id.backgroundLinearLayout);

        final TextView stationNameTextView = findViewById(R.id.stationNameTextView);
        final TextView busNameTextView = findViewById(R.id.busNameTextView);
        final TextView arriveInfoTextView = findViewById(R.id.arriveInfoTextView);
        final TextView updateTimeTextView = findViewById(R.id.updateTimeTextView);

        final Spinner stationSpinner = findViewById(R.id.StationSpinner);
        final Spinner busSpinner = findViewById(R.id.BusSpinner);

        final RadioGroup colorRadioGroup = findViewById(R.id.colorRadioGroup);

        final SeekBar transparentSeekBar = findViewById(R.id.transparentSeekBar);
        final TextView transparentValueTextView = findViewById(R.id.transparentValueTextView);

        final Button applyButton = findViewById(R.id.applyButton);

        /* 위젯 아이디 가져오기 */
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
                appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        final SharedPreferences storage = getSharedPreferences("widget" + appWidgetId, 0);

        /* 스피너 리스트 설정 */
        ArrayAdapter stationAdapter = ArrayAdapter.createFromResource(this, R.array.station_array, android.R.layout.simple_spinner_item);
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stationSpinner.setAdapter(stationAdapter);
        stationSpinner.setSelection(0);

        ArrayAdapter busAdapter = ArrayAdapter.createFromResource(this, R.array.bus_array, android.R.layout.simple_spinner_item);
        busAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busSpinner.setAdapter(busAdapter);
        busSpinner.setSelection(0);

        /* 컬러 라디오그룹 리스너 설정 */
        colorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.whiteRadioButton) {
                    backgroundLinearLayout.setBackgroundColor(Color.WHITE);
                    backgroundLinearLayout.getBackground().setAlpha(transparentSeekBar.getProgress());

                    stationNameTextView.setTextColor(Color.BLACK);
                    busNameTextView.setTextColor(Color.BLACK);
                    arriveInfoTextView.setTextColor(Color.BLACK);
                    updateTimeTextView.setTextColor(Color.BLACK);
                } else if (id == R.id.blackRadioButton) {
                    backgroundLinearLayout.setBackgroundColor(Color.BLACK);
                    backgroundLinearLayout.getBackground().setAlpha(transparentSeekBar.getProgress());

                    stationNameTextView.setTextColor(Color.WHITE);
                    busNameTextView.setTextColor(Color.WHITE);
                    arriveInfoTextView.setTextColor(Color.WHITE);
                    updateTimeTextView.setTextColor(Color.WHITE);
                }
            }
        });

        /* 투명도 시크바 리스너 설정 */
        transparentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int percent, boolean b) {
                backgroundLinearLayout.getBackground().setAlpha(percent);
                transparentValueTextView.setText(percent + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /* 위젯 정보 적용하기 */
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* 위젯 정보 저장하기 */
                storage.edit().putString("stationName", stationSpinner.getSelectedItem().toString()).apply();
                storage.edit().putString("busName", busSpinner.getSelectedItem().toString()).apply();
                storage.edit().putInt("backgroundTransparentValue", transparentSeekBar.getProgress()).apply();

                if(colorRadioGroup.getCheckedRadioButtonId() == R.id.whiteRadioButton)
                    storage.edit().putString("backgroundColorValue", "white").apply();
                else if (colorRadioGroup.getCheckedRadioButtonId() == R.id.blackRadioButton)
                    storage.edit().putString("backgroundColorValue", "black").apply();

                /* 위젯 업데이트 신호를 보낸다. */
                Intent intent = new Intent(getBaseContext(), StationProvider.class);
                intent.setAction(ACTION_UPDATE);
                sendBroadcast(intent);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }
}