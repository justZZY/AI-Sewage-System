package com.sewage.springboot.util.base;

public class Validator {

	
	/**
	 * 只要有一个对象为空就返回true，全不为空返回false
	 */
	public static boolean isNull(Object... objs) {
		for(Object o: objs) {
			if(o==null)
				return true;
		}
		return false;
	}
}
