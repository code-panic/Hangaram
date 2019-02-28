package com.hangaram.hellgaram.support;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import com.hangaram.hellgaram.R;

public class FontGiver {
    public static Typeface getNanumMyeongjo(Context context) {
        return Typeface.create(Typeface.createFromAsset(context.getAssets(), "fonts/f_nanum_myeongjo.mp3"),Typeface.NORMAL);
    }
}
