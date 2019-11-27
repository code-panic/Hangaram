package com.hangaram.hellgaram.bus;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class itemList {
    @Element
    private String arrmsg1;

    @Element
    private String arrmsg2;

    @Element
    private String isFullFlag1;

    @Element
    private String isFullFlag2;

    @Element
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
