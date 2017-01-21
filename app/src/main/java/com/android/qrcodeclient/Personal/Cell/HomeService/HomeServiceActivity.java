package com.android.qrcodeclient.Personal.Cell.HomeService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.adapter.HomeServiceAdapter;
import com.android.adapter.MessageAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.HomeServiceBean;
import com.android.model.MessageBean;
import com.android.qrcodeclient.R;
import com.android.utils.DialogMessageExit;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.TextUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by liujq on 2016/6/13.
 */
public class HomeServiceActivity extends BaseAppCompatActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener,AbsListView.OnScrollListener{

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

    List<HomeServiceBean> list;
    List<HomeServiceBean> listTemp = new ArrayList<>();

    HomeServiceAdapter adapter;
    int pageNumber = 0;
    int pageSize = 20;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_service);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //设置标题
        title.setText("家政服务");
        list = new ArrayList<>();
//        list.add(new HomeServiceBean("上门修电器","15522509000"));
//        list.add(new HomeServiceBean("维护水表","186000935333"));
//        list.add(new HomeServiceBean("清洁卫生","15522509000"));
        adapter = new HomeServiceAdapter(this,list);
        listView.setAdapter(adapter);

        getData();
    }

    @Override
    public void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefresh.setOnRefreshListener(this);
        listView.setOnScrollListener(this);

    }



    @Override
    public void onRefresh() {
        pageNumber = 0;
        list.clear();
        getData();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){


            default:
                break;
        }

    }



    private void getData() {

        RequestParams params = new RequestParams();
        params.put("pageSize",pageSize);
        params.put("pageNumber",pageNumber);
        params.put("propertytype","HOUSEKEEPING_SERVICE");
        HttpUtil.get(Constants.HOST + Constants.Message, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetUtil.checkNetInfo(HomeServiceActivity.this)) {

                    showToast("当前网络不可用,请检查网络");
                    return;
                }
                showLoadingDialog();
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
                                listTemp = JSON.parseArray(gg.getJSONArray("items").toString(), HomeServiceBean.class);
                                list.addAll(listTemp);
                                adapter.notifyDataSetChanged();
                                if (listTemp.size() == 10) {
                                    loadingMore = true;
                                } else {
                                    loadingMore = false;
                                }

                                if(list == null || list.size() <=0){

                                    showToast("暂无服务项目");
                                }

                            } else {

                                if("0".equals(jsonObject.getString("code"))){

                                    DialogMessageExit.getInstance(HomeServiceActivity.this).showDialog();


                                }else{

                                    showToast(jsonObject.getString("msg"));
                                }
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

                closeLoadDialog();
            }


        });


    }



}
