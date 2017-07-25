package com.quanmai.yiqu.ui.mys.realname.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CityInfo;
import com.quanmai.yiqu.api.vo.CommunityInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2016/7/7.
 * 住址选择--》列表适配器
 */
public class AddressChoiceListAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mTList;
    private T mT;


    public AddressChoiceListAdapter(Context context, T t) {
        this.mContext = context;
        this.mT = t;
        mTList = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return mTList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address_choice, parent, false);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.viewCuttingBottom = convertView.findViewById(R.id.viewCuttingBottom);
            viewHolder.viewCuttingBottomTwo = convertView.findViewById(R.id.viewCuttingBottomTwo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mT.getClass().equals(CityInfo.class)) {
            CityInfo cityInfo = (CityInfo) mTList.get(position);
            viewHolder.tvAddress.setText(cityInfo.city);
        } else if (mT.getClass().equals(CommunityInfo.class)) {
            CommunityInfo communityInfo = (CommunityInfo) mTList.get(position);
            viewHolder.tvAddress.setText(communityInfo.commname);
        } else if (mT.getClass().equals(String.class)) {
            viewHolder.tvAddress.setText((String) mTList.get(position));
        }

        if (position == mTList.size() - 1) {
            viewHolder.viewCuttingBottom.setVisibility(View.GONE);
            viewHolder.viewCuttingBottomTwo.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void clear() {
        mTList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        if (list == null || list.size() == 0) return;
        mTList.clear();
        mTList.addAll(list);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvAddress;
        View viewCuttingBottom;
        View viewCuttingBottomTwo;
    }


}
