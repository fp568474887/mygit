package com.icss.mvp.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.druid.support.json.JSONUtils;

/**
 * Created by Ray on 2018/4/20.
 */
public class HttpExecuteUtils {

    // private static final CloseableHttpClient httpClient;
    //
    // static {
    // RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
    // httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    // }

    public static String httpGet(String url, Map<String, Object> parameters) {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        String result = null;

        CloseableHttpResponse response = null;
        try {
            String urlParameters = "";
            // 拼接参数
            if (parameters != null && !parameters.isEmpty()) {
                List<NameValuePair> list = new ArrayList<>();
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                urlParameters = EntityUtils.toString(new UrlEncodedFormEntity(list, Consts.UTF_8));
            }

            HttpGet httpGet = new HttpGet(url + "?" + urlParameters);

            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            System.out.println("========HttpResponseProxy：========" + statusCode);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                System.out.println("========接口返回=======" + result);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static String httpPost(String url, Map<String, Object> parameters) {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        String result = null;

        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");

            // // 拼接参数
            // if (parameters != null && !parameters.isEmpty()) {
            // List<NameValuePair> list = new ArrayList<>();
            // for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            // list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            // }
            // // list.add(new BasicNameValuePair("参数队列头部", 调用参数));
            // httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            // }

            StringEntity stringEntity = new StringEntity(JSONUtils.toJSONString(parameters));
            stringEntity.setContentType("text/json");
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(stringEntity);

            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            System.out.println("========HttpResponseProxy：========" + statusCode);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                System.out.println("========接口返回=======" + result);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
