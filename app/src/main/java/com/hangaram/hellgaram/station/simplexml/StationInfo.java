package com.hangaram.hellgaram.station.simplexml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="ServiceResult", strict = false)
public class StationInfo {

    @Element
    public MsgBody msgBody;

    public MsgBody getMsgBody() {
        return msgBody;
    }
}