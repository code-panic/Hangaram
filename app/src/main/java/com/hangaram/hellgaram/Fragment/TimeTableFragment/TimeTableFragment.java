package com.hangaram.hellgaram.Fragment.TimeTableFragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.Activity.CautionActivity;
import com.hangaram.hellgaram.support.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

public class TimeTableFragment extends Fragment {
    private static final String Tag = "TimeTableActivity";

    private final String TABLE_NAME = "timetable";

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    private View view;
    private TableLayout timeTableLayout;
    private Button editButton;
    private Button toFinalChild;

    private int rowCount = 6; //가로줄
    private int columnCount = 5; //세로줄

    private TimeTableItem[][] items = new TimeTableItem[rowCount][columnCount];

    private int width;
    private int height;

    private int strokeWidth = 1;

    private static boolean isEditChecked = false;
    public static boolean isLinkFinalChild = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timetable, container, false);

        timeTableLayout = view.findViewById(R.id.timeTableLayout);
        editButton = view.findViewById(R.id.editButton);
        toFinalChild = view.findViewById(R.id.toFinalChild);

        ViewTreeObserver viewTreeObserver = timeTableLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    timeTableLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    width = ((timeTableLayout.getMeasuredWidth() - strokeWidth * (columnCount + 1)) / columnCount) * columnCount + strokeWidth * (columnCount + 1);
                    height = (width - strokeWidth * (columnCount + 1)) / columnCount * rowCount + strokeWidth * (rowCount + 1);
                    Log.d(Tag, "width of editText: " + width);
                    Log.d(Tag, "height of editText: " + height);
                    init();
                }
            }
        });

        return view;
    }

    private void init() {
        openDataBase(getContext());
        spotEditText();
        setEditTextLayoutParams();
        writeInfoFromDatabase();

        editButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isEditChecked) {
                        editButton.setBackgroundResource(R.drawable.border_gray);
                        setEditableFalse();
                        saveTimeTableData();
                        Toast.makeText(getContext(), "저장되었습니다", Toast.LENGTH_SHORT).show();
                        isEditChecked = false;
                    } else {
                        editButton.setBackgroundResource(R.drawable.border_red);
                        setEditableTrue();
                        isEditChecked = true;
                    }
                }
                return true;
            }
        });

        toFinalChild.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://xn--o39ai969f8llbkv0lb.xn--3e0b707e/"));
                    v.getContext().startActivity(intent);
                }
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity().getIntent().getData() != null && getActivity().getIntent().getData().getScheme().equals("htc") && isLinkFinalChild == false) {
            Intent intent = new Intent(getActivity(), CautionActivity.class);
            startActivityForResult(intent, 3000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //자동저장기능
        if (isEditChecked) {
            editButton.setBackgroundResource(R.drawable.border_gray);
            setEditableFalse();
            saveTimeTableData();
            Toast.makeText(view.getContext(), "저장되었습니다", Toast.LENGTH_SHORT).show();
            isEditChecked = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 3000) {
                try {
                    if (data.getBooleanExtra("result", false))
                        linkWithFinalChild(new JSONArray(getActivity().getIntent().getData().getQueryParameter("data")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //데이터 베이스 준비
    private Cursor openDataBase(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        db = dataBaseHelper.getReadableDatabase();

        cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        return cursor;
    }

    //EditText 배치하기
    private void spotEditText() {
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
                tableRow.addView(items[i][j]);
            }
        }
    }

    //가로줄 추가
    private void addVerticalLine(TableRow tableRow) {
        View verticalLine = new View(getContext());
        verticalLine.setLayoutParams(new TableRow.LayoutParams(strokeWidth, TableRow.LayoutParams.MATCH_PARENT));
        verticalLine.setBackgroundColor(Color.RED);
        tableRow.addView(verticalLine);
    }

    //세로줄 추가
    private void addHorizontalLine(TableLayout tableLayout) {
        View horiaontalLine = new View(getContext());
        horiaontalLine.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, strokeWidth));
        horiaontalLine.setBackgroundColor(Color.RED);
        tableLayout.addView(horiaontalLine);
    }

    //EditText 크기 계산
    private void setEditTextLayoutParams() {
        Log.d(Tag, "width of edittext: " + (width - strokeWidth * (columnCount + 1)) / columnCount);
        Log.d(Tag, "height of edittext: " + (height - strokeWidth * (rowCount + 1)) / rowCount);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                items[i][j].editText.setLayoutParams(
                        new LinearLayout.LayoutParams((width - strokeWidth * (columnCount + 1)) / columnCount, (height - strokeWidth * (rowCount + 1)) / rowCount));
            }
        }
    }

    //데이터베이스에서 정보 가져오기
    private void writeInfoFromDatabase() {
        cursor.moveToNext();

        for (int i = 0; i < rowCount; i++) {
            cursor.moveToPosition(i);
            for (int j = 0; j < columnCount; j++) {
                items[i][j].editText.setText(cursor.getString(j + 1));
            }
        }
        cursor.close();
    }

    //한가람 시간표와 연동
    public void linkWithFinalChild(JSONArray jsonArray) {
        //기존 내용 삭제
        editButton.setBackgroundResource(R.drawable.border_red);
        setEditableTrue();
        isEditChecked = true;
        isLinkFinalChild = true;

        //
        try {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    JSONObject jsonObject = jsonArray.getJSONArray(i).getJSONObject(j);
                    String sumString = "";

                    Log.d("linkWithFinalChild", jsonObject.getString("subject") + "\n"
                            + jsonObject.getString("teacher") + "\n"
                            + jsonObject.getString("room"));

                    if (jsonObject.getString("subject") != "null")
                        sumString += jsonObject.getString("subject");

                    if (jsonObject.getString("teacher") != "null")
                        sumString += "\n" + jsonObject.getString("teacher");

                    if (jsonObject.getString("room") != "null")
                        sumString += "\n" + jsonObject.getString("room");

                    items[j][i].editText.setText(sumString);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //데이터베이스 업데이트
    private void saveTimeTableData() {
        db = dataBaseHelper.getWritableDatabase();

        for (int i = 0; i < 6; i++) {
            Log.d(Tag, "num:" + i);

            CharSequence cs = new StringBuffer(items[i][0].editText.getText());
            String UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET mon = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(Tag, cs.toString());

            cs = new StringBuffer(items[i][1].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET tue = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(Tag, cs.toString());

            cs = new StringBuffer(items[i][2].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET wed = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(Tag, cs.toString());

            cs = new StringBuffer(items[i][3].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET thu = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(Tag, cs.toString());

            cs = new StringBuffer(items[i][4].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET fri = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(Tag, cs.toString());
        }
        Log.d(Tag, "func. saveTimeTableData");
    }


    //수정 가능하게 만들기
    private void setEditableTrue() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                items[i][j].editText.setFocusableInTouchMode(true);
                items[i][j].editText.setFocusable(true);
                items[i][j].editText.setClickable(true);
                items[i][j].editText.setCursorVisible(true);
            }
        }
    }

    //수정 불가능하게 만들기
    private void setEditableFalse() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                items[i][j].editText.setFocusableInTouchMode(false);
                items[i][j].editText.setFocusable(false);
                items[i][j].editText.setClickable(false);
                items[i][j].editText.setCursorVisible(false);
            }
        }
    }
}
