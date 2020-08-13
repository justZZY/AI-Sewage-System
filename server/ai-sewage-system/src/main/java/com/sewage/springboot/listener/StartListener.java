package com.sewage.springboot.listener;

import com.sewage.springboot.Global;
import com.sewage.springboot.graph.GraphUpdate;
import com.sewage.springboot.logger.ConsoleLoggerFactory;
import com.sewage.springboot.logger.Logger;
import com.sewage.springboot.logger.LoggerFactory;
import com.sewage.springboot.signalr.FBoxSignalRConnection;
import com.sewage.springboot.signalr.ServerCaller;
import com.sewage.springboot.signalr.StaticCredentialProvider;
import com.sewage.springboot.signalr.TokenManager;
import com.sewage.springboot.timer.task.WriteRecordTask;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class StartListener implements ApplicationListener<ApplicationReadyEvent> {
    private ConsoleLoggerFactory loggerFactory = new ConsoleLoggerFactory();
    private Logger logger = loggerFactory.createLogger("StartListener");

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.logger.logInformation("====服务器启动");
        // 半小时做一次图谱数据监测
        Timer timer = new Timer();
        timer.schedule(new GraphUpdate(), 0, 1800 * 1000);
        // 1小时记录一次数据
        timer.schedule(new WriteRecordTask(), 0, 3600 * 1000);
        // 启动数据监控
        startSignalR();
    }

    private void startSignalR() {
        // 指定连接服务器的凭据参数
        TokenManager tokenManager = new TokenManager(new StaticCredentialProvider(Global.clientId, Global.clientSecret, Global.username, Global.password), Global.idServerUrl, loggerFactory);

        ServerCaller commServer = new ServerCaller(tokenManager, Global.commServerApiUrl, Global.signalrClientId, loggerFactory);
        ServerCaller appServer = new ServerCaller(tokenManager, Global.appServerApiUrl, Global.signalrClientId, loggerFactory);
        ServerCaller hdataServer = new ServerCaller(tokenManager, Global.hdataServerApiUrl, Global.signalrClientId, loggerFactory);

        Global.commServer = commServer;
        Global.appServer = appServer;
        Global.hdataServer = hdataServer;

        //建立signalr实例，signalr为单例模式
        FBoxSignalRConnection fboxSignalR = new FBoxSignalRConnection(Global.commServerSignalRUrl, Global.signalrClientId, tokenManager, Global.proxy, loggerFactory);

        // 连接SignalR推送通道
        fboxSignalR.start();
    }
}