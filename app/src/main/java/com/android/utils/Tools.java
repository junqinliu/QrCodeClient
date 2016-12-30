package com.android.utils;

public class Tools {
	
	/**
	 * 获得10位的时间戳
	 * @return
	 */
	public static long getCurrentTimeStamp10() {
		long time1 = Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0, 10));
		return time1;
	}
	
	/**
	 *  生成二维码字符串
	 * @param buildingCode
	 * @param communityCode
	 * @param mode
	 * @return
	 */
	public static String createQrCodeStr(Integer buildingCode,Integer communityCode,String mode){
		StringBuilder qrcodeStr = new StringBuilder();
		String timeStamp = String.valueOf(getCurrentTimeStamp10());
		String buildingCodeStr = String.format("%03d", buildingCode);
		String communityCodeStr = String.format("%06d", communityCode);
		qrcodeStr.append(timeStamp).append(buildingCodeStr).append(communityCodeStr).append(mode).append("000");
		String orginalQrStr = qrcodeStr.toString();
		String enctyQR = MachineSecret.secret(orginalQrStr);
		return enctyQR;
	}
	
	public static void main(String[] args) {
		System.err.println(getCurrentTimeStamp10());
	}

}
