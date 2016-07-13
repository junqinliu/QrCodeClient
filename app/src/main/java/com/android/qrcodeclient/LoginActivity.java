package com.android.qrcodeclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.base.BaseAppCompatActivity;
import com.android.qrcodeclient.Card.CardMainActivity;

import butterknife.Bind;

public class LoginActivity extends BaseAppCompatActivity implements View.OnClickListener{



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

    }


    @Override
    public void initView() {


    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {

        btn_login.setOnClickListener(this);
        tv_resiger.setOnClickListener(this);
        tv_forgetpassword.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

      switch (view.getId()){

        //登录按钮
        case R.id.btn_login:

            Intent intent1 = new Intent(LoginActivity.this, CardMainActivity.class);

             startActivity(intent1);
          break;

        //注册按钮
       case  R.id.tv_resiger:


          Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
          startActivity(intent2);

          break;

        //忘记密码
        case  R.id.tv_forgetpassword:

          Intent intent3 = new Intent(LoginActivity.this, PwdForgetActivity.class);
          startActivity(intent3);

          break;
        default:
          break;


      }
    }
}
