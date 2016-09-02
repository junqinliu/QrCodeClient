package com.android.model;

/**
 * Created by liujunqin on 2016/7/24.
 */
public class CommunityBlockOtherBean {


    private String name;
    private String buildid;
    private boolean pass; // 是否通过


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildid() {
        return buildid;
    }

    public void setBuildid(String buildid) {
        this.buildid = buildid;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }
}
