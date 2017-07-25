package com.quanmai.yiqu.ui.mys.handwork.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.MaterialOrUserDetailsInfo;
import com.quanmai.yiqu.base.CommonList;

/**
 * 物料详情列表适配器
 * Created by James on 2016/10/17.
 */

public class MaterialDetailsPutWareHouseAdapter extends BaseAdapter {
    Context mContext;
    CommonList<MaterialOrUserDetailsInfo.InventoryListBean> mListI;

    public MaterialDetailsPutWareHouseAdapter(Context context) {
        this.mContext = context;
        mListI = new CommonList<>();
    }

    @Override
    public int getCount() {
        return mListI.size();
    }

    @Override
    public Object getItem(int position) {
        return mListI.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_material_putwarehouse, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mListI!=null&&mListI.size()>0) {
            MaterialOrUserDetailsInfo.InventoryListBean info = mListI.get(position);
            viewHolder.tvType.setVisibility(View.VISIBLE);
            viewHolder.tvTime.setText(info.getOpetime().trim().replaceAll(" ", "\n"));
            viewHolder.tvAmount.setText(info.getIssuenums()+"");
            viewHolder.tvType.setText(info.getBagtype());
            if (info.getBagtype().contains("厨余")){
                viewHolder.tvType.setTextColor(Color.parseColor("#48C299"));
            }else if (info.getBagtype().contains("其他")){
                viewHolder.tvType.setTextColor(Color.parseColor("#FFEC00"));
            }else if (info.getBagtype().contains("有害")){
                viewHolder.tvType.setTextColor(Color.parseColor("#FE5246"));
            }else if (info.getBagtype().contains("可回收")){
                viewHolder.tvType.setTextColor(Color.parseColor("#EB72B5"));
            }else if (info.getBagtype().contains("通用")){
                viewHolder.tvType.setTextColor(Color.parseColor("#EB72B5"));
            }else if (info.getBagtype().contains("带收紧带")){
                viewHolder.tvType.setTextColor(Color.parseColor("#EB72B5"));
            }
            viewHolder.tvBagCode.setText(info.getStartnum());
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
        mListI.clear();
        notifyDataSetChanged();
    }

    public void addAll(CommonList<MaterialOrUserDetailsInfo.InventoryListBean> listI) {
        this.mListI.addAll(listI);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView tvTime;
        public TextView tvAmount;
        public TextView tvType;
        public TextView tvBagCode;

        public ViewHolder(View rootView) {
            this.tvTime = (TextView) rootView.findViewById(R.id.tvTime);
            this.tvAmount = (TextView) rootView.findViewById(R.id.tvAmount);
            this.tvType = (TextView) rootView.findViewById(R.id.tvType);
            this.tvBagCode = (TextView) rootView.findViewById(R.id.tvBagCode);
        }
    }
}
