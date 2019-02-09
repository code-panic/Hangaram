package com.example.hellgarammobileapp.Fragment.LessonFragment;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hellgarammobileapp.Fragment.LessonFragment.LesDBAccess;
import com.example.hellgarammobileapp.R;

public class LessonActivity extends LinearLayout {
    private static final String LessonActivityLog = "LessonActivityLog";

    TextView appName;
    TextView lessonTextView;
    TextView hintTextVIew;

    public LessonActivity(Context context) {
        super(context);
        init(context);
    }

    public LessonActivity(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_lesson, this, true);

        appName = findViewById(R.id.appName);
        lessonTextView = findViewById(R.id.lessonText);
        hintTextVIew = findViewById(R.id.hintText);

        appName.setText("Hellgaram");

        LesDBAccess lesDBAccess = LesDBAccess.getInstance(context);
        lesDBAccess.open();
        Cursor cursor = lesDBAccess.getTodayQuote();
        lesDBAccess.close();

        Log.d(LessonActivityLog, "cursor:" + cursor.getCount());

        if(cursor.getCount()>0) {
            int lessonColumnIndex = cursor.getColumnIndex("quote");
            int hintColumnIndex = cursor.getColumnIndex("hint");

            cursor.moveToNext();

            lessonTextView.setText(cursor.getString(lessonColumnIndex));
            hintTextVIew.setText(cursor.getString(hintColumnIndex));

            Log.d(LessonActivityLog,cursor.getString(lessonColumnIndex));
            Log.d(LessonActivityLog,cursor.getString(hintColumnIndex));
        }

        cursor.close();

    }

}
