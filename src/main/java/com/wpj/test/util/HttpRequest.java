package com.wpj.test.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 网络请求封装
 *
 * @author wangpejian
 * @date 19-6-10 下午5:03
 */
@Slf4j
public class HttpRequest {

    public static String get(String url) {
        String res = "";

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            assert response.body() != null;
            res = response.body().string();
            log.info("请求结果： {}", res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

}
