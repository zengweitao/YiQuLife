package com.quanmai.yiqu.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.vo.AdInfo;
import com.quanmai.yiqu.api.vo.BannerInfo;
import com.quanmai.yiqu.api.vo.CommunityAddressInfo;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.api.vo.HomeInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.CustomDialog;
import com.quanmai.yiqu.common.widget.DraggableGridViewPager;
import com.quanmai.yiqu.ui.adapter.LocalImageHolderView;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.publish.PublishUnusedActivity;
import com.quanmai.yiqu.ui.transaction.DraggableGridViewAdapter;
import com.quanmai.yiqu.ui.transaction.TransactionSearchActivity;
import com.quanmai.yiqu.ui.unused.UnusedFragmentAdapter;
import com.quanmai.yiqu.ui.unused.UnusedListActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

/**
 * 闲置物品页面
 * A simple {@link Fragment} subclass.
 */
public class UnusedFragment extends Fragment implements View.OnClickListener, FragmentCallBack {

    private View mView;
    private PullToRefreshListView mList;
    TextView tv_left, tv_title, tv_right;
    ImageView iv_left;
    LinearLayout ll_search;
    ConvenientBanner banner;
    List<String> urls;
    LinearLayout content;
    DraggableGridViewPager draggable_grid_view_pager;
    LinearLayout lt_focus;
    DraggableGridViewAdapter mDraggableGridViewAdapter;
    TextView iv_no_data;
    ImageView imageViewBanner;

    ImageView[] imageViews = null;
    int page = 0;
    private String area_id = new String();
    private String class_id = new String();
    private String sort_type = new String();
    UnusedFragmentAdapter mAdapter;
    int count = 0;
    List<AdInfo> infos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_unused, container, false);

        initView();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("闲置"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("闲置"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
    }

    /*初始化*/
    private void initView() {
        tv_left = (TextView) mView.findViewById(R.id.tv_left);
        tv_title = (TextView) mView.findViewById(R.id.tv_title);
        tv_right = (TextView) mView.findViewById(R.id.tv_right);
        iv_left = (ImageView) mView.findViewById(R.id.iv_left);
        iv_no_data = (TextView) mView.findViewById(R.id.iv_no_data);
        mList = (PullToRefreshListView) mView.findViewById(R.id.list);
        content = (LinearLayout) mView.findViewById(R.id.content);
        tv_title.setText("闲置好货");

        urls = new ArrayList<>();
        tv_right.setOnClickListener(this);
        tv_left.setOnClickListener(this);

        //头部
        View mHeader = View.inflate(this.getActivity(), R.layout.fragment_unused_header, null);
        imageViewBanner = (ImageView) mHeader.findViewById(R.id.imageViewRule);
        imageViewBanner.setOnClickListener(this);
        ll_search = (LinearLayout) mView.findViewById(R.id.ll_search);
        banner = (ConvenientBanner) mHeader.findViewById(R.id.banner);
        ll_search.setOnClickListener(this);
        //中间icon列表
        draggable_grid_view_pager = (DraggableGridViewPager) mHeader.findViewById(R.id.draggable_grid_view_pager);
        draggable_grid_view_pager
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        if (mDraggableGridViewAdapter != null) {
                            Intent intent = new Intent(getActivity(),
                                    UnusedListActivity.class);
                            intent.putExtra("class_id",
                                    mDraggableGridViewAdapter.getItem(arg2).id);
                            intent.putExtra(
                                    "class_name",
                                    mDraggableGridViewAdapter.getItem(arg2).name);
                            startActivity(intent);
                        }
                    }
                });

        lt_focus = (LinearLayout) mHeader.findViewById(R.id.lt_focus);
        mList.getRefreshableView().addHeaderView(mHeader);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                Refresh();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                GoodsList();

            }
        });

        mAdapter = new UnusedFragmentAdapter(getActivity());
        mList.setAdapter(mAdapter);

        draggable_grid_view_pager
                .setOnPageChangeListener(new DraggableGridViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                        if (imageViews != null) {
                            for (int i = 0; i < imageViews.length; i++) {
                                imageViews[position % imageViews.length]
                                        .setBackgroundResource(R.drawable.adv_focus_radius);
                                if (position % imageViews.length != i) {
                                    imageViews[i]
                                            .setBackgroundResource(R.drawable.adv_radius);
                                }
                            }

                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        // TODO Auto-generated method stub

                    }
                });
        mList.setRefreshing();
    }

    /**
     * 下拉刷新
     */
    private void Refresh() {
        page = 0;
        GoodsApi.get().DealHomePage(getActivity(), new ApiConfig.ApiRequestListener<HomeInfo>() {

            @Override
            public void onSuccess(String msg, HomeInfo data) {
//                dismissLoadingDialog();
                mDraggableGridViewAdapter = new DraggableGridViewAdapter(
                        getActivity(), data.classList);
                draggable_grid_view_pager.setAdapter(mDraggableGridViewAdapter);
                int count = draggable_grid_view_pager.getPageCount();
                lt_focus.removeAllViews();
                imageViews = null;
                if (count > 1) {

                    imageViews = new ImageView[count];
                    if (count > 1) {
                        for (int i = 0; i < count; i++) {
                            imageViews[i] = new ImageView(getActivity());
                            imageViews[i]
                                    .setScaleType(ImageView.ScaleType.FIT_XY);

                            if (i == 0) {
                                // 默认选中第一张图片
                                imageViews[i]
                                        .setBackgroundResource(R.drawable.adv_focus_radius);
                            } else {
                                imageViews[i]
                                        .setBackgroundResource(R.drawable.adv_radius);
                            }
                            LinearLayout.LayoutParams wmParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            wmParams.setMargins(5, 0, 5, 0);
                            wmParams.width = 15;
                            wmParams.height = 15;
                            lt_focus.addView(imageViews[i], wmParams);
                        }
                    }
                }
                getAdvert();
            }

            @Override
            public void onFailure(String msg) {
                Utils.showCustomToast(getActivity(), msg);
                mList.onRefreshComplete();
            }
        });
        GoodsList();
    }

    /**
     * 获取广告列表
     */
    private void getAdvert() {
        GoodsApi.get().GetAvd(getActivity(), "2", new ApiConfig.ApiRequestListener<BannerInfo>() {
            @Override
            public void onSuccess(String msg, BannerInfo data) {
                urls.clear();
                infos = new ArrayList<AdInfo>();
                infos.addAll(data.adList);
                for (int i = 0; i < data.adList.size(); i++) {
                    urls.add(data.adList.get(i).adver_img);
                }
                if (urls.size() > 0) {
                    if (urls.size() == 1) {
                        imageViewBanner.setVisibility(View.VISIBLE);
                        banner.setVisibility(View.GONE);
                        ImageloaderUtil.displayImage(getActivity(), urls.get(0), imageViewBanner);
                        return;
                    } else {
                        imageViewBanner.setBackground(null);
                        imageViewBanner.setVisibility(View.GONE);
                        banner.setVisibility(View.VISIBLE);
                        initBanner();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                mList.onRefreshComplete();
                Utils.showCustomToast(getActivity(), msg);
            }
        });
    }

    /*导航栏*/
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
                if (infos != null && infos.size() > 0) {
                    if (!TextUtils.isEmpty(infos.get(position).adver_url)) {
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra("http_url", infos.get(position).adver_url);
                        MobclickAgent.onEvent(mContext, infos.get(position).adver_alias); //友盟自定义事件统计
                        startActivity(intent);
                    }
                }
            }
        });

    }

    /**
     * 获取商品列表
     */
    private void GoodsList() {
        GoodsApi.get().GoodsList(getActivity(), page, "", area_id, class_id,
                sort_type, new ApiConfig.ApiRequestListener<CommonList<GoodsBasic>>() {

                    @Override
                    public void onSuccess(String msg,
                                          CommonList<GoodsBasic> data) {
                        mList.onRefreshComplete();
                        if (page == 0) {
                            mAdapter.clear();
                        }
                        mAdapter.add(data);
                        if (mAdapter.getCount() == 0) {
                            iv_no_data.setText("暂无数据");
                        }
                        if (data.max_page > data.current_page) {
                            mList.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            if (mAdapter.getCount() > 0) {
                                Utils.showCustomToast(getActivity(), "已到最后");
                            }
                            mList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        page = data.current_page + 1;
                    }

                    @Override
                    public void onFailure(String msg) {
                        Utils.showCustomToast(getActivity(), msg);
                        mList.onRefreshComplete();

                        if (page == 0) {
                            mAdapter.clear();
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mAdapter.getCount() == 0) {
                            iv_no_data.setText(msg);
                        } else {

                        }
                    }
                });

    }


//    private void setOpen(boolean isOpen){
//        if (isOpen){
//            iv_left.setBackgroundResource(R.drawable.unused_triangle_up);
//        }else{
//            iv_left.setBackgroundResource(R.drawable.unused_triangle_down);
//        }
//    }

    //刷新数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            Refresh();
        }
    }

    //点击事件
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            //发布商品
            case R.id.tv_right: {
                if (UserInfo.get().getIsbind().equals("0")) {
                    CustomDialog dialog = new CustomDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("由于发布的信息仅限本小区可见，请您先在本小区设备上扫码取袋，绑定设备后再发布。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();

                    dialog.show();

                } else {
                    intent.setClass(getActivity(), PublishUnusedActivity.class);
                    startActivityForResult(intent, 101);
                }
                break;
            }
            //搜索
            case R.id.ll_search: {
                startActivity(new Intent(getActivity(), TransactionSearchActivity.class));
                break;
            }
            //banner页面跳转
            case R.id.imageViewRule: {
                if (infos != null && infos.size() > 0) {
                    if (!TextUtils.isEmpty(infos.get(0).adver_url)) {
                        intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra("http_url", infos.get(0).adver_url);
                        MobclickAgent.onEvent(mContext, infos.get(0).adver_alias); //友盟自定义事件统计
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
    public void onClick() {
        count++;
        if (count == 1) {
            if (mList != null) {
                if (!mList.isRefreshing()) {
                    mList.setRefreshing();
                }
            }

        }
    }

    public interface OnSelectListener {
        public void onSelect(CommunityAddressInfo info);

        public void onSelect(String city);
    }

}
