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
import com.quanmai.yiqu.api.vo.SortInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yin on 16/3/9.
 */
public class AddressSortAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommunityAddressInfo> mListData;
    private int selectedPos = -1;

    public AddressSortAdapter(Context context, List<CommunityAddressInfo> groupData) {
        selectedPos = -1;
        mContext = context;
        if (groupData == null) {
            mListData = new ArrayList<CommunityAddressInfo>();
        } else {
            mListData = groupData;
        }

    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public String getItem(int position) {
        return mListData.get(position).commcode;
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
                    R.layout.choose_item, parent, false);
            holder.relay_bg = convertView.findViewById(R.id.relay_bg);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_count = (TextView) convertView
                    .findViewById(R.id.tv_count);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String sortInfo = mListData.get(position).commname;
        holder.tv_name.setText(sortInfo);
        holder.line.setBackgroundColor(Color.parseColor("#cdcdcd"));
        holder.tv_name.setTextColor(Color.parseColor("#575757"));
        if (selectedPos == position) {
            holder.relay_bg.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            holder.relay_bg.setBackgroundColor(Color.parseColor("#f2f2f2"));
        }

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
        TextView tv_count;
        View relay_bg;
        View line;
    }
}
