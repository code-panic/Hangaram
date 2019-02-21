package com.example.hellgarammobileapp.Fragment.TimeTableFragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.hellgarammobileapp.Fragment.TimeTableFragment.TimeDBHelper;
import com.example.hellgarammobileapp.Fragment.TimeTableFragment.TimeTableItem;
import com.example.hellgarammobileapp.R;

public class TimeTableFragment extends Fragment {
    private static String log = "TimeTableActivity";
    private final String TABLE_NAME = TimeDBHelper.TABLE_NAME;

    private TimeDBHelper timeDbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    private View view;
    private TableLayout timeTableLayout;
    private View contour;
    private ImageView editButton;

    private int rowCount = 6; //가로줄
    private int columnCount = 5; //세로줄

    private TimeTableItem[][] items = new TimeTableItem[rowCount][columnCount];

    private int width;
    private int height;

    private int strokeWidth = 1;

    private boolean isEditChecked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_timetable, container, false);

        timeTableLayout = view.findViewById(R.id.timeTableLayout);
        contour = view.findViewById(R.id.contour);
        editButton = view.findViewById(R.id.editButton);

        ViewTreeObserver viewTreeObserver = timeTableLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    timeTableLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    width = ((int) (timeTableLayout.getMeasuredWidth() - strokeWidth * (columnCount + 1)) / columnCount) * columnCount + strokeWidth * (columnCount + 1);
                    height = (width - strokeWidth * (columnCount + 1)) / columnCount * rowCount + strokeWidth * (rowCount + 1);
                    Log.d(log, "width: " + width);
                    Log.d(log, "height: " + height);
                    init(getContext());
                }
            }
        });
        return view;
    }

    private void init(final Context context) {
        RelativeLayout.LayoutParams contourLayoutParams = new RelativeLayout.LayoutParams(width, 1);
        contourLayoutParams.addRule(RelativeLayout.BELOW, R.id.weekView);
        contourLayoutParams.topMargin = 0;
        contour.setLayoutParams(contourLayoutParams);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.contour);
        layoutParams.topMargin = 15;
        timeTableLayout.setLayoutParams(layoutParams);

        editButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isEditChecked) {
                        editButton.setImageResource(R.drawable.edit_off);
                        setEditableFalse();
                        saveTimeTableData();
                        Toast.makeText(context, "저장되었습니다", Toast.LENGTH_SHORT).show();
                        isEditChecked = false;
                    } else {
                        editButton.setImageResource(R.drawable.edit_on);
                        setEditableTrue();
                        isEditChecked = true;
                    }
                }
                return true;
            }
        });

        openDataBase(context);

        contentTimeTable();
        Log.d(log, "func contentTimeTable");

        setEditTextLayoutParams();
    }

    private Cursor openDataBase(Context context) {
        timeDbHelper = new TimeDBHelper(context);
        db = timeDbHelper.getReadableDatabase();
        Log.d(log, "set db and dbhelper");

        cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        return cursor;
    }

    private void contentTimeTable() {
        cursor.moveToNext();

        for (int i = 0; i <= rowCount; i++) {
            addHorizontalLine(timeTableLayout);
            if (i == rowCount)
                break;

            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            timeTableLayout.addView(tableRow);

            cursor.moveToPosition(i);
            for (int j = 0; j <= columnCount; j++) {
                addVerticalLine(tableRow);
                if (j == columnCount)
                    break;

                items[i][j] = new TimeTableItem(getContext());
                items[i][j].editText.setText(cursor.getString(j + 1));
                tableRow.addView(items[i][j]);
            }
        }
        cursor.close();
    }

    private void addVerticalLine(TableRow tableRow) {
        View verticalLine = new View(getContext());
        verticalLine.setLayoutParams(new TableRow.LayoutParams(strokeWidth, TableRow.LayoutParams.MATCH_PARENT));
        verticalLine.setBackgroundColor(Color.RED);
        tableRow.addView(verticalLine);
    }

    private void addHorizontalLine(TableLayout tableLayout) {
        View horiaontalLine = new View(getContext());
        horiaontalLine.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, strokeWidth));
        horiaontalLine.setBackgroundColor(Color.RED);
        tableLayout.addView(horiaontalLine);
    }

    private void setEditTextLayoutParams() {
        Log.d(log, "width of edittext: " + (width - strokeWidth * (columnCount + 1)) / columnCount);
        Log.d(log, "height of edittext: " + (height - strokeWidth * (rowCount + 1)) / rowCount);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                items[i][j].editText.setLayoutParams(
                        new LinearLayout.LayoutParams((width - strokeWidth * (columnCount + 1)) / columnCount, (height - strokeWidth * (rowCount + 1)) / rowCount));
            }
        }
    }

    private void saveTimeTableData() {
        db = timeDbHelper.getWritableDatabase();

        for (int i = 0; i < 6; i++) {
            Log.d(log, "num:" + i);

            CharSequence cs = new StringBuffer(items[i][0].editText.getText());
            String UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET mon = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(log, cs.toString());

            cs = new StringBuffer(items[i][1].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET tue = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(log, cs.toString());

            cs = new StringBuffer(items[i][2].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET wed = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(log, cs.toString());

            cs = new StringBuffer(items[i][3].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET thu = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(log, cs.toString());

            cs = new StringBuffer(items[i][4].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET fri = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(log, cs.toString());
        }
        Log.d(log, "func. saveTimeTableData");
    }

    private void setEditableTrue(){
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < columnCount; j++){
                items[i][j].editText.setFocusableInTouchMode(true);
                items[i][j].editText.setFocusable(true);
                items[i][j].editText.setClickable(true);
                items[i][j].editText.setCursorVisible(true);
            }
        }
    }

    private void setEditableFalse(){
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < columnCount; j++){
                items[i][j].editText.setFocusableInTouchMode(false);
                items[i][j].editText.setFocusable(false);
                items[i][j].editText.setClickable(false);
                items[i][j].editText.setCursorVisible(false);
            }
        }
    }
}
