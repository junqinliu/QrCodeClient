package com.android.model;

/**
 * Created by liujunqin on 2016/7/24.
 */
public class CityBean extends ModelBean{


    private String cityid;
    private String code;
    private String provincecode;

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(String provincecode) {
        this.provincecode = provincecode;
    }
}
