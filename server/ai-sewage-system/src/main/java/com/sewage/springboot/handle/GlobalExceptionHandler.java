package com.sewage.springboot.handle;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.handle.exception.response.BussinessException;
import com.sewage.springboot.util.CommonUtil;



/**
 * 全局异常处理类
 * @author shuchao
 * @date   2019年10月2日
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/**
	 * 默认系统状态码
	 */
	public static final int DEFAULT = -1001;
	
	/**
	 * 捕获所有抛出的Exception，进行下面分类处理
	 * @param request
	 * @param Exception
	 * @return
	 */
	@ExceptionHandler(value=Exception.class) 
	@ResponseBody
	private JSONObject exceptionHandler(HttpServletRequest request, Exception e) {
		/* 主动抛出异常，业务上出现错误时直接结束操作抛出此异常 */
		if(e instanceof BussinessException) {
			BussinessException e1 = (BussinessException)e;
			return CommonUtil.jsonResult(e1.getErrorNo(), e1.getErrorMsg(), e1.getData());
		}
		else {
			return CommonUtil.jsonResult(DEFAULT, e.getMessage(), "服务器异常，请稍候再试");
		}
		
	}
	
}
