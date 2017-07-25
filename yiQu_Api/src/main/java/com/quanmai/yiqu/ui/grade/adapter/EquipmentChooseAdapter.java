package com.quanmai.yiqu.ui.grade.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ChooseEqDetailInfo;
import com.quanmai.yiqu.api.vo.ChooseEqInfo;
import com.quanmai.yiqu.api.vo.ScoreCommDetailInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.grade.GradeEquipmentActivity;
import com.quanmai.yiqu.ui.grade.GradeScoreActivity;

import java.util.List;

/**
 * Created by 95138 on 2016/11/10.
 */

public class EquipmentChooseAdapter extends BaseAdapter {

    Context mContext;
    CommonList<ChooseEqInfo> mData;
    int mType;
    String communityName;
    ScoreCommDetailInfo mInfo;

    public EquipmentChooseAdapter(Context c, int type, String name, ScoreCommDetailInfo info){
        mContext = c;
        mData = new CommonList<>();
        mType = type;
        communityName = name;
        mInfo = info;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.choose_eq_item, null);

            holder.tvEQTitle = (TextView)view.findViewById(R.id.tvEQTitle);
            holder.tvEQStatus = (TextView)view.findViewById(R.id.tvEQStatus);
            holder.llContent = (LinearLayout)view.findViewById(R.id.llContent);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (mData.size()>0){
            final List<ChooseEqDetailInfo> mList = mData.get(position).getList();

            holder.tvEQTitle.setText(mData.get(position).getLetter());

            if ("1".equals(mData.get(position).getIscheck())){
                holder.tvEQStatus.setText("完成");
                holder.tvEQStatus.setTextColor(Color.parseColor("#979797"));
            }else {
                holder.tvEQStatus.setText("未完成");
                holder.tvEQStatus.setTextColor(Color.parseColor("#ED5639"));
            }

            holder.llContent.removeAllViews();
            for (int i=0;i<mList.size();i++){
                final ChooseEqDetailInfo info = mList.get(i);

                View contentView = LayoutInflater.from(mContext).inflate(R.layout.choose_eq_item_item,null);
                TextView tvEQName = (TextView)contentView.findViewById(R.id.tvEQName);
                ImageView ivEQCheck = (ImageView)contentView.findViewById(R.id.ivEQCheck);
                Button btnGrade = (Button)contentView.findViewById(R.id.btnGrade);

                tvEQName.setText(info.getAmenityName());

                if ("1".equals(info.getIscheck())){
                    ivEQCheck.setVisibility(View.VISIBLE);
                    contentView.setEnabled(true);
                }else {
                    btnGrade.setVisibility(View.VISIBLE);
                    contentView.setEnabled(false);
                }

                btnGrade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = null;
                        intent = new Intent(mContext, GradeEquipmentActivity.class);
                        intent.putExtra("Info",info);
                        intent.putExtra("CommName",communityName);
                        intent.putExtra("EQName",mData.get(position).getLetter()+"-"+info.getAmenityName());
                        mContext.startActivity(intent);
                    }
                });

                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, GradeScoreActivity.class);
                        intent.putExtra("ScoreType",GradeScoreActivity.TYPE_EQUIPMENT_SCORE_RECORD);
                        intent.putExtra("CommInfo",mInfo);
                        intent.putExtra("amenityid", info.getAmenityid());
                        mContext.startActivity(intent);
                    }
                });
                holder.llContent.addView(contentView);
            }
        }


        return view;
    }

    class ViewHolder{
        TextView tvEQTitle;
        TextView tvEQStatus;
        LinearLayout llContent;
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void add(CommonList<ChooseEqInfo> data) {
        notifyDataSetChanged();
        mData.addAll(data);
    }
}
