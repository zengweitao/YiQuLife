package com.quanmai.yiqu.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.vo.RealNameMember;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.MyActivityManager;
import com.quanmai.yiqu.ui.HomeActivity;
import com.quanmai.yiqu.ui.booking.BookingStatusActivity;
import com.quanmai.yiqu.ui.booking.BookingStatusRecordActivity;
import com.quanmai.yiqu.ui.booking.RecycleSuccessActivity;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;
import com.quanmai.yiqu.ui.integration.ShowQRCodeActivity;
import com.quanmai.yiqu.ui.mys.realname.BindingInfoActivity;
import com.quanmai.yiqu.ui.mys.realname.ResidentBindingActivity;
import com.quanmai.yiqu.ui.mys.realname.UndeterminedActivity;
import com.quanmai.yiqu.ui.news.NewsActivity;
import com.quanmai.yiqu.ui.recycle.RecycleOrderActivity;
import com.quanmai.yiqu.ui.recycle.RecycleScoreRecordActivity;
import com.quanmai.yiqu.ui.unused.UnusedDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhanjinj on 16/4/8.
 */
public class JPushReceiver extends BroadcastReceiver {
    public static final String ACTION_NOTIFICATION_CANCEL = "action_notification_cancel";

    private static final String TAG = "JPush";
    private String strType = ""; //推送类型
    private String id = ""; //商品id
    public static int mTypeId = 0; //
    public static String mOldType = "";

    private NotificationManager mNotificationManager; //通知栏管理器
    private NotificationCompat.Builder mBuilder; //通知栏构建类


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (null == mNotificationManager) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);

            getStrExtra(bundle);

            switch (strType) {
                case "msg": {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(PersonalFragment.ACTION_HAS_MESSAGE));
                    break;
                }
                case "sysmsg": {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(PersonalFragment.ACTION_HAS_MESSAGE));
                    break;
                }
                case "order_receive": {
                    if (MyActivityManager.getInstance().getCurrentActivityName().contains("RecycleOrderActivity")) {
                        Intent i = new Intent(RecycleOrderActivity.ACTION_HAVE_RECEIVE_ORDER);
                        i.putExtras(bundle);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                    } else {
                        processOrderReceiveMessage(bundle, context);
                    }
                    return;
                }
                case "order_verified": { //回收确认
                    processOrderMessage(bundle, context);
                    break;
                }
                case "order_recycle": { //回收提示
                    processOrderMessage(bundle, context);
                    break;
                }
                case "order_completed": {  //回收成功
                    processOrderMessage(bundle, context);
                    break;
                }

            }

            //推送无内容不在通知栏显示，自行构建通知
//            if (TextUtils.isEmpty(content)) {
//                processOrderMessage(bundle, context);
//            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 用户点击打开了通知");

            getStrExtra(bundle);
            switch (strType) {
                case "mymsg": { //私信
                    Intent i = new Intent(context, NewsActivity.class);
                    i.putExtra("currentItem", 0);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    break;
                }

                case "sysmsg": { //系统通知
                    Intent i = new Intent(context, NewsActivity.class);
                    i.putExtra("currentItem", 1);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    break;
                }
                case "goods_detail": { //商品评论
                    if (!TextUtils.isEmpty(id)) {
                        Intent i = new Intent(context, UnusedDetailActivity.class);
                        i.putExtra("goods_id", id);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } else { //接单
                        Intent i = new Intent(context, HomeActivity.class);
                        i.putExtra("currentItem", 2);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }
                    break;
                }
                //垃圾分类得分
                case "classify_Inspect": {
                    Intent i = new Intent(context, RecycleScoreRecordActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    break;
                }
                case "order_receive": { //接单
                    Intent i = new Intent(context, RecycleOrderActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    break;
                }
                case "order_recycle_user": {//回收人员收到的提示类型
                    Intent i = new Intent(context, RecycleOrderActivity.class);
                    i.putExtra("currentItem", 1);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    break;
                }
                case "order_cancel": { //用户取消订单
                    Intent i = new Intent(context, RecycleOrderActivity.class);
                    i.putExtra("currentItem", 1);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    break;
                }
                case "verifyAudit": { //实名制推送
                    getResidentBindingInfo(context);
                    break;
                }
            }
        } else if (ACTION_NOTIFICATION_CANCEL.equals(intent.getAction())) {
            Log.d(TAG, "用户消除了通知");
            int cancelTypeId = intent.getIntExtra("mTypeId", mTypeId);
            if (102 == cancelTypeId) {
                mOldType = "";
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[JPushReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[JPushReceiver] Unhandled intent - " + intent.getAction());
        }

    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    // 解析推送消息中的信息
    private void getStrExtra(Bundle bundle) {
        String strExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        strType = "";
        id = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strExtra);
            strType = jsonObject.optString("type");
            id = jsonObject.optString("id");
        } catch (JSONException e) {
            Log.e("JPush.EXTRA--->", "json解析失败");
        }
    }

    //预约用户处理回收订单确认推送
    private void processOrderMessage(Bundle bundle, Context mContext) {
        mBuilder = new NotificationCompat.Builder(mContext);

        String strExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String mType = "";
        String mId = "";
        try {
            JSONObject jsonObject = new JSONObject(strExtra);
            mType = jsonObject.optString("type");
            mId = jsonObject.optString("id");
        } catch (JSONException e) {
            Log.e("JPush.EXTRA--->", "json解析失败");
        }

        Intent notificationIntent = null;


        if (mType.equals("order_verified")) {
            mBuilder.setContentText("您的预约已被接单，请在指定时间内等待回收人员上门回收。");
        } else if (mType.equals("order_recycle")) {
            GregorianCalendar ca = new GregorianCalendar();
            System.out.println();
            if (ca.get(GregorianCalendar.AM_PM) == 0) {
                mBuilder.setContentText("回收人员将于今天上午上门回收，请留意，谢谢配合。");
            } else if (ca.get(GregorianCalendar.AM_PM) == 1) {
                mBuilder.setContentText("回收人员将于今天下午上门回收，请留意，谢谢配合。");
            } else {
                mBuilder.setContentText("回收人员将于今天上门回收，请留意，谢谢配合。");
            }

        } else if (mType.equals("order_completed")) {
            mBuilder.setContentText("您的订单回收完成，感谢您的支持。");
        }

        if (BookingStatusActivity.isForeground) {
            notificationIntent = new Intent(mContext, BookingStatusActivity.class);
            if (mType.equals("order_completed")) {
                notificationIntent.putExtra("status", "completed");
            }
        } else if (!BookingStatusActivity.isForeground) {
            if (BookingStatusRecordActivity.isForeground) {
                notificationIntent = new Intent(mContext, BookingStatusRecordActivity.class);
                if (mType.equals("order_completed")) {
                    notificationIntent.putExtra("status", "completed");
                }
            } else if (mType.equals("order_completed")) {
                notificationIntent = new Intent(mContext, RecycleSuccessActivity.class);
                notificationIntent.putExtra("status", "completed");
            } else if (mType.equals("pointManage")) {
                notificationIntent = new Intent(mContext, ShowQRCodeActivity.class);
            }
        }

        if (notificationIntent != null) {
            notificationIntent.putExtra("orderId", mId);
        }

        //TODO 暂时避免空指针异常，后期需完善处理
        if (notificationIntent == null) {
            return;
        }

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentTitle("益趣生活")//设置通知栏标题
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo))
                .setContentIntent(contentIntent) //设置通知栏点击意图
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.drawable.logo);//设置通知小ICON

        if (BookingStatusRecordActivity.isForeground || BookingStatusActivity.isForeground) {
            mContext.startActivity(notificationIntent);
        } else {
            if (ShowQRCodeActivity.isForeground && ShowQRCodeActivity.mId.equals(mId)) {
                mContext.startActivity(notificationIntent);
                return;
            }
            if (mType.equals("pointManage")) {

            } else {
                mNotificationManager.notify(1001, mBuilder.build());
            }
        }

    }

    //回收人员处理回收订单推送
    private void processOrderReceiveMessage(Bundle bundle, Context context) {
        mBuilder = new NotificationCompat.Builder(context);

        String strExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String mType = ""; //回收订单类型
        try {
            JSONObject jsonObject = new JSONObject(strExtra);
            mType = jsonObject.optString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!mOldType.equals(mType)) {
            mOldType = mType;
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
        } else {
            mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
        }

        Intent intent = new Intent();
        switch (mType) {
            case "order_receive": { //回收订单——预约回收
                mTypeId = 102;
                mBuilder.setContentText("您有新的回收预约通知");
                intent.setClass(context, RecycleOrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                break;
            }
        }

        mBuilder.setContentTitle("益趣生活")
                .setContentText("您有新的回收预约通知")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setDeleteIntent(PendingIntent.getBroadcast(context, 0, new Intent(ACTION_NOTIFICATION_CANCEL).putExtra("mTypeId", mTypeId), 0))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0); //延迟执行的intent
        mBuilder.setContentIntent(pendingIntent);

        if (MyActivityManager.getInstance().getCurrentActivityName().contains("RecycleOrderActivity")) {
            Intent i = new Intent(RecycleOrderActivity.ACTION_HAVE_RECEIVE_ORDER);
            i.putExtras(bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(i);
        } else {
            mNotificationManager.notify(mTypeId, mBuilder.build());
        }
    }

    //获取实名制用户信息
    private void getResidentBindingInfo(final Context context) {
        UserApi.get().getResidentBindingInfo(context, new ApiConfig.ApiRequestListener<Map<String, Object>>() {
            @Override
            public void onSuccess(String msg, Map<String, Object> data) {
                if (data == null || data.size() <= 0) {
                    return;
                }
                String username = (String) data.get("username");
                String tel = (String) data.get("tel");
                String address = (String) data.get("address");

                Intent intent = new Intent();
                intent.putExtra("name", username);
                intent.putExtra("phone", tel);
                intent.putExtra("address", address);
                intent.putExtra("appealstatus", (String) data.get("appealstatus"));
                intent.putExtra("usercompareid", (String) data.get("usercompareid"));
                intent.putExtra("usertype", (String) data.get("usertype"));
                switch ((String) data.get("usertype")) {
                    case "0": { //无
                        intent.setClass(context, ResidentBindingActivity.class);
                        break;
                    }
                    case "1": { //户主
                        if ("1".equals(data.get("bindtatus"))) { //绑定状态 0.没有申请解绑 1.绑定待审核
                            intent.putExtra("type", UndeterminedActivity.TYPE_UNBIND);
                            intent.setClass(context, UndeterminedActivity.class);
                            break;
                        }
                        intent.putExtra("membersList", (ArrayList<RealNameMember>) data.get("membersList"));
                        intent.setClass(context, BindingInfoActivity.class);
                        break;
                    }
                    case "2": { //成员
                        intent.putExtra("membersList", (ArrayList<RealNameMember>) data.get("membersList"));
                        intent.setClass(context, BindingInfoActivity.class);
                        break;
                    }
                    case "3": { //申请成员中
                        intent.putExtra("type", UndeterminedActivity.TYPE_APPLY);
                        intent.putExtra("status", "户主审核中");
                        intent.setClass(context, UndeterminedActivity.class);
                        break;
                    }
                    case "4": { //申诉中
                        intent.putExtra("type", UndeterminedActivity.TYPE_APPEAL);
                        intent.setClass(context, UndeterminedActivity.class);
                        break;
                    }
                    default:
                        break;
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(String msg) {
                Utils.showCustomToast(context, msg);
            }
        });
    }
}


