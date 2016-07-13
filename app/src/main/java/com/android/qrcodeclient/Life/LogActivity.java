package com.android.qrcodeclient.Life;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.adapter.LogAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.model.LogBean;
import com.android.qrcodeclient.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by jisx on 2016/6/13.
 */
public class LogActivity extends BaseAppCompatActivity implements View.OnClickListener ,
        SwipeRefreshLayout.OnRefreshListener,AbsListView.OnScrollListener{

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.listView)
    ListView listView;

    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    /*是不是在加载更多的状态中*/
    private boolean loadingMore = false;

    List<LogBean> list;

    LogAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //设置标题
        title.setText(R.string.log_title);

        list = new ArrayList<>();
        adapter = new LogAdapter(this,list);
        listView.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);
        swipeRefresh.setOnRefreshListener(this);
        listView.setOnScrollListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onRefresh() {
        adapter.reAddList(getData());
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
// 倒数第二个item为当前屏最后可见时，加载更多
        if ((firstVisibleItem + visibleItemCount + 1 >= totalItemCount) && !loadingMore) {
            loadingMore = true;
            //TODO 加载数据
            adapter.addAll(getData());
            loadingMore = false;
        }
    }

    private List<LogBean> getData() {
        List<LogBean> list = new ArrayList<>();
        list.add(new LogBean("小名1","2016-4-3 19:20:32"));
        list.add(new LogBean("小名2","2016-4-3 19:20:32"));
        list.add(new LogBean("小名3","2016-4-3 19:20:32"));
        list.add(new LogBean("小名4","2016-4-3 19:20:32"));
        list.add(new LogBean("小名5","2016-4-3 19:20:32"));
        list.add(new LogBean("小名6","2016-4-3 19:20:32"));
        list.add(new LogBean("小名7","2016-4-3 19:20:32"));
        list.add(new LogBean("小名8","2016-4-3 19:20:32"));
        list.add(new LogBean("小名9","2016-4-3 19:20:32"));
        list.add(new LogBean("小名10","2016-4-3 19:20:32"));
        list.add(new LogBean("小名11","2016-4-3 19:20:32"));
        list.add(new LogBean("小名12","2016-4-3 19:20:32"));
        return list;
    }

}
