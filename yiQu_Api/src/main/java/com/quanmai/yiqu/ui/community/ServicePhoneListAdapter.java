package com.quanmai.yiqu.ui.community;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CommServiceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95138 on 2016/3/9.
 */
public class ServicePhoneListAdapter extends BaseAdapter {

    List<CommServiceInfo> mList;
    Context mContext;

    public ServicePhoneListAdapter(Context context,List<CommServiceInfo> list){
        mContext = context;
        mList = new ArrayList<>();
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
                    R.layout.community_phone_item, parent, false);
            holder.textViewCommunityName = (TextView)convertView.findViewById(R.id.textViewCommunityName);
            holder.textViewCommunityAddress = (TextView)convertView.findViewById(R.id.textViewCommunityAddress);
            holder.relativeLayoutCommunityPhoneNo = (RelativeLayout)convertView.findViewById(R.id.relativeLayoutCommunityPhoneNo);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textViewCommunityName.setText(mList.get(position).title);
        holder.textViewCommunityAddress.setText(mList.get(position).address);
        holder.relativeLayoutCommunityPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                Uri data = Uri.parse("tel:" + mList.get(position).contact_tel);

                intent.setData(data);

                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView textViewCommunityName;
        TextView textViewCommunityAddress;
        RelativeLayout relativeLayoutCommunityPhoneNo;
    }
}
