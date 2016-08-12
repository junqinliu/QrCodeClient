package com.android.qrcodeclient.Life;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.TextUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by jisx on 2016/6/13.
 */
public class SendCardActivity extends BaseAppCompatActivity implements View.OnClickListener{

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.address_edit)
    EditText address_edit;
    @Bind(R.id.name_edit)
    EditText name_edit;
    @Bind(R.id.uesr_phone_edit)
    EditText uesr_phone_edit;
    @Bind(R.id.submit_button)
    Button submit_button;

    String buildid = "";
    String buildname = "";
    String housename = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_card);
    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        buildid = intent.getStringExtra("buildid");
        buildname = intent.getStringExtra("buildname");
        if(!TextUtil.isEmpty(buildname)){

            address_edit.setText(buildname);
        }

    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        title.setText(R.string.send_card_title);
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
        address_edit.setOnClickListener(this);
        submit_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){

           //选择来访地址
           case R.id.address_edit:

               Intent intent = new Intent(this,ComeAddressActivity.class);
               startActivityForResult(intent,100);

               break;

           //生成串门微卡
           case R.id.submit_button:

               submitInfo();

               break;

       }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){

            if(resultCode == 200){

                buildid = data.getStringExtra("buildid");
                buildname = data.getStringExtra("buildname");
                housename = data.getStringExtra("housename");
                address_edit.setText(housename+buildname);
            }
        }

    }

    /**
     * 生成串门微卡
     */
    private  void submitInfo(){


        if(TextUtil.isEmpty(buildid)){

            showToast("请输入地址");
            return;
        }
        if(TextUtil.isEmpty(name_edit.getText().toString())){

            showToast("请输入名字");
            return;
        }
        if(TextUtil.isEmpty(uesr_phone_edit.getText().toString())){

            showToast("请输入电话号码");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("invitename",name_edit.getText().toString());
            jsonObject.put("buildid",buildid);
            jsonObject.put("invitephone",uesr_phone_edit.getText().toString());
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

        HttpUtil.post(SendCardActivity.this, Constants.HOST + Constants.Invite, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetUtil.checkNetInfo(SendCardActivity.this)) {

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
                                shareToPlatForm();
                                // finish();
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


    /**
     *  分享到qq 微信 短信
     *
     */

    private void shareToPlatForm(){

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle("微卡");
        oks.setText("微卡");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if(!TextUtil.isEmpty(getOutputMediaFile()+"MicroCode.png")){
            oks.setImagePath(getOutputMediaFile()+"MicroCode.png");//确保SDcard下面存在此张图片
        }

        oks.show(this);


    }


    /**
     * 将图片保存到本地文件中
     */
    private String getOutputMediaFile() {

        //get the mobile Pictures directory   /storage/emulated/0/Pictures/IMAGE_20160315_134742.jpg
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if(!picDir.exists()){
            picDir.mkdir();
        }
        String str = picDir.getPath() + File.separator;
        return str;
    }

}
