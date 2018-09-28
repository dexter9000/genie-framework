package com.genie.security.util;

import com.alibaba.fastjson.util.IOUtils;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    /**
     * 发送Get请求
     *
     * @param url       请求地址
     * @param headerMap 请求头传递的参数
     * @return 请求成功返回响应字符串, 否则返回null
     */
    public static Map<String, String> sendGet(String url, Map<String, String> headerMap) {
        CloseableHttpResponse resp = null;
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            if (null != headerMap && headerMap.size() > 0) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    get.setHeader(entry.getKey(), entry.getValue());
                }
            }

            resp = client.execute(get);
            Map<String, String> respMap = new HashMap<>(2);
            respMap.put("statusCode", "" + resp.getStatusLine().getStatusCode());
            respMap.put("content", EntityUtils.toString(resp.getEntity(), "utf-8"));
            return respMap;
        } catch (IOException e) {
            log.info("sendGet IOException", e);
        } finally {
            IOUtils.close(client);
            IOUtils.close(resp);
        }
        return null;
    }

    public static String sendPost(String url, String content) {
        CloseableHttpClient client = null;
        CloseableHttpResponse resp = null;
        try {
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-type", "application/json");
            StringEntity entity = new StringEntity(content, ContentType.create("application/json", "UTF-8"));
            post.setEntity(entity);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(300000)
                    .setConnectionRequestTimeout(60000)
                    .setSocketTimeout(300000)
                    .build();
            post.setConfig(requestConfig);

            resp = client.execute(post);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                String str = EntityUtils.toString(resp.getEntity(), "utf-8");
                return str;
            }
        } catch (UnsupportedCharsetException | ParseException | IOException e) {
            log.info("sendPost error!", e);
        } finally {
            IOUtils.close(client);
            IOUtils.close(resp);
        }
        return null;
    }
}
