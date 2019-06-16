package com.hangaram.hellgaram.setting;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hangaram.hellgaram.R;

import java.util.ArrayList;

public class LinkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
        final LinkHolder linkHolder = (LinkAdapter.LinkHolder) holder;

        linkHolder.mUrl = mLinkItems.get(position).getUrl();
        linkHolder.mLinkText.setText(mLinkItems.get(position).getLinkName());
        linkHolder.mLinkUrl.setText(linkHolder.mUrl);

        //누르면 URL 주소로 이동하기
        linkHolder.mLinkCard.setOnClickListener(new View.OnClickListener() {
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
        CardView mLinkCard;
        TextView mLinkText;
        TextView mLinkUrl;
        String mUrl;

        public LinkHolder(View view){
            super(view);
            mLinkCard = view.findViewById(R.id.link_card);
            mLinkText = view.findViewById(R.id.link_text);
            mLinkUrl = view.findViewById(R.id.link_url);
        }
    }
}

