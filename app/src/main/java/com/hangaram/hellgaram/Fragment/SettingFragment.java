package com.hangaram.hellgaram.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hangaram.hellgaram.R;

public class SettingFragment extends Fragment {
    private static final String TAG = "SettingFragment";

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        init(mView.getContext());
        return mView;
    }

    private void init(Context context) {
        RecyclerView linkRecyclerView = mView.findViewById(R.id.link_recycler);

        TextView quote = mView.findViewById(R.id.quote);
        TextView hint = mView.findViewById(R.id.hint);

        //Link recyclerView 세팅하기
        LinearLayoutManager linkLayoutManager = new LinearLayoutManager(context);
        linkRecyclerView.setLayoutManager(linkLayoutManager);

        //Link 아이템 설정하기
        LinkAdapter linkItemAdapter = new LinkAdapter();

        linkItemAdapter.add(new LinkItem("한가람 홈페이지", "http://www.hangaram.hs.kr"));
        linkItemAdapter.add(new LinkItem("한가람 리로스쿨", "https://hangaram.riroschool.kr"));
        linkItemAdapter.add(new LinkItem("한가람 나무위키", "https://namu.wiki/w/한가람고등학교"));

        //Adapter 설정하기
        linkRecyclerView.setAdapter(linkItemAdapter);

        //오늘 명언 가져오기
        QuoteGiver quoteGiver = new QuoteGiver(mView.getContext());

        quote.setText(quoteGiver.getQuote());
        hint.setText(quoteGiver.getHint());
    }
}
