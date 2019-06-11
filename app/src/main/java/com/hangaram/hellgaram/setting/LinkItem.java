package com.hangaram.hellgaram.setting;

public class LinkItem {
    private String mLinkName;
    private String mUrl;

    public LinkItem(String mLinkName, String mUrl) {
        this.mLinkName = mLinkName;
        this.mUrl = mUrl;
    }

    public String getLinkName() {
        return mLinkName;
    }

    public String getUrl() {
        return mUrl;
    }
}
