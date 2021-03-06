package com.android.qrcodeclient.Life;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.adapter.MessageAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.LogBean;
import com.android.model.MessageBean;
import com.android.qrcodeclient.R;
import com.android.utils.DialogMessageExit;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by jisx on 2016/6/13.
 */
public class MessageActivity extends BaseAppCompatActivity implements View.OnClickListener ,
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

    List<MessageBean> list;
    List<MessageBean> listTemp = new ArrayList<>();

    MessageAdapter adapter;
    int pageNumber = 0;
    int pageSize = 20;

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
        title.setText(R.string.message_title);

        list = new ArrayList<>();
        adapter = new MessageAdapter(this,list);
        listView.setAdapter(adapter);

        Constants.isShowRedPoint = false;
        getData();
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
           /* loadingMore = true;
            //TODO 加载数据
            adapter.addAll(getData());
            loadingMore = false;*/
            getData();
        }
    }




    private void getData() {

        RequestParams params = new RequestParams();
        params.put("pageSize",pageSize);
        params.put("pageNumber",pageNumber);
        params.put("propertytype","PUSH");
        HttpUtil.get(Constants.HOST + Constants.Message, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetUtil.checkNetInfo(MessageActivity.this)) {

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
                                listTemp = JSON.parseArray(gg.getJSONArray("items").toString(), MessageBean.class);
                                list.addAll(listTemp);
                                adapter.notifyDataSetChanged();
                                if (listTemp.size() == 10) {
                                    loadingMore = true;
                                } else {
                                    loadingMore = false;
                                }

                                if(list == null || list.size() <=0){

                                    showToast("暂无消息通告");
                                }

                            } else {

                                if("0".equals(jsonObject.getString("code"))){

                                    DialogMessageExit.getInstance(MessageActivity.this).showDialog();


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
