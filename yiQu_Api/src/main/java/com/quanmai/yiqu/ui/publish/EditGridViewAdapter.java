package com.quanmai.yiqu.ui.publish;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.photopicker.utils.BaseImageLoader;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.XCRoundImageViewByXfermode;

import java.util.ArrayList;
import java.util.List;

public class EditGridViewAdapter extends BaseAdapter implements OnClickListener {
	public List<String> list;
	private LayoutInflater inflater;
	private EditUnusedActivity context;
	private final int TAKE_PHOTO_AGAIN = 202;
	OnCloseClick click;
	int count = 0;
	public EditGridViewAdapter(Activity context, List<String> list) {
		this.context = (EditUnusedActivity)context;
		this.list = new ArrayList<>();
		this.list.addAll(list);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		String path = getItem(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.publish_item, null);
			holder = new ViewHolder();
			holder.iv_close = (ImageView) convertView.findViewById(R.id.iv_close);
			holder.iv_picurl = (XCRoundImageViewByXfermode) convertView.findViewById(R.id.iv_picurl);
			holder.iv_picurl.setType(XCRoundImageViewByXfermode.TYPE_ROUND);
			holder.iv_picurl.setRoundBorderRadius(10);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		for (int i=0;i<list.size();i++){
//			if (list.size()>1&&(i+1)==list.size()){
//				holder.iv_picurl.setImageResource(R.drawable.icon_add);
//			}
//		}

		if(path == null){
			holder.iv_picurl.setImageResource(R.drawable.icon_picture_add);
			holder.iv_close.setImageDrawable(null);
			holder.iv_picurl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(context, SelectPicActivity.class);
//					intent.putExtra("type", 1);
//					intent.putExtra("max", 6 - list.size() + 1);
					if (list.size()>0){
						if (list.get(list.size()-1)==null){
							context.chooseHeadImg();
						}
					}else {
						Utils.showCustomToast(context,"最多只能选6张");
					}

//					context.startActivityForResult(intent,TAKE_PHOTO_AGAIN);
				}
			});

		}else{
			if(path.startsWith("http")){
//				Picasso.with(context)
//				.load(path)
//				.placeholder(R.drawable.icon_nopic)
//				//.error(R.drawable.default_error)
//				.resize(300, 300)
//				.centerCrop()
//				.into(holder.iv_picurl);
				ImageloaderUtil.displayImage(context,path,holder.iv_picurl);
			}else{//编辑
				Bitmap bitmap = null;

				bitmap = BaseImageLoader.getInstance().rotatingImageFromFile(path,150,150);
				holder.iv_picurl.setImageBitmap(bitmap);
			}
			holder.iv_close.setImageResource(R.drawable.icon_close_green);
			holder.iv_close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					click.onClose(list.get(position));
					list.remove(position);
					if(getItem(list.size()-1) != null)list.add(null);
					notifyDataSetChanged();
				}
			});
		}

		for (int i=0,count = 0;i<list.size();i++){
			if (list.get(i)==null){
				count++;
			}
			if (count==list.size()){
				click.changView();
				list.clear();
			}
		}

		return convertView;
	}

	public class ViewHolder {
		XCRoundImageViewByXfermode iv_picurl;
		ImageView  iv_close;
	}

	public void clear() {
		list.clear();
	}

	public void reset(ArrayList<String> infos) {
		this.list = infos;
		notifyDataSetChanged();
	}

	public void add(ArrayList<String> infos) {
		list.clear();
		list.addAll(infos);
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void addCloseListener(OnCloseClick closeClick){
		click = closeClick;
	}

	interface OnCloseClick{
		void changView();
		void onClose(String path);
	}
}
