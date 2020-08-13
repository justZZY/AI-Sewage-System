package com.sewage.springboot.signalr;

import com.github.signalr4j.client.ConnectionState;
import com.github.signalr4j.client.http.java.JavaHttpConnection;
import com.github.signalr4j.client.hubs.HubConnection;
import com.github.signalr4j.client.hubs.HubProxy;
import com.github.signalr4j.client.transport.ServerSentEventsTransport;
import com.sewage.springboot.logger.LoggerFactory;
import com.sewage.springboot.logger.Logger;

import java.io.IOException;
import java.net.Proxy;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public abstract class SignalRConnectionBase {
    private final Semaphore connectEvent;
    private HubConnection hubConnection;
    private HubProxy hubProxy;
    private final Logger logger;
    private final LoggerFactory loggerFactory;
    private final SignalRLoggerWrapper javaconnLogger;
    private final SignalRLoggerWrapper sseLogger;
    private String hubUrl;
    private String signalrClientId;
    private Proxy proxy;
    private TokenManager tokenManager;
    private String accessToken; // 记录当前token信息
    private boolean shouldGetNewToken;
    private int retryCount;
    private long lastConnectedTime = 0;
    private boolean reconnecting;
    private boolean monitorStarted;
    private boolean quitFlag = false;

    public SignalRConnectionBase(String hubUrl, String signalrClientId, TokenManager tokenManager, Proxy proxy, LoggerFactory loggerFactory) {
        this.hubUrl = hubUrl;
        this.signalrClientId = signalrClientId;
        this.proxy = proxy;
        // tokenManager 用于token信息重连
        // 优化服务器signalr连接方式
        // 用于数据重连
        // 2020.08.12
        this.tokenManager = tokenManager;
        this.loggerFactory = loggerFactory;
        this.logger = loggerFactory.createLogger("SignalRConnectionBase");
        this.sseLogger = new SignalRLoggerWrapper(this.loggerFactory.createLogger("ServerSentEventsTransport"));
        this.javaconnLogger = new SignalRLoggerWrapper(this.loggerFactory.createLogger("SignalRTransportConnection"));
        this.connectEvent = new Semaphore(1);
        new Thread(() -> {
            try {
                SignalRConnectWorker();
            } catch (InterruptedException e) {
                logger.logError("SignalR connect worker exited with " + e.toString());
            }
        }).start();
    }

    private void HookEvents() {
        this.hubConnection.error(throwable -> onConnectionError(throwable));
        this.hubConnection.reconnecting(() -> onReconnecting());
        this.hubConnection.reconnected(() -> onReconnected());
        this.hubConnection.closed(() -> onConnectionClosed());
        this.hubConnection.connected(() -> connected());
    }

    private void UnhookEvents() {
        this.hubConnection.error(null);
        this.hubConnection.reconnecting(null);
        this.hubConnection.reconnected(null);
        this.hubConnection.closed(null);
        this.hubConnection.connected(null);
    }

    private void onReconnecting() {
        this.logger.logInformation("Reconnecting. hubconn=" + this.hubConnection.getUrl());
    }

    private void onReconnected() {
        this.logger.logInformation("Reconnected. hubconn=" + this.hubConnection.getUrl());
    }

    private void onConnectionClosed() {
        this.logger.logInformation("Closed. hubconn=" + this.hubConnection.getUrl());
        this.connectEvent.release();
    }

    private void onConnectionError(Throwable e) {
        this.logger.logInformation(String.format("Connection error. exception=%s. hubConnection=%s", e, this.hubConnection.getUrl()));
        if (e != null) {
            Throwable cause = e.getCause();
            if (cause != null && cause.getMessage() != null && cause.getMessage().contains("401")) {
                this.shouldGetNewToken = true;
            }
        }
    }

    protected abstract void onHubProxyCreated(HubProxy hubProxy);

    protected abstract void onHubProxyDestroyed(HubProxy hubProxy);

    public void connected() {
        this.reconnecting = false;
        this.logger.logInformation("Connected. hubconn=" + this.hubConnection.getUrl());
    }

    private void UpdateToken() throws LoginFailedException, InterruptedException {
        int retryCount2 = 0;
        for (; ; ) {
            try {
                this.accessToken = this.tokenManager.getOrUpdateToken(this.accessToken);
                break;
            } catch (LoginFailedException e) {
                throw e;
            } catch (IOException e) {
                logger.logError("Get token failed. " + e.toString());
                int waitTime2 = retryCount2++ * 1000;
                if (waitTime2 > 300000) {
                    waitTime2 = 5000;
                }
                Thread.sleep(waitTime2);
            }
        }
    }

    private void SignalRConnectWorker() throws InterruptedException {
        //signalr重连代码
        for (; ; ) {
            do {
                if (this.hubConnection != null)
                    this.logger.logInformation("Current connection state is " + this.hubConnection.getState());
                this.connectEvent.acquire();
            }
            while (this.hubConnection != null && this.hubConnection.getState() == ConnectionState.Connected);
            if (quitFlag) {
                break;
            }
            try {
                //刷新token
                UpdateToken();
                if (this.hubConnection != null)
                    this.UnhookEvents();
                if (this.hubProxy != null) {
                    onHubProxyDestroyed(this.hubProxy);
                }
                if (this.hubConnection != null)
                    this.hubConnection.disconnect();
                this.hubConnection = new HubConnection(hubUrl, "cid=" + signalrClientId, true, new SignalRLoggerWrapper(loggerFactory.createLogger("SignalR")));
                this.hubConnection.setReconnectOnError(false);
                this.HookEvents();
                this.hubProxy = this.hubConnection.createHubProxy("clienthub");
                this.onHubProxyCreated(this.hubProxy);
                this.hubConnection.setCredentials(request -> request.addHeader("Authorization", "Bearer " + this.accessToken));
                this.connectEvent.drainPermits();
                this.hubConnection.start(new ServerSentEventsTransport(this.sseLogger, new JavaHttpConnection(this.javaconnLogger))).get(40, TimeUnit.SECONDS);
                this.retryCount = 0;
                this.shouldGetNewToken = false;
                this.lastConnectedTime = new Date().getTime();
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause != null) {
                    String msg = cause.getMessage();
                    if (msg != null && msg.contains("401"))
                        this.shouldGetNewToken = true;
                }

                int waitTime = this.retryCount++ * 1000;
                if (waitTime > 60000) {
                    waitTime = 5000;
                }

                if (waitTime > 0)
                    Thread.sleep(waitTime);
                this.connectEvent.release();
                this.reconnecting = true;
            }
        }
    }

    public void start() {
        this.connectEvent.release();
    }

    public void stop() {
        this.hubConnection.stop();
    }

    public void disConnect() {
        this.hubConnection.disconnect();
        quitFlag = true;
    }
}
