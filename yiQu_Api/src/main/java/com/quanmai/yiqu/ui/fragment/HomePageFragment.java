package com.quanmai.yiqu.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.AroundApi;
import com.quanmai.yiqu.api.BookingApi;
import com.quanmai.yiqu.api.CheckInApi;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.MessageApi;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.UserInfoApi;
import com.quanmai.yiqu.api.vo.AdInfo;
import com.quanmai.yiqu.api.vo.AdvertInfo;
import com.quanmai.yiqu.api.vo.BannerInfo;
import com.quanmai.yiqu.api.vo.BookingDetailInfo;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.api.vo.QuestionInfo;
import com.quanmai.yiqu.api.vo.ServicesInfo;
import com.quanmai.yiqu.api.vo.SignData;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseFragment;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.SPUtils;
import com.quanmai.yiqu.common.view.MyGridView;
import com.quanmai.yiqu.common.widget.CustomDialog;
import com.quanmai.yiqu.common.widget.DraggableGridViewPager;
import com.quanmai.yiqu.common.widget.SignInADPopupWindow;
import com.quanmai.yiqu.common.widget.SignInPopupWindow;
import com.quanmai.yiqu.ui.Around.AroundActivity;
import com.quanmai.yiqu.ui.Around.ShakeActivity;
import com.quanmai.yiqu.ui.adapter.GridViewServicesAdapter;
import com.quanmai.yiqu.ui.adapter.HomePageImageHolder;
import com.quanmai.yiqu.ui.booking.BookingActivity;
import com.quanmai.yiqu.ui.classifigarbage.ClassifyGameCompetitionActivity;
import com.quanmai.yiqu.ui.classifigarbage.GarbageSortingActivity;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.community.CommunityServiceActivity;
import com.quanmai.yiqu.ui.fuli.GainIntegralActivity;
import com.quanmai.yiqu.ui.fuli.LuckyBagActivity;
import com.quanmai.yiqu.ui.grade.PersonalGradesActivity;
import com.quanmai.yiqu.ui.login.LoginActivity;
import com.quanmai.yiqu.ui.mys.record.SignRecordActivity;
import com.quanmai.yiqu.ui.recycle.RecycleOrderActivity;
import com.quanmai.yiqu.ui.transaction.DraggableGridViewAdapter;
import com.quanmai.yiqu.ui.unused.UnusedActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 首页
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private View mView;
    private Context mContext;
    private ImageView imageViewBanner;
    private ConvenientBanner banner;  //导航栏
    private List<String> urls;        //导航栏跳转链接
    private List<AdInfo> mAdInfos;
    private DraggableGridViewPager draggable_grid_view_pager; //中部icon菜单
    private DraggableGridViewAdapter mDraggableGridViewAdapter; //中部菜单适配器
    private MyGridView gridViewServices;
    private LinearLayout linearParent;
    private PullToRefreshScrollView scrollView;
    private LinearLayout llPoint;

    private SignInPopupWindow popupWindow;
    private SignInADPopupWindow mSignInADPopupWindow; //签到成功弹窗（广告）
    private Dialog gainIntegralDialog; //答题赚积分--》答题已答完弹窗
    boolean flag = false;
    private String orderId;
    private List<AdvertInfo> mAdvertInfos = new ArrayList<>(); //中部icon菜单列表
    private int[] mAdvertIconList;
    private String[] mAdvertNameList;
    private ImageView[] imgPoints = null;
    private GridViewServicesAdapter mServicesAdapter;

    private static final int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "DefaultValue";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(BUNDLE_TITLE);
        }
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        initView();
        init();
        return mView;
    }

    private void initView() {
        ((TextView) mView.findViewById(R.id.tv_title)).setText("益趣生活");
        mView.findViewById(R.id.iv_back).setVisibility(View.GONE);

        scrollView = (PullToRefreshScrollView) mView.findViewById(R.id.scrollView);
        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getAdvert();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        banner = (ConvenientBanner) mView.findViewById(R.id.banner);
        imageViewBanner = (ImageView) mView.findViewById(R.id.imageViewBanner);
        draggable_grid_view_pager = (DraggableGridViewPager) mView.findViewById(R.id.draggable_grid_view_pager);
        llPoint = (LinearLayout) mView.findViewById(R.id.llPoint);
    }

    protected void init() {
        mAdvertIconList = new int[]{R.drawable.ic_garbage_classify, R.drawable.ic_recycle, R.drawable.ic_un_used,
                R.drawable.ic_community_services
                , R.drawable.ic_day_score, R.drawable.ic_gain_integral, R.drawable.ic_shake, R.drawable.ic_around};
        mAdvertNameList = new String[]{"垃圾分类", "废品回收", "闲置好货", "社区服务",
                "每日一袋", "答题赚积分", "摇一摇", "周边优惠"};
        initGridView(draggable_grid_view_pager);
        initPoints(llPoint, 0);

        gridViewServices = (MyGridView) mView.findViewById(R.id.gridViewServices);

        urls = new ArrayList<>();
        mAdInfos = new ArrayList<>();
        imageViewBanner.setOnClickListener(this);

        popupWindow = new SignInPopupWindow(getActivity());
        linearParent = (LinearLayout) mView.findViewById(R.id.linearParent);

        getAdvert();
        getCommunityService();
        autoSignIn();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.startTurning(3000);
        }
        MobclickAgent.onPageStart("首页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.stopTurning();
        }
        MobclickAgent.onPageEnd("首页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
    }

    /**
     * 初始化导航栏
     */
    private void initBanner() {
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new HomePageImageHolder(urls, getActivity());
            }
        }, urls)
                .setPageIndicator(new int[]{R.drawable.icon_gray_dot, R.drawable.icon_green_dot})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .notifyDataSetChanged();

        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mAdInfos != null && mAdInfos.size() > 0) {
                    if (!TextUtils.isEmpty(mAdInfos.get(position).adver_url)) {
                        if (mAdInfos.get(position).adver_url.equals("gameentry.eacheart")) {
                            if (!Session.get(getActivity()).isLogin()) {
                                Utils.showCustomToast(mContext, "未登录，请登录后再试");
                                startActivity(new Intent(mContext, LoginActivity.class));
                                return;
                            }
                            Intent intent = new Intent(getActivity(), ClassifyGameCompetitionActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            MobclickAgent.onEvent(mContext, mAdInfos.get(position).adver_alias); //友盟自定义事件统计
                            intent.putExtra("http_url", mAdInfos.get(position).adver_url);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    /**
     * 初始化中间Icon列表
     */
    private void initGridView(DraggableGridViewPager gridViewPager) {
        gridViewPager.setOnItemClickListener(this);
        for (int i = 0; i < mAdvertIconList.length; i++) {
            AdvertInfo advertInfo = new AdvertInfo();
            advertInfo.picId = mAdvertIconList[i];
            if (i < mAdvertNameList.length) {
                advertInfo.name = mAdvertNameList[i];
            }
            mAdvertInfos.add(advertInfo);
        }
        mDraggableGridViewAdapter = new DraggableGridViewAdapter(getActivity(), mAdvertInfos);
        draggable_grid_view_pager.setAdapter(mDraggableGridViewAdapter);
        draggable_grid_view_pager.setOnPageChangeListener(new DraggableGridViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (imgPoints != null) {
                    for (int i = 0; i < imgPoints.length; i++) {
                        imgPoints[position % imgPoints.length].setBackgroundResource(R.drawable.ic_pointer_checked);
                        if (position % imgPoints.length != i) {
                            imgPoints[i].setBackgroundResource(R.drawable.ic_pointer_unchecked);
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //初始化中间Icon列表指示器
    private void initPoints(LinearLayout llPoint, int num) {
        if (llPoint == null || num == 0) {
            return;
        }
        imgPoints = new ImageView[num];
        for (int i = 0; i < imgPoints.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.dp2px(mContext, 5), Utils.dp2px(mContext, 5));
            params.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.ic_pointer_checked);
            } else {
                imageView.setBackgroundResource(R.drawable.ic_pointer_unchecked);
            }
            imgPoints[i] = imageView;
            llPoint.addView(imageView);
        }
    }

    /**
     * 每日启动app自动签到
     */
    private void autoSignIn() {
        if (!Session.get(getActivity()).isLogin()) { //未登录
            return;
        }

        if (!(UserInfo.get().sign == 1)) { //用户未签到
            UserInfoApi.get().getUserHome(getActivity(), new ApiConfig.ApiRequestListener<UserInfo>() {
                @Override
                public void onSuccess(String msg, UserInfo data) {
                    CheckInApi.get().Sign(getActivity(), new ApiConfig.ApiRequestListener<SignData>() {
                        @Override
                        public void onSuccess(String msg, final SignData data) {
                            UserInfo.get().setSign(1);
                            SignInADPopupWindow.showOnCenter(getActivity(), linearParent, data.adInfo.adver_img, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, WebActivity.class);
                                    intent.putExtra("http_url", data.adInfo.adver_url);
                                    startActivity(intent);

                                    MobclickAgent.onEvent(mContext, "advertisement"); //友盟自定义事件统计——签到广告
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

                @Override
                public void onFailure(String msg) {
                    dismissLoadingDialog();
                    Utils.showCustomToast(getActivity(), msg);
                }
            });
        }
    }

    /**
     * 获取个人信息
     */
    private void getUserInfo() {
        showLoadingDialog("请稍候");
        UserInfoApi.get().getUserHome(getActivity(), new ApiConfig.ApiRequestListener<UserInfo>() {
            @Override
            public void onSuccess(String msg, UserInfo data) {
                dismissLoadingDialog();
                if (data.get().sign == 1) {
                    startActivity(new Intent(getActivity(), SignRecordActivity.class));
                } else {
                    showLoadingDialog("请稍候");
                    CheckInApi.get().Sign(getActivity(), new ApiConfig.ApiRequestListener<SignData>() {

                        @Override
                        public void onSuccess(String msg, final SignData data) {
                            dismissLoadingDialog();
                            UserInfo.get().setSign(1);
//                            showPicDialog(data.signpoint, data.signdays, data.score, data.vipInfo.level_name);
                            mSignInADPopupWindow = (SignInADPopupWindow) SignInADPopupWindow.showOnCenter(getActivity(), linearParent, data.adInfo.adver_img, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mSignInADPopupWindow.dismiss();
                                    MobclickAgent.onEvent(mContext, "advertisement"); //友盟自定义事件统计——签到广告
                                    Intent intent = new Intent(mContext, WebActivity.class);
                                    intent.putExtra("http_url", data.adInfo.adver_url);
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onFailure(String msg) {
                            dismissLoadingDialog();
                            Utils.showCustomToast(getActivity(), msg);
                        }
                    });
                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                Utils.showCustomToast(getActivity(), msg);
            }
        });
    }


    /**
     * 获取广告列表
     */
    private void getAdvert() {
        GoodsApi.get().GetAvd(getActivity(), "1", new ApiConfig.ApiRequestListener<BannerInfo>() {
            @Override
            public void onSuccess(String msg, BannerInfo data) {
                urls.clear();
                mAdInfos.clear();
                mAdInfos.addAll(data.adList);
                for (int i = 0; i < data.adList.size(); i++) {
                    urls.add(data.adList.get(i).adver_img);
                }
                if (urls.size() > 0) {
                    if (urls.size() == 1) {
                        imageViewBanner.setVisibility(View.VISIBLE);
                        banner.setVisibility(View.GONE);
                        ImageloaderUtil.displayImage(getActivity(), urls.get(0), imageViewBanner);
                    } else {
                        imageViewBanner.setVisibility(View.GONE);
                        banner.setVisibility(View.VISIBLE);
                        initBanner();
                    }
                }
                getCommunityService();
            }

            @Override
            public void onFailure(String msg) {
                Utils.showCustomToast(getActivity(), msg);
                getCommunityService();
            }
        });
    }

    /**
     * 获取社区公共便民服务列表
     */
    private void getCommunityService() {
        UserApi.get().GetCommunityService(mContext, "0", new ApiConfig.ApiRequestListener<CommonList<ServicesInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<ServicesInfo> data) {
                if (data == null || data.size() == 0) return;
                mServicesAdapter = new GridViewServicesAdapter(mContext, data);
                gridViewServices.setAdapter(mServicesAdapter);
                scrollView.onRefreshComplete();
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
                scrollView.onRefreshComplete();
            }
        });
    }

    /**
     * 查询预约列表
     */
    private void itHasBookingRecord(final String status) {
        showLoadingDialog("请稍候");
        flag = false;
        BookingApi.get().SearchBookingList(mContext, 0, status, new ApiConfig.ApiRequestListener<CommonList<BookingDetailInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<BookingDetailInfo> data) {
                dismissLoadingDialog();
                flag = true;
                /*if (data != null && data.size() > 0) {
                    orderId = data.get(0).id;
                    if (data.get(0).statue.equals("initial") || data.get(0).statue.equals("verified")) {
//                        Intent intent = new Intent(getActivity(), BookingStatusRecordActivity.class);
                        Intent intent = new Intent(getActivity(), BookingStatusActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                        return;
                    } else if (data.get(0).statue.equals("recycle")) {
                        Intent intent = new Intent(mContext, RecycleSuccessActivity.class);
                        intent.putExtra("orderId", data.get(0).id);
                        intent.putExtra("point", data.get(0).point);
                        startActivity(intent);
                        return;
                    }
                } else {
                    if (flag) {
                        startActivity(new Intent(getActivity(), BookingActivity.class));
                    }
                }*/
                startActivity(new Intent(getActivity(), BookingActivity.class));
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                flag = true;
                return;
            }
        });
    }

    /**
     * 获取广告答题信息
     */
    private void getQuestion() {
        MessageApi.get().getAdAnswerQuestionInfo(mContext, new ApiConfig.ApiRequestListener<QuestionInfo>() {
            @Override
            public void onSuccess(String isexist, QuestionInfo data) {
                dismissLoadingDialog();
                if ("0".equals(isexist)) {
                    showCustomToast("今天答题已完");
                    return;
                }

                Intent intent = new Intent(mContext, GainIntegralActivity.class);
                intent.putExtra("questionInfo", data);
                startActivity(intent);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                gainIntegralDialog = DialogUtil.getPopupConfirmDialog2(mContext, msg, "知识分类答题", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btnConfirm: {
                                startActivity(LuckyBagActivity.class);
                                gainIntegralDialog.dismiss();
                                break;
                            }
                            case R.id.btnCancel: {
                                gainIntegralDialog.dismiss();
                                break;
                            }
                        }
                    }
                });
                gainIntegralDialog.show();
            }
        });
    }

    //获取摇一摇次数和时间
    private void getShakeTimes() {
        AroundApi.getInstance().GetSuperiseByShake(getActivity(), "1", new ApiConfig.ApiRequestListener<CouponInfo>() {
            @Override
            public void onSuccess(String msg, CouponInfo data) {
                Intent intent = new Intent(getActivity(), ShakeActivity.class);
                int lastTime = Integer.parseInt(SPUtils.get(getActivity(), "maxTimes", 0) + "") - Integer.parseInt(SPUtils.get(getActivity(), "fetchTimes", 0) + "");
                intent.putExtra("lastTimes", lastTime);
                startActivity(intent);
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //banner跳转
            case R.id.imageViewBanner: {
                if (mAdInfos != null && mAdInfos.size() > 0) {
                    if (!TextUtils.isEmpty(mAdInfos.get(0).adver_url)) {
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        MobclickAgent.onEvent(mContext, mAdInfos.get(0).adver_alias); //友盟自定义事件统计
                        intent.putExtra("http_url", mAdInfos.get(0).adver_url);
                        startActivity(intent);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mDraggableGridViewAdapter != null) {
            switch (position) {
                case 0: { //垃圾分类
                    Intent intent = new Intent(mContext, GarbageSortingActivity.class);
                    mContext.startActivity(intent);
                    break;
                }
                case 1: {
                    /**
                     * 垃圾回收模块
                     * 		逻辑判断
                     * 			1.是否登陆---LoginActivity
                     * 		    2.是否回收人员---跳转到回收订单页面
                     * 			3.是否绑定机器---弹窗提示
                     * 			4.是否存在“预约中”的状态---BookingStatusRecordActivity
                     * 			5.是否已经预约过---BookingSecondActivity/AddRecycleAddressActivity
                     * 			6.是否存在“已回收”的状态---RecycleSuccessActivity
                     * 			7.预约界面---BookingActivity
                     * */
                    if (!Session.get(getActivity()).isLogin()) {
                        Utils.showCustomToast(mContext, "未登录，请登录后再试");
                        startActivity(new Intent(mContext, LoginActivity.class));
                        return;
                    }

                    /*if (UserInfo.get().usertype.contains(UserInfo.USER_RECYCLE)) {
                        startActivity(new Intent(getActivity(), RecycleOrderActivity.class));
                        return;
                    }*/

                    if (UserInfo.get().getIsbind().equals("0")) {
                        CustomDialog dialog = new CustomDialog.Builder(getActivity())
                                .setTitle("提示")
                                .setMessage("请先绑定小区设备！")
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

                    if (!UserInfo.get().getIsbind().equals("2")) {
                        CustomDialog dialog = new CustomDialog.Builder(getActivity())
                                .setTitle("提示")
                                .setMessage("该小区未开通废品回收功能！")
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

                    itHasBookingRecord("initial,verified,recycle");
                    break;
                }
                case 2: { //闲置好货
                    if (Session.get(mContext).isLogin()) {
                        Intent intent = new Intent(mContext, UnusedActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        Utils.showCustomToast(mContext, "未登录，请登录后再试");
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }

                    break;
                }

                case 3: { //社区服务
                    startActivity(new Intent(getActivity(), CommunityServiceActivity.class));
                    break;
                }
                case 4: { //趣味游戏  2017.04.20 功能改为“每日一袋”

                    if (Session.get(getActivity()).isLogin()) {
                        if (UserInfo.get().getIsbind().equals("0")) {
                            CustomDialog dialog = new CustomDialog.Builder(getActivity())
                                    .setTitle("提示")
                                    .setMessage("请先绑定小区设备！")
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

                        if (UserInfo.get().isScored.equals("0")) {
                            showCustomToast("抱歉！您所在的小区暂未开通自评分功能！");
                            return;
                        }

                        CustomDialog dialog = new CustomDialog.Builder(mContext)
                                .setTitle("个人评分细则")
                                .setMessage(getString(R.string.str_personal_grade))
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(mContext, PersonalGradesActivity.class));
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create2();
                        dialog.show();

                        //startActivity(ClassifyGameActivity.class);
                    } else {
                        Utils.showCustomToast(getActivity(), "未登录，请登录后再试");
                        startActivity(LoginActivity.class);
                    }
                    break;
                }
                case 5: { //答题得积分
                    MobclickAgent.onEvent(mContext, "advertisementAnswer"); //友盟自定义事件统计——广告答题
                    if (Calendar.getInstance().getTimeInMillis() - lastClickTime < MIN_CLICK_DELAY_TIME) {
                        return;
                    }
                    lastClickTime = Calendar.getInstance().getTimeInMillis();

                    if (Session.get(getActivity()).isLogin()) {
                        getQuestion();
                    } else {
                        showCustomToast("未登录，请登录后再试");
                        startActivity(LoginActivity.class);
                    }
                    break;
                }
                case 6: { //摇一摇
                    if (Utils.isNetworkAvailable(getActivity())) {
                        if (Session.get(getActivity()).isLogin()) {
                            getShakeTimes();
                        } else {
                            startActivity(LoginActivity.class);
                            showCustomToast("请登录后再操作");
                        }
                    } else {
                        showCustomToast("没有网络连接");
                    }
                    break;
                }
                case 7: { //周边优惠
                    startActivity(AroundActivity.class);
                    break;
                }

            }
        }
    }

    public static HomePageFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
