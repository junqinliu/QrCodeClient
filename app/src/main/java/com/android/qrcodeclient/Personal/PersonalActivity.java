package com.android.qrcodeclient.Personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.application.ExitApplication;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.qrcodeclient.Personal.Cell.CellActivity;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.android.view.ExitHintDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

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
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);
    }

    @OnClick({R.id.cell, R.id.entrance, R.id.apply, R.id.micro_card, R.id.problem, R.id.about, R.id.modify_pwd, R.id.loginout})
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
                goNext(MicroCardActivity.class,bundle);
                break;

            //问题反馈
            case R.id.problem:
                goNext(FeedBackActivity.class,bundle);
                break;

            //关于微卡
            case R.id.about:
                goNext(AboutActivity.class,bundle);
                break;

            //修改密码
            case R.id.modify_pwd:
                goNext(ModifyPwdActivity.class,bundle);
                break;

            //退出登录
            case R.id.loginout:
                if(exitHintDialog == null){
                    exitHintDialog = new ExitHintDialog(PersonalActivity.this);
                }
                exitHintDialog.show();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }


    /**
     * 注销方法
     */
    private void Logout(){

        RequestParams params = new RequestParams();
        params.put("userid", "");

        HttpUtil.post(Constants.HOST + Constants.LoginOut, params, new AsyncHttpResponseHandler() {
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
