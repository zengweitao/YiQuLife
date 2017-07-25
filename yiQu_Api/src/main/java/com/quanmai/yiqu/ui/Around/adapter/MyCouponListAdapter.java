package com.quanmai.yiqu.ui.Around.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.api.vo.ShopInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.GrayTransformation;
import com.quanmai.yiqu.ui.Around.CouponListActivity;
import com.quanmai.yiqu.ui.Around.MyCouponActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanjinj on 16/6/14.
 */
public class MyCouponListAdapter extends BaseAdapter {
    private int TYPE_SHOP = 0;
    private int TYPE_COUPON = 1;

    private Context mContext;
    private List<ShopInfo> mShopInfoList;
    private CommonList<CouponInfo> mCouponInfoList;

    public MyCouponListAdapter(Context context) {
        this.mContext = context;
        this.mShopInfoList = new ArrayList<>();
        this.mCouponInfoList = new CommonList<>();
    }

    @Override
    public int getCount() {
        return mShopInfoList.size() + mCouponInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < mShopInfoList.size()) {
            return mShopInfoList.get(position - 1);
        } else {
            return mCouponInfoList.get(position - mShopInfoList.size() - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShopViewHolder shopViewHolder;
        CouponViewHolder couponViewHolder;

        if (getItemViewType(position) == TYPE_SHOP) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_list, null);
                shopViewHolder = new ShopViewHolder();
                shopViewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
                shopViewHolder.tvShopName = (TextView) convertView.findViewById(R.id.tvShopName);
                shopViewHolder.tvCollectedNum = (TextView) convertView.findViewById(R.id.tvCollectedNum);
                shopViewHolder.rlMain = (RelativeLayout) convertView.findViewById(R.id.rlMain);
                convertView.setTag(shopViewHolder);
            } else {
                shopViewHolder = (ShopViewHolder) convertView.getTag();
            }
            final ShopInfo shopInfo = mShopInfoList.get(position);
            ImageloaderUtil.displayImage(mContext, shopInfo.shopPic, shopViewHolder.ivIcon);
            shopViewHolder.tvShopName.setText(shopInfo.shopName);
            shopViewHolder.tvCollectedNum.setText("已收藏 " + shopInfo.collectNum + " 张优惠券");
            shopViewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CouponListActivity.class);
                    intent.putExtra("couponListType", 1);
                    intent.putExtra("dialogType", "show");
                    intent.putExtra("shopId", shopInfo.shopId);
                    intent.putExtra("shopName", shopInfo.shopName);
                    ((Activity) mContext).startActivityForResult(intent, MyCouponActivity.REQUEST_CODE_MY_COUPON);
                }
            });

        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.coupon_list_item, null);
                couponViewHolder = new CouponViewHolder();
                couponViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                couponViewHolder.imageViewOutOfDate = (ImageView) convertView.findViewById(R.id.imageViewOutOfDate);
                couponViewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
                couponViewHolder.textViewPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
                couponViewHolder.textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
                convertView.setTag(couponViewHolder);
            } else {
                couponViewHolder = (CouponViewHolder) convertView.getTag();
            }

            CouponInfo couponInfo = mCouponInfoList.get(position - mShopInfoList.size());

            if (couponInfo.status.contentEquals("0")) {
                Glide.with(mContext)
                        .load(couponInfo.thumbnail)
                        .bitmapTransform(new GrayTransformation(mContext))
                        .into(couponViewHolder.imageView);
                couponViewHolder.textViewTitle.setTextColor(Color.parseColor("#b4b4b4"));
                couponViewHolder.textViewPrice.setTextColor(Color.parseColor("#b4b4b4"));
                couponViewHolder.textViewDate.setTextColor(Color.parseColor("#b4b4b4"));
                couponViewHolder.imageViewOutOfDate.setVisibility(View.VISIBLE);
            } else {
                Glide.with(mContext)
                        .load(couponInfo.thumbnail)
                        .into(couponViewHolder.imageView);
                couponViewHolder.imageViewOutOfDate.setVisibility(View.GONE);
                couponViewHolder.textViewTitle.setTextColor(Color.parseColor("#575757"));
                couponViewHolder.textViewPrice.setTextColor(Color.parseColor("#f36c60"));
                couponViewHolder.textViewDate.setTextColor(Color.parseColor("#979797"));
            }
            couponViewHolder.textViewTitle.setText(couponInfo.privilegeName);
            couponViewHolder.textViewPrice.setText(couponInfo.privilegePrice);
            couponViewHolder.textViewDate.setText("有效期至" + DateUtil.formatToOther(couponInfo.endTime, "yyyy-MM-dd", "yyyy年MM月dd日"));
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if (position < mShopInfoList.size()) {
            return TYPE_SHOP;
        } else {
            return TYPE_COUPON;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void addShopList(List<ShopInfo> list) {
        mShopInfoList.addAll(list);
        notifyDataSetChanged();
    }

    public void addCouponList(CommonList<CouponInfo> list) {
        mCouponInfoList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearShopList() {
        mShopInfoList.clear();
        notifyDataSetChanged();
    }

    public void clearCouponList() {
        mCouponInfoList.clear();
        notifyDataSetChanged();
    }

    public void remove(CouponInfo info) {
        if (info != null && mCouponInfoList.size() > 0) {
            if (mCouponInfoList.contains(info)) {
                mCouponInfoList.remove(info);
                notifyDataSetChanged();
            }
        }
    }

    private class ShopViewHolder {
        ImageView ivIcon;
        TextView tvShopName;
        TextView tvCollectedNum;
        RelativeLayout rlMain;
    }

    public class CouponViewHolder {
        ImageView imageView;
        ImageView imageViewOutOfDate;
        TextView textViewTitle;
        TextView textViewPrice;
        TextView textViewDate;
    }


}
