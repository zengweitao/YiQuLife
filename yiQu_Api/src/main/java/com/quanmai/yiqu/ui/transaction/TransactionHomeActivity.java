package com.quanmai.yiqu.ui.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.AdvertInfo;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.api.vo.HomeInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.widget.DraggableGridViewPager;
import com.quanmai.yiqu.common.widget.DraggableGridViewPager.OnPageChangeListener;
import com.quanmai.yiqu.common.widget.FocusMap;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.selectpic.SelectPicActivity;
import com.quanmai.yiqu.ui.transaction.filterview.ExpandTabView;
import com.quanmai.yiqu.ui.transaction.filterview.ExpandTabView.OnSelectListener1;
import com.quanmai.yiqu.ui.transaction.list.TransactionListActivity;
import com.quanmai.yiqu.ui.transaction.list.TransactionListAdapter0;

/**
 * 交易首页
 */
public class TransactionHomeActivity extends BaseActivity implements
        OnClickListener {
    TransactionListAdapter0 mAdapter;
    FocusMap mFocusMap;
    DraggableGridViewPager mDraggableGridViewPager;
    ViewGroup lt_focus;

    private ImageView[] imageViews = null;
    protected PullToRefreshListView mListView;
    TextView iv_no_data;
    private int page = 0;
    ExpandTabView expandTabView;
    private String area_id = new String();
    private String class_id = new String();
    private String sort_type = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_home);
        ((TextView) findViewById(R.id.tv_title)).setText("闲置物品");
        init();
    }

    private void init() {
        View iv_right = findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(this);
        View header = View.inflate(mContext,
                R.layout.activity_transaction_home_header, null);
        expandTabView = (ExpandTabView) header.findViewById(R.id.expandTabView);
        mFocusMap = (FocusMap) header.findViewById(R.id.focusmap);
        mDraggableGridViewPager = (DraggableGridViewPager) header
                .findViewById(R.id.draggable_grid_view_pager);
        mDraggableGridViewPager
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        if (mDraggableGridViewAdapter != null) {
                            Intent intent = new Intent(mContext,
                                    TransactionListActivity.class);
                            intent.putExtra("class_id",
                                    mDraggableGridViewAdapter.getItem(arg2).id);
                            intent.putExtra(
                                    "class_name",
                                    mDraggableGridViewAdapter.getItem(arg2).name);
                            startActivity(intent);
                        }
                    }
                });
        lt_focus = (ViewGroup) header.findViewById(R.id.lt_focus);
        iv_no_data = (TextView) findViewById(R.id.iv_no_data);
        mListView = (PullToRefreshListView) findViewById(R.id.list);
        mListView.getRefreshableView().addHeaderView(header);
        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

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
        mAdapter = new TransactionListAdapter0(mContext);
        mListView.setAdapter(mAdapter);
        mDraggableGridViewPager
                .setOnPageChangeListener(new OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (imageViews != null) {
                            for (int i = 0; i < imageViews.length; i++) {
                                imageViews[position % imageViews.length].setBackgroundResource(R.drawable.adv_focus_radius);
                                if (position % imageViews.length != i) {
                                    imageViews[i].setBackgroundResource(R.drawable.adv_radius);
                                }
                            }

                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        // TODO Auto-generated method stub

                    }
                });
        expandTabView.setOnSelectListener(new OnSelectListener1() {

            @Override
            public void onSelect(String id1, String id2, String id3) {
                area_id = id1;
                class_id = id2;
                sort_type = id3;
                showLoadingDialog("请稍候");
                Refresh();
            }
        });
        findViewById(R.id.tv_release).setOnClickListener(this);
        mListView.setRefreshing();
    }

    DraggableGridViewAdapter mDraggableGridViewAdapter;

    private void Refresh() {
        page = 0;
        Api.get().DealHomePage(mContext, new ApiRequestListener<HomeInfo>() {

            @Override
            public void onSuccess(String msg, HomeInfo data) {
                dismissLoadingDialog();
                mDraggableGridViewAdapter = new DraggableGridViewAdapter(
                        mContext, data.classList);
                mDraggableGridViewPager.setAdapter(mDraggableGridViewAdapter);
                int count = mDraggableGridViewPager.getPageCount();
                lt_focus.removeAllViews();
                imageViews = null;
                if (count > 1) {

                    imageViews = new ImageView[count];
                    if (count > 1) {
                        for (int i = 0; i < count; i++) {
                            imageViews[i] = new ImageView(mContext);
                            imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);

                            if (i == 0) {
                                // 默认选中第一张图片
                                imageViews[i].setBackgroundResource(R.drawable.adv_focus_radius);
                            } else {
                                imageViews[i].setBackgroundResource(R.drawable.adv_radius);
                            }
                            LinearLayout.LayoutParams wmParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            wmParams.setMargins(5, 0, 5, 0);
                            wmParams.width = 15;
                            wmParams.height = 15;
                            lt_focus.addView(imageViews[i], wmParams);
                        }
                    }
                }
                mFocusMap.setAdapter(data.adList);
                mFocusMap.setOnItemClickListener(new FocusMap.OnItemClickListener() {

                            @Override
                            public void onItemClick(int position, Object object) {

                                AdvertInfo info = (AdvertInfo) object;
                                if (!info.link_value.equals("")) {
                                    Intent intent = new Intent(mContext,
                                            WebActivity.class);
                                    intent.putExtra("http_url", info.link_value);
                                    startActivity(intent);
                                }
                            }
                        });
                mFocusMap.setAutomaticsliding(true);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();

            }
        });
        GoodsList();
    }

    void GoodsList() {
        Api.get().GoodsList(mContext, page, "", area_id, class_id,
                sort_type, new ApiRequestListener<CommonList<GoodsBasic>>() {

                    @Override
                    public void onSuccess(String msg,
                                          CommonList<GoodsBasic> data) {
                        mListView.onRefreshComplete();
                        if (page == 0) {
                            mAdapter.clear();
                        }
                        mAdapter.add(data);
                        if (mAdapter.getCount() == 0) {
                            iv_no_data.setText("暂无数据");
                        }
                        if (data.max_page > data.current_page) {
                            mListView.setMode(Mode.BOTH);
                        } else {
                            mListView.setMode(Mode.PULL_FROM_START);
                        }
                        page = data.current_page + 1;
                    }

                    @Override
                    public void onFailure(String msg) {
                        mListView.onRefreshComplete();

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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_right:
                startActivity(TransactionSearchActivity.class);
                break;
            case R.id.tv_release:
                intent.setClass(this, SelectPicActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("max", 9);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
