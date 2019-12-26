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
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.custom.CustomRadiobuttonContainer;
import com.hangaram.hellgaram.custom.CustomSeekbarContainer;

public class Timetable2Activity extends Activity {
    private static final String TAG = "TimeTable2Activity";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_TIMETABLE2";

    private int mAppWidgetId;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private LinearLayout mWidgetBackground;

    private LinearLayout mCustomBox;

    private CustomRadiobuttonContainer mColorContainer;
    private CustomSeekbarContainer mBlurContainer;
    private CustomSeekbarContainer mTransparentContainer;

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        mCustomBox = findViewById(R.id.custom_box);

        mWidgetBackground = findViewById(R.id.background_widget);

        mColorContainer = new CustomRadiobuttonContainer(getBaseContext());
        mBlurContainer = new CustomSeekbarContainer(getBaseContext());
        mTransparentContainer = new CustomSeekbarContainer(getBaseContext());

        mButton = findViewById(R.id.create_button);

        mColorContainer.getRadioGroup().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.white)
                    changeWidgetTextColor(Color.WHITE, Color.BLACK);
                else if (id == R.id.black)
                    changeWidgetTextColor(Color.BLACK, Color.WHITE);
            }
        });

        mBlurContainer.getCustomSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int percent, boolean b) {
                mWidgetBackground.getBackground().setAlpha(percent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.putInt("backgroundColor", ).apply();
                mEditor.putInt("blur", mBlurContainer.getCustomSeekBar().getProgress()).apply();
                mEditor.putInt("transparent", mTransparentContainer.getCustomSeekBar().getProgress()).apply();

                Intent intent = new Intent(getBaseContext(), Timetable2Provider.class);
                intent.setAction(ACTION_UPDATE);
                sendBroadcast(intent);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });

        mCustomBox.addView(mColorContainer);
        mCustomBox.addView(mBlurContainer);
        mCustomBox.addView(mTransparentContainer);

        /*위젯 아이디 가져오기*/
        mAppWidgetId = getWidgetId();

        mPref = getSharedPreferences("widget" + mAppWidgetId, 0);
        mEditor = mPref.edit();
    }

    /*위젯 아이디 가져오기*/
    private int getWidgetId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        return extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

    }
}