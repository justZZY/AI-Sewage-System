package com.sewage.springboot.logger;

import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleLogger implements Logger {
    private final String name;
    private final SimpleDateFormat formatter;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConsoleLogger.class);

    ConsoleLogger(String name) {
        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.name = name;
    }

    @Override
    public void logInformation(String msg) {
        msg = formatter.format(new Date()) + " [Info ][" + name + "] " + msg;
        // 注释 避免控制台大量打印
        // System.out.println(formatter.format(new Date()) + " [Info ][" + name + "] " + msg);
        logger.info(msg);
    }

    @Override
    public void logWarning(String msg) {
        msg = formatter.format(new Date()) + " [Warn ][" + name + "] " + msg;
        System.out.println(msg);
        logger.warn(msg);
    }

    @Override
    public void logError(String msg) {
        msg = formatter.format(new Date()) + " [Error][" + name + "] " + msg;
        System.out.println(msg);
        logger.error(msg);
    }

    @Override
    public void logTrace(String msg) {
        msg = formatter.format(new Date()) + " [Trace][" + name + "] " + msg;
        System.out.println(msg);
        logger.trace(msg);
    }
}
