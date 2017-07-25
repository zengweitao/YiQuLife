package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.BookingDetailInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 95138 on 2016/4/22.
 */
public class BookingStatusAdapter extends BaseAdapter {

    Context mContext;
    List<String> timeList;
    List<String> statueList;
    BookingDetailInfo mInfo;

    public BookingStatusAdapter(Context context){
        mContext = context;
        timeList = new ArrayList<>();
        statueList = new ArrayList<>();

    }

    public void addData(List<String> timeLis,List<String> statueList){
        if (timeLis!=null&&timeLis.size()>0){
            this.timeList.addAll(timeLis);
        }

        if (statueList!=null&&statueList.size()>0){
            this.statueList.addAll(statueList);
        }
        notifyDataSetChanged();
    }

    public void clear(){
        statueList.clear();
        timeList.clear();
        notifyDataSetChanged();
    }

    public void addInfo(BookingDetailInfo info){
        mInfo = info;
    }

    @Override
    public int getCount() {
        return statueList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_booking_status_listview,null);
            holder.imageViewStatus = (ImageView)convertView.findViewById(R.id.imageViewStatus);
            holder.textViewTitle = (TextView)convertView.findViewById(R.id.textViewTitle);
            holder.textViewDescription = (TextView)convertView.findViewById(R.id.textViewDescription);
            holder.textViewTime = (TextView)convertView.findViewById(R.id.textViewTime);
            holder.viewTopLine = convertView.findViewById(R.id.viewTopLine);
            holder.viewBottomLine = convertView.findViewById(R.id.viewBottomLine);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (position==0){
            holder.viewTopLine.setVisibility(View.INVISIBLE);
            holder.viewBottomLine.setVisibility(View.VISIBLE);
        }else if (statueList.size()==position+1){
            holder.viewTopLine.setVisibility(View.VISIBLE);
            holder.viewBottomLine.setVisibility(View.INVISIBLE);
        }else {
            holder.viewBottomLine.setVisibility(View.VISIBLE);
            holder.viewTopLine.setVisibility(View.VISIBLE);
        }

        String status = statueList.get(position);
        String time = timeList.get(position);

        if (status.equals("commit")){
            holder.imageViewStatus.setBackgroundResource(R.drawable.icon_booking_status_submit);
            holder.textViewTitle.setText("预约已提交");
            holder.textViewDescription.setText("请耐心等待回收人员确认");
        }else if (status.equals("initial")){
            holder.imageViewStatus.setBackgroundResource(R.drawable.icon_booking_status_wating);
            holder.textViewTitle.setText("等待回收人员接单");
            holder.textViewDescription.setText("超过预约时间尚未接单，将自动取消");
        } else if (status.equals("verified")) {
            holder.imageViewStatus.setBackgroundResource(R.drawable.icon_booking_status_confirm);
            holder.textViewTitle.setText("预约已确认");
            holder.textViewDescription.setText("将在"+mInfo.rangeDate+" "+mInfo.rangeTime+"上门回收");
        }else if (status.equals("standby")){
            holder.imageViewStatus.setBackgroundResource(R.drawable.icon_booking_status_will);
            holder.textViewTitle.setText("将在今天上门回收");
            holder.textViewDescription.setText("请确保您在"+mInfo.rangeDate+"在家");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        String timeDate = "";
        try {
            date = simpleDateFormat.parse(time);
            timeDate = new SimpleDateFormat("MM-dd HH:mm").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textViewTime.setText(timeDate);
        return convertView;
    }

    public class ViewHolder{
        ImageView imageViewStatus;
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewTime;
        View viewTopLine;
        View viewBottomLine;
    }
}
