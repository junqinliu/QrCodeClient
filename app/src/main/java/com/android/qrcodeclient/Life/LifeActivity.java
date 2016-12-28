package com.android.qrcodeclient.Life;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.adapter.LifeAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.ComunityMallBean;
import com.android.model.LifeItemBean;
import com.android.qrcodeclient.Personal.Cell.ComunityMall.WebViewActivity;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.TextUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liujunqin on 2016/5/31.
 */
public class LifeActivity extends BaseAppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    @Bind(R.id.grid_life)
    GridView gridLife;

    List<ComunityMallBean> list;
    List<ComunityMallBean> listtemp  = new ArrayList<>();

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.send_card_layout)
    LinearLayout send_card_layout;
    @Bind(R.id.log_layout)
    LinearLayout log_layout;
    @Bind(R.id.message_layout)
    LinearLayout message_layout;
    LifeAdapter lifeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_life);
    }



    @Override
    public void initView() {

        title.setText(R.string.life_title);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
    }

    @Override
    public void initData() {


//        list.add(new LifeItemBean("微购", R.mipmap.weigou, null));
//        list.add(new LifeItemBean("逛街吧", R.mipmap.guangjieba, null));
//        list.add(new LifeItemBean("蘑菇街", R.mipmap.mogujie, null));
//        list.add(new LifeItemBean("微卖", R.mipmap.weimai, null));
//        list.add(new LifeItemBean("喵喵微店", R.mipmap.miaomiaoweidian, null));
//        list.add(new LifeItemBean("拍拍小店", R.mipmap.paipai, null));
//        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, null));
//        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, null));
//        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, null));

        list = new ArrayList<>();
        lifeAdapter = new LifeAdapter(this, list);
        gridLife.setAdapter(lifeAdapter);

        getLifeAd();

    }

    @Override
    public void setListener() {

        gridLife.setOnItemClickListener(this);
     //   toolbar.setNavigationOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_card_layout.setOnClickListener(this);
        log_layout.setOnClickListener(this);
        message_layout.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(!TextUtil.isEmpty(list.get(position).getUrl())){


            Intent intent = new Intent(LifeActivity.this, WebViewActivity.class);
            intent.putExtra("title",list.get(position).getTitle());
            intent.putExtra("url",list.get(position).getUrl());

            startActivity(intent);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.send_card_layout:

                startActivity(new Intent(LifeActivity.this,SendCardActivity.class));

                break;
            case R.id.log_layout:

                startActivity(new Intent(LifeActivity.this,LogActivity.class));

                break;
            case R.id.message_layout:

                startActivity(new Intent(LifeActivity.this,MessageActivity.class));

                break;

        }


    }


    public  void getLifeAd(){

        RequestParams params = new RequestParams();
        HttpUtil.get(Constants.HOST + Constants.LifeAd, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetUtil.checkNetInfo(LifeActivity.this)) {

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

                                    lifeAdapter.notifyDataSetChanged();
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
