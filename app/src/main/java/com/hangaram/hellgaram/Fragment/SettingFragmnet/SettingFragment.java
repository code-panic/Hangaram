package com.hangaram.hellgaram.Fragment.SettingFragmnet;

import android.content.Context;
import android.content.Intent;
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
import com.hangaram.hellgaram.main.CreditActivity;

public class SettingFragment extends Fragment{
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter themeItemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView buttonTocredit;

    public static final int REQUEST_CODE_CREDIT = 101;

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
        ((ThemeItemAdapter) themeItemAdapter).add(new ThemeItem("기본",R.drawable.white_bg));

        recyclerView.setAdapter(themeItemAdapter);

        buttonTocredit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Intent intent = new Intent(getContext(), CreditActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_CREDIT);
                }
                return true;
            }
        });
    }
}
