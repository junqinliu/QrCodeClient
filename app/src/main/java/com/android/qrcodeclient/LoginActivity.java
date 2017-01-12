package com.android.qrcodeclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.application.ExitApplication;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.download.UpdateManger;
import com.android.model.UserInfoBean;
import com.android.qrcodeclient.Card.CardMainActivity;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.SharedPreferenceUtil;
import com.android.utils.TextUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import cn.jpush.android.api.JPushInterface;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class LoginActivity extends BaseAppCompatActivity implements View.OnClickListener {


    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.tv_resiger)
    TextView tv_resiger;
    @Bind(R.id.tv_forgetpassword)
    TextView tv_forgetpassword;

    @Bind(R.id.ed_account)
    EditText ed_account;

    @Bind(R.id.ed_password)
    EditText ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ExitApplication.getInstance().addAllActivity(this);
    }


    @Override
    public void initView() {


    }

    @Override
    public void initData() {

        if (!TextUtil.isEmpty(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("UserInfo", ""))) {

            UserInfoBean userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);
            if (userInfoBean != null && ("PASS".equals(userInfoBean.getAduitstatus()) || "AUDITING".equals(userInfoBean.getAduitstatus()))) {
                Intent intent1 = new Intent(LoginActivity.this, CardMainActivity.class);
                intent1.putExtra("phone", userInfoBean.getPhone());


                startActivity(intent1);

                //极光推送注册
                Set h=new HashSet();
                h.add("house_"+userInfoBean.getHouseid());
                JPushInterface.setAliasAndTags(getApplicationContext(),
                        "user_"+userInfoBean.getUserid(),h,null);
                finish();

                //如果网络可以用的话 线程请求获取用户个人信息的接口
                if (NetUtil.checkNetInfo(LoginActivity.this)) {


                    getUserInfo(userInfoBean.getPhone());
                }

            }
        }


    }

    @Override
    public void setListener() {

        btn_login.setOnClickListener(this);
        tv_resiger.setOnClickListener(this);
        tv_forgetpassword.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            //登录按钮
            case R.id.btn_login:
                //测试app下载更新
              //  new UpdateManger(LoginActivity.this).checkUpdateInfo();

                Login();


                break;

            //注册按钮
            case R.id.tv_resiger:


                Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent2);

                break;

            //忘记密码
            case R.id.tv_forgetpassword:

                Intent intent3 = new Intent(LoginActivity.this, PwdForgetActivity.class);
                startActivity(intent3);

                break;
            default:
                break;


        }
    }

    /**
     * 登录接口
     */
    private void Login() {


        if (TextUtil.isEmpty(ed_account.getText().toString())) {

            showToast("请输入手机号码");

            return;
        }
        if (TextUtil.isEmpty(ed_password.getText().toString())) {

            showToast("请输入密码");

            return;
        }

        RequestParams params = new RequestParams();
        params.put("phone", ed_account.getText().toString());
        params.put("password", ed_password.getText().toString());




        HttpUtil.post( Constants.HOST + Constants.Login, params,  new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetUtil.checkNetInfo(LoginActivity.this)) {

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

                                try {

                                    //如果操作登出，这边重新登录成功后再重新恢复极光推送
                                    JPushInterface.resumePush(getApplicationContext());

                                    UserInfoBean userInfoBean = JSON.parseObject(jsonObject.getJSONObject("data").toString(), UserInfoBean.class);
                                    userInfoBean.setPhone(ed_account.getText().toString());
                                    String userInfoBeanStr = JSON.toJSONString(userInfoBean);
                                    SharedPreferenceUtil.getInstance(LoginActivity.this).putData("UserInfo", userInfoBeanStr);

                                    Intent intent1 = new Intent(LoginActivity.this, CardMainActivity.class);
                                    intent1.putExtra("phone",ed_account.getText().toString());
                                    startActivity(intent1);
                                    finish();
                                }catch (Exception e){
                                    e.printStackTrace();
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
               /* store = JSON.parseObject(jsonObj.getJSONObject("store").toString(),Store.class);*/
                /*list = JSON.parseArray(jsonObj.getJSONArray("data").toString(),OrderListBean.class);*/

                if (responseBody != null) {
                    try {
                        String str1 = new String(responseBody);
                        JSONObject jsonObject1 = new JSONObject(str1);
                        showToast(jsonObject1.getString("msg"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("连接不到服务器,请联系管理员");
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


    /**
     * 获取用户个人信息
     */
    private void getUserInfo(final String phone) {

        RequestParams params = new RequestParams();


        HttpUtil.get(Constants.HOST + Constants.getUserInfo, params, new AsyncHttpResponseHandler() {
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

                                UserInfoBean    userInfoBean = JSON.parseObject(jsonObject.getJSONObject("data").toString(), UserInfoBean.class);
                                if (!TextUtil.isEmpty(phone)) {

                                    userInfoBean.setPhone(phone);
                                }
                                String userInfoBeanStr = JSON.toJSONString(userInfoBean);
                                SharedPreferenceUtil.getInstance(LoginActivity.this).putData("UserInfo", userInfoBeanStr);

                                //配置请求接口全局token 和 userid houseid
                                if (userInfoBean != null) {

                                    HttpUtil.getClient().addHeader("Token", userInfoBean.getToken());
                                    HttpUtil.getClient().addHeader("Userid", userInfoBean.getUserid());
                                    HttpUtil.getClient().addHeader("Houseid", userInfoBean.getHouseid());

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
                      //  showToast(jsonObject1.getString("msg"));

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
