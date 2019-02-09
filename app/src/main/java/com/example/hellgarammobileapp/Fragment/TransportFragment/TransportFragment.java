package com.example.hellgarammobileapp.Fragment.TransportFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hellgarammobileapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URI;
import java.net.URL;

public class TransportFragment extends Fragment {
    public static String TransportActivityLog = "TransportActivityLog";

    View view;

    boolean inarrmsg1 = false;
    boolean inrtNm = false;

    String arrmsg1 = null;
    String rtNm = null;

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_meal, container, false);
        init(view.getContext());
        return view;
    }

    void init(Context context) {
        textView = view.findViewById(R.id.busText);

        try {
            URL url = new URL("http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?serviceKey=Eac9cIWYCnUF%2FJtK50AVgrcFhfTJnhZwLafvzPCBxWjY%2FZpsYwPR7HpdyQYz%2FTP%2Bz8JwWIJX1Vgrh1Nxcp%2Fj7A%3D%3D&arsId=15148");

            StrictMode.enableDefaults();

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("arrmsg1"))
                            inarrmsg1 = true;
                        if (parser.getName().equals("rtNm"))
                            inrtNm = true;
                        break;
                    case XmlPullParser.TEXT:
                        if (inarrmsg1) {
                            arrmsg1 = parser.getText();
                            Log.d(TransportActivityLog,arrmsg1);
                            inarrmsg1 = false;
                        }
                        if (inrtNm){
                            rtNm = parser.getText();
                            Log.d(TransportActivityLog,rtNm);
                            inrtNm = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("itemList")){
                            textView.setText(textView.getText().toString()
                                            + rtNm + "\t" + arrmsg1 + "\n");
                            Log.d(TransportActivityLog,textView.getText().toString());
                        }
                        break;
                }
                parserEvent = parser.next();
            }

        } catch (Exception e) {
            textView.setText("Error! Error!");
        }
    }
}
