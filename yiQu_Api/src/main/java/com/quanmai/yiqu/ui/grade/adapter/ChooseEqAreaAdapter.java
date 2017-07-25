package com.quanmai.yiqu.ui.grade.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ChooseEqAreaInfo;
import com.quanmai.yiqu.api.vo.ScoreCommDetailInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.grade.ChooseEqAreaActivity;
import com.quanmai.yiqu.ui.grade.GradeCleanActivity;
import com.quanmai.yiqu.ui.grade.GradeScoreActivity;

/**
 * Created by James on 2016/11/15.
 * 巡检-清运评分设施点适配器
 */

public class ChooseEqAreaAdapter extends BaseAdapter {
    private Activity mContext;
    private CommonList<ChooseEqAreaInfo> mList;
    private int mType;
    private ScoreCommDetailInfo mCommInfo;

    public ChooseEqAreaAdapter(Activity c, int type, ScoreCommDetailInfo commInfo) {
        mContext = c;
        mList = new CommonList<>();
        mType = type;
        mCommInfo = commInfo;
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(final int position, View contentView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (contentView == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.choose_eq_item_item, null);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        final ChooseEqAreaInfo info = mList.get(position);
        holder.tvEQName.setText(info.getAmenityareaName());

        if ("1".equals(info.getIscheck())) { //是否已巡检
            //已巡检
            holder.ivEQCheck.setVisibility(View.VISIBLE);
            holder.btnGrade.setVisibility(View.GONE);
        } else {
            holder.ivEQCheck.setVisibility(View.GONE);
            holder.btnGrade.setVisibility(View.VISIBLE);
        }

        holder.btnGrade.setTag(position);
        holder.btnGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GradeCleanActivity.class);
                int position = (int) v.getTag();
                intent.putExtra("CommInfo", mCommInfo);
                intent.putExtra("EqAreaInfo", mList.get(position));
                mContext.startActivityForResult(intent, ChooseEqAreaActivity.REQUEST_CODE);
            }
        });

        holder.rlRootView.setTag(position);
        holder.rlRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(info.getIscheck())) { //是否已巡检
                    //未巡检，不做跳转处理
                    return;
                }
                Intent intent = new Intent(mContext, GradeScoreActivity.class);
                int position = (int)v.getTag();
                intent.putExtra("ScoreType",mType);
                intent.putExtra("CommInfo", mCommInfo);
                intent.putExtra("amenityid", mList.get(position).getId()+"");
                mContext.startActivity(intent);
            }
        });

        return contentView;
    }


    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void add(CommonList<ChooseEqAreaInfo> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public RelativeLayout rlRootView;
        public TextView tvEQName;
        public ImageView ivEQCheck;
        public Button btnGrade;

        public ViewHolder(View rootView) {
            this.rlRootView = (RelativeLayout) rootView.findViewById(R.id.rlRootView);
            this.tvEQName = (TextView) rootView.findViewById(R.id.tvEQName);
            this.ivEQCheck = (ImageView) rootView.findViewById(R.id.ivEQCheck);
            this.btnGrade = (Button) rootView.findViewById(R.id.btnGrade);
        }

    }
}
