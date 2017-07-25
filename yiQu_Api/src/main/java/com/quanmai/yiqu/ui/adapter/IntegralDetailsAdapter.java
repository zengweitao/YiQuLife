package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.api.vo.ScoreDayRecord;
import com.quanmai.yiqu.api.vo.ScoreMonthRecord;
import com.quanmai.yiqu.base.CommonList;

/**
 * Created by yin on 16/4/8.
 */
public class IntegralDetailsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private CommonList<ScoreMonthRecord> mList;
    private int mpoint_type;

    public IntegralDetailsAdapter(Context context,int point_type){
        mContext = context;
        inflater = LayoutInflater.from(context);
        mList = new CommonList<ScoreMonthRecord>();
        mpoint_type = point_type;
    }

    public void clear() {
        mList.clear();
    }

    public void add(CommonList<ScoreMonthRecord> list) {
        for (int i = 0; i < list.size(); i++) {
            mList.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
                    (R.layout.item_integral_details_head,parent,false);
            holder = new ViewHolder();
            holder.textViewDateTime = (TextView)convertView.findViewById(R.id.textViewDateTime);
            holder.textViewTotalGetIntegral = (TextView)convertView.findViewById(R.id.textViewTotalGetIntegral);
            holder.textViewTotalUsedIntegral = (TextView)convertView.findViewById(R.id.textViewTotalUsedIntegral);
            holder.linearContent = (LinearLayout)convertView.findViewById(R.id.linearContent);

            convertView.setTag(holder);
        }else {
           holder = (ViewHolder)convertView.getTag();
        }

        ScoreMonthRecord data = mList.get(position);
        holder.textViewDateTime.setText(data.year+data.month);
        if(mpoint_type==2){
            holder.textViewTotalGetIntegral.setText("已获得积分 "+data.get);
            holder.textViewTotalUsedIntegral.setText("已使用积分 "+data.cost);
        }else if (mpoint_type==3){
            holder.textViewTotalGetIntegral.setText("已获得福袋 "+data.get);
            holder.textViewTotalUsedIntegral.setText("已使用福袋 "+data.cost);
        }

        holder.linearContent.removeAllViews();
        for (int i=0;i<data.dayRecords.size();i++){
            ScoreDayRecord score = data.dayRecords.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_integral_details, null, false);
            TextView textViewTitle = (TextView)view.findViewById(R.id.textViewTitle);
            TextView textViewIntegral = (TextView)view.findViewById(R.id.textViewIntegral);
            TextView textViewTime = (TextView)view.findViewById(R.id.textViewTime);

            textViewTitle.setText(score.name);
            if (score.type==0){
                textViewIntegral.setText("+"+score.score);
            }else {
                textViewIntegral.setText("-"+score.score);
            }
            textViewTime.setText(score.time);

            holder.linearContent.addView(view);
        }

        return convertView;
    }


    public class ViewHolder{
        TextView textViewDateTime;
        TextView textViewTotalGetIntegral;
        TextView textViewTotalUsedIntegral;
        LinearLayout linearContent;
    }

}
