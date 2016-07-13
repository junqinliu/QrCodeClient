package com.android.model;

/**
 * Created by jisx on 2016/6/12.
 */
public class LifeItemBean {

    private String  itemName;

    private int itemIcon;

    private Class cls;

    public LifeItemBean() {
    }

    public LifeItemBean(String itemName, int itemIcon, Class cls) {
        this.itemName = itemName;
        this.itemIcon = itemIcon;
        this.cls = cls;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }
}
