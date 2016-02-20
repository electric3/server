package com.electric3.server.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.logging.Logger;

public class HttpSender {
    private static Logger log = Logger.getLogger(HttpSender.class.getName());

    public static HttpSender getInstance() {
        return HTTP_SENDER;
    }

    private static final HttpSender HTTP_SENDER = new HttpSender();

    public int sendPost(String url, String authHeaderValue, String data) throws Exception {

        int responseCode;
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("Authorization", authHeaderValue);
        post.setHeader("Content-type", "application/json; charset=UTF-8");

        post.setEntity(new StringEntity(data, "UTF-8"));

        HttpResponse response = client.execute(post);
        responseCode = response.getStatusLine().getStatusCode();
        log.info("Response Code : " + responseCode);
        log.info("Reason : " + response.getStatusLine().getReasonPhrase());

        if (responseCode != 400) {
            throw new Exception("Response code is not success");
        }

        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        log.info(responseString);
        return responseCode;
    }

    public String sendGet(String url, String authHeaderValue) throws Exception {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);

        // add header
        get.setHeader("Authorization", authHeaderValue);
        get.setHeader("Content-type", "application/json; charset=UTF-8");

        HttpResponse response = client.execute(get);
        int responseCode = response.getStatusLine().getStatusCode();
        log.info("Response Code : " + responseCode);
        log.info("Reason : " + response.getStatusLine().getReasonPhrase());

        if (responseCode >= 400) {
            throw new Exception("Response code is not success");
        }

        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return responseString;
    }
}
