package com.rjx.base.model;

import android.text.TextUtils;

import androidx.annotation.CallSuper;

import com.rjx.base.preference.BasicDataPreferenceUtil;
import com.rjx.base.util.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Jianxiong Rao
 * @date 2020-04-16
 */
public abstract class MvvmBaseModel<F, T extends ArrayList> implements MvvmNetworkObserver<F> {
    private CompositeDisposable compositeDisposable;
    protected ReferenceQueue<IBaseModelListener> mReferenceQueue;
    protected ConcurrentLinkedQueue<WeakReference<IBaseModelListener>> mWeakListenerArrayList;
    private BaseCachedData<F> mData;
    protected boolean isRefresh = true;
    protected int pageNumber = 0;
    private String mCachedPreferenceKey;
    private String mApkPredefinedData;
    private boolean mIsPaging;
    private Class<F> clazz;

    public MvvmBaseModel(Class<F> clazz, boolean isPaging, String cachePreferenceKey, String apkPredefinedData, int... initPageNumber) {
        this.mIsPaging = isPaging;
        this.clazz = clazz;
        if (initPageNumber != null && initPageNumber.length == 1) {
            this.pageNumber = initPageNumber[0];
        }
        this.mCachedPreferenceKey = cachePreferenceKey;
        this.mApkPredefinedData = apkPredefinedData;
        mReferenceQueue = new ReferenceQueue<>();
        mWeakListenerArrayList = new ConcurrentLinkedQueue<>();
        if (mCachedPreferenceKey != null) {
            mData = new BaseCachedData<F>();
        }
    }

    /**
     * 是否需要分页
     * @return
     */
    public boolean isPaging() {
        return mIsPaging;
    }

    /**
     * 注册监听
     *
     * @param listener
     */
    public void register(IBaseModelListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            //每次注册的时候清理已经被系统回收的对象
            Reference<? extends IBaseModelListener> releaseListener = null;
            while ((releaseListener = mReferenceQueue.poll()) != null) {
                mWeakListenerArrayList.remove(releaseListener);
            }
            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                IBaseModelListener listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    return;
                }
            }
            WeakReference<IBaseModelListener> weakListener = new WeakReference<>(listener, mReferenceQueue);
            mWeakListenerArrayList.add(weakListener);
        }
    }

    /**
     * 取消监听
     *
     * @param listener
     */
    public void unRegister(IBaseModelListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                IBaseModelListener listenerItem = weakListener.get();
                if (listener == listenerItem) {
                    mWeakListenerArrayList.remove(weakListener);
                    break;
                }
            }
        }
    }

    /**
     * 因为渠道处在App的首页，为了保证app打开的时候由于网络慢或者异常tablayout不为空的情况
     * 所以app对渠道数据进行了预制
     * 加载完后会立即轻轻网络数据，同时缓存在本地，今后app打开都会从preference读取，而不再apk预制数据，由于渠道数据变化没有那么快，在app
     * 第二次打开的时候会生效，并且是一天请求一次
     *
     * @param data
     */
    protected void saveDataToPreference(F data) {
        if (data != null) {
            mData.data = data;
            mData.updateTimeInMills = System.currentTimeMillis();
            BasicDataPreferenceUtil.getInstance().setString(mCachedPreferenceKey, GsonUtils.toJson(mData));
        }
    }

    public abstract void refresh();

    protected abstract void load();



    /**
     * 是否更新数据 ， 可以在这里设计策略 一天一次？ 一月一次
     * 默认每次请求都更新
     */
    protected boolean isNeedToUpdate() {
        return true;
    }

    /**
     * @CallSuper注解主要是用来强调在覆盖父类方法的时候，需要实现父类的方法， 及时调用对应的super.**方法，
     * 当使用 @CallSuper 修饰了某个方法，如果子类覆盖父类该方法后没有实现对父类方法的调用就会报错
     */
    @CallSuper
    public void cancel() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void addDisposable(Disposable d) {
        if (d == null) {
            return;
        }
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(d);
    }

    public void getCachedDataAndLoad() {
        if (mCachedPreferenceKey != null) {
            String saveDataString = BasicDataPreferenceUtil.getInstance().getString(mCachedPreferenceKey);
            if (!TextUtils.isEmpty(saveDataString)) {
                try {
                    F saveData = GsonUtils.fromLocalJson(new JSONObject(saveDataString).getString("data"), clazz);
                    if (saveData != null) {
                        onSuccess(saveData, true);
                        if (isNeedToUpdate()) {
                            load();
                        }
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (mApkPredefinedData != null) {
            F saveData = GsonUtils.fromLocalJson(mApkPredefinedData, clazz);
            if (saveData != null) {
                onSuccess(saveData, true);
            }
        }
        load();
    }

    /**
     * 发消息给UI线程
     */
    protected void loadSuccess(F networkResponseBean, T data, boolean isFromCache) {
        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                IBaseModelListener listenerItem = weakListener.get();
                if (listenerItem != null) {
                    if (isPaging()) {
                        listenerItem.onLoadFinish(this, data, isFromCache ? new PagingResult(false, true, true) : new PagingResult(data.isEmpty(), isRefresh, data.size() > 0));
                        if (mCachedPreferenceKey != null && isRefresh && !isFromCache) {
                            //不是拿缓存
                            saveDataToPreference(networkResponseBean);
                        }
                        if (!isFromCache) {
                            pageNumber++;
                        }
                    } else {
                        listenerItem.onLoadFinish(this, data);
                        if(mCachedPreferenceKey != null){
                            saveDataToPreference(networkResponseBean);
                        }
                    }
                }
            }
        }
    }

    protected void loadFail(String errorMessage){
        synchronized (this){
            for (WeakReference<IBaseModelListener> weakListener:mWeakListenerArrayList){
                if(weakListener.get() instanceof  IBaseModelListener){
                    IBaseModelListener listenerItem = weakListener.get();
                    if(listenerItem != null){
                        if(isPaging()){
                            listenerItem.onLoadFail(this,errorMessage,new PagingResult(true,isRefresh,false));
                        }else{
                            listenerItem.onLoadFail(this,errorMessage);
                        }
                    }
                }
            }
        }
    }
}
