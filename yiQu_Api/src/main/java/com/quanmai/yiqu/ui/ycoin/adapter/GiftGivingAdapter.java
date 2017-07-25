package com.quanmai.yiqu.ui.ycoin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.AwardRecordInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.ycoin.OnTextViewClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chasing-Li on 2017/5/24.
 */

public class GiftGivingAdapter extends BaseAdapter {
    Context context;
    CommonList<AwardRecordInfo> mDatas;
    OnTextViewClickListener mOnClickListener;

    public GiftGivingAdapter(Context context ,OnTextViewClickListener listener) {
        this.context = context;
        mDatas=new CommonList<>();
        mOnClickListener=listener;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position,View convertView, final ViewGroup parent) {
        ViewHoder mViewHoder=null;
        if (convertView==null){
            mViewHoder=new ViewHoder();
            convertView = View.inflate(context, R.layout.item_gift_giving,null);
            mViewHoder.tvName= (TextView) convertView.findViewById(R.id.tvName);
            mViewHoder.tvYCoins= (TextView) convertView.findViewById(R.id.tvYCoins);
            mViewHoder.tvGiving= (TextView) convertView.findViewById(R.id.tvGiving);
            convertView.setTag(mViewHoder);
        }else {
            mViewHoder= (ViewHoder) convertView.getTag();
        }
        if (mDatas!=null&&mDatas.size()!=0){
            mViewHoder.tvName.setText(mDatas.get(position).getGiftName());
            mViewHoder.tvYCoins.setText(mDatas.get(position).getAmount()+"益币");
            final View finalConvertView = convertView;
            mViewHoder.tvGiving.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.OnClick(position,v,parent);
                }
            });
        }
        return convertView;
    }
    class ViewHoder{
        TextView tvName;
        TextView tvYCoins;
        TextView tvGiving;
    }
    public void clear(){
        mDatas.clear();
        notifyDataSetChanged();
    }
    public void add(CommonList<AwardRecordInfo> data){
        mDatas.addAll(data);
        notifyDataSetChanged();
    }
}


