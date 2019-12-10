package com.sewage.springboot.handle.exception.response;

import com.sewage.springboot.util.constants.ErrorEnum;

/**
 * 文件上传失败异常
 * <br><br>
 *
 * @author：sc
 * @data： 2019年10月2日
 */
public class FileUploadException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private Object errorNo = -1; //默认返回值-1
	private String errorMsg;
	private Object data;
	public FileUploadException(String errorMsg){
		super();
		this.errorMsg = errorMsg;
	}
	public FileUploadException(Object errorNo,String errorMsg){
		super();
		this.errorNo = errorNo;
		this.errorMsg = errorMsg;
	}
	public FileUploadException(Object errorNo,String errorMsg,Object data){
		super();
		this.errorNo = errorNo;
		this.errorMsg = errorMsg;
		this.data = data;
	}
	public FileUploadException(ErrorEnum errorEnum) {
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
