package com.quanmai.yiqu.ui.fix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.FixInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;

/** 用户报修记录列表 */
public class FixRecordListActivity extends BaseActivity {
	TextView iv_no_data;
	FixRecordAdapter adapter;
	private PullToRefreshListView listView;
	int page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fix_record);
		initview();
	}

	protected void initview() {
		((TextView) findViewById(R.id.tv_title)).setText("报修记录");
		iv_no_data = (TextView) findViewById(R.id.iv_no_data);
		listView = (PullToRefreshListView) findViewById(R.id.list);
		adapter = new FixRecordAdapter(mContext);
		listView.getRefreshableView().setAdapter(adapter);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				Refresh();

			}

			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getListData();
			}
		});
		listView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						FixInfo fixInfo = (FixInfo) parent.getAdapter()
								.getItem(position);
						Intent intent = new Intent(mContext,
								FixRecordDetailActivity.class);
						intent.putExtra("service_id", fixInfo.id);
						startActivity(intent);
					}
				});
		listView.setEmptyView(iv_no_data);
		listView.setRefreshing();
	}

	private void Refresh() {
		page = 0;
		getListData();
	}

	private void getListData() {
		Api.get().FixRecords(mContext, page,
				new ApiRequestListener<CommonList<FixInfo>>() {

					@Override
					public void onSuccess(String msg, CommonList<FixInfo> data) {
						dismissLoadingDialog();
						listView.onRefreshComplete();
						if (page == 0) {
							adapter.clear();
						}
						adapter.add(data);
						if (adapter.getCount() == 0) {
							iv_no_data.setText("暂无数据");
						}

						if (data.max_page > data.current_page) {
							listView.setMode(Mode.BOTH);
						} else {
							listView.setMode(Mode.PULL_FROM_START);
						}
						page = data.current_page + 1;

					}

					@Override
					public void onFailure(String msg) {
						dismissLoadingDialog();
						listView.onRefreshComplete();
						if (adapter.getCount() == 0) {
							iv_no_data.setText(msg);
						} else {
							showCustomToast(msg);
						}

					}
				});
	}

}