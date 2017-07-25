package com.quanmai.yiqu.ui.Around.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.NewsInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

/**
 * 资讯列表适配器
 * Created by zhanjinj on 16/5/30.
 */
public class NewsListAdapter extends BaseAdapter {
    Context mContext;
    CommonList<NewsInfo> mList;

    public NewsListAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fragment_around, null);
            viewHolder = new ViewHolder();
            viewHolder.ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDescribed = (TextView) convertView.findViewById(R.id.tvDescribed);
            viewHolder.tvTag = (TextView) convertView.findViewById(R.id.tvTag);
            viewHolder.tvBrowseTimes = (TextView) convertView.findViewById(R.id.tvBrowseTimes);
            viewHolder.rlSpecial = (RelativeLayout) convertView.findViewById(R.id.rlSpecial);
            viewHolder.tvSpecialName = (TextView) convertView.findViewById(R.id.tvSpecialName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsInfo newsInfo = mList.get(position);

        ImageloaderUtil.displayImage(mContext, newsInfo.thumbnail, viewHolder.ivThumbnail);
        viewHolder.tvTitle.setText(newsInfo.title);
        viewHolder.tvDescribed.setText(newsInfo.described);
        viewHolder.tvTag.setText(newsInfo.tag + " | " + DateUtil.formatToOther(newsInfo.opeTime, "yyyy-MM-dd hh:mm:ss", "MM-dd"));
        viewHolder.tvBrowseTimes.setText(newsInfo.accTimes);
        if ("1".equals(newsInfo.top) || "1".equals(newsInfo.special)) {
            viewHolder.rlSpecial.setVisibility(View.VISIBLE);
            viewHolder.tvSpecialName.setText("1".equals(newsInfo.top) ? "置顶" : newsInfo.specialName);
        } else {
            viewHolder.rlSpecial.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void add(CommonList<NewsInfo> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clean() {
        mList.clear();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView ivThumbnail;
        TextView tvTitle;
        TextView tvDescribed;
        TextView tvTag;
        TextView tvBrowseTimes;
        RelativeLayout rlSpecial;
        TextView tvSpecialName;
    }
}
