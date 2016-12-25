package com.android.utils;


import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;


public class HttpUtil {
    /**
     * 网络超时时间
     */
    public static final int MY_TIME_OUT = 30 * 1000;
    public static final int FILE_TIME_OUT = 2 * 60 * 1000;
    public static final int MAX_RETRIE_TIMES = 1;//�?��上传次数
    private static AsyncHttpClient client;    //实例话对�?

    static {
        client = new AsyncHttpClient();
        //设置链接超时，如果不设置，默认为10s
        client.setMaxRetriesAndTimeout(MAX_RETRIE_TIMES, MY_TIME_OUT);
        client.setTimeout(MY_TIME_OUT);
      //  client.addHeader("Content-Type", "application/json");
        client.addHeader("Charset", "UTF-8");
//        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//        client.addHeader("Accept-Encoding", "gzip,deflate,sdch");
//        client.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");


    }

    /******************** * get请求*******************************/
    public static void get(String urlString, AsyncHttpResponseHandler res)    //用一个完整url获取�?��string对象
    {
        client.get(urlString, res);
    }

    public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler res)   //url里面带参�?
    {
        client.get(urlString, params, res);
    }
    public static void get(Context context,String urlString,Header[] headers, RequestParams params, AsyncHttpResponseHandler res)   //url里面带参�?
    {
        client.get(context,urlString,headers, params, res);


    }

    public static void get(String urlString, JsonHttpResponseHandler res)   //不带参数，获取json对象或�?数组
    {
        client.get(urlString, res);
    }

    public static void get(String urlString, RequestParams params, JsonHttpResponseHandler res)   //带参数，获取json对象或�?数组
    {
        client.get(urlString, params, res);
    }

    public static void get(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
    {
        client.get(uString, bHandler);

    }

    /********************
     * post请求
     *******************************/
    public static RequestHandle post(String urlString, AsyncHttpResponseHandler res)    //用一个完整url获取�?��string对象
    {
        return client.post(urlString, res);
    }

    public static RequestHandle post(String urlString, RequestParams params, AsyncHttpResponseHandler res)   //url里面带参�?
    {

        return client.post(urlString, params, res);
    }

    public static RequestHandle postFile(String urlString, RequestParams params, AsyncHttpResponseHandler res)   //url里面带参�?
    {
        return client.post(urlString, params, res);
    }

    public static RequestHandle post(String urlString, JsonHttpResponseHandler res)   //不带参数，获取json对象或�?数组
    {
        return client.post(urlString, res);
    }

    public static RequestHandle post(String urlString, RequestParams params, JsonHttpResponseHandler res)   //带参数，获取json对象或�?数组
    {
        return client.post(urlString, params, res);
    }

    public static RequestHandle post(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
    {
        return client.post(uString, bHandler);


    }

    public static RequestHandle post(Context context,String url,HttpEntity entity,String contentType,ResponseHandlerInterface responseHandler){

        return client.post(context,url,entity,contentType,responseHandler);


    }


    /********************
     * put请求
     *******************************/
    public static RequestHandle put(String urlString, RequestParams params, AsyncHttpResponseHandler res)   //url里面带参�?
    {

        return client.put(urlString, params, res);

    }
    public static RequestHandle put(Context context,String url,HttpEntity entity,String contentType,ResponseHandlerInterface responseHandler)   //url里面带参�?
    {

        return client.put(context,url,entity,contentType,responseHandler);


    }


    public static AsyncHttpClient getClient()
    {
        return client;
    }

}
