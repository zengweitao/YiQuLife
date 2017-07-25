package com.quanmai.yiqu.ui.mys.publishmanage;

import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.share.ShareActivity;
import com.quanmai.yiqu.ui.publish.EditUnusedActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhanjinj on 16/3/18.
 */
public class PublishHandleDialog extends Dialog implements View.OnClickListener {
    ShareActivity mContext;

    boolean isOpen = false;
    String goods_id = "";
    int verifyFlag = 0;
    int itemPosition;

    PublishHandleListener mHandleListener = null;

    public PublishHandleDialog(ShareActivity context, int verifyflag, boolean isOpen, PublishHandleListener listener) {
        super(context, R.style.DataSheet);
        this.mContext = context;
        this.verifyFlag = verifyflag;
        this.isOpen = isOpen;
        this.mHandleListener = listener;

        this.setCanceledOnTouchOutside(true);

        initDialog();
    }

    private void initDialog() {
        if (verifyFlag == 2) {  //未通过审核
            setContentView(R.layout.dialog_publish_did_not_verify);
            findViewById(R.id.buttonEdit).setOnClickListener(this);
        } else if (isOpen) {
            setContentView(R.layout.dialog_publish);
            findViewById(R.id.buttonEdit).setOnClickListener(this);
            findViewById(R.id.buttonShare).setOnClickListener(this);
            findViewById(R.id.buttonClose).setOnClickListener(this);
        } else {
            setContentView(R.layout.dialog_publish_closed);
            findViewById(R.id.buttonRenew).setOnClickListener(this);
        }

        findViewById(R.id.buttonDelete).setOnClickListener(this);
        findViewById(R.id.buttonCancel).setOnClickListener(this);


        Window w = getWindow();
        w.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        w.setWindowAnimations(R.style.mDialogAnimation); //设置弹出动画

        WindowManager.LayoutParams params = w.getAttributes();
        params.gravity = Gravity.BOTTOM;
        onWindowAttributesChanged(params);
    }

    public void showDialog(GoodsBasic goodsBasic, int position) {
        this.goods_id = goodsBasic.id;
        this.itemPosition = position;
        show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonEdit: {
                goodsEdit();
                break;
            }

            case R.id.buttonShare: {
                goodsShare();
                break;
            }

            case R.id.buttonClose: {
                goodsClosed(false);
                break;
            }

            case R.id.buttonRenew: {
                goodsClosed(true);
                break;
            }

            case R.id.buttonDelete: {
                DialogUtil.showDelDialog(mContext, new DialogUtil.OnDialogSelectId() {
                    @Override
                    public void onClick(View v) {
                        goodsDelete();
                    }
                });
                this.dismiss();
                break;
            }

            case R.id.buttonCancel: {
                dismiss();
                break;
            }
            default:
                break;

        }
        dismiss();
    }

    private void goodsEdit() {
        if (goods_id == null)
            return;
        Intent intent = new Intent(mContext, EditUnusedActivity.class);
        intent.putExtra("goods_id", goods_id);
        mContext.startActivityForResult(intent, 1);
    }

    private void goodsDelete() {
        if (goods_id == null)
            return;
        mContext.showLoadingDialog("请稍候");
        GoodsApi.get().GoodsDelete(mContext, goods_id,
                new ApiConfig.ApiRequestListener<String>() {

                    @Override
                    public void onSuccess(String msg, String data) {
                        Utils.showCustomToast(mContext, msg);
                        mHandleListener.refreshAll();
                    }

                    @Override
                    public void onFailure(String msg) {
                        mContext.dismissLoadingDialog();
                        Utils.showCustomToast(mContext, msg);
                    }
                });
    }

    private void goodsClosed(final boolean isOpen) {
        if (goods_id == null)
            return;
        mContext.showLoadingDialog("请稍候");
        GoodsApi.get().GoodsClosed(mContext, isOpen, goods_id,
                new ApiConfig.ApiRequestListener<String>() {

                    @Override
                    public void onSuccess(String msg, String data) {
                        mContext.dismissLoadingDialog();
                        Utils.showCustomToast(mContext, msg);
                        mHandleListener.refreshStatus(isOpen, itemPosition);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mContext.dismissLoadingDialog();
                        Utils.showCustomToast(mContext, msg);
                    }
                });
    }

    private void goodsShare() {
        GoodsApi.get().GoodsShare(mContext, goods_id,
                new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        try {
                            JSONObject shareObject = new JSONObject(data);
                            mContext.getShareDialog(
                                    shareObject.optString("share_link"),
                                    shareObject.optString("share_content"),
                                    UserInfo.get().prefixQiNiu + shareObject.optString("share_img")
                                    , "响应环保，一起加入闲置物品置换吧！");
                            mContext.showShareDialog();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            mContext.showCustomToast("数据错误");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        mContext.showCustomToast(msg);
                    }
                });
    }


    public interface PublishHandleListener {
        public void refreshStatus(Boolean isOpen, int position); //改变商品状态

        public void refreshAll(); //刷新界面
    }
}
