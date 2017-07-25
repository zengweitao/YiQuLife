package com.quanmai.yiqu.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.MessageApi;
import com.quanmai.yiqu.api.vo.CommentInfo;
import com.quanmai.yiqu.api.vo.Noticeinfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.comment.MessageActivity;
import com.quanmai.yiqu.ui.transaction.CommentListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 */
public class NewsActivity extends BaseActivity implements OnClickListener {
    private int width, current;

    private TextView tvMessage, tvSystemNotice;
    private List<View> mViews;
    private ImageView ivCursor;

    private ViewPager view_pager;
    PagerAdapter mAdapter = new PagerAdapter() {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position), 0);
            return mViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return mViews.size();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ((TextView) findViewById(R.id.tv_title)).setText("消息");
        init();
    }

    private void init() {
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvSystemNotice = (TextView) findViewById(R.id.tvSystemNotice);
        ivCursor = (ImageView) findViewById(R.id.ivCursor);

        tvMessage.setOnClickListener(new MyOnClickListener(0));
        tvSystemNotice.setOnClickListener(new MyOnClickListener(1));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels / 2;
        ViewGroup.LayoutParams params = ivCursor.getLayoutParams();
        params.width = width;
        ivCursor.setLayoutParams(params);

        mViews = new ArrayList<View>();
        mViews.add(View.inflate(mContext, R.layout.view_news_list, null));
        mViews.add(View.inflate(mContext, R.layout.view_news_list, null));
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        view_pager.setAdapter(mAdapter);
        view_pager.setOnPageChangeListener(new MyOnPageChangeListener());

        intMessage(mViews.get(0));
        intNotice(mViews.get(1));

        Intent intent = this.getIntent();
        view_pager.setCurrentItem(intent.getIntExtra("currentItem", 0));
    }

    private PullToRefreshListView mMsgListView;
    private CommentListAdapter mMsgListAdapter;
    TextView tvMsgNoData;
    private int mMessagePage = 0;

    void intMessage(View view) {
        mMsgListView = (PullToRefreshListView) view.findViewById(R.id.list);
        mMsgListAdapter = new CommentListAdapter(this);

        LinearLayout linearLayoutNoData = (LinearLayout) view.findViewById(R.id.linearLayoutNoData);
        tvMsgNoData = (TextView) view.findViewById(R.id.textViewNoData);
        mMsgListView.setEmptyView(linearLayoutNoData);
        mMsgListView.setAdapter(mMsgListAdapter);

        mMsgListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                MessageRefresh();

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                MessageList();
            }
        });
        mMsgListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (parent.getAdapter().getItem(position) != null) {
                    CommentInfo info = (CommentInfo) parent.getAdapter()
                            .getItem(position);
                    Intent intent = new Intent(mContext, MessageActivity.class);
                    intent.putExtra("user_id", info.user_id);
                    intent.putExtra("alias", info.alias + "");
                    startActivityForResult(intent, 1);
                }
            }

        });

        showLoadingDialog();
        MessageRefresh();
    }

    private void MessageRefresh() {
        mMessagePage = 0;
        MessageList();
    }

    private void MessageList() {
        MessageApi.get().MessageList(mContext, mMessagePage,
                new ApiRequestListener<CommonList<CommentInfo>>() {
                    @Override
                    public void onSuccess(String msg, CommonList<CommentInfo> data) {
                        dismissLoadingDialog();

                        mMsgListView.onRefreshComplete();
                        if (mMessagePage == 0) {
                            mMsgListAdapter.clear();
                        }
                        mMsgListAdapter.add(data);
                        if (mMsgListAdapter.getCount() == 0) {
                            tvMsgNoData.setText("暂无数据");
                        }
                        if (data.max_page > data.current_page) {
                            mMsgListView.setMode(Mode.BOTH);
                        } else {
                            mMsgListView.setMode(Mode.PULL_FROM_START);
                        }
                        mMessagePage = data.current_page + 1;

                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();

                        mMsgListView.onRefreshComplete();
                        if (mMsgListAdapter.getCount() == 0) {
                            tvMsgNoData.setText(msg);
                        } else {
                            showCustomToast(msg);
                        }
                    }
                });
    }

    private PullToRefreshListView mNoticeListView;
    private NoticeListAdapter mNoticeListAdapter;
    TextView tvNoticeNoData;
    private int mNoticePage = 0;

    void intNotice(View view) {
        mNoticeListView = (PullToRefreshListView) view.findViewById(R.id.list);
        mNoticeListAdapter = new NoticeListAdapter(this);
        mNoticeListView.getRefreshableView().setAdapter(mNoticeListAdapter);

        mNoticeListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                NoticeRefresh();

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                NoticeList();
            }
        });
        LinearLayout linearLayoutNoData = (LinearLayout) view.findViewById(R.id.linearLayoutNoData);
        tvNoticeNoData = (TextView) view.findViewById(R.id.textViewNoData);
        mNoticeListView.setEmptyView(linearLayoutNoData);

        mNoticeListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (parent.getAdapter().getItem(position) != null) {
                    Noticeinfo info = (Noticeinfo) parent.getAdapter()
                            .getItem(position);
                    Intent intent = new Intent(mContext, WebActivity.class);
                    if (!TextUtils.isEmpty(info.notice_link)) {
                        intent.putExtra("http_url", info.notice_link);
                        startActivity(intent);
                    }
                }
            }

        });


        NoticeRefresh();
    }

    private void NoticeRefresh() {
        mNoticePage = 0;
        NoticeList();
    }

    private void NoticeList() {
        MessageApi.get().NoticeList(mContext, mNoticePage,
                new ApiRequestListener<CommonList<Noticeinfo>>() {
                    @Override
                    public void onSuccess(String msg, CommonList<Noticeinfo> data) {
                        dismissLoadingDialog();

                        mNoticeListView.onRefreshComplete();
                        if (mNoticePage == 0) {
                            mNoticeListAdapter.clear();
                        }
                        mNoticeListAdapter.add(data);
                        if (mNoticeListAdapter.getCount() == 0) {
                            tvNoticeNoData.setText("暂无数据");
                        }
                        if (data.max_page > data.current_page) {
                            mNoticeListView.setMode(Mode.BOTH);
                        } else {
                            mNoticeListView.setMode(Mode.PULL_FROM_START);
                        }
                        mNoticePage = data.current_page + 1;

                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();

                        mNoticeListView.onRefreshComplete();
                        if (mNoticeListAdapter.getCount() == 0) {
                            tvNoticeNoData.setText(msg);
                        } else {
                            showCustomToast(msg);
                        }
                    }
                });
    }


    public class MyOnClickListener implements OnClickListener {
        private int index;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            view_pager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements OnPageChangeListener {
        Animation animation = null;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    if (current == 1) {
                        animation = new TranslateAnimation(width, 0, 0, 0);
                    }
                    break;

                case 1:
                    if (current == 0) {
                        animation = new TranslateAnimation(0, width, 0, 0);
                    }
                    break;
            }

            current = arg0;

            if (animation != null) {
                animation.setFillAfter(true); // 图片停在动画结束位置
                animation.setDuration(300);
                ivCursor.startAnimation(animation);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
