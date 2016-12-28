package com.android.qrcodeclient.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.adapter.ComunityMallAdapter;
import com.android.adapter.LifeAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.AdBean;
import com.android.model.ComunityMallBean;
import com.android.model.LifeItemBean;
import com.android.qrcodeclient.Personal.Cell.ComunityMall.WebViewActivity;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.TextUtil;
import com.flyco.banner.anim.select.RotateEnter;
import com.flyco.banner.anim.unselect.NoAnimExist;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liujunfeng on 2016/9/18.
 */
public class ComunityMallActivity extends BaseAppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.grid_life)
    GridView gridview;

    List<ComunityMallBean> list;
    List<ComunityMallBean> listtemp  = new ArrayList<>();
    ComunityMallAdapter comunityMallAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comunity_mall);
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

        comunityMallAdapter = new ComunityMallAdapter(this, list);
        gridview.setAdapter(comunityMallAdapter);

        getCommunityMallsAd();
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);
        gridview.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(!TextUtil.isEmpty(list.get(position).getUrl())){


            Intent intent = new Intent(ComunityMallActivity.this, WebViewActivity.class);

            intent.putExtra("title",list.get(position).getTitle());
            intent.putExtra("url",list.get(position).getUrl());

            startActivity(intent);
        }

    }


    public  void getCommunityMallsAd(){

        RequestParams params = new RequestParams();
        HttpUtil.get(Constants.HOST + Constants.CommunityMallsAd, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetUtil.checkNetInfo(ComunityMallActivity.this)) {

                    showToast("当前网络不可用,请检查网络");
                    return;
                }

            }


            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    try {
                        String str = new String(responseBody);
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject != null) {

                            if (jsonObject.getBoolean("success")) {
                                JSONObject gg = new JSONObject(jsonObject.getString("data"));
                                listtemp = JSON.parseArray(gg.getJSONArray("items").toString(), ComunityMallBean.class);
                                list.addAll(listtemp);
                                if (list != null && list.size() > 0) {

                                    for (int i = 0; i < list.size(); i++) {

                                        list.get(i).setPicurl(Constants.HOST + list.get(i).getPicurl());
                                    }

                                    comunityMallAdapter.notifyDataSetChanged();
                                }

                            } else {

                                showToast(jsonObject.getString("msg"));
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

}
