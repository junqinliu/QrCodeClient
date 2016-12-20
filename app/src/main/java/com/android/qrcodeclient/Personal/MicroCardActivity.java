package com.android.qrcodeclient.Personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.base.BaseAppCompatActivity;
import com.android.model.FamilyMicroCardBean;
import com.android.qrcodeclient.R;
import com.android.utils.TextUtil;

import butterknife.Bind;

/**
 * Created by jisx on 2016/6/13.家属微卡
 */
public class MicroCardActivity extends BaseAppCompatActivity implements View.OnClickListener{

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.family_member_name_edit)
    EditText family_member_name_edit;
    @Bind(R.id.family_member_phone_edit)
    EditText family_member_phone_edit;
    @Bind(R.id.family_member_pwd_edit)
    EditText family_member_pwd_edit;
    @Bind(R.id.family_member_start_edit)
    EditText family_member_start_edit;
    @Bind(R.id.family_member_end_edit)
    EditText family_member_end_edit;


    FamilyMicroCardBean familyMicroCardBean = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_card);
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

        familyMicroCardBean = (FamilyMicroCardBean)getIntent().getSerializableExtra("FamilyMicroCardBean");

        if(familyMicroCardBean != null ){

            //初始化数据
            family_member_name_edit.setText(familyMicroCardBean.getFamilyName());
            family_member_phone_edit.setText(familyMicroCardBean.getFamilyPhone());
            family_member_pwd_edit.setText(familyMicroCardBean.getFamilyPwd());
            family_member_start_edit.setText(familyMicroCardBean.getFamilyStart());
            family_member_end_edit.setText(familyMicroCardBean.getFamilyEnd());

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
}
