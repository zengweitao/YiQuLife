package com.quanmai.yiqu.ui.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.AdvertInfo;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

import java.util.ArrayList;
import java.util.List;

public class DraggableGridViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<AdvertInfo> myList;
	Context mContext;
	public DraggableGridViewAdapter(Context context) {
		mContext=context;
		this.myList = new ArrayList<AdvertInfo>();
		mInflater = LayoutInflater.from(context);
	}

	public DraggableGridViewAdapter(Context context, List<AdvertInfo> List) {
		this.myList = List;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public AdvertInfo getItem(int arg0) {
		return myList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.draggable_grid_item, null);
			holder = new ViewHolder();
			holder.picurl = (ImageView) convertView.findViewById(R.id.picurl);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
			AdvertInfo info = getItem(position);
			//设置资源
			if (info.picId!=0){
				holder.picurl.setImageResource(info.picId);
			}else {
				ImageloaderUtil.displayImage(mContext, info.picurl, holder.picurl);
			}
			holder.name.setText(info.name);
		return convertView;
	}

	class ViewHolder {
		private ImageView picurl;
		private TextView name;
	}

	public void Refresh() {
		myList.clear();
	}

	public void add(List<AdvertInfo> arrayList) {
		for (int i = 0; i < arrayList.size(); i++) {
			myList.add(arrayList.get(i));
		}
		notifyDataSetChanged();
	}

	// public interface onListClickListener {
	// public void onItemClick(ProjectItemInfo info);
	// }

}
