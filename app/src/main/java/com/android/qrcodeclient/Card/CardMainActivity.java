package com.android.qrcodeclient.Card;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.adapter.BlockAdapter;
import com.android.adapter.HouseAdapter;
import com.android.adapter.HouseOffLineAdapter;
import com.android.application.ExitApplication;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.download.UpdateManger;
import com.android.model.AdBean;
import com.android.model.ComunityMallBean;
import com.android.model.EntranceBean;
import com.android.model.HouseBean;
import com.android.model.OfflineData;
import com.android.model.UserInfoBean;
import com.android.qrcodeclient.Life.LifeActivity;
import com.android.qrcodeclient.Personal.ApplyActivity;
import com.android.qrcodeclient.Personal.PersonalActivity;
import com.android.qrcodeclient.R;
import com.android.utils.DialogMessageUtil;
import com.android.utils.HttpUtil;
import com.android.utils.ImageOpera;
import com.android.utils.NetUtil;
import com.android.utils.SharedPreferenceUtil;
import com.android.utils.TextUtil;
import com.android.utils.Tools;
import com.android.utils.Utils;
import com.android.utils.VoiceUtil;
import com.android.view.SimpleImageBanner;
import com.android.view.SquareImageView;
import com.bumptech.glide.Glide;
import com.flyco.banner.anim.select.RotateEnter;
import com.flyco.banner.anim.unselect.NoAnimExist;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
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

    @Bind(R.id.time_count_txt)
    TextView time_count_txt;

    int intScreenBrightness;
    int pageNumber = 0;
    int pageSize = 30;
    int QRsize = 500;
    //该用户的小区列表
    List<HouseBean> houseList = new ArrayList<>();
    List<HouseBean> houseListTemp = new ArrayList<>();
    //当前小区下的微卡列表
    List<EntranceBean> list = new ArrayList<>();
    List<EntranceBean> listTemp = new ArrayList<>();
    //当前小区离线下的微卡列表
    List<EntranceBean> offEntrancelist;
    private boolean isInit = false;
    public String[] titles;
    public String[] urls;

    public BlockAdapter blockAdapter;
    public HouseAdapter houseAdapter;
    public HouseOffLineAdapter houseofflineAdapter;
    private UserInfoBean userInfoBean = new UserInfoBean();
    List<AdBean> adBeanList = new ArrayList<>();
    TimeCount time;
    private String phone;
    // //获取楼栋列表需要还原head中的houseid
    private  static int needAddHead = 1;
    //获取楼栋列表不需要还原head中的houseid
    private  static int noAddHead = 0;
    //houseid的临时变量
    private  String houseidTemp;
    //选中的houseid
    String hasSelectHouseid = "";
    String hasSelectHousename = "";

   //二号广告位
    List<ComunityMallBean> qrcodeAd  = new ArrayList<>();

    PopupWindow popWindow;

    int  showTitle = 1000; //0 表示 提示去门禁申请或联系物业 1表示 黑白名单 或者 超时


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
        time = new TimeCount(30000, 1000);//构造CountDownTimer对象
    }

    @Override
    public void initData() {


        //TODO 当一次注册登录后 也申请了门禁卡 但是物业后台还没审核 这时候 进入首页点击其他按钮 不应该再跳到门禁申请界面

        userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);

           //配置请求接口全局token 和 userid houseid
        if (userInfoBean != null) {

            HttpUtil.getClient().addHeader("Token", userInfoBean.getToken());
            HttpUtil.getClient().addHeader("Userid", userInfoBean.getUserid());
            HttpUtil.getClient().addHeader("Houseid", userInfoBean.getHouseid());

        }

        toolbar.setNavigationIcon(R.mipmap.qiehuan);


        //从我的模块中我的门禁列表中选中的值回填
        EntranceBean entranceBean = (EntranceBean)getIntent().getSerializableExtra("FromEntranceActivity");

        if(entranceBean != null){

            //从我的门禁列表进来
            title.setText(entranceBean.getHousename()+"-"+entranceBean.getBuildname());//在头部描述当前小区以及楼栋名称
            //TODO 这个地方要替换为本地算法生成二维码的方法 暂时是采用从我的门禁选中带过来的秘钥
            binaryCode.setImageBitmap(Utils.createQRImage(this, entranceBean.getSecret(), QRsize, QRsize));
            ImageOpera.savePicToSdcard(Utils.createQRImage(CardMainActivity.this,entranceBean.getSecret(), QRsize, QRsize), getOutputMediaFile(), "MicroCode.png");
            //保存EntranceBean 到本地文件
            String BeanStr = JSON.toJSONString(entranceBean);
            SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("EntranceBean", BeanStr);
            //实时调用后台生成二维码的接口
            getQrCodeByBuildID(entranceBean.getBuildid());
            //获取图片路径
            getAdList();

        } else {

            //从免登陆方式 获取我的门禁列表(就是把上次二维码保存到share文件中再取出来用)
            if (!TextUtil.isEmpty(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("EntranceBean", ""))) {

                EntranceBean EntranceBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("EntranceBean", ""), EntranceBean.class);
                title.setText(EntranceBean.getHousename()+"-"+EntranceBean.getBuildname());//在头部描述当前小区以及楼栋名称
                //TODO 同上
                binaryCode.setImageBitmap(Utils.createQRImage(this, EntranceBean.getSecret(), QRsize, QRsize));
                ImageOpera.savePicToSdcard(Utils.createQRImage(CardMainActivity.this, EntranceBean.getSecret(), QRsize, QRsize), getOutputMediaFile(), "MicroCode.png");

                //实时调用后台生成二维码的接口
                getQrCodeByBuildID(EntranceBean.getBuildid());
               //获取图片路径
                getAdList();
            } else {

                //--------------------------------start--------------------------------------------
                //首次登陆进来，先设置默认图片 再调用接口获取个人信息 将UserInfoBean保存到本地 同时addHeader
                // 再去通过调用getMyCardList()接口默认第一条数据为开门二维码和获取广告列表
                //保存默认的第一条数据EntranceBean 到本地文件
                //--------------------------------end--------------------------------------------
                //binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
               // getQrcodeAdv();
                //获取个人用户信息
                isInit = true;
                getUserInfo();
            }



        }

        phone = getIntent().getStringExtra("phone");


    }

    @Override
    public void setListener() {


        life_layout.setOnClickListener(this);
        card_layout.setOnClickListener(this);
        my_layout.setOnClickListener(this);
        add_img.setOnClickListener(this);

        //切换小区按钮
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 断网了 或者在地下室 信息不好 ，则进行缓存加载
                if (!NetUtil.checkNetInfo(CardMainActivity.this)) {

                     showToast("当前网络不可用,将加载本地缓存数据");

                    if(!TextUtil.isEmpty(SharedPreferenceUtil.getInstance(CardMainActivity.this).getSharedPreferences().getString("OfflineData", ""))){

                        String  offlineDataStr = SharedPreferenceUtil.getInstance(CardMainActivity.this).getSharedPreferences().getString("OfflineData", "");
                        List<OfflineData>  offdataList = JSON.parseArray(offlineDataStr, OfflineData.class);
                       showOffLineCalendarPopwindow(v,offdataList);

                    }else{

                        DialogMessageUtil.showDialog(CardMainActivity.this, "暂无微卡");
                    }

                    return;
                }

                //---------------------start----------------------------------
                //点击切换小区按钮时，调用获取小区列表的接口。
                // 如果只有一个小区，就不会显示小区列表，只显示楼栋列表
                //如果是多个小区，会显示小区列表，如果进行小区切换
                // a:（同时把请求头部addhead一下 这里先不把houseid 更新到本地userInfoBean中的houseid 因为用户有可能不会去选择楼栋 ）
                // b: 用一个临时变量houseidTemp 来保存在点击切换小区按钮之前的houseid   场景：如果不选择楼栋 关掉弹出框 则把临时变量houseidTemp 把请求头部addhead一下（可以在每次获取楼栋列表回调中操作）
                //点击选择楼栋 （同时实时保存选中的哪一个楼栋保存到本地EntranceBean ,把houseid 更新到本地userInfoBean中的houseid和 housename，同时把请求头部addhead一下）再去调用生成二维码接口getQrcodeByBuildid()
                //首页头部描述显示重置
                //---------------------end------------------------------------

                    userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(CardMainActivity.this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);
                    houseidTemp = userInfoBean.getHouseid();
                    //开始调用获取小区列表接口
                    try{

                        getHouseList(v);
                    }catch (Exception e){

                        e.printStackTrace();
                    }

            }
        });


    }


    @OnClick(R.id.binaryCode)
    public void onClick() {


        if (!TextUtil.isEmpty(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("EntranceBean", ""))) {

            EntranceBean entranceBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("EntranceBean", ""), EntranceBean.class);
            //实时调用后台生成二维码的接口
            getQrCodeByBuildID(entranceBean.getBuildid());

        }else{

            if(showTitle == 1){

                showToast("处于黑名单或超时中");
            }else{

                DialogMessageUtil.showDialog(CardMainActivity.this, "暂无微卡");
            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);
        getAdList();

        //刚注册登录
        if(TextUtil.isEmpty(userInfoBean.getHouseid())){

            getUserInfo();
        }

        //检查app版本是否有更新
        UpdateAPPVersion();
        //获取离线数据
        getOfflineData();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            //生活
            case R.id.life_layout:


                startActivity(new Intent(this, LifeActivity.class));


                break;
            //获取微卡
            case R.id.card_layout:

                if (!TextUtil.isEmpty(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("EntranceBean", ""))) {

                    EntranceBean entranceBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("EntranceBean", ""), EntranceBean.class);
                    //实时调用后台生成二维码的接口
                    getQrCodeByBuildID(entranceBean.getBuildid());



                }else{

                    if(showTitle == 1){

                        showToast("处于黑名单或超时中");
                    }else{

                        DialogMessageUtil.showDialog(CardMainActivity.this, "暂无微卡");
                    }

                }



                break;
            //我的
            case R.id.my_layout:

                startActivity(new Intent(this, PersonalActivity.class));


                break;

            //分享
            case R.id.add_img:


                if (!TextUtil.isEmpty(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("EntranceBean", ""))) {

                    shareToPlatForm();

                }else{

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
                if (!NetUtil.checkNetInfo(CardMainActivity.this)) {

                    showToast("当前网络不可用,请检查网络");
                    List ad = new ArrayList<AdBean>();
                    ad.add(new AdBean("","",""));
                    advert.setSelectAnimClass(RotateEnter.class)
                            .setUnselectAnimClass(NoAnimExist.class)
                            .setSource(ad)
                            .startScroll();
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

                                    for(int i = 0; i< adBeanList.size();i++){

                                        adBeanList.get(i).setPicurl(Constants.HOST+adBeanList.get(i).getPicurl());
                                    }
                                    advert.setSelectAnimClass(RotateEnter.class)
                                            .setUnselectAnimClass(NoAnimExist.class)
                                            .setSource(adBeanList)
                                            .startScroll();
                                }else{

                                    //查不到数据给一张本地的默认图
                                    List ad = new ArrayList<AdBean>();
                                    ad.add(new AdBean("","",""));
                                    advert.setSelectAnimClass(RotateEnter.class)
                                            .setUnselectAnimClass(NoAnimExist.class)
                                            .setSource(ad)
                                            .startScroll();
                                }

                            } else {

                                showToast(jsonObject.getString("msg"));
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
     * 在线切换门禁卡弹出框
     *
     * @param v
     */
    private void showCalendarPopwindow(View v) {

        if(popWindow != null && popWindow.isShowing()){

            popWindow.dismiss();
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopWindow = inflater.inflate(R.layout.block_popwindow, null, false);
        final ListView build_list_view = (ListView) vPopWindow.findViewById(R.id.build_list_view);
        ListView house_list_view = (ListView) vPopWindow.findViewById(R.id.house_list_view);
        LinearLayout house_layout = (LinearLayout) vPopWindow.findViewById(R.id.house_layout);
        LinearLayout build_layout = (LinearLayout) vPopWindow.findViewById(R.id.build_layout);

        // 只有一个小区 则显示楼栋列表
        if(houseList != null && houseList.size() == 1){

            if (list != null && list.size() > 0){
                house_list_view.setVisibility(View.GONE);
                house_layout.setVisibility(View.GONE);
                blockAdapter = new BlockAdapter(this, list);
                build_list_view.setAdapter(blockAdapter);
                blockAdapter.notifyDataSetChanged();
            }
        }else{
            //多个小区 则显示小区列表
            //build_layout.setVisibility(View.GONE);
            build_list_view.setVisibility(View.GONE);
            houseAdapter = new HouseAdapter(this,houseList);
            house_list_view.setAdapter(houseAdapter);

            houseAdapter.notifyDataSetChanged();
        }

        popWindow = new PopupWindow(vPopWindow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);
        popWindow.showAsDropDown(v);

        //小区列表item点击事件
        house_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                //实时更新head中的houseid
                HttpUtil.getClient().addHeader("Houseid", houseList.get(arg2).getHouseid());
                hasSelectHouseid = houseList.get(arg2).getHouseid();
                hasSelectHousename = houseList.get(arg2).getName();
                build_list_view.setVisibility(View.GONE);
                houseAdapter.setSelectItem(arg2);
                houseAdapter.notifyDataSetChanged();
                getMyCardList(build_list_view, needAddHead);

            }
        });


        //楼栋列表的点击事件
        build_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //如果是多个小区 把head中的houseid更新一下，同时保存houseid 和 housename到userinfobean中，同时保存EntranceBean到本地文件
                //如果是一个小区 直接调用生成微卡接口
                //这个时候可能没网 但是上面的步骤已经完成了，不处理数据会有问题的 所以在获取微卡时在onStart()中没网 自己去生成微卡
                if(!TextUtil.isEmpty(hasSelectHouseid) && !TextUtil.isEmpty(hasSelectHousename)){

                    HttpUtil.getClient().addHeader("Houseid",hasSelectHouseid);

                    //获取图片路径 实时更新当前小区的广告轮播图片
                    getAdList();

                    userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(CardMainActivity.this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);
                    userInfoBean.setHouseid(hasSelectHouseid);
                    userInfoBean.setHousename(hasSelectHousename);
                    String userInfoBeanStr = JSON.toJSONString(userInfoBean);
                    SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("UserInfo", userInfoBeanStr);
                    String BeanStr = JSON.toJSONString(list.get(arg2));
                    SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("EntranceBean", BeanStr);
                    getQrCodeByBuildID(list.get(arg2).getBuildid());
                }else{

                    String BeanStr = JSON.toJSONString(list.get(arg2));
                    SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("EntranceBean", BeanStr);
                    getQrCodeByBuildID(list.get(arg2).getBuildid());
                }
                popWindow.dismiss();

            }
        });

    }

    /**
     * 离线切换门禁卡弹出框
     *
     * @param v
     */
    private void showOffLineCalendarPopwindow(final View v, final List<OfflineData> offlist) {

        if(popWindow != null && popWindow.isShowing()){

            popWindow.dismiss();
        }


        offEntrancelist = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopWindow = inflater.inflate(R.layout.block_popwindow, null, false);
        final ListView build_list_view = (ListView) vPopWindow.findViewById(R.id.build_list_view);
        ListView house_list_view = (ListView) vPopWindow.findViewById(R.id.house_list_view);
        LinearLayout house_layout = (LinearLayout) vPopWindow.findViewById(R.id.house_layout);
        LinearLayout build_layout = (LinearLayout) vPopWindow.findViewById(R.id.build_layout);

        // 只有一个小区 则显示楼栋列表
        if(offlist != null && offlist.size() == 1){

            if (offlist.get(0).getCardMap().getItems() != null && offlist.get(0).getCardMap().getItems().size() > 0){
                house_list_view.setVisibility(View.GONE);
                house_layout.setVisibility(View.GONE);
                offEntrancelist = offlist.get(0).getCardMap().getItems();
                blockAdapter = new BlockAdapter(this, offEntrancelist);
                build_list_view.setAdapter(blockAdapter);
                blockAdapter.notifyDataSetChanged();
            }
        }else{
            //多个小区 则显示小区列表

            build_list_view.setVisibility(View.GONE);
            houseofflineAdapter = new HouseOffLineAdapter(this,offlist);
            house_list_view.setAdapter(houseofflineAdapter);
            houseofflineAdapter.notifyDataSetChanged();
        }

        popWindow = new PopupWindow(vPopWindow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);
        popWindow.showAsDropDown(v);

        //小区列表item点击事件
        house_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


                hasSelectHouseid = offlist.get(arg2).getHouseid();
                hasSelectHousename = offlist.get(arg2).getName();
                houseofflineAdapter.setSelectItem(arg2);
                houseofflineAdapter.notifyDataSetChanged();
                build_list_view.setVisibility(View.VISIBLE);
                offEntrancelist = offlist.get(arg2).getCardMap().getItems();
                blockAdapter = new BlockAdapter(CardMainActivity.this,offEntrancelist );
                build_list_view.setAdapter(blockAdapter);
                blockAdapter.notifyDataSetChanged();

            }
        });


        //楼栋列表的点击事件
        build_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if(!TextUtil.isEmpty(hasSelectHouseid) && !TextUtil.isEmpty(hasSelectHousename)){

                    HttpUtil.getClient().addHeader("Houseid",hasSelectHouseid);
                    userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(CardMainActivity.this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);
                    userInfoBean.setHouseid(hasSelectHouseid);
                    userInfoBean.setHousename(hasSelectHousename);
                    String userInfoBeanStr = JSON.toJSONString(userInfoBean);
                    SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("UserInfo", userInfoBeanStr);
                    String BeanStr = JSON.toJSONString(offEntrancelist.get(arg2));
                    SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("EntranceBean", BeanStr);
                    getQrCodeByBuildID(offEntrancelist.get(arg2).getBuildid());



                }else{

                    String BeanStr = JSON.toJSONString(offEntrancelist.get(arg2));
                    SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("EntranceBean", BeanStr);
                    getQrCodeByBuildID(offEntrancelist.get(arg2).getBuildid());
                }
                popWindow.dismiss();

            }
        });

    }



    /**
     * 获取用户个人信息
     */
    private void getUserInfo() {

        RequestParams params = new RequestParams();


        HttpUtil.get(Constants.HOST + Constants.getUserInfo, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

                if (!NetUtil.checkNetInfo(CardMainActivity.this)) {

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


                                userInfoBean = JSON.parseObject(jsonObject.getJSONObject("data").toString(), UserInfoBean.class);
                                if (!TextUtil.isEmpty(phone)) {

                                    userInfoBean.setPhone(phone);
                                }
                                String userInfoBeanStr = JSON.toJSONString(userInfoBean);
                                SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("UserInfo", userInfoBeanStr);

                                //配置请求接口全局token 和 userid houseid
                                if (userInfoBean != null) {

                                    HttpUtil.getClient().addHeader("Token", userInfoBean.getToken());
                                    HttpUtil.getClient().addHeader("Userid", userInfoBean.getUserid());
                                    HttpUtil.getClient().addHeader("Houseid", userInfoBean.getHouseid());

                                }


                                //实时调用门禁列表接口 默认第一条数据为开门二维码
                                getMyCardList(add_img,noAddHead);
                                //获取图片路径
                                getAdList();


                            } else {

                                showToast(jsonObject.getString("msg"));
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
     * 获取用户的小区列表
     */
    public void getHouseList(final View v){

        RequestParams params = new RequestParams();

        HttpUtil.get(Constants.HOST + Constants.MyHouseList, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

                if (!NetUtil.checkNetInfo(CardMainActivity.this)) {

                    showToast("当前网络不可用,请检查网络");
                    return;
                }

                URI uri = this.getRequestURI();

                showLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {


                if (responseBody != null) {
                    try {
                        String str = new String(responseBody);
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject != null) {

                            if (jsonObject.getBoolean("success")) {


                                houseList.clear();
                                JSONObject gg = new JSONObject(jsonObject.getString("data"));
                                houseListTemp = JSON.parseArray(gg.getJSONArray("items").toString(), HouseBean.class);
                                houseList.addAll(houseListTemp);

                                if (houseList != null && houseList.size() > 0) {

                                    //如果只有一个小区 则只显示楼栋列表
                                    if (houseList.size() == 1) {
                                        //再去调用楼栋列表
                                        getMyCardList(v, noAddHead);

                                    } else {

                                        showCalendarPopwindow(v);
                                    }

                                } else {

                                    DialogMessageUtil.showDialog(CardMainActivity.this, "您还没有小区，请门禁申请或联系物业");
                                  //  binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
                                    getQrcodeAdv();
                                }
                            } else {

                                showToast(jsonObject.getString("msg"));
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

                closeLoadDialog();
            }


        });


    }

    /**
     * 我的门禁列表
     */
    private void getMyCardList(final View v, final int flag) {
        //三种场景调用这个接口
        //flag:0 表示不需要还原head中的houseid，场景：首次登陆调用或者是只有一个小区时调用
        //flag:1 表示需要还原head中的houseid, 场景：在切花小区时会把head中的houseid改了 如果用户不去点击楼栋 关闭弹出框 那应该把houseid 还原
        RequestParams params = new RequestParams();
        params.put("pageSize", pageSize);
        params.put("pageNumber", pageNumber);
        HttpUtil.get(Constants.HOST + Constants.MyCardList, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

                if (!NetUtil.checkNetInfo(CardMainActivity.this)) {

                    showToast("当前网络不可用,请检查网络");
                    return;
                }

                showLoadingDialog();
                URI uri = this.getRequestURI();
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
                                // TODO 黑名单 和 超时机制还没写逻辑  0 表示正常 1 表示 黑名单 2表示超时 （已写）
                                if("1".equals(jsonObject.getString("userStatus"))){

                                    title.setText("处于黑名单");
                                    showToast("处于黑名单状态中");
                                    if(popWindow != null) {
                                        popWindow.dismiss();
                                    }
                                    // binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
                                    getQrcodeAdv();

                                    return;
                                }
                                if("2".equals(jsonObject.getString("userStatus"))){

                                    title.setText("处于超时中");
                                    if(popWindow != null) {
                                        popWindow.dismiss();
                                    }
                                    //binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
                                    getQrcodeAdv();

                                    showToast("处于超时状态中");
                                    return;
                                }

                                showTitle = 1000;

                                JSONObject gg = new JSONObject(jsonObject.getString("data"));
                                listTemp = JSON.parseArray(gg.getJSONArray("items").toString(), EntranceBean.class);
                                list.addAll(listTemp);

                                if (list != null && list.size() > 0 ) {

                                    if(isInit){
                                        //表示第一次登录进来
                                        binaryCode.setImageBitmap(Utils.createQRImage(CardMainActivity.this, list.get(0).getSecret(), QRsize, QRsize));
                                        ImageOpera.savePicToSdcard(Utils.createQRImage(CardMainActivity.this, list.get(0).getSecret(), QRsize, QRsize), getOutputMediaFile(), "MicroCode.png");

                                        String BeanStr = JSON.toJSONString(list.get(0));
                                        SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("EntranceBean", BeanStr);
                                        title.setText(list.get(0).getHousename() + "-" + list.get(0).getBuildname());//在头部描述当前小区以及楼栋名称
                                        isInit = false;
                                        //开启倒计时
                                        time.start();
                                        //给按钮添加声音
                                        try {

                                            VoiceUtil.getInstance(CardMainActivity.this).startVoice();

                                        } catch (Exception e) {

                                            e.printStackTrace();
                                        }


                                    }else{
                                        //点击切换小区出来的楼栋列表
                                        //获取到houseid的临时变量 先做还原houseid
                                        if(flag == 1){
                                            //多个小区
                                            HttpUtil.getClient().addHeader("Houseid", houseidTemp);
                                            v.setVisibility(View.VISIBLE);
                                            blockAdapter = new BlockAdapter(CardMainActivity.this, list);
                                            ((ListView)v).setAdapter(blockAdapter);
                                            blockAdapter.notifyDataSetChanged();

                                        }else{
                                           //只有一个小区
                                            showCalendarPopwindow(v);
                                        }

                                    }

                                }else{

                                    try {

                                        if(flag == 1){
                                            //多个小区
                                            HttpUtil.getClient().addHeader("Houseid", houseidTemp);

                                        }

                                        DialogMessageUtil.showDialog(CardMainActivity.this, "您当前小区还没有楼栋列表，请门禁申请或联系物业");
                                     //   binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
                                        getQrcodeAdv();

                                    } catch (Exception e) {

                                    }


                                }


                            } else {
                                if(flag == 1){
                                    //多个小区
                                    HttpUtil.getClient().addHeader("Houseid", houseidTemp);

                                }

                                showToast(jsonObject.getString("msg"));
                                //这种场景是刚注册登录进来 还没门禁申请 时去点击获取微卡
                                if("小区无该用户".equals(jsonObject.getString("msg"))){
                                    showTitle = 0;

                                }



                                getQrcodeAdv();

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(flag == 1){
                            //多个小区
                            HttpUtil.getClient().addHeader("Houseid", houseidTemp);

                        }

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

                        if(flag == 1){
                            //多个小区
                            HttpUtil.getClient().addHeader("Houseid", houseidTemp);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();

                closeLoadDialog();
            }


        });

    }

    /**
     *t通过buildid来获取二维码
     */

    public void getQrCodeByBuildID(String buildId){



        RequestParams params = new RequestParams();



        HttpUtil.get(Constants.HOST + Constants.GetQrCodeByBuild + "/"+buildId +"/card", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                // TODO采用本地算法生成二维码
                if (!NetUtil.checkNetInfo(CardMainActivity.this)) {



                    if (!TextUtil.isEmpty(SharedPreferenceUtil.getInstance(CardMainActivity.this).getSharedPreferences().getString("EntranceBean", ""))) {

                        EntranceBean entranceBean = JSON.parseObject(SharedPreferenceUtil.getInstance(CardMainActivity.this).getSharedPreferences().getString("EntranceBean", ""), EntranceBean.class);
                        title.setText(entranceBean.getHousename() + "-" + entranceBean.getBuildname());//在头部描述当前小区以及楼栋名称
                        String code = "";
                        // TODO: 2017/1/2 当获取微卡没网的时候，采用本地的算法来生成微卡  （已写）
                        if("-1".equals(entranceBean.getBuildid())){
                            //表示全开
                            code =  Tools.createQrCodeStr(Integer.parseInt(entranceBean.getBuildcode()),Integer.parseInt(entranceBean.getHousecode()),"4");
                        }else{

                            code = Tools.createQrCodeStr(Integer.parseInt(entranceBean.getBuildcode()),Integer.parseInt(entranceBean.getHousecode()),"0");
                        }

                        if(!TextUtil.isEmpty(code)){


                            binaryCode.setImageBitmap(Utils.createQRImage(CardMainActivity.this, code, QRsize, QRsize));
                            ImageOpera.savePicToSdcard(Utils.createQRImage(CardMainActivity.this, code, QRsize, QRsize), getOutputMediaFile(), "MicroCode.png");

                            //开启倒计时
                            time.start();
                            //给按钮添加声音
                            try {

                                VoiceUtil.getInstance(CardMainActivity.this).startVoice();

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }


                      }



                    return;
                }
                showLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {


                if (responseBody != null) {
                    try {
                        String str = new String(responseBody);
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject != null) {

                            if (jsonObject.getBoolean("success")) {

                                // 0 表示正常 1 表示 黑名单 2表示超时
                                if("1".equals(jsonObject.getString("userStatus"))){

                                    if(title != null){

                                        title.setText("处于黑名单");
                                    }
                                    showToast("处于黑名单状态中");
                                    if(popWindow != null){

                                        popWindow.dismiss();
                                    }
                                   // binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
                                    getQrcodeAdv();

                                    showTitle = 1;
                                    return;

                                   }
                                if("2".equals(jsonObject.getString("userStatus"))){

                                    if(title != null){

                                        title.setText("处于超时中");
                                    }
                                    showToast("处于超时状态中");
                                    if(popWindow != null) {
                                        popWindow.dismiss();
                                    }

                                  //  binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
                                    getQrcodeAdv();

                                    showTitle = 1;
                                    return;

                                   }

                                showTitle = 1000;

                                //下面是userStatus为0
                                if (!TextUtil.isEmpty(SharedPreferenceUtil.getInstance(CardMainActivity.this).getSharedPreferences().getString("EntranceBean", ""))) {

                                    try {

                                        EntranceBean entranceBean = JSON.parseObject(SharedPreferenceUtil.getInstance(CardMainActivity.this).getSharedPreferences().getString("EntranceBean", ""), EntranceBean.class);

                                        if(title != null){

                                            title.setText(entranceBean.getHousename() + "-" + entranceBean.getBuildname());//在头部描述当前小区以及楼栋名称
                                        }

                                    }catch (Exception e){

                                    }


                                }


                                    //展示二维码和将二维码保存到sd卡用来做分享
                                    JSONObject gg = new JSONObject(jsonObject.getString("data"));
                                    String qrCode = gg.getString("secret");
                                    binaryCode.setImageBitmap(Utils.createQRImage(CardMainActivity.this, qrCode, QRsize, QRsize));
                                    ImageOpera.savePicToSdcard(Utils.createQRImage(CardMainActivity.this, qrCode, QRsize, QRsize), getOutputMediaFile(), "MicroCode.png");
                                    //开启倒计时
                                    time.start();
                                    //给按钮添加声音
                                    try {

                                        VoiceUtil.getInstance(CardMainActivity.this).startVoice();

                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }


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

                closeLoadDialog();
            }


        });


    }


    /**
     * 获取2号为广告接口
     */

    public void getQrcodeAdv(){

        RequestParams params = new RequestParams();

        HttpUtil.get(Constants.HOST + Constants.QrcodeAdv, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

                if (!NetUtil.checkNetInfo(CardMainActivity.this)) {

                   // showToast("当前网络不可用,请检查网络");
                    binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
                    return;
                }



            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                qrcodeAd.clear();
                if (responseBody != null) {
                    try {
                        String str = new String(responseBody);
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject != null) {

                            if (jsonObject.getBoolean("success")) {
                                JSONObject gg = new JSONObject(jsonObject.getString("data"));
                                qrcodeAd = JSON.parseArray(gg.getJSONArray("items").toString(), ComunityMallBean.class);
                               if(qrcodeAd != null && qrcodeAd.size() > 0){

                                   Glide.with(CardMainActivity.this).load(Constants.HOST + qrcodeAd.get(0).getPicurl()).into(binaryCode);

                               }

                            }else{

                                showToast(jsonObject.getString("msg"));
                                binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
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
                        binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));


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
     * 查询app是否有新版本
     */

    public void UpdateAPPVersion(){

        RequestParams params = new RequestParams();

        HttpUtil.get(Constants.HOST + Constants.UpdateAPPVersion, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

                if (!NetUtil.checkNetInfo(CardMainActivity.this)) {

                   // showToast("当前网络不可用,请检查网络");

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


                               String updateExplain = jsonObject.getString("updateExplain");
                               String updateTime = jsonObject.getString("updateTime");
                               String forceUpdate = jsonObject.getString("forceUpdate");
                               String downLoadURL = jsonObject.getString("downLoadURL");
                               String versionName = jsonObject.getString("version");
                               String localVersionName =Utils.getVersionName(CardMainActivity.this);
                                if(Float.parseFloat(localVersionName) < Float.parseFloat(versionName)){

                                    //需要版本升级
                                    new UpdateManger(CardMainActivity.this).checkUpdateInfo(downLoadURL);
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
     * 获取离线数据方法
     */

    private void getOfflineData() {

        RequestParams params = new RequestParams();
        HttpUtil.get(Constants.HOST + Constants.OfflineData, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetUtil.checkNetInfo(CardMainActivity.this)) {


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

                                List<OfflineData>  list  = JSON.parseArray(jsonObject.getJSONArray("data").toString(), OfflineData.class);

                                String offlineDataStr = JSON.toJSONString(list);
                                SharedPreferenceUtil.getInstance(CardMainActivity.this).putData("OfflineData", offlineDataStr);

                                //List<OfflineData>  list1 = JSON.parseArray(offlineDataStr, OfflineData.class);


                            } else {

                                showToast(jsonObject.getString("msg"));
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





    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发

            try{

                if(card_layout != null && time_count_txt != null && binaryCode != null ) {
                    time_count_txt.setText("扫描二维码开门(" + "0" + ")");
                    card_layout.setClickable(true);
                    binaryCode.setClickable(true);
                    //binaryCode.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.default_qrcode));
                    getQrcodeAdv();
                }

            }catch (Exception e){

            }



        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示

            try{

                if(card_layout != null && time_count_txt != null && binaryCode != null){

                    time_count_txt.setText("扫描二维码开门("+ --millisUntilFinished / 1000 + ")");
                    card_layout.setClickable(false);
                    binaryCode.setClickable(false);
                }

            }catch (Exception e){

            }

        }
    }



    /**
     * 将图片保存到本地文件中
     */
    private String getOutputMediaFile() {


        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if(!picDir.exists()){
            picDir.mkdir();
        }

        String str = picDir.getPath() + File.separator;
        return str;
    }

    /**
     *  分享到qq 微信 短信
     *
     */

    private void shareToPlatForm(){

        try {

            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();
            //  oks.setTitle("微卡");
            // oks.setText("微卡");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            if(!TextUtil.isEmpty(getOutputMediaFile()+"MicroCode.png")){
                oks.setImagePath(getOutputMediaFile()+"MicroCode.png");//确保SDcard下面存在此张图片
            }

            oks.show(this);

        }catch (Exception e){

        }


    }
    private void screenBrightness_check()
    {
        //先关闭系统的亮度自动调节
        try
        {
            if(android.provider.Settings.System.getInt(getContentResolver(),android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE) == android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
            {
                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        }
        catch (Settings.SettingNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //获取当前亮度,获取失败则返回255
        intScreenBrightness=(int)(android.provider.Settings.System.getInt(getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                255));
        //文本、进度条显示
       // mSeekBar_light.setProgress(intScreenBrightness);
       // mTextView_light.setText(""+intScreenBrightness*100/255);
    }
    //屏幕亮度
    private void setWindowBrightness(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

}
