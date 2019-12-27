package com.hangaram.hellgaram.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hangaram.hellgaram.R;

public class CustomSeekbarContainer extends LinearLayout {
    TextView mTitleTextView;
    SeekBar mCustomSeekBar;
    TextView mValueTextView;

    public CustomSeekbarContainer(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.widget_custom_seekbar, this);

        mTitleTextView = view.findViewById(R.id.title_textview);
        mCustomSeekBar = view.findViewById(R.id.custom_seekbar);
        mValueTextView = view.findViewById(R.id.value_textview);

        mCustomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int percent, boolean b) {
                mValueTextView.setText(percent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public int getProgress () {
        return mCustomSeekBar.getProgress();
    }
    public void setTitleTextView (String title) { mTitleTextView.setText(title); }
    public SeekBar getCustomSeekBar() { return mCustomSeekBar; }
}
