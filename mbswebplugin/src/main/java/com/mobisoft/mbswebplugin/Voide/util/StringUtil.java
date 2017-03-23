package com.mobisoft.mbswebplugin.Voide.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

	/**
	 * 将毫秒时间转化为分钟：秒钟格式
	 * @param str
	 * @return
	 */
	public static String simpleTime(long str) {
		SimpleDateFormat dateformat = new SimpleDateFormat("mm:ss");
		String a2 = dateformat.format(new Date(str));
		return a2;
	}
}
