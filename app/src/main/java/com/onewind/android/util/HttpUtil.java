package com.onewind.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by bin on 2017/11/4.
 */

public class HttpUtil {

    /**
     * 创建一个网络连接，回调方法
     * @param url
     * @param callback
     */
    public static void sendOkHttpRequest(String url, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
