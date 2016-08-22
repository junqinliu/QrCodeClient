package com.android.qrcodeclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.android.application.ExitApplication;

public class StartActivity extends Activity implements View.OnClickListener {

    private static final int GO_HOME = 1000;
    /**
     * 延时时间
     */
    private static final long SPLASH_DELAY_MILLIS = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       // 使用Handler的postDelayed方法，1.5秒后执行跳转到MainActivity
        mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        ExitApplication.getInstance().addAllActivity(this);
    }





    @Override
    public void onClick(View view) {


    }


    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 调到主页
     */
    private void goHome() {

        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
