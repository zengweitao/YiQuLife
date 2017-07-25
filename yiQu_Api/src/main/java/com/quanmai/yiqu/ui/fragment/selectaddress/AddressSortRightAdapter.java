package com.quanmai.yiqu.ui.fragment.selectaddress;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CommunityAddressInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yin on 16/3/9.
 */
public class AddressSortRightAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommunityAddressInfo> mListData;
    private int selectedPos = -1;

    public AddressSortRightAdapter(Context context) {
        selectedPos = -1;
        mContext = context;
        mListData = new ArrayList<CommunityAddressInfo>();
//        if (groupData == null) {
//
//        } else {
//            mListData = groupData;
//        }
    }

    public void addData(List<CommunityAddressInfo> data){
        mListData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(){
        mListData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public CommunityAddressInfo getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.address_choose_item, parent, false);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommunityAddressInfo sortInfo = mListData.get(position);
        holder.tv_name.setText(sortInfo.commname);

        return convertView;
    }

    public void selet(int position) {
        if (selectedPos != position) {
            selectedPos=position;
            notifyDataSetChanged();
        }

    }

    class ViewHolder {
        TextView tv_name;
    }
}
