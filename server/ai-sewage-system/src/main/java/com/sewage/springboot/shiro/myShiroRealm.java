package com.sewage.springboot.shiro;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.entity.UserSessionInfo;
import com.sewage.springboot.service.LoginService;
import com.sewage.springboot.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义shiro身份验证和权限验证方法
 */
public class myShiroRealm extends AuthorizingRealm {
	private Logger logger = LoggerFactory.getLogger(myShiroRealm.class);

	@Autowired
	private LoginService loginService;

	/**
	 * 当访问的页面需要鉴权的时候会调用这个方法
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Session session = SecurityUtils.getSubject().getSession();
		UserSessionInfo user = (UserSessionInfo) session.getAttribute(Constants.SESSION_USER_INFO);
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 添加身份验证
		authorizationInfo.addStringPermission(user.getIdentity());
		return authorizationInfo;
	}

	/**
	 * 验证当前登录的Subject
	 * LoginController.login()方法中执行Subject.login()时 执行此方法
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername();
		String password = String.valueOf(token.getPassword());
		JSONObject user = loginService.getUser(username);
		if (user == null) {
			throw new UnknownAccountException("账号/密码错误");
		}
		if (user.getInteger("delete_status") == 1) {
			throw new UnknownAccountException("用户账号已冻结");
		}
		// 这里需要对输入的密码进行手动的二次md5加密
		String salt = user.getString("salt");
		String newPassword = new Md5Hash(password, salt, 2).toString();
		token.setPassword(newPassword.toCharArray());
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getString("username"),
				user.getString("password"), getName());
		String test = ByteSource.Util.bytes(user.getString("salt")).toHex();
		authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getString("salt")));
		UserSessionInfo userSessionInfo = new UserSessionInfo();
		userSessionInfo.setUsername(user.getString("username"));
		userSessionInfo.setDelete_status(user.getInteger("delete_status"));
		userSessionInfo.setIdentity(user.getString("identity"));
		userSessionInfo.setArea(user.getString("area"));
		userSessionInfo.setPhone(user.getString("phone"));
		userSessionInfo.setMail(user.getString("mail"));
		setSession(Constants.SESSION_USER_INFO, userSessionInfo);
		return authenticationInfo;
	}

	/**
	 * 将一些数据放到ShiroSession中,以便于其它地方使用,将用户存放到session中
	 * 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
	 */
	private void setSession(Object key, Object value) {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser != null) {
			Session session = currentUser.getSession();
			// System.out.println("====session id:" + session.getId());
			// 2小时
			session.setTimeout(7200000);
			session.setAttribute(key, value);
		}
	}
}
