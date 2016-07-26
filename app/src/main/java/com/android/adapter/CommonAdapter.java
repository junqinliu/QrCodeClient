package com.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.model.BlockBean;
import com.android.model.ModelBean;
import com.android.qrcodeclient.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liujunqin on 2016/5/12.
 */
public class CommonAdapter<T extends ModelBean> extends BaseAdapter {

    Context context;
    List<T> list;

    public CommonAdapter(Context context, List<T> list) {
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
            convertView = View.inflate(context, R.layout.item_common, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.block_txt.setText(list.get(i).getName());

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
