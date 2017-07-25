package com.quanmai.yiqu.ui.grade.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.GradeScoreInfo;
import com.quanmai.yiqu.ui.grade.GradeScoreActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2016/11/11.
 */

public class GradeScoreAdapter extends BaseAdapter {
    List<GradeScoreInfo> mList;
    Context mContext;
    private int mType;   //页面类型（设备或者清运）


    public GradeScoreAdapter(Context context, int type) {
        mList = new ArrayList<GradeScoreInfo>();
        mContext = context;
        mType = type;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public GradeScoreInfo getItem(int arg0) {
        return mList.get(arg0 - 1);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_grade_score, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GradeScoreInfo info = mList.get(pos);
        holder.tvGrade.setText("总评分：" + info.checkScore);
        holder.tvDate.setText(info.datetime);
        if (mType == GradeScoreActivity.TYPE_EQUIPMENT_SCORE_RECORD) {
            holder.tvEquipment.setText(info.amenityareaName + "-" + info.amenityName);
        } else {
            holder.tvEquipment.setText(info.amenityareaName);
        }

        return convertView;
    }


    public void clear() {
        mList.clear();
    }

    public void add(List<GradeScoreInfo> child) {
        mList.addAll(child);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvGrade;
        public TextView tvEquipment;
        public TextView tvDate;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvGrade = (TextView) rootView.findViewById(R.id.tvGrade);
            this.tvEquipment = (TextView) rootView.findViewById(R.id.tvEquipment);
            this.tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        }
    }
}
