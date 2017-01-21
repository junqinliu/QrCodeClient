package com.android.qrcodeclient.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.application.AppContext;
import com.android.application.ExitApplication;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.AddressBean;
import com.android.model.CBBean;
import com.android.model.KeyAddressBean;
import com.android.model.UserInfoBean;
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

import java.io.UnsupportedEncodingException;
import java.util.logging.Handler;

import butterknife.Bind;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by jisx on 2016/6/13.
 */
public class ApplyActivity extends BaseAppCompatActivity implements View.OnClickListener{

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_text)
    EditText search_text;
    @Bind(R.id.search_button)
    ImageView search_button;
    @Bind(R.id.provice)
    EditText provice;
    @Bind(R.id.city)
    EditText city;
    @Bind(R.id.zone)
    EditText zone;
    @Bind(R.id.street)
    EditText street;
    @Bind(R.id.xiaoqu)
    EditText xiaoqu;
    @Bind(R.id.block)
    EditText block;
    @Bind(R.id.floor)
    EditText floor;
    @Bind(R.id.house_num)
    EditText house_num;
    @Bind(R.id.user_phone)
    EditText user_phone;
    @Bind(R.id.user_name)
    EditText user_name;

    @Bind(R.id.owner_phone_num_edit)
    EditText owner_phone_num_edit;

    @Bind(R.id.submit_apply_btn)
    Button submit_apply_btn;

    AddressBean addressBean;
    CBBean cBBean;
    KeyAddressBean keyAddressBean;
    AppContext myApplicaton;
    String houseid = "";
    String buildid = "";
    String buildfloor = "";
    String hasSelectBuildfloor = "";

    private String flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //设置标题
        title.setText(getResources().getString(R.string.access_control_application));
        flag = getIntent().getStringExtra("flag");

        UserInfoBean   userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);
        if("register".equals(flag)){

            //表示是注册界面进来

            //配置请求接口全局token 和 userid
            if (userInfoBean != null) {

                HttpUtil.getClient().addHeader("Token", userInfoBean.getToken());
                HttpUtil.getClient().addHeader("Userid", userInfoBean.getUserid());
                //HttpUtil.getClient().addHeader("Houseid","1");

            }
        }

        user_phone.setText(userInfoBean.getPhone());
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
        search_button.setOnClickListener(this);
        provice.setOnClickListener(this);
        xiaoqu.setOnClickListener(this);
        floor.setOnClickListener(this);
        submit_apply_btn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myApplicaton = (AppContext)getApplication();
        addressBean = myApplicaton.getAddressBean();
        if(addressBean != null ){

            provice.setText(addressBean.getProvinceName());
            city.setText(addressBean.getCityName());
            zone.setText(addressBean.getAreaName());

        }

        cBBean = myApplicaton.getcBBean();
        if(cBBean != null){

            xiaoqu.setText(cBBean.getAreaName());
            block.setText(cBBean.getName());
            houseid = cBBean.getAreaId();
            buildid = cBBean.getBuildid();
            buildfloor = cBBean.getBuildfloor();
        }

        keyAddressBean = myApplicaton.getKeyAddressBean();

        if(keyAddressBean != null){

            provice.setText(keyAddressBean.getProvice());
            city.setText(keyAddressBean.getCity());
            zone.setText(keyAddressBean.getArea());
            xiaoqu.setText(keyAddressBean.getHouseName());
            block.setText(keyAddressBean.getBuildname());
            houseid = keyAddressBean.getHouseId();
            buildid = keyAddressBean.getBuildid();
            buildfloor = keyAddressBean.getBuildfloor();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
         addressBean = null;
         cBBean = null;
        keyAddressBean = null;
        myApplicaton.setAddressBean(addressBean);
        myApplicaton.setcBBean(cBBean);
        myApplicaton.setKeyAddressBean(keyAddressBean);

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){

           //关键字查询
           case R.id.search_button:

               if(TextUtil.isEmpty(search_text.getText().toString())){

                   showToast("请输入小区名称");
                   return;
               }

               //清空上次所选的地址和小区楼栋
               addressBean = null;
               cBBean = null;
               myApplicaton.setAddressBean(addressBean);
               myApplicaton.setcBBean(cBBean);
               Intent intent1 = new Intent(this,KeySearchCommunityActivity.class);
               intent1.putExtra("queryword",search_text.getText().toString());
               startActivity(intent1);
               clearData();
               break;

           //获取省份信息
           case R.id.provice:

               //清空上次的关键字查询
               keyAddressBean = null;
               myApplicaton.setKeyAddressBean(keyAddressBean);
               startActivity(new Intent(this,ProviceActivity.class));
               clearData();

               break;

           //获取小区列表
           case R.id.xiaoqu:

               if(TextUtil.isEmpty(provice.getText().toString())){

                   showToast("请先选省份");
                   return;
               }



               if(addressBean != null){

                   if(!TextUtil.isEmpty(addressBean.getAreaCode())){

                       Intent intent = new Intent(this,CommunityActivity.class);
                       intent.putExtra("areacode",addressBean.getAreaCode());
                       startActivity(intent);
                   }


               }
               break;

           //获取楼层号列表
           case R.id.floor:

               if(!TextUtil.isEmpty(buildfloor)){

                   Intent intent2 = new Intent(this,BuildFloorActivity.class);
                   intent2.putExtra("buildfloor",buildfloor);
                   startActivityForResult(intent2,2000);

               }else{

                   showToast("没有楼层号，不能申请门禁");

               }

               break;

           //提交申请
           case R.id.submit_apply_btn:

              // submit();
               if(TextUtil.isEmpty(houseid)){
                   showToast("请选择小区");
                   return;
               }
               if(TextUtil.isEmpty(buildid)){
                   showToast("请选择楼栋");
                   return;
               }
               if(TextUtil.isEmpty(user_phone.getText().toString())){

                   showToast("请输入手机");
                   return;
               }
               if(TextUtil.isEmpty(user_name.getText().toString())){

                   showToast("请输入姓名");
                   return;
               }


               cardApply( buildid, hasSelectBuildfloor);
               break;

           default:
               break;
       }
    }


    /**
     * 提交申请  (废弃掉 已经不用这个接口)
     */

    private  void submit(){


        if(TextUtil.isEmpty(houseid)){
            showToast("请选择小区");
            return;
        }
        if(TextUtil.isEmpty(buildid)){
            showToast("请选择楼栋");
            return;
        }
        if(TextUtil.isEmpty(user_phone.getText().toString())){

            showToast("请输入手机");
            return;
        }
        if(TextUtil.isEmpty(user_name.getText().toString())){

            showToast("请输入姓名");
            return;
        }
        if(TextUtil.isEmpty(owner_phone_num_edit.getText().toString())){

            showToast("请输入业主号码");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("houseid",houseid);
            jsonObject.put("buildid",buildid);
            jsonObject.put("name",user_name.getText().toString());
            jsonObject.put("sex","1");
            jsonObject.put("ownerphone",owner_phone_num_edit.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpUtil.post(ApplyActivity.this, Constants.HOST + Constants.submitCardApply, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if(!NetUtil.checkNetInfo(ApplyActivity.this)){

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

                               // showToast("提交成功");
                                //门卡申请提交成功后，改变本地文件userinfobean中的状态变为审核
                                UserInfoBean   userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(ApplyActivity.this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);
                               // userInfoBean.setAduitstatus("AUDITING");
                                userInfoBean.setAduitstatus("PASS");
                                String  userInfoBeanStr = JSON.toJSONString(userInfoBean);
                                SharedPreferenceUtil.getInstance(ApplyActivity.this).putData("UserInfo", userInfoBeanStr);

                                //去调用微卡申请接口
                                //cardApply();

//                                if("register".equals(flag)){
//                                   Intent intent = new Intent(ApplyActivity.this, CardMainActivity.class);
//                                    startActivity(intent);
//                                    ExitApplication.getInstance().exitAll();
//                                }else{
//                                    finish();
//                                }


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

                closeLoadDialog();
            }


        });


    }

    private void clearData(){

        provice.setText("");
        city.setText("");
        zone.setText("");
        xiaoqu.setText("");
        block.setText("");
        floor.setText("");
    }


    /**
     * 微卡申请接口
     */
    private void cardApply(String buildid,String floor){


        if(TextUtil.isEmpty(houseid)){
            showToast("请选择小区");
            return;
        }
        if(TextUtil.isEmpty(buildid)){
            showToast("请选择楼栋");
            return;
        }
        if(TextUtil.isEmpty(user_phone.getText().toString())){

            showToast("请输入手机");
            return;
        }
        if(TextUtil.isEmpty(user_name.getText().toString())){

            showToast("请输入姓名");
            return;
        }


        RequestParams params = new RequestParams();
        params.put("buildid",buildid);
        params.put("floor",floor);
        params.put("surname",user_name.getText().toString());



        HttpUtil.post(Constants.HOST + Constants.CardApply, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetUtil.checkNetInfo(ApplyActivity.this)) {

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

                                showToast("提交成功");
                                //去调用微卡申请接口

                                if ("register".equals(flag)) {
                                    Intent intent = new Intent(ApplyActivity.this, CardMainActivity.class);
                                    startActivity(intent);
                                    ExitApplication.getInstance().exitAll();
                                } else {
                                    finish();
                                }


                            } else {

                                if("0".equals(jsonObject.getString("code"))){

                                    DialogMessageExit.getInstance(ApplyActivity.this).showDialog();


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

            }


        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2000){

            if(resultCode == 1000){

                hasSelectBuildfloor = data.getExtras().getString("hasSelectFloor");
                floor.setText(hasSelectBuildfloor);
            }
        }

    }
}
