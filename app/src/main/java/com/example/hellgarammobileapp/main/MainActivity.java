package com.example.hellgarammobileapp.main;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hellgarammobileapp.R;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ContentsPagerAdapter contentsPagerAdapter;

//    Typeface typeface = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/DXRMbxB-KSCpc-EUC-H.mp3");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager);
        contentsPagerAdapter = new ContentsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(contentsPagerAdapter);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        timeTableFragement.saveTimeTableData();
//    }
}
