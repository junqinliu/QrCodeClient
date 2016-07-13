package com.android.model;

/**
 * Created by jisx on 2016/6/13.
 */
public class MessageBean {

    //可扩展消息类型

    private String time;

    private String  content;

    public MessageBean() {
    }

    public MessageBean(String time, String content) {
        this.time = time;
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
