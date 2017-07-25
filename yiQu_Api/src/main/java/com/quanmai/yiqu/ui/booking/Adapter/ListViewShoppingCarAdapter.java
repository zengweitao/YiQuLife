package com.quanmai.yiqu.ui.booking.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.RecycleGarbagesMapInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/6/30.
 */

public class ListViewShoppingCarAdapter extends BaseAdapter {
    Context context;
    List<RecycleGarbagesMapInfo> recycleGarbagesMapInfos;

    public ListViewShoppingCarAdapter(Context context) {
        this.context = context;
        recycleGarbagesMapInfos=new ArrayList<>();
    }

    public void clear(){
        recycleGarbagesMapInfos.clear();
        notifyDataSetChanged();
    }

    public void add(Map<String, RecycleGarbagesMapInfo> garbageMap){
        if (garbageMap.size()>0){
            recycleGarbagesMapInfos=new ArrayList<>(garbageMap.values());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recycleGarbagesMapInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return recycleGarbagesMapInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder=null;
        if (convertView==null){
            mViewHolder=new ViewHolder();
            convertView=View.inflate(context, R.layout.item_list_shopping_car,null);
            mViewHolder.textview_name= (TextView) convertView.findViewById(R.id.textview_name);
            mViewHolder.textview_price= (TextView) convertView.findViewById(R.id.textview_price);
            mViewHolder.textview_number= (TextView) convertView.findViewById(R.id.textview_number);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder= (ViewHolder) convertView.getTag();
        }
        if (recycleGarbagesMapInfos!=null&&recycleGarbagesMapInfos.size()>0){
            mViewHolder.textview_name.setText(recycleGarbagesMapInfos.get(position).getName());
            mViewHolder.textview_price.setText(recycleGarbagesMapInfos.get(position).getCount()+"");
            mViewHolder.textview_number.setText(recycleGarbagesMapInfos.get(position).getNumbers()+"");
        }
        return convertView;
    }

    class ViewHolder{
        TextView textview_name;
        TextView textview_price;
        TextView textview_number;
    }
}
