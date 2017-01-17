package com.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.model.HomeServiceBean;
import com.android.model.MessageBean;
import com.android.qrcodeclient.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liujunqin on 2016/5/12.
 */
public class HomeServiceAdapter extends BaseAdapter {

    Context context;
    List<HomeServiceBean> list;

    public HomeServiceAdapter(Context context, List<HomeServiceBean> list) {
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
            convertView = View.inflate(context, R.layout.item_home_service, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.time.setText(list.get(i).getTime());
        holder.title.setText(list.get(i).getTitle());
        holder.content.setText(list.get(i).getContent());
        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.title)
        TextView title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
