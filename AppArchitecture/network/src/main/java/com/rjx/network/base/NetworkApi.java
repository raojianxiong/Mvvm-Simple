package com.rjx.network.base;

import com.rjx.network.environment.EnvironmentActivity;
import com.rjx.network.environment.IEnvironment;
import com.rjx.network.errorhandler.HttpErrorHandle;
import com.rjx.network.interceptor.CommonRequestInterceptor;
import com.rjx.network.interceptor.CommonResponseInterceptor;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Jianxiong Rao
 * @date 2020-04-15
 */
public abstract class NetworkApi implements IEnvironment {
    private static INetworkRequiredInfo iNetworkRequiredInfo;
    private static HashMap<String, Retrofit> retrofitHashMap = new HashMap<>();
    private String mBaseUrl;
    private OkHttpClient mOkHttpClient;
    private static boolean mIsNormal = true;

    public NetworkApi() {
        if (mIsNormal) {
            mBaseUrl = getNormal();
        } else {
            mBaseUrl = getTest();
        }
    }

    public static void init(INetworkRequiredInfo networkRequiredInfo) {
        iNetworkRequiredInfo = networkRequiredInfo;
        mIsNormal = EnvironmentActivity.isOfficialEnvironment(networkRequiredInfo.getApplicationContext());
    }

    protected Retrofit getRetrofit(Class service) {
        if(retrofitHashMap.get(mBaseUrl + service.getName()) != null){
            return  retrofitHashMap.get(mBaseUrl + service.getName());
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mBaseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofitHashMap.put(mBaseUrl + service.getName(),retrofit);
        return  retrofit;

    }
    private OkHttpClient getOkHttpClient(){
        if(mOkHttpClient == null){
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            if(getInterceptor() != null){
                okHttpClientBuilder.addInterceptor(getInterceptor());
            }
            okHttpClientBuilder.addInterceptor(new CommonRequestInterceptor(iNetworkRequiredInfo));
            okHttpClientBuilder.addInterceptor(new CommonResponseInterceptor());
            if(iNetworkRequiredInfo != null && iNetworkRequiredInfo.isDebug()){
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            }
            mOkHttpClient = okHttpClientBuilder.build();
        }
        return  mOkHttpClient;
    }

    public <T> ObservableTransformer<T,T> applySchedulers(final Observer<T> observer){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable<T> observable = (Observable<T>)  upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(getAppErrorHandler())
                        .onErrorResumeNext(new HttpErrorHandle<T>());
                observable.subscribe(observer);
                return observable;
            }
        };
    }


    public abstract Interceptor getInterceptor();
    protected abstract <T>Function<T,T> getAppErrorHandler();

}
