package com.jt;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class TestHttpClient {
    /**
     * 思路:
     * 1.创建工具api对象
     * 2.定义远程url地址
     * 3.get/post定义请求类型对象
     * 4.发起请求,获取相应结果
     * 5.从响应对象中获取数据
     */
    @Test
    public void testGet() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url ="https://www.huya.com/g/lol";
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode()==200){
            //从中获取响应数据
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
        }else {
            System.out.println("请求失败,,请稍后再试");
        }
    }
}
