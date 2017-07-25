package com.quanmai.yiqu.ui.groupbuy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by 95138 on 2016/9/21.
 */
public class CallPhoneModule extends ReactContextBaseJavaModule {
    public CallPhoneModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "CallPhoneModule";
    }

    /**
     *  Js调用原生拨打电话组件
     * */
    @ReactMethod
    public void callPhone(String phoneNo){
        try{
            Activity currentActivity = getCurrentActivity();
            if(null!=currentActivity){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + phoneNo));
                currentActivity.startActivity(intent);
            }
        }catch (Exception e){
            throw new JSApplicationIllegalArgumentException(
                    "无法拨打电话 : "+e.getMessage());
        }
    }
}
