package com.hangaram.hellgaram.bus;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "itemList", strict = false)
public class ItemList {
    @Element(name="arrmsg1")
    private String arrmsg1;

    @Element(name="arrmsg2")
    private String arrmsg2;

    @Element(name="isFullFlag1")
    private String isFullFlag1;

    @Element(name="isFullFlag2")
    private String isFullFlag2;

    @Element(name="rtNm")
    private String rtNm;

    public String getArrmsg1() {
        return arrmsg1;
    }

    public String getArrmsg2() {
        return arrmsg2;
    }

    public String getIsFullFlag1() {
        return isFullFlag1;
    }

    public String getIsFullFlag2() {
        return isFullFlag2;
    }

    public String getRtNm() {
        return rtNm;
    }
}
