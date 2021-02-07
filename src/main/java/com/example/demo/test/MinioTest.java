package com.example.demo.test;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lichang
 * @version 1.0.0
 * @program
 * @description
 * @createDate 2021-01-31 14:08
 */
public class MinioTest
{
    public static void main(String[] args) throws Throwable
    {
       /* MinioClient minioClient = MinioClient.builder()
                .endpoint("http://minio.uindata.com:9000")
                .credentials("minioadmin", "minioadmin")
                .build();*/


        /*MinioClient minioClient = MinioClient.builder()
                        .endpoint("http://minio.uindata.com:9000")
                        .credentials("minioadmin", "minioadmin")
                        .build();

        //minioClient.setBucketPolicy();

        GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket("package-demo")
                .object("刻盘内容/")
                .expiry(2, TimeUnit.HOURS)
                .build();

        String presignedObjectUrl = minioClient.getPresignedObjectUrl(urlArgs);
        System.out.println(presignedObjectUrl);*/


        //HttpUtil.post("http://minio.uindata.com:9000/minio/webrpc","")


     /*   Map<String, String> downLoadHeaders = new HashMap<>();
        downLoadHeaders.put("Content-Type", "application/json");
        downLoadHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");

        JsonRpcHttpClient downLoadClient = new JsonRpcHttpClient(new URL(" http://192.168.1.146:9000/minio/zip"+"?token="+token));
        JSONObject downLoadParams = new JSONObject();
        downLoadParams.put("bucketName","package-demo");
        downLoadParams.put("objects","[\"刻盘内容/\"]");
        downLoadParams.put("prefix","");


        Object invoke1 = downLoadClient.invoke("", downLoadParams, Object.class, downLoadHeaders);


        System.out.println(invoke1);
*/
        //System.out.println(split[1]);






        String downLoadToken = getDownLoadToken("http://192.168.1.146:9000", "minioadmin", "minioadmin");

        System.out.println(downLoadToken);


        final String CONTENT_TYPE_TEXT_JSON = "text/json";
        DefaultHttpClient client = new DefaultHttpClient(
                new PoolingClientConnectionManager());

        String url = "http://192.168.1.146:9000/minio/zip"+"?token="+downLoadToken;
        String js = "{\n" +
                "\t\"bucketName\":\"package-demo\",\n" +
                "\t\"objects\":[\"刻盘内容/\"],\n" +
                "\t\"prefix\":\"\"\n" +
                "}";

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "text/plan;charset=UTF-8");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");


        StringEntity se = new StringEntity(js);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);

        httpPost.setEntity(se);

        CloseableHttpResponse response2 = client.execute(httpPost);
        InputStream content = response2.getEntity().getContent();

        ///Users/thefei/Desktop
        FileUtils.copyInputStreamToFile(content,new File("/Users/thefei/Desktop"+"/a.zip"));


    }

    /**
     *
     * @param url http://192.168.1.146:9000
     * @param username minioadmin
     * @param password minioadmin
     * @return
     * @throws Throwable
     */
    public static String getDownLoadToken(String url,String username,String password) throws Throwable
    {
        url = url+"/minio/webrpc";

        JSONObject loginParams = new JSONObject();
        loginParams.put("username", username);
        loginParams.put("password", password);

        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("Content-Type", "application/json");
        extraHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        JsonRpcHttpClient client = new JsonRpcHttpClient(new URL(url));
        String invoke = client.invoke("Web.Login", loginParams, Object.class,extraHeaders).toString();


        extraHeaders.put("Authorization","Bearer " + subString(invoke, "=", ","));
        invoke = client.invoke("Web.CreateURLToken", loginParams, Object.class, extraHeaders).toString();
        return subString(invoke, "=", ",");
    }


    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     *
     * @param str      string
     * @param strStart strStart
     * @param strEnd   strEnd
     * @return
     */
    public static String subString(String str, String strStart, String strEnd)
    {
        //找出指定的2个字符在 该字符串里面的位置
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);
        //index 为负数 即表示该字符串中 没有该字符
        if (strStartIndex < 0)
        {
            return null;
        }
        if (strEndIndex < 0)
        {
            return null;
        }
        //开始截取
        return str.substring(strStartIndex, strEndIndex).substring(strStart.length());
    }
}
