package com.sewage.springboot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.sewage.springboot.Global;
import com.sewage.springboot.entity.EquipJson;
import com.sewage.springboot.entity.EquipValueJson;
import com.sewage.springboot.logger.ConsoleLoggerFactory;
import com.sewage.springboot.signalr.FBoxSignalRConnection;
import okhttp3.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

//允许外部访问
@CrossOrigin
//@RestController注解能够使项目支持Rest
@RestController
@SpringBootApplication
//表示该controller类下所有的方法都公用的一级上下文根
@RequestMapping(value = "/test")
public class UserController {
    //这里使用@RequestMapping注解表示该方法对应的二级上下文路径
    @RequestMapping(value = "/testLogin", method = RequestMethod.POST)
    //测试登录信息
    String testLogin() throws IOException {
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("username", Global.username)
                .add("password", Global.password)
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
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    @RequestMapping(value = "/testEquipments", method = RequestMethod.GET)
    //如果需要参数 @RequestParam(value = "test") String test
    String testEquipments(@RequestParam(value = "Authorization") String authorization) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://fbox360.com/api/client/box/grouped")
                .addHeader("Authorization", authorization)
                .addHeader("X-FBox-ClientId", Global.signalrClientId)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            return response.body().string();
        }else{
            throw new IOException("Unexpected code " + response);
        }
    }

    // 在入参设置@RequestBody注解表示接收整个报文体，这里主要用在接收整个POST请求中的json报文体，
    // 目前主流的请求报文也都是JSON格式了，使用该注解就能够获取整个JSON报文体作为入参，使用JSON解析工具解析后获取具体参数
    @RequestMapping(value = "/getUserByJson",method = RequestMethod.POST)
    String getUserByJson(@RequestBody String data){
        return "Json is " + data;
    }

    // 获取监控设备信息
    @RequestMapping(value = "/getEquipMonitor", method = RequestMethod.POST)
    String getEquipMonitor (@RequestBody JSONObject jsonObject) throws IOException {
        // 通过fastJson获取相关的post参数
        String authorization = jsonObject.getString("authorization");
        String apiBaseUrl = jsonObject.getString("apiBaseUrl");
        String boxNo = jsonObject.getString("boxNo");
        String url = apiBaseUrl + "v2/box/dmon/grouped?boxNo=" + boxNo;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url)
                .addHeader("Authorization", authorization)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    /*
     * 获取设备值
     * args: jsonObject组装的json数据 通过fastJson解析
     * jsonObject: {authorization, apiBaseUrl, boxNo, names}
     */
    @RequestMapping(value = "/getEquipValue", method = RequestMethod.POST)
    String getEquipValue (@RequestBody JSONObject jsonObject) throws IOException {
        // 解析传递的json
        String authorization = jsonObject.getString("authorization");
        String apiBaseUrl = jsonObject.getString("apiBaseUrl");
        String boxNo = jsonObject.getString("boxNo");
        String url = apiBaseUrl + "v2/dmon/value/get?boxNo=" + boxNo;
        JSONArray names = jsonObject.getJSONArray("names");
        OkHttpClient client = new OkHttpClient();
        // 配置gson解析对象
        EquipJson equipJson = new EquipJson();
        String[] str = new String[names.size()];
        for (int i = 0; i < names.size(); i++) {
            str[i] = names.getString(i);
        }
        equipJson.setNames(str);
        equipJson.setTimeOut(5000);
        equipJson.setUseCache(false);
        // 解析对象为json
        Gson gson = new Gson();
        String json = gson.toJson(equipJson);
        // 请求
        okhttp3.RequestBody requestBody = FormBody.create(MEDIA_TYPE, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", authorization)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    /*
     * 设置设备寄存器的值
     */
    @RequestMapping(value = "setEquipValue", method = RequestMethod.POST)
    String setEquipValue (@RequestBody JSONObject jsonObject) throws IOException {
        String authorization = jsonObject.getString("authorization");
        String apiUrl = jsonObject.getString("apiUrl");
        String boxNo = jsonObject.getString("boxNo");
        String name = jsonObject.getString("name");
        int type = jsonObject.getIntValue("type");
        String value = jsonObject.getString("value");
        String url = apiUrl + "v2/dmon/value?boxNo=" + boxNo;
        // 生成json类
        EquipValueJson equipValueJson = new EquipValueJson();
        equipValueJson.setName(name);
        equipValueJson.setType(type);
        equipValueJson.setValue(value);
        Gson gson = new Gson();
        String json = gson.toJson(equipValueJson);
        // 建立json连接
        okhttp3.RequestBody requestBody = FormBody.create(MEDIA_TYPE, json);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", authorization)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.headers());
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    /*
     * 建立signalr实时监控
     */
    @RequestMapping(value = "/createSignalRConnect", method = RequestMethod.POST)
    String createSignalRConnect (@RequestBody JSONObject jsonObject) {
        String signalrUrl = jsonObject.getString("url");
        String token = jsonObject.getString("token");
        System.out.println("====打开signalr");
        // 启动signalr
        ConsoleLoggerFactory loggerFactory = new ConsoleLoggerFactory();
        signalRConnection = new FBoxSignalRConnection(signalrUrl, token,
                Global.signalrClientId, Global.proxy, loggerFactory);
        signalRConnection.start();
        return "success";
    }
    /*
     * 关闭signalr监控
     */
    @RequestMapping(value = "/closeSignalRConnect", method = RequestMethod.POST)
    String closeSignalRConnect () {
        signalRConnection.stop();
        System.out.println("closeSignalR");
        return "success";
    }
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private FBoxSignalRConnection signalRConnection = null;
}
