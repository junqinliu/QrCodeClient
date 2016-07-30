package com.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author liujq
 *
 */
public class SharedPreferenceUtil {

	private static final String XML_FILE_DATA = "DATA"; 
	private static SharedPreferenceUtil sharedPreferenceUtil;
	private SharedPreferences preferences;
	
	private SharedPreferenceUtil(Context context){
		
		preferences =  context.getSharedPreferences(XML_FILE_DATA,0);
	}
	
	
	public static SharedPreferenceUtil getInstance(Context context){
		
		if(sharedPreferenceUtil == null){
			
			sharedPreferenceUtil = new SharedPreferenceUtil(context);
		}
		
		return sharedPreferenceUtil;
	}
	
	/**
	 * �洢���
	 */
	public void putData(String key,Object value){
		
		if(value instanceof Boolean){
			
			preferences.edit().putBoolean(key, (boolean)value).commit();
		}
		
		if(value instanceof String){
			
			preferences.edit().putString(key, (String)value).commit();
		}
		
		if(value instanceof Integer){
			
			preferences.edit().putInt(key, (Integer)value).commit();
		}
		
		if(value instanceof Long){
			
			preferences.edit().putLong(key, (Long)value).commit();
		}
		
		if(value instanceof Float){
			
			preferences.edit().putFloat(key, (Float)value).commit();
		}
	  
		
	}

	/**
	 * 删除本地文件
	 */
	public void deleteData(){

		preferences.edit().clear().commit();
	}
	
	
	/**
	 * ��ȡSharedPreferences����
	 */
	public SharedPreferences getSharedPreferences(){
		
		return preferences;
	}
	
	
}
