package com.sewage.springboot.logger;

public class NullLogger implements Logger {
    @Override
    public void logInformation(String msg) {
        
    }

    @Override
    public void logWarning(String msg) {

    }

    @Override
    public void logError(String msg) {

    }

    @Override
    public void logTrace(String msg) {

    }
}
