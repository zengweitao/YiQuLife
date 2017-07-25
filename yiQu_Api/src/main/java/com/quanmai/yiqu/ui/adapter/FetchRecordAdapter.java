package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.AwardRecordInfo;
import com.quanmai.yiqu.api.vo.ScoreDayRecord;
import com.quanmai.yiqu.api.vo.ScoreMonthRecord;
import com.quanmai.yiqu.base.CommonList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 益币and礼品领取记录
 * Created by 95138 on 2017/3/2.
 */

public class FetchRecordAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private Context mContext;
    private List<ScoreDayRecord> mList;
    private CommonList<AwardRecordInfo> mAwardList;

    public FetchRecordAdapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
        mAwardList = new CommonList<>();
    }

    public void clear() {
        mList.clear();
        mAwardList.clear();
    }


    public void add(List<ScoreDayRecord> list) {
        for (int i = 0; i < list.size(); i++) {
            mList.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    public void addAwards(CommonList<AwardRecordInfo> list){
        if (list!=null&&list.size()>0){
            mAwardList.addAll(list);
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size()>0?mList.size():mAwardList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList.size()>0){
            return mList.get(position);
        }else {
            return mAwardList.get(position);
        }

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView==null){
            convertView = inflater.inflate
                    (R.layout.item_fetch_record_list,parent,false);
            holder = new ViewHolder();

            holder.textViewTitle = (TextView)convertView.findViewById(R.id.textViewTitle);
            holder.textViewDesc = (TextView)convertView.findViewById(R.id.textViewDesc);
            holder.textViewTime = (TextView)convertView.findViewById(R.id.textViewTime);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (mList.size()>0){
            ScoreDayRecord record = mList.get(position);
            if (record!=null){
                if (record.type==0){
                    holder.textViewTitle.setText("+"+record.score+"益币");
                }else {
                    holder.textViewTitle.setText("-"+record.score+"益币");
                }
                holder.textViewDesc.setText(record.name);
                holder.textViewTime.setText(record.time);
            }
        }else if (mAwardList.size()>0){
            AwardRecordInfo info  = mAwardList.get(position);
            if (info!=null){
                holder.textViewTitle.setText(info.getGiftName());
                holder.textViewDesc.setText(info.getContent()+"  -"+info.getAmount()+"益币");

                String time = "";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = format.parse(info.getInputTime());
                    SimpleDateFormat format1 = new SimpleDateFormat("MM-dd HH:mm");
                    time = format1.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.textViewTime.setText(time);
            }
        }


        return convertView;
    }

    public class ViewHolder{
        TextView textViewTitle;
        TextView textViewDesc;
        TextView textViewTime;
    }
}
