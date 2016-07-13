package com.android.model;

/**
 * Created by jisx on 2016/6/13.
 */
public class LogBean {

    private String name;

    private String time;

    public LogBean() {
    }

    public LogBean(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
