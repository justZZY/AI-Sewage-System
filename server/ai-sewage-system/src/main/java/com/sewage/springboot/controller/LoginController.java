package com.sewage.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author: hxy
 * @description: 登录相关Controller
 * @date: 2017/10/24 10:33
 */
@CrossOrigin
@RestController
@SpringBootApplication
@RequestMapping(value = "/login")
public class LoginController {

	@Autowired
	private LoginService loginService;

	/**s
	 * 登录
	 * @return
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	String authLogin(@RequestParam Map<String, Object> params) throws IOException {
		return loginService.authLogin(params);
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
