package com.example.hellgarammobileapp.Fragment.TransportFragment;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hellgarammobileapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class TransportFragment extends Fragment {
    private View view;
    private BusArriveInfoTask busArriveInfoTask;
    private StationItemView stationItemView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter transportAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_transport, container, false);
        init(getContext());
        return view;
    }

    void init(Context context) {
        stationItemView = view.findViewById(R.id.stationContainer);
        recyclerView = stationItemView.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        busArriveInfoTask = new BusArriveInfoTask(this);
        busArriveInfoTask.execute();
    }

    public void setBusInfo(ArrayList<TransportItem> transportItems){
        transportAdapter= new TransportAdapter(transportItems);
        recyclerView.setAdapter(transportAdapter);
    }
}
