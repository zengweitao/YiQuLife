package com.quanmai.yiqu.ui.recycle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.RecycleOrderInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.App;
import com.quanmai.yiqu.base.BaseFragmentActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.booking.BookingStatusActivity;
import com.quanmai.yiqu.ui.recycle.RecycleOrderActivity;
import com.quanmai.yiqu.ui.recycle.fragment.RecycleOrderNoTakeFragment;
import com.quanmai.yiqu.ui.recycle.fragment.RecycleOrderUnreceivedFragment;

import java.util.List;

/**
 * Created by zhanjinj on 16/4/18.
 * 回收人员回收订单列表（待接单）适配器
 */
public class OrderUnreceivedAdapter extends BaseAdapter implements View.OnClickListener {
    Context mContext;
    CommonList<RecycleOrderInfo> mList;

    public OrderUnreceivedAdapter(Context context) {
        this.mContext = context;
        mList = new CommonList<>();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_order_unreceived, null);
            viewHolder = new ViewHolder();
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.tvAppointment = (TextView) convertView.findViewById(R.id.tvAppointmentTime);
            viewHolder.btnReceive = (Button) convertView.findViewById(R.id.btnReceive);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvAddress.setText(mList.get(position).address);
        viewHolder.tvAppointment.setText((mList.get(position).rangeDate.replace("-", ".") + " " + mList.get(position).rangeTime));

        viewHolder.btnReceive.setTag(position);
        viewHolder.btnReceive.setOnClickListener(this);

        convertView.findViewById(R.id.btnReceive).setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReceive: {
                int position = (int) v.getTag();
                if (UserInfo.get().getUserid().equals(mList.get(position).userId)) {
                    ((BaseFragmentActivity) mContext).showCustomToast("不能接自己的订单！");
                    return;
                }
                Intent intent = new Intent(mContext,BookingStatusActivity.class);
                intent.putExtra("id",mList.get(position).id);
                intent.putExtra("userId",mList.get(position).userId);
                intent.putExtra("recycler","order");
                mContext.startActivity(intent);
                //confirmAppointmentOrder(mList.get(position).id, mList.get(position).userId, position);
                break;
            }
        }
    }

    public void clean() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<RecycleOrderInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void add(List<RecycleOrderInfo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tvAddress;
        TextView tvAppointment;
        Button btnReceive;
    }

    /**
     * 接单
     *
     * @param id
     * @param position
     */
    private void confirmAppointmentOrder(String id, String userid, final int position) {
        ((BaseFragmentActivity) mContext).showLoadingDialog();

        RecycleApi.get().confirmAppointmentOrder(mContext, id, userid, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                ((BaseFragmentActivity) mContext).dismissLoadingDialog();
                ((BaseFragmentActivity) mContext).showCustomToast(msg);

                mList.remove(position);
                notifyDataSetChanged();

                //接单成功，发送广播刷新“待收取”列表页面
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(RecycleOrderNoTakeFragment.ACTION_NETWORKING_TO_REFRESH_NO_TAKE));
                //接单成功，发送广播通知首页跳转到“待收取”列表
                Intent intent = new Intent(RecycleOrderActivity.ACTION_CHANGE_CURRENT_PAGER);
                intent.putExtra("currentPager", 1);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }

            @Override
            public void onFailure(String msg) {
                //接单失败，发送广播刷新“待接单”列表页面
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(RecycleOrderUnreceivedFragment.ACTION_NETWORKING_TO_REFRESH_UNRECEIVE));

                ((BaseFragmentActivity) mContext).dismissLoadingDialog();
//                ((BaseFragmentActivity) mContext).showCustomToast(msg);
                Toast.makeText(App.getInstance(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
