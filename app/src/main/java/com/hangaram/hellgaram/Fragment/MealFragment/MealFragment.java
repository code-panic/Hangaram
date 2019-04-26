package com.hangaram.hellgaram.Fragment.MealFragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.support.DataBaseHelper;
import com.hangaram.hellgaram.support.TimeGiver;


public class MealFragment extends Fragment {
    private static final String Tag = "MealFragment";

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView menuTextView;
    private TextView changedMealText;
    private RelativeLayout mealToggle;
    private ImageView leftButton;
    private ImageView rightButton;
    private TextView dayTextView;

    private int gap = 0;
    private int maxGap = 7; //다음주 급식까지 가져오기
    private int minGap = -7; //저번주 급식까지 가져오기

    private String menuString = "저장해 놓은\n급식정보가 없습니다\n인터넷에 연결하여\n다시 접속해주세요";

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Boolean islunchChecked;

    private int swipeNum = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_meal, container, false);
        init(view.getContext());
        return view;
    }

    public void init(Context context) {
        swipeRefreshLayout = view.findViewById(R.id.mealSwipe);
        menuTextView = view.findViewById(R.id.menu);
        mealToggle = view.findViewById(R.id.mealToggle);
        changedMealText = view.findViewById(R.id.chandedMealText);
        leftButton = view.findViewById(R.id.button_meal_left);
        rightButton = view.findViewById(R.id.button_meal_right);
        dayTextView = view.findViewById(R.id.mealDayTextView);

        openDataBase(context);

        //첫 시작화면에 오늘 점심 메뉴 보여주기
        setDayTextView();
        showLunch();

        //점심 저녁 바꿔보기 이벤트 설정
        mealToggle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
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
                if (event.getAction() == MotionEvent.ACTION_UP) {
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
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (checkMealIs(gap - 1))
                        gap--;
                    setDayTextView();
                    showLunch();
                    setButtonState();
                }
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //인터넷이 연결체크
                ConnectivityManager connectivityManager = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                //다운받을 급식 정보가 없을 때
                if (checkMealIs(maxGap)) {
                    if (swipeNum < 10)
                        Toast.makeText(view.getContext(), "급식 정보를 모두 가져왔습니다", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(view.getContext(), "확씨... 작작해! 이 급식충아!", Toast.LENGTH_SHORT).show();
                } else if (networkInfo != null) {
                    //데이터 베이스 정보 삭제
                    String DELETE_ALL_meal = "DELETE FROM " + DataBaseHelper.TABLE_NAME_meal;
                    sqLiteDatabase.execSQL(DELETE_ALL_meal);
                    Log.d(Tag, "Delete Database meal data");

                    //데이터 베이스 정보 추가
                    for (int gap = minGap; gap <= maxGap; gap++) {
                        MealTask mealTask = new MealTask(gap, view.getContext());
                        mealTask.execute();
                    }
                    Log.d(Tag, "Add meal data in Database");
                    Toast.makeText(view.getContext(), "로딩중...", Toast.LENGTH_SHORT).show();
                } else {
                    if (swipeNum < 10)
                        Toast.makeText(view.getContext(), "인터넷을 연결하여 다시 시도해주세요 ", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(view.getContext(), "느그 집엔 와이파이도 없지?", Toast.LENGTH_SHORT).show();
                }
                swipeNum++;
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorRed);

//        checkCount();
    }

//    private void checkCount(){
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * From " + DataBaseHelper.TABLE_NAME_meal , null);
//        Log.d(Tag,"cursorCount: "+ cursor.getCount());
//    }

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
            rightButton.setImageResource(R.drawable.date_right);
            rightButton.setClickable(true);
        } else {
            rightButton.setImageResource(R.drawable.date_right_off);
            rightButton.setClickable(false);
        }

        if (checkMealIs(gap - 1)) {
            leftButton.setImageResource(R.drawable.date_left);
            leftButton.setClickable(true);
        } else {
            leftButton.setImageResource(R.drawable.date_left_off);
            leftButton.setClickable(false);
        }
    }

    private void showLunch() {
        //점심메뉴 보여주기
        changedMealText.setText("점심");

        String[] args = {TimeGiver.getYear(gap), TimeGiver.getMonth(gap), TimeGiver.getDate(gap)};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT lunch From " + DataBaseHelper.TABLE_NAME_meal + " WHERE year = ? AND month = ? AND date = ?", args);

        Log.d(Tag, "cursor: " + cursor.getCount());
        Log.d(Tag, "showLunch");

        if (cursor.getCount() > 0) {
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

        if (cursor.getCount() > 0) {
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
