package com.quanmai.yiqu.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.MyActivityManager;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

public class App extends MultiDexApplication {
    public static UploadManager uploadManager;
    //	public LocationClient mLocationClient;
//	public MyLocationListener mMyLocationListener;
//	public Vibrator mVibrator;

    private static Context appContext;

    public static Context getInstance() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//		UmengUpdateAgent.setDefault();
//		UmengUpdateAgent.setUpdateOnlyWifi(false);
//		UmengUpdateAgent.update(this);

        //第二个参数是appkey，就是百川应用创建时候的appkey
        FeedbackAPI.initAnnoy(this, "23362535");
        ImageloaderUtil.initImageLoader(this);
//		SDKInitializer.initialize(this);
        Configuration config = new Configuration.Builder()
                .chunkSize(256 * 1024)// 分片上传时，每片的大小。 默认 256K
                .putThreshhold(512 * 1024)// 启用分片上传阀值。默认 512K
                .connectTimeout(10)// 链接超时。默认 10秒
                .responseTimeout(60)// 服务器响应超时。默认 60秒
                .recorder(null)// recorder 分片上传时，已上传片记录器。默认 null
                .recorder(null, null)// keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(Zone.zone0)// 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
                .build();
        // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
        uploadManager = new UploadManager(config);

//        Bugtags.start("5106fa49afa1bea4c8f44958b5e898dc", this, Bugtags.BTGInvocationEventBubble);
//		mLocationClient = new LocationClient(this.getApplicationContext());
//        mMyLocationListener = new MyLocationListener();
//        mLocationClient.registerLocationListener(mMyLocationListener);
//        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
//        initPush();
        appContext = this.getApplicationContext();

        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
       // JPushInterface.resumePush(this);
        JPushInterface.setLatestNotificationNumber(this, 3);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyActivityManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        /**
         * 友盟统计
         */
        //场景类型设置(普通统计场景类型)
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        //禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
        //打开调试模式
        MobclickAgent.setDebugMode(false);
    }

//    private PushAgent mPushAgent;

//    public void initPush() {
//        mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.setDebugMode(false);
//        /**
//         * 该Handler是在IntentService中被调用，故
//         * 1. 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
//         * 2. IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
//         * 	      或者可以直接启动Service
//         * */
//        UmengMessageHandler messageHandler = new UmengMessageHandler() {
//            @Override
//            public void dealWithCustomMessage(final Context context, final UMessage msg) {
//                new Handler(getMainLooper()).post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
////						UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
////						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            @Override
//            public Notification getNotification(Context context,
//                                                UMessage msg) {
//                switch (msg.builder_id) {
//                    default:
//                        //默认为0，若填写的builder_id并不存在，也使用默认。
//                        return super.getNotification(context, msg);
//                }
//            }
//        };
//        mPushAgent.setMessageHandler(messageHandler);
//
//        /**
//         * 该Handler是在BroadcastReceiver中被调用，故
//         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
//         * */
//        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
//            @Override
//            public void dealWithCustomAction(Context context, UMessage msg) {
//                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//            }
//        };
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);
//    }


    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");// 位置语义化信息
//            sb.append(location.getLocationDescribe());
//            List<Poi> list = location.getPoiList();// POI信息
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }


    }


}
