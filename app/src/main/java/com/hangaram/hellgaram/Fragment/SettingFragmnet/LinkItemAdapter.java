package com.hangaram.hellgaram.Fragment.SettingFragmnet;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hangaram.hellgaram.R;

import java.util.ArrayList;

public class LinkItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String log = "LinkItemAdapter";

    ArrayList<LinkItem> linkItems = new ArrayList<LinkItem>();

    public static class LinkHolder extends RecyclerView.ViewHolder{
        TextView linkTextView;
        ImageView linkImageView;
        String url;

        public LinkHolder(View view){
            super(view);
            linkTextView = view.findViewById(R.id.linkItemText);
            linkImageView = view.findViewById(R.id.linkItemImage);
        }
    }

    public void add(LinkItem linkItem){
        linkItems.add(linkItem);
    }

    @Override
    public LinkItemAdapter.LinkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link,parent,false);
        return new LinkItemAdapter.LinkHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LinkItemAdapter.LinkHolder linkHolder = (LinkItemAdapter.LinkHolder) holder;

        linkHolder.linkTextView.setText(linkItems.get(position).getLinkName());
        linkHolder.linkImageView.setImageResource(linkItems.get(position).getLinkImageresId());
        linkHolder.url = linkItems.get(position).getUrl();

        linkHolder.linkTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkHolder.url));
                    v.getContext().startActivity(intent);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return linkItems.size();
    }
}

