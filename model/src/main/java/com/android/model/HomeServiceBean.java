package com.android.model;

/**
 * Created by jisx on 2016/6/13.
 */
public class HomeServiceBean {

    //可扩展消息类型

    private String time;

    private String  title;

    private String content;



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
