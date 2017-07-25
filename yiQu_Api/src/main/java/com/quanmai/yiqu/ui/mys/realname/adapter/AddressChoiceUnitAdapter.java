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

public class AddressChoiceUnitAdapter extends BaseAdapter {
    Context context;
    List<AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean> unitList;
    setOnUnitItemClick msetOnUnitItemClick;

    public AddressChoiceUnitAdapter(Context context , setOnUnitItemClick onUnitItemClick) {
        this.context = context;
        this.msetOnUnitItemClick=onUnitItemClick;
        this.unitList=new ArrayList<>();
    }

    public void clear(){
        this.unitList.clear();
        notifyDataSetChanged();
    }
    public void add(List<AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean> list){
        unitList.addAll(list);
        for (int i=0;i<unitList.size();i++){
            Log.e("--选择地址添加内容","== "+unitList.get(i));
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return unitList.size();
    }

    @Override
    public Object getItem(int position) {
        return unitList.get(position);
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
            viewHolder.rlAddressChoice = (LinearLayout) convertView.findViewById(R.id.rlAddressChoice);
            viewHolder.viewCuttingBottom = convertView.findViewById(R.id.viewCuttingBottom);
            viewHolder.viewCuttingBottomTwo = convertView.findViewById(R.id.viewCuttingBottomTwo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvAddress.setText(unitList.get(position).getUnitNo()+"单元");
        viewHolder.rlAddressChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msetOnUnitItemClick.OnUnitItemClick(v,position,unitList.get(position).getRoomList());
            }
        });
        return convertView;
    }
    class ViewHolder{
        LinearLayout rlAddressChoice;
        TextView tvAddress;
        View viewCuttingBottom;
        View viewCuttingBottomTwo;
    }
    public interface setOnUnitItemClick{
        public void OnUnitItemClick(View v, int poestion,List<AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean.RoomListBean> roomList );
    }
}
