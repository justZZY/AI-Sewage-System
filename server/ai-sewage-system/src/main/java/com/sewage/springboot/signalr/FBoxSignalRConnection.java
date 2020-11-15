package com.sewage.springboot.signalr;

import com.github.signalr4j.client.hubs.HubProxy;
import com.github.signalr4j.client.transport.WebsocketTransport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sewage.springboot.Global;
import com.sewage.springboot.been.SpringContextHolder;
import com.sewage.springboot.entity.BoxStateChanged;
import com.sewage.springboot.logger.Logger;
import com.sewage.springboot.logger.LoggerFactory;
import com.sewage.springboot.service.JobService;
import com.sewage.springboot.service.impl.JobServiceImpl;
import com.sewage.springboot.websoket.WebSocket;

import java.io.IOException;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import javax.annotation.PostConstruct;


/**
 * @desc 服务器实时推送函数, 适应本项目服务器连接方法
 * @Author zzy
 */
public class FBoxSignalRConnection extends SignalRConnectionBase {
    private final Gson gson;
    private final Logger logger;
    private ConcurrentHashMap<Long, LongAdder> dmonIds = new ConcurrentHashMap<>();
    private LongAdder dmonMsgCounter = new LongAdder();
    private Proxy proxy;
    private LongAdder dmonItemCounter = new LongAdder();
    private String token;
    
    private static JobService jobService;
    
    public FBoxSignalRConnection(String hubUrl, String signalrClientId, TokenManager tokenManager, Proxy proxy, LoggerFactory loggerFactory) {
        super(hubUrl, signalrClientId, tokenManager, proxy, loggerFactory);
        this.logger = loggerFactory.createLogger("FBoxSignalRConnection");
        this.proxy = proxy;
        gson = new GsonBuilder().create();
    }

    @Override
    public void connected() {
        super.connected();
        dmonIds.clear();
    }


    protected void onHubProxyDestroyed(HubProxy hubProxy){
        hubProxy.removeSubscription("dmonUpdateValue");
        hubProxy.removeSubscription("alarmTriggered");
        hubProxy.removeSubscription("alarmRecovered");
        hubProxy.removeSubscription("boxConnStateChanged");
    }

    SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onHubProxyCreated(HubProxy hubProxy) {
        //signalr实时数据推送事件，接收此事件数据前提条件，开启监控点数据推送控制接口（订阅）
        hubProxy.subscribe("dmonUpdateValue").addReceivedHandler(jsonElements -> {
            Global.threadPool.submit(() -> {
                //try{
                this.dmonMsgCounter.increment();
                JsonArray items = jsonElements[1].getAsJsonArray();
                String boxUid = jsonElements[2].getAsString();
                String timestamp = "";
                for (com.google.gson.JsonElement jsonElement : items) {
                    JsonObject item = jsonElement.getAsJsonObject();
                    this.dmonIds.computeIfAbsent(item.get("id").getAsLong(), aLong -> new LongAdder()).increment();
                    this.dmonItemCounter.increment();
                    // 收到的推送数据
                    String name = item.get("name").getAsString();
                    String value = item.get("value").getAsString();
                    long time = item.get("t").getAsLong();
                    timestamp = sdf.format(time);
                    this.logger.logDebug(String.format(" %s, %s, %d, %s\n", name, value, time, timestamp));
                    WebSocket.sendAll(boxUid + '_' + name + '_' + value + '_' + timestamp);
                }
                //打印监控点的值集合，集合详细信息请看接口文档http://docs.flexem.net/fbox/zh-cn/tutorials/RealtimeDataPush.html
                System.out.printf("%d", jsonElements[1].getAsLong());
                //打印boxUid
                System.out.printf("%d", jsonElements[2].getAsLong());
            });
        });

        // signalr报警触发事件
        hubProxy.subscribe("alarmTriggered").addReceivedHandler(jsonElements -> {
            Global.threadPool.submit(() -> {
//                System.out.println("Alarm triggered: ");
                for (com.google.gson.JsonElement jsonElement : jsonElements) {
                    //报警推送消息全部打印。具体参数解释请看接口文档http://docs.flexem.net/fbox/zh-cn/tutorials/AlarmTiggerPush.html
//                    System.out.println("\t" + jsonElement);
                    WebSocket.sendAll(jsonElement.toString());
                }

                /** 报警工单 */
                if(jobService==null) jobService = SpringContextHolder.getBean(JobServiceImpl.class); 
                String boxUid = jsonElements[2].getAsString();
                JsonArray alarmElements = jsonElements[1].getAsJsonArray();
                for (com.google.gson.JsonElement alarmElement : alarmElements) {
	                JsonObject alarmItemJson = alarmElement.getAsJsonObject();
	                String id = alarmItemJson.get("id").getAsString();			// 监控点条目的unique_id
	                String value = alarmItemJson.get("value").getAsString();	// 监控点条目值
	                String name = alarmItemJson.get("name").getAsString();		// 报警条目编码
	                String msg = alarmItemJson.get("msg").getAsString();		// 报警条目信息
	                String status = alarmItemJson.get("status").getAsString(); // 条目状态 ,如果条目正常，则无此属性
	                System.out.println("box:" + boxUid + "\nname：" + name + "\nmsg："+ msg);
	                jobService.CreateAlarmJob(boxUid, name, msg);
                }
                //打印报警条目的值集合
                System.out.printf("%d",jsonElements[1].getAsLong());
                //打印boxUid
                System.out.printf("%d",jsonElements[2].getAsLong());
            });
        });

        // signalr报警还原事件
        hubProxy.subscribe("alarmRecovered").addReceivedHandler(jsonElements -> {
            Global.threadPool.submit(() -> {
//                System.out.println("Alarm recovered: ");
                for (com.google.gson.JsonElement jsonElement : jsonElements) {
                    //报警推送消息全部打印。具体参数解释请看接口文档http://docs.flexem.net/fbox/zh-cn/tutorials/AlarmReductionPush.html
//                    System.out.println("\t" + jsonElement);
                    WebSocket.sendAll(jsonElement.toString());
                };
                //打印报警条目的值集合
                System.out.printf("%d",jsonElements[1].getAsLong());
                //打印boxUid
                System.out.printf("%d",jsonElements[2].getAsLong());
            });
        });

        // signalr盒子状态变更事件
        hubProxy.subscribe("boxConnStateChanged").addReceivedHandler(jsonElements -> {
            Global.threadPool.submit(() -> {
                this.logger.logInformation("Box state changed.");
                if (jsonElements.length <= 0)
                    return;
                BoxStateChanged[] stateChanges = gson.fromJson(jsonElements[0], BoxStateChanged[].class);
                this.logger.logInformation(String.format("receive count: %d", stateChanges.length));
                for (BoxStateChanged stateChange : stateChanges) {
                    // stateChange.id 是盒子列表中BoxReg对象下的box.id，可以根据这个过滤要开的盒子。
                    // stateChange.state 为1、2是盒子上线事件。实时数据推送需要开点
                    if (stateChange.state == 1 || stateChange.state == 2) {
                        try {
                            // 盒子每次上线后，均需要开启FBox数据推送控制接口（订阅）
                            Global.commServer.executePost("box/" + stateChange.id + "/dmon/start", String.class);
                            // token有效期为两小时。若token过期，demo会自动刷新token。所以返回401后均需要重试接口
                            this.logger.logInformation(String.format("Start dmon points on box ok %s\n",stateChange.id));
                        } catch (IOException e) {
                            System.out.println(e);
                            e.printStackTrace();
                        }
                    }
                }

            });
        });
    }
}
