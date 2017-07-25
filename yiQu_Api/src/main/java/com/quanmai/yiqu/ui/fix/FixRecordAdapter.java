package com.quanmai.yiqu.ui.fix;

import java.util.ArrayList;
import java.util.List;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.FixInfo;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FixRecordAdapter extends BaseAdapter {
	List<FixInfo> mList;
	LayoutInflater inflater;
	Context mContext;

	public FixRecordAdapter(Context c) {
		mList = new ArrayList<FixInfo>();
		mContext = c;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public FixInfo getItem(int arg0) {
		return mList.get(arg0);
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
			convertView = View.inflate(mContext, R.layout.item_repair_record,
					null);
			holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
			holder.tv_problem = (TextView) convertView
					.findViewById(R.id.tv_problem);
			holder.tv_phone = (TextView) convertView
					.findViewById(R.id.tv_phone);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			holder.tv_detail = (TextView) convertView
					.findViewById(R.id.tv_detail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FixInfo fixInfo = mList.get(pos);
		ImageloaderUtil.displayImage(mContext, fixInfo.picurl, holder.iv_pic);
		holder.tv_problem.setText("问题：" + fixInfo.description);
		holder.tv_time.setText(fixInfo.come_time);
		holder.tv_phone.setText("手机：" + fixInfo.phone);
		if (fixInfo.result == 0) {
			holder.tv_status.setText("未处理");
		} else if (fixInfo.result == 1) {
			holder.tv_status.setText("处理中");
		} else {
			holder.tv_status.setText("已处理");
		}
		holder.tv_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						FixRecordDetailActivity.class);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView iv_pic;
		TextView tv_problem;
		TextView tv_time;
		TextView tv_phone;
		TextView tv_status;
		TextView tv_detail;
	}

	public void clear() {
		mList.clear();
	}

	public void add(List<FixInfo> child) {
		mList.addAll(child);
		notifyDataSetChanged();
	}

}
