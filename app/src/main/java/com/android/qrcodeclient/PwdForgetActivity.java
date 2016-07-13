package com.android.qrcodeclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.base.BaseAppCompatActivity;
import com.android.utils.TextUtil;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class PwdForgetActivity extends BaseAppCompatActivity implements View.OnClickListener {


    @Bind(R.id.toolbar)
    Toolbar toolBar;
    @Bind(R.id.title)
    TextView toolbar_title;
    @Bind(R.id.btn_Next)
    Button btn_Next;
    @Bind(R.id.btn_get)
    Button btn_get;
    @Bind(R.id.user_phone)
    EditText user_phone;
    @Bind(R.id.code)
    EditText code;
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    String userPhone = "";
    TimeCount time;
    EventHandler eventHandler;
    MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

    }


    @Override
    public void initView() {

        toolBar.setTitle("");
        toolbar_title.setText(R.string.forget_pwd_title);
        setSupportActionBar(toolBar);
        toolBar.setNavigationIcon(android.R.drawable.ic_menu_revert);

    }

    @Override
    public void initData() {

        handler = new MyHandler(this);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = SMSDDK_HANDLER;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void setListener() {

        btn_get.setOnClickListener(this);
        btn_Next.setOnClickListener(this);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //获取验证
            case R.id.btn_get:

                userPhone = user_phone.getText().toString();

                if (TextUtil.isEmpty(userPhone)) {

                    showToast("请输入手机号");
                    return;
                }
                new AlertDialog.Builder(PwdForgetActivity.this)
                        .setTitle("发送短信")
                        .setMessage("我们将把验证码发送到以下号码:\n" + "+86:" + userPhone)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SMSSDK.getVerificationCode("86", userPhone);
                                btn_get.setClickable(false);
                                time.start();

                            }
                        })
                        .create()
                        .show();
                break;
            //注册按钮
            case R.id.btn_Next:

                if (TextUtil.isEmpty(user_phone.getText().toString()) || TextUtil.isEmpty(code.getText().toString())) {

                    showToast("请完善信息");
                    return;
                }
                //先去提交验证到mob平台验证，其次跳转界面
                SMSSDK.submitVerificationCode("86", userPhone, code.getText().toString());//对验证码进行验证->回调函数

                break;

            default:
                break;


        }
    }


    class MyHandler extends Handler {

        WeakReference<PwdForgetActivity> mActivity;

        MyHandler(PwdForgetActivity activity) {
            mActivity = new WeakReference<PwdForgetActivity>(activity);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SMSDDK_HANDLER:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    //获取验证码和提交验证的共同回调处理函数
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //验证码验证成功
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            Toast.makeText(PwdForgetActivity.this, "验证成功", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(PwdForgetActivity.this, PwdForgetNextActivity.class);
                            startActivity(intent);
                        }
                        //已发送验证码
                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            Toast.makeText(getApplicationContext(), "验证码已经发送",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    //失败
                    if (result == SMSSDK.RESULT_ERROR) {
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(getApplicationContext(), des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            //do something
                        }
                    }
                    break;
            }
        }
    }


    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            if (btn_get != null) {

                btn_get.setText("获取验证码");
                btn_get.setClickable(true);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            if (btn_get != null) {

                btn_get.setClickable(false);
                btn_get.setText("重新发送(" + --millisUntilFinished / 1000 + "s)");
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);

    }

}
