package com.hangaram.hellgaram.Fragment.TimeTableFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hangaram.hellgaram.R;


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

        editText = findViewById(R.id.edittext);
    }
}
