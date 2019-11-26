package com.hangaram.hellgaram.bus;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hangaram.hellgaram.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusHolder> {
    private static final  String TAG = "BusAdapter";

    private ArrayList<HashMap<String,String>> busInfoList;

    public BusAdapter(ArrayList<HashMap<String,String>> busList) {
        busInfoList = busList;
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
        HashMap<String,String> busInfo = busInfoList.get(position);

        /*ex)571   #14분 #7정거장 #여유*/
        busHolder.rtNmText.setText(busInfo.get("rtNm"));
        busHolder.arrmsg1Text.setText(getArrmsgText(busInfo.get("arrmsg1"), busInfo.get("isFullFlag1")));
        busHolder.arrmsg2Text.setText(getArrmsgText(busInfo.get("arrmsg2"), busInfo.get("isFullFlag2")));
    }

    /*아이템 개수 반환*/
    @Override
    public int getItemCount() {
        return busInfoList.size();
    }

    private String getArrmsgText (String arrmsg, String isFullFlag) {
        return "#" + BusTask.getArrmsg(arrmsg) + "\t"
                + "#" + BusTask.getStamsg(arrmsg) + "\t"
                + "#" + BusTask.getFullFlag(isFullFlag);
    }
}