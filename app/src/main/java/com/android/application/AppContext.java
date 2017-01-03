package com.android.application;


import android.app.Application;
import com.android.exception.CrashHandler;
import com.android.model.AddressBean;
import com.android.model.CBBean;
import com.android.model.KeyAddressBean;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;


public class AppContext extends Application {
	
	private static AppContext appContext;
	AddressBean addressBean;
	CBBean cBBean;
	KeyAddressBean keyAddressBean;
	
	/**
	 *
	 * 将在Application中注册未捕获异常处理器。
	 */
	@Override
	public void onCreate() {

		super.onCreate();
	
		CrashHandler crashHandler = CrashHandler.getInstance();
		// 注册crashHandler
		crashHandler.init(getApplicationContext());

		//社会化分享初始化
		try{

			ShareSDK.initSDK(this);

		}catch (Exception e){

		}

		//SMMSDK初始化
		SMSSDK.initSDK(this, "146d57ebbef52", "8a6b993fb9b85a0998a51729374ea4c1");

		//极光推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		
	}


	
	public static AppContext getInstance() {
		return appContext;
	}

	public AddressBean getAddressBean() {
		return addressBean;
	}

	public void setAddressBean(AddressBean addressBean) {
		this.addressBean = addressBean;
	}

	public CBBean getcBBean() {
		return cBBean;
	}

	public void setcBBean(CBBean cBBean) {
		this.cBBean = cBBean;
	}

	public KeyAddressBean getKeyAddressBean() {
		return keyAddressBean;
	}

	public void setKeyAddressBean(KeyAddressBean keyAddressBean) {
		this.keyAddressBean = keyAddressBean;
	}
}
