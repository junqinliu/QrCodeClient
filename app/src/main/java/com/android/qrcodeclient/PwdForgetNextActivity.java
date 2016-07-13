package com.android.qrcodeclient;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.base.BaseAppCompatActivity;

import butterknife.Bind;

public class PwdForgetNextActivity extends BaseAppCompatActivity implements View.OnClickListener{



    @Bind(R.id.toolbar)
    Toolbar toolBar;
    @Bind(R.id.title)
    TextView toolbar_title;

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

    }

    @Override
    public void initData() {

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
       /* case R.id.btn_login:

          Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
          startActivity(intent);
          finish();
          break;*/
        default:
          break;


      }
    }
}
