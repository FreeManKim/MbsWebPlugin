package com.mobisoft.library.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {
	/**
	 * URL检查<br>
	 * <br>
	 * 
	 * @param pInput
	 *            要检查的字符串<br>
	 * @return boolean 返回检查结果<br>
	 */
	public static boolean isUrl(String pInput) {
		if (pInput == null) {
			return false;
		}
		String regEx = "^(http|https|ftp)//://([a-zA-Z0-9//.//-]+(//:[a-zA-"
				+ "Z0-9//.&%//$//-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
				+ "2}|[1-9]{1}[0-9]{1}|[1-9])//.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
				+ "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-4][0-9]|"
				+ "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-"
				+ "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
				+ "-9//-]+//.)*[a-zA-Z0-9//-]+//.[a-zA-Z]{2,4})(//:[0-9]+)?(/"
				+ "[^/][a-zA-Z0-9//.//,//?//'///////+&%//$//=~_//-@]*)*$";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(pInput);

		return matcher.matches();



	}

	/**
	 *
	 * @param pInput
	 * @return
     */
	public static Boolean isNewUrl(String pInput) {
		String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Pattern patt = Pattern. compile(regex );
		Matcher matcher1 = patt.matcher(pInput);
		boolean  isMatch = matcher1.matches();
		return  isMatch;
	}

}
