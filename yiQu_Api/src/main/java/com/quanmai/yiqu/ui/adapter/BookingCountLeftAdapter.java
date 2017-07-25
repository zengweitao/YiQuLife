package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.RecycleGarbageListInfo;
import com.quanmai.yiqu.base.CommonList;


/**
 * Created by 95138 on 2016/5/31.
 */
public class BookingCountLeftAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private CommonList<RecycleGarbageListInfo> mList;
    private Context mContext;
    private int mPosition;

    public BookingCountLeftAdapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from(context);
        mList = new CommonList<RecycleGarbageListInfo>();
    }

    public void clear() {
        mList.clear();
    }

    public void add(CommonList<RecycleGarbageListInfo> List) {
        for (int i = 0; i < List.size(); i++) {
            mList.add(List.get(i));
        }
        notifyDataSetChanged();
    }

    public void addAll(CommonList<RecycleGarbageListInfo> List) {
        mList.clear();
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
    public String getItem(int position) {
        return mList.get(position).getTypeName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.booking_count_left_item,null);
            viewHolder = new ViewHolder();
            viewHolder.textViewTitle = (TextView)convertView.findViewById(R.id.textViewTitle);
            viewHolder.greenLine = convertView.findViewById(R.id.greenLine);
            viewHolder.relativeItem = (RelativeLayout) convertView.findViewById(R.id.relativeItem);
            viewHolder.imageview_show_ishave= (ImageView) convertView.findViewById(R.id.imageview_show_ishave);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final RecycleGarbageListInfo infos = mList.get(position);
        viewHolder.textViewTitle.setText(infos.getTypeName());

        if (mPosition == position) {
            viewHolder.relativeItem.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.greenLine.setVisibility(View.VISIBLE);
            viewHolder.textViewTitle.setTextColor(Color.parseColor("#575757"));
        } else {
            viewHolder.relativeItem.setBackgroundColor(Color.parseColor("#f2f2f2"));
            viewHolder.greenLine.setVisibility(View.GONE);
            viewHolder.textViewTitle.setTextColor(Color.parseColor("#979797"));
        }
        int count=0;
        for (int i = 0; i < mList.get(position).getGarbageList().size(); i++) {
            count+=mList.get(position).getGarbageList().get(i).getCount();
        }
        mList.get(position).setCount(count);
        if (count<=0){
            viewHolder.imageview_show_ishave.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.imageview_show_ishave.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void setPosition(int position){
        if (mPosition != position) {
            mPosition=position;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder{
        TextView textViewTitle;
        View greenLine;
        RelativeLayout relativeItem;
        ImageView imageview_show_ishave;
    }
}
