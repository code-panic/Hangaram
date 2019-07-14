package com.hangaram.hellgaram.bus;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hangaram.hellgaram.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BusFragment extends Fragment {
    private static final String TAG = "BusFragment";

    private View view;

    private RelativeLayout contentContainer;
    private TabLayout tabLayout;
    private Button updateButton;

    private String stNm = "월촌중학교";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bus, container, false);

        /*뷰 객체 초기화*/
        contentContainer = view.findViewById(R.id.container_content_bus);
        tabLayout = view.findViewById(R.id.content_tab_bus);
        updateButton = view.findViewById(R.id.button_update_bus);

        /*리스너 초기화*/
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /*탭이 선택되면
                * 탭의 텍스트에 따라 적절히 inflate 를 하고
                * 버튼의 텍스트를 "업데이트"로 바꾼다.*/
                stNm = tab.getText().toString();

                if (stNm.equals("월촌중학교") && BusTask.busList1 != null)
                    inflateShowContent(inflater, BusTask.busList1);
                else if (stNm.equals("목동이대병원") && BusTask.busList2 != null)
                    inflateShowContent(inflater, BusTask.busList2);
                else
                    inflateUpdateContent(inflater);
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
                BusTask busTask = new BusTask(new BusTask.BusCall() {
                    @Override
                    public void onSuccess(ArrayList<HashMap<String, String>> busList) {
                        inflateShowContent(inflater, busList);
                    }

                    @Override
                    public void onFailure() {
                        inflateUpdateContent(inflater);
                        Toast.makeText(getContext(), "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                }, stNm);

                busTask.execute();
            }
        });

        /*적절히 inflate 하기(직관성을 위해 일부러 함수흫 만들지 않음)*/
        if (stNm.equals("월촌중학교") && BusTask.busList1 != null)
            inflateShowContent(inflater, BusTask.busList1);
        else if (stNm.equals("목동이대병원") && BusTask.busList2 != null)
            inflateShowContent(inflater, BusTask.busList2);
        else
            inflateUpdateContent(inflater);

        return view;
    }

    /*showContent inflate 하기*/
    private void inflateShowContent(LayoutInflater inflater, ArrayList<HashMap<String, String>> busList) {
        View content = inflater.inflate(R.layout.content_show_bus, contentContainer, false);

        /*recyclerView 초기화*/
        RecyclerView itemContainer = content.findViewById(R.id.bus_container);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemContainer.setLayoutManager(layoutManager);

        BusAdapter busAdapter = new BusAdapter(busList);
        itemContainer.setAdapter(busAdapter);

        /*content 추가하가*/
        contentContainer.removeAllViews();
        contentContainer.addView(content);

        /*마지막 업데이트 시간 알려주기*/
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);

        updateButton.setText("마지막 업데이트\t" + busList.get(0).get("hour") + "시 " + busList.get(0).get("min") + "분");
    }

    /*updateContent inflate 하기*/
    private void inflateUpdateContent(LayoutInflater inflater) {
        inflater.inflate(R.layout.content_update_bus, contentContainer, true);
        updateButton.setText("업데이트");
    }

}
