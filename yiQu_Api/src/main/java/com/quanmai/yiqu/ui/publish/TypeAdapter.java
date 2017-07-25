package com.quanmai.yiqu.ui.publish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.AreaInfo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TypeAdapter extends BaseAdapter {
	Context mContext;
	List<ProductType> typeList;
	public TypeAdapter(Context context, List<ProductType> typeList) {
		this.typeList = typeList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return typeList.size();
	}

	@Override
	public ProductType getItem(int arg0) {
		return typeList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_address, null);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_address.setText(typeList.get(pos).getClass_name());
		return convertView;
	}

	class ViewHolder {
		TextView tv_address;
	}

}
