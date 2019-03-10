package com.hangaram.hellgaram.Fragment.SettingFragmnet;

public class LinkItem {
    private String linkName;
    private String url;
    private int linkImageresId;

    public LinkItem(String linkName, String url, int linkImageresId) {
        this.linkName = linkName;
        this.url = url;
        this.linkImageresId = linkImageresId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLinkImageresId() {
        return linkImageresId;
    }

    public void setLinkImageresId(int linkImageresId) {
        this.linkImageresId = linkImageresId;
    }
}
