package com.android.model;

import java.io.Serializable;

/**
 * Created by liujunqin on 2016/7/24.
 */
public class FamilyMicroCardBean implements Serializable{

   private String familyName;
   private String familyPhone;
   private String familyPwd;
   private String familyStart;
   private String familyEnd;

    public FamilyMicroCardBean(String familyName, String familyPhone, String familyPwd, String familyStart, String familyEnd) {
        this.familyName = familyName;
        this.familyPhone = familyPhone;
        this.familyPwd = familyPwd;
        this.familyStart = familyStart;
        this.familyEnd = familyEnd;
    }


    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyPhone() {
        return familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getFamilyPwd() {
        return familyPwd;
    }

    public void setFamilyPwd(String familyPwd) {
        this.familyPwd = familyPwd;
    }

    public String getFamilyStart() {
        return familyStart;
    }

    public void setFamilyStart(String familyStart) {
        this.familyStart = familyStart;
    }

    public String getFamilyEnd() {
        return familyEnd;
    }

    public void setFamilyEnd(String familyEnd) {
        this.familyEnd = familyEnd;
    }
}
