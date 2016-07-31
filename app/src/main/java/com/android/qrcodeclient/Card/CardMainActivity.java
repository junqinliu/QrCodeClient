package com.android.qrcodeclient.Card;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.adapter.BlockAdapter;
import com.android.application.ExitApplication;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.AdBean;
import com.android.model.BannerItemBean;
import com.android.model.BlockBean;
import com.android.model.EntranceBean;
import com.android.model.ProviceBean;
import com.android.model.UserInfoBean;
import com.android.notify.PushService;
import com.android.notify.ServiceUtil;
import com.android.qrcodeclient.Life.LifeActivity;
import com.android.qrcodeclient.Life.SendCardActivity;
import com.android.qrcodeclient.Personal.ApplyActivity;
import com.android.qrcodeclient.Personal.PersonalActivity;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.SharedPreferenceUtil;
import com.android.utils.TextUtil;
import com.android.utils.Utils;
import com.android.view.SimpleImageBanner;
import com.android.view.SquareImageView;
import com.flyco.banner.anim.select.RotateEnter;
import com.flyco.banner.anim.unselect.NoAnimExist;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by liujunqin on 2016/5/31.
 */
public class CardMainActivity extends BaseAppCompatActivity implements View.OnClickListener {

    SimpleImageBanner advert;

    @Bind(R.id.binaryCode)
    SquareImageView binaryCode;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.life_layout)
    LinearLayout life_layout;
    @Bind(R.id.card_layout)
    LinearLayout card_layout;
    @Bind(R.id.my_layout)
    LinearLayout my_layout;
    @Bind(R.id.add_img)
    ImageView add_img;


    int pageNumber = 0;
    int pageSize = 30;
    List<EntranceBean> list = new ArrayList<>();
    List<EntranceBean> listTemp = new ArrayList<>();
    String buildname = "";//表示选中的楼栋名称 用来与 点击获取微卡获取的最新楼栋列表做比较 更新二维码
    String buildid = "";


    public String[] titles;
    public String[] urls;

    public BlockAdapter blockAdapter;
    private UserInfoBean userInfoBean = new UserInfoBean();
    List<AdBean> adBeanList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_card);

        //开启闹钟定时任务
        //ServiceUtil.invokeTimerPOIService(this);
    }

    @Override
    public void initView() {
        ExitApplication.getInstance().addActivity(this);
        advert = (SimpleImageBanner) this.findViewById(R.id.advert);
        add_img.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

        userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);

        //配置请求接口全局token 和 userid
        if (userInfoBean != null) {

            HttpUtil.getClient().addHeader("Token", userInfoBean.getToken());
            HttpUtil.getClient().addHeader("Userid", userInfoBean.getUserid());

        }

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        title.setText(R.string.card_title);

        //广告
       /* advert.setSelectAnimClass(RotateEnter.class)
                .setUnselectAnimClass(NoAnimExist.class)
                .setSource(adBeanList)
                .startScroll();*/

        //从我的模块中我的门禁列表中选中的值回填
        String secret = getIntent().getStringExtra("secret");
        if (!TextUtil.isEmpty(secret)) {
            //从我的门禁列表进来
            buildname = getIntent().getStringExtra("buildname");
            buildid = getIntent().getStringExtra("buildid");
            showToast(secret);
            binaryCode.setImageBitmap(Utils.createQRImage(this, secret, 500, 500));
        } else {

            //从登陆界面进来 获取我的门禁列表(就是把上次二维码保存到share文件中再取出来用)
            if (!TextUtil.isEmpty(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("EntranceBean", ""))) {

                EntranceBean EntranceBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("EntranceBean", ""), EntranceBean.class);
                buildname = EntranceBean.getBuildname();
                buildid = EntranceBean.getBuildid();
                binaryCode.setImageBitmap(Utils.createQRImage(this, EntranceBean.getSecret(), 500, 500));

            } else {

                if ("PASS".equals(userInfoBean.getAduitstatus())) {
                    //已经审核通过
                    binaryCode.setImageBitmap(Utils.createQRImage(this, "test", 500, 500));

                } else {

                    //表示用户还没审核通过 则显示默认的图片
                    binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
                }
            }

            if ("PASS".equals(userInfoBean.getAduitstatus())) {
                //已经审核通过 开始获取我的门禁
                getMyCard();
            }

        }

        //获取图片路径
        getAdList();


    }

    @Override
    public void setListener() {


        life_layout.setOnClickListener(this);
        card_layout.setOnClickListener(this);
        my_layout.setOnClickListener(this);
        add_img.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("PASS".equals(userInfoBean.getAduitstatus())) {

                    showCalendarPopwindow(v);
                } else if ("AUDITING".equals(userInfoBean.getAduitstatus())) {

                    showToast("您所申请的微卡正在审核。。。");

                } else {
                    //跳到门禁申请界面
                    startActivity(new Intent(CardMainActivity.this, ApplyActivity.class));
                }


            }
        });


    }


    @OnClick(R.id.binaryCode)
    public void onClick() {

    }


    @Override
    protected void onResume() {
        super.onResume();

        userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //生活
            case R.id.life_layout:

                if ("PASS".equals(userInfoBean.getAduitstatus())) {

                    startActivity(new Intent(this, LifeActivity.class));

                } else if ("AUDITING".equals(userInfoBean.getAduitstatus())) {

                    showToast("您所申请的微卡正在审核。。。");

                } else {
                    //跳到门禁申请界面
                    startActivity(new Intent(this, ApplyActivity.class));
                }


                break;
            //获取微卡
            case R.id.card_layout:


                if ("PASS".equals(userInfoBean.getAduitstatus())) {

                    getMyCard();

                } else if ("AUDITING".equals(userInfoBean.getAduitstatus())) {

                    showToast("您所申请的微卡正在审核。。。");

                } else {
                    //跳到门禁申请界面
                    startActivity(new Intent(this, ApplyActivity.class));
                }


                break;
            //我的
            case R.id.my_layout:


                if ("PASS".equals(userInfoBean.getAduitstatus())) {

                    startActivity(new Intent(this, PersonalActivity.class));
                } else if ("AUDITING".equals(userInfoBean.getAduitstatus())) {

                    showToast("您所申请的微卡正在审核。。。");

                } else {
                    //跳到门禁申请界面
                    startActivity(new Intent(this, ApplyActivity.class));
                }


                break;

            //分享
            case R.id.add_img:

                if ("PASS".equals(userInfoBean.getAduitstatus())) {

                    Intent intent = new Intent(this, SendCardActivity.class);
                    intent.putExtra("buildname", buildname);
                    intent.putExtra("buildid", buildid);
                    startActivity(intent);
                } else if ("AUDITING".equals(userInfoBean.getAduitstatus())) {

                    showToast("您所申请的微卡正在审核。。。");

                } else {
                    //跳到门禁申请界面
                    startActivity(new Intent(this, ApplyActivity.class));
                }


                break;
            default:
                break;


        }
    }

    /**
     * 获取广告列表的方法
     */

    private void getAdList() {

        RequestParams params = new RequestParams();
        HttpUtil.get(Constants.HOST + Constants.AdList, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if(!NetUtil.checkNetInfo(CardMainActivity.this)){

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
                                JSONObject gg = new JSONObject(jsonObject.getString("data"));
                                adBeanList = JSON.parseArray(gg.getJSONArray("items").toString(), AdBean.class);
                                if (adBeanList != null && adBeanList.size() > 0) {

                                    advert.setSelectAnimClass(RotateEnter.class)
                                            .setUnselectAnimClass(NoAnimExist.class)
                                            .setSource(adBeanList)
                                            .startScroll();
                                }

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
     * 切换门禁卡弹出框
     *
     * @param v
     */
    private void showCalendarPopwindow(View v) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopWindow = inflater.inflate(R.layout.block_popwindow, null, false);
        ListView list_view = (ListView) vPopWindow.findViewById(R.id.list_view);
        blockAdapter = new BlockAdapter(this, list);
        list_view.setAdapter(blockAdapter);
        blockAdapter.notifyDataSetChanged();
        final PopupWindow popWindow = new PopupWindow(vPopWindow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);
        popWindow.showAsDropDown(v);


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //选中的楼栋二维码
                if (!TextUtil.isEmpty(list.get(arg2).getSecret())) {
                    buildname = list.get(arg2).getBuildname();
                    buildid = list.get(arg2).getHouseid();
                    showToast(list.get(arg2).getSecret());
                    binaryCode.setImageBitmap(Utils.createQRImage(CardMainActivity.this, list.get(arg2).getSecret(), 500, 500));
                    popWindow.dismiss();
                    //把选中的楼栋的信息保存到本地，下次进来直接可以显示
                    String BeanStr = JSON.toJSONString(list.get(arg2));
                    SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("EntranceBean", BeanStr);
                }

            }
        });


    }


    /**
     * 我的门禁列表
     */
    private void getMyCard() {

        RequestParams params = new RequestParams();
        params.put("pageSize", pageSize);
        params.put("pageNumber", pageNumber);
        HttpUtil.get(Constants.HOST + Constants.MyCardList, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

                if(!NetUtil.checkNetInfo(CardMainActivity.this)){

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
                                list.clear();
                                //  pageNumber = pageNumber + 1;
                                JSONObject gg = new JSONObject(jsonObject.getString("data"));
                                listTemp = JSON.parseArray(gg.getJSONArray("items").toString(), EntranceBean.class);
                                list.addAll(listTemp);
                                //  blockAdapter.notifyDataSetChanged();
                              /*  if (listTemp.size() == 10) {
                                    loadingMore = true;
                                } else {
                                    loadingMore = false;
                                }*/


                                if (!TextUtil.isEmpty(buildname)) {

                                    for (int i = 0; i < list.size(); i++) {

                                        if (buildname.equals(list.get(i).getBuildname())) {

                                            //表示上次选中的二维码，此时更新最新的二维码
                                            buildname = list.get(i).getBuildname();
                                            buildid = list.get(i).getBuildid();
                                            showToast(buildname + "比较" + list.get(i).getBuildname());
                                            binaryCode.setImageBitmap(Utils.createQRImage(CardMainActivity.this, list.get(i).getSecret(), 500, 500));

                                            //把选中的楼栋的信息保存到本地，下次进来直接可以显示
                                            String BeanStr = JSON.toJSONString(list.get(i));
                                            SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("EntranceBean", BeanStr);

                                        }
                                    }

                                } else {

                                    //初始化二维码
                                    if(list == null || list.size() <= 0){

                                        binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
                                        return;
                                    }
                                    if (!TextUtil.isEmpty(list.get(0).getSecret())) {
                                        buildname = list.get(0).getBuildname();
                                        buildid = list.get(0).getBuildid();
                                        showToast(list.get(0).getSecret());
                                        binaryCode.setImageBitmap(Utils.createQRImage(CardMainActivity.this, list.get(0).getSecret(), 500, 500));

                                        //把选中的楼栋的信息保存到本地，下次进来直接可以显示
                                        String BeanStr = JSON.toJSONString(list.get(0));
                                        SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("EntranceBean", BeanStr);
                                    }
                                }


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


}
