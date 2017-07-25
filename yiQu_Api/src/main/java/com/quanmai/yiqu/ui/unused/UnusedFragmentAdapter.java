package com.quanmai.yiqu.ui.unused;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

/**
 * Created by yin on 16/3/7.
 */
public class UnusedFragmentAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private CommonList<GoodsBasic> mList;
    private Context mContext;

    public UnusedFragmentAdapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from(context);
        mList = new CommonList<GoodsBasic>();
    }

    public void clear() {
        mList.clear();
    }

    public void add(CommonList<GoodsBasic> List) {
        for (int i = 0; i < List.size(); i++) {
            mList.add(List.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_fragment_unused,null);
            viewHolder = new ViewHolder();
            viewHolder.ivPicture = (ImageView)convertView.findViewById(R.id.ivPicture);
            viewHolder.tvIntroduce = (TextView)convertView.findViewById(R.id.tvIntroduce);
            viewHolder.textViewDegree = (TextView)convertView.findViewById(R.id.textViewDegree);
            viewHolder.tvPrice = (TextView)convertView.findViewById(R.id.tvPrice);
            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
            viewHolder.ll = (LinearLayout)convertView.findViewById(R.id.ll);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GoodsBasic basic = mList.get(position);
        if (basic.img.size()>0){
            viewHolder.ivPicture.setImageResource(android.R.color.transparent);
            ImageloaderUtil.displayImage(mContext, basic.img.get(0)+"?imageMogr2/thumbnail/!200x200r/interlace/1", viewHolder.ivPicture);
        }
        viewHolder.tvIntroduce.setText(basic.name);

        if (basic.type == 1) {
            viewHolder.tvPrice.setText("捐赠品");
        } else {
            viewHolder.tvPrice.setText(Utils.getPrice(basic.price));
        }
        if (basic.degree>0){
            viewHolder.textViewDegree.setText(basic.degree+"成新");
        }
        viewHolder.tvDate.setText(basic.release_time);
        viewHolder.ll.setTag(basic.id);
        viewHolder.ll.setOnClickListener(onClickListener);

        return convertView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,
                    UnusedDetailActivity.class);
            intent.putExtra("goods_id", (String) v.getTag());
            mContext.startActivity(intent);
        }
    };


    public class ViewHolder{
        ImageView ivPicture;
        TextView tvIntroduce;
        TextView textViewDegree;
        TextView tvPrice;
        TextView tvDate;
        LinearLayout ll;
    }
}
