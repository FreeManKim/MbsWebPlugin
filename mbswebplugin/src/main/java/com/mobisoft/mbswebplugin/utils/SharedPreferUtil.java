package com.mobisoft.mbswebplugin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharedpreferences
 * @author Li Yong
 *  2016年6月6日 上午9:15:03
 * @version V1.0
 * 缓存操作
 */
public class SharedPreferUtil {

	/**
	 * 修改 sharedpreferences 必须 commit
	 */

	private static SharedPreferences settings;
	private static SharedPreferences.Editor editor;
	private static SharedPreferUtil instance;

	public static final String PREF_NAME = "saveInfo";

	/**
	 * 单例模式
	 *
	 * @param context
	 *            上线文
	 * @return
	 */
	public static SharedPreferUtil getInstance(Context context) {

		return getInstance(context, Context.MODE_PRIVATE);
	}

	/**
	 * 单例模式
	 * 
	 * @param context
	 *            上线文
	 * @param spName
	 *            文件名
	 * @return
	 */
	public static SharedPreferUtil getInstance(Context context, String spName) {
		return getInstance(context, spName, Context.MODE_PRIVATE);
	}

	/**
	 * 单例模式
	 * 
	 * @param context
	 *            上线文
	 * @param mode
	 *            文件共享模式
	 * @return
	 */
	public static SharedPreferUtil getInstance(Context context, int mode) {
		if (instance == null) {
			instance = new SharedPreferUtil();
		}
		settings = context.getSharedPreferences(PREF_NAME, mode);
		editor = settings.edit();
		return instance;
	}

	/**
	 * 单例模式
	 *
	 * @param context
	 *            上线文
	 * @param spName
	 *            文件名
	 * @param mode
	 *            文件共享模式
	 * @return
	 */
	public static SharedPreferUtil getInstance(Context context, String spName, int mode) {
		if (instance == null) {
			instance = new SharedPreferUtil();
		}
		settings = context.getSharedPreferences(spName, mode);
		editor = settings.edit();//获取编辑器，主要用来存入信息
		return instance;
	}

	public String getPrefString(String key, final String defaultValue) {
		return settings.getString(key, defaultValue);
	}

	public void setPrefString(final String key, final String value) {
		settings.edit().putString(key, value).commit();
	}

	public boolean getPrefBoolean(final String key, final boolean defaultValue) {
		return settings.getBoolean(key, defaultValue);
	}

	public void setPrefBoolean(final String key, final boolean value) {
		settings.edit().putBoolean(key, value).commit();
	}

	public void setPrefInt(final String key, final int value) {
		settings.edit().putInt(key, value).commit();
	}

	public int getPrefInt(final String key, final int defaultValue) {
		return settings.getInt(key, defaultValue);
	}

	public void setPrefFloat(final String key, final float value) {
		settings.edit().putFloat(key, value).commit();
	}

	public float getPrefFloat(final String key, final float defaultValue) {
		return settings.getFloat(key, defaultValue);
	}

	public void setPrefLong(final String key, final long value) {

		settings.edit().putLong(key, value).commit();
	}

	public long getPrefLong(final String key, final long defaultValue) {
		return settings.getLong(key, defaultValue);
	}

	/**
	 *通过名称查看该首选项是否存在
	 */
	public boolean hasKey(final String key) {
		return settings.contains(key);
	}

	/**
	 * 通过名称移除某个首选项
	 */
	public void removeKey(String... keyArray) {
		for (String key : keyArray){
			editor.remove(key).commit();
		}
	}

	/**
	 * 移除所有首选项
	 */
	public void clearPreference() {
		editor.clear();
		editor.commit();
	}

	/**********************天信手势保存*******************************/
	public static String getCheckPasswork(Context mContext, String fileName, String keyName)
	{
		SharedPreferences preferences = mContext.getSharedPreferences(fileName,
				0);
		String initDataCount = preferences.getString(keyName, null);
		return initDataCount;
	}

	public static void setCheckPassword(Context mContext, String intStrCount, String fileName, String keyName)
	{
		SharedPreferences.Editor token = mContext.getSharedPreferences(
				fileName, 0).edit();
		token.putString(keyName, intStrCount);
		token.commit();
	}

}
