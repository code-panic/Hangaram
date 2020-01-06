package com.hangaram.hellgaram.station;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.station.simplexml.BusInfo;

import java.util.Calendar;
import java.util.List;

public class StationFragment extends Fragment {
    private static final String TAG = "BusFragment";

    private View view;

    private RelativeLayout contentContainer;
    private TabLayout tabLayout;
    private Button updateButton;

    private String arsId = "15148";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_station, container, false);

        /*뷰 객체 초기화*/
        contentContainer = view.findViewById(R.id.container_content_bus);
        tabLayout = view.findViewById(R.id.content_tab_bus);
        updateButton = view.findViewById(R.id.button_update_bus);

        /*리스너 초기화*/
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /*  월촌중학교 : 15148
                *   목동이대병원 : 15154  */
                inflateDefault(inflater);
                Log.d(TAG, "1");

                if (tab.getText().toString().equals("월촌중학교")) {
                    Log.d(TAG, "2");
                    arsId = "15148";

                    if (StationTask.busInfoList15148 != null) {
                        Log.d(TAG, "3");
                        inflateUpdated(inflater, StationTask.busInfoList15148);
                    }
                } else if (tab.getText().toString().equals("목동이대병원")) {
                    arsId = "15154";

                    if (StationTask.busInfoList15154 != null) {
                        inflateUpdated(inflater, StationTask.busInfoList15154);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*버스 정보 불러오기*/
                StationTask busTask = new StationTask(new StationTask.BusCallBack() {
                    @Override
                    public void onSuccess(List<BusInfo> busList) {
                        inflateUpdated(inflater, busList);
                    }

                    @Override
                    public void onFailure() {
                        inflateDefault(inflater);
                        Toast.makeText(getContext(), "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                });

                busTask.execute(arsId);
            }
        });

        if (arsId.equals("15148") && StationTask.busInfoList15148 != null)
            inflateUpdated(inflater, StationTask.busInfoList15148);
        else if (arsId.equals("15154") && StationTask.busInfoList15154 != null)
            inflateUpdated(inflater, StationTask.busInfoList15154);
        else
            inflateDefault(inflater);

        return view;
    }

    /*  defaultContent inflate 하기   */
    private void inflateDefault(LayoutInflater inflater) {
        inflater.inflate(R.layout.fragment_station_default, contentContainer, true);
        updateButton.setText("업데이트");
    }

    /*  topicContent inflate 하기  */
    private void inflateUpdated(LayoutInflater inflater, List<BusInfo> busList) {
        final View content = inflater.inflate(R.layout.fragment_station_updated, contentContainer, false);

        /*recyclerView 초기화*/
        RecyclerView itemContainer = content.findViewById(R.id.bus_container);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemContainer.setLayoutManager(layoutManager);

        StationAdapter stationAdapter = new StationAdapter(busList);
        itemContainer.setAdapter(stationAdapter);

        /*content 추가하가*/
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contentContainer.removeAllViews();
                contentContainer.addView(content);
            }
        });

        /*마지막 업데이트 시간 알려주기*/
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);

        updateButton.setText("마지막 업데이트\t" + cal.get(Calendar.HOUR) + "시 " + cal.get(Calendar.MINUTE) + "분");
    }
}
