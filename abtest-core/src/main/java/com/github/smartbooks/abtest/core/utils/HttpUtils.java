package com.github.smartbooks.abtest.core.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class HttpUtils {

    public static final Logger logger = LogManager.getLogger(HttpUtils.class);
    public static final String CHARSET = "UTF-8";
    public static final String CONTENT_TYPE = "application/json";
    public static final int HTTP_CONNECTION_TIMEOUT = 500;
    public static final int HTTP_SOCKET_TIMEOUT = 1500;
    public static RequestConfig config;

    static {
        config = RequestConfig
                .custom()
                .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                .setConnectTimeout(HTTP_CONNECTION_TIMEOUT)
                .build();
    }

    public static String sendPost(String url, String data, String charset, String contentType) {

        HttpClient httpClient = HttpClients.createDefault();

        HttpResponse httpresponse;

        try {

            StringEntity entity = new StringEntity(data, charset);

            entity.setContentType(contentType);

            HttpPost httpPost = new HttpPost(url);

            httpPost.setConfig(config);

            httpPost.setEntity(entity);

            httpresponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpresponse.getEntity();

            return EntityUtils.toString(httpEntity, charset);

        } catch (IOException e) {
            logger.error(e);
        }

        return null;
    }

}
