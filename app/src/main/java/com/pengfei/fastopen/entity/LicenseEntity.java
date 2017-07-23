package com.pengfei.fastopen.entity;

/**
 * 开源协议
 * Created by mengfei on 2017/7/23.
 */
public class LicenseEntity {
    public String title;
    public String desc;
    public String url;

    public LicenseEntity(String title, String desc, String url) {
        this.title = title;
        this.desc = desc;
        this.url = url;
    }
}
