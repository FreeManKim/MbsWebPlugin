package com.mobisoft.library.util;

import java.io.File;

import android.os.Environment;

/**
 * 文件操作工具包
 * 
 */
public class FileUtil {
	public static final String ROOT_MOBISOFT = "mobisoft";
	public static final String CRASH_MOBISOFT = "crash";
	public static final String LOG_MOBISOFT = "log";

	/**
	 * crash 存放文件夹
	 */
	public static final String CRASH_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator
			+ FileUtil.ROOT_MOBISOFT + File.separator + FileUtil.CRASH_MOBISOFT;

	/**
	 * 错误日志文件后缀
	 */
	public static final String ERROR_LOG_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator
			+ FileUtil.ROOT_MOBISOFT + File.separator + FileUtil.LOG_MOBISOFT;

	/**
	 * error文件后缀
	 */
	public static final String ERROR_LOG_SUFFIX = ".log";

}
