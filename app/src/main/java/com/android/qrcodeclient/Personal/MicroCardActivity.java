package com.android.qrcodeclient.Personal;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.base.BaseAppCompatActivity;
import com.android.model.FamilyMicroCardBean;
import com.android.qrcodeclient.R;
import com.android.timeselect.wheelview.DateUtils;
import com.android.timeselect.wheelview.JudgeDate;
import com.android.timeselect.wheelview.ScreenInfo;
import com.android.timeselect.wheelview.WheelMain;
import com.android.utils.TextUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;

/**
 * Created by liujq on 2016/12/21.家属微卡
 */
public class MicroCardActivity extends BaseAppCompatActivity implements View.OnClickListener{

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_center)
    TextView tv_center;

    @Bind(R.id.family_member_name_edit)
    EditText family_member_name_edit;
    @Bind(R.id.family_member_phone_edit)
    EditText family_member_phone_edit;
    @Bind(R.id.family_member_pwd_edit)
    EditText family_member_pwd_edit;
    @Bind(R.id.family_member_start_edit)
    EditText family_member_start_edit;
    @Bind(R.id.family_member_end_edit)
    EditText family_member_end_edit;

    @Bind(R.id.family_member_pwd_layout)
    LinearLayout family_member_pwd_layout;

    private WheelMain wheelMainDate;
    private String beginTime;
    FamilyMicroCardBean familyMicroCardBean = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_card);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //设置标题
        String titleName = getIntent().getStringExtra(getResources().getString(R.string.develop_title));
        if (!TextUtil.isEmpty(titleName)) {
            title.setText(titleName);
        }

        familyMicroCardBean = (FamilyMicroCardBean)getIntent().getSerializableExtra("FamilyMicroCardBean");

        if(familyMicroCardBean != null ){


            //初始化数据
            title.setText("编辑家属");
            family_member_name_edit.setText(familyMicroCardBean.getFamilyName());
            family_member_phone_edit.setText(familyMicroCardBean.getFamilyPhone());
            family_member_pwd_edit.setText(familyMicroCardBean.getFamilyPwd());
            family_member_start_edit.setText(familyMicroCardBean.getFamilyStart());
            family_member_end_edit.setText(familyMicroCardBean.getFamilyEnd());

            family_member_phone_edit.setFocusable(false);
            family_member_pwd_layout.setVisibility(View.GONE);

        }

    }

    @Override
    public void setListener() {

        toolbar.setNavigationOnClickListener(this);
        family_member_start_edit.setOnClickListener(this);
        family_member_end_edit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.family_member_start_edit:

                showBottoPopupWindow(family_member_start_edit);
                break;

            case R.id.family_member_end_edit:

                showBottoPopupWindow(family_member_end_edit);
                break;

            default:
                break;

        }
    }

    private java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public void showBottoPopupWindow( final EditText tv) {
        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.show_popup_window,null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, (int)(width*0.8),
                ActionBar.LayoutParams.WRAP_CONTENT);
        ScreenInfo screenInfoDate = new ScreenInfo(this);
        wheelMainDate = new WheelMain(menuView, true);
        wheelMainDate.screenheight = screenInfoDate.getHeight();
        String time = DateUtils.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMainDate.initDateTimePicker(year, month, day, hours,minute);
        final String currentTime = wheelMainDate.getTime().toString();
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(tv_center, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        tv_pop_title.setText("选择时间");
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                beginTime = wheelMainDate.getTime().toString();
                try {
                    Date begin = dateFormat.parse(currentTime);
                    Date end = dateFormat.parse(beginTime);
                    tv.setText(DateUtils.formateStringH(beginTime,DateUtils.yyyyMMddHHmm));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
    }
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

}
