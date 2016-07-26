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
import com.android.adapter.CommonAdapter;
import com.android.application.AppContext;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.AddressBean;
import com.android.model.AreaBean;
import com.android.model.CityBean;
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
import cz.msebera.android.httpclient.Header;

/**
 * Created by liujq on 2016/7/24.
 */
public class AreaActivity extends BaseAppCompatActivity implements View.OnClickListener{

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.provice_listview)
    ListView provice_listview;

    CommonAdapter commonAdapter;
    List<AreaBean> areaBeanList = new ArrayList<>();
    List<AreaBean> areaBeanListTemp = new ArrayList<>();

    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String areaCode;
    private String areaName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provice);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //设置标题
        String titleName = getResources().getString(R.string.zone_title);
        if (!TextUtil.isEmpty(titleName)) {
            title.setText(titleName);
        }

        Intent intent=getIntent();
        provinceCode=intent.getStringExtra("provinceCode");
        provinceName=intent.getStringExtra("provinceName");
        cityCode=intent.getStringExtra("cityCode");
        cityName=intent.getStringExtra("cityName");
        commonAdapter = new CommonAdapter(this,areaBeanList);
        provice_listview.setAdapter(commonAdapter);

        getCityList();

    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        provice_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                areaCode = areaBeanList.get(arg2).getCode();
                areaName = areaBeanList.get(arg2).getName();


                AppContext myApplicaton = (AppContext)getApplication();
                AddressBean addressBean = new AddressBean();
                //addressBean.setProvinceId(provinceId);
                addressBean.setProvinceName(provinceName);
                addressBean.setProvinceCode(provinceCode);
               // addressBean.setCityId(cityId);
                addressBean.setCityName(cityName);
                addressBean.setCityCode(cityCode);
                //addressBean.setAreaId(areaId);
                addressBean.setAreaName(areaName);
                addressBean.setAreaCode(areaCode);
                myApplicaton.setAddressBean(addressBean);
                finish();



            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    private void  getCityList(){

        RequestParams params = new RequestParams();


        HttpUtil.get(Constants.HOST + Constants.Area + "/"+cityCode,params,new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    try {
                        String str = new String(responseBody);
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject != null) {

                            if(jsonObject.getBoolean("success")){

                                areaBeanList.clear();
                                JSONObject gg = new JSONObject(jsonObject.getString("data"));
                                areaBeanListTemp = JSON.parseArray(gg.getJSONArray("items").toString(),AreaBean.class);
                                areaBeanList.addAll(areaBeanListTemp);
                                commonAdapter.notifyDataSetChanged();


                            }else{

                                showToast("请求接口失败，请联系管理员");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                if(responseBody != null){
                    try {
                        String str1 = new String(responseBody);
                        JSONObject jsonObject1 = new JSONObject(str1);
                        showToast(jsonObject1.getString("msg"));

                    }catch (JSONException e){
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
