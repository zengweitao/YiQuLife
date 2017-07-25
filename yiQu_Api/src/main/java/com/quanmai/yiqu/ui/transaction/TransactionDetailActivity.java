package com.quanmai.yiqu.ui.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.CommentInfo;
import com.quanmai.yiqu.api.vo.GoodsInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.share.ShareActivity;
import com.quanmai.yiqu.ui.comment.CommentActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 交易详情
 */
public class TransactionDetailActivity extends ShareActivity implements
        OnClickListener {
    String goods_id;
    //	, user_id, phone,alias;
    protected PullToRefreshListView mListView;
    View iv_no_data;
    private int page = 0;
    CommentListAdapter mAdapter;
    TextView tv_description, tv_release_time, tv_phone, tv_collection_count,
            tv_comment_count, tv_accesses_count, tv_alias, tv_address,
            tv_level, tv_price,
            tv_degree;
    ImageView iv_face, iv_level;
    FrameLayout ft_img;
    int mWidth;
    ImageView iv_collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        ((TextView) findViewById(R.id.tv_title)).setText("详情");
        init();
    }

    private void init() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = (dm.widthPixels - 30) / 3; // 得到宽度
        goods_id = getIntent().getStringExtra("goods_id");
        View header = View.inflate(mContext,
                R.layout.activity_transaction_detail_header, null);
        iv_face = (ImageView) header.findViewById(R.id.iv_face);
        iv_collection = (ImageView) findViewById(R.id.iv_collection);
        iv_level = (ImageView) header.findViewById(R.id.iv_level);
        tv_degree = (TextView) header.findViewById(R.id.tv_degree);
        tv_price = (TextView) header.findViewById(R.id.tv_price);
        tv_level = (TextView) header.findViewById(R.id.tv_level);
        tv_alias = (TextView) header.findViewById(R.id.tv_alias);
        tv_address = (TextView) header.findViewById(R.id.tv_address);
        ft_img = (FrameLayout) header.findViewById(R.id.ft_img);
        header.findViewById(R.id.lt_user).setOnClickListener(this);
        findViewById(R.id.lt_comment).setOnClickListener(this);
        findViewById(R.id.lt_collection).setOnClickListener(this);
        findViewById(R.id.lt_share).setOnClickListener(this);
        iv_no_data = findViewById(R.id.iv_no_data);
        mListView = (PullToRefreshListView) findViewById(R.id.list);
        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                Refresh();

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                GoodsCommentList();

            }
        });
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (parent.getAdapter().getItem(position) != null) {
                    CommentInfo info = (CommentInfo) parent.getAdapter()
                            .getItem(position);
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    intent.putExtra("goods_id", goods_id);
                    intent.putExtra("alias", info.alias + "");
                    startActivityForResult(intent, 1);
                }
            }

        });
        mListView.getRefreshableView().addHeaderView(header);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_release_time = (TextView) findViewById(R.id.tv_release_time);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);
        tv_collection_count = (TextView) findViewById(R.id.tv_collection_count);
        tv_comment_count = (TextView) findViewById(R.id.tv_comment_count);
        tv_accesses_count = (TextView) findViewById(R.id.tv_accesses_count);
        mAdapter = new CommentListAdapter(mContext);
        mListView.setAdapter(mAdapter);
        Refresh();

    }

    private void Refresh() {
        GoodsDetail();
        page = 0;
        GoodsCommentList();
    }

    GoodsInfo mGoodsInfo;

    private void GoodsDetail() {
        Api.get().GoodsDetail(mContext, goods_id,
                new ApiRequestListener<GoodsInfo>() {
                    @Override
                    public void onSuccess(String msg, GoodsInfo data) {
                        mGoodsInfo = data;
                        tv_description.setText(data.description);
//						tv_address.setText(data.area_name);
                        if (data.degree == 10) {
                            tv_degree.setText("全新");
                        } else {
                            tv_degree.setText(data.degree + "成新");
                        }
                        if (data.type == 1) {
                            tv_price.setText("捐赠品");
                        } else {
                            tv_price.setText(Utils.getPrice(data.price));
                        }
                        if (data.goods_status == 1) {
                            iv_collection.setImageResource(R.drawable.icon_collection_1);
                        } else {
                            iv_collection.setImageResource(R.drawable.icon_collection);
                        }
                        ImageloaderUtil.displayImage(mContext, data.face, iv_face);
                        tv_alias.setText(data.alias);
                        tv_release_time.setText(data.release_time);
                        tv_collection_count.setText(data.collection_count + "");
                        tv_comment_count.setText(data.comment_count + "");
                        tv_accesses_count.setText(data.accesses_count + "次浏览");
                        tv_level.setText(data.vipInfo.level_name);
                        iv_level.setImageResource(data.vipInfo.level_img_id);
                        if (ft_img.getChildCount() == 0) {
                            for (int i = 0; i < data.img.size(); i++) {
                                ImageView imageView = new ImageView(mContext);
                                imageView.setScaleType(ScaleType.CENTER_CROP);
                                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                        mWidth, mWidth);
                                layoutParams.leftMargin = (mWidth + 15)
                                        * (i % 3);
                                layoutParams.topMargin = (mWidth + 15)
                                        * ((int) i / 3);
                                // imageView.setPadding(left, top, right,
                                // bottom)
                                imageView.setTag(i);
                                imageView.setOnClickListener(imgClickListener);
                                ft_img.addView(imageView, layoutParams);
                                ImageloaderUtil.displayImage(mContext, data.img.get(i),
                                        imageView);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String msg) {

                    }
                });

    }

    OnClickListener imgClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mGoodsInfo != null) {
                Intent intent = new Intent();
                intent.setClass(mContext, LookupBigPicActivity.class);
                intent.putExtra("current", (Integer) v.getTag());
                intent.putStringArrayListExtra("piclist", mGoodsInfo.img);
                startActivity(intent);
            }

        }
    };

    private void GoodsCommentList() {
        iv_no_data.setVisibility(View.GONE);
        Api.get().GoodsCommentList(mContext, page, goods_id,
                new ApiRequestListener<CommonList<CommentInfo>>() {

                    @Override
                    public void onSuccess(String msg,
                                          CommonList<CommentInfo> data) {
                        mListView.onRefreshComplete();
                        if (page == 0) {
                            mAdapter.clear();
                        }
                        mAdapter.add(data);
                        if (mAdapter.getCount() == 0) {
                            iv_no_data.setVisibility(View.VISIBLE);
                        } else {
                            iv_no_data.setVisibility(View.GONE);
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
                        if (mAdapter.getCount() == 0) {
                            iv_no_data.setVisibility(View.VISIBLE);
                        } else {
                            iv_no_data.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void GoodsCollection() {
//		showLoadingDialog("请稍候");
        Api.get().GoodsCollection(mContext, goods_id,
                new ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
//						dismissLoadingDialog();
//						showCustomToast(msg);
                        GoodsDetail();
                    }

                    @Override
                    public void onFailure(String msg) {
//						dismissLoadingDialog();
                        showCustomToast(msg);
                    }
                });
    }

    private void GoodsShare() {
        Api.get().GoodsShare(mContext, goods_id,
                new ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
//						showCustomToast(msg);
//						GoodsDetail();
                        try {
                            JSONObject shareObject = new JSONObject(data);
                            getShareDialog(
                                    shareObject.optString("share_link"),
                                    shareObject.optString("share_content"),
                                    UserInfo.get().prefixQiNiu
                                            + shareObject
                                            .optString("share_img"), "响应环保，一起加入闲置物品置换吧！");
                            showShareDialog();
                        } catch (JSONException e) {
                            showCustomToast("数据错误");
                            e.printStackTrace();
                        }
//						System.out.println(data);
                    }

                    @Override
                    public void onFailure(String msg) {
                        showCustomToast(msg);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.lt_comment:
                intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("goods_id", goods_id);
                startActivityForResult(intent, 1);
                break;
            case R.id.lt_collection:
                if (mGoodsInfo != null) {
                    if (mGoodsInfo.goods_status == 0) {
                        GoodsCollection();
                    }
                }
                break;
            case R.id.lt_user:
                if (mGoodsInfo != null) {
                    intent = new Intent(mContext, TransactionZoneActivity.class);
                    intent.putExtra("user_id", mGoodsInfo.user_id);
                    startActivity(intent);
                }
                break;
            case R.id.lt_share:
                GoodsShare();
                // if (user_id != null) {
                // intent = new Intent(mContext, TransactionZoneActivity.class);
                // intent.putExtra("user_id", user_id);
                // startActivity(intent);
                // }
                break;
            case R.id.tv_phone:
                if (mGoodsInfo != null) {
                    DialogUtil.getPhoneDialog(mContext, mGoodsInfo.alias, mGoodsInfo.phone).show();
                }
                // if (user_id != null) {
                // intent = new Intent(mContext, TransactionZoneActivity.class);
                // intent.putExtra("user_id", user_id);
                // startActivity(intent);
                // }
                break;

            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Refresh();
        }
    }

}
