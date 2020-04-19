package com.rjx.network.base;

import android.app.Application;

/**
 * @author Jianxiong Rao
 * @date 2020-04-15
 */
public interface INetworkRequiredInfo {
    String getAppVersionName();
    String getAppVersionCode();
    boolean isDebug();
    Application getApplicationContext();
}
