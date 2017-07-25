package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.BookingDetailInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.ui.booking.BookingDetailsActivity;
import com.quanmai.yiqu.ui.booking.BookingStatusActivity;

/**
 * Created by 95138 on 2016/4/21.
 */
public class RecycleRecordsAdapter extends BaseAdapter {

    Context mContext;
    CommonList<BookingDetailInfo> mList;
    OnDeleteRecordListener mListener;

    public RecycleRecordsAdapter(Context context,OnDeleteRecordListener listener){
        mContext = context;
        mList = new CommonList<>();
        mListener = listener;
    }

    public void clear(){
        if (mList!=null);
        mList.clear();
    }

    public void addData( CommonList<BookingDetailInfo> list){
        if (list!=null&&list.size()>0){
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void removeData(BookingDetailInfo info){
        mList.remove(info);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_record,null);

            holder.textViewStatus = (TextView)convertView.findViewById(R.id.textViewStatus);
            holder.imageViewDelete = (ImageView)convertView.findViewById(R.id.imageViewDelete);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
            holder.textViewTitle = (TextView)convertView.findViewById(R.id.textViewTitle);
            holder.textViewTimeTitle = (TextView)convertView.findViewById(R.id.textViewTimeTitle);
            holder.textViewTime = (TextView)convertView.findViewById(R.id.textViewTime);
            holder.textViewGotIntegral = (TextView)convertView.findViewById(R.id.textViewGotIntegral);
            holder.textViewIntegral = (TextView)convertView.findViewById(R.id.textViewIntegral);
            holder.textViewDidntGetIntegral = (TextView)convertView.findViewById(R.id.textViewDidntGetIntegral);
            holder.linearLayoutContent = (LinearLayout)convertView.findViewById(R.id.linearLayoutContent);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BookingDetailInfo info = mList.get(position);
        //对方未回收
        if (info.statue.equals("overdue")){
            holder.textViewStatus.setText("对方未回收");
            holder.textViewStatus.setTextColor(Color.parseColor("#ff776e"));

            holder.textViewTimeTitle.setText("结束时间：");
            holder.textViewTime.setText(info.recycleTime);
            holder.imageViewDelete.setVisibility(View.VISIBLE);
            holder.textViewGotIntegral.setVisibility(View.GONE);
            holder.textViewIntegral.setVisibility(View.GONE);
            holder.textViewDidntGetIntegral.setVisibility(View.VISIBLE);
        }else if (info.statue.equals("completed")||info.statue.equals("recycle")){
            //回收成功
            holder.textViewStatus.setText("回收成功");
            holder.textViewStatus.setTextColor(mContext.getResources().getColor(R.color.theme));

            holder.imageViewDelete.setVisibility(View.VISIBLE);
            holder.textViewIntegral.setVisibility(View.VISIBLE);
            holder.textViewGotIntegral.setVisibility(View.VISIBLE);
            holder.textViewDidntGetIntegral.setVisibility(View.GONE);
            holder.textViewTimeTitle.setText("完成时间：");
            holder.textViewTime.setText(info.recycleTime);
            holder.textViewIntegral.setText("总计   "+info.point+"益币");
        }else if (info.statue.equals("cancel")){
            //取消预约
            holder.textViewStatus.setText("取消预约");
            holder.textViewStatus.setTextColor(Color.parseColor("#b4b4b4"));

            holder.imageViewDelete.setVisibility(View.VISIBLE);
            holder.textViewTimeTitle.setText("取消时间：");
            holder.textViewTime.setText(info.recycleTime);
            holder.textViewGotIntegral.setVisibility(View.GONE);
            holder.textViewIntegral.setVisibility(View.GONE);
            holder.textViewDidntGetIntegral.setVisibility(View.VISIBLE);
        }else if (info.statue.equals("initial")){
            //待接单
            holder.textViewStatus.setText("待接单");
            holder.textViewStatus.setTextColor(Color.parseColor("#f08e1b"));

            holder.textViewTimeTitle.setText("预约时间：");
            holder.textViewTime.setText(info.rangeDate+"  "+info.rangeTime);
            holder.imageViewDelete.setVisibility(View.GONE);
            holder.textViewGotIntegral.setVisibility(View.GONE);
            holder.textViewDidntGetIntegral.setVisibility(View.GONE);
            holder.textViewIntegral.setVisibility(View.GONE);

        }else if (info.statue.equals("verified")){
            //待收取
            holder.textViewStatus.setText("待收取");
            holder.textViewStatus.setTextColor(Color.parseColor("#f08e1b"));

            holder.textViewTimeTitle.setText("预约时间：");
            holder.textViewTime.setText(info.rangeDate+"  "+info.rangeTime);
            holder.imageViewDelete.setVisibility(View.GONE);
            holder.textViewGotIntegral.setVisibility(View.GONE);
            holder.textViewDidntGetIntegral.setVisibility(View.GONE);
            holder.textViewIntegral.setVisibility(View.GONE);
        }
        ImageloaderUtil.displayImage(mContext,info.pic.split(",")[0],holder.imageView);
        holder.textViewTitle.setText(info.memo);

//        if (Integer.parseInt(info.point)>0){
//            holder.textViewIntegral.setVisibility(View.VISIBLE);
//            holder.textViewDidntGetIntegral.setVisibility(View.GONE);
//            holder.textViewIntegral.setText("总计   "+info.point+"积分");
//        }else {
//            holder.textViewIntegral.setVisibility(View.GONE);
//            holder.textViewDidntGetIntegral.setVisibility(View.VISIBLE);
//        }

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleted(mList.get(position));
            }
        });

        holder.linearLayoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.statue.equals("overdue")||info.statue.equals("completed")||
                        info.statue.equals("recycle")||info.statue.equals("cancel")){
                    Intent intent = new Intent(mContext,BookingDetailsActivity.class);
                    intent.putExtra("BookingDetailInfo",mList.get(position));
                    mContext.startActivity(intent);
                }else if (info.statue.equals("initial")){
                    Intent intent = new Intent(mContext,BookingStatusActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("BookingDetailInfo",mList.get(position));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }else if (info.statue.equals("verified")){
                    Intent intent = new Intent(mContext,BookingStatusActivity.class);
                    intent.putExtra("BookingDetailInfo",mList.get(position));
                    mContext.startActivity(intent);
                }

            }
        });
        return convertView;
    }

    public class ViewHolder{
        TextView textViewStatus; //状态
        ImageView imageViewDelete; //删除图片
        ImageView imageView; //图片
        TextView textViewTitle; //title名字
        TextView textViewTimeTitle; //时间头部
        TextView textViewTime; //时间
        TextView textViewGotIntegral; //获得益币
        TextView textViewIntegral; //总计获得益币
        TextView textViewDidntGetIntegral; //未获得益币
        LinearLayout linearLayoutContent; //条目布局点击事件
    }

    public interface OnDeleteRecordListener{
        void deleted(BookingDetailInfo info);
    }
}
