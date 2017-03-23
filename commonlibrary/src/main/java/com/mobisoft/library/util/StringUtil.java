package com.mobisoft.library.util;

import android.content.Context;

/**
 * 字符串操作工具包
 * 
 */
public class StringUtil {

	/**
	 * 判断给定字符串是否空白串。 <br >
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串<br >
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
	 * 
	 * @param sourceDate
	 * 
	 * @param formatLength
	 * 
	 * @return 重组后的数据
	 * 
	 */

	public static String addzero(int sourceDate, int formatLength) {

		// 0 指前面补充零 formatLength 字符总长度为 formatLength d 代表为正数。
		return String.format("%0" + formatLength + "d", sourceDate);

	}

	/**
	 * 获取字符串资源文件
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static String getStringRes(Context context, int id) {
		return context.getString(id) == null ? "" : context.getString(id);
	}

}
