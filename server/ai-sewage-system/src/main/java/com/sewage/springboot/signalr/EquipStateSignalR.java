package com.sewage.springboot.signalr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler3;

public class EquipStateSignalR {
    private HubConnection hubConnection = null;
    public ConnectionState createSignalRConnect (String url,String qs) {
        Logger logger = new Logger() {
            @Override
            public void log(String s, LogLevel logLevel) {
                s = "start connecting";
                logLevel = LogLevel.Information;
            }
        };
        System.out.println(url);
        System.out.println(qs);
        hubConnection = new HubConnection(url, qs, true, logger);
        HubProxy hubProxy = hubConnection.createHubProxy("clientHub");
        hubProxy.on("dMonUpdateValue", new SubscriptionHandler3<Integer, JsonArray, String>() {
            @Override
            public void run(Integer integer, JsonArray jsonElements, String s) {
                System.out.println("=======");
                System.out.println(integer);
                System.out.println(jsonElements);
                System.out.println(s);
            }
        }, Integer.class, JsonArray.class, String.class);
        hubConnection.start();
        ConnectionState connectionState =  hubConnection.getState();
        System.out.println(connectionState);
        return connectionState;
    }
    public ConnectionState stopSignalRConnection () {
        hubConnection.stop();
        ConnectionState connectionState =  hubConnection.getState();
        return connectionState;
    }
}
