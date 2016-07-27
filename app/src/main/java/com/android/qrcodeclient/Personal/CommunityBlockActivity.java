package com.android.qrcodeclient.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.adapter.CommunityAdapter;
import com.android.adapter.CommunityBlockAdapter;
import com.android.application.AppContext;
import com.android.application.ExitApplication;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.CBBean;
import com.android.model.CommunityBean;
import com.android.model.CommunityBlockBean;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liujq on 2016/7/24.
 */
public class CommunityBlockActivity extends BaseAppCompatActivity implements View.OnClickListener ,
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

    List<CommunityBlockBean> list;
    List<CommunityBlockBean> listTemp = new ArrayList<>();
    CommunityBlockAdapter adapter;

    int pageNumber = 0;
    int pageSize = 10;
    String  buildid;
    String  name;
    String  areaId;
    String  areaName;

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
        title.setText(R.string.block_name);

        Intent intent=getIntent();
        areaId=intent.getStringExtra("areaId");
        areaName=intent.getStringExtra("areaName");

        list = new ArrayList<>();
        adapter = new CommunityBlockAdapter(this,list);
        listView.setAdapter(adapter);
        getData();
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);
        swipeRefresh.setOnRefreshListener(this);
        listView.setOnScrollListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                buildid = list.get(arg2).getBuildid();
                name = list.get(arg2).getName();

                CBBean cb = new CBBean();
                cb.setAreaId(areaId);
                cb.setAreaName(areaName);
                cb.setBuildid(buildid);
                cb.setName(name);
                AppContext myApplicaton = (AppContext)getApplication();
                myApplicaton.setcBBean(cb);
                ExitApplication.getInstance().exitAddressActivity();
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
       // adapter.reAddList(getData());
        pageNumber = 0;
        list.clear();
        getData();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
       // 倒数第二个item为当前屏最后可见时，加载更多
        if ((firstVisibleItem + visibleItemCount + 1 >= totalItemCount) && loadingMore) {

            //TODO 加载数据
            getData();

        }
    }

    private void getData() {

        RequestParams params = new RequestParams();
        params.put("pageSize",pageSize);
        params.put("pageNumber",pageNumber);
        HttpUtil.get(Constants.HOST + Constants.Block + "/"+ areaId, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    try {
                        String str = new String(responseBody);
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject != null) {

                            if (jsonObject.getBoolean("success")) {


                                pageNumber = pageNumber + 1;
                                JSONObject gg = new JSONObject(jsonObject.getString("data"));
                                listTemp = JSON.parseArray(gg.getJSONArray("items").toString(), CommunityBlockBean.class);
                                list.addAll(listTemp);
                                adapter.notifyDataSetChanged();
                                if(listTemp.size() == 10){
                                    loadingMore = true;
                                }else{
                                    loadingMore = false;
                                }

                            } else {

                                showToast("请求接口失败，请联系管理员");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                if (responseBody != null) {
                    try {
                        String str1 = new String(responseBody);
                        JSONObject jsonObject1 = new JSONObject(str1);
                        showToast(jsonObject1.getString("msg"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();

            }


        });



    }

   public static Handler myHandler = new Handler() {

       public void handleMessage(Message msg) {
           switch (msg.what) {
               case 1:



                   break;
           }
           super.handleMessage(msg);
       }




    };

}


