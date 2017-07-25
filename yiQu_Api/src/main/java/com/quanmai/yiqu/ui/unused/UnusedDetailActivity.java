package com.quanmai.yiqu.ui.unused;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.CollectApi;
import com.quanmai.yiqu.api.CommentApi;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.vo.CommentInfo;
import com.quanmai.yiqu.api.vo.GoodsInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.share.ShareActivity;
import com.quanmai.yiqu.ui.comment.CommentActivity;
import com.quanmai.yiqu.ui.comment.MessageActivity;
import com.quanmai.yiqu.ui.transaction.CommentListAdapter;
import com.quanmai.yiqu.ui.transaction.LookupBigPicActivity;
import com.quanmai.yiqu.ui.transaction.TransactionZoneActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class UnusedDetailActivity extends ShareActivity implements View.OnClickListener {

    TextView tv_title;
    PullToRefreshListView mList;
    LinearLayout lt_collection, lt_comment, lt_share;
    TextView textViewPrivateLetter;
    TextView tv_collection_count, tv_comment_count;
    LinearLayout linear_no_data;
    //headView的组件
    ImageView imageViewHeadPortrait;
    TextView textViewName;
    TextView textViewTime;
    TextView textViewCommentCount;
    Button buttonLevel;
    TextView textViewMoney, textViewDegree, textViewAddress;
    TextView textViewDesciption;
    LinearLayout linearLayoutContent;

    String goods_id;
    private int page = 0;
    GoodsInfo mGoodsInfo;
    CommentListAdapter mAdapter;
    int collectionCount = 0;
    Dialog dialog;
    Dialog longClickDialog;
    String selectCommentId = "";
    String goodsId = "";
    String user_id;
    ImageView iv_collection;
    ImageView iv_selected;
    Bitmap bitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused_detail);

        goods_id = getIntent().getStringExtra("goods_id");
        init();
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("详情");
        mList = (PullToRefreshListView) findViewById(R.id.list);
        lt_collection = (LinearLayout) findViewById(R.id.lt_collection);
        lt_comment = (LinearLayout) findViewById(R.id.lt_comment);
        lt_share = (LinearLayout) findViewById(R.id.lt_share);
        textViewPrivateLetter = (TextView) findViewById(R.id.textViewPrivateLetter);
        tv_collection_count = (TextView) findViewById(R.id.tv_collection_count);
        tv_comment_count = (TextView) findViewById(R.id.tv_comment_count);
        linear_no_data = (LinearLayout) findViewById(R.id.linear_no_data);
        iv_collection = (ImageView) findViewById(R.id.iv_collection);
        lt_collection.setOnClickListener(this);
        lt_comment.setOnClickListener(this);
        lt_share.setOnClickListener(this);
        textViewPrivateLetter.setOnClickListener(this);
        //头部view
        View header = View.inflate(mContext,
                R.layout.unused_detail_header, null);
        imageViewHeadPortrait = (ImageView) header.findViewById(R.id.imageViewHeadPortrait);
        imageViewHeadPortrait.setOnClickListener(this);
        textViewName = (TextView) header.findViewById(R.id.textViewName);
        textViewTime = (TextView) header.findViewById(R.id.textViewTime);
        textViewCommentCount = (TextView) header.findViewById(R.id.textViewCommentCount);
        buttonLevel = (Button) header.findViewById(R.id.tvLevel);
        textViewMoney = (TextView) header.findViewById(R.id.textViewMoney);
        textViewDegree = (TextView) header.findViewById(R.id.textViewDegree);
        textViewAddress = (TextView) header.findViewById(R.id.textViewAddress);
        textViewDesciption = (TextView) header.findViewById(R.id.textViewDesciption);
        linearLayoutContent = (LinearLayout) header.findViewById(R.id.linearLayoutContent);
        mList.getRefreshableView().addHeaderView(header);

        mAdapter = new CommentListAdapter(mContext);
        mList.setAdapter(mAdapter);
        showLoadingDialog("请稍候");
        Refresh();

        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

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

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (parent.getAdapter().getItem(position) != null) {
                    CommentInfo info = (CommentInfo) parent.getAdapter()
                            .getItem(position);
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    intent.putExtra("goods_id", goods_id);
                    intent.putExtra("customerid", mGoodsInfo.user_id);
                    intent.putExtra("goods_name", mGoodsInfo.name);
//                    intent.putExtra("comment", "回复@" + info.alias + "：");
                    intent.putExtra("alias", info.alias);
                    startActivityForResult(intent, 1);
                }
            }

        });

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (UserInfo.get() != null) {
                    Log.e("mark", UserInfo.get().userid + "---");
                    if (!TextUtils.isEmpty(UserInfo.get().userid)) {
                        if (mGoodsInfo != null && !TextUtils.isEmpty(mGoodsInfo.user_id)) {
                            if (mGoodsInfo.user_id.equals(UserInfo.get().userid)) {
                                selectCommentId = mAdapter.getItem(i - 2).id;
                                user_id = mAdapter.getItem(i - 2).user_id;
                                showDialog();
                            } else if (UserInfo.get().userid.equals(mAdapter.getItem(i - 2).user_id)) {
                                selectCommentId = mAdapter.getItem(i - 2).id;
                                user_id = "";
                                showDialog();
                            }
                        }
                    }
                }

                return false;
            }
        });
    }

    /**
     * 获取评论
     */
    private void GoodsCommentList() {
        CommentApi.get().GoodsCommentList(mContext, page, goods_id, new ApiConfig.ApiRequestListener<CommonList<CommentInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<CommentInfo> data) {
                dismissLoadingDialog();
                mList.onRefreshComplete();
                if (page == 0) {
                    mAdapter.clear();
                }
                mAdapter.add(data);
                if (data.total > 0) {
                    tv_comment_count.setText(data.total + "");
                }
                if (data.max_page > data.current_page) {
                    mList.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    if (mAdapter.getCount() > 0) {
                        Utils.showCustomToast(UnusedDetailActivity.this, "没有更多了~");
                    }
                    mList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                page = data.current_page + 1;
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
                dismissLoadingDialog();
                mList.onRefreshComplete();
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void Refresh() {
        GoodsDetail();
        page = 0;
//        GoodsCommentList();
    }

    /**
     * 获取物品详情
     */
    private void GoodsDetail() {
        GoodsApi.get().GoodsDetail(mContext, goods_id, new ApiConfig.ApiRequestListener<GoodsInfo>() {
            @Override
            public void onSuccess(String msg, GoodsInfo data) {
                dismissLoadingDialog();
                mGoodsInfo = data;
                goodsId = data.id;
                if (!TextUtils.isEmpty(data.alias)) {
                    textViewName.setText(data.alias);
                } else {
                    textViewName.setText("未填写");
                }
                textViewAddress.setText(data.area_name);
                buttonLevel.setText(data.vipInfo.level_name);
                if (data.accesses_count > 0) {
                    GoodsCommentList();
                    textViewCommentCount.setText(data.accesses_count + "浏览");
                } else {
                    mList.onRefreshComplete();
//                    textViewCommentCount.setText("暂无评论");
                }
                textViewTime.setText("发布时间：" + data.release_time);
                if (!TextUtils.isEmpty(data.face)) {
                    ImageloaderUtil.displayImage(mContext, data.face, imageViewHeadPortrait);
                } else {
                    Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_header);
                    imageViewHeadPortrait.setImageBitmap(mBitmap);
                }

                if (data.type == 1) {
                    textViewMoney.setText("捐赠品");
                } else {
                    textViewMoney.setText(Utils.getPrice(data.price));
                }

                if (data.degree == 10) {
                    textViewDegree.setText("全新");
                } else {
                    textViewDegree.setText(data.degree + "成新");
                }

                if (data.goods_status == 0) {
                    iv_collection.setBackgroundResource(R.drawable.icon_love);
                } else if (data.goods_status == 1) {
                    iv_collection.setBackgroundResource(R.drawable.icon_heart);
                }
//                textViewAddress.setText();
                textViewDesciption.setText(data.description);
                collectionCount = data.collection_count;
                tv_collection_count.setText(collectionCount + "");
                tv_comment_count.setText(data.comment_count + "");
                if (linearLayoutContent.getChildCount() == 0) {
                    for (int i = 0; i < data.img.size(); i++) {
                        ImageView imageView = new ImageView(mContext);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setBackgroundColor(Color.parseColor("#f2f2f2"));
                        LinearLayout.LayoutParams params =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        Utils.dp2px(mContext, 194));
                        params.topMargin = Utils.dp2px(mContext, 10);
                        imageView.setTag(i);
                        imageView.setOnClickListener(imgClickListener);
                        imageView.setOnLongClickListener(imgLongClickListener);
                        linearLayoutContent.addView(imageView, params);
                        ImageloaderUtil.displayImage(mContext, data.img.get(i) + "?",
                                imageView);
                    }

                }
            }

            @Override
            public void onFailure(String msg) {
                mList.setEmptyView(linear_no_data);
                showCustomToast(msg);
                dismissLoadingDialog();
            }
        });
    }

    /**
     * 收藏
     */
    private void GoodsCollection() {
//		showLoadingDialog("请稍候");
        CollectApi.get().GoodsCollection(mContext, goods_id,
                new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
//						dismissLoadingDialog();
                        showCustomToast(msg);
                        iv_collection.setBackgroundResource(R.drawable.icon_heart);
                        page = 0;
                        GoodsDetail();
                    }

                    @Override
                    public void onFailure(String msg) {
//						dismissLoadingDialog();
                        showCustomToast(msg);
                    }
                });
    }

    /**
     * 分享
     */
    private void GoodsShare() {
        GoodsApi.get().GoodsShare(mContext, goods_id,
                new ApiConfig.ApiRequestListener<String>() {
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

    //点击图片查看大图
    View.OnClickListener imgClickListener = new View.OnClickListener() {

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

    View.OnLongClickListener imgLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View imageView) {
            iv_selected = (ImageView) imageView;
            longClickDialog = DialogUtil.getSelectDialog(mContext, "保存到本地", "分享", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.buttonOne: {
                            iv_selected.setDrawingCacheEnabled(true);
                            bitmap = iv_selected.getDrawingCache();
                            if (bitmap != null) {
                                new SaveImageTask().execute(bitmap);
                            }

                            if (longClickDialog.isShowing()) {
                                longClickDialog.dismiss();
                            }
                            break;
                        }
                        case R.id.buttonTwo: {
                            GoodsShare();
                            if (longClickDialog.isShowing()) {
                                longClickDialog.dismiss();
                            }
                            break;
                        }
                        default:
                            break;
                    }
                }
            });
            longClickDialog.show();
            return true;
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //收藏
            case R.id.lt_collection: {
                if (Utils.isFastClick()) {
                    return;
                }
                if (mGoodsInfo != null) {
                    if (mGoodsInfo.goods_status == 0) {
                        GoodsCollection();
                    } else if (mGoodsInfo.goods_status == 1) {
                        CancelGoodsCollection();
                    }
                }
                break;
            }
            //评论
            case R.id.lt_comment: {
                intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("goods_id", goods_id);
                intent.putExtra("customerid", mGoodsInfo.user_id);
                intent.putExtra("goods_name", mGoodsInfo.name);
                startActivityForResult(intent, 1);
                break;
            }

            //分享
            case R.id.lt_share: {
                GoodsShare();
                break;
            }
            //私信
            case R.id.textViewPrivateLetter: {
                intent = new Intent(mContext, MessageActivity.class);
                if (mGoodsInfo != null) {
                    intent.putExtra("user_id", mGoodsInfo.user_id);
                    startActivity(intent);
                }
                break;
            }
            //查看用户详情
            case R.id.imageViewHeadPortrait: {
                intent = new Intent(mContext, TransactionZoneActivity.class);
                intent.putExtra("user_id", mGoodsInfo.user_id);
                startActivity(intent);
                break;
            }
            //删除评论
            case R.id.buttonConfirm: {
                if (!TextUtils.isEmpty(selectCommentId) && !TextUtils.isEmpty(goodsId)) {
                    DeleteComment(selectCommentId, goodsId, user_id);
                }
                dialog.dismiss();
            }
            case R.id.buttonCancel: {
                dialog.dismiss();
            }

            default:
                break;
        }
    }

    /**
     * 取消收藏
     */
    private void CancelGoodsCollection() {
        CollectApi.get().GoodsCollectionCancel(mContext, goods_id,
                new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        if (collectionCount > 0) {
                            collectionCount = collectionCount - 1;
                        }
                        mGoodsInfo.goods_status = 0;
                        tv_collection_count.setText(collectionCount + "");
                        iv_collection.setBackgroundResource(R.drawable.icon_love);
                        showCustomToast(msg);
                    }

                    @Override
                    public void onFailure(String msg) {
                        showCustomToast(msg);
                    }
                });

    }

    //删除评论
    private void DeleteComment(String commentId, String goods_id, String user_id) {
        GoodsApi.get().DeleteComment(mContext, commentId, goods_id, user_id, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                page = 0;
                GoodsCommentList();
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            page = 0;
            GoodsCommentList();
        }
    }

    //评论删除弹窗
    private void showDialog() {
        if (dialog == null) {
            dialog = new Dialog(mContext, R.style.CustomDialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_common);
            dialog.findViewById(R.id.buttonConfirm).setOnClickListener(this);
            dialog.findViewById(R.id.buttonCancel).setOnClickListener(this);
            ((TextView) dialog.findViewById(R.id.textViewTitle)).setText("确认删除？");

            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            window.setWindowAnimations(R.style.mDialogAnimation);
            window.setAttributes(params);
        }
        dialog.show();
    }

    private class SaveImageTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... params) {
            String result = getResources().getString(R.string.save_picture_failed);
            try {
                String sdCard = Environment.getExternalStorageDirectory().toString();
                File file = new File(sdCard + "/Download");
                if (!file.exists()) {
                    file.mkdirs();
                }
                File imageFile = new File(file.getAbsolutePath(), new Date().getTime() + ".jpg");

                FileOutputStream outputStream = null;
                outputStream = new FileOutputStream(imageFile);
                Bitmap bitmap = params[0];
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                result = getResources().getString(R.string.save_picture_success, file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            iv_selected.setDrawingCacheEnabled(false);
            showCustomToast(s);
        }
    }
}
