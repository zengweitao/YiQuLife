package com.quanmai.yiqu.ui.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.Noticeinfo;
import com.quanmai.yiqu.ui.common.WebActivity;

import java.util.ArrayList;
import java.util.List;

public class NoticeListAdapter extends BaseAdapter {
    private List<Noticeinfo> list;
    private LayoutInflater inflater;
    private Context mContext;

    public NoticeListAdapter(Context context) {
        list = new ArrayList<Noticeinfo>();
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Noticeinfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_news_notice, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Noticeinfo info = list.get(position);
        holder.tv_name.setText(info.notice_name);
        holder.tv_content.setText(info.notice_content);
        holder.tv_time.setText(info.notice_time);
        if (!TextUtils.isEmpty(info.notice_link)) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra("http_url", info.notice_link);
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tv_name;
        TextView tv_content;
        TextView tv_time;
    }

    public void clear() {
        list.clear();
    }

    public void add(List<Noticeinfo> infos) {
        for (int i = 0; i < infos.size(); i++) {
            list.add(infos.get(i));
        }
        notifyDataSetChanged();
    }
}
