package com.quanmai.yiqu.ui.recycle.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.BookingApi;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.BookingDetailInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbageInfo;
import com.quanmai.yiqu.api.vo.RecycleOrderInfo;
import com.quanmai.yiqu.base.App;
import com.quanmai.yiqu.base.BaseFragmentActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.RecycleOrderDialog;
import com.quanmai.yiqu.common.widget.XCRoundImageView;
import com.quanmai.yiqu.ui.booking.BookingSecondActivity;
import com.quanmai.yiqu.ui.booking.EditAmountActivity;
import com.quanmai.yiqu.ui.recycle.RecycleOrderActivity;
import com.quanmai.yiqu.ui.recycle.fragment.RecycleOrderFinishedFragment;
import com.quanmai.yiqu.ui.recycle.fragment.RecycleOrderNoTakeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanjinj on 16/4/18.
 * 回收人员回收订单列表（待收取）适配器
 */
public class OrderNotTakeAdapter extends BaseAdapter implements View.OnClickListener {
    public static Integer REQUEST_CODE_EDIT_AMOUNT = 101;

    Context mContext;
    List<RecycleOrderInfo> mList;
    RecycleOrderDialog mOrderDialog;
    Dialog callDialog;

    int currentPosition = 0;
    OnCancleClick mOnCancleClick;

    public void setOnCancleClick(OnCancleClick cancleClick){
        this.mOnCancleClick=cancleClick;
    }
    public OnCancleClick getOnCancleClick(){
        return this.mOnCancleClick;
    }

    public OrderNotTakeAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_order_not_take, null);
            viewHolder = new ViewHolder();
            viewHolder.tvAppointmentTime = (TextView) convertView.findViewById(R.id.tvAppointmentTime);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
            viewHolder.imgHead = (XCRoundImageView) convertView.findViewById(R.id.imgHead);
            viewHolder.btnRecycle = (Button) convertView.findViewById(R.id.btnRecycle);
            viewHolder.button_cancel = (Button) convertView.findViewById(R.id.button_cancel);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvAppointmentTime.setText("预约收取时间  " + mList.get(position).rangeDate.replace("-", ".") + " " + mList.get(position).rangeTime);
        viewHolder.tvAddress.setText(mList.get(position).address);
        viewHolder.tvName.setText(mList.get(position).publisher);
        viewHolder.tvPhone.setText(mList.get(position).mobile);

        viewHolder.imgHead.setImageResource(R.drawable.default_header);
        viewHolder.imgHead.setBorderWidth(Utils.dp2px(mContext, 3));
        viewHolder.imgHead.setBorderColor(0x4BFFFFFF);
        ImageloaderUtil.displayImage(mContext, mList.get(position).portrait, viewHolder.imgHead);

        viewHolder.btnRecycle.setTag(position);
        viewHolder.button_cancel.setTag(position);
        viewHolder.tvPhone.setTag(position);
        viewHolder.btnRecycle.setOnClickListener(this);
        viewHolder.tvPhone.setOnClickListener(this);
        viewHolder.button_cancel.setOnClickListener(this);
        return convertView;
    }

    public void clean() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void add(List<RecycleOrderInfo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(List<RecycleOrderInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateRecycleDetails(String recycleDetails) {
        Type type = new TypeToken<CommonList<RecycleGarbageInfo>>() {
        }.getType();
        CommonList<RecycleGarbageInfo> list = new Gson().fromJson(recycleDetails, type);

        mList.get(currentPosition).recycleDetails = list;
        mOrderDialog.updateRecycleDetails(list);
    }

    public void updatePoint(int point) {
        mList.get(currentPosition).point = point + "";
        if (point >= 1000) {
            point = 1000;
        }
        mOrderDialog.updatePoint(point);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRecycle: {
                int position = (int) v.getTag();
                currentPosition = position;
                Intent intent=new Intent(mContext,BookingSecondActivity.class);
               // Bundle mBundle=new Bundle();
               // mBundle.putSerializable("RecycleOrderInfo",mList.get(position));
                intent.putExtra("isRecycleLocal",false);
                intent.putExtra("RecycleOrderInfo",mList.get(position));
                mContext.startActivity(intent);
                //getRecycleOrderDetail(mList.get(position).id, position);
                break;
            }

            case R.id.button_cancel: {
                int position = (int) v.getTag();
                currentPosition = position;
                cancelBooking(mList.get(position).id,mList.get(position).recycleId+"");
                break;
            }

            case R.id.btnClose: {
                mOrderDialog.dismiss();
                break;
            }

            case R.id.btnCancel: {
                RecycleOrderInfo recycleOrderInfo = mList.get((Integer) v.getTag());
                String strRecycleOrderInfo = new Gson().toJson(recycleOrderInfo);
                try {
                    JSONObject jsonObject = new JSONObject(strRecycleOrderInfo);
                    BookingDetailInfo bookingDetailInfo = new BookingDetailInfo(jsonObject);
                    Intent intent = new Intent(mContext, EditAmountActivity.class);
                    intent.putExtra("points", recycleOrderInfo.point);
                    intent.putExtra("orderId", recycleOrderInfo.id);
                    intent.putExtra("info", bookingDetailInfo);
                    ((FragmentActivity) mContext).startActivityForResult(intent, REQUEST_CODE_EDIT_AMOUNT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case R.id.btnConfirm: {
                RecycleOrderInfo recycleOrderInfo = mList.get((Integer) v.getTag());
                confirmRecycleOrder(recycleOrderInfo);
                break;
            }

            case R.id.tvPhone: {
                final int position = (int) v.getTag();
                callDialog = DialogUtil.getCallDialog(mContext, mList.get(position).mobile, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.buttonConfirm: {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + mList.get(position).mobile));
                                mContext.startActivity(intent);

                                if (callDialog.isShowing()) {
                                    callDialog.dismiss();
                                }
                                break;
                            }
                            case R.id.buttonCancel: {
                                if (callDialog.isShowing()) {
                                    callDialog.dismiss();
                                }
                                break;
                            }
                        }
                    }
                });
                callDialog.show();
            }
            default:
                break;
        }
    }

    class ViewHolder {
        TextView tvAppointmentTime;
        TextView tvAddress;
        TextView tvName;
        TextView tvPhone;
        XCRoundImageView imgHead;
        Button btnRecycle;
        Button button_cancel;
    }

    public interface OnCancleClick{
        public void OnCancle();
    }


    /**
     * 获取订单详情，显示回收弹窗
     *
     * @param orderid
     */
    private void getRecycleOrderDetail(String orderid, final int position) {
        ((BaseFragmentActivity) mContext).showLoadingDialog();

        RecycleApi.get().getRecycleOrderDetail(mContext, orderid, new ApiConfig.ApiRequestListener<RecycleOrderInfo>() {
            @Override
            public void onSuccess(String msg, RecycleOrderInfo data) {
                ((BaseFragmentActivity) mContext).dismissLoadingDialog();

                if (data == null) {
                    return;
                }

                if (RecycleApi.orderStatus.verified.toString().equals(data.statue)) {
                    mList.set(currentPosition, data);
                    mOrderDialog = new RecycleOrderDialog(mContext, "您将回收", position, data, data.point, OrderNotTakeAdapter.this);
                    mOrderDialog.show();
                } else {
                    Utils.showCustomToast(mContext, "该订单已被用户取消");
                    mList.remove(currentPosition);
                    notifyDataSetChanged();
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(RecycleOrderActivity.ACTION_REFRESH_RECYCLE_ORDER_NUM));
                }
            }

            @Override
            public void onFailure(String msg) {
                ((BaseFragmentActivity) mContext).dismissLoadingDialog();
//                ((BaseFragmentActivity) mContext).showCustomToast(msg);
                Toast.makeText(App.getInstance(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 确认回收
     *
     * @param recycleOrderInfo
     */
    private void confirmRecycleOrder(RecycleOrderInfo recycleOrderInfo) {
        ((BaseFragmentActivity) mContext).showLoadingDialog();

        String recycleDetails = new Gson().toJson(recycleOrderInfo.recycleDetails);
        if (!recycleDetails.contains("pic")) recycleDetails = "";


        RecycleApi.get().confirmRecycleOrder(mContext, recycleOrderInfo.id, recycleOrderInfo.userId, recycleOrderInfo.point, recycleDetails,
                new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        mOrderDialog.dismiss();
                        ((BaseFragmentActivity) mContext).dismissLoadingDialog();
                        ((BaseFragmentActivity) mContext).showCustomToast(msg);

                        mList.remove(currentPosition);
                        notifyDataSetChanged();

                        //回收成功，发送广播刷新“已完成”列表页面
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(RecycleOrderFinishedFragment.ACTION_NETWORKING_TO_REFRESH_FINISH));
                        //回收成功，发送广播通知首页跳转到“已完成”列表
                        Intent intent = new Intent(RecycleOrderActivity.ACTION_CHANGE_CURRENT_PAGER);
                        intent.putExtra("currentPager", 2);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mOrderDialog.dismiss();
                        ((BaseFragmentActivity) mContext).dismissLoadingDialog();
                        ((BaseFragmentActivity) mContext).showCustomToast(msg);

                        //接单失败，发送广播刷新“待收取”列表页面
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(RecycleOrderNoTakeFragment.ACTION_NETWORKING_TO_REFRESH_NO_TAKE));
                    }
                });
    }
    //取消预约
    private void cancelBooking(String id,String recycleId){
        ((BaseFragmentActivity) mContext).showLoadingDialog();
        BookingApi.get().CancelBookings(mContext, id,recycleId, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                ((BaseFragmentActivity) mContext).dismissLoadingDialog();
                ((BaseFragmentActivity) mContext).showCustomToast("已取消");
                getOnCancleClick().OnCancle();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                ((BaseFragmentActivity) mContext).showCustomToast(msg);
            }
        });
    }
}
