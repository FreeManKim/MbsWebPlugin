package com.mobisoft.mbswebplugin.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.mobisoft.mbswebplugin.view.LockPattern.LockPatternUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;


public class BaseApp extends Application {
	public static BaseApp mInstance;
	private LockPatternUtils mLockPatternUtils;

	public static RefWatcher getRefWatcher(Context context) {
		BaseApp application = (BaseApp) context.getApplicationContext();
		return application.refWatcher;
	}

	private RefWatcher refWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mLockPatternUtils = new LockPatternUtils(this);
		refWatcher = LeakCanary.install(this);
//		ActivityManager.get().registerSelf(getApplicationContext());
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				Log.i("app", " onViewInitFinished is " + arg0);
			}

			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb);


	}

	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
}
