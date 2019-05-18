package com.hangaram.hellgaram.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hangaram.hellgaram.R;

import java.util.ArrayList;

public class LinkItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "LinkItemAdapter";

    private ArrayList<LinkItem> mLinkItems = new ArrayList<>();

    @Override
    public LinkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LinkHolder 와 view 를 일대일 대응시키기
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link,parent,false);
        return new LinkHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //추가한 View 마다 정보 부여하기

        final LinkHolder linkHolder = (LinkItemAdapter.LinkHolder) holder;

        //view 의 이름과 주소를 설정하기
        linkHolder.mLinkButton.setText(mLinkItems.get(position).getLinkName());
        linkHolder.mUrl = mLinkItems.get(position).getUrl();

        //누르면 URL 주소로 이동하기
        linkHolder.mLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkHolder.mUrl));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLinkItems.size();
    }

    //아이템 추가하는 함수
    void add(LinkItem linkItem){
        mLinkItems.add(linkItem);
    }

    private static class LinkHolder extends RecyclerView.ViewHolder{
        Button mLinkButton;
        String mUrl;

        public LinkHolder(View view){
            super(view);
            mLinkButton = view.findViewById(R.id.link);
        }
    }

}

