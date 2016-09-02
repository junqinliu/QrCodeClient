package com.android.qrcodeclient.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.UserInfoBean;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.SharedPreferenceUtil;
import com.android.utils.TextUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import butterknife.Bind;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by jisx on 2016/6/13.
 */
public class ApplyOtherActivity extends BaseAppCompatActivity implements View.OnClickListener{

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.xiaoqu)
    EditText xiaoqu;
    @Bind(R.id.block)
    EditText block;
    @Bind(R.id.unit)
    EditText unit;
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
    String houseid = "";
    String buildid = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_other);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //设置标题
        title.setText(getResources().getString(R.string.access_control_application));


        UserInfoBean   userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);
        user_phone.setText(userInfoBean.getPhone());
        xiaoqu.setText(userInfoBean.getHousename());
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

        block.setOnClickListener(this);
        submit_apply_btn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){




           //获取楼栋列表
           case R.id.block:

               Intent intent = new Intent(this,CommunityBlockOtherActivity.class);
               startActivityForResult(intent,3000);

               break;

           //提交申请
           case R.id.submit_apply_btn:

               submit();

               break;

           default:
               break;
       }
    }


    /**
     * 提交申请
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

        cardApply();


    }



    /**
     * 微卡申请接口
     */
    private void cardApply(){


        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("buildid",buildid);

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



        HttpUtil.post(ApplyOtherActivity.this, Constants.HOST + Constants.submitCardApply, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if(!NetUtil.checkNetInfo(ApplyOtherActivity.this)){

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3000){

            if(resultCode == 4000){

                buildid = data.getStringExtra("buildid");
                block.setText(data.getStringExtra("buildname"));
            }

        }

    }
}
