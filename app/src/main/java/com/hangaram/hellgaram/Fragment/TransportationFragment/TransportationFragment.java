package com.hangaram.hellgaram.Fragment.TransportationFragment;

import android.content.Context;
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

import com.hangaram.hellgaram.R;

public class TransportationFragment extends Fragment {
    private View view;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter stationItemAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ImageView busRefreshIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_transport, container, false);
        init(getContext());
        return view;
    }

    void init(Context context) {
        recyclerView = view.findViewById(R.id.stationRecyclerView);
        busRefreshIcon = view.findViewById(R.id.bud_refresh_icon);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        stationItemAdapter = new StationItemAdapter();
        recyclerView.setAdapter(stationItemAdapter);

        busRefreshIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                }
                return true;
            }
        });

    }
}
