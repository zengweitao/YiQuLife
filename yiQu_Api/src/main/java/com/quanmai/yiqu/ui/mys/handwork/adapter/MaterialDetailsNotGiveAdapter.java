package com.quanmai.yiqu.ui.mys.handwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.MaterialOrUserDetailsInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.ui.mys.handwork.ScanningActivity;

/**
 * 物料详情列表适配器
 * Created by James on 2016/10/17.
 */

public class MaterialDetailsNotGiveAdapter extends BaseAdapter {
    Context mContext;
    CommonList<MaterialOrUserDetailsInfo.UsercompareListBean> mListU;

    String mCommname;

    public MaterialDetailsNotGiveAdapter(Context context) {
        this.mContext = context;
        if (mListU == null) {
            mListU = new CommonList<>();
        }

    }
    public void setCommname(String commname){
        mCommname = commname;
    }
    public String getCommname(){
        return this.mCommname;
    }

    @Override
    public int getCount() {
        return mListU.size();
    }

    @Override
    public Object getItem(int position) {
        return mListU.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_material_notgive, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mListU!=null&&mListU.size()>0){
            final MaterialOrUserDetailsInfo.UsercompareListBean info = mListU.get(position);
            viewHolder.ivRight.setVisibility(View.VISIBLE);
            viewHolder.tvName.setText(info.getName());
            viewHolder.tvPhone.setText(info.getMtel().trim());
            viewHolder.tvStatus.setText("去发放");
            viewHolder.tvStatus.setTextColor(Color.parseColor("#6cbf9c"));
            viewHolder.tvAddress.setText(info.getHoursenum());

            viewHolder.llStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ScanningActivity.class);
                    intent.putExtra("UsercompareListBean", info);
                    intent.putExtra("Commname",getCommname());
                    intent.putExtra("type", ScanningActivity.TYPE_GRANT_BAG);
                    mContext.startActivity(intent);
                }
            });
        }
        /*viewHolder.tvTime.setText(info.opetime.trim().replaceAll(" ", "\n"));
        if (TextUtils.isEmpty(info.place)) { //地址为空，显示横线并居中
            viewHolder.tvAddress.setText("--");
            viewHolder.tvAddress.setGravity(Gravity.CENTER);
        } else {
            viewHolder.tvAddress.setText(info.place.trim());
        }

        if ("1".equals(info.materialsource)) {
            viewHolder.tvStatus.setText("接收");
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.text_color_fe5236));
        } else {
            viewHolder.tvStatus.setText("发放");
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.theme));
        }

        String strBagType = "";
        switch (info.bagtype) {//垃圾袋类型:（1.厨余垃圾 2其他垃圾 3.有害垃圾 4.可回收）
            case "1": {
                strBagType = "厨余垃圾";
                break;
            }
            case "2": {
                strBagType = "其他垃圾";
                break;
            }
            case "3": {
                strBagType = "有害垃圾";
                break;
            }
            case "4": {
                strBagType = "可回收垃圾";
                break;
            }
            case "5": {
                strBagType = "通用";
                break;
            }
            case "6": {
                strBagType = "通用【带收紧带】";
                break;
            }
            default: {
                strBagType = "厨余垃圾";
            }
        }

        if ("1".equals(info.bagtype)) {
            viewHolder.tvNumAndType.setText(StringUtil.changeLocalTextColor(info.bagnums + "\n" + strBagType,
                    "#64D79A", info.bagnums.length(), (info.bagnums + "\n" + strBagType).length()));
        } else {
            viewHolder.tvNumAndType.setText(StringUtil.changeLocalTextColor(info.bagnums + "\n" + strBagType,
                    "#FF8E86", info.bagnums.length(), (info.bagnums + "\n" + strBagType).length()));
        }*/

        return convertView;
    }

    public void clear() {
        mListU.clear();
        notifyDataSetChanged();
    }

    public void addAll(CommonList<MaterialOrUserDetailsInfo.UsercompareListBean> listU) {
        this.mListU.addAll(listU);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView tvName;
        public TextView tvPhone;
        public TextView tvStatus;
        public TextView tvAddress;
        public ImageView ivRight;
        public LinearLayout llStatus;

        public ViewHolder(View rootView) {
            this.tvName = (TextView) rootView.findViewById(R.id.tvName);
            this.tvPhone = (TextView) rootView.findViewById(R.id.tvPhone);
            this.tvAddress = (TextView) rootView.findViewById(R.id.tvAddress);
            this.tvStatus = (TextView) rootView.findViewById(R.id.tvStatus);
            this.ivRight = (ImageView) rootView.findViewById(R.id.ivRight);
            this.llStatus = (LinearLayout) rootView.findViewById(R.id.llStatus);
        }
    }
}
