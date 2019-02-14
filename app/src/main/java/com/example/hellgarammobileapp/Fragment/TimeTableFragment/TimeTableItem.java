package com.example.hellgarammobileapp.Fragment.TimeTableFragment;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.hellgarammobileapp.R;


public class TimeTableItem extends LinearLayout {
    EditText editText;

    public TimeTableItem(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_timetable, this, true);

//        DisplayMetrics dm = new DisplayMetrics();
//        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = width * mainImage.getHeight() / mainImage.getWidth(); //mainImage is the Bitmap I'm drawing
//        addView(mainImageView,new LinearLayout.LayoutParams(
//                width, height));

        editText = findViewById(R.id.edittext);
    }
}
