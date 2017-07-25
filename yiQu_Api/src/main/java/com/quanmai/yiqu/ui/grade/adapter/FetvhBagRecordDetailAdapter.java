package com.quanmai.yiqu.ui.grade.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/7.
 */

public class FetvhBagRecordDetailAdapter extends BaseAdapter {
    Context context;
    List<String> datalist;

    public FetvhBagRecordDetailAdapter(Context context) {
        this.context = context;
        datalist=new ArrayList<>();
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public String getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder mViewHoder=null;
        if (convertView==null){
            mViewHoder=new ViewHoder();
            convertView=View.inflate(context, R.layout.item_fetch_bag_record_detail,null);
            mViewHoder.tvNo= (TextView) convertView.findViewById(R.id.tvNo);
            mViewHoder.tvScore= (TextView) convertView.findViewById(R.id.tvScore);
            convertView.setTag(mViewHoder);
        }else {
            mViewHoder= (ViewHoder) convertView.getTag();
        }
        if (datalist.size()!=0){
            mViewHoder.tvNo.setText((position+1)+"");
            mViewHoder.tvScore.setText(datalist.get(position).toString());
        }
        return convertView;
    }
    class ViewHoder{
        TextView tvNo;
        TextView tvScore;
    }
    public void clear(){
        datalist.clear();
    }
    public void add(List<String> data){
        datalist.addAll(data);
        notifyDataSetChanged();
    }
}
