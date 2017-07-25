package com.quanmai.yiqu.ui.Around.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ShopClassInfo;
import com.quanmai.yiqu.common.widget.CheckableRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠卷分类-目录适配器
 * Created by James on 2016/9/13.
 */
public class CouponCatalogAdapter extends RecyclerView.Adapter<CouponCatalogAdapter.ViewHolder> implements View.OnClickListener{
    Context mContext;
    List<ShopClassInfo> mList;
    OnItemClickListener mOnItemClickListener=null;

    public CouponCatalogAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_catalog, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopClassInfo shopClassInfo = mList.get(position);
        if (shopClassInfo.isCheck) {
            holder.rlCatalog.setBackgroundResource(R.drawable.bg_coupon_catalog_checked);
        } else {
            holder.rlCatalog.setBackgroundResource(R.drawable.bg_coupon_catalog_unchecked);
        }
        holder.tvCatalogName.setText(shopClassInfo.type.trim());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        CheckableRelativeLayout rlCatalog;
        TextView tvCatalogName;

        public ViewHolder(View itemView) {
            super(itemView);
            rlCatalog = (CheckableRelativeLayout) itemView.findViewById(R.id.rlCatalog);
            tvCatalogName = (TextView) itemView.findViewById(R.id.tvCatalogName);
        }
    }

    public void add( List<ShopClassInfo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void refresh( List<ShopClassInfo> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public interface OnItemClickListener{
          void onItemClick(View view,int position);
    }
}
