package com.sewage.springboot.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.Global;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class FboxTools {
    private String authorization;
    private final String API_BASE_URL = "http://fbcs101.fbox360.com/api/";
    /**
     * @desc 用于服务启动时获取站点设备以及硬件设备数据
     * @return 设备数据信息 json数组的方式
     * @throws IOException
     */
    public JSONArray getSiteData() throws IOException {
        // 获取设备session
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("username", Global.username)
                .add("password", Global.password)
                .add("client_id", Global.clientId)
                .add("client_secret", Global.clientSecret)
                .add("scope", Global.scope)
                .add("grant_type", Global.grant_type_login)
                .build();
        Request requestSession = new Request.Builder()
                .url("https://account.flexem.com/core/connect/token")
                .post(formBody)
                .build();
        Response responseSession = client.newCall(requestSession).execute();
        if (responseSession.isSuccessful()) {
            JSONObject sessionJson = JSONObject.parseObject(responseSession.body().string());
            // 获取设备组信息
            Request requestEquip = new Request.Builder().url("http://fbox360.com/api/client/box/grouped")
                    .addHeader("Authorization", "Bearer " + sessionJson.getString("access_token"))
                    .addHeader("X-FBox-ClientId", Global.signalrClientId)
                    .build();
            // 更新session用于站点与设备的请求
            this.authorization = "Bearer " + sessionJson.getString("access_token");
            Response responseEquip = client.newCall(requestEquip).execute();
            if (responseEquip.isSuccessful()){
                // 这里获取到的是站点名称的数据
                JSONArray equipJson = JSONObject.parseArray(responseEquip.body().string());
                responseEquip.body().close();
                responseSession.body().close();
                return equipJson;
            }else{
                throw new IOException("Unexpected code " + responseEquip);
            }
        } else {
            throw new IOException("Unexpected code " + responseSession);
        }
    }

    /**
     * @desc 获取具体某个站点的设备数据
     * @param siteNo
     * @return
     * @throws IOException
     */
    public JSONArray getEquipData (String siteNo) throws IOException {
        String url = this.API_BASE_URL + "v2/box/dmon/grouped?boxNo=" + siteNo;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url)
                .addHeader("Authorization", this.authorization)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            JSONArray res = JSONArray.parseArray(response.body().string());
            response.body().close();
            return res;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
