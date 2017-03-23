package com.mobisoft.library.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import com.mobisoft.library.log.TLog;

public class DateUtil {
	// 格式：年－月－日 小时：分钟：秒
	public static final String FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";

	// 格式：年－月－日 小时：分钟
	public static final String FORMAT_TWO = "yyyy-MM-dd HH:mm";

	// 格式：年月日 小时分钟秒
	public static final String FORMAT_THREE = "yyyyMMdd-HHmmss";

	// 格式：年－月－日
	public static final String LONG_DATE_FORMAT = "yyyy-MM-dd";

	// 格式：月－日
	public static final String SHORT_DATE_FORMAT = "MM-dd";

	// 格式：小时：分钟：秒
	public static final String LONG_TIME_FORMAT = "HH:mm:ss";

	// 格式：年-月
	public static final String MONTG_DATE_FORMAT = "yyyy-MM";

	// 年的加减
	public static final int SUB_YEAR = Calendar.YEAR;

	// 月加减
	public static final int SUB_MONTH = Calendar.MONTH;

	// 天的加减
	public static final int SUB_DAY = Calendar.DATE;

	// 小时的加减
	public static final int SUB_HOUR = Calendar.HOUR;

	// 分钟的加减
	public static final int SUB_MINUTE = Calendar.MINUTE;

	// 秒的加减
	public static final int SUB_SECOND = Calendar.SECOND;

	static final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DateUtil() {

	}

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 * 
	 * @param dateStr
	 * @return
	 */

	@SuppressLint("SimpleDateFormat")
	public static java.util.Date string2Date(String dateStr, String format) {

		Date d = null;
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			formater.setLenient(false);
			d = formater.parse(dateStr);
		} catch (Exception e) {
			d = null;
		}

		return d;

	}

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 */

	@SuppressLint("SimpleDateFormat")
	public static java.util.Date string2Date(String dateStr, String format, ParsePosition pos) {

		Date d = null;

		SimpleDateFormat formater = new SimpleDateFormat(format);

		try {

			formater.setLenient(false);
			d = formater.parse(dateStr, pos);

		} catch (Exception e) {

			d = null;

		}

		return d;

	}

	/**
	 * 把日期转换为字符串
	 * 
	 * @param date
	 * @return
	 */

	@SuppressLint("SimpleDateFormat")
	public static String date2String(java.util.Date date, String format) {

		String result = "";
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			result = formater.format(date);
		} catch (Exception e) {
			// log.error(e);
		}

		return result;

	}

	/**
	 * 获取当前时间的指定格式
	 * 
	 * @param format
	 * 
	 * @return
	 */

	public static String getCurrDate(String format) {

		return date2String(new Date(), format);

	}

	/**
	 * @param dateStr
	 * @param amount
	 * @return
	 */

	public static String dateSub(int dateKind, String dateStr, int amount) {

		Date date = string2Date(dateStr, FORMAT_ONE);

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		calendar.add(dateKind, amount);

		return date2String(calendar.getTime(), FORMAT_ONE);

	}

	/**
	 * 两个日期相减
	 * 
	 * @param firstTime
	 * @param secTime
	 * @return 相减得到的秒数
	 */

	public static long timeSub(String firstTime, String secTime) {

		long first = string2Date(firstTime, FORMAT_ONE).getTime();

		long second = string2Date(secTime, FORMAT_ONE).getTime();

		return (second - first) / 1000;

	}

	/**
	 * 
	 * 获得某月的天数
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return int
	 */

	public static int getDaysOfMonth(String year, String month) {

		int days = 0;

		if (month.equals("1") || month.equals("3") || month.equals("5") || month.equals("7") || month.equals("8")
				|| month.equals("10") || month.equals("12")) {

			days = 31;

		} else if (month.equals("4") || month.equals("6") || month.equals("9") || month.equals("11")) {

			days = 30;

		} else {

			if ((Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0)
					|| Integer.parseInt(year) % 400 == 0) {

				days = 29;

			} else {

				days = 28;

			}

		}

		return days;

	}

	/**
	 * 获取某年某月的天数
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int 月份[1-12]
	 * @return int
	 */

	public static int getDaysOfMonth(int year, int month) {

		Calendar calendar = Calendar.getInstance();

		calendar.set(year, month - 1, 1);

		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

	}

	/**
	 * 获得当前日期
	 * 
	 * @return int
	 */

	public static int getToday() {

		Calendar calendar = Calendar.getInstance();

		return calendar.get(Calendar.DATE);

	}

	/**
	 * 获得当前月份
	 * 
	 * @return int
	 */

	public static int getToMonth() {

		Calendar calendar = Calendar.getInstance();

		return calendar.get(Calendar.MONTH) + 1;

	}

	/**
	 * 获得当前年份
	 * 
	 * @return int
	 */

	public static int getToYear() {

		Calendar calendar = Calendar.getInstance();

		return calendar.get(Calendar.YEAR);

	}

	/**
	 * 返回日期的天
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */

	public static int getDay(Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.DATE);

	}

	/**
	 * 返回日期的年
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */

	public static int getYear(Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.YEAR);

	}

	/**
	 * 返回日期的月份，1-12
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */

	public static int getMonth(Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.MONTH) + 1;

	}

	/**
	 * 计算两个日期相差的天数，如果date2 > date1 返回正数，否则返回负数
	 * 
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return long
	 */

	public static long dayDiff(Date date1, Date date2) {

		return (date2.getTime() - date1.getTime()) / 86400000;

	}

	/**
	 * 比较两个日期的年差
	 * 
	 * @param befor
	 * @param after
	 * @return
	 * 
	 */

	public static int yearDiff(String before, String after) {

		Date beforeDay = string2Date(before, LONG_DATE_FORMAT);

		Date afterDay = string2Date(after, LONG_DATE_FORMAT);

		return getYear(afterDay) - getYear(beforeDay);

	}

	/**
	 * 比较指定日期与当前日期的差
	 * 
	 * @param befor
	 * @param after
	 * @return
	 */

	public static int yearDiffCurr(String after) {

		Date beforeDay = new Date();

		Date afterDay = string2Date(after, LONG_DATE_FORMAT);

		return getYear(beforeDay) - getYear(afterDay);

	}

	/**
	 * 
	 * 比较指定日期与当前日期的差
	 * 
	 * @param before
	 * 
	 * @return
	 * 
	 */

	public static long dayDiffCurr(String before) {

		Date currDate = DateUtil.string2Date(currDay(), LONG_DATE_FORMAT);

		Date beforeDate = string2Date(before, LONG_DATE_FORMAT);

		return (currDate.getTime() - beforeDate.getTime()) / 86400000;

	}

	/**
	 * 
	 * 获取每月的第一周
	 * 
	 * @param year
	 * 
	 * @param month
	 * 
	 * @return
	 * 
	 */

	public static int getFirstWeekdayOfMonth(int year, int month) {

		Calendar c = Calendar.getInstance();

		c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天

		c.set(year, month - 1, 1);

		return c.get(Calendar.DAY_OF_WEEK);

	}

	/**
	 * 
	 * 获取每月的最后一周
	 * 
	 * @param year
	 * 
	 * @param month
	 * 
	 * @return
	 * 
	 */

	public static int getLastWeekdayOfMonth(int year, int month) {

		Calendar c = Calendar.getInstance();

		c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天

		c.set(year, month - 1, getDaysOfMonth(year, month));

		return c.get(Calendar.DAY_OF_WEEK);

	}

	/**
	 * 
	 * 获得当前日期字符串，格式"yyyy_MM_dd_HH_mm_ss"
	 * 
	 * @return
	 * 
	 */ 
	public static String getCurrent() {

		Calendar cal = Calendar.getInstance();

		cal.setTime(new Date());

		int year = cal.get(Calendar.YEAR);

		int month = cal.get(Calendar.MONTH) + 1;

		int day = cal.get(Calendar.DAY_OF_MONTH);

		int hour = cal.get(Calendar.HOUR_OF_DAY);

		int minute = cal.get(Calendar.MINUTE);

		int second = cal.get(Calendar.SECOND);

		StringBuffer sb = new StringBuffer();

		sb.append(year).append("_").append(StringUtil.addzero(month, 2)).append("_").append(StringUtil.addzero(day, 2))
				.append("_").append(StringUtil.addzero(hour, 2)).append("_")

		.append(StringUtil.addzero(minute, 2)).append("_").append(StringUtil.addzero(second, 2));

		return sb.toString();

	}

	/**
	 * 
	 * 获得当前日期字符串，格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 * 
	 */

	public static String getNow() {
		Calendar today = Calendar.getInstance();
		return date2String(today.getTime(), FORMAT_ONE);
	}

	/**
	 * 
	 * 
	 * 判断日期是否有效,包括闰年的情况
	 * 
	 * 
	 * @param date
	 * 
	 * 
	 *            YYYY-mm-dd
	 * 
	 * 
	 * @return
	 * 
	 * 
	 */

	public static boolean isDate(String date) {

		StringBuffer reg = new StringBuffer("^((\\d{2}(([02468][048])|([13579][26]))-?((((0?");

		reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))");

		reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|");

		reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12");

		reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))");

		reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");

		reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[");

		reg.append("1-9])|(1[0-9])|(2[0-8]))))))");

		Pattern p = Pattern.compile(reg.toString());

		return p.matcher(date).matches();

	}

	/**
	 * 
	 * 
	 * 取得指定日期过 months 月后的日期 (当 months 为负数表示指定月之前);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param date
	 * 
	 * 
	 *            日期 为null时表示当天
	 * 
	 * 
	 * @param month
	 * 
	 * 
	 *            相加(相减)的月数
	 * 
	 * 
	 */

	public static Date nextMonth(Date date, int months) {

		Calendar cal = Calendar.getInstance();

		if (date != null) {

			cal.setTime(date);

		}

		cal.add(Calendar.MONTH, months);

		return cal.getTime();

	}

	/**
	 * 
	 * 
	 * 取得指定日期过 day 天后的日期 (当 day 为负数表示指日期之前);
	 * 
	 * 
	 * @param date
	 * 
	 * 
	 *            日期 为null时表示当天
	 * 
	 * 
	 * @param month
	 * 
	 * 
	 *            相加(相减)的月数
	 * 
	 * 
	 */

	public static Date nextDay(Date date, int day) {

		Calendar cal = Calendar.getInstance();

		if (date != null) {

			cal.setTime(date);

		}

		cal.add(Calendar.DAY_OF_YEAR, day);

		return cal.getTime();

	}

	/**
	 * 
	 * 
	 * 取得距离今天 day 日的日期
	 * 
	 * @param day
	 * 
	 * 
	 * @param format
	 * 
	 * 
	 * @return
	 * 
	 * 
	 * @author chenyz
	 * 
	 * 
	 */

	public static String nextDay(int day, String format) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(new Date());

		cal.add(Calendar.DAY_OF_YEAR, day);

		return date2String(cal.getTime(), format);

	}

	/**
	 * 
	 * 
	 * 取得指定日期过 day 周后的日期 (当 day 为负数表示指定月之前)
	 * 
	 * @param date
	 * 
	 *            日期 为null时表示当天
	 * 
	 */

	public static Date nextWeek(Date date, int week) {

		Calendar cal = Calendar.getInstance();

		if (date != null) {

			cal.setTime(date);

		}

		cal.add(Calendar.WEEK_OF_MONTH, week);

		return cal.getTime();

	}

	/**
	 * 
	 * 
	 * 获取当前的日期(yyyy-MM-dd)
	 * 
	 * 
	 */

	public static String currDay() {

		return DateUtil.date2String(new Date(), DateUtil.LONG_DATE_FORMAT);

	}

	/**
	 * 
	 * 获取昨天的日期
	 * 
	 * 
	 * @return
	 * 
	 * 
	 */

	public static String befoDay() {

		return befoDay(DateUtil.LONG_DATE_FORMAT);

	}

	/**
	 * 
	 * 根据时间类型获取昨天的日期
	 * 
	 * @param format
	 * 
	 * @return
	 * 
	 * @author chenyz
	 * 
	 * 
	 */

	public static String befoDay(String format) {

		return DateUtil.date2String(DateUtil.nextDay(new Date(), -1), format);

	}

	/**
	 * 
	 * 获取明天的日期
	 * 
	 */

	public static String afterDay() {

		return DateUtil.date2String(DateUtil.nextDay(new Date(), 1), DateUtil.LONG_DATE_FORMAT);

	}

	/**
	 * 
	 * 取得当前时间距离1900/1/1的天数
	 * 
	 * @return
	 * 
	 */

	public static int getDayNum() {

		int daynum = 0;

		GregorianCalendar gd = new GregorianCalendar();

		Date dt = gd.getTime();

		GregorianCalendar gd1 = new GregorianCalendar(1900, 1, 1);

		Date dt1 = gd1.getTime();

		daynum = (int) ((dt.getTime() - dt1.getTime()) / (24 * 60 * 60 * 1000));

		return daynum;

	}

	/** 针对yyyy-MM-dd HH:mm:ss格式,显示yyyymmdd */

	public static String getYmdDateCN(String datestr) {

		if (datestr == null)

			return "";

		if (datestr.length() < 10)

			return "";

		StringBuffer buf = new StringBuffer();

		buf.append(datestr.substring(0, 4)).append(datestr.substring(5, 7)).append(datestr.substring(8, 10));

		return buf.toString();

	}

	/**
	 * 
	 * 获取本月第一天
	 * 
	 * @param format
	 * 
	 * @return
	 * 
	 * 
	 */

	public static String getFirstDayOfMonth(String format) {

		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.DATE, 1);

		return date2String(cal.getTime(), format);

	}

	/**
	 * 
	 * 获取本月最后一天
	 * 
	 * @param format
	 * 
	 * @return
	 */

	public static String getLastDayOfMonth(String format) {

		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.DATE, 1);

		cal.add(Calendar.MONTH, 1);

		cal.add(Calendar.DATE, -1);

		return date2String(cal.getTime(), format);

	}

	public static int getAge(Date dateOfBirth) {
		int age = 0;
		Calendar born = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		if (dateOfBirth != null) {
			now.setTime(new Date());
			born.setTime(dateOfBirth);
			if (born.after(now)) {
				throw new IllegalArgumentException(" Can't be born in the future");
			}
			age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
			if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
				age -= 1;
			}
		}
		return age;
	}

	/**
	 * 获取今天的日期
	 * 
	 * @return
	 */
	public static String getToDayWeek() {
		// 获得当前日期
		Date date = new Date();
		// 格式化日期，EEEE为星期几格式化
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		TLog.e("星期", "今天是：" + dateFm.format(date));
		// TLog.e("星期", "今天是："+new SimpleDateFormat("yyyyMMddEEEE").format(date));
		return dateFm.format(date);
	}

	/**
	 * 获取上本周一的日期
	 * 
	 * @return
	 */
	public static String getToUpMonDay() {
		Calendar cal = Calendar.getInstance();
		// n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		int n = -1;
		String monday;
		cal.add(Calendar.DATE, n * 7);
		// 想周几，这里就传几Calendar.MONDAY（TUESDAY...）
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		monday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		return monday;
	}

	/**
	 * 获取本周一的日期
	 * 
	 * @return
	 */
	public static String getToMonDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
		String last = df.format(cal.getTime());
		return last;
	}

	/**
	 * 获取本周日的日期
	 * 
	 * @return
	 */
	public static String getToSunDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		// 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// 增加一个星期，才是我们中国人理解的本周日的日期
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		String last = df.format(cal.getTime());
		return last;
	}

	/**
	 * 获取指定日期的该月第一天
	 * 
	 * @param strdate
	 * @return
	 */
	public static String getToMonthStart(String strdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 小写的mm表示的是分钟
		Calendar calendar = null;
		try {
			Date date = sdf.parse(strdate);
			calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, 1);// 本月第一天

			// calendar.add(Calendar.MONTH, 1);
			// calendar.set(Calendar.DAY_OF_MONTH, 1);
			// calendar.add(Calendar.DAY_OF_MONTH, -1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(calendar.getTime());
	}

	/**
	 * 获取指定日期的该月最后一天
	 * 
	 * @param strdate
	 * @return
	 */
	public static String getToMonthEnd(String strdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 小写的mm表示的是分钟
		Calendar calendar = null;
		try {
			Date date = sdf.parse(strdate);
			calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(calendar.getTime());
	}
}
