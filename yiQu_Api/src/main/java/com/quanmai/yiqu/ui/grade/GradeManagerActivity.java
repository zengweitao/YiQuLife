package com.quanmai.yiqu.ui.grade;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.CheckInApi;
import com.quanmai.yiqu.api.EquipmentApi;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.BarCodeInfo;
import com.quanmai.yiqu.api.vo.InspectionRecordInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.ui.adapter.GradeManagerAdapter;
import com.quanmai.yiqu.ui.code.MipcaActivityCapture;
import com.quanmai.yiqu.ui.mys.record.ManHour;
import com.quanmai.yiqu.ui.mys.record.view.QMonthCellDescriptor;
import com.quanmai.yiqu.ui.mys.record.view.QMonthDescriptor;
import com.quanmai.yiqu.ui.mys.record.view.QMonthView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * 评分管理
 *
 * @author
 */
public class GradeManagerActivity extends BaseActivity implements View.OnClickListener {
	Context mContext;
	Calendar calendar;

	PullToRefreshListView listViewGrade;
	TextView tvInspectionRate, tvInspectionDays, tvDate;

	TextView tv_right;
	GradeManagerAdapter mAdapter;
	Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grade_manager);
		((TextView) findViewById(R.id.tv_title)).setText("巡检记录");
//		tv_right = (TextView) findViewById(R.id.tv_right);
//		tv_right.setText("输入条码");
		mContext = this;

		init();

		mDialog = DialogUtil.getPopupConfirmEditTextDialog(mContext, "输入条码", "确定", "取消", new DialogUtil.OnDialogClickListener() {
			@Override
			public void onClick(String str) {
				if (TextUtils.isEmpty(str)){
					showCustomToast("请输入条码");
				}else {
					takeScore(str);
				}
			}
		});

//		tv_right.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				mDialog.show();
//			}
//		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		getInspectionRecord();
	}

	private void init() {
		listViewGrade = (PullToRefreshListView) findViewById(R.id.listViewGrade);
		listViewGrade.setPullToRefreshOverScrollEnabled(false);
		View headerView = LayoutInflater.from(mContext).inflate(R.layout.view_grade_manager_header, null);
//		View footerView = LayoutInflater.from(mContext).inflate(R.layout.view_grade_manager_footer, null);


		tvInspectionRate = (TextView) headerView.findViewById(R.id.textViewInspectionRate);
		tvInspectionDays = (TextView) headerView.findViewById(R.id.textViewInspectionDays);
		tvDate = (TextView) headerView.findViewById(R.id.textViewDate);

		headerView.findViewById(R.id.imageViewLastMonth).setOnClickListener(this);
		headerView.findViewById(R.id.imageViewNextMonth).setOnClickListener(this);
		findViewById(R.id.buttonScan).setOnClickListener(this);

		calendar = Calendar.getInstance(Locale.getDefault());
		tvDate.setText(calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1));

		listViewGrade.getRefreshableView().addHeaderView(headerView);
//		listViewGrade.getRefreshableView().addFooterView(footerView);
		mAdapter = new GradeManagerAdapter(mContext);
		listViewGrade.setAdapter(mAdapter);

	}

	/**
	 * 获取巡检员月巡检时间记录
	 */
	private void getInspectionRecord() {
		showLoadingDialog("请稍候");
		GradeApi.get().InspectionRecordByMonth(mContext, calendar.get(Calendar.YEAR) + "-" + String.format("%02d", (calendar.get(Calendar.MONTH) + 1)),
				new ApiConfig.ApiRequestListener<InspectionRecordInfo>() {
					@Override
					public void onSuccess(String msg, InspectionRecordInfo data) {
						dismissLoadingDialog();

						InspectionRecordInfo recordInfo = null;
						if (data != null) {
							recordInfo = data;
						} else {
							recordInfo = new InspectionRecordInfo(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
						}

//						if (recordInfo.checkrate != null) {
//							tvInspectionRate.setText(recordInfo.checkrate + "%");
//						} else {
//							tvInspectionRate.setText("0%");
//						}

						if (recordInfo.days != null) {
							tvInspectionDays.setText(String.valueOf(recordInfo.days.size()));
						} else {
							tvInspectionDays.setText("0");
						}

						List<InspectionRecordInfo> list = new ArrayList<InspectionRecordInfo>();
						list.add(recordInfo);
						mAdapter.addAll(list);
					}

					@Override
					public void onFailure(String msg) {
						dismissLoadingDialog();
						showCustomToast(msg);
						List<InspectionRecordInfo> list = new ArrayList<InspectionRecordInfo>();
						list.add(new InspectionRecordInfo(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1));
						mAdapter.addAll(list);
					}
				});
	}

	private void takeScore(final String resultString){
		EquipmentApi.get().BarCode(mContext, resultString,
				new ApiConfig.ApiRequestListener<BarCodeInfo>() {
					@Override
					public void onSuccess(String msg, BarCodeInfo data) {
						dismissLoadingDialog();
						Intent intent = new Intent(mContext,
								ScanResultActivity.class);
						Bundle bundle = new Bundle();
						data.code = resultString;
						bundle.putSerializable("barcodeinfo", data);
						intent.putExtras(bundle);
						startActivity(intent);
						mDialog.dismiss();
					}

					@Override
					public void onFailure(String msg) {
						showCustomToast(msg);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imageViewLastMonth: {
				calendar.add(Calendar.MONTH, -1);
				tvDate.setText(calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1));
				getInspectionRecord();
				break;
			}
			case R.id.imageViewNextMonth: {
				calendar.add(Calendar.MONTH, 1);
				tvDate.setText(calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1));
				getInspectionRecord();
				break;
			}
			case R.id.buttonScan: {
				Intent intent = new Intent(this, MipcaActivityCapture.class);
				intent.putExtra("type", 1);
				startActivity(intent);
				break;
			}

			default:
				break;
		}
	}
}
