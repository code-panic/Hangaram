package com.example.hellgarammobileapp.Fragment.SettingFragmnet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hellgarammobileapp.Fragment.TransportationFragment.StationItemAdapter;
import com.example.hellgarammobileapp.R;

public class SettingFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter themeItemAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_setting,container,false);
        init(view.getContext());
        return view;
    }

    private void init(Context context){
        recyclerView = view.findViewById(R.id.themeRecyclerView);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        themeItemAdapter = new ThemeItemAdapter();
        ((ThemeItemAdapter) themeItemAdapter).add(new ThemeItem("기본",R.drawable.tabicon_bus));

        recyclerView.setAdapter(themeItemAdapter);
    }


}
