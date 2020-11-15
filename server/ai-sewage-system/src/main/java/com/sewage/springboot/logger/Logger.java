package com.sewage.springboot.logger;

public interface Logger{
    void logInformation(String msg);
    void logWarning(String msg);
    void logError(String msg);
    void logTrace(String msg);
    void logDebug(String msg);
}


