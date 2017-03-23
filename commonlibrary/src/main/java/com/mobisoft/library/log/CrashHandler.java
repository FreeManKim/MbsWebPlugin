package com.mobisoft.library.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.mobisoft.library.AppManager;
import com.mobisoft.library.util.DateUtil;
import com.mobisoft.library.util.FileUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler {

	private static final String TAG = "CrashHandler";

	/**
	 * 系统默认的UncaughtException处理类 Thread.UncaughtExceptionHandler : mDefaultHandler
	 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	/**
	 * CrashHandler实例 CrashHandler : mInstance
	 */
	private static CrashHandler mInstance = null;

	/**
	 * Context对象 Context : mContext
	 */
	private Context mContext;

	/**
	 * 用来存储设备信息和异常信息 Map<String,String> : mLogInfo
	 */
	private Map<String, String> mLogInfo = new HashMap<String, String>();

	/**
	 * Creates a new instance of CrashHandler.
	 */
	private CrashHandler() {

	}

	/**
	 * getInstance:{获取CrashHandler实例 ,单例模式 }
	 * 
	 * @return CrashHandler @throws
	 */

	public static CrashHandler getInstance() {
		if (mInstance == null) {
			mInstance = new CrashHandler();
		}
		return mInstance;

	}

	/**
	 * init:{初始化}
	 * 
	 * @param context
	 * @return void
	 */
	public void init(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);

	}

	/**
	 * handleException:{自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.}
	 * 
	 * @param throwable
	 * @return true:如果处理了该异常信息;否则返回false.
	 * @since I used to be a programmer like you, then I took an arrow in the knee Ver 1.0
	 * 
	 */

	public boolean handleException(Throwable throwable) {

		if (throwable == null)
			return false;

		new Thread() {

			@Override
			public void run() {

				Looper.prepare();

				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出", Toast.LENGTH_LONG).show();

				Looper.loop();

			}

		}.start();

		// 获取设备参数信息
		getDeviceInfo(mContext);
		// 保存日志文件
		saveCrashLogToFile(throwable);

		return true;

	}

	/**
	 * 
	 * 
	 * getDeviceInfo:{获取设备参数信息}
	 * 
	 * @param context
	 * 
	 * 
	 * @since I used to be a programmer like you, then I took an arrow in the knee Ver 1.0
	 */

	public void getDeviceInfo(Context context) {

		try {

			// 获得包管理器
			PackageManager mPackageManager = context.getPackageManager();
			// 得到该应用的信息，即主Activity
			PackageInfo mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);

			if (mPackageInfo != null) {
				String versionName = mPackageInfo.versionName == null ? "null" : mPackageInfo.versionName;
				String versionCode = String.valueOf(mPackageInfo.versionCode);
				mLogInfo.put("versionName", versionName);
				mLogInfo.put("versionCode", versionCode);
				mLogInfo.put("release", android.os.Build.VERSION.RELEASE);
				mLogInfo.put("model", android.os.Build.MODEL);
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();

		}

		// 反射机制
		Field[] mFields = Build.class.getDeclaredFields();

		// 迭代Build的字段key-value 此处的信息主要是为了在服务器端手机各种版本手机报错的原因
		for (Field field : mFields) {

			try {
				field.setAccessible(true);
				mLogInfo.put(field.getName(), field.get("").toString());
				TLog.d(TAG, field.getName() + ":" + field.get(""));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();

			}

		}

	}

	/**
	 * 
	 * saveCrashLogToFile:{将崩溃的Log保存到本地} <br >
	 * TODO 可拓展，将Log上传至指定服务器路径
	 * 
	 * @param throwable
	 * @return FileName
	 * 
	 * @since I used to be a programmer like you, then I took an arrow in the knee Ver 1.0
	 * 
	 */

	private String saveCrashLogToFile(Throwable throwable) {

		StringBuffer buffer = new StringBuffer();

		for (Map.Entry<String, String> entry : mLogInfo.entrySet()) {

			String key = entry.getKey();
			String value = entry.getValue();
			buffer.append(key + "=" + value + "\r\n");
		}

		Writer writer = new StringWriter();

		PrintWriter printWriter = new PrintWriter(writer);

		throwable.printStackTrace(printWriter);

		Throwable mThrowable = throwable.getCause();

		// 迭代栈队列把所有的异常信息写入writer中

		while (mThrowable != null) {

			mThrowable.printStackTrace(printWriter);
			// 换行 每个个异常栈之间换行
			printWriter.append("\r\n");
			mThrowable = mThrowable.getCause();

		}

		// 记得关闭
		printWriter.close();

		String mResult = writer.toString();
		// TLog.e(TAG, mResult);// 将错误信息打印到logCat
		buffer.append(mResult);

		// 保存文件，设置文件名
		String mTime = DateUtil.getCurrent();

		String mFileName = FileUtil.CRASH_MOBISOFT + "-" + mTime + FileUtil.ERROR_LOG_SUFFIX;

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			try {
				File mDirectory = new File(FileUtil.CRASH_DIRECTORY);

				if (!mDirectory.exists()) {
					mDirectory.mkdirs();
				}

				FileOutputStream mFileOutputStream = new FileOutputStream(mDirectory + File.separator + mFileName);
				mFileOutputStream.write(buffer.toString().getBytes());
				mFileOutputStream.close();

				return mFileName;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return null;

	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理 (non-Javadoc)
	 * 
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@SuppressWarnings("static-access")
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO 系统在try/catch后也无法捕获到的异常崩溃记录
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				// 如果处理了，让程序继续运行1秒再退出，保证文件保存并上传到服务器
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();

			}
			// 退出程序
			AppManager.getAppManager().ExitApp(mContext);

		}

	}

}
