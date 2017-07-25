package com.quanmai.yiqu.ui.booking.Adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.AwardRecordInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

import java.util.ArrayList;

/**
 * Created by admin on 2017/6/22.
 */

public class BookingGoodsRecyclerAdapter extends  RecyclerView.Adapter<BookingGoodsRecyclerAdapter.MyViewHoder>  {
    Context mContext;
    BookingPhotoRecyclerAdapter.OnCloseClick click;
    CommonList<AwardRecordInfo> list;
    SpaceItemDecoration mSpaceItemDecoration;

    public BookingGoodsRecyclerAdapter(Context mContext, CommonList<AwardRecordInfo> list) {
        this.mContext = mContext;
        this.list = list;
    }
    @Override
    public MyViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHoder holder = new MyViewHoder(LayoutInflater.from(
                mContext).inflate(R.layout.item_goods, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHoder holder, int position) {
        if (list!=null&&list.size()!=0){
            ImageloaderUtil.displayImage(mContext,list.get(position).getImg(),holder.imageview_goods);
            holder.textview_goods_price.setText(list.get(position).getAmount()+"益币");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(CommonList<AwardRecordInfo> mlist){
        list.addAll(mlist);
        Log.e("--商品","== "+list.size());
        notifyDataSetChanged();
    }
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    class MyViewHoder extends RecyclerView.ViewHolder{
        ImageView imageview_goods;
        TextView textview_goods_price;
    public MyViewHoder(View itemView) {
        super(itemView);
        imageview_goods= (ImageView) itemView.findViewById(R.id.imageview_goods);
        textview_goods_price= (TextView) itemView.findViewById(R.id.textview_goods_price);
    }
}
    public SpaceItemDecoration getSpaceItemDecoration(int space){
        return mSpaceItemDecoration=new SpaceItemDecoration(space);
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int space;
        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            //outRect.left = space;
            // outRect.right = space;
            //outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if(parent.getChildPosition(view) <list.size())
                outRect.right = space;
        }
    }

}
