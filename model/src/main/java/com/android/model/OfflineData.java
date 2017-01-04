package com.android.model;

import java.util.List;

/**
 * Created by liujunqin on 2017/1/4.
 */
public class OfflineData{


    String houseid;
    String description;
    String name;
    CardMap cardMap;
    String housecode;

    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CardMap getCardMap() {
        return cardMap;
    }

    public void setCardMap(CardMap cardMap) {
        this.cardMap = cardMap;
    }

    public String getHousecode() {
        return housecode;
    }

    public void setHousecode(String housecode) {
        this.housecode = housecode;
    }
}
