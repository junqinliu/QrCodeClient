package com.android.model;

/**
 * Created by jisx on 2016/6/14.
 */
public class EntranceBean {
    /*门禁名称*/
    private String entranceName;


    public EntranceBean() {
    }

    public EntranceBean(String entranceName) {
        this.entranceName = entranceName;
    }

    public String getEntranceName() {
        return entranceName;
    }

    public void setEntranceName(String entranceName) {
        this.entranceName = entranceName;
    }
}
