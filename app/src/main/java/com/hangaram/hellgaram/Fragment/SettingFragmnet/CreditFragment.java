package com.hangaram.hellgaram.Fragment.SettingFragmnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hangaram.hellgaram.R;

public class CreditFragment extends Fragment {
    private View view;
    public SettingFragment settingFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_credit,container,false);
        return view;
    }
}
