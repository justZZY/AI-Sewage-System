package com.sewage.springboot.signalr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sewage.springboot.logger.Logger;
import com.sewage.springboot.logger.LoggerFactory;
import org.apache.http.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.ExponentialBackOffSchedulingStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerCaller {
    private static final long RETRY_SLEEP_TIME_MILLIS = 2000;
    private static ArrayList<Class<?>> exceptionWhitelist = new ArrayList<>();
    private static ArrayList<Class<?>> exceptionBlacklist = new ArrayList<>();

    static {
        // Retry if the server dropped connection on us
        exceptionWhitelist.add(NoHttpResponseException.class);
        // retry-this, since it may happens as part of a Wi-Fi to 3G failover
        exceptionWhitelist.add(UnknownHostException.class);
        // retry-this, since it may happens as part of a Wi-Fi to 3G failover
        exceptionWhitelist.add(SocketException.class);

        // never retry timeouts
        exceptionBlacklist.add(InterruptedIOException.class);
        // never retry SSL handshake failures
        exceptionBlacklist.add(SSLException.class);
    }

    private final String baseUrl;
    private final String signalrClientId;
    private final Gson gson;
    private final Logger logger;
    private CloseableHttpClient http;
    private TokenManager tokenManager;
    private String accessToken;
    private int maxRetries = 30;

    public ServerCaller(TokenManager tokenManager, LoggerFactory loggerFactory) throws MalformedURLException {
        this(tokenManager, null, null, loggerFactory);
    }

    public ServerCaller(TokenManager tokenManager, String signalrClientId, LoggerFactory loggerFactory) {
        this(tokenManager, null, signalrClientId, null, null, loggerFactory);
    }

    public ServerCaller(TokenManager tokenManager, String baseUrl, String signalrClientId, LoggerFactory loggerFactory) {
        this(tokenManager, baseUrl, signalrClientId, null, null, loggerFactory);
    }

    public ServerCaller(TokenManager tokenManager, String baseUrl, String signalrClientId, CloseableHttpClient http, HttpHost proxy, LoggerFactory loggerFactory) {
        logger = loggerFactory.createLogger("ServerCaller");
        this.baseUrl = baseUrl;
        this.tokenManager = tokenManager;
        this.signalrClientId = signalrClientId;
        this.http = http;

        this.gson = new GsonBuilder().create();

        ArrayList<Header> headers = new ArrayList<>();
        if (signalrClientId != null) {
            headers.add(new BasicHeader("X-FBox-ClientId", signalrClientId));
        }

        if (http == null) {
            CacheConfig cc = CacheConfig.DEFAULT;
            ExponentialBackOffSchedulingStrategy ebo = new ExponentialBackOffSchedulingStrategy(cc);

            HttpClientBuilder httpBuilder = HttpClients.custom()
                    .setDefaultHeaders(headers).setServiceUnavailableRetryStrategy(new ServiceUnavailableRetryStrategy() {
                        @Override
                        public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
                            if (executionCount > maxRetries)
                                return false;
                            if (response == null) {
                                return false;
                            }
                            if (response.getStatusLine()== null) {
                                return false;
                            }
                            switch (response.getStatusLine().getStatusCode()) {
//                                case 401:
//                                    Object otm = context.getAttribute("ServerCaller");
//                                    if (otm == null)
//                                        return false;
//
//                                    ServerCaller tm = (ServerCaller) otm;
//                                    try {
//                                        tm.accessToken = tm.tokenManager.getOrUpdateToken(tm.accessToken);
//                                    } catch (IOException e) {
//                                        System.err.printf("Error fetching token in service unavailable retry logic %s.\n", e.toString());
//                                        return true;
//                                    }
//
//                                    HttpClientContext clientContext = HttpClientContext.adapt(context);
//                                    clientContext.getRequest().setHeader("Authorization", "Bearer " + tm.accessToken);
//                                    return true;

                                case HttpStatus.SC_BAD_GATEWAY:
                                case HttpStatus.SC_SERVICE_UNAVAILABLE:
                                    return true;
                                default:
                                    return false;
                            }
                        }

                        @Override
                        public long getRetryInterval() {
                            return 1000;
                        }
                    })
                    .setRetryHandler((exception, executionCount, context) -> {
                        if (exception != null) {
                            System.err.printf("Retrying due to %s.\n", exception.toString());
                        }
                        if (executionCount > maxRetries) {
                            // Do not retry if over max retry count
                            System.err.printf("Maxmimum retry count reached.\n");
                            return false;
                        } else if (isInList(exceptionBlacklist, exception)) {
                            // immediately cancel retry if the error is blacklisted
                            return false;
                        } else if (isInList(exceptionWhitelist, exception)) {
                            // immediately retry if error is whitelisted
                        }

                        HttpClientContext clientContext = HttpClientContext.adapt(context);
                        if (clientContext == null) {
                            return true;
                        }

                        HttpRequest request = clientContext.getRequest();
                        if (request == null) {
                            return true;
                        }

                        HttpResponse response = clientContext.getResponse();
                        if (response == null) {
                            return true;
                        }
                        StatusLine statusLine = response.getStatusLine();

                        if (statusLine != null && statusLine.getStatusCode() == 401) {
                            try {
                                System.err.println("ServerCaller: try get another token for " + request.toString());
                                this.accessToken = this.tokenManager.getOrUpdateToken(this.accessToken);
                                request.setHeader("Authorization", "Bearer " + this.accessToken);
                                return true;
                            } catch (IOException e) {
                                System.err.printf("Error fetching token in retry logic %s.\n", e.toString());
                            }
                        } else {

                        }

                        try {
                            Thread.sleep(RETRY_SLEEP_TIME_MILLIS);
                        } catch (InterruptedException e) {
                        }
                        return true;
                    });
            if (proxy != null)
                httpBuilder = httpBuilder.setProxy(proxy);
            this.http = httpBuilder.build();
        }
    }

    protected boolean isInList(ArrayList<Class<?>> list, Throwable error) {
        Iterator<Class<?>> itr = list.iterator();
        while (itr.hasNext()) {
            if (itr.next().isInstance(error)) {
                return true;
            }
        }
        return false;
    }

    public <T> T executeGet(String url, Class<T> responseType) throws IOException {
        HttpGet request;
        request = new HttpGet(baseUrl + url);
        return executeCore(request, responseType);
    }

    public <T> T executePost(String url, Class<T> responseType) throws IOException {
        return executePost(url, null, responseType);
    }

    public <T> T executePost(String url, Object entity, Class<T> responseType) throws IOException {
        HttpPost request;
        request = new HttpPost(baseUrl + url);
        if (entity != null) {
            String str = gson.toJson(entity);
            request.setEntity(new StringEntity(str, ContentType.APPLICATION_JSON));
        }
        return executeCore(request, responseType);
    }

    public <T> T executePost(String url, HttpEntity body, Class<T> responseType) throws IOException {
        HttpPost request;
        request = new HttpPost(baseUrl + url);
        if (body != null)
            request.setEntity(body);
        return executeCore(request, responseType);
    }

    public <T> T executePut(String url, HttpEntity body, Class<T> responseType) throws IOException {
        HttpPut request;
        request = new HttpPut(baseUrl + url);
        request.setEntity(body);
        return executeCore(request, responseType);
    }

    public <T> T executeDelete(String url, Class<T> responseType) throws IOException {
        HttpDelete request;
        request = new HttpDelete(baseUrl + url);
        return executeCore(request, responseType);
    }

    private <T> T executeCore(HttpUriRequest request, Class<T> responseType) throws IOException {
        if (this.accessToken == null) {
            this.accessToken = this.tokenManager.getOrUpdateToken(this.accessToken);
        }
        request.setHeader("Authorization", "Bearer " + this.accessToken);
        request.addHeader("Accept", "application/json; */*");
        for (; ; ) {
            String method = request.getMethod();
            URI uri = request.getURI();
         //   this.logger.logTrace(String.format("Executing request %s %s", method, uri));
            CloseableHttpResponse response = null;
            BasicHttpContext ctx = new BasicHttpContext();
            ctx.setAttribute("ServerCaller", this);
            try {
                response = this.http.execute(request, ctx);
            } catch (IOException e) {
                System.err.printf("Request failed with error: %s.\n", e.toString());
                throw e;
            }
            try {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine == null)
                    throw new HttpResponseException(0, "Null status line.");
                int statusCode = statusLine.getStatusCode();
              //  this.logger.logTrace(String.format("Executed request %s %s with code %d", method, uri, statusCode));
                if (statusCode == 401) {
//                    throw new HttpResponseException(401, "Unauthorized request");
                    this.logger.logTrace("ServerCaller: 401 from " + request.getURI().toString());
                    this.accessToken = this.tokenManager.getOrUpdateToken(this.accessToken);
                    request.setHeader("Authorization", "Bearer " + this.accessToken);
                    continue;
                } else if (statusCode >= 300) {
                    String exmsg = statusLine.getReasonPhrase();
                    Header errCodeHeader = response.getFirstHeader("X-FBox-Code");
                    int errCode = 0;
                    if (errCodeHeader != null) {
                        errCode = Integer.parseInt(errCodeHeader.getValue());
                        exmsg += " code=" + errCode;
                    }
                    throw new BoxServerResponseException(statusCode, exmsg, errCode);
                }
                HttpEntity body = response.getEntity();
                if (body != null && responseType != null) {
                    String str = EntityUtils.toString(body);
                    try {
                        return gson.fromJson(str, responseType);
                    } catch (JsonSyntaxException ex) {
                        if (responseType.isAssignableFrom(String.class))
                            return (T) str;
                        else
                            throw new IllegalArgumentException("Response cannot be parsed to " + responseType.toString() + " contentType is " + body.getContentType());
                    }
                }
//                this.logger.logTrace(String.format("Request %s %s returned with empty body.", method, uri));
                return null;
            } finally {
                response.close();
            }
        }
    }
}
