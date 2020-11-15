package com.sewage.springboot.listener;

import com.sewage.springboot.graph.GraphUpdate;
import com.sewage.springboot.logger.ConsoleLoggerFactory;
import com.sewage.springboot.logger.Logger;
import com.sewage.springboot.timer.task.WriteRecordTask;

import com.sewage.springboot.util.SignalRTools;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class StartListener implements ApplicationListener<ApplicationReadyEvent> {
    private ConsoleLoggerFactory loggerFactory = new ConsoleLoggerFactory();
    private Logger logger = loggerFactory.createLogger("StartListener");

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.logger.logWarning("====服务器启动");
        // 半小时做一次图谱数据监测
        Timer timer = new Timer();
        timer.schedule(new GraphUpdate(), 0, 1800 * 1000);
        // 1小时记录一次数据
        timer.schedule(new WriteRecordTask(), 0, 3600 * 1000);
        // 启动数据监控
        SignalRTools signalRTools = new SignalRTools();
        signalRTools.start();
    }
}