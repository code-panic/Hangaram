package com.example.hellgarammobileapp.support;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;

import com.example.hellgarammobileapp.R;

public class FontGiver {
    private static Typeface nanumMyeongjo;
    private static Typeface nanumSquareRound;

    public static Typeface getNanumMyeongjo(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nanumMyeongjo = context.getResources().getFont(R.font.f_nanum_myeongjo);
        }
        return nanumMyeongjo;
    }

    public static Typeface getNanumSquareRound(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nanumSquareRound = context.getResources().getFont(R.font.f_nanum_square_round);
        }
        return nanumSquareRound;
    }
}
