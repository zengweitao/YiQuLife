package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.widget.GrayTransformation;


/**
 * Created by 95138 on 2016/5/31.
 */
public class CouponListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private CommonList<CouponInfo> mList;
    private Context mContext;

    public CouponListAdapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from(context);
        mList = new CommonList<CouponInfo>();
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

    public void remove(CouponInfo info){
        if (info!=null&&mList.size()>0){
            if (mList.contains(info)){
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
        return mList.get(position-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView==null){
            convertView = inflater.inflate(R.layout.coupon_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
            viewHolder.imageViewOutOfDate = (ImageView)convertView.findViewById(R.id.imageViewOutOfDate);
            viewHolder.textViewTitle = (TextView)convertView.findViewById(R.id.textViewTitle);
            viewHolder.textViewPrice = (TextView)convertView.findViewById(R.id.textViewPrice);
            viewHolder.textViewDate = (TextView)convertView.findViewById(R.id.textViewDate);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CouponInfo mInfo= mList.get(position);

        if (mInfo.status.split("-")[0].equals("1")){
            Glide.with(mContext)
                    .load(mInfo.thumbnail)
                    .bitmapTransform(new GrayTransformation(mContext))
                    .into(viewHolder.imageView);
            viewHolder.textViewTitle.setTextColor(Color.parseColor("#b4b4b4"));
            viewHolder.textViewPrice.setTextColor(Color.parseColor("#b4b4b4"));
            viewHolder.textViewDate.setTextColor(Color.parseColor("#b4b4b4"));
            viewHolder.imageViewOutOfDate.setVisibility(View.VISIBLE);
        }else {
            Glide.with(mContext)
                    .load(mInfo.thumbnail)
                    .into(viewHolder.imageView);
            viewHolder.imageViewOutOfDate.setVisibility(View.GONE);
            viewHolder.textViewTitle.setTextColor(Color.parseColor("#575757"));
            viewHolder.textViewPrice.setTextColor(Color.parseColor("#f36c60"));
            viewHolder.textViewDate.setTextColor(Color.parseColor("#979797"));
        }
        viewHolder.textViewTitle.setText(mInfo.privilegeName);
        viewHolder.textViewPrice.setText("￥"+mInfo.price);
        viewHolder.textViewDate.setText("有效期至"+ DateUtil.formatToOther(mInfo.endTime,"yyyy-MM-dd","yyyy年MM月dd日"));
        return convertView;
    }

    public class ViewHolder{
        ImageView imageView;
        ImageView imageViewOutOfDate;
        TextView textViewTitle;
        TextView textViewPrice;
        TextView textViewDate;
    }
}
