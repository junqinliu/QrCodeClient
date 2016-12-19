package com.android.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.qrcodeclient.R;
import com.android.utils.TextUtil;

import butterknife.ButterKnife;

/**
 * Created by liujunqin on 2016/5/25.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements CommonInterface{

    Dialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

    }


    public void showToast(String text) {
        if (TextUtil.isEmpty(text)) {
            return;
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void goNext(Class toClass, Bundle bundle) {
        Intent intent = new Intent(this, toClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void goNext(Class toClass) {
        Intent intent = new Intent(this, toClass);
        startActivity(intent);
    }

    public void showLoadingDialog() {
        if (dialog == null) {

            final LayoutInflater inflater = LayoutInflater.from(this);
            dialog = new Dialog(this, R.style.dialog);
            View dialogLayout = inflater.inflate(R.layout.dialog_loading, null);
            ImageView loadimg = ((ImageView) dialogLayout.findViewById(R.id.loading_img));

            Animation operatingAnim = AnimationUtils.loadAnimation(BaseAppCompatActivity.this, R.anim.loading_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);

            if (operatingAnim != null) {
                loadimg.startAnimation(operatingAnim);
            }

            dialog.setContentView(dialogLayout);
        } else {
            ImageView loadimg = ((ImageView) dialog.findViewById(R.id.loading_img));

            Animation operatingAnim = AnimationUtils.loadAnimation(BaseAppCompatActivity.this, R.anim.loading_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);

            if (operatingAnim != null) {
                loadimg.startAnimation(operatingAnim);
            }
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void closeLoadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

}
