package com.sewage.springboot;

import com.sewage.springboot.signalr.ServerCaller;

import java.net.Proxy;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Global {
    // 线程池
    public static ExecutorService threadPool = Executors.newCachedThreadPool();
    public static ServerCaller commServer;
    public static ServerCaller appServer;
    public static ServerCaller hdataServer;

    // 以下服务器地址是繁易公有云，私有云请根据实际情况修改
    public static final String idServerUrl = "https://account.flexem.com/core/";
    public static String appServerApiUrl = "http://fbox360.com/api/client/";
    public static final String commServerApiUrl = "http://fbcs101.fbox360.com/api/";
    public static final String commServerSignalRUrl = "http://fbcs101.fbox360.com/push";
    public static String hdataServerApiUrl = "http://fbhs1.fbox360.com/api/";
    public static final String signalRUrl = "http://fbcs101.fbox360.com/push";
    public static String signalrClientId = UUID.randomUUID().toString();
    public static Proxy proxy = null;

    // FlexManager
    public static String username = "ynzmhj";
    public static String password = "zmhj123456";
    // 获取API账号请咨询对接的销售。
    public static String clientId = "kmbq";
    public static String clientSecret = "a89f97dc2ed2457aa0c6e58eb40142b2";
    // 登录参数
    public static String scope = "openid offline_access fbox email profile";
    public static String grant_type_login = "password";
    public static String grant_type_refresh = "refresh_token";

    // 服务器启动时间 日志记录时间
    public static final Date createDate = new Date(2020, 6, 8, 0, 0, 0);
}
