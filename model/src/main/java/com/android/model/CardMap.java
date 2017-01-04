package com.android.model;

import java.util.List;

/**
 * Created by liujunqin on 2017/1/4.
 */
public class CardMap {

    String total;

    List<EntranceBean> items;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<EntranceBean> getItems() {
        return items;
    }

    public void setItems(List<EntranceBean> items) {
        this.items = items;
    }
}
