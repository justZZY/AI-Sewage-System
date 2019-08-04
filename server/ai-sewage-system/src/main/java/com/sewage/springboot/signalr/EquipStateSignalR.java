package com.sewage.springboot.signalr;

import com.alibaba.fastjson.JSONObject;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

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
        hubConnection = new HubConnection(url, qs, true, logger);
        HubProxy hubProxy = hubConnection.createHubProxy("clientHub");
        hubProxy.on("SubscriptionHandler1", new SubscriptionHandler1<JSONObject>() {
            @Override
            public void run(JSONObject jsonObject) {
                System.out.println(jsonObject.toString());
            }
        }, JSONObject.class);
        hubConnection.start();
        ConnectionState connectionState =  hubConnection.getState();
        return connectionState;
    }
    public ConnectionState stopSignalRConnection () {
        hubConnection.stop();
        ConnectionState connectionState =  hubConnection.getState();
        return connectionState;
    }
}
