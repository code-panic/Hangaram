package com.example.hellgarammobileapp.Fragment.MealFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hellgarammobileapp.CustomView.BlumeText;
import com.example.hellgarammobileapp.R;


public class MealFragment extends Fragment {
    private String log = "MealFragment";

    private String Schoolcode = "B100000549"; //한가람고 학교코드

    private View view;
    private TextView menu;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_meal, container, false);
        init(view.getContext());
        return view;
    }

    public void init(Context context){
        menu = view.findViewById(R.id.menu);

//        String str = "https://schoolmenukr.ml/api/high/" + Schoolcode
//                + "?year=" + TimeGiver.getYear()
//                + "&month=" + TimeGiver.getMonth()
//                + "&date=" + TimeGiver.getDate();

        String str = "https://schoolmenukr.ml/api/high/" + Schoolcode
                + "?year=" + "2019"
                + "&month=" + "01"
                + "&date=" + "24";

        MealTask mealTask = new MealTask(this);
        mealTask.execute(str);
    }

    public void setMenu(String str) {
        Log.d(log,str);
        menu.setText(str);
    }
}
