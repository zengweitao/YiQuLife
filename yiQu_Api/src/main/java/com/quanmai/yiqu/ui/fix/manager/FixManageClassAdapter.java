package com.quanmai.yiqu.ui.fix.manager;
import java.util.List;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.FixClass;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.MyImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FixManageClassAdapter extends BaseAdapter {
	List<FixClass> mList;
	LayoutInflater inflater;
	Context mContext;

	public FixManageClassAdapter(Context c, List<FixClass> mList) {
		this.mList = mList;
		mContext = c;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public FixClass getItem(int arg0) {
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
			convertView = View.inflate(mContext, R.layout.item_repair_manage, null);
			holder.iv_icon = (MyImageView) convertView
					.findViewById(R.id.iv_icon);
			holder.tv_msgnum = (TextView) convertView
					.findViewById(R.id.tv_msgnum);
			holder.tv_text = (TextView) convertView
					.findViewById(R.id.tv_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FixClass fixClass = mList.get(pos);
		ImageloaderUtil.displayImage(mContext, fixClass.classimage, holder.iv_icon);
		if(fixClass.classcontentnum==0)
			holder.tv_msgnum.setVisibility(View.GONE);
		else
			holder.tv_msgnum.setText(fixClass.classcontentnum+"");
		holder.tv_text.setText(fixClass.classname);
		return convertView;
	}

	class ViewHolder {
		MyImageView iv_icon;
		TextView tv_msgnum;
		TextView tv_text;
	}


}
