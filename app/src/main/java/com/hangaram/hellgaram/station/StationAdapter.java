package com.hangaram.hellgaram.station;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.station.simplexml.BusInfo;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.BusHolder> {
    private static final  String TAG = "BusAdapter";

    private List<BusInfo> busInfoList;

    public StationAdapter(List<BusInfo> busInfoList) {
        this.busInfoList = busInfoList;
    }

    public static class BusHolder extends RecyclerView.ViewHolder {
        public TextView rtNmText;
        public TextView arrmsg1Text;
        public TextView arrmsg2Text;

        public BusHolder(LinearLayout view) {
            super(view);

            rtNmText = view.findViewById(R.id.bus_rtNm_text);
            arrmsg1Text = view.findViewById(R.id.bus_arrmsg1_text);
            arrmsg2Text = view.findViewById(R.id.bus_arrmsg2_text);
        }
    }

    /*BusHolder 와 View 객체 연결*/
    @Override
    public BusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);

        BusHolder busHolder = new BusHolder(view);
        return busHolder;
    }

    /*데이터 바인딩*/
    @Override
    public void onBindViewHolder(BusHolder busHolder, int position) {
        BusInfo busInfo = busInfoList.get(position);

        /*ex)571   #14분 #7정거장 #여유*/
        busHolder.rtNmText.setText(busInfo.getRtNm());
        busHolder.arrmsg1Text.setText(getBusInfoText(busInfo.getArrmsg1(), busInfo.getIsFullFlag1()));
        busHolder.arrmsg2Text.setText(getBusInfoText(busInfo.getArrmsg2(), busInfo.getIsFullFlag2()));
    }

    /*아이템 개수 반환*/
    @Override
    public int getItemCount() {
        return busInfoList.size();
    }

    private String getBusInfoText(String arrmsg, String isFullFlag) {
        return "#" + StationTask.getArrmsg(arrmsg) + "\t"
                + "#" + StationTask.getStamsg(arrmsg) + "\t"
                + "#" + StationTask.getFullFlag(isFullFlag);
    }
}