package com.rjx.apparchitecture.application;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.kingja.loadsir.core.LoadSir;
import com.rjx.base.loadsir.EmptyCallback;
import com.rjx.base.loadsir.ErrorCallback;
import com.rjx.base.loadsir.LoadingCallback;
import com.rjx.base.preference.PreferencesUtil;
import com.rjx.base.util.ToastUtil;
import com.rjx.network.base.NetworkApi;

/**
 * @author Jianxiong Rao
 */
public class ArchApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesUtil.init(this);
        NetworkApi.init(new NetworkRequestInfo(this));
        ToastUtil.init(this);
        ARouter.init(this);
        ARouter.openDebug();
        ARouter.openLog();
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new EmptyCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }
}
