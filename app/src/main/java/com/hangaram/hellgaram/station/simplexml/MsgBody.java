package com.hangaram.hellgaram.station.simplexml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "msgBody", strict = false)
public class MsgBody {
    @ElementList(inline = true, required = false)
    public List<BusInfo> itemLists;

    public List<BusInfo> getItemLists() {
        return itemLists;
    }
}
