package com.sewage.springboot.util.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.net.www.protocol.https.Handler;
/**
 * URL工具类
 * <br><br>
 * 用作获取互联网资源
 * @Date 2018年3月23日
 * @Author 舒超
 */
public class URLHelper {
	
	private static Logger logger = LoggerFactory.getLogger(URLHelper.class);
	
	public static void main(String[] args) {
		String html = getMethod("http://www.duxiu.com/views/specific/mobilejson/searchgroupadmininfo.jsp?areaid=274&unitid=7522&mobiletag=2&json=mjson&intertag=4", "UTF-8");
		System.out.println(html);
	}
	
	/**
	 *  ajax提交get请求
	 * 
	 * @param  urlString 请求地址
	 * @param  htmlCharset 请求地址
	 * @return String
	 */
	public static String getMethod(String urlString, String htmlCharset){
		return getMethod(urlString, null, htmlCharset);
	}
	
	/**
	 * get方法,使用统一资源定位符获取资源
	 * 
	 * @param  urlString 请求地址
	 * @param  htmlCharset 请求地址
	 * @return String
	 */
	public static String getMethodAjax(String urlString, String htmlCharset){
		return getMethod(urlString, new HashMap<String, String>(){{put("X-Requested-With", "XMLHttpRequest");}}, htmlCharset);
	}
	
	/**
	 * post方法,使用统一资源定位符获取资源
	 * 
	 * @param  urlString 请求地址
	 * @param  htmlCharset 请求地址
	 * @return String
	 */
	public static String postMethod(String urlString, String htmlCharset){
		return postMethod(urlString, null, htmlCharset);
	}
	
	/**
	 *  ajax提交post请求
	 * 
	 * @param  urlString 请求地址
	 * @param  htmlCharset 请求地址
	 * @return String
	 */
	public static String postMethodAjax(String urlString, String htmlCharset){
		return postMethod(urlString, new HashMap<String, String>(){{put("X-Requested-With", "XMLHttpRequest");}}, htmlCharset);
	}
	
	/**
	 * get方法,使用统一资源定位符获取资源
	 * <br><br>
	 * 
	 * @param  urlString 请求地址
	 * @param  headerMap 请求头
	 * @param  htmlCharset 请求地址
	 * @return String
	 */
	public static String getMethod(String urlString, Map<String, String> headerMap, String htmlCharset){
		InputStream in = null;
		HttpURLConnection httpConn = null;
		try {
			httpConn = urlString.startsWith("https")?
					wrapURLConnection(new URL(null,urlString,new Handler()).openConnection())
					:(HttpURLConnection)new URL(urlString).openConnection();
			//设置参数
			httpConn.setUseCaches(false);  		//不允许缓存
			httpConn.setRequestMethod("GET");   //设置GET方式连接
			//设置请求属性
			if(headerMap == null) 					headerMap = new HashMap<String, String>();
			if(headerMap.get("User-Agent") == null) headerMap.put("User-Agent", "Mozilla/5.0");
			if(headerMap.get("Accept") == null) 	headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			Iterator<Entry<String, String>> iterable = headerMap.entrySet().iterator();
			while(iterable.hasNext()){
				Entry<String,String> entry = iterable.next();
				httpConn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			//连接
			httpConn.connect();
			//获得响应状态
		    return toString(httpConn, htmlCharset);
		} catch (Exception e) {
			logger.error("getMethod异常",e);
		}finally{
			try {
				if (in != null) {
					in.close();
				}
				httpConn.disconnect();
			} catch (Exception e2) {
				logger.error("关闭流与连接异常",e2);
			}
			
		}
		return null;
		
	}
	/**
	 * post方法,使用统一资源定位符获取资源
	 * <br><br>
	 * 
	 * @param  urlString 请求地址
	 * @param  headerMap 请求头
	 * @param  htmlCharset 请求地址
	 * @return String
	 */
	public static String postMethod(String urlString, Map<String, String> headerMap, String htmlCharset){
		InputStream in = null;
		HttpURLConnection httpConn = null;
		try {
			String query = urlString.contains("?") ?
				urlString.substring(urlString.indexOf("?")+1) : "";
			urlString = urlString.contains("?") ?
				urlString.substring(0,urlString.indexOf("?")) : urlString;
			httpConn = urlString.startsWith("https")?
					wrapURLConnection(new URL(null,urlString,new Handler()).openConnection())
					:(HttpURLConnection)new URL(urlString).openConnection();
			 //设置参数
			httpConn.setDoOutput(true);  //需要输出
			httpConn.setDoInput(true);   //需要输入
			httpConn.setUseCaches(false);//不允许缓存
			httpConn.setRequestMethod("POST");   //设置POST方式连接
			//设置请求属性
			if(headerMap == null) 					headerMap = new HashMap<String, String>();
			if(headerMap.get("User-Agent") == null) headerMap.put("User-Agent", "Mozilla/5.0");
			if(headerMap.get("Accept") == null) 	headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			Iterator<Entry<String, String>> iterable = headerMap.entrySet().iterator();
			while(iterable.hasNext()){
				Entry<String,String> entry = iterable.next();
				httpConn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			//连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
			httpConn.connect();
			//建立输入流，向指向的URL传入参数
			DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
			dos.writeBytes(query);
			dos.flush();
			dos.close();
			//获得响应状态
		    return toString(httpConn, htmlCharset); 
		} catch (Exception e) {
			logger.error("postMethod异常",e);
		}finally{
			try {
				if (in != null) {
					in.close();
				}
				httpConn.disconnect();
			} catch (Exception e2) {
				logger.error("关闭流与连接异常",e2);
			}
		}
		return null;
	}
		
	/**
	 * 流转字节
	 * <br><br>
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 * @date 2018年3月22日
	 * @author 舒超
	 */
	public static byte[] toByteArray(InputStream in) throws IOException{
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int i = 0;
		while((i=in.read(buffer)) !=-1){
			byteOutputStream.write(buffer, 0, i);
		}
		byteOutputStream.flush();
		byte[] b = byteOutputStream.toByteArray();
		byteOutputStream.close();
		return b;
	}
	
	/**
	 * 读取响应文本
	 * <br><br>
	 * 
	 * @param httpConn
	 * @return
	 * @date 2018年3月22日
	 * @author 舒超
	 */
	public static String toString(HttpURLConnection httpConn, String charset){
		String html = null;
		try {
			InputStream in = httpConn.getInputStream();
			if(in!=null){
				byte[] b = toByteArray(in);
				if(charset==null || charset.trim().equals("")){
					html = new String(b);
				}else{
					html = new String(b, charset);
				}
			}
		} catch (Exception e) {
			logger.error("toString方法异常",e);
		}
		return html ;
	}
	/**
     * URLConnection --> HttpsURLConnection 信任证书、信任主机
     * <br><br>
     * 
     * @param uRLConnection
     * @return
     * @date 2018年4月24日
     */
    public static HttpsURLConnection wrapURLConnection(URLConnection uRLConnection) {
    	HttpsURLConnection httpsURLConnection = (HttpsURLConnection) uRLConnection;
    	try {
    		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
    		X509TrustManager trustManager = new X509TrustManager() {
    			@Override
    			public void checkClientTrusted(X509Certificate[] x509Certificates, String s)throws CertificateException {
    			}
    			@Override
    			public void checkServerTrusted(X509Certificate[] x509Certificates, String s)throws CertificateException {
    			}
    			@Override
    			public X509Certificate[] getAcceptedIssuers() {
    				return null;
    			}
    		};
    		TrustManager[] tm = new TrustManager[] { trustManager };
    		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
    		sslContext.init(null, tm, new java.security.SecureRandom());
    		// 创建证书信任
    		HostnameVerifier hVerifier = new HostnameVerifier() {
    			@Override
    			public boolean verify(String arg0, SSLSession arg1) {
    				return true;
    			}
    		};
    		
    		httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
    		httpsURLConnection.setHostnameVerifier(hVerifier);
		} catch (Exception e) {
			logger.error("wrapURLConnection", e);
		}
        return httpsURLConnection;
	}

}

