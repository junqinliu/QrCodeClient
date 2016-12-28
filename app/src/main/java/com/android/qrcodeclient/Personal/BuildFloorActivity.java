package com.android.qrcodeclient.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.adapter.BuildFloorAdapter;
import com.android.application.ExitApplication;
import com.android.base.BaseAppCompatActivity;
import com.android.qrcodeclient.R;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liujq on 2016/7/24.
 */
public class BuildFloorActivity extends BaseAppCompatActivity implements View.OnClickListener ,
        SwipeRefreshLayout.OnRefreshListener,AbsListView.OnScrollListener{

    static String buildfloor ;
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.listView)
    ListView listView;

    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    List<String> list = null;
    BuildFloorAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
    }

    @Override
    public void initView() {

        ExitApplication.getInstance().addAddressActivity(this);

        buildfloor = getIntent().getStringExtra("buildfloor");
    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //设置标题
        title.setText(R.string.floor_name);

         list = new ArrayList<>();
        for(int i = 0;i< Integer.parseInt(buildfloor);i++){

            list.add(String.valueOf(i+1));
        }
        adapter = new BuildFloorAdapter(this,list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);
        swipeRefresh.setOnRefreshListener(this);
        listView.setOnScrollListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent();
                intent.putExtra("hasSelectFloor",list.get(arg2));
                setResult(1000,intent);
                finish();

            }
        });

    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }






}


