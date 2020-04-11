package com.sewage.springboot.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.handle.exception.response.BussinessException;
import com.sewage.springboot.handle.exception.response.FileUploadException;
import com.sewage.springboot.util.CommonUtil;



/**
 * 全局异常处理类
 * @author shuchao
 * @date   2019年10月2日
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/** 默认系统异常状态码 */
	public static final int DEFAULT = -1000;
	
	/* 异常拦截请按顺序在最后面加 */
	
	
	/** 捕获业务异常 */
	@ExceptionHandler(value=BussinessException.class) 
	private JSONObject exceptionHandler(BussinessException e) {
		/* 主动抛出异常，业务上出现错误时直接结束操作抛出此异常，用于便捷回滚事务 */
		return CommonUtil.jsonResult(e.getErrorNo(), e.getErrorMsg(), e.getData());
	}
	
	/** 文件上传异常 */
	@ExceptionHandler(value=FileUploadException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	private JSONObject exceptionHandler(FileUploadException e) {
		/* 文件上上传出错抛出异常，配合elementui的upload组件使用 */
		return CommonUtil.jsonResult(e.getErrorNo(), e.getErrorMsg(), e.getData());
	}
}
