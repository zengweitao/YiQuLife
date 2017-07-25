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

public class AddressChoiceRidgepoleAdapter extends BaseAdapter {
    Context context;
    List<AddressRoomInfo.CommunityBean.BuildingListBean> buildingList;
    setOnRidgepoleItemClick msetOnRidgepoleItemClick;

    public AddressChoiceRidgepoleAdapter(Context context, setOnRidgepoleItemClick onRidgepoleItemClick) {
        this.context = context;
        this.msetOnRidgepoleItemClick = onRidgepoleItemClick;
        this.buildingList = new ArrayList<>();
    }

    public void clear() {
        this.buildingList.clear();
        notifyDataSetChanged();
    }

    public void add(List<AddressRoomInfo.CommunityBean.BuildingListBean> list) {
        buildingList.addAll(list);
        for (int i = 0; i < buildingList.size(); i++) {
            Log.e("--选择地址添加内容", "== " + buildingList.get(i).getBuildingNo());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return buildingList.size();
    }

    @Override
    public Object getItem(int position) {
        return buildingList.get(position);
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
        viewHolder.tvAddress.setText(buildingList.get(position).getBuildingNo()+"栋");
        viewHolder.rlAddressChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msetOnRidgepoleItemClick.OnRidgepoleItemClick(v, position, buildingList.get(position).getUnitList());
            }
        });
        return convertView;
    }

    class ViewHolder {
        LinearLayout rlAddressChoice;
        TextView tvAddress;
        View viewCuttingBottom;
        View viewCuttingBottomTwo;
    }

    public interface setOnRidgepoleItemClick {
        void OnRidgepoleItemClick(View v, int poestion, List<AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean> unitList);
    }
}
