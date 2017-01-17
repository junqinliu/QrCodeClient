package com.android.qrcodeclient.Personal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.application.ExitApplication;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.UserInfoBean;
import com.android.qrcodeclient.LoginActivity;
import com.android.qrcodeclient.Personal.Cell.CellActivity;
import com.android.qrcodeclient.R;
import com.android.timeTask.AlarmManagerTask;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.SharedPreferenceUtil;
import com.android.utils.TextUtil;
import com.android.view.ExitHintDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by liujunqin on 2016/5/31.
 */
public class PersonalActivity extends BaseAppCompatActivity implements View.OnClickListener {


    @Bind(R.id.cell)
    TextView cell;
    @Bind(R.id.entrance)
    TextView entrance;
    @Bind(R.id.apply)
    TextView apply;
    @Bind(R.id.micro_card)
    TextView microCard;
    @Bind(R.id.problem)
    TextView problem;
    @Bind(R.id.about)
    TextView about;
    @Bind(R.id.tel)
    TextView tel;
    @Bind(R.id.modify_pwd)
    TextView modifyPwd;
    @Bind(R.id.loginout)
    TextView loginout;

    ExitHintDialog exitHintDialog;
    Bundle bundle;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.apply_layout)
    LinearLayout apply_layout;
    @Bind(R.id.card_family_layout)
    LinearLayout card_family_layout;

     UserInfoBean userInfoBean = new UserInfoBean();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal);
    }


    @Override
    public void initView() {

        title.setText(R.string.personal_title);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        ExitApplication.getInstance().addActivity(this);

    }

    @Override
    public void initData() {
        bundle = new Bundle();
        userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);
        if(userInfoBean != null){

            //是家属身份  没有门禁申请和家属微卡的权限
            if("ROLE_FAMILY".equals(userInfoBean.getAuthority())){

                apply_layout.setVisibility(View.GONE);
                card_family_layout.setVisibility(View.GONE);
            }

        }

        //获取电话号码
        getPhone();
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);
    }

    @OnClick({R.id.cell, R.id.entrance, R.id.apply, R.id.micro_card, R.id.problem, R.id.about,R.id.tel, R.id.modify_pwd, R.id.loginout})
    public void onClick(TextView view) {
        bundle.putString(getResources().getString(R.string.develop_title), view.getText().toString());
        switch (view.getId()) {

            //我的小区
            case R.id.cell:
                goNext(CellActivity.class,bundle);
                break;

            //我的门禁
            case R.id.entrance:
                goNext(EntranceActivity.class,bundle);
                break;

            //门禁申请
            case R.id.apply:

                goNext(ApplyActivity.class,bundle);

                break;

            //家属微卡
            case R.id.micro_card:
                //goNext(MicroCardActivity.class,bundle);
                goNext(FamilyMicroActivity.class,bundle);
                break;

            //使用说明
            case R.id.problem:
                goNext(FeedBackActivity.class,bundle);

//测试崩溃异常报文提交
//                List<String> list = null;
//                showToast(list.get(0));


                break;

            //关于微卡
            case R.id.about:
                goNext(AboutActivity.class,bundle);
                break;
            case R.id.tel:

                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setTitle("提示")
                        .setMessage("确定拨打？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", phonedialogListener).create().show();

                break;

            //修改密码
            case R.id.modify_pwd:
                goNext(ModifyPwdActivity.class,bundle);
                break;

            //退出登录
            case R.id.loginout:

                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setTitle("提示")
                        .setMessage("确定退出？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", dialogListener).create().show();



                break;
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }


    // 退出提示框按钮监听
    android.content.DialogInterface.OnClickListener dialogListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

           //调用退出登录接口
            //Logout();
            showToast("退出登录成功");
            //跳转到登录接口 并且把本地文件的数据清除掉
            Intent intent = new Intent(PersonalActivity.this, LoginActivity.class);
            startActivity(intent);
            SharedPreferenceUtil.getInstance(PersonalActivity.this).deleteData();
            ExitApplication.getInstance().exitActivity();

            //停止定时任务
            AlarmManagerTask.getInstance(PersonalActivity.this).cancelAlarmManager();

            //停止极光推送
            JPushInterface.stopPush(getApplicationContext());

        }
    };
    // 拨打电话提示框按钮监听
    android.content.DialogInterface.OnClickListener phonedialogListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if(!TextUtil.isEmpty(tel.getText().toString())){

                //用intent启动拨打电话
                Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel.getText().toString()));
                startActivity(intent1);
            }



        }
    };


    /**
     * 获取电话号码
     */

    public void getPhone(){


        RequestParams params = new RequestParams();


        HttpUtil.get(Constants.HOST + Constants.GetPhone, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetUtil.checkNetInfo(PersonalActivity.this)) {

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
                                tel.setText( "null".equals(jsonObject.getString("data")) || jsonObject.getString("data") == null? "":jsonObject.getString("data"));
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

    /**
     * 注销方法
     */
    private void Logout(){


        ByteArrayEntity entity = null;
        HttpUtil.post(PersonalActivity.this,Constants.HOST + Constants.LoginOut, entity,"application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if(!NetUtil.checkNetInfo(PersonalActivity.this)){

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

                            if(jsonObject.getBoolean("success")){

                                showToast("退出登录成功");
                                //跳转到登录接口 并且把本地文件的数据清除掉
                                Intent intent = new Intent(PersonalActivity.this, LoginActivity.class);
                                startActivity(intent);
                                SharedPreferenceUtil.getInstance(PersonalActivity.this).deleteData();
                                ExitApplication.getInstance().exitActivity();

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
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

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
