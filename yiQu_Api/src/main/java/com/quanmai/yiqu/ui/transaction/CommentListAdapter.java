package com.quanmai.yiqu.ui.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CommentInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.XCRoundImageView;

public class CommentListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private CommonList<CommentInfo> mList;
    private Context mContext;

    public CommentListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mList = new CommonList<CommentInfo>();
        mContext = context;
    }

    @Override
    public int getCount() {
        // return 20;
        return mList.size();
    }

    @Override
    public CommentInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_news_message, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_head = (XCRoundImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.bottomView = convertView.findViewById(R.id.bottomView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CommentInfo info = mList.get(position);
        if (!TextUtils.isEmpty(info.face)) {
            ImageloaderUtil.displayImage(mContext, info.face, viewHolder.iv_head);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_header);
            viewHolder.iv_head.setImageBitmap(bitmap);
        }
        if (TextUtils.isEmpty(info.alias)) {
            viewHolder.tv_name.setText("匿名用户");
        } else {
            viewHolder.tv_name.setText(info.alias);
        }
        viewHolder.tv_content.setText(info.content);
        viewHolder.tv_time.setText(info.time);
        if (position == mList.size() - 1) {
            viewHolder.bottomView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.bottomView.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {

        private TextView tv_time;
        private XCRoundImageView iv_head;
        private TextView tv_name;
        private TextView tv_content;
        private View bottomView;
    }

    public void clear() {
        mList.clear();
    }

    public void add(CommonList<CommentInfo> List) {
        for (int i = 0; i < List.size(); i++) {
            mList.add(List.get(i));
        }
        notifyDataSetChanged();
    }

}
