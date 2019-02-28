package com.hangaram.hellgaram.Fragment.TransportationFragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hangaram.hellgaram.R;

import java.util.ArrayList;

public class StationItemView extends LinearLayout {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter transportAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView stationNameText;

    public StationItemView(Context context) {
        super(context);
        init(context);
    }

    public StationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_station, this, true);

        recyclerView = findViewById(R.id.transportRecyclerView);
        stationNameText = findViewById(R.id.stationNameText);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setBusInfo(ArrayList<VehicleItem> vehicleItems){
        transportAdapter= new VehicleItemAdapter(vehicleItems);
        recyclerView.setAdapter(transportAdapter);
    }

    public void setStationNameText(String stationName){
        stationNameText.setText(stationName);
    }

    public void executeBusArriveInfo(String arsId){
        BusArriveInfoTask busArriveInfoTask = new BusArriveInfoTask(this);
        busArriveInfoTask.execute(arsId);
    }
}
