package com.sewage.springboot.signalr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sewage.springboot.logger.Logger;
import com.sewage.springboot.logger.LoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TokenManager implements Closeable {
    private final CredentialProvider credentialProvider;
    private final Object tokenLock;
    private final Gson gson;
    private final Logger logger;
    private CloseableHttpClient http;
    private final String idSvrUrl;

    public TokenManager(CredentialProvider credentialProvider, LoggerFactory loggerFactory) {
        this(credentialProvider, "https://account.flexem.com/core/", loggerFactory);
    }

    public TokenManager(CredentialProvider credentialProvider, String idSvrUrl, LoggerFactory loggerFactory) {
        this(credentialProvider, idSvrUrl, null, loggerFactory);
    }

    public TokenManager(CredentialProvider credentialProvider, String idSvrUrl, CloseableHttpClient http, LoggerFactory loggerFactory) {
        this.logger = loggerFactory.createLogger("TokenManager");
        this.idSvrUrl = idSvrUrl;
        this.credentialProvider = credentialProvider;
        this.tokenLock = new Object();
        this.http = http;
        if (http == null) {
            this.http = HttpClients.createDefault();
        }
        this.gson = new GsonBuilder().create();
    }

    private String accessToken;

    public String getOrUpdateToken(String currentToken) throws IOException {
        if (currentToken == null) {
            String at = accessToken;
            if (at != null)
                return at;
        }

        synchronized (tokenLock) {
            String at = accessToken;
            if (at != null && at != currentToken)
                return at;

            at = fetchToken();
            accessToken = at;
            return at;
        }
    }

    private String fetchToken() throws IOException {
        ClientCredential clientCredential = credentialProvider.getClientCredential();
        UserCredential userCredential = credentialProvider.getUserCredential();
        // 登录接口，用于获取access_Token，access_Token有效期2小时
        HttpPost request = new HttpPost(idSvrUrl + "connect/token");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("username", userCredential.userName));
        formparams.add(new BasicNameValuePair("password", userCredential.password));
        formparams.add(new BasicNameValuePair("scope", "openid offline_access fbox email profile"));
        formparams.add(new BasicNameValuePair("client_id", clientCredential.clientId));
        formparams.add(new BasicNameValuePair("client_secret", clientCredential.clientSecret));
        formparams.add(new BasicNameValuePair("grant_type", "password"));
        request.setEntity(new UrlEncodedFormEntity(formparams));
        CloseableHttpResponse response = http.execute(request);
        //401
        final RequestConfig params = RequestConfig.custom().setConnectTimeout(8000).setSocketTimeout(8000).build();
        request.setConfig(params);


        try {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            int statusCode = statusLine.getStatusCode();
            // the following status code range mostly caused by incorrect credential.
            if (statusCode >= 400 && statusCode < 500 && statusCode != 429) {
                throw new LoginFailedException(
                        statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }
            if (statusCode >= 300) {
                throw new HttpResponseException(statusCode, statusLine.getReasonPhrase());
            }
            if (entity == null) {
                throw new ClientProtocolException("Response contains no content");
            }
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            Reader reader = new InputStreamReader(entity.getContent(), charset);
            String at;
            try {
                at = (gson.fromJson(reader, TokenResponse.class)).access_token;
            } finally {
                reader.close();
            }
            this.logger.logInformation("Fetched new token.");
            return at;
        } finally {
            response.close();
        }
    }

    @Override
    public void close() throws IOException {
        if (this.http instanceof Closeable) {
            ((Closeable) this.http).close();
        }
    }
}
