package com.example.hellgarammobileapp.Fragment.SettingFragmnet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hellgarammobileapp.Fragment.TransportationFragment.VehicleItemAdapter;
import com.example.hellgarammobileapp.R;

import java.util.ArrayList;

public class ThemeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String log = "ThemeItemAdapter";

    ArrayList<ThemeItem> themeItems = new ArrayList<ThemeItem>();

    public static class ThemeHolder extends RecyclerView.ViewHolder{
        TextView themeNameTextView;
        ImageView themeImageView;

        public ThemeHolder(View view){
            super(view);
            themeNameTextView = view.findViewById(R.id.themeItemText);
            themeImageView = view.findViewById(R.id.themeItemImage);
        }
    }

    public void add(ThemeItem themeItem){
        themeItems.add(themeItem);
    }

    @Override
    public ThemeItemAdapter.ThemeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme,parent,false);
        return new ThemeItemAdapter.ThemeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ThemeItemAdapter.ThemeHolder themeHolder = (ThemeItemAdapter.ThemeHolder) holder;

        themeHolder.themeNameTextView.setText(themeItems.get(position).themeName);
        themeHolder.themeImageView.setImageResource(themeItems.get(position).themeImageresId);
    }

    @Override
    public int getItemCount() {
        return themeItems.size();
    }
}
