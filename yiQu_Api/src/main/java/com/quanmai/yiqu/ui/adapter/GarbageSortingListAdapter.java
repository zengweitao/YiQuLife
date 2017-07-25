package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.api.vo.GarbageClassifyInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.GrayTransformation;
import com.quanmai.yiqu.ui.classifigarbage.ClassifyKnowledgeActivity;


/**
 * Created by 95138 on 2016/5/31.
 */
public class GarbageSortingListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private CommonList<GarbageClassifyInfo> mList;
    private Context mContext;

    public GarbageSortingListAdapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from(context);
        mList = new CommonList<GarbageClassifyInfo>();
    }

    public void clear() {
        mList.clear();
    }

    public void add(CommonList<GarbageClassifyInfo> List) {
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
    public GarbageClassifyInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView==null){
            convertView = inflater.inflate(R.layout.garbage_sorting_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageViewCover = (ImageView)convertView.findViewById(R.id.imageViewCover);
            viewHolder.textViewTitle = (TextView)convertView.findViewById(R.id.textViewTitle);
            viewHolder.textViewDescription = (TextView)convertView.findViewById(R.id.textViewDescription);
            viewHolder.buttonCheck = (Button) convertView.findViewById(R.id.buttonCheck);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final GarbageClassifyInfo mInfo= mList.get(position);

//
//            Glide.with(mContext)
//                    .load(mInfo.img)
//                    .bitmapTransform(new GrayTransformation(mContext))
//                    .into(viewHolder.imageViewCover);
            ImageloaderUtil.displayImage(mContext,mInfo.img,viewHolder.imageViewCover);

            if (!TextUtils.isEmpty(mInfo.name)){
                viewHolder.textViewTitle.setText(mInfo.name+"");
            }
            if (!TextUtils.isEmpty(mInfo.depict)&&!mInfo.depict.equals("null")){
                viewHolder.textViewDescription.setText(mInfo.depict+"");
            }
            viewHolder.textViewDescription.setText(mInfo.depict+"");
            viewHolder.buttonCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,ClassifyKnowledgeActivity.class);
                    intent.putExtra("http_url",mInfo.url);
                    mContext.startActivity(intent);
                }
            });



        return convertView;
    }

    public class ViewHolder{
        ImageView imageViewCover;
        TextView textViewTitle;
        TextView textViewDescription;
        Button buttonCheck;
    }
}
