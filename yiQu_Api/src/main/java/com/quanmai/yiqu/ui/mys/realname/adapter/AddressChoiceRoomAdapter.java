package com.quanmai.yiqu.ui.mys.realname.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.AddressRoomInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/13.
 */

public class AddressChoiceRoomAdapter extends BaseAdapter {
    Context context;
    List<AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean.RoomListBean> roomList;
    setOnRoomItemClick msetOnRoomItemClick;

    public AddressChoiceRoomAdapter(Context context , setOnRoomItemClick onRoomItemClick) {
        this.context = context;
        this.msetOnRoomItemClick=onRoomItemClick;
        this.roomList=new ArrayList<>();
    }

    public void clear(){
        this.roomList.clear();
        notifyDataSetChanged();
    }
    public void add(List<AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean.RoomListBean> list){
        roomList.addAll(list);
        for (int i=0;i<roomList.size();i++){
            Log.e("--选择地址添加内容","== "+roomList.get(i));
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int position) {
        return roomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address_choice, parent, false);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.tvphone = (TextView) convertView.findViewById(R.id.tvphone);
            viewHolder.tvaddress = (TextView) convertView.findViewById(R.id.tvaddress);
            viewHolder.rlAddressChoice = (LinearLayout) convertView.findViewById(R.id.rlAddressChoice);
            viewHolder.viewCuttingBottom = convertView.findViewById(R.id.viewCuttingBottom);
            viewHolder.viewCuttingBottomTwo = convertView.findViewById(R.id.viewCuttingBottomTwo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvphone.setVisibility(View.VISIBLE);
        viewHolder.tvaddress.setVisibility(View.VISIBLE);
        viewHolder.tvAddress.setText(roomList.get(position).getName());
        viewHolder.tvphone.setText(roomList.get(position).getMtel());
        viewHolder.tvaddress.setText(roomList.get(position).getRoomNo()+"室");
        viewHolder.rlAddressChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msetOnRoomItemClick.OnRoomItemClick(v,position,roomList.get(position));
            }
        });
        return convertView;
    }
    class ViewHolder{
        LinearLayout rlAddressChoice;
        TextView tvAddress;
        TextView tvphone;
        TextView tvaddress;

        View viewCuttingBottom;
        View viewCuttingBottomTwo;
    }
    public interface setOnRoomItemClick{
        void OnRoomItemClick(View v, int poestion,AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean.RoomListBean roomBean);
    }
}
