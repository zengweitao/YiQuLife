package com.quanmai.yiqu.common.util.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.DialogCommunityInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */

public class DialogCommunityAdapter extends BaseAdapter {
    Context context;
    List<DialogCommunityInfo.CommListBean> infos;
    setOnCommunityClick msetOnCommunityClick;

    public DialogCommunityAdapter(Context context,setOnCommunityClick click) {
        this.context = context;
        this.msetOnCommunityClick = click;
        this.infos = new ArrayList<>();
    }

    public void clear(){
        infos.clear();
        notifyDataSetChanged();
    }

    public void add(List<DialogCommunityInfo.CommListBean> list){
        infos.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder=null;
        if (convertView==null){
            mViewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_dialog_community,null);
            mViewHolder.tv_dialog_comm = (TextView) convertView.findViewById(R.id.tv_dialog_comm);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder= (ViewHolder) convertView.getTag();
        }
        if (infos!=null&&infos.size()>0){
            mViewHolder.tv_dialog_comm.setText(infos.get(position).getCommname());
            mViewHolder.tv_dialog_comm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msetOnCommunityClick.OnCommunityClick(v);
                }
            });
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_dialog_comm;
    }

    public interface setOnCommunityClick{
         void OnCommunityClick(View v);
    }
}
