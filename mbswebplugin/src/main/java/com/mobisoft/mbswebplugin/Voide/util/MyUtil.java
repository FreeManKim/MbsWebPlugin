package com.mobisoft.mbswebplugin.Voide.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.Surface;

public class MyUtil {

	/**
	 * 设置声音
	 *
	 * @param context
	 * @param tempVolume
	 *            比例
	 */
	public static void setVolume(Context context, int tempVolume) {
		// 音量控制,初始化定义
		AudioManager mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// 当前音量
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, tempVolume, 0); // tempVolume:音量绝对值
	}

	/**
	 * 获取当前音量
	 *
	 * @param context
	 * @return
	 */
	public static int getCurrentVolume(Context context) {
		// 音量控制,初始化定义
		AudioManager mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// 当前音量
		int currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		return currentVolume;
	}

	/**
	 * 获取最大的声音
	 *
	 * @param context
	 * @return
	 */
	public static int getMaxVolume(Context context) {
		// 音量控制,初始化定义
		AudioManager mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// 当前音量
		int maxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		return maxVolume;
	}

	/**
	 * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	public static int getScreenMode(Context context) {
		int screenMode = 0;
		try {
			screenMode = Settings.System.getInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE);
		} catch (Exception localException) {

		}
		return screenMode;
	}

	/**
	 * 获得当前屏幕亮度值 0--255
	 */
	public static int getScreenBrightness(Context context) {
		int screenBrightness = 255;
		try {
			screenBrightness = Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception localException) {

		}
		return screenBrightness;
	}

	/**
	 * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	public static void setScreenMode(Context context, int paramInt) {
		try {
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 设置当前屏幕亮度值 0--255
	 */
	public static void saveScreenBrightness(Context context, int paramInt) {
		try {
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}


	public void setScreenLock(Activity context, boolean isLock){
		if(isLock){
			int rotation = context.getWindowManager().getDefaultDisplay().getRotation();
			switch (rotation) {
				case Surface.ROTATION_0:	//竖屏向上
					context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
					break;
				case Surface.ROTATION_90:	//横屏 	顺时针转90度
					context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					break;
				case Surface.ROTATION_180://竖屏
					context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					break;
				case Surface.ROTATION_270://横屏
					context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					break;
				default:
					break;
			}

		}else{
			context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
	}

	/**
	 * 修改横屏竖屏模式！
	 * @param context
	 * @param flag
	 */
	public void setScreenMode(Activity context, boolean flag){
		if(flag){
			int rotation = context.getWindowManager().getDefaultDisplay().getRotation();
			switch (rotation) {
				case Surface.ROTATION_0:	//竖屏向上
					context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					break;
				case Surface.ROTATION_90:	//横屏 	顺时针转90度
					context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
					break;
				case Surface.ROTATION_180://竖屏
					context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					break;
				case Surface.ROTATION_270://横屏
					context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
					break;
				default:
					context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					break;
			}
		}else{
			context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
	}


}
