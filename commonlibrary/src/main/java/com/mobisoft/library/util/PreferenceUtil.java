package com.mobisoft.library.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

	/**
	 * 
	 * 修改 sharedpreferences 必须 commit
	 */

	private static SharedPreferences settings;
	private static SharedPreferences.Editor editor;
	private static PreferenceUtil instance;

	/**
	 * 单例模式
	 * 
	 * @param context
	 *            上线文
	 * @param spName
	 *            文件名
	 * @return
	 */
	public static PreferenceUtil getInstance(Context context, String spName) {
		return getInstance(context, spName, Context.MODE_PRIVATE);
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
	public static PreferenceUtil getInstance(Context context, String spName, int mode) {
		if (instance == null) {
			instance = new PreferenceUtil();
		}
		settings = context.getSharedPreferences(spName, mode);
		editor = settings.edit();
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

	public boolean hasKey(final String key) {
		return settings.contains(key);
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

	public void setSettingLong(final String key, final long value) {

		settings.edit().putLong(key, value).commit();
	}

	public long getPrefLong(final String key, final long defaultValue) {
		return settings.getLong(key, defaultValue);
	}

	public void clearPreference() {
		editor.clear();
		editor.commit();
	}

	public boolean removeKey(final String key) {
		return editor.remove(key).commit();
	}
}
