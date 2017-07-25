package com.quanmai.yiqu.ui.grade.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.FetchBagRecordInfo;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.ui.grade.FetchBagRecordDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2016/11/15.
 */

public class FetchBagAdapter extends BaseAdapter {
    private List<FetchBagRecordInfo> mList;
    private Context mContext;

    public FetchBagAdapter(Context c) {
        mList = new ArrayList<FetchBagRecordInfo>();
        mContext = c;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public FetchBagRecordInfo getItem(int arg0) {
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
            holder.textViewNumber = (TextView) convertView.findViewById(R.id.textViewNumber);
            holder.textViewScore = (TextView) convertView.findViewById(R.id.textViewScore);
            holder.textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FetchBagRecordInfo bagInfo = mList.get(pos);

        holder.textViewNumber.setText(bagInfo.terminalno);  //编号
        holder.textViewScore.setText(bagInfo.nums + "");      //取袋数量
        holder.textViewNumber.setEllipsize(TextUtils.TruncateAt.END);
        holder.textViewNumber.setWidth(Utils.dp2px(mContext, 123));
        holder.textViewTime.setText(DateUtil.formatToOther(bagInfo.opetime, "yyyy-MM-dd hh:mm:ss", "MM.dd HH:mm"));

        return convertView;
    }

    class ViewHolder {
        TextView textViewNumber;
        TextView textViewScore;
        TextView textViewTime;
    }

    public void clear() {
        mList.clear();
    }

    public void add(List<FetchBagRecordInfo> child) {
        mList.addAll(child);
        notifyDataSetChanged();
    }
}
