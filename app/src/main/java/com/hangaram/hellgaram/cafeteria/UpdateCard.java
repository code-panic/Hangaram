package com.hangaram.hellgaram.cafeteria;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.time.TimeGiver;

public class UpdateCard extends Fragment {
    View mView;

    TextView mDateText;
    Button mDownloadButton;

    int mDateGap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        mView = inflater.inflate(R.layout.card_update,container,false);

        mDateGap = getArguments().getInt("dateGap");

        mDateText = mView.findViewById(R.id.date_text);
        mDownloadButton = mView.findViewById(R.id.download_button);

        mDateText.setText(TimeGiver.getYear(mDateGap) + "." + TimeGiver.getMonth(mDateGap) + "." + TimeGiver.getDate(mDateGap));

        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CafeTask cafeTask = new CafeTask(mView.getContext());
                cafeTask.execute(Integer.parseInt(TimeGiver.getYear(mDateGap)), Integer.parseInt(TimeGiver.getMonth(mDateGap)));


            }
        });

        return mView;
    }
}
