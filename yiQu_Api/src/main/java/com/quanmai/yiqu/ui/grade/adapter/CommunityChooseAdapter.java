package com.quanmai.yiqu.ui.grade.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ScoreCommDetailInfo;
import com.quanmai.yiqu.api.vo.ScoreCommInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.grade.ChooseEquipmentActivity;
import com.quanmai.yiqu.ui.grade.ChooseEqAreaActivity;
import com.quanmai.yiqu.ui.grade.ChooseScoreCommunityActivity;
import com.quanmai.yiqu.ui.grade.GradeScoreActivity;

import java.util.List;

/**
 * Created by 95138 on 2016/11/10.
 */

public class CommunityChooseAdapter extends BaseAdapter {

    Context mContext;
    CommonList<ScoreCommInfo> mData;
    int mScoreType;

    public CommunityChooseAdapter(Context c,int type) {
        mContext = c;
        mData = new CommonList<>();
        mScoreType = type;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.choose_score_comm_section, null);

            holder.tvLetter = (TextView)view.findViewById(R.id.tvLetter);
            holder.llContent = (LinearLayout) view.findViewById(R.id.llContent);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (mData.size()>0){
            List<ScoreCommDetailInfo> mList = mData.get(position).getList();
            holder.tvLetter.setText(mData.get(position).getLetter());
            holder.llContent.removeAllViews();

            for (int i=0;i<mList.size();i++){
                final ScoreCommDetailInfo info = mList.get(i);

                View contentView = LayoutInflater.from(mContext).inflate(R.layout.choose_score_community_item,null);
                final TextView tvCommunity = (TextView)contentView.findViewById(R.id.tvCommunity);
                View line = contentView.findViewById(R.id.line);

                tvCommunity.setText(info.getCommname());

                if (i==mList.size()-1){
                    line.setVisibility(View.GONE);
                }
                holder.llContent.addView(contentView);

                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        switch (mScoreType){
                            //设施评分
                            case ChooseScoreCommunityActivity.TYPE_EQUIPMENT_SCORE:{
                                intent = new Intent(mContext, ChooseEquipmentActivity.class);
                                intent.putExtra("ScoreType",ChooseScoreCommunityActivity.TYPE_EQUIPMENT_SCORE);
                                intent.putExtra("Info",info);
                                mContext.startActivity(intent);
                                break;
                            }
                            //清运评分
                            case ChooseScoreCommunityActivity.TYPE_CLEAN_SCORE:{
                                intent = new Intent(mContext, ChooseEqAreaActivity.class);
                                intent.putExtra("ScoreType",ChooseScoreCommunityActivity.TYPE_CLEAN_SCORE);
                                intent.putExtra("Info",info);
                                mContext.startActivity(intent);
                                break;
                            }
                            //宣传评分
                            case ChooseScoreCommunityActivity.TYPE_PUBLISH_SCORE:{
                                break;
                            }

                            //设施评分记录
                            case ChooseScoreCommunityActivity.TYPE_EQUIPMENT_SCORE_RECORD:{
                                intent = new Intent(mContext, GradeScoreActivity.class);
                                intent.putExtra("ScoreType",ChooseScoreCommunityActivity.TYPE_EQUIPMENT_SCORE_RECORD);
                                intent.putExtra("CommInfo",info);
                                mContext.startActivity(intent);
                                break;
                            }

                            //清运评分记录
                            case ChooseScoreCommunityActivity.TYPE_CLEAN_SCORE_RECORD:{
                                intent = new Intent(mContext, GradeScoreActivity.class);
                                intent.putExtra("ScoreType",ChooseScoreCommunityActivity.TYPE_CLEAN_SCORE_RECORD);
                                intent.putExtra("CommInfo",info);
                                mContext.startActivity(intent);
                                break;
                            }
                            default:break;
                        }
                    }
                });
            }
        }
        return view;
    }

    class ViewHolder {
        TextView tvLetter;
        LinearLayout llContent;
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void add(CommonList<ScoreCommInfo> data) {
        notifyDataSetChanged();
        mData.addAll(data);
    }
}
