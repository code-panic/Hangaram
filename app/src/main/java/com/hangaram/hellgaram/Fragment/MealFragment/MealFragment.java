package com.hangaram.hellgaram.Fragment.MealFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.support.DataBaseHelper;
import com.hangaram.hellgaram.support.TimeGiver;

import java.sql.Time;


public class MealFragment extends Fragment {
    private static final String Tag = "MealFragment";

    private View view;
    private TextView menuTextView;
    private TextView changedMealText;
    private RelativeLayout mealToggle;
    private ImageView leftButton;
    private ImageView rightButton;
    private TextView dayTextView;

    private int gap = 0;
    private int maxGap = 7;
    private int minGap = -7;

    private String menuString;

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Boolean islunchChecked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_meal, container, false);
        init(view.getContext());
        return view;
    }

    public void init(Context context) {
        menuTextView = view.findViewById(R.id.menu);
        mealToggle = view.findViewById(R.id.mealToggle);
        changedMealText = view.findViewById(R.id.chandedMealText);
        leftButton = view.findViewById(R.id.button_meal_left);
        rightButton = view.findViewById(R.id.button_meal_right);
        dayTextView = view.findViewById(R.id.mealDayTextView);

        openDataBase(context);

        //인터넷이 연결되어 있다면 정보 가져오기
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //인터넷이 연결되어 있고 오늘 급식이 데이터 베이스에 없을 때
        if (networkInfo != null && !checkMealIs(gap + maxGap)) {
            //데이터 베이스 정보 삭제
            String DELETE_ALL_meal = "DELETE FROM " + DataBaseHelper.TABLE_NAME_meal;
            sqLiteDatabase.execSQL(DELETE_ALL_meal);
            Log.d(Tag,"Delete Database meal data");

            //데이터 베이스 정보 추가
            for (int gap = minGap; gap <= maxGap; gap++) {
                MealTask mealTask = new MealTask(gap, context);
                mealTask.execute();
            }
            Log.d(Tag,"Add meal data in Database");
        }

        Log.d(Tag,"gap -" + gap);

        //첫 시작화면에 오늘 점심 메뉴 보여주기
        setDayTextView();
        showLunch();

        //점심 저녁 바꿔보기 이벤트 설정
        mealToggle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (islunchChecked) {
                        showDinner();
                    } else {
                        showLunch();
                    }
                }
                return true;
            }
        });

        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkMealIs(gap + 1))
                        gap++;
                    setDayTextView();
                    showLunch();
                    setButtonState();
                }
                return true;
            }
        });

        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkMealIs(gap - 1))
                        gap--;
                    setDayTextView();
                    showLunch();
                    setButtonState();
                }
                return true;
            }
        });

//        // 인텐트 필터 설정
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//
//
//        // 동적리시버 생성
//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//
//                //인터넷이 연결된 경우
//                if (networkInfo != null) {
//                    openDataBase(context);
//
//                    //데이터 베이스 정보 삭제
//                    String DELETE_ALL_meal = "DELETE FROM " + DataBaseHelper.TABLE_NAME_meal;
//                    sqLiteDatabase.execSQL(DELETE_ALL_meal);
//
//                    //데이터 베이스 정보 추가
//                    for(int gap = -4; gap < 5; gap++){
//                        MealTask mealTask = new MealTask(gap,context);
//                        mealTask.execute();
//                    }
//
//                    Log.d(Tag,"Load MealInfo");
//                }
//
//            }
//        };
//
//        // 위에서 설정한 인텐트필터+리시버정보로 리시버 등록
//        context.registerReceiver(receiver, intentFilter);
    }

    private boolean checkMealIs(int gap) {
        String[] args = {TimeGiver.getYear(gap), TimeGiver.getMonth(gap), TimeGiver.getDate(gap)};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT lunch From " + DataBaseHelper.TABLE_NAME_meal + " WHERE year = ? AND month = ? AND date = ?", args);

        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    private void setButtonState() {
        if (checkMealIs(gap + 1)) {
            rightButton.setImageResource(R.drawable.edit_on);
            rightButton.setClickable(true);
        } else {
            rightButton.setImageResource(R.drawable.edit_off);
            rightButton.setClickable(false);
        }

        if (checkMealIs(gap - 1)) {
            leftButton.setImageResource(R.drawable.edit_on);
            leftButton.setClickable(true);
        } else {
            leftButton.setImageResource(R.drawable.edit_off);
            leftButton.setClickable(false);
        }
    }

    private void showLunch() {
        //점심메뉴 보여주기
        changedMealText.setText("점심");

        String[] args = {TimeGiver.getYear(gap), TimeGiver.getMonth(gap), TimeGiver.getDate(gap)};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT lunch From " + DataBaseHelper.TABLE_NAME_meal + " WHERE year = ? AND month = ? AND date = ?", args);

        Log.d(Tag, "cursor: " + cursor.getCount());
        Log.d(Tag,"showLunch");

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Log.d(Tag, cursor.getString(0));
            menuString = cursor.getString(0);
        }

        cursor.close();

        islunchChecked = true;
        menuTextView.setText(menuString);
    }

    public void showDinner() {
        //저녁 메뉴 보여주기
        changedMealText.setText("저녁");

        String[] args = {TimeGiver.getYear(gap), TimeGiver.getMonth(gap), TimeGiver.getDate(gap)};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT dinner From " + DataBaseHelper.TABLE_NAME_meal + " WHERE year = ? AND month = ? AND date = ?", args);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Log.d(Tag, cursor.getString(0));
            menuString = cursor.getString(0);
        }

        cursor.close();

        islunchChecked = false;
        menuTextView.setText(menuString);
    }

    private void openDataBase(Context context) {
        //데이터 베이스 준비
        dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getReadableDatabase();
    }

    private void setDayTextView() {
        if (gap == 0)
            dayTextView.setText("오늘");
        else if (gap == 1)
            dayTextView.setText("내일");
        else if (gap == -1)
            dayTextView.setText("어제");
        else
            dayTextView.setText(TimeGiver.getMonth(gap) + "/" + TimeGiver.getDate(gap) + "(" + TimeGiver.getWeek(gap) + ")");
    }
}
