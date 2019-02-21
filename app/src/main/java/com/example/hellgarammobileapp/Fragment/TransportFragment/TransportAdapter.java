package com.example.hellgarammobileapp.Fragment.TransportFragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hellgarammobileapp.R;

import java.util.ArrayList;

public class TransportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String log = "TransportAdapter";

    ArrayList<TransportItem> TransportItems = new ArrayList<TransportItem>();

    public static class TransportHolder extends RecyclerView.ViewHolder{
        TextView transportNameText;
        TextView arrInfoText;

        public TransportHolder(View view){
            super(view);
            transportNameText = view.findViewById(R.id.transportNameText);
            arrInfoText = view.findViewById(R.id.arriveInfoText);
        }
    }

    public TransportAdapter(ArrayList<TransportItem> transportItems){
        this.TransportItems = transportItems;
        Log.d(log,"transportItem size: " + transportItems.size());
    }

    @Override
    public TransportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transport,parent,false);
        return new TransportHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       TransportHolder transportHolder = (TransportHolder) holder;

       transportHolder.transportNameText.setText(TransportItems.get(position).transportName);
       transportHolder.arrInfoText.setText(TransportItems.get(position).arrInfo);

       Log.d(log,"transportItems" + position + ": " + transportHolder.transportNameText.getText());
        Log.d(log,"transportItems" + position + ": " + transportHolder.arrInfoText.getText());
    }

    @Override
    public int getItemCount() {
        return TransportItems.size();
    }
}
