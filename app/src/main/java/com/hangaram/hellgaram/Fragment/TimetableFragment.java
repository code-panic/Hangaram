package com.hangaram.hellgaram.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.support.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimetableFragment extends Fragment {
    private static final String TAG = "TimetableFragment";
    private static final String TABLE_NAME = "timetable";

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mDatabase;

    private View mView;
    private TableLayout mTableLayout;

    private int mRowCount = 7; //가로줄(행)
    private int mColumnCount = 6; //세로줄(열)

    private boolean mIsEditTextSetCheked = false;
    private boolean mIsEditedChecked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_timetable, container, false);

        //뷰 초기화 및 버튼 리스너 설정
        init();

        return mView;
    }

    /* onCreateVIew 에 setEditTexts() 함수를 두면
    editText 의 값들이 시스템에 의해 onResume 전에 바뀌므로
    여기서 editText 의 텍스트들을 초기화한다. */
    @Override
    public void onStart() {
        super.onStart();

        //EditText 배치 및 초기화
        if (!mIsEditTextSetCheked)
            setEditTexts();

        mIsEditTextSetCheked = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        //저장하기
        saveData();
    }

    private void init() {
        mTableLayout = mView.findViewById(R.id.table_layout);

        Button editButton = mView.findViewById(R.id.edit_button);
        Button toHangaramTimetable = mView.findViewById(R.id.to_hangaram_timetable);

        mDataBaseHelper = new DataBaseHelper(getContext());
        mDatabase = mDataBaseHelper.getReadableDatabase();

        //편집 버튼 리스너
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //모든 editText 편집 가능한 상태로 바꾸기
                for (int row = 0; row < mRowCount; row++) {
                    TableRow tableRow = (TableRow) mTableLayout.getChildAt(row);
                    for (int column = 0; column < mColumnCount; column++) {
                        EditText editText = (EditText) tableRow.getChildAt(column);

                        editText.setFocusable(true);
                        editText.setClickable(true);
                        editText.setCursorVisible(true);
                        editText.setFocusableInTouchMode(true);

                        mIsEditedChecked = true;
                    }
                }
            }
        });

        //한가람 시간표로 이동하는 버튼 리스너
        toHangaramTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //한가람 시간표로 이동하기
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://xn--o39ai969f8llbkv0lb.xn--3e0b707e/"));
                v.getContext().startActivity(intent);
            }
        });
    }

    private void setEditTexts() {
        //editText 를 인스턴스할 LayoutInflater 객체 생성하기
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //데이터베이스에서 값을 읽어올 Cursor 가져오기
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        for (int row = 0; row < mRowCount; row++) {
            TableRow tableRow = new TableRow(getContext());
            cursor.moveToPosition(row);

            for (int column = 0; column < mColumnCount; column++) {
                EditText editText;

                //editText 위치에 따라 인스턴스화 하기
                /*
                item_timetable1 : 채우기 용 editText
                item_timetable2 : 요일 editText
                item_timetable3 : 교시 editText
                item_timetable4 : 과목 정보 editText
                */
                if (row == 0 && column == 0)
                    editText = (EditText) layoutInflater.inflate(R.layout.item_timetable1, tableRow, false);
                else if (row == 0)
                    editText = (EditText) layoutInflater.inflate(R.layout.item_timetable2, tableRow, false);
                else if (column == 0)
                    editText = (EditText) layoutInflater.inflate(R.layout.item_timetable3, tableRow, false);
                else
                    editText = (EditText) layoutInflater.inflate(R.layout.item_timetable4, tableRow, false);

                editText.setText(cursor.getString(column));

                tableRow.addView(editText);
            }
            mTableLayout.addView(tableRow);
        }

        //안드로이드 권장사항! 지우지 말자
        cursor.close();
    }

    //꺼졌을 때 자동으로 저장하기
    private void saveData() {
        if (mIsEditedChecked) {
            String[] arr = {"period", "mon", "tue", "wed", "thu", "fri"};

            for (int row = 0; row < mRowCount; row++) {
                TableRow tableRow = (TableRow) mTableLayout.getChildAt(row);
                ContentValues contentValues = new ContentValues();

                for (int column = 0; column < mColumnCount; column++) {
                    String text = ((EditText) tableRow.getChildAt(column)).getText().toString();
                    contentValues.put(arr[column], text);
                }

                /*id는 1부터 시작하기 때문에 row 에 1을 더해주어야 한다.*/
                String[] args = {row + 1 + ""};
                mDatabase.update(TABLE_NAME, contentValues, "id = ?", args);

                //완료됨을 알려주는 Toast 메세지 보내기
                Toast.makeText(getContext(), "시간표가 저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //한가람 시간표와 연동하기
    public void linkWithHangaramTimetable(JSONArray jsonArray) {

        try {
            for (int row = 1; row < mRowCount; row++) {
                TableRow tableRow = (TableRow) mTableLayout.getChildAt(row);
                for (int column = 1; column < mColumnCount; column++) {
                    JSONObject jsonObject = jsonArray.getJSONArray(column - 1).getJSONObject(row - 1);
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

                    EditText editText = (EditText) tableRow.getChildAt(column);
                    editText.setText(sumString);
                }

                mIsEditedChecked = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
