package com.quanmai.yiqu.ui.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.UserInfoApi;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.api.vo.ZoneInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.XCRoundImageView;
import com.quanmai.yiqu.ui.comment.MessageActivity;
import com.quanmai.yiqu.ui.transaction.list.TransactionListAdapter;

/**
 * 他的主页
 */
public class TransactionZoneActivity extends BaseActivity {
	private TransactionListAdapter mAdapter;
	protected PullToRefreshListView mListView;
	private TextView iv_no_data;
	private int page = 0;
	private String user_id;
	TextView tv_alias;
	ImageView iv_level;
	XCRoundImageView iv_face;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_zone);

		init();
	}

	private void init() {
		user_id = getIntent().getStringExtra("user_id");
		View header = View.inflate(mContext,
				R.layout.view_transaction_zone_header, null);
		tv_alias = (TextView) header.findViewById(R.id.textViewName);
		iv_level = (ImageView) header.findViewById(R.id.imageViewLevel);
		iv_face = (XCRoundImageView) header.findViewById(R.id.imageViewHeadPortrait);
		iv_face.setImageResource(R.drawable.default_header);
		iv_face.setBorderColor(0x4BFFFFFF);

		mListView = (PullToRefreshListView) findViewById(R.id.list);
		iv_no_data = (TextView) findViewById(R.id.iv_no_data);
		mListView.getRefreshableView().addHeaderView(header);
		mListView.setEmptyView(iv_no_data);
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				Refresh();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				ZoneGoodsList();

			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if (parent.getAdapter().getItem(position) != null) {
					String goods_id = ((GoodsBasic) parent.getAdapter()
							.getItem(position)).id;
					Intent intent = new Intent(mContext,
							TransactionDetailActivity.class);
					intent.putExtra("goods_id", goods_id);
					mContext.startActivity(intent);
				}
			}
		});
		mAdapter = new TransactionListAdapter(mContext);
		mListView.setAdapter(mAdapter);
		View iv_right_2 = findViewById(R.id.iv_right_2);
		iv_right_2.setVisibility(View.VISIBLE);
		iv_right_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				message();
			}
		});
		mListView.setRefreshing();
	}

	private void Refresh() {
		page = 0;
		ZoneGoodsList();
		ZoneDetail();
	}

	private void ZoneDetail() {
		UserInfoApi.get().ZoneDetail(mContext, user_id,
				new ApiRequestListener<ZoneInfo>() {

					@Override
					public void onSuccess(String msg, ZoneInfo data) {
//						((TextView) findViewById(R.id.tv_title)).setText(data.alias + "的空间");
						((TextView) findViewById(R.id.tv_title)).setText("他的主页");
						ImageloaderUtil.displayImage(mContext, data.face, iv_face);
						tv_alias.setText(data.alias);
						iv_level.setImageResource(data.vipInfo.level_img_id);
					}

					@Override
					public void onFailure(String msg) {
						// TODO Auto-generated method stub
						showCustomToast(msg);
					}
				});
	}

	private void ZoneGoodsList() {
		UserInfoApi.get().ZoneGoodsList(mContext, page, user_id,
				new ApiRequestListener<CommonList<GoodsBasic>>() {

					@Override
					public void onSuccess(String msg,
										  CommonList<GoodsBasic> data) {
						mListView.onRefreshComplete();
						if (page == 0) {
							mAdapter.clear();
						}
						mAdapter.add(data);
						if (mAdapter.getCount() == 0) {
							iv_no_data.setText("暂无数据");
						}
						if (data.max_page > data.current_page) {
							mListView.setMode(Mode.BOTH);
						} else {
							mListView.setMode(Mode.PULL_FROM_START);
						}
						page = data.current_page + 1;
					}

					@Override
					public void onFailure(String msg) {
						mListView.onRefreshComplete();
						showCustomToast(msg);
						if (mAdapter.getCount() == 0) {
							iv_no_data.setText(msg);
						}
					}
				});
	}

	private void message() {
		Intent intent = new Intent(mContext, MessageActivity.class);
		intent.putExtra("user_id", user_id);
		startActivityForResult(intent, 1);
	}
}
