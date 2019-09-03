package com.sewage.springboot;

import java.net.Proxy;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Global {
    // 线程池
    public static ExecutorService threadPool = Executors.newCachedThreadPool();

    // 以下服务器地址是繁易公有云，私有云请根据实际情况修改
    public static final String idServerUrl = "https://account.flexem.com/core/";
    public static final String signalRUrl = "http://fbcs101.fbox360.com/push";
    public static String signalrClientId = UUID.randomUUID().toString();
    public static Proxy proxy = null;
//    // FBox客户端注册的账号
//    public static String username = "ligaofa";
//    public static String password = "gf229192";
//    // 获取API账号请咨询对接的销售。
//    public static String clientId = "ynsk";
//    public static String clientSecret = "af6a37beddb28136eed65bda1f16547f";
    public static String username = "kmzmhj";
    public static String password = "zmhj123456";
    // 获取API账号请咨询对接的销售。
    public static String clientId = "kmbq";
    public static String clientSecret = "a89f97dc2ed2457aa0c6e58eb40142b2";
    // 登录参数
    public static String scope = "openid offline_access fbox email profile";
    public static String grant_type = "password";
}
