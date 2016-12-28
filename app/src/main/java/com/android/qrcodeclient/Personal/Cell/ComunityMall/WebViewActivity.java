package com.android.qrcodeclient.Personal.Cell.ComunityMall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.adapter.ComunityMallAdapter;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.ComunityMallBean;
import com.android.qrcodeclient.R;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.TextUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liujq on 2016/12/28.
 */
public class WebViewActivity extends BaseAppCompatActivity implements View.OnClickListener{
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webview)
    WebView webview;



    String titletext;
    String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_web_view);



    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //设置标题
        Intent intent = getIntent();
        titletext = intent.getStringExtra("title");
        url = intent.getStringExtra("url");

        if (null == url) {
            finish();
            return;
        }


        title.setText(titletext);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);

        // 启动缓存
        webview.getSettings().setAppCacheEnabled(true);
        // 设置缓存模式
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 加载网页
        webview.loadUrl(url);
        // 在当前的浏览器中响应
        webview.setWebViewClient(new myWebViewClient());

    }



    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        finish();
    }





    final class myWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            String jsStr = "javascript:" +
                    "var btns = document.getElementsByTagName('input');" +
                    "for(var i = 0 ; i < btns.length;i++)" +
                    "{if(btns[i] && btns[i].type =='button')" +
                    "{btns[i].parentNode.parentNode.removeChild(btns[i].parentNode);}}";
            // 调用方法将打印、关闭按钮 取消
         //   webview.loadUrl(jsStr);

        }
    }

}
