package com.quanmai.yiqu.ui.community;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CommServiceInfo;
import com.quanmai.yiqu.api.vo.ServicesInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95138 on 2016/3/9.
 */
public class ServiceListAdapter extends BaseAdapter {

    CommonList<ServicesInfo> mList;
    Context mContext;

    public ServiceListAdapter(Context context, CommonList<ServicesInfo> list){
        mContext = context;
        mList = new CommonList<>();
        mList.addAll(list);
    }

    public void clear(){
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_community_services, parent, false);
            holder.textViewName = (TextView)convertView.findViewById(R.id.textViewName);
            holder.imageViewCover = (ImageView)convertView.findViewById(R.id.imageViewCover);
            holder.relativeLayoutItem = (RelativeLayout)convertView.findViewById(R.id.relativeLayoutItem);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textViewName.setText(mList.get(position).name);
        holder.relativeLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ServiceSearchActivity.class);
                intent.putExtra("weburl",mList.get(position).href);
                intent.putExtra("title",mList.get(position).name);
                if (mList.get(position).href.startsWith("http")){
                    mContext.startActivity(intent);
                }
            }
        });
        ImageloaderUtil.displayImage(mContext,mList.get(position).icon,holder.imageViewCover);
        return convertView;
    }

    class ViewHolder{
        ImageView imageViewCover;
        TextView textViewName;
        RelativeLayout relativeLayoutItem;
    }
}
