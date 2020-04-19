package com.rjx.network.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Jianxiong Rao
 * @date 2020-04-15
 * @description : 添加公共返回打印
 */
public class CommonResponseInterceptor implements Interceptor {
    private static final String TAG = "ResponseInterceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        long requestTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        Log.d(TAG,"requestTime = "+ (System.currentTimeMillis() - requestTime));
        return response;
    }
}
