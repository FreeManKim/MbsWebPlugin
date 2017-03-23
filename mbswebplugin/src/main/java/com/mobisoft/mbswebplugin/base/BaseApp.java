package com.mobisoft.mbswebplugin.base;

import android.app.Application;

import com.mobisoft.mbswebplugin.view.LockPattern.LockPatternUtils;


public class BaseApp extends Application {
	private static BaseApp mInstance;
	private LockPatternUtils mLockPatternUtils;

	public static BaseApp getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mLockPatternUtils = new LockPatternUtils(this);
		ActivityManager.get().registerSelf(getApplicationContext());
	}

	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
}
