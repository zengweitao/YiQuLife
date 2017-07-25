package com.quanmai.yiqu.ui.fragment;


import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IWxCallback;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.UserInfoApi;
import com.quanmai.yiqu.api.vo.RealNameMember;
import com.quanmai.yiqu.api.vo.SignData;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseFragment;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.CustomDialog;
import com.quanmai.yiqu.common.widget.PullToZoomScrollView;
import com.quanmai.yiqu.common.widget.SignInADPopupWindow;
import com.quanmai.yiqu.common.widget.SignInPopupWindow;
import com.quanmai.yiqu.common.widget.XCRoundImageView;
import com.quanmai.yiqu.share.ShareUtil;
import com.quanmai.yiqu.ui.Around.MyCouponActivity;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.fuli.LuckyBagActivity;
import com.quanmai.yiqu.ui.grade.FetchBagRecordActivity;
import com.quanmai.yiqu.ui.grade.GradeIndexActivity;
import com.quanmai.yiqu.ui.groupbuy.MyGroupBuyActivity;
import com.quanmai.yiqu.ui.groupbuy.RNCacheViewManager;
import com.quanmai.yiqu.ui.integration.GiveIntegrationActivity;
import com.quanmai.yiqu.ui.login.LoginActivity;
import com.quanmai.yiqu.ui.mys.VipDetailActivity;
import com.quanmai.yiqu.ui.mys.collect.CollectListActivity;
import com.quanmai.yiqu.ui.mys.handwork.HandWorkActivity;
import com.quanmai.yiqu.ui.mys.publishmanage.PublishManageActivity;
import com.quanmai.yiqu.ui.mys.realname.BindingInfoActivity;
import com.quanmai.yiqu.ui.mys.realname.ResidentBindingActivity;
import com.quanmai.yiqu.ui.mys.realname.UndeterminedActivity;
import com.quanmai.yiqu.ui.mys.record.SignRecordActivity;
import com.quanmai.yiqu.ui.mys.setting.PersonalInfoSettingActivity;
import com.quanmai.yiqu.ui.mys.setting.SettingActivity;
import com.quanmai.yiqu.ui.news.NewsActivity;
import com.quanmai.yiqu.ui.recycle.RecycleOrderActivity;
import com.quanmai.yiqu.ui.recycle.RecycleScoreRecordActivity;
import com.quanmai.yiqu.ui.ycoin.GiftGivingActivity;
import com.quanmai.yiqu.ui.ycoin.ScanRecycleLocalActivity;
import com.quanmai.yiqu.ui.ycoin.YCoinsActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Map;

/**
 * 个人主页
 */
public class PersonalFragment extends BaseFragment implements OnClickListener {
    public static int REQUEST_CODE_SETTING = 101;
    public static int REQUEST_CODE_MESSAGE = 102;
    public static String ACTION_NETWORKING_TO_REFRESH_DATA = "action_networking_to_refresh_data"; //联网刷新
    public static String ACTION_REFRESH_PAGE = "action_refresh_page"; //刷新
    public static String ACTION_HAS_MESSAGE = "action_had_message"; //消息通知

    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "DefaultValue";

    View mView;
    XCRoundImageView imageViewHead;
    ImageView imageViewSex;
    Button buttonLevel;
    TextView textViewName;
    TextView textViewYCoin;
    TextView textViewIntegral; //积分
    TextView textViewSignIn; //签到
    TextView textViewFednums; //福袋数
    ImageView imageViewBadge; //新消息提示红点
    LinearLayout linearContent;
    SignInPopupWindow popupWindow;
    SignInADPopupWindow mSignInADPopupWindow; //签到成功弹窗（广告）

    LocalBroadcastReceiver mLocalBroadcastReceiver;
    MessageBroadcastReceiver mMsgBroadcastReceiver;
    ImageView iv_red;

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    iv_red.setVisibility(View.VISIBLE);
                    break;
                case 102:
                    iv_red.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(BUNDLE_TITLE);
        }
        mView = inflater.inflate(R.layout.fragment_personal, container, false);
        init();

        getUserInfo();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserInfo.get() != null && UserInfo.get().sign == 1) {
            textViewSignIn.setText("签到记录");
        } else {
            textViewSignIn.setText("点击签到");
        }
        getFeedBackMessage(getActivity());
        MobclickAgent.onPageStart("我的"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
    }

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mLocalBroadcastReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mMsgBroadcastReceiver);
        super.onDestroyView();
    }

    protected void init() {
        imageViewHead = (XCRoundImageView) mView.findViewById(R.id.imageViewHeadPortrait);
        imageViewHead.setImageResource(R.drawable.default_header);
        imageViewHead.setBorderWidth(Utils.dp2px(mContext, 3));
        imageViewHead.setBorderColor(0x4BFFFFFF);
        iv_red = (ImageView) mView.findViewById(R.id.iv_red);
        buttonLevel = (Button) mView.findViewById(R.id.tvLevel);
        textViewYCoin = (TextView) mView.findViewById(R.id.textViewYCoin);
        textViewName = (TextView) mView.findViewById(R.id.textViewName);
        imageViewSex = (ImageView) mView.findViewById(R.id.imageViewSex);
        textViewIntegral = (TextView) mView.findViewById(R.id.textViewLeft);
        textViewSignIn = (TextView) mView.findViewById(R.id.textViewRight);
        textViewFednums = (TextView) mView.findViewById(R.id.textViewFednums);
        imageViewBadge = (ImageView) mView.findViewById(R.id.imageViewBadge);

        imageViewHead.setOnClickListener(this);
        textViewIntegral.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);

        mView.findViewById(R.id.relativeLevel).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutMessage).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutPublish).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutMyCoupon).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutClosed).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutCollection).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutSetting).setOnClickListener(this);
        mView.findViewById(R.id.relativeShare).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutInspection).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutLuckyBag).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutGivePoint).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutRecycleOrder).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutRecycleScore).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutResidentBinding).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutMyGroupBuy).setOnClickListener(this);
        mView.findViewById(R.id.relativeHandWork).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutFetchBagRecord).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutYCoin).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutGiftGiving).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutRecycleLocal).setOnClickListener(this);
        mView.findViewById(R.id.relativeLayoutgetuserdata).setOnClickListener(this);

        popupWindow = new SignInPopupWindow(getActivity());
        linearContent = (LinearLayout) mView.findViewById(R.id.linearContent);

        mLocalBroadcastReceiver = new LocalBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NETWORKING_TO_REFRESH_DATA);
        intentFilter.addAction(ACTION_REFRESH_PAGE);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mLocalBroadcastReceiver, intentFilter);

        mMsgBroadcastReceiver = new MessageBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mMsgBroadcastReceiver, new IntentFilter(ACTION_HAS_MESSAGE));

        ((PullToZoomScrollView) mView.findViewById(R.id.pullToZoomScrollView)).setOnRefreshListener(new PullToZoomScrollView.OnRefreshListener() {

            @Override
            public void onPullDownToRefresh() {
                showLoadingDialog();
                getUserInfo();
                getFeedBackMessage(getActivity());
            }
        });

    }

    private void getFeedBackMessage(Context context) {
        FeedbackAPI.getFeedbackUnreadCount(context, "", new IWxCallback() {
            @Override
            public void onSuccess(Object... objects) {
                Message message = new Message();
                if (Integer.parseInt(String.valueOf(objects[0])) > 0) {
                    message.what = 101;
                    myHandler.sendMessage(message);
                } else {
                    message.what = 102;
                    myHandler.sendMessage(message);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.e("mark", "onError");
            }

            @Override
            public void onProgress(int i) {
                Log.e("mark", "onProgress");
            }
        });
    }

    /**
     * 获取用户详细信息
     */
    public void getUserInfo() {
        if (!Session.get(mContext).isLogin()) return;

        UserInfoApi.get().getUserHome(mContext, new ApiConfig.ApiRequestListener<UserInfo>() {
                    @Override
                    public void onSuccess(String msg, UserInfo data) {
                        dismissLoadingDialog();
                        refreshPage();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                    }
                }

        );
    }

    private void refreshPage() {
        if (UserInfo.get().vipInfo.level_name.contains("LV")) {
            buttonLevel.setVisibility(View.VISIBLE);
            buttonLevel.setText(UserInfo.get().vipInfo.level_name);
        }
        textViewIntegral.setText("我的积分：" + String.valueOf(UserInfo.get().score));
        textViewYCoin.setText(UserInfo.get().amount + "");

        if (imageViewHead.getTag() == null || !imageViewHead.getTag().equals(UserInfo.get().face)) {
            imageViewHead.setTag(UserInfo.get().face);
            ImageloaderUtil.displayImage(mContext, UserInfo.get().face, imageViewHead);
        }

        if (TextUtils.isEmpty(UserInfo.get().alias)) {
            textViewName.setText("未设置");
        } else {
            textViewName.setText(UserInfo.get().alias);
        }

        if (UserInfo.get().usertype == UserInfo.USER_INSPECTION) {
            mView.findViewById(R.id.relativeLayoutInspection).setVisibility(View.VISIBLE);
        }

        mView.findViewById(R.id.relativeHandWork).setVisibility(View.GONE);
        mView.findViewById(R.id.relativeLayoutInspection).setVisibility(View.GONE);
        mView.findViewById(R.id.relativeLayoutRecycleOrder).setVisibility(View.GONE);
        mView.findViewById(R.id.relativeLayoutGivePoint).setVisibility(View.GONE);
        mView.findViewById(R.id.relativeLayoutRecycleScore).setVisibility(View.GONE);
        mView.findViewById(R.id.relativeLayoutGiftGiving).setVisibility(View.GONE);
        mView.findViewById(R.id.relativeLayoutRecycleLocal).setVisibility(View.GONE);
        mView.findViewById(R.id.relativeLayoutgetuserdata).setVisibility(View.GONE);

        //普通用户
        if (UserInfo.get().usertype.contains(UserInfo.USER_GENERAL)) {
            mView.findViewById(R.id.relativeLayoutRecycleScore).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeLayoutFetchBagRecord).setVisibility(View.VISIBLE);
        }
        //巡检员
        if (UserInfo.get().usertype.contains(UserInfo.USER_INSPECTION)) {
            mView.findViewById(R.id.relativeLayoutInspection).setVisibility(View.VISIBLE);
        }
        //回收人员
        if (UserInfo.get().usertype.contains(UserInfo.USER_RECYCLE)) {
            mView.findViewById(R.id.relativeLayoutRecycleOrder).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeLayoutRecycleLocal).setVisibility(View.VISIBLE);
        }
        //积分赠送管理员
        if (UserInfo.get().usertype.contains(UserInfo.USER_MANAGER)) {
            mView.findViewById(R.id.relativeLayoutGivePoint).setVisibility(View.VISIBLE);
        }
        //手工发袋管理员或物料入库管理员
        if (UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK) ||
                UserInfo.get().usertype.contains(UserInfo.USER_WAREHOUSE)) {
            mView.findViewById(R.id.relativeHandWork).setVisibility(View.VISIBLE);
        }
        //手工发袋管理员
        if (UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK)) {
            mView.findViewById(R.id.relativeLayoutgetuserdata).setVisibility(View.VISIBLE);
        }
        //礼品赠送人员（益币）
        if (UserInfo.get().usertype.contains(UserInfo.USER_GIFT_GIVING)) {
            mView.findViewById(R.id.relativeLayoutGiftGiving).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeLayoutgetuserdata).setVisibility(View.VISIBLE);
        }
        //拥有所有权限
        if (UserInfo.get().usertype.contains(UserInfo.USER_ALL)) {
            mView.findViewById(R.id.relativeHandWork).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeLayoutInspection).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeLayoutRecycleOrder).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeLayoutGivePoint).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeLayoutRecycleScore).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeHandWork).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeLayoutGiftGiving).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeLayoutRecycleLocal).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.relativeLayoutgetuserdata).setVisibility(View.VISIBLE);
        }

        switch (UserInfo.get().getSex()) {
            case 0: { //未设置
                imageViewSex.setVisibility(View.GONE);
                break;
            }
            case 1: { //男
                imageViewSex.setVisibility(View.VISIBLE);
                imageViewSex.setBackgroundResource(R.drawable.ic_sex_man);
                break;
            }
            case 2: { //女
                imageViewSex.setVisibility(View.VISIBLE);
                imageViewSex.setBackgroundResource(R.drawable.ic_sex_women);
                break;
            }
            default:
                break;
        }
        if (UserInfo.get().sign == 1) {
            textViewSignIn.setText("签到记录");
        } else {
            textViewSignIn.setText("点击签到");
        }

        textViewFednums.setText(UserInfo.get().freefednums + "个");


    }


    private void showPicDialog(String signpoint, String signDay, String score, String level) {
        popupWindow.setData(signpoint, signDay, score, level);
        popupWindow.showAtLocation(linearContent, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //个人信息设置
            case R.id.imageViewHeadPortrait: {
                startActivity(PersonalInfoSettingActivity.class);
                break;
            }

            //会员详情
            case R.id.relativeLevel: {
                startActivity(VipDetailActivity.class);
                break;
            }

            case R.id.textViewLeft: {
                startActivity(VipDetailActivity.class);
                break;
            }

            //签到&签到记录
            case R.id.textViewRight: {
                if (UserInfo.get().sign == 1) {
                    startActivity(new Intent(getActivity(), SignRecordActivity.class));
                } else {
                    showLoadingDialog("正在加载");
                    Api.get().Sign(getActivity(), new ApiConfig.ApiRequestListener<SignData>() {

                        @Override
                        public void onSuccess(String msg, final SignData data) {
                            dismissLoadingDialog();
                            UserInfo.get().setSign(1);
                            textViewSignIn.setText("签到记录");
                            getUserInfo();
                            mSignInADPopupWindow = (SignInADPopupWindow) SignInADPopupWindow.showOnCenter(getActivity(), linearContent, data.adInfo.adver_img, new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MobclickAgent.onEvent(mContext, "advertisement"); //友盟自定义事件统计——签到广告
                                    mSignInADPopupWindow.dismiss();

                                    Intent intent = new Intent(mContext, WebActivity.class);
                                    intent.putExtra("http_url", data.adInfo.adver_url);
                                    startActivity(intent);
                                }
                            });
//                            showPicDialog(data.signpoint, data.signdays, data.score, data.vipInfo.level_name);
                        }

                        @Override
                        public void onFailure(String msg) {
                            dismissLoadingDialog();
                        }
                    });
                }
                break;
            }

            //我的福袋
            case R.id.relativeLayoutLuckyBag: {
                startActivity(LuckyBagActivity.class);
                break;
            }

            //我的消息
            case R.id.relativeLayoutMessage: {
                Intent intent = new Intent(mContext, NewsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MESSAGE);
                break;
            }

            //发布管理
            case R.id.relativeLayoutPublish: {
                startActivity(PublishManageActivity.class);
                break;
            }

            //我的优惠卷
            case R.id.relativeLayoutMyCoupon: {
                startActivity(MyCouponActivity.class);
                break;
            }

            //我的收藏
            case R.id.relativeLayoutCollection: {
                startActivity(CollectListActivity.class);
                break;
            }

            /**我的团购*/
            case R.id.relativeLayoutMyGroupBuy: {
                Intent intent = new Intent(getActivity(), MyGroupBuyActivity.class);
                intent.putExtra("result", Session.get(getActivity()).getToken());
                intent.putExtra("data", "personal");
                startActivity(intent);
                break;
            }

            //住户实名制
            case R.id.relativeLayoutResidentBinding: {
                if ("0".equals(UserInfo.get().isbind)) {
                    CustomDialog dialog = new CustomDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("请您先在本小区设备上扫码领取环保袋，绑定设备后才能绑定住户！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                    return;
                }
                getResidentBindingInfo();
                break;
            }

            //设置
            case R.id.relativeLayoutSetting: {
                startActivity(SettingActivity.class);
                break;
            }

            //分享
            case R.id.relativeShare: {
                ShareUtil shareUtil = new ShareUtil((Activity) mContext);
                shareUtil.getShareDialog(UserInfo.get().share_url, "公益心·中国梦", R.drawable.logo, "下载益趣生活，体验美好生活");
                shareUtil.showShareDialog();
                break;
            }

            //赠送积分
            case R.id.relativeLayoutGivePoint: {
                Intent intent = new Intent(mContext, GiveIntegrationActivity.class);
                intent.putExtra("type", "integral");
                startActivity(intent);
                break;
            }

            //垃圾回收订单
            case R.id.relativeLayoutRecycleOrder: {
                startActivity(RecycleOrderActivity.class);
                break;
            }

            //回收垃圾分类得分
            case R.id.relativeLayoutRecycleScore: {
                startActivity(RecycleScoreRecordActivity.class);
                break;
            }

            //普通用户、检修、巡检
            case R.id.relativeLayoutInspection: {
                startActivity(GradeIndexActivity.class);
                break;
            }

            //手工发袋
            case R.id.relativeHandWork: {
                startActivity(HandWorkActivity.class);
                break;
            }

            //取袋记录
            case R.id.relativeLayoutFetchBagRecord: {
                startActivity(FetchBagRecordActivity.class);
                break;
            }

            //我的益币
            case R.id.relativeLayoutYCoin: {
                startActivity(YCoinsActivity.class);
                break;
            }
            //礼品赠送
            case R.id.relativeLayoutGiftGiving: {
                startActivity(GiftGivingActivity.class);
                break;
            }
            //现场回收
            case R.id.relativeLayoutRecycleLocal: {
                Intent intent = new Intent(mContext, ScanRecycleLocalActivity.class);
                startActivity(intent);
                break;
            }
            //扫户码获取信息
            case R.id.relativeLayoutgetuserdata: {
                Intent intent = new Intent(mContext, ScanRecycleLocalActivity.class);
                intent.putExtra("getUserData", "getUserData");
                startActivity(intent);
                break;
            }
        }
    }

    //住户实名制绑定新手引导
    private void residentBindingGuide() {
        FrameLayout parentView = (FrameLayout) getActivity().getWindow().getDecorView();
        final View childView = LayoutInflater.from(mContext).inflate(R.layout.layout_resident_binding_guide, null);
        RelativeLayout rlBindingAbove = (RelativeLayout) childView.findViewById(R.id.rlBindingAbove);
        ImageView imgBindingTag = (ImageView) childView.findViewById(R.id.imgBindingTag);
        parentView.setLayoutTransition(new LayoutTransition());
        parentView.addView(childView);

        int[] location = new int[2];
        mView.findViewById(R.id.relativeLayoutResidentBinding).getLocationOnScreen(location);//获取在屏幕中的绝对位置

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, location[1]);
        rlBindingAbove.setLayoutParams(params);

        imgBindingTag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Session.get(mContext).setFirstResidentBindRemind(false);
                childView.setVisibility(View.GONE);
                return true;
            }
        });

        childView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && Session.get(mContext).isFirstResidentBindRemind()) {
            residentBindingGuide();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    //获取实名制用户信息
    private void getResidentBindingInfo() {
        showLoadingDialog();
        UserApi.get().getResidentBindingInfo(mContext, new ApiConfig.ApiRequestListener<Map<String, Object>>() {
            @Override
            public void onSuccess(String msg, Map<String, Object> data) {
                dismissLoadingDialog();
                if (data == null || data.size() <= 0) {
                    showCustomToast(msg);
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
                intent.putExtra("housecode", (String) data.get("housecode"));
                intent.putExtra("housecodeX", (String) data.get("housecodeX"));
                switch ((String) data.get("usertype")) {
                    case "0": {
                        intent.setClass(mContext, ResidentBindingActivity.class);
                        break;
                    }
                    case "1": { //户主
                        if ("1".equals(data.get("bindtatus"))) { //绑定状态 0.没有申请解绑 1.绑定待审核
                            intent.putExtra("type", UndeterminedActivity.TYPE_UNBIND);
                            intent.setClass(mContext, UndeterminedActivity.class);
                            break;
                        }
                        intent.putExtra("membersList", (ArrayList<RealNameMember>) data.get("membersList"));
                        intent.setClass(mContext, BindingInfoActivity.class);
                        break;
                    }
                    case "2": { //成员
                        intent.putExtra("membersList", (ArrayList<RealNameMember>) data.get("membersList"));
                        intent.setClass(mContext, BindingInfoActivity.class);
                        break;
                    }
                    case "3": { //申请成员中
                        intent.putExtra("type", UndeterminedActivity.TYPE_APPLY);
                        intent.putExtra("status", "（户主审核中）");
                        intent.setClass(mContext, UndeterminedActivity.class);
                        break;
                    }
                    case "4": { //申诉中
                        intent.putExtra("type", UndeterminedActivity.TYPE_APPEAL);
                        intent.setClass(mContext, UndeterminedActivity.class);
                        break;
                    }
                    default:
                        break;
                }
                startActivity(intent);
//                if ("1".equals(data.get("isappeal"))) { //是否已经申诉	0.否 1.是
//                    intent.putExtra("type", UndeterminedActivity.TYPE_APPEAL);
//                    intent.setClass(mContext, UndeterminedActivity.class);
//                } else if ("1".equals(data.get("bindtatus"))) { //绑定状态 0.没有申请解绑 1.绑定待审核
//                    intent.putExtra("type", UndeterminedActivity.TYPE_UNBIND);
//                    intent.setClass(mContext, UndeterminedActivity.class);
//                } else if ("1".equals(data.get("remark"))) {  //用户申请入户状态标识 0.通过 1.审核中
//                    intent.putExtra("type", UndeterminedActivity.TYPE_APPLY);
//                    intent.setClass(mContext, UndeterminedActivity.class);
//                } else if ("1".equals(data.get("isreal"))) { //是否实名制 0.否 1.是
//                    intent.putExtra("membersList", (ArrayList<RealNameMember>) data.get("membersList"));
//                    intent.setClass(mContext, BindingInfoActivity.class);
//                } else {
//                    intent.setClass(mContext, ResidentBindingActivity.class);
//                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SETTING && Session.get(mContext).isLogin()) {
            getUserInfo();
        } else if (requestCode == REQUEST_CODE_MESSAGE) {
            imageViewBadge.setVisibility(View.GONE);
        }
    }

    public static PersonalFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_REFRESH_PAGE.equals(intent.getAction())) {
                refreshPage();
            } else if (ACTION_NETWORKING_TO_REFRESH_DATA.equals(intent.getAction())) {
                getUserInfo();
                RNCacheViewManager.init(getActivity(), new MyGroupBuyActivity("MyGroupBuy", new Bundle(), getActivity()));
            }

        }
    }

    class MessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_HAS_MESSAGE.equals(intent.getAction())) {
                imageViewBadge.setVisibility(View.VISIBLE);
            }
        }
    }
}
