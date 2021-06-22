package com.qbk.string;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * url参数拼接
 */
public class StringUrlParam {
    public static void main(String[] args) {
        String url = "http://ip:port/get";
        String url2 = "http://ip:port/post";
        Map<String, Object> params = new HashMap<>();
        params.put("admin","123");
        params.put("user","123");

        //方式一 使用httpclient的jar
        List<NameValuePair> valuePairs = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                valuePairs.add(new BasicNameValuePair(entry.getKey(),
                        entry.getValue().toString()));
            }
        }
        url = url + "?" + URLEncodedUtils.format(valuePairs, StandardCharsets.UTF_8);
        System.out.println(url);


        //方式二 使用jdk
        StringJoiner stringJoiner = new StringJoiner("&");
        params.forEach((k,v) ->{
            String strJoin = null;
            strJoin = String.join("=",k, String.valueOf(v));
            stringJoiner.add(strJoin);
        });
        url2 = url2.concat("?").concat(stringJoiner.toString());
        System.out.println(url2);
    }
}
