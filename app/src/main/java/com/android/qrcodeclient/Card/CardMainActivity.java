package com.android.qrcodeclient.Card;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.adapter.BlockAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.BannerItemBean;
import com.android.model.BlockBean;
import com.android.model.UserInfoBean;
import com.android.notify.PushService;
import com.android.notify.ServiceUtil;
import com.android.qrcodeclient.Life.LifeActivity;
import com.android.qrcodeclient.Personal.PersonalActivity;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.android.utils.SharedPreferenceUtil;
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
public class CardMainActivity extends BaseAppCompatActivity implements View.OnClickListener{

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

    public static String[] titles = new String[]{
            "伪装者:胡歌演绎'痞子特工'",
            "无心法师:生死离别!月牙遭虐杀",
            "花千骨:尊上沦为花千骨",
            "综艺饭:胖轩偷看夏天洗澡掀波澜",
            "碟中谍4:阿汤哥高塔命悬一线,超越不可能",
    };
    public static String[] urls = new String[]{//640*360 360/640=0.5625
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160323071011277.jpg",//伪装者:胡歌演绎"痞子特工"
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144158380433341332.jpg",//无心法师:生死离别!月牙遭虐杀
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160286644953923.jpg",//花千骨:尊上沦为花千骨
            "http://photocdn.sohu.com/tvmobilemvms/20150902/144115156939164801.jpg",//综艺饭:胖轩偷看夏天洗澡掀波澜
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144159406950245847.jpg",//碟中谍4:阿汤哥高塔命悬一线,超越不可能
    };

    public BlockAdapter blockAdapter;
    public List<BlockBean> blockBeanList = new ArrayList<BlockBean>();
    private UserInfoBean userInfoBean = new UserInfoBean();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_card);
        //开启闹钟定时任务
        //ServiceUtil.invokeTimerPOIService(this);
    }

    @Override
    public void initView() {

        advert = (SimpleImageBanner) this.findViewById(R.id.advert);
        add_img.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

        userInfoBean = JSON.parseObject(SharedPreferenceUtil.getInstance(this).getSharedPreferences().getString("UserInfo", ""), UserInfoBean.class);

        //配置请求接口全局token 和 userid
        if(userInfoBean != null){

            HttpUtil.getClient().addHeader("Token",userInfoBean.getToken());
            HttpUtil.getClient().addHeader("Userid",userInfoBean.getUserid());
        }

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        title.setText(R.string.card_title);
        advert.setSelectAnimClass(RotateEnter.class)
                .setUnselectAnimClass(NoAnimExist.class)
                .setSource(getList())
                .startScroll();
        binaryCode.setImageBitmap(Utils.createQRImage(this, "test", 500, 500));

    }

    @Override
    public void setListener() {

     //   toolbar.setNavigationOnClickListener(this);
        life_layout.setOnClickListener(this);
        card_layout.setOnClickListener(this);
        my_layout.setOnClickListener(this);
        add_img.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarPopwindow(v);
            }
        });

    }

    public static ArrayList<BannerItemBean> getList() {
        ArrayList<BannerItemBean> list = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            BannerItemBean item = new BannerItemBean();
            item.imgUrl = urls[i];
            item.title = titles[i];
            list.add(item);
        }

        return list;
    }

    @OnClick(R.id.binaryCode)
    public void onClick() {

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            //生活
            case R.id.life_layout:

                startActivity(new Intent(this, LifeActivity.class));

                break;
            //获取微卡
            case R.id.card_layout:

                break;
            //我的
            case R.id.my_layout:

                startActivity(new Intent(this, PersonalActivity.class));
                break;

            //分享
            case R.id.add_img:

                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

                // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle("微卡管理");
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                // oks.setTitleUrl("http://sharesdk.cn");
                // text是分享文本，所有平台都需要这个字段
                oks.setText("我是分享文本");

                oks.setTitleUrl("http://mob.com");

                oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");

                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                //oks.setUrl("http://sharesdk.cn");
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                //   oks.setComment("我是测试评论文本");
                // site是分享此内容的网站名称，仅在QQ空间使用
                // oks.setSite(getString(R.string.app_name));
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                // oks.setSiteUrl("http://sharesdk.cn");

                // 启动分享GUI
                oks.show(this);



                break;
            default:
                break;


        }
    }

    /**
     * 获取广告列表的方法
     */

    private void getAdList(){


        RequestParams params = new RequestParams();
      //  params.put("phone", "15522503900");


        HttpUtil.post(Constants.HOST + Constants.AdList, params, new AsyncHttpResponseHandler() {
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


    /**
     * 切换楼栋弹出框
     * @param v
     */
    private void showCalendarPopwindow(View v){
        blockBeanList.clear();
        blockBeanList.add(new BlockBean("方舟苑小区", "111"));
        blockBeanList.add(new BlockBean("威尼斯花园","111"));
        blockBeanList.add(new BlockBean("富泉花园","111"));
        blockBeanList.add(new BlockBean("阳光都市","111"));
        blockBeanList.add(new BlockBean("怡禾国际中心小区","111"));
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopWindow=inflater.inflate(R.layout.block_popwindow, null, false);
        ListView list_view = (ListView)vPopWindow.findViewById(R.id.list_view);
        blockAdapter = new BlockAdapter(this,blockBeanList);
        list_view.setAdapter(blockAdapter);
        blockAdapter.notifyDataSetChanged();
        final PopupWindow popWindow = new PopupWindow(vPopWindow,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);

        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);

        //popWindow.showAtLocation(getCurrentFocus(), Gravity.RIGHT | Gravity.TOP, 0,210);
        popWindow.showAsDropDown(v);
    }





}
