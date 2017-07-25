package com.quanmai.yiqu.ui.recycle.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.RecycleOrderInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.ui.recycle.RecycleOrderDetailActivity;

/**
 * Created by zhanjinj on 16/4/19.
 * 回收人员回收订单列表适配器（已完成）
 */
public class OrderFinishedAdapter extends BaseAdapter implements View.OnClickListener {
    Context mContext;
    CommonList<RecycleOrderInfo> mList;

    public OrderFinishedAdapter(Context context) {
        this.mContext = context;
        this.mList = new CommonList<>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_finished_order, null);
            viewHolder = new ViewHolder();
            viewHolder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tvOrderStatus);
            viewHolder.tvFinishedDate = (TextView) convertView.findViewById(R.id.tvFinishedDate);
            viewHolder.tvAppointmentDate = (TextView) convertView.findViewById(R.id.tvAppointmentDate);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.tvGainPoint = (TextView) convertView.findViewById(R.id.tvGainPoint);
            viewHolder.viewMain = (LinearLayout) convertView.findViewById(R.id.viewMain);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvFinishedDate.setText(DateUtil.timeFormatToMinnue(StringUtil.stringNullFilter(mList.get(position).recycleTime)));
        viewHolder.tvAppointmentDate.setText(mList.get(position).rangeDate.replace("-", ".") + " " + mList.get(position).rangeTime);
        viewHolder.tvAddress.setText(mList.get(position).address);

        if (RecycleApi.orderStatus.cancel.toString().equals(mList.get(position).statue)) {
            viewHolder.tvGainPoint.setText("无");
        } else {
            viewHolder.tvGainPoint.setText(Integer.parseInt(mList.get(position).point) >= 1000 ? "1000" : mList.get(position).point);
        }

        for (RecycleApi.orderStatus status : RecycleApi.orderStatus.values()) {
            if (status.toString().equals(mList.get(position).statue)) {
                viewHolder.tvOrderStatus.setText(status.getStrName());
                viewHolder.tvOrderStatus.setTextColor(mContext.getResources().getColor(status.getColor()));
            }
        }

        viewHolder.viewMain.setTag(mList.get(position).id);
        viewHolder.viewMain.setOnClickListener(this);

        return convertView;
    }

    public void clean() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void add(CommonList<RecycleOrderInfo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(CommonList<RecycleOrderInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewMain: {
                Intent intent = new Intent(mContext, RecycleOrderDetailActivity.class);
                intent.putExtra("orderid", (String) v.getTag());
                mContext.startActivity(intent);
                break;
            }
            default:
                break;
        }
    }


    class ViewHolder {
        TextView tvOrderStatus;
        TextView tvFinishedDate;
        TextView tvAppointmentDate;
        TextView tvAddress;
        TextView tvGainPoint;
        LinearLayout viewMain;
    }
}
