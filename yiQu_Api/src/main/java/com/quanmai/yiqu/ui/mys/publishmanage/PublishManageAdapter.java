package com.quanmai.yiqu.ui.mys.publishmanage;

import java.util.ArrayList;
import java.util.List;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.share.ShareActivity;
import com.quanmai.yiqu.ui.unused.UnusedDetailActivity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PublishManageAdapter extends BaseAdapter implements OnClickListener {
	ShareActivity mContext;
	List<GoodsBasic> mList;

	ViewHolder holder = null;
	PublishManagerListener mManagerListener = null;

	public PublishManageAdapter(ShareActivity context, PublishManagerListener listener) {
		this.mContext = context;
		mList = new ArrayList<>();
		this.mManagerListener = listener;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GoodsBasic goodsBasic = (GoodsBasic) mList.get(position);
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_publish_manager, null);
			holder = new ViewHolder();
			holder.imageViewGoods = (ImageView) convertView.findViewById(R.id.imageViewGoods);
			holder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
			holder.textViewStatus = (TextView) convertView.findViewById(R.id.textViewStatus);
			holder.textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
			holder.textViewAccessCount = (TextView) convertView.findViewById(R.id.textViewAccessCount);
			holder.buttonHandle = (TextView) convertView.findViewById(R.id.buttonHandle);
			holder.relativeLayoutGoods = (RelativeLayout) convertView.findViewById(R.id.relativeLayoutGoods);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		holder.textViewTitle.setText(goodsBasic.name);
		holder.textViewDate.setText("发布于" + goodsBasic.release_time);
		holder.textViewAccessCount.setText("浏览" + goodsBasic.accesses_count + "次");

		if (goodsBasic.verifyflag == 2) {
			holder.textViewStatus.setText(Html.fromHtml("<font color='#ff796f'>审核未通过<font>"));
		} else {
			Boolean isOpen = goodsBasic.goods_status == 0 ? true : false;
			holder.textViewStatus.setText(isOpen ?
					Html.fromHtml("<font color='#7fd981'>展示中<font>") :
					Html.fromHtml("<font color='#ff796f'>已关闭<font>"));
		}


		if (goodsBasic.img.size() > 0) {
			ImageloaderUtil.displayImage(mContext, goodsBasic.img.get(0), holder.imageViewGoods);
		}

		holder.buttonHandle.setTag(position);
		holder.relativeLayoutGoods.setTag(goodsBasic.id);

		holder.buttonHandle.setOnClickListener(this);
		holder.relativeLayoutGoods.setOnClickListener(this);

		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imageViewGoods: {
//				Intent intent = new Intent(mContext, TransactionDetailActivity.class);\
				Intent intent = new Intent(mContext, UnusedDetailActivity.class);
				intent.putExtra("goods_id", (String) v.getTag());
				mContext.startActivity(intent);
				break;
			}

			case R.id.relativeLayoutGoods: {
//				Intent intent = new Intent(mContext, TransactionDetailActivity.class);
				Intent intent = new Intent(mContext, UnusedDetailActivity.class);
				intent.putExtra("goods_id", (String) v.getTag());
				mContext.startActivity(intent);
				break;
			}

			case R.id.buttonHandle: {
				PublishHandleDialog publishHandleDialog = new PublishHandleDialog(mContext,
						mList.get((Integer) v.getTag()).verifyflag,
						mList.get((Integer) v.getTag()).goods_status == 0 ? true : false,
						new PublishHandleDialog.PublishHandleListener() {

							@Override
							public void refreshStatus(Boolean isOpen, int position) {
								mList.get(position).goods_status = isOpen ? 0 : 1;
								notifyDataSetChanged();
							}

							@Override
							public void refreshAll() {
								mManagerListener.onRefresh();
							}
						});
				publishHandleDialog.showDialog(mList.get((Integer) v.getTag()), (Integer) v.getTag());
				break;
			}

			default:
				break;
		}
	}

	private class ViewHolder {
		private ImageView imageViewGoods;
		private TextView textViewTitle;
		private TextView textViewStatus;
		private TextView textViewDate;
		private TextView textViewAccessCount;
		private TextView buttonHandle;
		private RelativeLayout relativeLayoutGoods;
	}

	public void clear() {
		mList.clear();
		notifyDataSetChanged();
	}

	public void addAll(List<GoodsBasic> goodsBasicList) {
		mList.clear();
		mList.addAll(goodsBasicList);
		notifyDataSetChanged();
	}

	public void add(List<GoodsBasic> goodsBasicList) {
		mList.addAll(goodsBasicList);
		notifyDataSetChanged();
	}

	interface PublishManagerListener {
		public void onRefresh();
	}
}
