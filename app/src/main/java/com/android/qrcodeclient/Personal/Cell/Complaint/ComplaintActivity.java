package com.android.qrcodeclient.Personal.Cell.Complaint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.android.utils.TextUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by jisx on 2016/6/13.
 */
public class ComplaintActivity extends BaseAppCompatActivity implements View.OnClickListener{

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.complaint_content_edt)
    EditText complaint_content_edt;

    @Bind(R.id.complaint_submit_btn)
    EditText complaint_submit_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //设置标题
        String titleName = getIntent().getStringExtra(getResources().getString(R.string.develop_title));
        if (!TextUtil.isEmpty(titleName)) {
            title.setText(titleName);
        }
    }

    @Override
    public void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        complaint_submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            //提交
            case R.id.complaint_submit_btn:

                if(TextUtil.isEmpty(complaint_content_edt.getText().toString())){

                    showToast("请输入具体原因");
                    return;

                }
                break;
            default:
                break;
        }

    }

    /**
     * 服务投诉提交方法
     */
    private  void submitComplaintContent(){

        RequestParams params = new RequestParams();
        params.put("title", "");
        params.put("content", "");
        params.put("userid", "");
        params.put("propertytype", "REPAIR");


        HttpUtil.post(Constants.HOST + Constants.Property, params, new AsyncHttpResponseHandler() {
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
