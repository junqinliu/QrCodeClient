package com.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.model.EntranceBean;
import com.android.qrcodeclient.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liujunqin on 2016/5/12.
 */
public class EntranceAdapter extends BaseAdapter {

    Context context;
    List<EntranceBean> list;

    public EntranceAdapter(Context context, List<EntranceBean> list) {
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
            convertView = View.inflate(context, R.layout.item_entrance, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.entrance.setText(list.get(i).getEntranceName());
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.entrance)
        TextView entrance;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
