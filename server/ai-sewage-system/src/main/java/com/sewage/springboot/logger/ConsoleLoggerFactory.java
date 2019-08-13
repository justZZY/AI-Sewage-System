package com.sewage.springboot.logger;

public class ConsoleLoggerFactory implements LoggerFactory {
    @Override
    public Logger createLogger(String name) {
        if (name.equals("FBoxSignalRConnection") || name.equals("SignalRConnectionBase") || name.equals("ServerCaller")
                || name.equals("WebSocket")) {
            return new ConsoleLogger(name);
        }
        return new NullLogger();
    }
}
