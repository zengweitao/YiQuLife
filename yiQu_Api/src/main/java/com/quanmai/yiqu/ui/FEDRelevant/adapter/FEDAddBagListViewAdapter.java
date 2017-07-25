package com.quanmai.yiqu.ui.FEDRelevant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.quanmai.yiqu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chasing-Li on 2017/5/20.
 */

public class FEDAddBagListViewAdapter extends BaseAdapter{
    Context mContext;
    List<String> datas;

    public FEDAddBagListViewAdapter(Context mContext) {
        this.mContext = mContext;
        datas=new ArrayList<>();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder mViewHoder=null;
        if (convertView==null){
            mViewHoder=new ViewHoder();
           // convertView=View.inflate(R.layout.item_fed_channel_data,parent,false);
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_fed_channel_data,parent,false);
            mViewHoder.button_channel= (Button) convertView.findViewById(R.id.button_channel);
            convertView.setTag(mViewHoder);
        }else {
            mViewHoder= (ViewHoder) convertView.getTag();
            mViewHoder.button_channel.setText("");
            mViewHoder.button_channel.setBackgroundColor(Color.parseColor("#f0eff5"));
        }
        if (datas!=null&&datas.size()!=0){
            if (datas.get(position).equals("0")){
                mViewHoder.button_channel.setBackgroundColor(Color.parseColor("#f0eff5"));
            }else{
                mViewHoder.button_channel.setBackgroundColor(Color.parseColor("#48c299"));
                mViewHoder.button_channel.setText(datas.get(position).substring
                        (datas.get(position).length()-6,datas.get(position).length()));
            }
        }
        return convertView;
    }
    class ViewHoder{
        Button button_channel;
    }
    public void clear(){
        datas.clear();
    }
    public void add( List<String> data){
        datas.addAll(data);
        notifyDataSetChanged();
    }
}
