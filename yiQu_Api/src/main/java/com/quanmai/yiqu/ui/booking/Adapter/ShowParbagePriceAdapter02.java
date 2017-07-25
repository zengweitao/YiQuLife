package com.quanmai.yiqu.ui.booking.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.RecycleGarbagesInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

/**
 * Created by admin on 2017/6/23.
 */

public class ShowParbagePriceAdapter02 extends BaseAdapter {
    Context context;
    CommonList<RecycleGarbagesInfo> Garbageinfos;

    public ShowParbagePriceAdapter02(Context context) {
        this.context = context;
        Garbageinfos=new CommonList();
    }

    public void add(CommonList<RecycleGarbagesInfo> listInfo){
        Garbageinfos.addAll(listInfo);
        notifyDataSetChanged();
    }
    public void clear(){
        Garbageinfos.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return Garbageinfos.size();
    }

    @Override
    public Object getItem(int position) {
        return Garbageinfos.get(position);
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
            convertView = View.inflate(context, R.layout.item_show_parbage_price_02,null);
            mViewHolder.imageview_show_pg_pirce = (ImageView) convertView.findViewById(R.id.imageview_show_pg_pirce);
            mViewHolder.textView_parbage_name= (TextView) convertView.findViewById(R.id.textView_parbage_name);
            mViewHolder.textView_parbage_price= (TextView) convertView.findViewById(R.id.textView_parbage_price);
            mViewHolder.textView_unit= (TextView) convertView.findViewById(R.id.textView_unit);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        if (Garbageinfos!=null&&Garbageinfos.size()!=0){
            ImageloaderUtil.displayImage(context, Garbageinfos.get(position).pic,mViewHolder.imageview_show_pg_pirce);
            mViewHolder.textView_parbage_name.setText(Garbageinfos.get(position).garbage);
            mViewHolder.textView_parbage_price.setText(Garbageinfos.get(position).point+"益币");
            mViewHolder.textView_unit.setText(Garbageinfos.get(position).getUnit());
        }
        return convertView;
    }

    class ViewHolder{
        ImageView imageview_show_pg_pirce;
        TextView textView_parbage_name;
        TextView textView_parbage_price;
        TextView textView_unit;
    }
}
