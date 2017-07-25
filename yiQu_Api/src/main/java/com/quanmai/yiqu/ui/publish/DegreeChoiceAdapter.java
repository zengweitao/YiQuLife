package com.quanmai.yiqu.ui.publish;

import java.util.List;
import com.quanmai.yiqu.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DegreeChoiceAdapter extends BaseAdapter {
	Context mContext;
	List<Integer> degreeList;
	public DegreeChoiceAdapter(Context context, List<Integer> degreeList) {
		this.degreeList = degreeList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return degreeList.size();
	}

	@Override
	public Integer getItem(int arg0) {
		return degreeList.get(arg0);
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
		if(pos!=0)
		holder.tv_address.setText(degreeList.get(pos)+"成新");
		else
			holder.tv_address.setText("全新");
		return convertView;
	}

	class ViewHolder {
		TextView tv_address;
	}

}
