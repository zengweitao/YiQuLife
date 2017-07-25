package com.quanmai.yiqu.common.util;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by zhanjinj on 16/4/27.
 */
public class MyActivityManager {
    private static MyActivityManager sInstance = new MyActivityManager();
    private WeakReference<Activity> mActivityWeakReference;

    private MyActivityManager() {

    }

    public static MyActivityManager getInstance() {
        return sInstance;
    }

    public void setCurrentActivity(Activity activity) {
        if (activity != null) {
            mActivityWeakReference = new WeakReference<Activity>(activity);
        }
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (mActivityWeakReference != null) {
            currentActivity = mActivityWeakReference.get();
        }
        return currentActivity;
    }

    public String getCurrentActivityName() {
        Activity currentActivity = null;
        if (mActivityWeakReference != null) {
            currentActivity = mActivityWeakReference.get();
        }
        return currentActivity.toString().trim();
    }

}
