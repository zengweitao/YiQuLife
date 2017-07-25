package com.quanmai.yiqu.ui.groupbuy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.react.BuildConfig;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.shell.MainReactPackage;
import com.microsoft.codepush.react.CodePush;


import java.util.Arrays;
import java.util.List;

public class GroupBuyActivity extends AbsRnInfo {

    private String mModuleName;
    private Bundle mLaunchOptions;
    private Context myContext;

    public GroupBuyActivity() {
        super();
    }

    public GroupBuyActivity(String moduleName) {
        this.mModuleName = moduleName;
    }

    public GroupBuyActivity(String moduleName, Bundle launchOptions, Context  context) {
        this.mModuleName = moduleName;
        this.mLaunchOptions = launchOptions;
        myContext = context;
    }

    @javax.annotation.Nullable
    @Override
    protected String getJSBundleFile() {
        return CodePush.getJSBundleFile();
    }

    @Nullable
    @Override
    public Bundle getLaunchOptions() {
        return mLaunchOptions;
    }

    @Override
    protected String
    getMainComponentName() {
        return "RnForYiQu";
    }

    @Override
    protected boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new MyReactPackage(),
                new CodePush("IBRdfg7DwjXe-lYXVhvU7DGHKB1eEJYplWsoW", myContext, BuildConfig.DEBUG)
        );
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
