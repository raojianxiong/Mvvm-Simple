package com.rjx.base.util;

import android.content.SharedPreferences;
import android.os.Build;

/**
 * @author Jianxiong Rao
 */
public class EditorUtils {
    public static void fastCommit(SharedPreferences.Editor editor){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1){
            editor.apply();
        }else{
            editor.commit();
        }
    }
}
