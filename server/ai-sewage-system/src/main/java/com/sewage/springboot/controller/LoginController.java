package com.sewage.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

/**
 * @author: zzy
 * @description: 登录相关Controller
 */
@CrossOrigin
@RestController
@SpringBootApplication
@RequestMapping(value = "/login")
public class LoginController {

	@Autowired
	private LoginService loginService;

	/**
	 * 登录
	 * @return json:用户身份信息和地区权限信息
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public JSONObject authLogin(@RequestBody JSONObject jsonObject) {
		jsonObject = jsonObject.getJSONObject("data");
		return loginService.authLogin(jsonObject);
	}

	/**
	 * 查询当前登录用户的信息
	 */
	@RequestMapping(value = "/getInfo", method = RequestMethod.POST)
	public JSONObject getInfo() {
		//System.out.println(loginService.getInfo());
		return loginService.getInfo();
	}

	/**
	 * 登出
	 */
	@PostMapping("/logout")
	public JSONObject logout() {
		return loginService.logout();
	}
}
