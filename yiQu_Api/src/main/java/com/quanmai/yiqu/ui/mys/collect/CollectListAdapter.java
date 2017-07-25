package com.quanmai.yiqu.ui.mys.collect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CollectionGoods;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.ui.unused.UnusedDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class CollectListAdapter extends BaseAdapter {
    List<CollectionGoods> mList;
    OnClickListener deleteClickListener;
    int mWidth;
    Context mContext;

    public CollectListAdapter(Context context, OnClickListener listener) {
        mList = new ArrayList<CollectionGoods>();
        deleteClickListener = listener;
        mContext = context;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = (dm.widthPixels) / 4; // 得到宽度
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CollectionGoods getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.activity_collect_item, null);
            viewHolder = new ViewHolder();
            viewHolder.llMain = (LinearLayout) convertView.findViewById(R.id.llMain);
            viewHolder.ivPicture = (ImageView) convertView.findViewById(R.id.ivPicture);
            viewHolder.tvIntroduce = (TextView) convertView.findViewById(R.id.tvIntroduce);
            viewHolder.tvPublishDate = (TextView) convertView.findViewById(R.id.tvPublishDate);
            viewHolder.tvAccessesTimes = (TextView) convertView.findViewById(R.id.tvAccessesTimes);
            viewHolder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CollectionGoods goods = mList.get(position);

        if (goods.img.size() > 0) {
            ImageloaderUtil.displayImage(mContext, goods.img.get(0), viewHolder.ivPicture);
        }
        viewHolder.tvIntroduce.setText(goods.name);
        viewHolder.tvPublishDate.setText("发布于" + goods.release_time);
        viewHolder.tvAccessesTimes.setText("浏览" + goods.collection_count + "次");
        viewHolder.btnDelete.setTag(goods.id);
        viewHolder.btnDelete.setOnClickListener(deleteClickListener);
        viewHolder.llMain.setTag(goods.id);
        viewHolder.llMain.setOnClickListener(goodsClickListener);
        return convertView;
    }

    OnClickListener goodsClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,
                    UnusedDetailActivity.class);
            intent.putExtra("goods_id", (String) v.getTag());
            mContext.startActivity(intent);

        }
    };


    private class ViewHolder {
        private LinearLayout llMain;
        private ImageView ivPicture;
        private TextView tvIntroduce;
        private TextView tvPublishDate;
        private TextView tvAccessesTimes;
        private Button btnDelete;
    }


    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void add(List<CollectionGoods> infos) {
        mList.addAll(infos);
        notifyDataSetChanged();
    }
}
