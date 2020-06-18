package com.sewage.springboot.logger;

public class ConsoleLoggerFactory implements LoggerFactory {
    @Override
    public Logger createLogger(String name) {
        return new ConsoleLogger(name);
    }
}
