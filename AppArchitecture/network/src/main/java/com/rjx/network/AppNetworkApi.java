package com.rjx.network;

import com.rjx.network.base.NetworkApi;
import com.rjx.network.beans.BaseResponse;
import com.rjx.network.errorhandler.ExceptionHandle;
import com.rjx.network.utils.Utils;

import java.io.IOException;

import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AppNetworkApi extends NetworkApi {
    private static  volatile  AppNetworkApi sIntance;
    public static AppNetworkApi getInstance(){
        if(sIntance == null){
            synchronized (AppNetworkApi.class){
                if(sIntance == null){
                    sIntance = new AppNetworkApi();
                }
            }
        }
        return  sIntance;
    }

    public static <T> T getService(Class<T> service){
        return getInstance().getRetrofit(service).create(service);
    }
    @Override
    public Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String timeStr = Utils.getTimeStr();
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("Source","source");
                builder.addHeader("Authorization",Utils.getAuthorization(timeStr));
                builder.addHeader("Date",timeStr);
                return chain.proceed(builder.build());
            }
        };
    }

    @Override
    protected <T> Function<T, T> getAppErrorHandler() {
        return new Function<T, T>() {
            @Override
            public T apply(T response) throws Exception {
                //response中code不会是0
                if(response instanceof BaseResponse &&  ((BaseResponse) response).showapiResCode != 0){
                    ExceptionHandle.ServerException exception = new ExceptionHandle.ServerException();
                    exception.code = ((BaseResponse) response).showapiResCode;
                    exception.message = ((BaseResponse) response).showapiResError != null ?  ((BaseResponse) response).showapiResError : "";
                    throw  exception;
                }
                return response;
            }
        };
    }

    @Override
    public String getNormal() {
        return "http://service-o5ikp40z-1255468759.ap-shanghai.apigateway.myqcloud.com/";
    }

    @Override
    public String getTest() {
        return "http://service-o5ikp40z-1255468759.ap-shanghai.apigateway.myqcloud.com/";
    }
}
