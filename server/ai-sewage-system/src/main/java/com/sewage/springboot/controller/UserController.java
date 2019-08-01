package com.sewage.springboot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.sewage.springboot.entity.EquipJson;
import okhttp3.*;
import okio.BufferedSink;
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
//                .add("username", "kmzmhj")
//                .add("password", "zmhj123456")
//                .add("client_id", "kmbq")
//                .add("client_secret", "a89f97dc2ed2457aa0c6e58eb40142b2")
                .add("username", "ligaofa")
                .add("password", "gf229192")
                .add("client_id", "ynsk")
                .add("client_secret", "af6a37beddb28136eed65bda1f16547f")
                .add("scope", "openid offline_access fbox email profile")
                .add("grant_type", "password")
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
    String testEquipments(@RequestParam(value = "Authorization") String authorization,
                          @RequestParam(value = "XFBoxClientId") String xfboxclientid) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://fbox360.com/api/client/box/grouped")
                .addHeader("Authorization", authorization)
                .addHeader("X-FBox-ClientId", xfboxclientid)
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
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
}
