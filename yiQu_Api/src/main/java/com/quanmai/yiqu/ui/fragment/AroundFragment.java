package com.quanmai.yiqu.ui.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.AroundApi;
import com.quanmai.yiqu.api.vo.BannerTwoInfo;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.api.vo.NewsInfo;
import com.quanmai.yiqu.api.vo.NewsListInfo;
import com.quanmai.yiqu.api.vo.ShopListInfo;
import com.quanmai.yiqu.base.BaseFragment;
import com.quanmai.yiqu.base.BaseFragmentActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.SPUtils;
import com.quanmai.yiqu.ui.Around.AllCouponActivity;
import com.quanmai.yiqu.ui.Around.AroundDetailsActivity;
import com.quanmai.yiqu.ui.Around.GroupBuyingActivity;
import com.quanmai.yiqu.ui.Around.NewsSearchActivity;
import com.quanmai.yiqu.ui.Around.ShakeActivity;
import com.quanmai.yiqu.ui.Around.ShowCouponDirectlyActivity;
import com.quanmai.yiqu.ui.Around.adapter.NewsListAdapter;
import com.quanmai.yiqu.ui.Around.adapter.ShopListAdapter;
import com.quanmai.yiqu.ui.adapter.LocalImageHolderView;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.login.LoginActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 周边优惠
 * Created by zhanjinj on 16/5/26.
 */
public class AroundFragment extends BaseFragment implements View.OnClickListener, FragmentCallBack {
    public static int MESSAGE_SHAKE = 101;
    public static String ACTION_NETWORKING_TO_REFRESH_DATA = "action_networking_to_refresh_data";

    View mView;
    PullToRefreshListView mList;
    ImageButton btnShake;
    ImageView iv_right;
    ConvenientBanner banner;
    ImageView ivBanner;
    RecyclerView recyclerViewBusiness, recyclerViewAround;
    RelativeLayout rlBusiness, rlAround;

    ShopListAdapter mBusinessAdapter, mAroundAdapter;
    NewsListAdapter mListAdapter;

    NewsListInfo mNewsListInfo;
    BannerTwoInfo mBannerTwoInfo;
    List<String> urls;
    int currentPage = 0;

    Timer mTimer;
    Dialog remindDialog;
    LocalBroadcastReceiver mLocalBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_around, container, false);
        initView();
        init();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTimer != null) mTimer.cancel();

        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MESSAGE_SHAKE);
            }
        };
        mTimer.schedule(timerTask, 1000 * 60, 1000 * 60);
        MobclickAgent.onPageStart("周边优惠"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)

        isShowAround();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimer != null) mTimer.cancel();
        MobclickAgent.onPageEnd("周边优惠"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.purge();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mLocalBroadcastReceiver);
    }

    public void initView() {
        ((TextView) mView.findViewById(R.id.tv_title)).setText("周边优惠");
        iv_right = (ImageView) mView.findViewById(R.id.iv_right);
        mList = (PullToRefreshListView) mView.findViewById(R.id.list);
        btnShake = (ImageButton) mView.findViewById(R.id.btnShake);

        //头部headerView——
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.fragment_around_header, null);
        banner = (ConvenientBanner) headerView.findViewById(R.id.banner);
        ivBanner = (ImageView) headerView.findViewById(R.id.ivBanner);
        banner.setOnClickListener(this);
        btnShake.setOnClickListener(this);

        rlBusiness = (RelativeLayout) headerView.findViewById(R.id.rlBusiness);
        rlAround = (RelativeLayout) headerView.findViewById(R.id.rlAround);
        rlBusiness.setOnClickListener(this);
        rlAround.setOnClickListener(this);

        recyclerViewBusiness = (RecyclerView) headerView.findViewById(R.id.recyclerViewBusiness);
        recyclerViewAround = (RecyclerView) headerView.findViewById(R.id.recyclerViewAround);

        //设置布局管理器，将recyclerView设置为横向布局
        LinearLayoutManager lmBusiness = new LinearLayoutManager(mContext);
        LinearLayoutManager lmAround = new LinearLayoutManager(mContext);
        lmBusiness.setOrientation(LinearLayoutManager.HORIZONTAL);
        lmAround.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerViewBusiness.setLayoutManager(lmBusiness);
        recyclerViewAround.setLayoutManager(lmAround);

        recyclerViewBusiness.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAround.setItemAnimator(new DefaultItemAnimator());

        mBusinessAdapter = new ShopListAdapter(mContext);
        mAroundAdapter = new ShopListAdapter(mContext);
        recyclerViewBusiness.setAdapter(mBusinessAdapter);
        recyclerViewAround.setAdapter(mAroundAdapter);

        mList.getRefreshableView().addHeaderView(headerView);
    }

    public void init() {
        urls = new ArrayList<>();
        mListAdapter = new NewsListAdapter(mContext);
        mList.setAdapter(mListAdapter);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getNewsInfoList();
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsInfo newsInfo = mNewsListInfo.infoList.get(position - 2);
                Intent intent;
                if ("2".equals(newsInfo.type)) {
                    intent = new Intent(mContext, GroupBuyingActivity.class);
                    intent.putExtra("newsInfoId", newsInfo.id);
                } else if (("1".equals(newsInfo.special)) && !TextUtils.isEmpty(newsInfo.linkUrl)) { //专题资讯：跳转到外链
                    intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra("http_url", newsInfo.linkUrl);
                } else { //置顶和普通资讯跳转到咨询详情页面
                    intent = new Intent(mContext, AroundDetailsActivity.class);
                    intent.putExtra("newsInfo", newsInfo.id);
                }
                startActivity(intent);
            }
        });
        mList.setRefreshing();
        iv_right.setOnClickListener(this);
        ivBanner.setOnClickListener(this);

        mLocalBroadcastReceiver = new LocalBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NETWORKING_TO_REFRESH_DATA);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mLocalBroadcastReceiver, filter);
    }

    private void refresh() {
        currentPage = 0;
        getAroundBannerData();
        getShopInfoList();
        getNewsInfoList();
    }

    /**
     * 获取顶部Banner数据
     */
    public void getAroundBannerData() {
        AroundApi.getInstance().GetAroundBanner(mContext, new ApiConfig.ApiRequestListener<BannerTwoInfo>() {
            @Override
            public void onSuccess(String msg, BannerTwoInfo data) {
                if (data == null) {
                    return;
                }
                mBannerTwoInfo = data;
                urls.clear();
                for (int i = 0; i < data.infoList.size(); i++) {
                    urls.add(data.infoList.get(i).img);
                }

                if (urls.size() == 1) {
                    banner.setVisibility(View.GONE);
                    ivBanner.setVisibility(View.VISIBLE);
                    ImageloaderUtil.displayImage(mContext, urls.get(0), ivBanner);
                } else if (urls.size() > 0) {
                    banner.setVisibility(View.VISIBLE);
                    ivBanner.setVisibility(View.GONE);
                    initBanner();
                }

            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    /**
     * 初始化顶部Banner
     */
    private void initBanner() {
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new LocalImageHolderView();
            }
        }, urls)
                .setPageIndicator(new int[]{R.drawable.icon_gray_dot, R.drawable.icon_green_dot})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(3000)
                .notifyDataSetChanged();

        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                switch (mBannerTwoInfo.infoList.get(position).type) {
                    case "1": {
                        intent = new Intent(mContext, ShowCouponDirectlyActivity.class);
                        intent.putExtra("info", mBannerTwoInfo.infoList.get(position).linkUrl);
                        intent.putExtra("type", "shake");
                        break;
                    }
                    case "2": {
                        intent = new Intent(mContext, AroundDetailsActivity.class);
                        intent.putExtra("newsInfo", mBannerTwoInfo.infoList.get(position).linkUrl);
                        break;
                    }
                    case "3": {
                        intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("http_url", mBannerTwoInfo.infoList.get(position).linkUrl);
                        break;
                    }
                }
                startActivity(intent);
            }
        });
    }

    /**
     * 获取商户信息列表
     */
    private void getShopInfoList() {
        AroundApi.getInstance().getShopInfoList(mContext, "", new ApiConfig.ApiRequestListener<ShopListInfo>() {
            @Override
            public void onSuccess(String msg, ShopListInfo shopListInfo) {
                if (shopListInfo == null) return;

                mBusinessAdapter.clean();
                mAroundAdapter.clean();

                //1、用户未登陆or未绑定社区时，只显示全网商家
                //2、用户登陆且绑定社区时，显示全网和周边商家
                if (shopListInfo.data.size() > 0) {
                    for (int i = 0; i < shopListInfo.data.size(); i++) {
                        switch (shopListInfo.data.get(i).type) {
                            case ShopListInfo.typeBusiness: {
                                mBusinessAdapter.add(shopListInfo.data.get(i).infoList);
                                if (shopListInfo.data.get(i).infoList.size() != 0) { //商家数量为空，不显示全网优惠券标题
                                    rlBusiness.setVisibility(View.VISIBLE);
                                }
                                break;
                            }
                            case ShopListInfo.typeAround: {
                                mAroundAdapter.add(shopListInfo.data.get(i).infoList);
                                if (shopListInfo.data.get(i).infoList.size() != 0 && Session.get(mContext).isLogin() && Session.get(mContext).isBind()) {
                                    rlAround.setVisibility(View.VISIBLE);
                                    recyclerViewAround.setVisibility(View.VISIBLE);
                                } else {
                                    rlAround.setVisibility(View.GONE);
                                    recyclerViewAround.setVisibility(View.GONE);
                                }
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    /**
     * 获取资讯列表信息
     */
    private void getNewsInfoList() {
        AroundApi.getInstance().getNewsInfoList(mContext, null, currentPage, new ApiConfig.ApiRequestListener<NewsListInfo>() {
            @Override
            public void onSuccess(String msg, NewsListInfo data) {
                mList.onRefreshComplete();
                if (data == null) return;

                if (currentPage == 0) {
                    mListAdapter.clean();
                    mNewsListInfo = data;
                } else {
                    mNewsListInfo.infoList.addAll(data.infoList);
                }

                mListAdapter.add(data.infoList);

                if (data.current_page < data.max_page) {
                    mList.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    if (mListAdapter.getCount() > 0) {
                        Utils.showCustomToast(getActivity(), "已到最后");
                    }
                    mList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                currentPage = data.current_page + 1;
            }

            @Override
            public void onFailure(String msg) {
                mList.onRefreshComplete();
                showCustomToast(msg);
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101: {
                    RotateAnimation rotateAnimation = new RotateAnimation(-10f, 10f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.8f);
                    rotateAnimation.setDuration(100);
                    rotateAnimation.setRepeatCount(3);
                    btnShake.startAnimation(rotateAnimation);
                    break;
                }
            }
        }
    };

    //当前Fragment是否被用户可见
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mTimer != null) mTimer.cancel();

        if (isVisibleToUser) {
            mTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(MESSAGE_SHAKE);
                }
            };
            mTimer.schedule(timerTask, 1000 * 60, 1000 * 60);
        } else if (mTimer != null) {
            mTimer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                break;
            }
            //搜索
            case R.id.iv_right: {
                startActivity(NewsSearchActivity.class);
                break;
            }
            //摇一摇特惠
            case R.id.btnShake: {
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
            //弹窗——关闭
            case R.id.rlClose: {
                if (remindDialog.isShowing()) {
                    remindDialog.dismiss();
                }
                break;
            }
            //弹窗——确定
            case R.id.btnConfirm: {
                if (((BaseFragmentActivity) getActivity()).mSession.isFirst() &&
                        !((BaseFragmentActivity) getActivity()).mSession.isLogin()) {
                    startActivity(LoginActivity.class);
                }

                if (remindDialog.isShowing()) remindDialog.dismiss();
            }
            //Banner点击
            case R.id.ivBanner: {
                Intent intent = new Intent();
                switch (mBannerTwoInfo.infoList.get(0).type) {
                    case "1": {
                        intent = new Intent(mContext, ShowCouponDirectlyActivity.class);
                        intent.putExtra("info", mBannerTwoInfo.infoList.get(0).linkUrl);
                        intent.putExtra("type", "shake");
                        break;
                    }
                    case "2": {
                        intent = new Intent(mContext, AroundDetailsActivity.class);
                        intent.putExtra("newsInfo", mBannerTwoInfo.infoList.get(0).linkUrl);
                        break;
                    }
                    case "3": {
                        intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("http_url", mBannerTwoInfo.infoList.get(0).linkUrl);
                        break;
                    }
                }
                startActivity(intent);
                break;
            }
            //全网优惠卷-查看全部
            case R.id.rlBusiness: {
                Intent intent = new Intent(mContext, AllCouponActivity.class);
                intent.putExtra("couponType", ShopListInfo.typeBusiness);
                startActivity(intent);
                break;
            }
            //周边优惠卷-查看全部
            case R.id.rlAround: {
                Intent intent = new Intent(mContext, AllCouponActivity.class);
                intent.putExtra("couponType", ShopListInfo.typeAround);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    //首页切换时的回调
    @Override
    public void onClick() {
        Log.i("isLogin--->", Session.get(mContext).isLogin() + "");
        Log.i("isBind--->", Session.get(mContext).isBind() + "");
        //第一次使用App时，未登陆提示
        if (Session.get(mContext).isFirst() &&
                !Session.get(mContext).isLogin()) {
            remindDialog = DialogUtil.getNotLoginRemindDialog(mContext, getString(R.string.not_login_remind), "立刻登陆", this);
            remindDialog.show();
        }
        //未绑定社区提醒（只提醒一次）
        else if (Session.get(mContext).isFirstBindRemind()
                && !Session.get(mContext).isBind()) {

            Session.get(mContext).setFirstBindRemind(false);
            remindDialog = DialogUtil.getNotLoginRemindDialog(mContext, getString(R.string.not_bind_remind), "好的", this);
            remindDialog.show();
        }
        //在登陆且绑定社区时，才显示周边信息
        else if (Session.get(mContext).isLogin() && Session.get(mContext).isBind()) {
            rlAround.setVisibility(View.VISIBLE);
            recyclerViewAround.setVisibility(View.VISIBLE);
        }
    }

    //是否显示周边优惠卷
    private void isShowAround() {
        Log.i("isLogin--->", Session.get(mContext).isLogin() + "");
        Log.i("isBind--->", Session.get(mContext).isBind() + "");
        //第一次使用App时，未登陆提示
        if (Session.get(mContext).isFirst() &&
                !Session.get(mContext).isLogin()) {
            remindDialog = DialogUtil.getNotLoginRemindDialog(mContext, getString(R.string.not_login_remind), "立刻登陆", this);
            remindDialog.show();
        }
        //未绑定社区提醒（只提醒一次）
        else if (Session.get(mContext).isFirstBindRemind()
                && !Session.get(mContext).isBind()) {

            Session.get(mContext).setFirstBindRemind(false);
            remindDialog = DialogUtil.getNotLoginRemindDialog(mContext, getString(R.string.not_bind_remind), "好的", this);
            remindDialog.show();
        }
        //在登陆且绑定社区时，才显示周边信息
//        else if (Session.get(mContext).isLogin() && Session.get(mContext).isBind()) {
//            rlAround.setVisibility(View.VISIBLE);
//            recyclerViewAround.setVisibility(View.VISIBLE);
//        }
    }

    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_NETWORKING_TO_REFRESH_DATA.equals(intent.getAction())) {
                refresh();

                if (Session.get(mContext).isLogin()
                        && Session.get(mContext).isBind()) {
                    rlAround.setVisibility(View.VISIBLE);
                    recyclerViewAround.setVisibility(View.VISIBLE);
                } else {
                    rlAround.setVisibility(View.GONE);
                    recyclerViewAround.setVisibility(View.GONE);
                }
            }
        }
    }
}
