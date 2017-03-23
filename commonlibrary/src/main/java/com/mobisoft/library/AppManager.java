package com.mobisoft.library;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

public class AppManager  {

	private Stack<Activity> activityStack = new Stack<Activity>();

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	private static AppManager instance = new AppManager();

	private AppManager() {
	}

	public static AppManager getAppManager() {
		return instance;
	}

	/**
	 * 添加当前Activity 到堆栈
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityStack.push(activity);
	}

	/**
	 * 获取当前的Activity （堆栈中的最后一个压入的）
	 * 
	 * @return Activity
	 */
	public Activity getCurrentActivity() {
		return activityStack.lastElement();
	}

	/**
	 * 销毁当前的Activity
	 */
	public void finishActivity() {
		finishActivity(activityStack.lastElement());
	}

	/**
	 * 销毁指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null && !activityStack.isEmpty()) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 销毁指定的Activity
	 * 
	 * @param clazz
	 */
	public void finishActivity(Class<?> clazz) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(clazz)) {
				finishActivity(activity);
			}
		}
	}

	public void finishAllActivity() {
		for (Activity activity : activityStack) {
			finishActivity(activity);
		}
		activityStack.clear();
	}

	public boolean isExitActivity(Class<?> clazz) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(clazz)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 退出应用程序
	 */
	@SuppressWarnings("deprecation")
	public void ExitApp(Context context) {
		finishAllActivity();
		// ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// activityMgr.restartPackage(context.getPackageName());
		System.exit(0);
	}

}
