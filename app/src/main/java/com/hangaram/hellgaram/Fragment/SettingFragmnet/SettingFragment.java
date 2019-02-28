package com.hangaram.hellgaram.Fragment.SettingFragmnet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hangaram.hellgaram.R;

public class SettingFragment extends Fragment {
    private static String log = "SettingFragment";

    private View view;
    public FrameLayout settingContainer;

    private ThemeFragment themeFragment;
    private CreditFragment creditFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_setting,container,false);
        init(view.getContext());
        return view;
    }

    private void init(Context context){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        settingContainer = view.findViewById(R.id.settingContainer);
        themeFragment = new ThemeFragment();
        themeFragment.settingFragment = this;
        creditFragment = new CreditFragment();
        creditFragment.settingFragment = this;
        fragmentTransaction.add(R.id.settingContainer,themeFragment).commit();
    }

    public void changeToCredit(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.settingContainer,creditFragment).commit();
    }

    public void changeToTheme(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.settingContainer,themeFragment).commit();
    }
}
