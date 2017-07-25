package com.quanmai.yiqu;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.common.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class Session extends Observable {
    private SessionManager mSessionManager;
    private static Session mInstance;
    private double Location_x;
    private double Location_y;
    private Context mContext;
    private String osVersion;
    private String version;
    private String username;
    private boolean isFirst;
    private boolean isLogin = false;
    private boolean isBind = false;
    private boolean isFirstBindRemind = true; //是否社区未绑定初次提醒
    private boolean isFirstResidentBindRemind = true; //是否用户实名制初次提醒
    private String token;
    private String imei;
    private String community;
    private String community_code;
    private String community_city;
    private String sign_in_day; //签到日期，yyyy-MM-dd

    //预约人信息
    private String bookingName;
    private String bookingPhone;
    private String bookingCommunity;
    private String bookingDetailAddress;

    //启动页广告
    private String urlAdImg; //图片
    private String urlAd; //链接

    public String getCommunity_city() {
        return community_city;
    }

    public void setCommunity_city(String community_city) {
        this.community_city = community_city;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_COMMUNITY_CITY, community_city));
    }

    public String getBookingDetailAddress() {
        return bookingDetailAddress;
    }

    public void setBookingDetailAddress(String bookingDetailAddress) {
        this.bookingDetailAddress = bookingDetailAddress;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_BOOKING_DETAIL_ADDRESS, bookingDetailAddress));
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_BOOKING_NAME, bookingName));
    }

    public String getBookingPhone() {
        return bookingPhone;
    }

    public void setBookingPhone(String bookingPhone) {
        this.bookingPhone = bookingPhone;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_BOOKING_PHONE, bookingPhone));
    }

    public String getBookingCommunity() {
        return bookingCommunity;
    }

    public void setBookingCommunity(String bookingCommunity) {
        this.bookingCommunity = bookingCommunity;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_BOOKING_COMMUNITY, bookingCommunity));
    }

    public String getCommunity_code() {
        return community_code;
    }

    public void setCommunity_code(String community_code) {
        this.community_code = community_code;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_COMMUNITY_CODE, community_code));
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_COMMUNITY, community));
    }

    public static Session get(Context context) {
        if (mInstance == null) {
            mInstance = new Session(context);
        }

        return mInstance;
    }

    private Session(Context context) {
        synchronized (this) {
            mContext = context;
            osVersion = "Android" + android.os.Build.VERSION.RELEASE;
            version = getVERSION(context);
            imei = setImei(context);
            readSettings();
        }
    }


    private String setImei(Context context) {
        String imei = null;
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        return imei;
    }

    public String getImei() {
        return imei;
    }


    public double getLocation_x() {
        return Location_x;
    }

    public void setLocation_x(double location_x) {
        Location_x = location_x;
    }

    public double getLocation_y() {
        return Location_y;
    }

    public void setLocation_y(double location_y) {
        Location_y = location_y;
    }

    private void setUsername(String username) {
        this.username = username;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_USERNAME, username));
    }

    public String getUsername() {
        return username;
    }


    public String getVERSION(Context context) {
        String version = null;

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            version = info.versionName;

            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getVersion() {
        return version;
    }

    public void close() {
        mSessionManager.writePreferenceQuickly();
        mInstance = null;
    }

//	private void setPushAlias(String Alias) {
//		if (Alias == null) {
//			Alias = new String();
//		}
//		PushUtils.setAlias(mContext, Alias);
//	}

    public String getOsVersion() {
        return osVersion;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean isFirst) {
        if (this.isFirst == isFirst) {
            return;
        }

        this.isFirst = isFirst;

        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_ISFIRST, isFirst));
    }

    public String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;

        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(SessionManager.P_TOKEN,
                token));
    }

    public boolean isLogin() {
        return isLogin;
        // return false;

    }

    private void setLogin(boolean isLogin) {
        // there is no need to update for [same] value
        if (this.isLogin == isLogin) {
            return;
        }
        this.isLogin = isLogin;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_ISLOGIN, isLogin));
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        if (this.isBind == bind) {
            return;
        }
        this.isBind = bind;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_ISBIND, isBind));
    }

    public boolean isFirstBindRemind() {
        return isFirstBindRemind;
    }

    public void setFirstBindRemind(boolean firstBindRemind) {
        if (this.isFirstBindRemind = firstBindRemind) {
            return;
        }
        this.isFirstBindRemind = firstBindRemind;

        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_ISFIRST_BIND_REMIND, isFirstBindRemind
        ));
    }

    public boolean isFirstResidentBindRemind() {
        return isFirstResidentBindRemind;
    }

    public void setFirstResidentBindRemind(boolean firstResidentBindRemind) {
        if (this.isFirstResidentBindRemind = firstResidentBindRemind) {
            return;
        }
        this.isFirstResidentBindRemind = firstResidentBindRemind;

        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_ISFIRST_RESIDENT_BIND_REMIND, isFirstResidentBindRemind
        ));
    }

    public String getSign_in_day() {
        return sign_in_day;
    }

    public void setSign_in_day(String sign_in_day) {
        if (this.sign_in_day == sign_in_day) {
            return;
        }
        this.sign_in_day = sign_in_day;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_SIGN_IN_DAY, sign_in_day
        ));
    }

    public String getUrlAdImg() {
        return urlAdImg;
    }

    public void setUrlAdImg(String urlAdImg) {
        if (this.urlAdImg.equals(urlAdImg)) {
            return;
        }
        this.urlAdImg = urlAdImg;

        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_URL_AD_IMG, urlAdImg
        ));
    }

    public String getUrlAd() {
        return urlAd;
    }

    public void setUrlAd(String urlAd) {
        if (this.urlAdImg.equals(urlAd)) {
            return;
        }
        this.urlAd = urlAd;
        super.setChanged();
        super.notifyObservers(new Pair<String, Object>(
                SessionManager.P_URL_AD, urlAd
        ));
    }

    public void Login(String userName, String token) {
//		setPushAlias(userName);
        setUsername(userName);
        setToken(token);
        setLogin(true);
    }

    public void addAddress(String bookingName, String bookingPhone, String bookingCommunity, String bookingDetailAddress) {
        setBookingName(bookingName);
        setBookingPhone(bookingPhone);
        setBookingCommunity(bookingCommunity);
        setBookingDetailAddress(bookingDetailAddress);
    }

    public void Logout() {
        setLogin(false);
        setToken("");
        setUsername("");
        setBind(false);
        UserInfo.clean();
        //调用此 API 来同时设置别名，这里""（空字符串）表示取消之前的设置。
        //Tag为空数组或列表表示取消之前的设置。
        JPushInterface.setAliasAndTags(mContext, "",new HashSet<String>(), new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String s, Set<String> set) {
                Utils.E("JPush cancel setAlias--->" + ((responseCode == 0) ? "success" : "false"));
            }
        });
    }

    /**
     * 读取用户所有的设置
     */
    private void readSettings() {
        new Thread() {
            public void run() {
                mSessionManager = SessionManager.get(mContext);
                addObserver(mSessionManager);
                HashMap<String, Object> preference = mSessionManager
                        .readPreference();
                isLogin = (Boolean) preference.get(SessionManager.P_ISLOGIN);
                isFirst = (Boolean) preference.get(SessionManager.P_ISFIRST);
                isFirstBindRemind = (Boolean) preference.get(SessionManager.P_ISFIRST_BIND_REMIND);
                isFirstResidentBindRemind = (Boolean) preference.get(SessionManager.P_ISFIRST_RESIDENT_BIND_REMIND);
                isBind = (Boolean) preference.get(SessionManager.P_ISBIND);
                token = (String) preference.get(SessionManager.P_TOKEN);
                username = (String) preference.get(SessionManager.P_USERNAME);
                community = (String) preference.get(SessionManager.P_COMMUNITY);
                community_code = (String) preference.get(SessionManager.P_COMMUNITY_CODE);
                community_city = (String) preference.get(SessionManager.P_COMMUNITY_CITY);
                sign_in_day = (String) preference.get(SessionManager.P_SIGN_IN_DAY);
                bookingName = (String) preference.get(SessionManager.P_BOOKING_NAME);
                bookingPhone = (String) preference.get(SessionManager.P_BOOKING_PHONE);
                bookingCommunity = (String) preference.get(SessionManager.P_BOOKING_COMMUNITY);
                bookingDetailAddress = (String) preference.get(SessionManager.P_BOOKING_DETAIL_ADDRESS);
                urlAdImg = (String) preference.get(SessionManager.P_URL_AD_IMG);
                urlAd = (String) preference.get(SessionManager.P_URL_AD);
            }
        }.start();
    }

}