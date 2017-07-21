package com.pengfei.fastopen.entity;

/**
 * 用于主界面的Spinner
 * Created by mengfei on 2017/7/21.
 */
public class SpinnerEntity {

    private int iconRes;
    private String showStr;

    public SpinnerEntity(String showStr, int iconRes) {
        this.showStr = showStr;
        this.iconRes = iconRes;
    }

    public int getIconRes() {
        return iconRes;
    }

    public String getShowStr() {
        return showStr;
    }
}
