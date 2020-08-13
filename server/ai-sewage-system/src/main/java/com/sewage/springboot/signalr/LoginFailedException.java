package com.sewage.springboot.signalr;

import org.apache.http.client.HttpResponseException;

public class LoginFailedException extends HttpResponseException {
    public LoginFailedException(int statusCode, String s) {
        super(statusCode, s);
    }
}
