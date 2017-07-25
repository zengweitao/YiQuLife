package com.quanmai.yiqu.ui.unused;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.transaction.list.TransactionListAdapter0;

public class UnusedListActivity extends BaseActivity implements View.OnClickListener {

	RelativeLayout relativeLayoutTimeSorting,
			relativeLayoutPriceSorting, relativeLayoutDegreeSorting;
	PullToRefreshListView mListView;
	TextView textViewTime, textViewPrice, textViewDegree;
	TextView tv_title;
	String class_id;
	UnusedFragmentAdapter mAdapter;
	LinearLayout linear_no_data;
	int page = 0;
	String sort_type = "3"; //默认按照时间排序

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unused_list);
		class_id = getIntent().getStringExtra("class_id");

		init();
	}


	private void init() {
		relativeLayoutTimeSorting = (RelativeLayout) findViewById(R.id.relativeLayoutTimeSorting);
		relativeLayoutPriceSorting = (RelativeLayout) findViewById(R.id.relativeLayoutPriceSorting);
		relativeLayoutDegreeSorting = (RelativeLayout) findViewById(R.id.relativeLayoutDegreeSorting);
		textViewTime = (TextView) findViewById(R.id.textViewTime);
		textViewPrice = (TextView) findViewById(R.id.textViewPrice);
		textViewDegree = (TextView) findViewById(R.id.textViewDegree);
		linear_no_data = (LinearLayout) findViewById(R.id.linear_no_data);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getIntent().getStringExtra("class_name"));

		relativeLayoutTimeSorting.setOnClickListener(this);
		relativeLayoutPriceSorting.setOnClickListener(this);
		relativeLayoutDegreeSorting.setOnClickListener(this);
		mListView = (PullToRefreshListView) findViewById(R.id.list);
		mListView.setEmptyView(linear_no_data);
		mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				Refresh();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				GoodsList();

			}
		});
		mAdapter = new UnusedFragmentAdapter(mContext);
		mListView.setAdapter(mAdapter);
		mListView.setRefreshing();
	}

	private void Refresh() {
		page = 0;
		GoodsList();
	}

	private void GoodsList() {
		GoodsApi.get().GoodsList(mContext, page, "", "", class_id,
				sort_type, new ApiConfig.ApiRequestListener<CommonList<GoodsBasic>>() {


					@Override
					public void onSuccess(String msg,
										  CommonList<GoodsBasic> data) {
						dismissLoadingDialog();
						mListView.onRefreshComplete();
						if (page == 0) {
							mAdapter.clear();
						}
						mAdapter.add(data);
						if (mAdapter.getCount() == 0) {

						}
						if (data.max_page > data.current_page) {
							mListView.setMode(PullToRefreshBase.Mode.BOTH);
						} else {
							if (mAdapter.getCount()>0){
								Utils.showCustomToast(UnusedListActivity.this, "已到最后");
							}
							mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
						}
						page = data.current_page + 1;
					}

					@Override
					public void onFailure(String msg) {
						dismissLoadingDialog();
						showCustomToast(msg);
						mListView.onRefreshComplete();
						if (page == 0) {
							mAdapter.clear();
							mAdapter.notifyDataSetChanged();
						}
						if (mAdapter.getCount() == 0) {

						} else {

						}
					}
				});


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//时间排序
			case R.id.relativeLayoutTimeSorting: {
				showLoadingDialog("请稍候");
				textViewTime.setTextColor(getResources().getColor(R.color.theme));
				textViewPrice.setTextColor(getResources().getColor(R.color.text_color_default));
				textViewDegree.setTextColor(getResources().getColor(R.color.text_color_default));
				sort_type = "3";
				Refresh();
				break;
			}
			//价格排序
			case R.id.relativeLayoutPriceSorting: {
				showLoadingDialog("请稍候");
				textViewTime.setTextColor(getResources().getColor(R.color.text_color_default));
				textViewPrice.setTextColor(getResources().getColor(R.color.theme));
				textViewDegree.setTextColor(getResources().getColor(R.color.text_color_default));
				sort_type = "1";
				Refresh();
				break;
			}
			//成色排序
			case R.id.relativeLayoutDegreeSorting: {
				showLoadingDialog("请稍候");
				textViewTime.setTextColor(getResources().getColor(R.color.text_color_default));
				textViewPrice.setTextColor(getResources().getColor(R.color.text_color_default));
				textViewDegree.setTextColor(getResources().getColor(R.color.theme));
				sort_type = "2";
				Refresh();
				break;
			}
			default:
				break;
		}
	}
}
