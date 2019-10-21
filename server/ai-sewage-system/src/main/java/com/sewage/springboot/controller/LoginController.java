package com.sewage.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.Global;
import com.sewage.springboot.service.LoginService;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

	Logger logger = LoggerFactory.getLogger(getClass());
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
	 * @desc 查询当前登录用户的信息
	 */
	@RequestMapping(value = "/getInfo", method = RequestMethod.POST)
	public JSONObject getInfo() {
		//System.out.println(loginService.getInfo());
		return loginService.getInfo();
	}

	/**
	 * @desc 刷新认证token
	 */
	@RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
	public JSONObject refreshToken (@RequestBody JSONObject jsonObject) throws IOException {
		JSONObject loginInfo = jsonObject.getJSONObject("loginInfo");
		JSONObject shiroTokenInfo = loginService.authLogin(loginInfo);
		JSONObject tokenInfo = new JSONObject();
		String refreshToken = loginInfo.getString("refreshToken");
		JSONObject fboxTokenInfo = refreshFboxToken(refreshToken);
		if (fboxTokenInfo.getString("flag").equals("true")) {
			tokenInfo.put("flag", "true");
			tokenInfo.put("shiroTokenInfo", shiroTokenInfo);
			tokenInfo.put("fboxTokenInfo", fboxTokenInfo);
		} else {
			tokenInfo.put("flag", "false");
		}
		return tokenInfo;
	}
	/**
	 * 登出
	 */
	@PostMapping("/logout")
	public JSONObject logout() {
		return loginService.logout();
	}

	private JSONObject refreshFboxToken (String refreshToken) throws IOException {
		boolean fboxFlag = false;
		JSONObject fboxTokenInfo = new JSONObject();
		String fboxInfo = "";
		OkHttpClient client = new OkHttpClient();
		FormBody formBody = new FormBody.Builder()
				.add("refresh_token", refreshToken)
				.add("client_id", Global.clientId)
				.add("client_secret", Global.clientSecret)
				.add("scope", Global.scope)
				.add("grant_type", Global.grant_type)
				.build();
		Request request = new Request.Builder()
				.url("https://account.flexem.com/core/connect/token")
				.post(formBody)
				.build();
		Response response = client.newCall(request).execute();
//		System.out.println(response.toString());
		logger.warn(response.toString());
		logger.warn("====body: " + response.body().toString());
		if (response.isSuccessful()) {
			fboxInfo = response.body().toString();
			response.body().close();
			fboxFlag = true;
		} else {
			response.body().close();
		}
		if (fboxFlag) {
			// 组装info
			fboxTokenInfo.put("flag", "true");
		} else {
			fboxTokenInfo.put("flag", "false");
		}
		return fboxTokenInfo;
	}
}
