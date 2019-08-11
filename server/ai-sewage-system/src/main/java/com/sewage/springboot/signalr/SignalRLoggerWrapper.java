package com.sewage.springboot.signalr;

import com.github.signalr4j.client.LogLevel;
import com.github.signalr4j.client.Logger;

public class SignalRLoggerWrapper implements Logger {

    private final com.sewage.springboot.logger.Logger logger;

    public SignalRLoggerWrapper(com.sewage.springboot.logger.Logger logger){
        this.logger = logger;
    }
    @Override
    public void log(String message, LogLevel level) {
        switch (level) {
            case Critical:
                this.logger.logError(message);
                break;
            case Information:
                this.logger.logInformation(message);
                break;
            case Verbose:
                this.logger.logTrace(message);
                break;
        }
    }

}
