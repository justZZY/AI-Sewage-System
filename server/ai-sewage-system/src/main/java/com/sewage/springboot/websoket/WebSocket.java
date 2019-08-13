package com.sewage.springboot.websoket;

import com.sewage.springboot.logger.ConsoleLoggerFactory;
import com.sewage.springboot.logger.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@EnableWebSocket
@ServerEndpoint(value = "/websocket")
public class WebSocket {
    private Session session;
    // 线程安全集合 存放每一个socket对象
    private static CopyOnWriteArraySet<WebSocket> webSokets = new CopyOnWriteArraySet<WebSocket>();
    //静态变量 用来记录当前在线连接数 应该把它设计成线程安全的。
    private static int onlineCount = 0;
    private Logger logger = new ConsoleLoggerFactory().createLogger("WebSocket");
    /*
     * 建立socket连接
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSokets.add(this);
        addOnlineCount();
        // undo-发送建立连接成功函数
        // 打印连接建立成功日志
        logger.logInformation("建立WebSocket连接成功 当前连接数:" + getOnlineCount());
    }

    /*
     * 关闭socket连接
     */
    @OnClose
    public void onClose () {
        webSokets.remove(this);
        subOnlineCount();
        // 打印连接断开日志
        logger.logInformation("关闭WebSocket连接成功 当前连接数:" + getOnlineCount());
    }

    /*
     * 接收客户端消息
     */
    @OnMessage
    public void onMessage (String message, Session session) {
        // 打印日志信息
        logger.logInformation("来自客户端的消息: " + message);
    }

    @OnError
    public void onError (Session session, Throwable error) {
        logger.logError("WebSocket连接发生错误");
        error.printStackTrace();
    }

    // 发消息
    public void sendMessage (String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    // 群发socket消息
    public static void sendAll (String message) {
        if (getOnlineCount() == 0) {
            
        }
        for (WebSocket item : webSokets) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 线程安全级别的数量增减函数
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }
}
