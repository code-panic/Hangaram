package com.hangaram.hellgaram.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hangaram.hellgaram.R;

public class CustomRadiobuttonContainer extends LinearLayout {
    private RadioGroup mRadioGroup;

    public CustomRadiobuttonContainer(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.widget_custom_radiobutton, this);

        mRadioGroup = view.findViewById(R.id.custom_radiogroup);
    }

    public String getCheckedButtonText() {
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = mRadioGroup.findViewById(selectedId);

        return radioButton.getText().toString();
    }

    public RadioGroup getRadioGroup() {
        return mRadioGroup;
    }
}
