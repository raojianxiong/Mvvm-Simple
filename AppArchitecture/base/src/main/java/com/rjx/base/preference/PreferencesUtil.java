package com.rjx.base.preference;

import android.app.Application;

/**
 * @author Jianxiong Rao
 */
public class PreferencesUtil extends BasePreferences {
    private static PreferencesUtil sInstance;

    public static PreferencesUtil getInstance() {
        if (sInstance == null) {
            synchronized (PreferencesUtil.class) {
                if (sInstance == null) {
                    sInstance = new PreferencesUtil();
                }
            }
        }
        return sInstance;
    }

    public static void init(Application application) {
        sApplication = application;
    }


    @Override
    public String getSPFileName() {
        return "common_data";
    }
}
