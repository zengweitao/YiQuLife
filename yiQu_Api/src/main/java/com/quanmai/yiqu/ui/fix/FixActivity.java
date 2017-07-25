package com.quanmai.yiqu.ui.fix;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.AddressInfo;
import com.quanmai.yiqu.api.vo.FixInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.QiniuUtil;
import com.quanmai.yiqu.common.util.QiniuUtil.OnQiniuUploadListener;
import com.quanmai.yiqu.common.widget.DateSelectionDialog;
import com.quanmai.yiqu.common.widget.DateSelectionDialog.OnDateTimeSetListener;
import com.quanmai.yiqu.ui.selectpic.SelectPicActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**报修提交*/
public class FixActivity extends BaseActivity implements
OnClickListener {
	TextView tv_time, tv_address, tv_problem;
	ImageView iv_img;
	private EditText et_description;
	private CheckBox cb;
	public final int FLAG_QUESTION = 1, FLAG_ADDRESS = 2, FLAG_PIC = 3;
	String problem,problem_id;
	AddressInfo mAddressInfo;
	public List<String> imglist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repair_and_complain);
		init();
	}

	private void init() {
		imglist = new ArrayList<String>();
		((TextView) findViewById(R.id.tv_title)).setText("小区报修");
		TextView tv_right = (TextView) findViewById(R.id.tv_right);
		tv_right.setText("报修记录");
		tv_right.setOnClickListener(this);
		iv_img=(ImageView)findViewById(R.id.iv_img);
		tv_time=(TextView)findViewById(R.id.tv_time);
		tv_problem = ((TextView) findViewById(R.id.tv_problem));
		et_description = (EditText) findViewById(R.id.et_description);
		tv_address=(TextView)findViewById(R.id.tv_address);
		cb = (CheckBox) findViewById(R.id.cb);

		findViewById(R.id.linear_address).setOnClickListener(this);
		findViewById(R.id.linear_time).setOnClickListener(this);
		findViewById(R.id.linear_common_question).setOnClickListener(this);
		findViewById(R.id.tv).setOnClickListener(this);
		findViewById(R.id.btn_sure).setOnClickListener(this);
		findViewById(R.id.iv_img).setOnClickListener(this);
	}

	Dialog timedialog;
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.linear_address:
//			startActivityForResult(AddressChoiceActivity.class,FLAG_ADDRESS);
			break;
		case R.id.linear_time:
			if (timedialog == null) {
				timedialog = new DateSelectionDialog(mContext,
						new OnDateTimeSetListener() {
					public void onDateTimeSet(int year,
							int monthOfYear, int day) {
						tv_time.setText(year + "-" + monthOfYear + "-" + day);
					}
				});
			}
			timedialog.show();
			break;

		case R.id.linear_common_question:
			intent.setClass(this, ProblemChoiceActivity.class);
			startActivityForResult(intent, FLAG_QUESTION);
			break;
		case R.id.tv:
			showCustomToast("条款正在总结中...");
			break;

		case R.id.btn_sure:
			sure();
			break;
		case R.id.tv_right:
			startActivity(FixRecordListActivity.class);
			break;
		case R.id.iv_img:
			imglist.clear();
			intent.setClass(this, SelectPicActivity.class);
			intent.putExtra("type", 2);
			intent.putExtra("max", 1);
			startActivityForResult(intent, FLAG_PIC);
			break;
		default:
			break;

		}
	}

	private void sure() {
		final String description = et_description.getText().toString().trim();
		final String time = tv_time.getText().toString().trim();
		if (mAddressInfo == null) {
			showCustomToast("请选择小区和详细地址");
			return;
		}
		if (time == null) {
			showCustomToast("请选择上门维修时间");
			return;
		}
		if (problem == null) {
			showCustomToast("请选择常见问题");
			return;
		}
		if (description.equals("")) {
			showCustomToast("提交的内容不能为空");
			return;
		}
		if (imglist.size() == 0) {
			showCustomToast("请选一张图片");
			return;
		}
/*		if (!cb.isChecked()) {
			showCustomToast("请勾选条款");
			return;
		}*/

		new QiniuUtil(mContext, imglist, new OnQiniuUploadListener() {

			@Override
			public void success(String names) {
//				System.out.println("names "+names);
				/*FixInfo fixInfo=new FixInfo(mAddressInfo.address_id, time, problem_id, problem, description, names);
				Api.get().FixCommit(mContext, fixInfo, new ApiRequestListener<String>() {

					@Override
					public void onSuccess(String msg, String data) {
						showCustomToast(msg);
						startActivity(FixRecordListActivity.class);
						finish();
					}

					@Override
					public void onFailure(String msg) {
						showCustomToast(msg);
					}
				});
				//		showCustomToast("提交成功，我们将尽快解决您的问题");
				//		startActivity(new Intent(this, FixRecordActivity.class));
				//		finish();*/
			}

			@Override
			public void failure() {
				showShortToast("图片上传失败");
			}
		}).upload();

	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case FLAG_QUESTION:
			if(resultCode==RESULT_OK)
			{
				problem = intent.getStringExtra("problem");
				problem_id=intent.getStringExtra("problem_id");
				tv_problem.setText(problem);
			}
			break;
		case FLAG_ADDRESS:
			if(resultCode==RESULT_OK)
			{
				mAddressInfo = (AddressInfo) intent.getSerializableExtra("addressInfo");
				//tv_address.setText(mAddressInfo.area_name+mAddressInfo.position);
			}
			break;
		case FLAG_PIC:
			if(resultCode==RESULT_OK){
				imglist = intent.getStringArrayListExtra("list");
				File imageFile = new File(imglist.get(0));
				// 显示图片
				Picasso.with(this)
				.load(imageFile)
				.placeholder(R.drawable.icon_nopic)
				//.error(R.drawable.default_error)
				.resize(100, 100)
				.centerCrop()
				.into(iv_img);
			}
			break;
		default:	
			break;
		}
	}
}
