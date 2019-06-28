package com.zlkj.admin.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpClientUtil {
    public static String doPost(String url, Map<String, String> map, String charset) {
        //1.创建CloseableHttpClient对象。
        CloseableHttpClient client = HttpClients.createDefault();
        //2.创建请求方法的实例，并指定请求URL。如果需要发送GET请求，创建HttpGet对象；如果需要发送POST请求，创建HttpPost对象。
        HttpPost httpPost;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpPost = new HttpPost(url);
            //整理参数
            List<NameValuePair> list = new ArrayList<>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                //3.如果需要发送请求参数，可可调用setEntity(HttpEntity entity)方法来设置请求参数。setParams方法已过时（4.4.1版本）
                httpPost.setEntity(entity);
            }
            //4.调用HttpGet、HttpPost对象的setHeader(String name, String value)方法设置header信息，

            // 或者调用setHeaders(Header[] headers)设置一组header信息。

            //5.调用CloseableHttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个CloseableHttpResponse

            response = client.execute(httpPost);

            if (response != null) {

                //6.调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器的响应内容。
                // 程序可通过该对象获取服务器的响应内容；调用CloseableHttpResponse的getAllHeaders()、getHeaders(String name)等方法可获取服务器的响应头。
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
            //7. 释放连接。无论执行方法是否成功，都必须释放连接
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}