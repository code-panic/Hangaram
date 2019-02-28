package com.hangaram.hellgaram.support;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ConvertUnit {
    public static int convertSpToPixel(int sp, Context context) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static float convertPixelToSp(int px, Context context){
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int convertDpToPixel(int dp, Context context){
        return (int)(dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float convertPixelsToDp(int px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
