package com.quanmai.yiqu.ui.Around;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.AroundApi;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.share.ShareActivity;

public class ShowCouponDirectlyActivity extends ShareActivity implements View.OnClickListener{

    ImageView imageViewBack,imageViewShare;
    ImageView imageViewBg;
    TextView textViewTitle,textViewPrice,textViewDateTime;
    ImageView imageViewCollection;
    TextView textViewContent;

    CouponInfo mInfo;
    boolean isCollected = false;
    String type="";
    Dialog mDialog;
    String couponId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_coupon_directly);

        init();
        couponId =getIntent().getStringExtra("info");
        if(getIntent().hasExtra("type")){
            type = getIntent().getStringExtra("type");
        }

        if(type.equals("shake")){
            imageViewCollection.setVisibility(View.GONE);
        }

        getCouponDetails(couponId);
    }

    private void init() {
        imageViewBack = (ImageView)findViewById(R.id.imageViewBack);
        imageViewShare = (ImageView)findViewById(R.id.imageViewShare);
        imageViewBg = (ImageView)findViewById(R.id.imageViewBg);
        textViewTitle = (TextView)findViewById(R.id.textViewTitle);
        textViewPrice = (TextView)findViewById(R.id.textViewPrice);
        textViewDateTime = (TextView)findViewById(R.id.textViewDateTime);
        imageViewCollection = (ImageView)findViewById(R.id.imageViewCollection);
        textViewContent = (TextView)findViewById(R.id.textViewContent);

        imageViewBack.setOnClickListener(this);
        imageViewShare.setOnClickListener(this);
        imageViewCollection.setOnClickListener(this);
    }

    private void getCouponDetails(String couponId){
        AroundApi.getInstance().getCouponDetails(mContext, couponId, new ApiConfig.ApiRequestListener<CouponInfo>() {
            @Override
            public void onSuccess(String msg, CouponInfo data) {
                mInfo = data;
                if (mInfo!=null){
                    //调整imageview的图片高度
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int screenWidth  = dm.widthPixels;
//            ViewGroup.LayoutParams lp = imageViewBg.getLayoutParams();
//            lp.width = screenWidth;
//            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            imageViewBg.setLayoutParams(lp);
//            imageViewBg.setMaxWidth(screenWidth);
                    imageViewBg.setMaxHeight((int) (screenWidth * 2));// 这里其实可以根据需求而定，我这里测试为最大宽度的1.5倍

                    if (!mInfo.described.equals("null")){
                        textViewContent.setText(mInfo.described);
                    }else {
                        textViewContent.setVisibility(View.GONE);
                    }
                    ImageloaderUtil.displayImage(mContext,mInfo.titleimg,imageViewBg);
                    textViewTitle.setText(mInfo.privilegeName);
                    if (!TextUtils.isEmpty(mInfo.privilegePrice)&&!mInfo.privilegePrice.equals("null")){
                        textViewPrice.setText(mInfo.privilegePrice+"");
                    }else {
                        textViewPrice.setVisibility(View.GONE);
                    }

                    textViewDateTime.setText("有效期至"+ DateUtil.formatToOther(mInfo.endTime,"yyyy-MM-dd","yyyy年MM月dd日"));
                    if (mInfo.status.split("-")[1].equals("0")){
                        isCollected = false;
                        imageViewCollection.setBackgroundResource(R.drawable.around_details_collection_none);
                    }else {
                        isCollected = true;
                        imageViewCollection.setBackgroundResource(R.drawable.around_details_collection);
                    }
                }

            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    //收藏优惠券
    private void collectedCoupon(final boolean isCollect, String couponId){
        AroundApi.getInstance().couponCollect(mContext, isCollect, couponId, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                isCollected = !isCollected;
                if (isCollected){
                    showCustomToast("收藏成功");
                    imageViewCollection.setBackgroundResource(R.drawable.around_details_collection);
                }else {
                    showCustomToast("已取消收藏");
                    imageViewCollection.setBackgroundResource(R.drawable.around_details_collection_none);
                }
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    private void showCollectionDialog(){
        mDialog = DialogUtil.getConfirmDialog(mContext, "是否取消收藏？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonConfirm: {
                        collectedCoupon(isCollected,mInfo.id);
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        break;
                    }
                    case R.id.buttonCancel: {
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        break;
                    }
                }
            }
        });
        mDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageViewBack:{
                finish();
                break;
            }
            case R.id.imageViewShare:{
                getShareDialog(
                        mInfo.share_link,
                        mInfo.privilegeName,
                        UserInfo.get().prefixQiNiu
                                + mInfo.thumbnail
                        , mInfo.described);
                showShareDialog();
                break;
            }
            case R.id.imageViewCollection:{
                if (getIntent().hasExtra("dialogType")
                        &&getIntent().getStringExtra("dialogType").equals("show")&&isCollected){
                    showCollectionDialog();
                }else {
                    collectedCoupon(isCollected,mInfo.id);
                }
                break;
            }
            default:break;
        }
    }
}
