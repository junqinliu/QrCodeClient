package com.android.qrcodeclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.utils.HttpUtil;
import com.android.utils.TextUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import cz.msebera.android.httpclient.entity.StringEntity;

public class PwdForgetNextActivity extends BaseAppCompatActivity implements View.OnClickListener{



    @Bind(R.id.toolbar)
    Toolbar toolBar;
    @Bind(R.id.title)
    TextView toolbar_title;
    String phone;
    @Bind(R.id.mphone)
    EditText mphone;

    @Bind(R.id.btn_finsh)
    Button btn_finsh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd_next);

    }


    @Override
    public void initView() {

        toolBar.setTitle("");
        toolbar_title.setText(R.string.forget_pwd_title_next);
        setSupportActionBar(toolBar);
        toolBar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        btn_finsh.setOnClickListener(this);

    }

    @Override
    public void initData() {

        phone = getIntent().getStringExtra("phone");
    }

    @Override
    public void setListener() {

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }


    @Override
    public void onClick(View view) {

      switch (view.getId()){

        //注册按钮
        case R.id.btn_finsh:

            if(TextUtil.isEmpty(mphone.getText().toString())){

                showToast("请输入密码");
                return;
            }

            ForgetPassWord();
         /* Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
          startActivity(intent);*/
         /* finish();*/
          break;
        default:
          break;


      }
    }

    /**
     * 修改密码
     */
    private void ForgetPassWord(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phone);
            jsonObject.put("password",mphone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonObject.toString());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpUtil.put(PwdForgetNextActivity.this, Constants.HOST + Constants.ForgetPassword, entity, "application/json", new AsyncHttpResponseHandler() {
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

                                showToast("新密码提交成功");
                                Intent mIntent = new Intent();
                                setResult(RESULT_OK, mIntent);
                                finish();
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

            }


        });

    }



}
