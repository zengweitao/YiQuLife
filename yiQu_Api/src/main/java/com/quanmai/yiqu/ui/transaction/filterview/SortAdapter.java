package com.quanmai.yiqu.ui.transaction.filterview;

import java.util.ArrayList;
import java.util.List;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.SortInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter {
	private Context mContext;
	private List<SortInfo> mListData;
	private int selectedPos = -1;

	
	public SortAdapter(Context context, List<SortInfo> groupData) {
		selectedPos = -1;
		mContext = context;
		if (groupData == null) {
			mListData = new ArrayList<SortInfo>();
		} else {
			mListData = groupData;
		}

	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public SortInfo getItem(int position) {
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

		SortInfo sortInfo = mListData.get(position);
		holder.tv_name.setText(sortInfo.sort_name);
		if (selectedPos == position) {
			holder.line.setBackgroundColor(0xff81c146);
			holder.tv_name.setTextColor(0xff81c146);
		} else {
			holder.line.setBackgroundColor(0xffececec);
			holder.tv_name.setTextColor(0xff373737);
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
