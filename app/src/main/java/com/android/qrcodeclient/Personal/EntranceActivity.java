package com.android.qrcodeclient.Personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.adapter.EntranceAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.EntranceBean;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.android.utils.TextUtil;
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

        return list;
    }

    /**
     * 我的门禁列表
     */
    private void getMyCard(){

        RequestParams params = new RequestParams();
        params.put("userid", "");


        HttpUtil.post(Constants.HOST + Constants.MyCard, params, new AsyncHttpResponseHandler() {
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                if (responseBody != null) {


                    String str = new String(responseBody);
                    System.out.print(str);
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();

            }


        });


    }

}
