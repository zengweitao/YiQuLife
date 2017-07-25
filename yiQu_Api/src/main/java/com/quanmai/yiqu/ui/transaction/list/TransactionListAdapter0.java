package com.quanmai.yiqu.ui.transaction.list;

import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.unused.UnusedDetailActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TransactionListAdapter0 extends BaseAdapter {

	private LayoutInflater inflater;
	private CommonList<GoodsBasic> mList;
	private Context mContext;

	public TransactionListAdapter0(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		mList = new CommonList<GoodsBasic>();
	}

	@Override
	public int getCount() {
		return (mList.size() + 1) / 2;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.activity_transaction_item_0, null);
			viewHolder = new ViewHolder();
			viewHolder.lt_1 = convertView.findViewById(R.id.lt_1);
			viewHolder.lt_0 = convertView.findViewById(R.id.lt_0);
			viewHolder.tv_img_0 = (ImageView) convertView
					.findViewById(R.id.tv_img_0);
			viewHolder.tv_name_0 = (TextView) convertView
					.findViewById(R.id.tv_name_0);
			viewHolder.tv_price_0 = (TextView) convertView
					.findViewById(R.id.tv_price_0);
			viewHolder.tv_degree_0 = (TextView) convertView
					.findViewById(R.id.tv_degree_0);
			viewHolder.tv_img_1 = (ImageView) convertView
					.findViewById(R.id.tv_img_1);
			viewHolder.tv_name_1 = (TextView) convertView
					.findViewById(R.id.tv_name_1);
			viewHolder.tv_price_1 = (TextView) convertView
					.findViewById(R.id.tv_price_1);
			viewHolder.tv_degree_1 = (TextView) convertView
					.findViewById(R.id.tv_degree_1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		position = position * 2;
		GoodsBasic basic = mList.get(position);
		if (basic.img.size() > 0) {
			viewHolder.tv_img_0.setImageResource(android.R.color.transparent);
			ImageloaderUtil.displayImage(mContext, basic.img.get(0), viewHolder.tv_img_0);
		}
		viewHolder.tv_name_0.setText(basic.name);
		if (basic.type == 1) {
			viewHolder.tv_price_0.setText("捐赠品");
		} else {
			viewHolder.tv_price_0.setText(Utils.getPrice(basic.price));
		}
		if (basic.degree == 10) {
			viewHolder.tv_degree_0.setText("全新");
		} else {
			viewHolder.tv_degree_0.setText(basic.degree + "成新");
		}
		viewHolder.lt_0.setTag(basic.id);
		viewHolder.lt_0.setOnClickListener(onClickListener);
		position = position + 1;
		if (position < mList.size()) {
			viewHolder.lt_1.setVisibility(View.VISIBLE);
			basic = mList.get(position);
			if (basic.img.size() > 0) {
				viewHolder.tv_img_1
						.setImageResource(android.R.color.transparent);
				ImageloaderUtil.displayImage(mContext, basic.img.get(0),
						viewHolder.tv_img_1);
			}
			viewHolder.tv_name_1.setText(basic.name);
//			if (basic.type == 1) {
//				viewHolder.tv_price_1.setText("捐赠品");
//			} else {
				viewHolder.tv_price_1.setText(Utils.getPrice(basic.price));
//			}

			if (basic.degree == 10) {
				viewHolder.tv_degree_1.setText("全新");
			} else {
				viewHolder.tv_degree_1.setText(basic.degree + "成新");
			}
			viewHolder.lt_1.setTag(basic.id);
			viewHolder.lt_1.setOnClickListener(onClickListener);
		} else {
			viewHolder.lt_1.setVisibility(View.INVISIBLE);
			viewHolder.lt_1.setOnClickListener(null);
		}
		return convertView;
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext,
					UnusedDetailActivity.class);
			intent.putExtra("goods_id", (String) v.getTag());
			mContext.startActivity(intent);
		}
	};

	private class ViewHolder {
		private View lt_0, lt_1;
		private ImageView tv_img_0;
		private TextView tv_name_0;
		private TextView tv_price_0;
		private TextView tv_degree_0;
		private ImageView tv_img_1;
		private TextView tv_name_1;
		private TextView tv_price_1;
		private TextView tv_degree_1;
	}

	public void clear() {
		mList.clear();
	}

	public void add(CommonList<GoodsBasic> List) {
		for (int i = 0; i < List.size(); i++) {
			mList.add(List.get(i));
		}
		notifyDataSetChanged();
	}
}
