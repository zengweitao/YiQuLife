package com.quanmai.yiqu.ui.classifigarbage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ChooseEqDetailInfo;
import com.quanmai.yiqu.api.vo.ClassifyScoreRankingDetailInfo;
import com.quanmai.yiqu.api.vo.ClassifyScoreRankingInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Chasing-Li on 2017/4/23.
 */
public class ClassifyGarbageScoreRankingAdapter  extends BaseAdapter {
    Context mContext;
    CommonList<ClassifyScoreRankingInfo> mData;

    public ClassifyGarbageScoreRankingAdapter(Context c) {
        mContext=c;
        mData=new CommonList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_classify_score_ranking, null);
            holder.textview_ranking= (TextView) convertView.findViewById(R.id.textview_ranking);
            holder.textview_user= (TextView) convertView.findViewById(R.id.textview_user);
            holder.textview_score_times= (TextView) convertView.findViewById(R.id.textview_score_times);
            holder.textview_scoreAvg= (TextView) convertView.findViewById(R.id.textview_scoreAvg);
            convertView.setTag(holder);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
            holder = (ViewHolder) convertView.getTag();
        }
        if (mData.size()!=0){
            List<ClassifyScoreRankingDetailInfo> mList = mData.get(position).getCommScoreRankList();
            if (mList.size()!=0){
                holder.textview_ranking.setText(mList.get(position).getRank());
                int color = mContext.getResources().getColor(R.color.theme);
                if (mList.get(position).getIsCurrent().equals("1")){
                    convertView.setBackgroundColor(color);
                }
                holder.textview_user.setText(mList.get(position).getAccount());
                holder.textview_score_times.setText(mList.get(position).getScoreTimes());
                holder.textview_scoreAvg.setText(mList.get(position).getScoreAvg()+"");
            }
        }

        return convertView;
    }

    class ViewHolder{
        TextView textview_ranking;
        TextView textview_user;
        TextView textview_score_times;
        TextView textview_scoreAvg;
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void add(CommonList<ClassifyScoreRankingInfo> data){
        mData.addAll(data);
        notifyDataSetChanged();

    }
}
