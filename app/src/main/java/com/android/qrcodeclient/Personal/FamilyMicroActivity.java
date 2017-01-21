package com.android.qrcodeclient.Personal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.android.adapter.FamilyMicroAdapter;
import com.android.application.ExitApplication;
import com.android.base.BaseAppCompatActivity;
import com.android.constant.Constants;
import com.android.model.FamilyMicroCardBean;
import com.android.qrcodeclient.LoginActivity;
import com.android.qrcodeclient.R;
import com.android.utils.DialogMessageExit;
import com.android.utils.HttpUtil;
import com.android.utils.NetUtil;
import com.android.utils.SharedPreferenceUtil;
import com.android.utils.TextUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;

/**
 * Created by liujq on 2016/12/20.
 */
public class FamilyMicroActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Bind(R.id.listView)
    ListView listView;

    @Bind(R.id.add_img)
    ImageView add_img;


    List<FamilyMicroCardBean> list;
    List<FamilyMicroCardBean> listTemp = new ArrayList<>();

    FamilyMicroAdapter adapter;
    int pageNumber = 0;
    int pageSize = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
    }

    @Override
    public void initView() {


    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        add_img.setImageResource(android.R.drawable.ic_menu_add);
        add_img.setVisibility(View.VISIBLE);
        //设置标题
        String titleName = getIntent().getStringExtra(getResources().getString(R.string.develop_title));
        if (!TextUtil.isEmpty(titleName)) {
            title.setText(titleName);
        }

        list = new ArrayList<>();
        adapter = new FamilyMicroAdapter(this,list);
        listView.setAdapter(adapter);
        getFamilyCardList();
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(this);

        add_img .setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(getResources().getString(R.string.develop_title), "添加家属");
                bundle.putString("flag", "add");
                goNext(MicroCardActivity.class, bundle);
            }


        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Bundle bundle = new Bundle();
                bundle.putString(getResources().getString(R.string.develop_title), "编辑家属");
                bundle.putString("flag", "ediit");
                bundle.putSerializable("FamilyMicroCardBean", (FamilyMicroCardBean) list.get(arg2));
                goNext(MicroCardActivity.class, bundle);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                index = i;
               // deleteFamilyMember(list.get(i).getId(),i);
                new AlertDialog.Builder(FamilyMicroActivity.this, AlertDialog.THEME_HOLO_LIGHT).setTitle("提示")
                        .setMessage("确定删除该家属？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", dialogListener).create().show();

                return true;
            }
        });

    }

   int index;
    // 提示框按钮监听
    android.content.DialogInterface.OnClickListener dialogListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            deleteFamilyMember(list.get(index).getId(),index);

        }
    };



    @Override
    public void onClick(View v) {
        finish();
    }



    /**
     * 家属微卡列表
     */
    private void getFamilyCardList(){

        RequestParams params = new RequestParams();
        params.put("pageSize",pageSize);
        params.put("pageNumber",pageNumber);
        HttpUtil.get(Constants.HOST + Constants.FamilyCardList, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetUtil.checkNetInfo(FamilyMicroActivity.this)) {

                    showToast("当前网络不可用,请检查网络");
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

                                list.clear();
                                listTemp.clear();

                                JSONObject gg = new JSONObject(jsonObject.getString("data"));
                                listTemp = JSON.parseArray(gg.getJSONArray("items").toString(), FamilyMicroCardBean.class);
                                list.addAll(listTemp);
                                adapter.notifyDataSetChanged();


                            } else {

                                if("0".equals(jsonObject.getString("code"))){

                                    DialogMessageExit.getInstance(FamilyMicroActivity.this).showDialog();


                                }else{

                                    showToast(jsonObject.getString("msg"));
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

    @Override
    protected void onResume() {
        super.onResume();

        getFamilyCardList();
    }


    /**
     * 删除某个家属
     */
    public void deleteFamilyMember(String id, final int i){

        RequestParams params = new RequestParams();
        params.put("id",id);

        HttpUtil.get(Constants.HOST + Constants.DeleteFamilyMember, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if(!NetUtil.checkNetInfo(FamilyMicroActivity.this)){

                    showToast("当前网络不可用,请检查网络");
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

                                list.remove(i);
                                adapter.notifyDataSetChanged();


                            } else {

                                if("0".equals(jsonObject.getString("code"))){

                                    DialogMessageExit.getInstance(FamilyMicroActivity.this).showDialog();


                                }else{

                                    showToast(jsonObject.getString("msg"));
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


}
