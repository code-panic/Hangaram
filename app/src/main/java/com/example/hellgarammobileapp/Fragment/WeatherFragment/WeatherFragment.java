package com.example.hellgarammobileapp.Fragment.WeatherFragment;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hellgarammobileapp.R;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

public class WeatherFragment extends LinearLayout {

    TextView snowText;
    TextView rainText;
    TextView tempText;
    TextView fineText;

    public WeatherFragment(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_transport, this, true);

        snowText = findViewById(R.id.snowText);
        rainText = findViewById(R.id.rainText);
        tempText = findViewById(R.id.temperatureText);
        fineText = findViewById(R.id.fineDustText);
    }
}
