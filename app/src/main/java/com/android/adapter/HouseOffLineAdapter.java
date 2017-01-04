package com.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.model.HouseBean;
import com.android.model.OfflineData;
import com.android.qrcodeclient.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liujunqin on 2016/5/12.
 */
public class HouseOffLineAdapter extends BaseAdapter {

    Context context;
    List<OfflineData> list;
    private int  selectItem=-1;

    public HouseOffLineAdapter(Context context, List<OfflineData> list) {
        this.context = context;
        this.list = list;
    }
    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
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
            convertView = View.inflate(context, R.layout.item_house, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.block_txt.setText(list.get(i).getName());

        if (i == selectItem) {

            convertView.setBackgroundColor(Color.parseColor("#F0F0F0"));
        }
        else {
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.block_txt)
        TextView block_txt;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
