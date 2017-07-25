package com.quanmai.yiqu.ui.fix.manager;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.FixClass;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
/**
 * 物业（管理员）
 * @author Administrator
 *
 */
public class FixManageClassActivity extends BaseActivity{
	TextView tv_gongyongsheshi, tv_gaoyagongshui, tv_qujianlvdi, tv_xiaofang, tv_tingche, tv_zhuangxiu, tv_baojie, tv_dianti, tv_gonggongzhixu, tv_qita; 
	PullToRefreshGridView gridview;
	List<FixClass> list;
	FixManageClassAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fix_manage);
		initview();
		showLoadingDialog("请稍候");
		getdata();
	}

	private void getdata() {
		 Api.get().FixManagerClass(mContext, new ApiRequestListener<CommonList<FixClass>>(){

			@Override
			public void onSuccess(String msg, CommonList<FixClass> data) {
				dismissLoadingDialog();
				gridview.onRefreshComplete();
				adapter = new FixManageClassAdapter(mContext, data);
				gridview.setAdapter(adapter);
			}

			@Override
			public void onFailure(String msg) {
				dismissLoadingDialog();
				gridview.onRefreshComplete();
				showCustomToast(msg);
			}
			 
		 });
	}

	private void initview() {
		((TextView) findViewById(R.id.tv_title)).setText("保修管理");
		gridview = (PullToRefreshGridView) findViewById(R.id.gridview);
		gridview.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				getdata();
				
			}
		});
		gridview.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, FixManageRecordListActivity.class);
				intent.putExtra("classcode", adapter.getItem(position).classcode);
				intent.putExtra("classname", adapter.getItem(position).classname);
				startActivity(intent);
			}
		});
	}

}