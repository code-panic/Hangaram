package com.hangaram.hellgaram.Fragment.SettingFragmnet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.main.CreditActivity;
import com.hangaram.hellgaram.support.QuoteGiver;
import com.hangaram.hellgaram.support.TimeGiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment {
    private static String log = "SettingFragment";

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter themeItemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView buttonTocredit;

    public TextView quote;
    public TextView hint;

    public static final int REQUEST_CODE_CREDIT = 101;

    private QuoteGiver quoteGiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_theme, container, false);
        init(view.getContext());
        return view;
    }

    private void init(Context context) {
        recyclerView = view.findViewById(R.id.themeRecyclerView);
        buttonTocredit = view.findViewById(R.id.buttonToCredit);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        themeItemAdapter = new ThemeItemAdapter();
        ((ThemeItemAdapter) themeItemAdapter).add(new ThemeItem("기본", R.drawable.white_bg));

        recyclerView.setAdapter(themeItemAdapter);

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
