package com.hangaram.hellgaram.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DateBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction())) {

        }
    }
}
