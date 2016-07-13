package com.android.adapter;

import java.util.List;

/**
 * 主要是增加添加item的方法
 * Created by jisx on 2016/5/11.
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    public abstract List<T> getList();

    public void addItem(T item) {
        getList().add(item);
        this.notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        getList().addAll(list);
        this.notifyDataSetChanged();
    }

    /**
     * 重新给list赋值
     * @param list
     */
    public void reAddList(List<T> list) {
        getList().clear();
        addAll(list);
        this.notifyDataSetChanged();
    }

    public void clearList(){
        getList().clear();
        this.notifyDataSetChanged();
    }

    public void destory(){
        clearList();
    }

}
