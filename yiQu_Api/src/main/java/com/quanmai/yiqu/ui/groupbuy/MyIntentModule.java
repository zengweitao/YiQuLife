package com.quanmai.yiqu.ui.groupbuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by 95138 on 2016/9/19.
 */
public class MyIntentModule extends ReactContextBaseJavaModule {

    public MyIntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "MyIntentModule";
    }

    @ReactMethod
    public void getDataFromIntent(Callback successBack, Callback erroBack){
        try{
            Activity currentActivity = getCurrentActivity();
            String result = currentActivity.getIntent().getStringExtra("result");//会有对应数据放入
            String data = currentActivity.getIntent().getStringExtra("data");//会有对应数据放入
            String phone = currentActivity.getIntent().getStringExtra("phone");//会有对应数据放入
            if (TextUtils.isEmpty(result)){
                result = "没有数据";
            }
            successBack.invoke(result,data,phone);
        }catch (Exception e){
            erroBack.invoke(e.getMessage());
        }
    }


    /**
     * 从JS页面跳转到原生activity   同时也可以从JS传递相关数据到原生
     * @param name  需要打开的Activity的class
     * @param params
     */
    @ReactMethod
    public void startActivityFromJS(String name, String params){
        try{
            Activity currentActivity = getCurrentActivity();
            if(null!=currentActivity){
                Class toActivity = Class.forName(name);
                Intent intent = new Intent(currentActivity,toActivity);
                intent.putExtra("newsInfoId", params);
                intent.putExtra("type","MyGroupBuyActivity");
                currentActivity.startActivity(intent);
            }
        }catch(Exception e){
            throw new JSApplicationIllegalArgumentException(
                    "不能打开Activity : "+e.getMessage());
        }
    }

    /**
     * 结束当前页面
     * */
    @ReactMethod
    public void finishMySelf(){
        try{
            Activity currentActivity = getCurrentActivity();
            if(null!=currentActivity){
                currentActivity.finish();
            }
        }catch(Exception e){
            throw new JSApplicationIllegalArgumentException(
                    "不能打开Activity : "+e.getMessage());
        }
    }
}
