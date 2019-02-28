package com.hangaram.hellgaram.Fragment.TransportationFragment;

public class VehicleItem {
    String transportName;
    String arrInfo;

    public VehicleItem(String transportName, String arrInfo) {
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
