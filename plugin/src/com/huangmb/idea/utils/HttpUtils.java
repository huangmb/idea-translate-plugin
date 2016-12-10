package com.huangmb.idea.utils;

import com.huangmb.idea.bean.BaseTask;
import com.huangmb.idea.bean.Method;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by huangmb on 2016/12/10.
 */
public class HttpUtils {
    private static HttpClient client;
    private static RequestConfig config;

    static {
        config = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        client = HttpClients.createDefault();
    }

    public static String query(BaseTask task) throws IOException {
        if (task.getMethod() == Method.GET){
            return get(task);
        }
        return null;
    }

    public static String get(BaseTask task) throws IOException {
        URI uri = createGetURI(task);
        if (uri == null){
            throw new IOException("invalid uri");
        }
        HttpGet httpGet = new HttpGet(createGetURI(task));
        httpGet.setConfig(config);
        HttpResponse response = client.execute(httpGet);
        if (task.isCancel()){
            try {
                client.getConnectionManager().shutdown();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        if (response == null){
            return null;
        }
        int code = response.getStatusLine().getStatusCode();
        if (code >= 200 && code < 300){
            return EntityUtils.toString(response.getEntity());
        }
        return null;
    }

    private static URI createGetURI(BaseTask task) {
        try {
            URIBuilder builder = new URIBuilder(task.getUrl());
            for (Map.Entry<String,String> entry : task.getParams().entrySet()){
                builder.addParameter(entry.getKey(),entry.getValue());
            }
            return builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
