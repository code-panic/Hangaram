package com.hangaram.hellgaram.bus;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="ServiceResult", strict = false)
public class BusInfo {

    @Element
    public MsgBody msgBody;

    public MsgBody getMsgBody() {
        return msgBody;
    }
}