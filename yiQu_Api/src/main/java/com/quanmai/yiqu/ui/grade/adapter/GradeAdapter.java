package com.quanmai.yiqu.ui.grade.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ScoreRecordInfo;
import com.quanmai.yiqu.ui.grade.ClassificationDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class GradeAdapter extends BaseAdapter {
    List<ScoreRecordInfo> mList;
    LayoutInflater inflater;
    Context mContext;
    String dateTime;

    public GradeAdapter(Context c) {
        mList = new ArrayList<ScoreRecordInfo>();
        mContext = c;
    }

    public void addTime(String time){
        if (!TextUtils.isEmpty(time)){
            dateTime = time;
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ScoreRecordInfo getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_score_detail, null);
            holder.textViewNumber = (TextView) convertView
                    .findViewById(R.id.textViewNumber);
            holder.textViewScore = (TextView) convertView
                    .findViewById(R.id.textViewScore);
            holder.textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);
            holder.rlItem = (LinearLayout) convertView.findViewById(R.id.rlItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ScoreRecordInfo fixInfo = mList.get(pos);


        holder.textViewNumber.setText(fixInfo.barcode);
        holder.textViewScore.setText("+" + fixInfo.score);
        holder.textViewTime.setText(fixInfo.opetime);
        holder.rlItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ClassificationDetailsActivity.class);
                intent.putExtra("Info",fixInfo);
                intent.putExtra("time",dateTime);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView textViewNumber;
        TextView textViewScore;
        TextView textViewTime;
        LinearLayout rlItem;
    }

    public void clear() {
        mList.clear();
    }

    public void add(List<ScoreRecordInfo> child) {
        mList.addAll(child);
        notifyDataSetChanged();
    }

}
