package com.quanmai.yiqu.ui.recycle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ScoreRecordInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.DateUtil;

/**
 * Created by zhanjinj on 16/5/11.
 */
public class RecycleScoreRecordAdapter extends BaseAdapter {
    Context mContext;
    CommonList<ScoreRecordInfo> mList;

    public RecycleScoreRecordAdapter(Context context) {
        this.mContext = context;
        mList = new CommonList<>();
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_recycle_score, null);
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_score, null);
            viewHolder = new ViewHolder();
            viewHolder.tvBarcode = (TextView) convertView.findViewById(R.id.tvBarcode);
            viewHolder.tvScore = (TextView) convertView.findViewById(R.id.tvScore);
            viewHolder.tvOpeTime = (TextView) convertView.findViewById(R.id.tvOpeTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvBarcode.setText(mList.get(position).barcode);
        viewHolder.tvScore.setText("+" + mList.get(position).score);
        viewHolder.tvOpeTime.setText(DateUtil.formatToOther(mList.get(position).opetime, "yyyy-MM-dd hh:mm:ss", "yyyy.MM.dd"));

        return convertView;
    }

    public void add(CommonList<ScoreRecordInfo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clean() {
        mList.clear();
    }

    public void addAll(CommonList<ScoreRecordInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView tvBarcode;
        private TextView tvScore;
        private TextView tvOpeTime;
    }


}
