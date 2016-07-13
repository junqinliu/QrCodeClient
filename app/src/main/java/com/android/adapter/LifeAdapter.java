package com.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.model.LifeItemBean;
import com.android.qrcodeclient.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liujunqin on 2016/5/12.
 */
public class LifeAdapter extends BaseAdapter {

    Context context;
    List<LifeItemBean> list;

    public LifeAdapter(Context context, List<LifeItemBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public List getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_life, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(list.get(i).getItemIcon()).into(holder.itemIcon);
        holder.itemName.setText(list.get(i).getItemName());
        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.item_icon)
        ImageView itemIcon;
        @Bind(R.id.item_name)
        TextView itemName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
