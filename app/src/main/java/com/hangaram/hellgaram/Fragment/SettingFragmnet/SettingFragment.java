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
import android.widget.TextView;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.main.CreditActivity;
import com.hangaram.hellgaram.support.QuoteGiver;

public class SettingFragment extends Fragment {
    private static final String Tag = "SettingFragment";

    private View view;

    private RecyclerView themeRecyclerView;
    private RecyclerView.Adapter themeItemAdapter;
    private RecyclerView.LayoutManager themeLayoutManager;

    private RecyclerView linkRecyclerView;
    private RecyclerView.Adapter linkItemAdapter;
    private RecyclerView.LayoutManager linkLayoutManager;

    public TextView quote;
    public TextView hint;

    private ImageView buttonTocredit;

    public static final int REQUEST_CODE_CREDIT = 101;

    private QuoteGiver quoteGiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_theme, container, false);
        init(view.getContext());
        return view;
    }

    private void init(Context context) {
        buttonTocredit = view.findViewById(R.id.buttonToCredit);

        //themeRecyclerView 세팅
        themeRecyclerView = view.findViewById(R.id.themeRecyclerView);

        themeLayoutManager = new LinearLayoutManager(context);
        themeRecyclerView.setLayoutManager(themeLayoutManager);

        themeItemAdapter = new ThemeItemAdapter();
        ((ThemeItemAdapter) themeItemAdapter).add(new ThemeItem("기본", R.drawable.white_bg));

        themeRecyclerView.setAdapter(themeItemAdapter);

        //linkRecyclerView 세팅
        linkRecyclerView = view.findViewById(R.id.linkRecyclerView);

        linkLayoutManager = new LinearLayoutManager(context);
        linkRecyclerView.setLayoutManager(linkLayoutManager);


        linkItemAdapter = new LinkItemAdapter();
        ((LinkItemAdapter) linkItemAdapter).add(new LinkItem("한가람 홈페이지", "http://www.hangaram.hs.kr",R.drawable.white_bg));
        ((LinkItemAdapter) linkItemAdapter).add(new LinkItem("한가람 리로스쿨", "https://hangaram.riroschool.kr",R.drawable.white_bg));
        ((LinkItemAdapter) linkItemAdapter).add(new LinkItem("한가람 나무위키", "https://namu.wiki/w/한가람고등학교",R.drawable.white_bg));

        linkRecyclerView.setAdapter(linkItemAdapter);

        //개발자 화면 버튼 세팅
        buttonTocredit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(getContext(), CreditActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_CREDIT);
                }
                return true;
            }
        });

        quote = view.findViewById(R.id.quote);
        hint = view.findViewById(R.id.hint);

        quoteGiver = new QuoteGiver(context,this);
    }
}
