package com.hangaram.hellgaram.bus;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "msgBody", strict = false)
public class MsgBody {
    @ElementList(inline = true, required = false)
    public List<ItemList> itemLists;

    public List<ItemList> getItemLists() {
        return itemLists;
    }
}
