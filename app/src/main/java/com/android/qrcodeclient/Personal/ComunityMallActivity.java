package com.android.qrcodeclient.Personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.adapter.ComunityMallAdapter;
import com.android.adapter.LifeAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.model.LifeItemBean;
import com.android.qrcodeclient.R;
import com.android.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liujunfeng on 2016/9/18.
 */
public class ComunityMallActivity extends BaseAppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.grid_life)
    GridView gridLife;

    List<LifeItemBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comunity_mall);
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

        list = new ArrayList<>();
        list.add(new LifeItemBean("微购", R.mipmap.weigou, null));
        list.add(new LifeItemBean("逛街吧", R.mipmap.guangjieba, null));
        list.add(new LifeItemBean("蘑菇街", R.mipmap.mogujie, null));
        list.add(new LifeItemBean("微卖", R.mipmap.weimai, null));
        list.add(new LifeItemBean("喵喵微店", R.mipmap.miaomiaoweidian, null));
        list.add(new LifeItemBean("拍拍小店", R.mipmap.paipai, null));
        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, null));
        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, null));
        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, null));
        gridLife.setAdapter(new ComunityMallAdapter(this, list));
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);
        gridLife.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class cls = list.get(position).getCls();
        if (cls != null) {
            Bundle bundle = new Bundle();
            bundle.putString(getResources().getString(R.string.develop_title), list.get(position).getItemName());
            goNext(cls, bundle);
        } else {
            showToast("暂未开放");
        }
    }
}
