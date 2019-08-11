package com.sewage.springboot.logger;

public class ConsoleLoggerFactory implements LoggerFactory {
    @Override
    public Logger createLogger(String name) {
        if (name == "FBoxSignalRConnection" || name == "SignalRConnectionBase" || name == "ServerCaller") {
            return new ConsoleLogger(name);
        }

        return new NullLogger();
    }
}
