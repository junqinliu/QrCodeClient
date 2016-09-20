package com.android.qrcodeclient.Life;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
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
public class LifeActivity extends BaseAppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    @Bind(R.id.grid_life)
    GridView gridLife;

    List<LifeItemBean> list;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.send_card_layout)
    LinearLayout send_card_layout;
    @Bind(R.id.log_layout)
    LinearLayout log_layout;
    @Bind(R.id.message_layout)
    LinearLayout message_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_life);
    }



    @Override
    public void initView() {

        title.setText(R.string.life_title);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        /*list.add(new LifeItemBean("给亲友微卡", R.mipmap.fasongweika, SendCardActivity.class));
        list.add(new LifeItemBean("往来日志", R.mipmap.rizhi, LogActivity.class));
        list.add(new LifeItemBean("我的消息", R.mipmap.message, MessageActivity.class));*/

       /* list.add(new LifeItemBean("百度", R.mipmap.baidu2, DevelopingActivity.class));
        list.add(new LifeItemBean("票务", R.mipmap.ticket, DevelopingActivity.class));
        list.add(new LifeItemBean("百度外卖", R.mipmap.baiduwaimai11, DevelopingActivity.class));
        list.add(new LifeItemBean("社区商城", R.mipmap.shangcheng, ComunityMallActivity.class));
        list.add(new LifeItemBean("滴滴打车", R.mipmap.dididache, DevelopingActivity.class));
        list.add(new LifeItemBean("游戏   ", R.mipmap.youxi, DevelopingActivity.class));*/
        list.add(new LifeItemBean("微购", R.mipmap.weigou, null));
        list.add(new LifeItemBean("逛街吧", R.mipmap.guangjieba, null));
        list.add(new LifeItemBean("蘑菇街", R.mipmap.mogujie, null));
        list.add(new LifeItemBean("微卖", R.mipmap.weimai, null));
        list.add(new LifeItemBean("喵喵微店", R.mipmap.miaomiaoweidian, null));
        list.add(new LifeItemBean("拍拍小店", R.mipmap.paipai, null));
        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, null));
        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, null));
        list.add(new LifeItemBean("门店招商", R.mipmap.addshop, null));
        gridLife.setAdapter(new LifeAdapter(this, list));
    }

    @Override
    public void setListener() {

        gridLife.setOnItemClickListener(this);
     //   toolbar.setNavigationOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_card_layout.setOnClickListener(this);
        log_layout.setOnClickListener(this);
        message_layout.setOnClickListener(this);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.send_card_layout:

                startActivity(new Intent(LifeActivity.this,SendCardActivity.class));

                break;
            case R.id.log_layout:

                startActivity(new Intent(LifeActivity.this,LogActivity.class));

                break;
            case R.id.message_layout:

                startActivity(new Intent(LifeActivity.this,MessageActivity.class));

                break;

        }

       // finish();
    }
}
