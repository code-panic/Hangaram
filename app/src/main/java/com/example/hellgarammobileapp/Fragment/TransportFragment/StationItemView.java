package com.example.hellgarammobileapp.Fragment.TransportFragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.hellgarammobileapp.R;

public class StationItemView extends LinearLayout {
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
    }

}
