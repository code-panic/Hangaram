package com.hangaram.hellgaram.bus;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class ServiceResult {
    @ElementList
    private List<itemList> itemList;

    public List<itemList> getItemList() {
        return itemList;
    }
}