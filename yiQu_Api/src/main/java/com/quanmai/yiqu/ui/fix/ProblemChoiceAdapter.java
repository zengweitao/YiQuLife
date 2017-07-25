package com.quanmai.yiqu.ui.fix;

import java.util.ArrayList;
import java.util.List;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.ui.fix.ProblemChoiceActivity.Problem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProblemChoiceAdapter extends BaseAdapter {
	List<Problem> list;
	Context mContext;

	public ProblemChoiceAdapter(Context context) {
		this.list = new ArrayList<Problem>();
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Problem getItem(int arg0) {
		return list.get(arg0);
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
			holder.iv_icon = (ImageView) convertView
					.findViewById(R.id.iv_icon);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String text = list.get(pos).problem;
		holder.tv_address.setText(text);
		if("共用设施设备".equals(text))
			holder.iv_icon.setImageResource(R.drawable.icon_gongyongsheshi);
		if("区间绿地".equals(text))
			holder.iv_icon.setImageResource(R.drawable.icon_qujianlvdi);
		if("保洁".equals(text))
			holder.iv_icon.setImageResource(R.drawable.icon_baojie);
		if("公共秩序维护".equals(text))
			holder.iv_icon.setImageResource(R.drawable.icon_gonggongzhixu);
		if("停车管理".equals(text))
			holder.iv_icon.setImageResource(R.drawable.icon_tingche);
		if("消防管理".equals(text))
			holder.iv_icon.setImageResource(R.drawable.icon_xiaofang);
		if("高压供水养护、运行、维修".equals(text))
			holder.iv_icon.setImageResource(R.drawable.icon_gaoyagongshui);
		if("电梯养护、运行、维护".equals(text))
			holder.iv_icon.setImageResource(R.drawable.icon_dianti);
		if("装修管理".equals(text))
			holder.iv_icon.setImageResource(R.drawable.icon_zhuangxiu);
		if("其他".equals(text))
			holder.iv_icon.setImageResource(R.drawable.icon_qita);
		return convertView;
	}

	class ViewHolder {
		TextView tv_address;
		ImageView iv_icon;
	}

	public void Refresh(List<Problem> list) {
		this.list = list;
		notifyDataSetChanged();

	}
}
