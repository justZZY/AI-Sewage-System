package com.sewage.springboot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.sewage.springboot.Global;
import com.sewage.springboot.entity.EquipJson;
import com.sewage.springboot.entity.EquipValueJson;
import com.sewage.springboot.graph.TDBPersistence;
import com.sewage.springboot.logger.ConsoleLoggerFactory;
import com.sewage.springboot.service.GraphService;
import com.sewage.springboot.service.impl.GraphServiceImpl;
import com.sewage.springboot.signalr.FBoxSignalRConnection;
import com.sewage.springboot.util.CommonUtil;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.*;
import java.util.List;

//允许外部访问
@CrossOrigin
//@RestController注解能够使项目支持Rest
@RestController
@SpringBootApplication
//表示该controller类下所有的方法都公用的一级上下文根
@RequestMapping(value = "/equip")
public class EquipController {
    @Autowired
    private GraphService graphService;
    /**
     * @desc 获取fbox连接的token信息
     */
    //这里使用@RequestMapping注解表示该方法对应的二级上下文路径
    @RequestMapping(value = "/equipLogin", method = RequestMethod.POST)
    String equipLogin() throws IOException {
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("username", Global.username)
                .add("password", Global.password)
                .add("client_id", Global.clientId)
                .add("client_secret", Global.clientSecret)
                .add("scope", Global.scope)
                .add("grant_type", Global.grant_type_login)
                .build();
        Request request = new Request.Builder()
                .url("https://account.flexem.com/core/connect/token")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String res = response.body().string();
            response.body().close();
            return res;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * @desc 获取设备组数据
     */
    @RequestMapping(value = "/getEquipments", method = RequestMethod.GET)
    String getEquipments(@RequestParam(value = "Authorization") String authorization) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://fbox360.com/api/client/box/grouped")
                .addHeader("Authorization", authorization)
                .addHeader("X-FBox-ClientId", Global.signalrClientId)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            String res = response.body().string();
            response.body().close();
            return res;
        }else{
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * @desc 获取监控设备信息
     */
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
            String res = response.body().string();
            response.body().close();
            return res;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    /**
     * @desc 获取设备值
     * @args jsonObject组装的json数据 通过fastJson解析
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
            String res = response.body().string();
            response.body().close();
            return res;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    /**
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
        if (response.isSuccessful()) {
            int code = response.code();
            String res = response.body().string();
            response.body().close();
            if (code == 200) {
                return "{\"msg\": \"success\"}";
            } else {
                return res;
            }
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * @desc 获取各个站点的异常数据
     */
    @RequestMapping(value = "/getExceptionSite", method = RequestMethod.GET)
    String getExceptionSite() {
        JSONArray jsonArray = new JSONArray();
        // 查找异常设备
        List<Statement> equipList = graphService.getEquipExceptionInference("sewage_iot", null,
                "https://www.zm-iot-platform.com/hasExceptionEquip", null);
        formatTDBData(jsonArray, equipList);
        return jsonArray.toJSONString();
    }
    @RequestMapping(value = "/getGraphEquip", method = RequestMethod.POST)
    String getGraphEquip (@RequestBody JSONObject object) {
        String siteURI = object.getString("siteURI");
        JSONArray jsonArray = new JSONArray();
        List<Statement> equipList = graphService.getEquipExceptionInference("sewage_iot", siteURI,
                "https://www.zm-iot-platform.com/hasEquip", null);
        formatTDBData(jsonArray, equipList);
        return jsonArray.toJSONString();
    }
    // 从推理中获取主谓宾构造json数组
    private void formatTDBData(JSONArray jsonArray, List<Statement> equipList) {
        equipList.forEach(item -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("subject", item.getSubject().getURI());
            jsonObject.put("predicate", item.getPredicate().getURI());
            jsonObject.put("object", item.getObject().toString());
            jsonArray.add(jsonObject);
        });
    }

    /**
     * 建立signalr实时监控
     */
    @RequestMapping(value = "/createSignalRConnect", method = RequestMethod.POST)
    String createSignalRConnect (@RequestBody JSONObject jsonObject) {
        String signalrUrl = jsonObject.getString("url");
        String token = jsonObject.getString("token");
        // System.out.println("====打开signalr");
        // 启动signalr
        ConsoleLoggerFactory loggerFactory = new ConsoleLoggerFactory();
        signalRConnection = new FBoxSignalRConnection(signalrUrl, token,
                Global.signalrClientId, Global.proxy, loggerFactory);
        signalRConnection.start();
        return "success";
    }
    /**
     * 关闭signalr监控
     */
    @RequestMapping(value = "/closeSignalRConnect", method = RequestMethod.POST)
    String closeSignalRConnect () {
        signalRConnection.disConnect();
        return "success";
    }
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private FBoxSignalRConnection signalRConnection = null;

    /**
     * 根据前端发来的站点id获取站点数据
     * 先读json数据 修改好后重新写入
     * @param jsonObject
     * @return 站点json数据
     */
    @RequestMapping(value = "/getSiteDetail", method = RequestMethod.POST)
    String getSiteDetail(@RequestBody JSONObject jsonObject) throws IOException {
        String siteID = jsonObject.getString("siteID");
        JSONArray jsonArray = readJsonFile();
        JSONObject ans = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            if (siteID.equals(jsonArray.getJSONObject(i).getString("id"))) {
                ans = jsonArray.getJSONObject(i);
            }
        }
        // 找不到数据自己构造一个初始数据表
        if (ans == null) {
            ans = new JSONObject(jsonArray.getJSONObject(0));
            ans.put("id", siteID);
            // 加入json数据表中
            jsonArray.add(ans);
            writeJsonFile(jsonArray.toJSONString());
        }
        return ans.toJSONString();
    }

    @RequestMapping(value = "/setSiteDetail", method = RequestMethod.POST)
    JSONObject setSiteDetail(@RequestBody JSONObject jsonObject) throws IOException {
        String siteID = jsonObject.getString("id");
        JSONArray jsonArray = readJsonFile();
        for (int i = jsonArray.size() - 1; i >= 0; i--) {
            if (siteID.equals(jsonArray.getJSONObject(i).getString("id"))) {
                jsonArray.remove(i);
                jsonArray.add(jsonObject);
                break; // 同一个站点id只应该出现一次
            }
        }
        writeJsonFile(jsonArray.toJSONString());
        return CommonUtil.jsonResult(1, "更新成功");
    };

    private JSONArray readJsonFile () throws IOException {
        File siteFile = new File("siteDetails/siteDetails.json");
        String input = FileUtils.readFileToString(siteFile, "UTF-8");
        return JSONArray.parseArray(input);
    }

    private boolean writeJsonFile (String content) {
        boolean flag = true;
        String filePath = "siteDetails/siteDetails.json";
        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(filePath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(content);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
}
