package com.hangaram.hellgaram.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private static final String TAG = "TimeTableActivity";

    private final String TABLE_NAME = "timetable";

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;

    private View mView;

    private TableLayout mTableLayout;

    private int mRowCount = 7; //가로줄(행)
    private int mColumnCount = 6; //세로줄(열)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_timetable, container, false);
        init();
        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();

        //꺼졌을 때 자동으로 저장하기
        for (int row = 0; row < mRowCount; row++) {
            TableRow tableRow = (TableRow) mTableLayout.getChildAt(row);
            for (int column = 0; column < mColumnCount; column++){
                String text = ((EditText)tableRow.getChildAt(column)).getText().toString();

            }
        }
    }

    private void init() {
        final Button editButton = mView.findViewById(R.id.edit_button);
        final Button toHangaramTimetable = mView.findViewById(R.id.to_hangaram_timetable);

        mDataBaseHelper = new DataBaseHelper(getContext());
        mDatabase = mDataBaseHelper.getReadableDatabase();



        //EditText 배치 및 초기화
        setEditTexts();

        //편집 버튼 리스너
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int row = 0; row < mRowCount; row++) {
                    TableRow tableRow = (TableRow) mTableLayout.getChildAt(row);
                    for (int column = 0; column < mColumnCount; column++){
                       EditText editText = (EditText)tableRow.getChildAt(column);

                       editText.setFocusable(true);
                       editText.setClickable(true);
                       editText.setCursorVisible(true);
                    }
                }
            }
        });

        //한가람 시간표로 이동하는 버튼 리스너
        toHangaramTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://xn--o39ai969f8llbkv0lb.xn--3e0b707e/"));
                v.getContext().startActivity(intent);
            }
        });
    }

    private void setEditTexts() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int row = 0; row < mRowCount; row++) {
            TableRow tableRow = new TableRow(getContext());
            for (int column = 0; column < mColumnCount; column++) {
                View editText;

                if (row == 0 && column == 0)
                    editText = layoutInflater.inflate(R.layout.item_timetable1, tableRow, false);
                else if (row == 0)
                    editText = layoutInflater.inflate(R.layout.item_timetable2, tableRow, false);
                else if (column == 1)
                    editText = layoutInflater.inflate(R.layout.item_timetable3, tableRow, false);
                else
                    editText = layoutInflater.inflate(R.layout.item_timetable4, tableRow, false);



                tableRow.addView(editText);
            }
        }
    }

    //한가람 시간표와 연동
    public void linkWithFinalChild(JSONArray jsonArray) {
        //
        try {
            for (int row = 1; row < mRowCount; row++) {
                TableRow tableRow = (TableRow) mTableLayout.getChildAt(row);
                for (int column = 1; column < mColumnCount; column++) {
                    JSONObject jsonObject = jsonArray.getJSONArray(row).getJSONObject(column);
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

                    EditText editText = (EditText)tableRow.getChildAt(column);
                    editText.setText(sumString);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
