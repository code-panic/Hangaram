package com.example.hellgarammobileapp.Fragment.MealFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hellgarammobileapp.R;
import com.example.hellgarammobileapp.support.TimeGiver;


public class MealFragment extends Fragment {
    View view;

    String Schoolcode = "B100000549"; //한가람고 학교코드

    TextView lunchText;
    TextView dinnerText;

    TextView lunchMenu;
    TextView dinnerMenu;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_meal, container, false);
        init(view.getContext());
        return view;
    }

    public void init(Context context){
        lunchText = view.findViewById(R.id.lunchText);
        dinnerText = view.findViewById(R.id.dinnerText);

        lunchMenu = view.findViewById(R.id.lunchMenu);
        dinnerMenu = view.findViewById(R.id.dinnerMenu);

        lunchText.setText("Lunch");
        dinnerText.setText("Dinner");

        String str = "https://schoolmenukr.ml/api/high/" + Schoolcode
                + "?year=" + TimeGiver.getYear()
                + "&month=" + TimeGiver.getMonth()
                + "&date=" + TimeGiver.getDate();

        MealTask mealTask = new MealTask(this);
        mealTask.execute(str);
    }
    public void setLunchMenu(String str) {
        this.lunchMenu.setText(str);
    }

    public void setDinnerMenu(String str) {
        this.dinnerMenu.setText(str);
    }
}
