package com.android.constant;

public class Constants {


	/**
	 * url地址
	 */
	public static final String HOST = "http://120.76.202.60:8080/microcard";

	/**
	 * 注册接口
	 */
	public static final String Register = "/v0.1/user/register";
	/**
	 * 登录接口
	 */
	public static final String Login = "/v0.1/user/login";
	/**
	 * 广告列表接口
	 */
	public static final String AdList = "/v0.1/ad";

	/**
	 * 我的信息 服务投诉 设备报修接口
	 */
	public static final String Property = "/v0.1/property";

	/**
	 * 修改密码
	 */

	public static final String  ModifyPwd = "/v0.1/user/updatePassword";
	/**
	 * 忘记密码
	 */

	public static final String  ForgetPassword = "/v0.1/user/forgetPassword";

	/**
	 * 注销
	 */
	public static final String  LoginOut = "/v0.1/user/loginout";

	/**
	 * 获取省份
	 */
	public static final String  Provice = "/v0.1/place/province";
	/**
	 * 获取城市
	 */
	public static final String  City = "/v0.1/place/city";
	/**
	 * 获取区县
	 */
	public static final String  Area = "/v0.1/place/area";

	/**
	 * 小区列表
	 */
	public static final String  House = "/v0.1/place/house";
	/**
	 * 楼栋列表
	 */
	public static final String  Block = "/v0.1/place/build";

	/**
	 * 往来日志
	 */
	public static final String  InviteLog = "/v0.1/user/invite";
	/**
	 * 邀请
	 */
	public static final String  Invite = "/v0.1/user/invite";

	/**
	 * 我的门禁列表
	 */
	public static final String  MyCardList = "/v0.1/place/build/card";

	/**
	 * 申请微卡提交申请
	 */
	public static final String  submitCardApply = "/v0.1/user/audit/apply";
}
