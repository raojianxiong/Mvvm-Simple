package com.rjx.common.arouter;

import android.text.TextUtils;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author Jianxiong Rao
 */
public class RouteServiceManager {

    public static <T extends IProvider> T provide(Class<T> clazz,String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        IProvider provider = null;
        try{
            provider = (IProvider) ARouter.getInstance().build(path)
                    .navigation();
        }catch (Exception e){
            e.printStackTrace();
        }
        return (T)provider;
    }
}
