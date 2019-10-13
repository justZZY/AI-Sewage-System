package com.sewage.springboot.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleLogger implements Logger {
    private final String name;
    private final SimpleDateFormat formatter;

    ConsoleLogger(String name) {
        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.name = name;
    }

    @Override
    public void logInformation(String msg) {
        // 注释 避免控制台大量打印
        // System.out.println(formatter.format(new Date()) + " [Info ][" + name + "] " + msg);
    }

    @Override
    public void logWarning(String msg) {
        System.out.println(formatter.format(new Date()) + " [Warn ][" + name + "] " + msg);
    }

    @Override
    public void logError(String msg) {
        System.out.println(formatter.format(new Date()) + " [Error][" + name + "] " + msg);
    }

    @Override
    public void logTrace(String msg) {
        System.out.println(formatter.format(new Date()) + " [Trace][" + name + "] " + msg);
    }
}
