package com.android.qrcodeclient.Personal.Cell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.base.BaseAppCompatActivity;
import com.android.qrcodeclient.Life.DevelopingActivity;
import com.android.qrcodeclient.Personal.Cell.Complaint.ComplaintActivity;
import com.android.qrcodeclient.Personal.Cell.Warranty.WarrantyActivity;
import com.android.qrcodeclient.R;
import com.android.utils.TextUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jisx on 2016/6/14.
 */
public class CellActivity extends BaseAppCompatActivity implements View.OnClickListener{

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.announcement)
    TextView announcement;

    @Bind(R.id.warranty)
    TextView warranty;

    @Bind(R.id.home_service)
    TextView homeService;

    @Bind(R.id.community_service)
    TextView communityService;

    @Bind(R.id.service_complaint)
    TextView serviceComplaint;

    Bundle bundle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        bundle = new Bundle();

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
    }


    @Override
    public void onClick(View v) {
        finish();
    }

    @OnClick({R.id.announcement, R.id.warranty, R.id.home_service, R.id.community_service, R.id.service_complaint})
    public void onClick(TextView view) {
        switch (view.getId()) {
            case R.id.announcement:
                break;
            case R.id.warranty:
                bundle.putString(getResources().getString(R.string.develop_title), view.getText().toString());
                goNext(WarrantyActivity.class, bundle);
                break;
            case R.id.home_service:
                bundle.putString(getResources().getString(R.string.develop_title), view.getText().toString());
                goNext(DevelopingActivity.class, bundle);
                break;
            case R.id.community_service:
                bundle.putString(getResources().getString(R.string.develop_title), view.getText().toString());
                goNext(DevelopingActivity.class, bundle);
                break;
            case R.id.service_complaint:
                bundle.putString(getResources().getString(R.string.develop_title), view.getText().toString());
                goNext(ComplaintActivity.class, bundle);
                break;
        }
    }
}
