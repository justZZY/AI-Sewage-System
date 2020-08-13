package com.sewage.springboot.signalr;

import org.apache.http.client.HttpResponseException;

public class BoxServerResponseException extends HttpResponseException {
    public BoxServerResponseException(int statusCode, String s) {
        super(statusCode, s);
    }

    public BoxServerResponseException(int statusCode, String s, int errCode) {
        super(statusCode, s);
        this.errCode = errCode;
    }

    public int errCode;


}
