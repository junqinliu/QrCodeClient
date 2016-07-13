package com.android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.qrcodeclient.R;
import com.android.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jisx on 2016/6/15.
 */
public class ExitHintDialog extends Dialog {

    Context context;

    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.confirm)
    TextView confirm;


    public ExitHintDialog(Context context) {
        super(context, R.style.PromptDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exit_hint);//布局
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = Utils.getScreenWidth(context) / 5 * 4; // 宽度设置为屏幕的宽度
        dialogWindow.setAttributes(p);
    }


    @OnClick({R.id.cancel, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                //退出
                dismiss();
                break;
        }
    }

    @Override
    public void dismiss() {
        ButterKnife.unbind(this);
        super.dismiss();
    }

}
