package com.example.hellgarammobileapp.Fragment.TransportFragment;

public class TransportItem {
    String transportName;
    String arrInfo;

    public TransportItem(String transportName, String arrInfo) {
        this.transportName = transportName;
        this.arrInfo = arrInfo;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getArrInfo() {
        return arrInfo;
    }

    public void setArrInfo(String arrInfo) {
        this.arrInfo = arrInfo;
    }
}
