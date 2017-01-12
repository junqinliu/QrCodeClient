package com.android.constant;

public class Constants {


	/**
	 * url地址
	 */

	//public static final String HOST = "http://diyidaojia.com:8888";
	public static final String HOST = "http://101.201.223.67:8888";


	/**
	 * 注册接口
	 */
	public static final String Register = "/v0.1/user/register";
	/**
	 * 登录接口
	 */
	public static final String Login = "/v0.1/user/login";

	/**
	 * 获取用户信息
	 */
	public static final String getUserInfo = "/v0.1/user";

	/**
	 * 轮播广告列表接口
	 */

	public static final String AdList = "/v0.1/ad/carouselFigure";
	/**
	 * 2号广告接口
	 */

	public static final String QrcodeAdv = "/v0.1/ad/qrcodeAdv";

	/**
	 * 生活板块九宫格接口
	 */
	public static final String LifeAd = "/v0.1/ad/lifePlate";

	/**
	 * 社区商城九宫格接口
	 */
	public static final String CommunityMallsAd = "/v0.1/ad/communityMalls";

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
	 * 搜索小区
	 */
	public static final String  SearchHouse = "/v0.1/place/house";
	/**
	 * 楼栋列表
	 */
	public static final String  Block = "/v0.1/place/build";
	/**
	 * 楼栋列表(多次楼栋列表)
	 */
	public static final String  OtherBlock = "/v0.1/place/build/owner";

	/**
	 * 往来日志
	 */
	public static final String  InviteLog = "/v0.1/user/invite";
	/**
	 * 我的消息/家政服务
	 */
	public static final String  Message = "/v0.1/property";
	/**
	 * 邀请
	 */
	public static final String  Invite = "/v0.1/user/invite";
	/**
	 * 用户的小区列表
	 */
	public static final String  MyHouseList = "/v0.1/place/myhouses";
	/**
	 * 我的门禁列表
	 */
	public static final String  MyCardList = "/v0.1/place/build/card";
	/**
	 * 单个门禁对应的二维码
	 */
	public static final String  GetQrCodeByBuild = "/v0.1/place/build";

	/**
	 * 申请微卡提交申请(需求变更 这个接口没用了)
	 */
	public static final String  submitCardApply = "/v0.1/user/audit/apply";
	/**
	 * 申请门禁卡提交申请
	 */
	public static final String  CardApply = "/v0.1/place/build/card/owner";


	/**
	 * 家属微卡列表
	 */
	public static final String  FamilyCardList = "/v0.1/user/getFamilyMembers";

	/**
	 * 添加家属
	 */
	public static final String  AddFamilyMember = "/v0.1/user/addFamilyMember";
	/**
	 * 编辑家属
	 */
	public static final String  EditFamilyMember = "/v0.1/user/updateFamilyMember";
	/**
	 * 删除家属
	 */
	public static final String  DeleteFamilyMember = "/v0.1/user/delFamilyMember";


	/**
	 * 获取客户电话号码
	 */

	public static final String  GetPhone = "/v0.1/place/communityContactNumber";
	/**
	 * 提交错误日志
	 */

	public static final String  ErrorReport = "/v0.1/exception/report";
	/**
	 * APP 更新接口
	 */

	public static final String  UpdateAPPVersion = "/v0.1/version/checkUpdate";
	/**
	 * 离线功能接口
	 */

	public static final String  OfflineData = "/v0.1/place/offline/mycards";


}
