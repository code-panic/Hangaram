package com.hangaram.hellgaram.station.simplexml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "itemList", strict = false)
public class BusInfo {
    @Element(name="arrmsg1")
    private String firstBusArriveInfo;

    @Element(name="arrmsg2")
    private String secondBusArriveInfo;

//    @Element(name="isFullFlag1")
//    private String isFullFlag1;
//
//    @Element(name="isFullFlag2")
//    private String isFullFlag2;

    @Element(name="rtNm")
    private String busName;

    public String getFirstBusArriveInfo() {
        return firstBusArriveInfo;
    }

    public String getSecondBusArriveInfo() {
        return secondBusArriveInfo;
    }

//    public String getIsFullFlag1() {
//        return isFullFlag1;
//    }
//
//    public String getIsFullFlag2() {
//        return isFullFlag2;
//    }

    public String getBusName() {
        return busName;
    }
}
