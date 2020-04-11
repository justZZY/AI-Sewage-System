package com.sewage.springboot.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.sewage.springboot.entity.UserSessionInfo;
import com.sewage.springboot.handle.exception.response.BussinessException;
import com.sewage.springboot.util.constants.Constants;
import com.sewage.springboot.util.constants.ErrorEnum;

/**
 * 会话用户信息帮助类
 *
 * @author：sc
 * @data： 2019年10月2日
 */
public class UserInfoUtils {
	
	/**
	 * 获取当前会话用户信息,若无抛出BussinessException异常
	 *
	 * @author：sc
	 * @data： 2019年10月2日
	 */
	public static UserSessionInfo getUserInfo() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		UserSessionInfo userInfo = (UserSessionInfo)session.getAttribute(Constants.SESSION_USER_INFO);
		// 无登录状态必须抛出异常，以防业务出错
		if(userInfo==null) throw new BussinessException(ErrorEnum.E_20011);
		return userInfo;
	}
	
	
}
