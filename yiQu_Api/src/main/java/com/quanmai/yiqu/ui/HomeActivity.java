package com.quanmai.yiqu.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.SessionManager;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.CheckInApi;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.UserInfoApi;
import com.quanmai.yiqu.api.vo.SignData;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.baidu.BaiduUtil;
import com.quanmai.yiqu.base.BaseFragmentActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.common.widget.SignInADPopupWindow;
import com.quanmai.yiqu.tabfragment.AlphaTabsIndicator;
import com.quanmai.yiqu.tabfragment.MainAdapter;
import com.quanmai.yiqu.ui.code.MipcaActivityCapture;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.fragment.AroundFragment;
import com.quanmai.yiqu.ui.fragment.HomePageFragment;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;
import com.quanmai.yiqu.ui.fragment.UnusedFragment;
import com.quanmai.yiqu.ui.groupbuy.GroupBuyActivity;
import com.quanmai.yiqu.ui.groupbuy.MyGroupBuyActivity;
import com.quanmai.yiqu.ui.groupbuy.RNCacheViewManager;
import com.quanmai.yiqu.ui.login.LoginActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/***
 * @author Administrator
 */
public class HomeActivity extends BaseFragmentActivity implements OnClickListener {
    private ViewPager viewPager_home;
    private LinearLayout menu_home;
    //    private LinearLayout menu_around;
//    private LinearLayout menu_unused;
    private LinearLayout menu_personal;
    private RelativeLayout menu_fetch;
    private ImageView imageView_home;
    //    private ImageView imageView_around;
//    private ImageView imageView_unused;
    private ImageView imageView_personal;
    private TextView tv_home;
    private TextView tv_around;
    private TextView tv_unused;
    private TextView tv_personal;
    Session mSession;
    AroundFragment aroundFragment;
    UnusedFragment unusedFragment;
    PersonalFragment personalFragment;
    HomePageFragment homePageFragment;
    private ImageView imageViewHomeLogo;
    private String DEFULT_LOGO_URL = "http://115.28.228.192:8080/yiqu/img/scan_button.png?time=";
    private SignInADPopupWindow mSignInADPopupWindow; //签到成功弹窗（广告）

    private Dialog mUpdateDialog;
    private int mIntTag = 2; //是否处于app更新弹窗提醒的状态标示符  0.强制 1.提醒 2.不提醒

    AlphaTabsIndicator alphaTabsIndicator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//		PushAgent mPushAgent = PushAgent.getInstance(this); // 友盟推送
//		mPushAgent.enable();
//		mPushAgent.onAppStart();
//		UmengUpdateAgent.update(this);
        mSession = Session.get(this);
        initView();
        //initViewPager();
//        RNCacheViewManager.init((Activity) this, new GroupBuyActivity("RnForYiQu",new Bundle()));
        // 检查更新信息
//		UmengUpdateAgent.setUpdateOnlyWifi(false);
//		final String forceUpdate = MobclickAgent.getConfigParams(this, "force_update_android_hsh");
//		UmengUpdateAgent.setUpdateAutoPopup(false);
//		UmengUpdateAgent.forceUpdate(this);
//		if(forceUpdate.equals("1")){//如果是 1  强制更新
//		UmengUpdateAgent.forceUpdate(this);
//		}else{
//			UmengUpdateAgent.update(this);
//		}
//		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//			@Override
//			public void onUpdateReturned(int updateStatus,
//										 UpdateResponse updateInfo) {
//				switch (updateStatus) {
//					case UpdateStatus.Yes: // has update
//						UmengUpdateAgent.showUpdateDialog(HomeActivity.this, updateInfo);
//						break;
//					case UpdateStatus.No: // has no update
//						break;
//					case UpdateStatus.NoneWifi: // none wifi
//						break;
//					case UpdateStatus.Timeout: // time out
//						break;
//				}
//			}
//		});
//
//		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
//			@Override
//			public void onClick(int status) {
//				switch (status) {
//					case UpdateStatus.Update:
//
//						break;
//					default:
//						if (forceUpdate.equals("1")) {
//							finish();
//						}
//						break;
//				}
//			}
//		});
//        /*增加 web启动app功能 2015/12/8 by yinweichao*/
//        if (getIntent().getExtras()!=null){
//            if (getIntent().getExtras().getString("keyFromHtml").equals("nearby")){
//                viewPager_home.setCurrentItem(1, false);
//            }else if (getIntent().getExtras().getString("keyFromHtml").equals("property_management")){
//                viewPager_home.setCurrentItem(2, false);
//            }
//        }
        appUpdateCheck();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra("currentItem")) {
            int currentItem = intent.getIntExtra("currentItem", 0);
            alphaTabsIndicator.initViewPager(currentItem);
        }
        if (intent.hasExtra("loginSuccess") && intent.getBooleanExtra("loginSuccess", false)) {
            getUserInfo();
        }
        super.onNewIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //(友盟页面统计)统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //友盟页面统计
    }

    /**
     * find view and set listener
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void initView() {

        viewPager_home = (ViewPager) this.findViewById(R.id.viewPager_home);
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), this);
        viewPager_home.setAdapter(mainAdapter);
        viewPager_home.addOnPageChangeListener(mainAdapter);
        alphaTabsIndicator = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator);
        alphaTabsIndicator.setViewPager(viewPager_home);

        menu_fetch = (RelativeLayout) this.findViewById(R.id.menu_fetch);

        imageViewHomeLogo = (ImageView) findViewById(R.id.imageViewHomeLogo);
        int random = (int) (Math.random() * 1000);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.icon_home_scan_button)
                .cacheInMemory(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100, true, true, true))
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoader.getInstance().displayImage(ApiConfig.LOGO_URL + random, imageViewHomeLogo, options);

        menu_fetch.setOnClickListener(this);

        SessionManager.get(this).readPreference();
        new BaiduUtil(getApplicationContext()).start(new BaiduUtil.onLocationCompleteListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onLocationSuccess(double x, double y,
                                          String city) {
                mSession.setLocation_x(x);
                mSession.setLocation_y(y);
                // locateSuccess = true;
            }

            @Override
            public void onError(String info) {
            }
        });
    }

    //获取app版本信息，检查更新
    private void appUpdateCheck() {
        UserApi.get().getAppVersionInfo(mContext, new ApiConfig.ApiRequestListener<Map<String, String>>() {
            @Override
            public void onSuccess(String msg, final Map<String, String> data) {
                if (data == null || data.size() <= 0) {
                    return;
                }

                if (StringUtil.versionCompare(data.get("currentversion"), (data.get("latestversion")))) {
                    return;
                }

                if ("0".equals(data.get("updatemode"))) {
                    mIntTag = 0;
                }

                if ("2".equals(data.get("updatemode"))) {
                    return;
                }

                mUpdateDialog = DialogUtil.getUpdateDialog(mContext, data.get("updatemode"), data.get("description"), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Utils.appDownload(mContext, data.get("url"));
                        mUpdateDialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(data.get("url")));
                        mContext.startActivity(intent.createChooser(intent, "请选择浏览器"));

                        switch (v.getId()) {
                            case R.id.btnConfirm: {
                                break;
                            }
                            case R.id.btnUpdateNow: {
                                finish();
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                });

                mUpdateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (mIntTag == 0 && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                            mUpdateDialog.dismiss();
                            finish();
                            return true;
                        }
                        return false;
                    }
                });

                mUpdateDialog.show();
            }

            @Override
            public void onFailure(String msg) {
                //此处不做任何提示处理
            }
        });
    }

    /**
     * 每日启动app自动签到
     */
    private void autoSignIn() {
        if (!Session.get(mContext).isLogin()) { //未登录
            return;
        }
        if (!(UserInfo.get().sign == 1)) { //用户未签到，且为当天首次启动
            CheckInApi.get().Sign(mContext, new ApiConfig.ApiRequestListener<SignData>() {

                @Override
                public void onSuccess(String msg, final SignData data) {
                    dismissLoadingDialog();
                    UserInfo.get().setSign(1);
//                    showPicDialog(data.signpoint, data.signdays, data.score, data.vipInfo.level_name);
                    mSignInADPopupWindow = (SignInADPopupWindow) SignInADPopupWindow.showOnCenter(HomeActivity.this, viewPager_home, data.adInfo.adver_img, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MobclickAgent.onEvent(mContext, "advertisement"); //友盟自定义事件统计——签到广告
                            mSignInADPopupWindow.dismiss();

                            if (!TextUtils.isEmpty(data.adInfo.adver_url)) {
                                Intent intent = new Intent(mContext, WebActivity.class);
                                intent.putExtra("http_url", data.adInfo.adver_url);
                                startActivity(intent);
                            }
                        }
                    });
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(PersonalFragment.ACTION_REFRESH_PAGE));
                }

                @Override
                public void onFailure(String msg) {
                    dismissLoadingDialog();
                }
            });
        }
    }

    //获取用户详细信息
    private void getUserInfo() {
        UserInfoApi.get().getUserHome(mContext, new ApiConfig.ApiRequestListener<UserInfo>() {
            @Override
            public void onSuccess(String msg, UserInfo data) {
                autoSignIn();
            }

            @Override
            public void onFailure(String msg) {
            }
        });
    }

    /**
     *
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_fetch: { //扫码取袋
                MobclickAgent.onEvent(mContext, "scanCode"); //友盟自定义事件统计——扫码取袋
                if (Session.get(mContext).isLogin()) {
                    Intent intent = new Intent(mContext, MipcaActivityCapture.class);
                    intent.putExtra("whichjump", "whichjump");
                    startActivity(intent);
                } else {
                    Utils.showCustomToast(mContext, "未登录，请登录后再试");
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
//                Intent intent = new Intent(this, GroupBuyActivity.class);
//                intent.putExtra("result",mSession.getToken());
//                startActivity(intent);
                break;
            }
        }
    }

    private long lastExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - lastExitTime > 1500) {
                lastExitTime = System.currentTimeMillis();
                Utils.showCustomToast(this, "再按一次，退出应用");
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
