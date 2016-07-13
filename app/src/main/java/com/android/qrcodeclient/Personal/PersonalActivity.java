package com.android.qrcodeclient.Personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.base.BaseAppCompatActivity;
import com.android.qrcodeclient.Personal.Cell.CellActivity;
import com.android.qrcodeclient.R;
import com.android.view.ExitHintDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liujunqin on 2016/5/31.
 */
public class PersonalActivity extends BaseAppCompatActivity implements View.OnClickListener {


    @Bind(R.id.cell)
    TextView cell;
    @Bind(R.id.entrance)
    TextView entrance;
    @Bind(R.id.apply)
    TextView apply;
    @Bind(R.id.micro_card)
    TextView microCard;
    @Bind(R.id.problem)
    TextView problem;
    @Bind(R.id.about)
    TextView about;
    @Bind(R.id.modify_pwd)
    TextView modifyPwd;
    @Bind(R.id.loginout)
    TextView loginout;

    ExitHintDialog exitHintDialog;
    Bundle bundle;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal);
    }


    @Override
    public void initView() {

        title.setText(R.string.life_title);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);

    }

    @Override
    public void initData() {
        bundle = new Bundle();
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);
    }

    @OnClick({R.id.cell, R.id.entrance, R.id.apply, R.id.micro_card, R.id.problem, R.id.about, R.id.modify_pwd, R.id.loginout})
    public void onClick(TextView view) {
        bundle.putString(getResources().getString(R.string.develop_title), view.getText().toString());
        switch (view.getId()) {
            case R.id.cell:
                goNext(CellActivity.class,bundle);
                break;
            case R.id.entrance:
                goNext(EntranceActivity.class,bundle);
                break;
            case R.id.apply:
                goNext(ApplyActivity.class,bundle);
                break;
            case R.id.micro_card:
                goNext(MicroCardActivity.class,bundle);
                break;
            case R.id.problem:
                goNext(FeedBackActivity.class,bundle);
                break;
            case R.id.about:
                goNext(AboutActivity.class,bundle);
                break;
            case R.id.modify_pwd:
                goNext(ModifyPwdActivity.class,bundle);
                break;
            case R.id.loginout:
                if(exitHintDialog == null){
                    exitHintDialog = new ExitHintDialog(PersonalActivity.this);
                }
                exitHintDialog.show();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
