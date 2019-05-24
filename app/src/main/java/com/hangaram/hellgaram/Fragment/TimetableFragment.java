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

    private boolean mHangaramTimetableSet = false;
    private boolean mEdited = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_timetable, container, false);

        //뷰 초기화 및 버튼 리스너 설정
        init();
        locateEditTexts();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        /* EditText 값 설정하기 */
        /* 한가람 시간표에서 가져온 정보가 덮어질 염려가 있으므로 첫번째 데이터베이스 세팅인지 검사한다 */
        if (!mHangaramTimetableSet)
            setEditTextInfo();

        mHangaramTimetableSet = false;
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

                        mEdited = true;
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

    private void locateEditTexts() {
        //editText 를 인스턴스할 LayoutInflater 객체 생성하기
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int row = 0; row < mRowCount; row++) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setBaselineAligned(false);

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

                tableRow.addView(editText);
            }
            mTableLayout.addView(tableRow);
        }
    }

    private void setEditTextInfo() {
        //데이터베이스에서 값을 읽어올 Cursor 가져오기
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        for (int row = 0; row < mRowCount; row++) {
            TableRow tableRow = (TableRow) mTableLayout.getChildAt(row);
            cursor.moveToPosition(row);

            for (int column = 0; column < mColumnCount; column++) {
                EditText editText = (EditText) tableRow.getChildAt(column);
                editText.setText(cursor.getString(column));
            }
        }

        //안드로이드 권장사항! 지우지 말자
        cursor.close();
    }

    private void saveData() {
        if (mEdited) {
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

                mEdited = true;
                mHangaramTimetableSet = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
