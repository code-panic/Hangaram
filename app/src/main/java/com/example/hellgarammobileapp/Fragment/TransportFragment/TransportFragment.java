package com.example.hellgarammobileapp.Fragment.TransportFragment;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hellgarammobileapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URI;
import java.net.URL;

public class TransportFragment extends Fragment {
    View view;

    EditText searchStationEdittext;
    TextView textView = null;

    BusArriveInfoTask busArriveInfoTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_transport, container, false);
        init();
        return view;
    }

    void init() {
        searchStationEdittext = view.findViewById(R.id.searchStationEdittext);
        textView = view.findViewById(R.id.busText);

        busArriveInfoTask = new BusArriveInfoTask(this);
        busArriveInfoTask.execute();
    }

    public void setBusInfo(String str){
        textView.setText(str);
    }
}
