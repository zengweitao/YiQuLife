package com.quanmai.yiqu.ui.fuli;

import java.util.ArrayList;
import java.util.List;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.PicValidateInfo;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class PicValidateAdapter extends BaseAdapter{
	List<PicValidateInfo> mList;
	LayoutInflater inflater;
	Context mContext;

	public PicValidateAdapter(Context c,ArrayList<PicValidateInfo> arrayList) {
		mList = arrayList;
		mContext = c;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public PicValidateInfo getItem(int arg0) {
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
			convertView = View.inflate(mContext, R.layout.item_pic_validate, null);
			holder.iv_image = (ImageView) convertView
					.findViewById(R.id.iv_image);
			holder.imageViewBorder = (ImageView) convertView.findViewById(R.id.imageViewBorder);
			holder.content = (FrameLayout)convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PicValidateInfo info = mList.get(pos);
		if(info.isCheck)
		{
			holder.imageViewBorder.setBackgroundResource(R.drawable.icon_answer_select);
		}else {
			holder.imageViewBorder.setBackground(null);
		}

		ImageloaderUtil.displayImage(mContext, info.image, holder.iv_image);
		holder.imageViewBorder.setTag(info);
		holder.imageViewBorder.setOnClickListener(checkListener);

		
		return convertView;
	}
	
	OnClickListener checkListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			PicValidateInfo info = (PicValidateInfo) v.getTag();
			if(info.isCheck){
				info.isCheck=false;
				((ImageView)v).setBackground(null);
			}else {
				info.isCheck=true;
				((ImageView)v).setBackgroundResource(R.drawable.icon_answer_select);
			}

		}
	};
	
	public String getCheckId() {
		String id=new String();
		for (int i = 0; i < mList.size(); i++) {
			PicValidateInfo info =mList.get(i);
			if(info.isCheck)
			{
				if(id.length()==0)
				{
					id=info.id;
				}else {
					id=id+","+info.id;
				}
				
			}
		}
		return id;

	}

	private class ViewHolder {
		ImageView iv_image;
		ImageView imageViewBorder;
		FrameLayout content;
	}

	public void clear() {
		mList.clear();
	}

	public void add(List<PicValidateInfo> child) {
		mList.addAll(child);
		notifyDataSetChanged();
	}

}
