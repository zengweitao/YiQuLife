package com.quanmai.yiqu.ui.booking;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.GarbageThrowRecordInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2016/12/29.
 */

public class GarbageThrowRecordAdapter extends BaseAdapter {
    Context mContext;
    List<GarbageThrowRecordInfo> mList;
    String mType;

    public GarbageThrowRecordAdapter(Context context, String type) {
        this.mContext = context;
        mList = new ArrayList<>();
        mType = type;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList.size() > position + 1) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_garbage_throw_record, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GarbageThrowRecordInfo info = mList.get(position);
        viewHolder.tvName.setText(info.garbagname);
        viewHolder.tvDate.setText(info.opetime);
        if (mType.equals("0")) {
            switch (info.status) {
                case "0": { //未投放
                    viewHolder.tvStatus.setText("未投放");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#FFA7A0"));
                    break;
                }
                case "1": { //未审核
                    viewHolder.tvStatus.setText(info.weight + "kg " + "未审核");
                    break;
                }
                case "2": { //审核未通过
                    viewHolder.tvStatus.setText(info.weight + "kg " + "未通过");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#FFA7A0"));
                    break;
                }
                case "3": { //审核通过
                    /*if (mType.equals("0")) {
                        viewHolder.tvStatus.setText(info.weight + "kg " + info.points + "积分");
                    } else {
                        viewHolder.tvStatus.setText(info.weight + "kg " + info.points + "益币");
                    }*/
                    viewHolder.tvStatus.setText(info.weight + "kg " + info.points + "益币");
                    viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.theme));
                    break;
                }
            }
        } else {
            if (info.status.contains("0")) { //未投放
                viewHolder.tvStatus.setText("未投放");
                viewHolder.tvStatus.setTextColor(Color.parseColor("#FFA7A0"));
            } else { //已投放
                viewHolder.tvStatus.setText(info.weight +"kg 已投放");
                viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.theme));
            }

        }

        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvName;
        public TextView tvStatus;
        public TextView tvDate;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvName = (TextView) rootView.findViewById(R.id.tvName);
            this.tvStatus = (TextView) rootView.findViewById(R.id.tvStatus);
            this.tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        }

    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<GarbageThrowRecordInfo> list) {
        if (list == null) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void add(List<GarbageThrowRecordInfo> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }
}
