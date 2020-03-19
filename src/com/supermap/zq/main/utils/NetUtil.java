package com.supermap.zq.main.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NetUtil {

    private static Logger _log = LoggerFactory.getLogger(NetUtil.class);

    public static String get(String url) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        // 设置连接超时
        final RequestConfig timeParams = RequestConfig.custom().setConnectTimeout(5000).build();
        httpGet.setConfig(timeParams);
        HttpResponse response = client.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        System.out.println(statusCode == HttpStatus.SC_OK);
        // 获取结果
        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String content = null;
        while ((content = br.readLine()) != null) {
            result.append(content);
        }
        br.close();
        return result.toString();
    }

    public static String post(String url) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        // 请求参数
        /*
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("k", "abc"));
        post.setEntity(new UrlEncodedFormEntity(params)); nb
        */
        // 设置连接超时
        final RequestConfig timeParams = RequestConfig.custom().setConnectTimeout(5000).build();
        post.setConfig(timeParams);
        HttpResponse response = client.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        _log.info(statusCode + "");
        // 获取结果
        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String content = null;
        while ((content = br.readLine()) != null) {
            result.append(content);
        }
        br.close();
        return result.toString();
    }
}
