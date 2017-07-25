package com.quanmai.yiqu.ui.booking.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.RecycleGarbageListInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.views.CustomGridView;

/**
 * Created by admin on 2017/6/23.
 */

public class ShowParbagePriceAdapter01 extends BaseAdapter {
    Context context;
    CommonList<RecycleGarbageListInfo> datas;

    public ShowParbagePriceAdapter01(Context context) {
        this.context = context;
        datas=new CommonList();
    }

    public void add(CommonList<RecycleGarbageListInfo> listInfo){
        datas.addAll(listInfo);
        notifyDataSetChanged();
    }
    public void clear(){
        datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
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
            convertView = View.inflate(context, R.layout.item_show_parbage_price_01,null);
            mViewHolder.textview_show_pg_pirce= (TextView) convertView.findViewById(R.id.textview_show_pg_pirce);
            mViewHolder.coustomGridView_show_pg_pirce= (CustomGridView) convertView.findViewById(R.id.coustomGridView_show_pg_pirce);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        if (datas!=null&&datas.size()!=0){
            mViewHolder.textview_show_pg_pirce.setText(datas.get(position).getTypeName());
            ShowParbagePriceAdapter02 mShowParbagePriceAdapter02=new ShowParbagePriceAdapter02(context);
            mViewHolder.coustomGridView_show_pg_pirce.setAdapter(mShowParbagePriceAdapter02);
            mShowParbagePriceAdapter02.add(datas.get(position).getGarbageList());
        }
        return convertView;
    }

    class ViewHolder{
        TextView textview_show_pg_pirce;
        CustomGridView coustomGridView_show_pg_pirce;
    }
}
