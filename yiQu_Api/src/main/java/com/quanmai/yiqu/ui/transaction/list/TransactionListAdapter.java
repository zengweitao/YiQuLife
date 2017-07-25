package com.quanmai.yiqu.ui.transaction.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.ui.unused.UnusedDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanjinj on 16/3/10.
 */
public class TransactionListAdapter extends BaseAdapter {

	int mWidth;

	Context mContext;
	List<GoodsBasic> mList;


	public TransactionListAdapter(Context context) {
		this.mContext = context;
		this.mList = new ArrayList<>();

		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mWidth = (metrics.widthPixels - Utils.dp2px(mContext, 40)) / 3;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_activity_transaction, null);
			viewHolder = new ViewHolder();
			viewHolder.textViewDescription = (TextView) convertView.findViewById(R.id.textViewDescription);
			viewHolder.textViewPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
			viewHolder.textViewDegree = (TextView) convertView.findViewById(R.id.textViewDegree);
			viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
			viewHolder.linearLayoutImgMain = (LinearLayout) convertView.findViewById(R.id.linearLayoutImgMain);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		GoodsBasic goodsBasic = mList.get(position);

		viewHolder.textViewDescription.setText(goodsBasic.name);
		viewHolder.textViewPrice.setText("￥" + String.valueOf(goodsBasic.price));
		viewHolder.textViewDegree.setText(String.valueOf(goodsBasic.degree) + "成新");
		viewHolder.textViewDate.setText(String.valueOf(goodsBasic.release_time));
//		viewHolder.textViewCommunity.setText(String.valueOf(goodsBasic.price));
		viewHolder.linearLayoutImgMain.removeAllViews();
		if (goodsBasic.img.size() > 0) {
			for (int i = 0; i < goodsBasic.img.size(); i++) {
				ImageView imageView = new ImageView(mContext);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mWidth, mWidth);
				params.rightMargin = Utils.dp2px(mContext, 5);
				viewHolder.linearLayoutImgMain.addView(imageView, params);
				ImageloaderUtil.displayImage(mContext, goodsBasic.img.get(i), imageView);
			}
		}
		viewHolder.linearLayoutImgMain.setTag(goodsBasic.id);
		viewHolder.linearLayoutImgMain.setOnClickListener(goodsClickListener);

		return convertView;
	}


	private class ViewHolder {
		private TextView textViewDescription;
		private TextView textViewPrice;
		private TextView textViewDegree;
		private TextView textViewCommunity;
		private TextView textViewDate;
		private LinearLayout linearLayoutImgMain;
	}

	public void clear() {
		mList.clear();
		notifyDataSetChanged();
	}

	public void add(List<GoodsBasic> goodsBasics) {
		mList.addAll(goodsBasics);
		notifyDataSetChanged();
	}

	View.OnClickListener goodsClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext,
					UnusedDetailActivity.class);
			intent.putExtra("goods_id", (String) v.getTag());
			mContext.startActivity(intent);

		}
	};
}
