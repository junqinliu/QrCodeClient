package com.android.qrcodeclient.Personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.adapter.EntranceAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.model.EntranceBean;
import com.android.qrcodeclient.R;
import com.android.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by jisx on 2016/6/13.
 */
public class EntranceActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.listView)
    ListView listView;

    /*是不是在加载更多的状态中*/
    private boolean loadingMore = false;

    List<EntranceBean> list;

    EntranceAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //设置标题
        String titleName = getIntent().getStringExtra(getResources().getString(R.string.develop_title));
        if (!TextUtil.isEmpty(titleName)) {
            title.setText(titleName);
        }

        list = new ArrayList<>();
        adapter = new EntranceAdapter(this, getData());
        listView.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    private List<EntranceBean> getData() {
        List<EntranceBean> list = new ArrayList<>();
        list.add(new EntranceBean("福建福州西滨好美家1栋"));
        list.add(new EntranceBean("福建福州西滨好美家2栋"));
        list.add(new EntranceBean("福建福州西滨好美家3栋"));
        list.add(new EntranceBean("福建福州西滨好美家4栋"));
        list.add(new EntranceBean("福建福州西滨好美家5栋"));
        list.add(new EntranceBean("福建福州西滨好美家6栋"));
        list.add(new EntranceBean("福建福州西滨好美家7栋"));
        list.add(new EntranceBean("福建福州西滨好美家8栋"));
        list.add(new EntranceBean("福建福州西滨好美家9栋"));
        list.add(new EntranceBean("福建福州西滨好美家10栋"));
        return list;
    }

}
