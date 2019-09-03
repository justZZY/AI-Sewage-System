package com.sewage.springboot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.dao.LoginDao;
import com.sewage.springboot.service.LoginService;
import com.sewage.springboot.service.PermissionService;
import com.sewage.springboot.util.CommonUtil;
import com.sewage.springboot.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: hxy
 * @description: 登录service实现类
 * @date: 2017/10/24 11:53
 */
@Service
public class LoginServiceImpl implements LoginService {
static Object user_info;
	@Autowired
	private LoginDao loginDao;
	@Autowired
	private PermissionService permissionService;

	/**
	 * 登录表单提交
	 */
	@Override
	public String authLogin(Map<String, Object> params) {
		String username = params.get("username").toString();
		String password = params.get("password").toString();
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		try {
			currentUser.login(token);
			user_info = currentUser.getSession().getAttribute(Constants.SESSION_USER_INFO);
			return "success";
		} catch (AuthenticationException e) {
			return "fail";
		}
	}

	/**
	 * 根据用户名和密码查询对应的用户
	 */
	@Override
	public JSONObject getUser(String username, String password) {
		return loginDao.getUser(username, password);
	}

	/**
	 * 查询当前登录用户的权限等信息
	 */
	@Override
	public JSONObject getInfo() {
		//从session获取用户信息
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		// System.out.println(currentUser.getSession().getAttribute(Constants.SESSION_USER_INFO));
		JSONObject userInfo = (JSONObject)user_info;
		String username = userInfo.getString("username");
		JSONObject info = new JSONObject();
		JSONObject userPermission = permissionService.getUserPermission(username);
		session.setAttribute(Constants.SESSION_USER_PERMISSION, userPermission);
		session.setAttribute(Constants.SESSION_USER_INFO, userInfo);
		//System.out.println(session.getAttribute(Constants.SESSION_USER_INFO));
		info.put("userInfo", userInfo);
		info.put("userPermission", userPermission);
		return CommonUtil.successJson(info) ;
	}

	/**
	 * 退出登录
	 */
	@Override
	public JSONObject logout() {
		try {
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.logout();
		} catch (Exception e) {
		}
		return CommonUtil.successJson();
	}
}
