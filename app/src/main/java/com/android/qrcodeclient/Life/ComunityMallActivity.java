package com.android.qrcodeclient.Life;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.adapter.LifeAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.model.LifeItemBean;
import com.android.qrcodeclient.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liujunqin on 2016/5/31.
 */
public class ComunityMallActivity extends BaseAppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    @Bind(R.id.grid_life)
    GridView gridLife;

    List<LifeItemBean> list;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_life);
    }



    @Override
    public void initView() {

        title.setText(R.string.mall_title);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        list.add(new LifeItemBean("微购", R.mipmap.weigou, SendCardActivity.class));
        list.add(new LifeItemBean("逛街吧", R.mipmap.guangjieba, LogActivity.class));
        list.add(new LifeItemBean("蘑菇街", R.mipmap.mogujie, MessageActivity.class));
        list.add(new LifeItemBean("微卖", R.mipmap.weimai, DevelopingActivity.class));
        list.add(new LifeItemBean("喵喵微店", R.mipmap.miaomiaoweidian, DevelopingActivity.class));
        list.add(new LifeItemBean("拍拍小店", R.mipmap.paipai, DevelopingActivity.class));
        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, DevelopingActivity.class));
        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, DevelopingActivity.class));
        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, DevelopingActivity.class));
      //  gridLife.setAdapter(new LifeAdapter(this, list));

    }

    @Override
    public void setListener() {
        gridLife.setOnItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        showToast("暂未开放");
//        Class cls = list.get(position).getCls();
//        if (cls != null) {
//            Bundle bundle = new Bundle();
//            bundle.putString(getResources().getString(R.string.develop_title), list.get(position).getItemName());
//            goNext(cls, bundle);
//        } else {
//            showToast("暂未开放");
//        }
    }


    @Override
    public void onClick(View view) {
        finish();
    }
}
