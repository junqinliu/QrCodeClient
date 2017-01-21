package com.android.qrcodeclient.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.adapter.EntranceAdapter;
import com.android.application.ExitApplication;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.EntranceBean;
import com.android.model.FamilyMicroCardBean;
import com.android.model.LogBean;
import com.android.qrcodeclient.Card.CardMainActivity;
import com.android.qrcodeclient.R;
import com.android.utils.DialogMessageExit;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.SharedPreferenceUtil;
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
    List<EntranceBean> listTemp = new ArrayList<>();

    EntranceAdapter adapter;
    int pageNumber = 0;
    int pageSize = 10;

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
        adapter = new EntranceAdapter(this,list);
        listView.setAdapter(adapter);
        getMyCard();
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //把选中的楼栋的信息保存到本地，下次进来直接可以显示
                String  BeanStr = JSON.toJSONString(list.get(arg2));
                SharedPreferenceUtil.getInstance(EntranceActivity.this).putData("EntranceBean", BeanStr);

                ExitApplication.getInstance().exitActivity();
                Intent intent = new Intent(EntranceActivity.this, CardMainActivity.class);
//                intent.putExtra("secret",list.get(arg2).getSecret());
//                intent.putExtra("buildname",list.get(arg2).getBuildname());
//                intent.putExtra("buildid",list.get(arg2).getBuildid());
//                intent.putExtra("houseid",list.get(arg2).getHouseid());
//                intent.putExtra("housename",list.get(arg2).getHousename());
//                intent.putExtra("model",list.get(arg2).getModel());

                Bundle bundle = new Bundle();
                bundle.putSerializable("FromEntranceActivity",(EntranceBean)list.get(arg2));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onClick(View v) {
        finish();
    }



    /**
     * 我的门禁列表
     */
    private void getMyCard(){

        RequestParams params = new RequestParams();
        params.put("pageSize",pageSize);
        params.put("pageNumber",pageNumber);
        HttpUtil.get(Constants.HOST + Constants.MyCardList, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if(!NetUtil.checkNetInfo(EntranceActivity.this)){

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
                                listTemp = JSON.parseArray(gg.getJSONArray("items").toString(), EntranceBean.class);
                                list.addAll(listTemp);
                                adapter.notifyDataSetChanged();
                                if (listTemp.size() == 10) {
                                    loadingMore = true;
                                } else {
                                    loadingMore = false;
                                }

                            } else {

                                if("0".equals(jsonObject.getString("code"))){

                                    DialogMessageExit.getInstance(EntranceActivity.this).showDialog();


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
