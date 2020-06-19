package com.sewage.springboot.util.message;

import com.sewage.springboot.util.StringTools;
import com.sewage.springboot.util.http.URLHelper;

public class SmsSender {

	/** UTF-8编码发送接口地址： **/
	public static final String SMS_SEND_API = "http://utf8.api.smschinese.cn/?Uid=%s&Key=%s&smsMob=%s&smsText=%s";
	
	/** UTF-8编码获取短信数量接口地址： **/
	public static final String SMS_SURPLUS_COUNT_API = "http://www.smschinese.cn/web_api/SMS/?Action=SMS_Num&Uid=%s&Key=%s";
	
	/** 短信服务商注册用户名  **/
	private static final String UID = "axm1314";
	
	/** 接口安全密钥（已使用MD5加密）  **/
	private static final String KEY = "sc950925alm970617";
	
	/** 短信发送一次最多接收人数量 **/
	public static final int MAX_RECEIVER_COUNT = 10;
	
	/** 验证码短信模板 **/
	public static final String SMM_VERIFY_CODE_TEMPLET = "您好，您本次请求的验证码为：%s，10分钟内有效。";
	
	
	/**
	 * 发送短信验证码
	 * <br><br>
	 * 
	 * @param mobilephones 手机号码（多个号码用逗号隔开，一次最多对10个手机发送）
	 * @param vcode 短信验证码
	 * @return 发送成功条数
	 * @date 2018年5月24日
	 * @author 舒超
	 */
	public static int sendSmsVerifyCode(String mobilephones, String vcode){
		String text = String.format(SMM_VERIFY_CODE_TEMPLET, vcode);
		return sendSmsMsg(UID, KEY, mobilephones, text);
	}
	
	/**
	 * 发送短信
	 * <br><br>
	 * 
	 * @param mobilephones 手机号码（多个号码用逗号隔开，一次最多对10个手机发送）
	 * @param text 短信内容（最多支持400个字，普通短信70个字/条，长短信64个字/条计费）
	 * @return 发送成功条数
	 * @date 2018年5月24日
	 * @author 舒超
	 */
	public static int sendSmsMsg(String mobilephones, String text){
		return sendSmsMsg(UID, KEY, mobilephones, text);
	}
	
	/**
	 * 发送短信
	 * <br><br>
	 * 
	 * @param uid 本站用户名
	 * @param key 接口安全秘钥
	 * @param mobilephones 手机号码（多个号码用逗号隔开，一次最多对10个手机发送）
	 * @param text 短信内容（最多支持400个字，普通短信70个字/条，长短信64个字/条计费）
	 * @return 发送成功条数
	 * @date 2018年5月24日
	 * @author 舒超
	 */
	public static int sendSmsMsg(String uid, String key, String mobilephones, String text){
		if(mobilephones!=null && mobilephones.split(",").length > MAX_RECEIVER_COUNT){
			throw new IllegalArgumentException("一次最多允许发送"+MAX_RECEIVER_COUNT+"个手机号！");
		}
		String url = String.format(SMS_SEND_API, uid, key, mobilephones, text);
		String html = URLHelper.getMethod(url, null, "utf-8");
		int i = StringTools.parseInt(html, 0);
		return i;
	}
	
	
	/**
	 * 查询本机配置的短息账户短信余量
	 * <br><br>
	 * 
	 * @return
	 * @date 2018年5月24日
	 * @author 舒超
	 */
	public static int getSmsSurplusCount(){
		return getSmsSurplusCount(UID, KEY);
	}
	/**
	 * 查询短信余量
	 * <br><br>
	 * 
	 * @param uid 本站用户名
	 * @param key 接口安全秘钥
	 * @return
	 * @date 2018年5月24日
	 * @author 舒超
	 */
	public static int getSmsSurplusCount(String uid, String key){
		String url = String.format(SMS_SURPLUS_COUNT_API, uid, key);
		String html = URLHelper.getMethod(url, null, "utf-8");
		int i = StringTools.parseInt(html, 0);
		return i;
	}
	
	public static void main(String[] args) {
		System.out.println(sendSmsMsg("13136179259", "您好,云南省玉溪市洪江市安宁真污水水村站点设备发生异常断电。"));
		System.out.println(getSmsSurplusCount());
	}
}
