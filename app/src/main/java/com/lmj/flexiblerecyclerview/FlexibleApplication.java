package com.lmj.flexiblerecyclerview;

import android.app.Application;
import android.os.Handler;

/**
 * author: lmj
 * date  : 2018/4/2.
 */

public class FlexibleApplication extends Application {
    private static long mMainTreadId;
    private static Handler mHandler;
    private static FlexibleApplication mContext = null;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        mContext = this;
        mMainTreadId = android.os.Process.myPid();
        mHandler = new Handler();
    }

    /*
   * 获取全局Application
   */
    public static FlexibleApplication getContext() {
        return mContext;
    }

    public static long getMainTreadId(){
        return mMainTreadId;
    }

    public static Handler getHandler() {
        return mHandler;
    }

}
