package com.hangaram.hellgaram.Fragment.SettingFragmnet;

public class ThemeItem {
    private String themeName;
    private int themeImageresId;

    public ThemeItem(){}

    public ThemeItem(String themeName, int themeImageId) {
        this.themeName = themeName;
        this.themeImageresId = themeImageId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public int getThemeImageId() {
        return themeImageresId;
    }

    public void setThemeImageId(int themeImageId) {
        this.themeImageresId = themeImageId;
    }
}
