package com.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.model.EntranceBean;
import com.android.model.FamilyMicroCardBean;
import com.android.qrcodeclient.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liujunqin on 2016/5/12.
 */
public class FamilyMicroAdapter extends BaseAdapter {

    Context context;
    List<FamilyMicroCardBean> list;

    public FamilyMicroAdapter(Context context, List<FamilyMicroCardBean> list) {
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
            convertView = View.inflate(context, R.layout.item_family_card, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.family_member_name_txt.setText(list.get(i).getSurname());
        holder.family_member_phone_txt.setText(list.get(i).getTel());
        holder.time_point.setText("有效时间:"+list.get(i).getValidStartTime()+" 至 "+list.get(i).getValidEndTime());
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.family_member_name_txt)
        TextView family_member_name_txt;
        @Bind(R.id.family_member_phone_txt)
        TextView family_member_phone_txt;
        @Bind(R.id.time_point)
        TextView time_point;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
