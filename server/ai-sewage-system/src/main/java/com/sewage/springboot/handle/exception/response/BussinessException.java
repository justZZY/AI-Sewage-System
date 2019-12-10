package com.sewage.springboot.handle.exception.response;

import com.sewage.springboot.util.constants.ErrorEnum;

/**
 * 业务异常
 * <br><br>
 * 在对参数验证失败，或者操作失败时，直接从业务层结束业务。需配合异常拦截器使用
 * @author：sc
 * @data： 2019年10月2日
 */
public class BussinessException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private Object errorNo = -1; //默认返回值-1
	private String errorMsg;
	private Object data;
	public BussinessException(String errorMsg){
		super();
		this.errorMsg = errorMsg;
	}
	public BussinessException(Object errorNo,String errorMsg){
		super();
		this.errorNo = errorNo;
		this.errorMsg = errorMsg;
	}
	public BussinessException(Object errorNo,String errorMsg,Object data){
		super();
		this.errorNo = errorNo;
		this.errorMsg = errorMsg;
		this.data = data;
	}
	public BussinessException(ErrorEnum errorEnum) {
		super();
		this.errorNo = errorEnum.getErrorCode();
		this.errorMsg = errorEnum.getErrorMsg();
	}
	public Object getErrorNo() {
		return errorNo;
	}
	public void setErrorNo(int errorNo) {
		this.errorNo = errorNo;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	

}
