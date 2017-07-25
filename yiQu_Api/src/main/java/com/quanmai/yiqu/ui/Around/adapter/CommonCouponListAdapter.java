package com.quanmai.yiqu.ui.Around.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.AroundApi;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.StringUtil;

/**
 * Created by zhanjinj on 16/6/14.
 */
public class CommonCouponListAdapter extends BaseAdapter implements View.OnClickListener {
    private LayoutInflater inflater;
    private CommonList<CouponInfo> mList;
    private Context mContext;
    private Dialog mDialog;
    private int mCouponListType;

    public CommonCouponListAdapter(Context context, int couponListType) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mList = new CommonList<CouponInfo>();
        this.mCouponListType = couponListType;
    }

    public void clear() {
        mList.clear();
    }

    public void add(CommonList<CouponInfo> List) {
        for (int i = 0; i < List.size(); i++) {
            mList.add(List.get(i));
        }
        notifyDataSetChanged();
    }

    public void remove(CouponInfo info) {
        if (info != null && mList.size() > 0) {
            if (mList.contains(info)) {
                mList.remove(info);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.coupon_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.imageViewOutOfDate = (ImageView) convertView.findViewById(R.id.imageViewOutOfDate);
            viewHolder.imageViewCollect = (ImageView) convertView.findViewById(R.id.imageViewCollect);
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            viewHolder.textViewPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
            viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
            viewHolder.rlCollect = (RelativeLayout) convertView.findViewById(R.id.rlCollect);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CouponInfo mInfo = mList.get(position);
        Boolean isCollected = StringUtil.isRegexMatches(".?-\\b1\\b", mInfo.status);

        ImageloaderUtil.displayImage(mContext, mInfo.thumbnail, viewHolder.imageView);
        viewHolder.textViewTitle.setText(mInfo.privilegeName);
        viewHolder.textViewPrice.setText(mInfo.privilegePrice);
        viewHolder.textViewDate.setText("有效期至" + DateUtil.formatToOther(mInfo.endTime, "yyyy-MM-dd", "yyyy年MM月dd日"));
        viewHolder.imageViewCollect.setTag(position);
        viewHolder.imageViewCollect.setVisibility(View.VISIBLE);
        viewHolder.rlCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isCollected = StringUtil.isRegexMatches(".?-\\b1\\b", mInfo.status);
                if (!isCollected) {
                    couponCollect(isCollected, mInfo.id, position);
                } else if (mCouponListType == 1) { //我的收藏优惠卷，取消收藏需要弹窗
                    mDialog = DialogUtil.getConfirmDialog(mContext, "是否取消收藏？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.buttonConfirm: {
                                    couponCollect(true, mInfo.id, position);
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
                } else { //普通商家优惠卷，直接取消收藏
                    couponCollect(true, mInfo.id, position);
                }
            }
        });
        viewHolder.imageViewCollect.setBackgroundResource(isCollected ? R.drawable.ic_collected : R.drawable.ic_not_collected);

        return convertView;
    }

    //收藏、取消收藏优惠卷
    private void couponCollect(final Boolean isCollected, String couponId, final int position) {
        AroundApi.getInstance().couponCollect(mContext, isCollected, couponId, new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        Utils.showCustomToast(mContext, data);
                        String strTemp = mList.get(position).status.substring(0, mList.get(position).status.length() - 1);
                        mList.get(position).status = isCollected ? strTemp + "0" : strTemp + "1";

//                        if (isCollected){
//                             mList.get(position).status.replaceAll("-\\w","-0"); //正则表达式，横杠+数字或者字母
//                        }else {
//                            mList.get(position).status.replaceAll("-\\w","-1");
//                        }

                        notifyDataSetChanged();
                        ((BaseActivity) mContext).dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(String msg) {
                        ((BaseActivity) mContext).dismissLoadingDialog();
                        Utils.showCustomToast(mContext, msg);
                    }
                }
        );

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonConfirm: {
                CouponInfo couponInfo = mList.get((Integer) v.getTag());
                couponCollect(true, couponInfo.id, (Integer) v.getTag());
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

    public class ViewHolder {
        ImageView imageView;
        ImageView imageViewCollect;
        ImageView imageViewOutOfDate;
        RelativeLayout rlCollect;
        TextView textViewTitle;
        TextView textViewPrice;
        TextView textViewDate;
    }
}
