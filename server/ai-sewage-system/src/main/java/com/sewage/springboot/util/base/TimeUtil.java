package com.sewage.springboot.util.base;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	/**
	 * 返回时间字符串(2019-07-18 14:10)
	 * @author shuchao
	 * @data   2019年7月18日
	 * @param date
	 * @return
	 */
	public static String getDateTimeStr(Date date) {
		try {
			String formater = "yyyy-MM-dd HH:mm";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			return format.format(date);
		} catch (Exception e) {
			return "";
		}
	}
	
	 /**
	 * 返回格式化的日期yyyy-MM-dd
	 * @author shuchao
	 * @data   2019年7月18日
	 * @return
	 */
	public static String getFullDate(Date myDate) {
		String formater = "yyyy-MM-dd";
		SimpleDateFormat format = new SimpleDateFormat(formater);
		return format.format(myDate);
	}
	
	/**
	 * 返回格式化的日期yyyy-MM-dd
	 * @author shuchao
	 * @data   2019年7月18日
	 * @return
	 */
	public static String getFullDate() {
		String formater = "yyyy-MM-dd";
		SimpleDateFormat format = new SimpleDateFormat(formater);
		Date myDate = new Date();
		return format.format(myDate);
	}
	
	public static String getStrDate(Date date, String formater) {
		SimpleDateFormat format = new SimpleDateFormat(formater);
		return format.format(date);
	}
	
	// 返回格式化的日期
	/**
	 * yyyy-MM-dd
	 */
	public static String getFullDate(String sDate) {
		try {
			String formater = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			Date date = format.parse(sDate);
			formater = "yyyy-MM-dd";
			format = new SimpleDateFormat(formater);
			return format.format(date);
		} catch (Exception ex) {
			return "";
		}
	}
	
	// 返回格式化的日期
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurDateTime() {
		String formater = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(formater);
		Date myDate = new Date();
		return format.format(myDate);
	}
	
	// 返回格式化的日期
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static String getFullDateTime(String sDate) {
		try {
			String formater = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			Date date = format.parse(sDate);
			return format.format(date);
		} catch (Exception ex) {
			return "";
		}
	}
	
	/**
	 * yyyy-MM-dd HH:mm
	 * 
	 * @param sDate
	 * @return
	 */
	public static String getSecondDateTime(String sDate) {
		try {
			String formater = "yyyy-MM-dd HH:mm";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			Date date = format.parse(sDate);
			return format.format(date);
		} catch (Exception ex) {
			return "";
		}
	}
	
	public static String getFullDateWeekTime(String sDate) {
		try {
			String formater = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			Date date = format.parse(sDate);
			format.applyPattern("yyyy-MM-dd E HH:mm:ss");
			return format.format(date);
		} catch (Exception ex) {
			System.out.println("TimeUtil  getFullDateWeekTime" + ex.getMessage());
			return "";
		}
	}
	
	// 返回格式化的日期
	public static String getSimpleDate() {
		String formater = "yyyy-M-d";
		SimpleDateFormat format = new SimpleDateFormat(formater);
		Date myDate = new Date();
		return format.format(myDate);
	}
	
	// 返回保留两位小数的浮点数
	public static String getFloat(double price) {
		
		return (new DecimalFormat("0.00").format(price));
	}
	
	// 返回格式化的日期
	
	public static String getSomeDate(String sDate, int iDay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = format.parse(sDate);
			long Time = (date.getTime() / 1000) + 60 * 60 * 24 * iDay;
			date.setTime(Time * 1000);
			return format.format(date);
		} catch (Exception ex) {
			return "";
		}
	}
	
	// 返回日期的差值
	
	public static int getNumericDatePeriod(String sDate) {
		int iTime = 0;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(sDate);
			Date date1 = new Date();
			iTime = (int) ((date.getTime() - date1.getTime()) / 1000L);
		} catch (Exception ex) {
		}
		return iTime;
	}
	
	// 判断日期的前后
	public static boolean isDateLater(String sDate, String sDate1) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = format.parse(sDate);
			Date date1 = format.parse(sDate1);
			if (date.after(date1)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @author shuchao
	 * @data   2019年7月18日
	 * @param date
	 * @return
	 */
	public static String getFullDateTime(Date date) {
		try {
			String formater = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			return format.format(date);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获取从现在开始 n 天后的日期
	 * @author shuchao
	 * @data   2019年3月9日
	 * @param days 天
	 * @return
	 */
	public static long getDateAfterDays(int days) {
		Calendar c = Calendar.getInstance();
		c.add(c.DATE, days);
		return c.getTimeInMillis();
	}
	
	/**
	 * 判断现在是否在时间范围内（如：07:00 - 23:15）
	 * <br><br>
	 * @param startTime 格式 HH:mm
	 * @param endTime 格式 HH:mm， 如果小于startTime，则认为是第二天该时刻
	 * @return 抛出异常时返回false
	 */
	public static boolean isTimeRange(String startTime, String endTime) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		try {
			Date now = df.parse(df.format(new Date()));
			Date begin = df.parse(startTime);
			Date end = df.parse(endTime);
			Calendar nowTimeC = Calendar.getInstance();
			nowTimeC.setTime(now);
			Calendar beginTimeC = Calendar.getInstance();
			beginTimeC.setTime(begin);
			Calendar endTimeC = Calendar.getInstance();
			endTimeC.setTime(end);
			// 如果结束时刻在开始时刻之前，则结束时刻认为是第二天的该时刻
			if(endTimeC.before(beginTimeC)) {
				if(nowTimeC.before(endTimeC)) {
					return true;
				}
				endTimeC.add(Calendar.DATE, 1);
			}
			if (nowTimeC.before(endTimeC) && nowTimeC.after(beginTimeC)) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 判断现在是否在指定时间之前
	 * <br><br>
	 * @param time 格式 HH:mm
	 * @return 抛出异常时返回false
	 */
	public static boolean beforeTime(String time) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		try {
			Date now = df.parse(df.format(new Date()));
			Date begin = df.parse(time);
			Calendar nowTimeC = Calendar.getInstance();
			nowTimeC.setTime(now);
			Calendar beginTimeC = Calendar.getInstance();
			beginTimeC.setTime(begin);
			// 如果结束时刻在开始时刻之前，则结束时刻认为是第二天的该时刻
			if(nowTimeC.before(beginTimeC)) {
				return true;
			}else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断现在是否在指定时间之后
	 * <br><br>
	 * @param time 格式 HH:mm
	 * @return 抛出异常时返回false
	 */
	public static boolean afterTime(String time) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		try {
			Date now = df.parse(df.format(new Date()));
			Date theTime = df.parse(time);
			Calendar nowTimeC = Calendar.getInstance();
			nowTimeC.setTime(now);
			Calendar theTimeC = Calendar.getInstance();
			theTimeC.setTime(theTime);
			// 如果结束时刻在开始时刻之前，则结束时刻认为是第二天的该时刻
			if(nowTimeC.after(theTimeC)) {
				return true;
			}else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		String s = getStrDate(new Date(), "yyyy年MM月dd日 HH:mm");
		System.out.println(isTimeRange("14:35", "11:36"));
	}
}