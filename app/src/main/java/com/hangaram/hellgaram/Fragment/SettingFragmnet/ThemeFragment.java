package com.hangaram.hellgaram.Fragment.SettingFragmnet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hangaram.hellgaram.R;

public class ThemeFragment extends Fragment{
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter themeItemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView buttonTocredit;

    public SettingFragment settingFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_theme,container,false);
        init(view.getContext());
        return view;
    }

    private void init(Context context){
        recyclerView = view.findViewById(R.id.themeRecyclerView);
        buttonTocredit = view.findViewById(R.id.buttonToCredit);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        themeItemAdapter = new ThemeItemAdapter();
        ((ThemeItemAdapter) themeItemAdapter).add(new ThemeItem("기본",R.drawable.tabicon_bus));

        recyclerView.setAdapter(themeItemAdapter);

        buttonTocredit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    settingFragment.changeToCredit();
                }
                return true;
            }
        });
    }
}
