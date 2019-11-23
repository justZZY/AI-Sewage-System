package com.sewage.springboot.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.sewage.springboot.entity.UserSessionInfo;
import com.sewage.springboot.util.constants.Constants;

/**
 * 会话用户信息帮助类
 *
 * @author：sc
 * @data： 2019年10月2日
 */
public class UserInfoUtils {
	
	/**
	 * 获取当前会话用户信息
	 *
	 * @author：sc
	 * @data： 2019年10月2日
	 */
	public static UserSessionInfo getUserInfo() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		UserSessionInfo userInfo = (UserSessionInfo)session.getAttribute(Constants.SESSION_USER_INFO);
		return userInfo;
	}
	
	
}
