package com.sewage.springboot.signalr;

import com.github.signalr4j.client.hubs.HubProxy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sewage.springboot.Global;
import com.sewage.springboot.entity.BoxStateChanged;
import com.sewage.springboot.logger.Logger;
import com.sewage.springboot.logger.LoggerFactory;
import okhttp3.*;

import java.io.IOException;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class FBoxSignalRConnection extends SignalRConnectionBase {
    private final Gson gson;
    private final Logger logger;
    private ConcurrentHashMap<Long, LongAdder> dmonIds = new ConcurrentHashMap<>();
    private LongAdder dmonMsgCounter = new LongAdder();
    private long lastDmonItemCount;
    private long lastDmonMsgCount;
    private long lastReportTime;
    private Proxy proxy;
    private LongAdder dmonItemCounter = new LongAdder();
    private String token;

    public FBoxSignalRConnection(String hubUrl, String token, String signalrClientId, Proxy proxy, LoggerFactory loggerFactory) {
        super(hubUrl, token, signalrClientId, proxy, loggerFactory);
        this.logger = loggerFactory.createLogger("FBoxSignalRConnection");
        this.proxy = proxy;
        this.token = token;
        gson = new GsonBuilder().create();
        new Thread(() -> {
            //统计条目数线程
            for (; ; ) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long currentTime = System.nanoTime();
                long currentMsgCount = this.dmonMsgCounter.longValue();
                long currentItemCount = this.dmonItemCounter.longValue();
                long msgRate = (currentMsgCount - this.lastDmonMsgCount) * 1000000000 / (currentTime - this.lastReportTime);
                long itemRate = (currentItemCount - this.lastDmonItemCount) * 1000000000 / (currentTime - this.lastReportTime);
                this.logger.logInformation(String.format("Dmon id count: %d, item rate: %d, message rate: %d", this.dmonIds.size(), itemRate, msgRate));
                this.lastReportTime = currentTime;
                this.lastDmonMsgCount = currentMsgCount;
                this.lastDmonItemCount = currentItemCount;
            }
        }).start();
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

    SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Override
    protected void onHubProxyCreated(HubProxy hubProxy) {
        //signalr实时数据推送事件，接收此事件数据前提条件，开启监控点数据推送控制接口（订阅）
        hubProxy.subscribe("dmonUpdateValue").addReceivedHandler(jsonElements -> {
            Global.threadPool.submit(() -> {
                //try{
                    this.dmonMsgCounter.increment();
                    //System.out.println("Dmon data received: ");
                    JsonArray items = jsonElements[1].getAsJsonArray();
                    String timestamp = "";
                    for (com.google.gson.JsonElement jsonElement : items) {
                        JsonObject item = jsonElement.getAsJsonObject();
                        this.dmonIds.computeIfAbsent(item.get("id").getAsLong(), aLong -> new LongAdder()).increment();
                        this.dmonItemCounter.increment();
                        //收到的推送数据
                        String name = item.get("name").getAsString();
                        String value = item.get("value").getAsString();
                        long time = item.get("t").getAsLong();
                        timestamp = String.valueOf(sdf.format(time));
                        this.logger.logInformation(String.format(" %s, %s, %d, %s\n",name,value,time,timestamp));
                        //System.out.printf("%s:%s:%d:%s\n", name,value,time,timestamp);
                        //监控点正常无status属性
                        //long statu = item.get("status").getAsLong();
                    };
                    //打印监控点的值集合，集合详细信息请看接口文档http://docs.flexem.net/fbox/zh-cn/tutorials/RealtimeDataPush.html
                    System.out.printf("%d",jsonElements[1].getAsLong());
                    //打印boxUid
                    System.out.printf("%d",jsonElements[2].getAsLong());
//                }
//                catch (Exception e) {
//                    System.out.printf("%s", e);
//                }
            });
        });

        // signalr报警触发事件
        hubProxy.subscribe("alarmTriggered").addReceivedHandler(jsonElements -> {
            Global.threadPool.submit(() -> {
                System.out.println("Alarm triggered: ");
                for (com.google.gson.JsonElement jsonElement : jsonElements) {
                    //报警推送消息全部打印。具体参数解释请看接口文档http://docs.flexem.net/fbox/zh-cn/tutorials/AlarmTiggerPush.html
                    System.out.println("\t" + jsonElement);
                };
                //打印报警条目的值集合
                System.out.printf("%d",jsonElements[1].getAsLong());
                //打印boxUid
                System.out.printf("%d",jsonElements[2].getAsLong());
            });
        });

        // signalr报警还原事件
        hubProxy.subscribe("alarmRecovered").addReceivedHandler(jsonElements -> {
            Global.threadPool.submit(() -> {
                System.out.println("Alarm recovered: ");
                for (com.google.gson.JsonElement jsonElement : jsonElements) {
                    //报警推送消息全部打印。具体参数解释请看接口文档http://docs.flexem.net/fbox/zh-cn/tutorials/AlarmReductionPush.html
                    System.out.println("\t" + jsonElement);
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
                System.out.println("Box state changed.");
                if (jsonElements.length <= 0)
                    return;
                BoxStateChanged[] stateChanges = gson.fromJson(jsonElements[0], BoxStateChanged[].class);
                this.logger.logInformation(String.format("receive count: %d", stateChanges.length));
                for (BoxStateChanged stateChange : stateChanges) {
                    // stateChange.id 是盒子列表中BoxReg对象下的box.id，可以根据这个过滤要开的盒子。
                    // stateChange.state 为1、2是盒子上线事件。实时数据推送需要开点
                    if (stateChange.state == 1 || stateChange.state == 2) {
                            try {
                                activeData(stateChange.id);
                            } catch (IOException e) {
                                System.out.println(e);
                                e.printStackTrace();
                            }
                    }
                }
            });
        });
    }
    /*
     * 启动设备订阅接口
     */
    private void activeData (long id) throws IOException {
        String url = "http://fbcs101.fbox360.com/api/box/" + id + "/dmon/start";
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().build();
        Request request = new Request.Builder().url(url)
                .post(formBody)
                .addHeader("Authorization", "Bearer " + this.token)
                .addHeader("X-FBox-ClientId", Global.signalrClientId)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            // 盒子每次上线后，均需要开启FBox数据推送控制接口（订阅）
            // token有效期为两小时。若token过期，demo会自动刷新token。所以返回401后均需要重试接口
            this.logger.logInformation(String.format("Start dmon points on box ok %s\n", id));
        }else{
            throw new IOException("Unexpected code " + response);
        }
    }
}
