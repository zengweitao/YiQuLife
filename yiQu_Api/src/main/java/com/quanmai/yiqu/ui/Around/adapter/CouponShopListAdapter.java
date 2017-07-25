package com.quanmai.yiqu.ui.Around.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ShopInfo;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.ui.Around.CouponListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠卷分类-商家列表适配器
 * Created by James on 2016/9/13.
 */
public class CouponShopListAdapter extends RecyclerView.Adapter<CouponShopListAdapter.ViewHolder>{
    private Context mContext;
    private List<ShopInfo> mList;

    public CouponShopListAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon_shop_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageloaderUtil.displayImage(mContext, mList.get(position).shoppic, holder.ivIcon);
        holder.tvName.setText(mList.get(position).name);
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CouponListActivity.class);
                intent.putExtra("shopId", mList.get(position).id);
                intent.putExtra("shopName", mList.get(position).name);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
        }
    }

    public void add(List<ShopInfo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
