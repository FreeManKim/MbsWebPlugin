package com.mobisoft.library.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.WindowManager;

import com.mobisoft.library.AppConfig;
import com.mobisoft.library.Constants;
import com.mobisoft.library.util.FileUtil;
import com.mobisoft.library.util.PreferenceUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.UUID;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//		CrashHandler.getInstance().init(getApplicationContext());// 注册crash监控
//		deleteCrash();
        getPackageInfo();
        getScreenInfo();
        createUUID();

    }

    private void createUUID() {
        if (PreferenceUtil.getInstance(getApplicationContext(), AppConfig.APP).hasKey(AppConfig.APP_UUID)//
                && null != PreferenceUtil.getInstance(getApplicationContext(), AppConfig.APP)
                .getPrefString(AppConfig.APP_UUID, "")//
                && !"".equals(PreferenceUtil.getInstance(getApplicationContext(), AppConfig.APP)
                .getPrefString(AppConfig.APP_UUID, "").trim())//
                ) {
            AppConfig.UUID = PreferenceUtil.getInstance(getApplicationContext(), AppConfig.APP)
                    .getPrefString(AppConfig.APP_UUID, UUID.randomUUID().toString());

        } else {
            AppConfig.UUID = UUID.randomUUID().toString();
        }
        PreferenceUtil.getInstance(getApplicationContext(), AppConfig.APP).setPrefString(AppConfig.APP_UUID,
                AppConfig.UUID);
    }

    // 删除在当天之前的所有crash文件，使用线程不论成功失败，均要进入APP
    private void deleteCrash() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // 删除crash
                File mCrashDirectory = new File(FileUtil.CRASH_DIRECTORY);
                if (mCrashDirectory.exists()) {// 文件夹存在
                    File[] files = mCrashDirectory.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return filename.startsWith(FileUtil.CRASH_MOBISOFT)
                                    && filename.endsWith(FileUtil.ERROR_LOG_SUFFIX);

                        }
                    });
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    for (File file : files) {
                        if ((currentTime - file.lastModified()) / 86400000 > 0) {
                            file.delete();
                        }
                    }
                }

                // 删除log
                mCrashDirectory = new File(FileUtil.ERROR_LOG_DIRECTORY);
                if (mCrashDirectory.exists()) {// 文件夹存在
                    File[] files = mCrashDirectory.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return filename.endsWith(FileUtil.ERROR_LOG_SUFFIX);

                        }
                    });
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    for (File file : files) {
                        if ((currentTime - file.lastModified()) / 86400000 > 0) {
                            file.delete();
                        }
                    }
                }

            }
        }).start();
    }

    // 取得当前APP的包的相关信息
    private void getPackageInfo() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);

            Constants.VERSION_CODE = info.versionCode;
            Constants.VERSION_NAME = info.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获取屏幕的相关信息
    @SuppressWarnings("deprecation")
    private void getScreenInfo() {

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Constants.SCREEN_WIDTH = wm.getDefaultDisplay().getWidth();// 屏幕分辨率
        Constants.SCREEN_HEIGTH = wm.getDefaultDisplay().getHeight();
    }


}
