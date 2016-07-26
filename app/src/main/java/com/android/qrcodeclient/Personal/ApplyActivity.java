package com.android.qrcodeclient.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.application.AppContext;
import com.android.base.BaseAppCompatActivity;
import com.android.model.AddressBean;
import com.android.model.CBBean;
import com.android.qrcodeclient.R;
import com.android.utils.TextUtil;

import java.util.logging.Handler;

import butterknife.Bind;

/**
 * Created by jisx on 2016/6/13.
 */
public class ApplyActivity extends BaseAppCompatActivity implements View.OnClickListener{

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.provice)
    EditText provice;
    @Bind(R.id.city)
    EditText city;
    @Bind(R.id.zone)
    EditText zone;
    @Bind(R.id.street)
    EditText street;
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

    AddressBean addressBean;
    CBBean cBBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
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
        toolbar.setNavigationOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        provice.setOnClickListener(this);
        xiaoqu.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppContext myApplicaton = (AppContext)getApplication();
        addressBean = myApplicaton.getAddressBean();
        if(addressBean != null ){
            provice.setText(addressBean.getProvinceName());
            city.setText(addressBean.getCityName());
            zone.setText(addressBean.getAreaName());

        }

        cBBean = myApplicaton.getcBBean();
        if(cBBean != null){

            xiaoqu.setText(cBBean.getAreaName());
            block.setText(cBBean.getName());
        }
    }



    @Override
    public void onClick(View v) {
       switch (v.getId()){

           //获取省份信息
           case R.id.provice:

                 startActivity(new Intent(this,ProviceActivity.class));

               break;

           //获取小区列表
           case R.id.xiaoqu:

               startActivity(new Intent(this,CommunityActivity.class));
               break;

           default:
               break;
       }
    }
}
