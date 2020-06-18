package com.sewage.springboot.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author: hxy
 * @date: 2017/10/24 10:16
 */
public class StringTools {

	public static boolean isNullOrEmpty(String str) {
		return null == str || "".equals(str) || "null".equals(str);
	}

	public static boolean isNullOrEmpty(Object obj) {
		return null == obj || "".equals(obj);
	}
	
	public static boolean isEmpty(String str) {
		return null == str || "".equals(str.trim());
	}
	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}
	public static int parseInt(Object o, int defaultV) {
		if (o == null)
			return defaultV;
		try {
			return Integer.parseInt(o.toString().trim());
		} catch (NumberFormatException e) {
			return defaultV;
		}
	}
	
	
}
