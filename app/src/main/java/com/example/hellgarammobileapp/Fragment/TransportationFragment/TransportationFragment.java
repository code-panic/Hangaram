package com.example.hellgarammobileapp.Fragment.TransportationFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hellgarammobileapp.R;

public class TransportationFragment extends Fragment {
    private View view;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter stationItemAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_transport, container, false);
        init(getContext());
        return view;
    }

    void init(Context context) {
        recyclerView = view.findViewById(R.id.stationRecyclerView);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        stationItemAdapter = new StationItemAdapter();
        recyclerView.setAdapter(stationItemAdapter);
    }
}
