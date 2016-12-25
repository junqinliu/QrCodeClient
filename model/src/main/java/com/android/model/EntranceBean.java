package com.android.model;

import java.io.Serializable;

/**
 * Created by jisx on 2016/6/14.
 */
public class EntranceBean implements Serializable {


    private String houseid;
    private String housename;
    private String secret; // 微卡秘钥二维码
    private String buildname;
    private String buildid;
    private String model;

    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getBuildname() {
        return buildname;
    }

    public void setBuildname(String buildname) {
        this.buildname = buildname;
    }

    public String getBuildid() {
        return buildid;
    }

    public void setBuildid(String buildid) {
        this.buildid = buildid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
