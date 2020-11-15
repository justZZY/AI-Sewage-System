package com.sewage.springboot.util;

import com.sewage.springboot.Global;
import com.sewage.springboot.logger.ConsoleLoggerFactory;
import com.sewage.springboot.logger.Logger;
import com.sewage.springboot.signalr.FBoxSignalRConnection;
import com.sewage.springboot.signalr.ServerCaller;
import com.sewage.springboot.signalr.StaticCredentialProvider;
import com.sewage.springboot.signalr.TokenManager;

public class SignalRTools {
    private FBoxSignalRConnection fBoxSignalRConnection;
    private ConsoleLoggerFactory loggerFactory = new ConsoleLoggerFactory();
    private Logger logger = loggerFactory.createLogger("SignalRTools");

    public SignalRTools() {
        // 指定连接服务器的凭据参数
        TokenManager tokenManager = new TokenManager(new StaticCredentialProvider(Global.clientId, Global.clientSecret, Global.username, Global.password), Global.idServerUrl, loggerFactory);

        ServerCaller commServer = new ServerCaller(tokenManager, Global.commServerApiUrl, Global.signalrClientId, loggerFactory);
        ServerCaller appServer = new ServerCaller(tokenManager, Global.appServerApiUrl, Global.signalrClientId, loggerFactory);
        ServerCaller hdataServer = new ServerCaller(tokenManager, Global.hdataServerApiUrl, Global.signalrClientId, loggerFactory);

        Global.commServer = commServer;
        Global.appServer = appServer;
        Global.hdataServer = hdataServer;

        //建立signalr实例，signalr为单例模式
        this.fBoxSignalRConnection = new FBoxSignalRConnection(Global.commServerSignalRUrl, Global.signalrClientId, tokenManager, Global.proxy, loggerFactory);
    }

    public void start () {
        this.fBoxSignalRConnection.start();
        logger.logWarning("====Start SignalR Connection");
    }

    public void stop () {
        this.fBoxSignalRConnection.stop();
        logger.logWarning("====Stop SignalR Connection");
    }
}
