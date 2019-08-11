package com.sewage.springboot.signalr;

import com.github.signalr4j.client.ConnectionState;
import com.github.signalr4j.client.http.java.JavaHttpConnection;
import com.github.signalr4j.client.hubs.HubConnection;
import com.github.signalr4j.client.hubs.HubProxy;
import com.github.signalr4j.client.transport.ServerSentEventsTransport;
import com.sewage.springboot.logger.LoggerFactory;
import com.sewage.springboot.logger.Logger;
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
    private String accessToken;
    private boolean shouldGetNewToken;
    private int retryCount;
    private long lastConnectedTime = 0;
    private boolean reconnecting;
    private boolean monitorStarted;

    public SignalRConnectionBase(String hubUrl, String token, String signalrClientId, Proxy proxy, LoggerFactory loggerFactory) {
        this.hubUrl = hubUrl;
        this.accessToken = token;
        this.signalrClientId = signalrClientId;
        this.proxy = proxy;
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

    private void SignalRConnectWorker() throws InterruptedException {
        //signalr重连代码
        for (; ; ) {
            do {
//                this.logger.logTrace("Current connection state is " + this.hubConnection.getState());
                this.connectEvent.acquire();
            }
            while (this.hubConnection != null && this.hubConnection.getState() == ConnectionState.Connected);

            try {
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
}
