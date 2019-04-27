package com.hangaram.hellgaram.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.hangaram.hellgaram.R;

public class CautionActivity extends AppCompatActivity {

    Button cancelButton;
    Button downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_caution);

        cancelButton = findViewById(R.id.cancelButton);
        downloadButton = findViewById(R.id.downloadButton);

        cancelButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent();
                    intent.putExtra("result",false);
                    setResult(RESULT_OK,intent);
                    finish();
                    Log.d("test","jdklajdklfajlajfdlkafj");
                }
                return true;
            }
        });

        downloadButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent();
                    intent.putExtra("result",true);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                return true;
            }
        });
    }

    //액티비티 꺼질 때 애니메이션 효과 없애기
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
}
