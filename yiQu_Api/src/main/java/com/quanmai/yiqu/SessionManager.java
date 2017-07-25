package com.quanmai.yiqu;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class SessionManager implements Observer {
    private Thread mCurrentUpdateThread;
    private static SessionManager mInstance;
    public static final String P_USERNAME = "pref.username";
    // public static final String P_IMGURL = "pref.imgurl";
    // public static final String P_ISMANAGER = "pref.ismanager";
    public static final String P_ISLOGIN = "pref.isLogin";
    public static final String P_ISBIND = "pref.isBind";
    public static final String P_ISFIRST = "pref.isFirst";
    public static final String P_ISFIRST_BIND_REMIND = "pref.isFirst.bind.remind";
    public static final String P_ISFIRST_RESIDENT_BIND_REMIND = "pref.isFirst.resident.bind.remind";
    public static final String P_TOKEN = "pref.token";
    public static final String P_COMMUNITY = "pref.community";
    public static final String P_COMMUNITY_CODE = "pref.community.code";
    public static final String P_COMMUNITY_CITY = "pref.community.city";
    public static final String P_SIGN_IN_DAY = "pref.sign.in.day";

    public static final String P_BOOKING_DETAIL_ADDRESS = "pref.booking.detail.address";
    public static final String P_BOOKING_NAME = "pref.booking.name";
    public static final String P_BOOKING_PHONE = "pref.booking.phone";
    public static final String P_BOOKING_COMMUNITY = "pref.booking.community";

    public static final String P_URL_AD_IMG = "pref.url.ad.img";
    public static final String P_URL_AD = "pref.url.ad";

    private LinkedList<Pair<String, Object>> mUpdateQueue = new LinkedList<Pair<String, Object>>();
    private SharedPreferences mPreference;
    private Context mContext;

    private SessionManager(Context context) {
        synchronized (this) {
            mContext = context;
            if (mPreference == null) {
                mPreference = PreferenceManager
                        .getDefaultSharedPreferences(mContext);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof Pair) {
            synchronized (mUpdateQueue) {
                if (data != null) {
                    mUpdateQueue.add((Pair<String, Object>) data);
                }
            }
            writePreferenceSlowly();
        }
    }

    public static SessionManager get(Context context) {
        if (mInstance == null) {
            mInstance = new SessionManager(context);
        }
        return mInstance;
    }

    /**
     * v
     * Do Hibernation slowly
     */
    private void writePreferenceSlowly() {
        if (mCurrentUpdateThread != null) {
            if (mCurrentUpdateThread.isAlive()) {
                // the update thread is still running,
                // so no need to start a new one
                return;
            }
        }

        // update the seesion value back to preference
        // ATTENTION: some more value will be add to the queue while current
        // task is running
        mCurrentUpdateThread = new Thread() {

            @Override
            public void run() {

                try {
                    // sleep 10secs to wait some concurrent task be
                    // inserted into the task queue
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                writePreference();
            }

        };

        mCurrentUpdateThread
                .setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mCurrentUpdateThread.start();
    }

    /**
     * Do Hibernation immediately
     */
    public void writePreferenceQuickly() {
        // update the seesion value back to preference
        // ATTENTION: some more value will be add to the queue while current
        // task is running
        mCurrentUpdateThread = new Thread() {

            @Override
            public void run() {
                writePreference();
            }

        };

        mCurrentUpdateThread
                .setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mCurrentUpdateThread.start();
    }

    /**
     * Write session value back to preference
     */
    private void writePreference() {
        Editor editor = mPreference.edit();

        synchronized (mUpdateQueue) {
            while (!mUpdateQueue.isEmpty()) {
                // remove already unused reference from the task queue
                Pair<String, Object> updateItem = mUpdateQueue.remove();

                // the preference key
                final String key = (String) updateItem.first;
                if (P_ISLOGIN.equals(key)) {
                    editor.putBoolean(key, (Boolean) updateItem.second);
                } else if (P_ISFIRST.equals(key)) {
                    editor.putBoolean(key, (Boolean) updateItem.second);
                } else if (P_ISFIRST_BIND_REMIND.equals(key)) {
                    editor.putBoolean(key, (Boolean) updateItem.second);
                } else if (P_ISFIRST_RESIDENT_BIND_REMIND.equals(key)) {
                    editor.putBoolean(key, (Boolean) updateItem.second);
                } else if (P_ISBIND.equals(key)) {
                    editor.putBoolean(key, (Boolean) updateItem.second);
                } else if (P_USERNAME.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_TOKEN.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_COMMUNITY.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_COMMUNITY_CODE.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_COMMUNITY_CITY.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_BOOKING_NAME.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_BOOKING_PHONE.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_BOOKING_COMMUNITY.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_BOOKING_DETAIL_ADDRESS.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_SIGN_IN_DAY.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_URL_AD_IMG.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                } else if (P_URL_AD.equals(key)) {
                    editor.putString(key, String.valueOf(updateItem.second));
                }
            }
        }

        // update the preference
        apply(editor);
    }

    public HashMap<String, Object> readPreference() {
        if (isPreferenceNull()) {
            return null;
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put(P_ISLOGIN, mPreference.getBoolean(P_ISLOGIN, false));
        data.put(P_ISFIRST, mPreference.getBoolean(P_ISFIRST, true));
        data.put(P_ISFIRST_BIND_REMIND, mPreference.getBoolean(P_ISFIRST_BIND_REMIND, true));
        data.put(P_ISFIRST_RESIDENT_BIND_REMIND, mPreference.getBoolean(P_ISFIRST_RESIDENT_BIND_REMIND, true));
        data.put(P_ISBIND, mPreference.getBoolean(P_ISBIND, true));
        data.put(P_TOKEN, mPreference.getString(P_TOKEN, ""));
        data.put(P_USERNAME, mPreference.getString(P_USERNAME, ""));
        data.put(P_COMMUNITY, mPreference.getString(P_COMMUNITY, ""));
        data.put(P_COMMUNITY_CODE, mPreference.getString(P_COMMUNITY_CODE, ""));
        data.put(P_COMMUNITY_CITY, mPreference.getString(P_COMMUNITY_CITY, ""));
        data.put(P_BOOKING_NAME, mPreference.getString(P_BOOKING_NAME, ""));
        data.put(P_BOOKING_PHONE, mPreference.getString(P_BOOKING_PHONE, ""));
        data.put(P_BOOKING_COMMUNITY, mPreference.getString(P_BOOKING_COMMUNITY, ""));
        data.put(P_BOOKING_DETAIL_ADDRESS, mPreference.getString(P_BOOKING_DETAIL_ADDRESS, ""));
        data.put(P_SIGN_IN_DAY, mPreference.getString(P_SIGN_IN_DAY, ""));
        data.put(P_URL_AD_IMG, mPreference.getString(P_URL_AD_IMG, ""));
        data.put(P_URL_AD, mPreference.getString(P_URL_AD, ""));
        return data;
    }

    private boolean isPreferenceNull() {
        if (mPreference == null)
            return true;
        return false;
    }

    /**
     * Use this method to modify preference
     */
    public static void apply(SharedPreferences.Editor editor) {
        if (sApplyMethod != null) {
            try {
                sApplyMethod.invoke(editor);
                return;
            } catch (InvocationTargetException unused) {
                // fall through
            } catch (IllegalAccessException unused) {
                // fall through
            }
        }

        editor.commit();
    }

    private static final Method sApplyMethod = findApplyMethod();

    private static Method findApplyMethod() {
        try {
            Class<Editor> cls = SharedPreferences.Editor.class;
            return cls.getMethod("apply");
        } catch (NoSuchMethodException unused) {
            // fall through
        }
        return null;
    }
}