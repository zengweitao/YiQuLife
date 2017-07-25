package com.quanmai.yiqu.ui.code;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.BarCodeInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.DialogUtil.OnDialogSelectId;
import com.quanmai.yiqu.common.util.QiniuUtil;
import com.quanmai.yiqu.common.util.QiniuUtil.OnQiniuUploadListener;
import com.quanmai.yiqu.ui.selectpic.SelectPicActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描结果
 */
public class BarCodeActivity extends BaseActivity implements OnClickListener {
	BarCodeInfo barcodeinfo;
	SeekBar seekBar;
	ImageView iv_img;
	LinearLayout ll_img;
	Button btn_submit;
	public List<String> imglist;
	public final int FLAG_PIC = 3;
	
	LinearLayout lt_seek;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bar_code);
		init();
	}

	private void init() {
		((TextView) findViewById(R.id.tv_title)).setText("扫描结果");
		lt_seek=(LinearLayout)findViewById(R.id.lt_seek);
		btn_submit=(Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
		iv_img=(ImageView)findViewById(R.id.iv_img);
		ll_img=(LinearLayout)findViewById(R.id.ll_img);
		ll_img.setOnClickListener(this);
		seekBar=(SeekBar)findViewById(R.id.seekBar);
		((TextView)lt_seek.getChildAt(0)).setTextColor(0xffed1c24);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				for (int i = 0; i < 11; i++) {
					if(progress==i){
						((TextView)lt_seek.getChildAt(i)).setTextColor(0xffed1c24);
					}else {
						((TextView)lt_seek.getChildAt(i)).setTextColor(0xffcfcfcf);
					}
					
				}
				
			}
		});
		barcodeinfo = (BarCodeInfo) getIntent().getSerializableExtra(
				"barcodeinfo");
		if (barcodeinfo != null) {
//			((TextView) findViewById(R.id.tv_code)).setText(barcodeinfo.code);
			((TextView) findViewById(R.id.tv_terminalno))
					.setText(barcodeinfo.terminalno);
//			((TextView) findViewById(R.id.tv_phone)).setText(barcodeinfo.phone);
			((TextView) findViewById(R.id.tv_time)).setText(barcodeinfo.time);
			((TextView) findViewById(R.id.tv_address))
					.setText(barcodeinfo.address);
			findViewById(R.id.btn_submit).setOnClickListener(this);
		}
		imglist = new ArrayList<String>();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			submit();
			break;
		case R.id.ll_img:
			imglist.clear();
			Intent intent = new Intent();
			intent.setClass(this, SelectPicActivity.class);
			intent.putExtra("type", 2);
			intent.putExtra("max", 1);
			startActivityForResult(intent, FLAG_PIC);
			break;
		}
	}

	public void submit() {
		
		DialogUtil.showConfirmDialog(mContext, "是否确定当前打分为最终分值", new OnDialogSelectId() {
			
			@Override
			public void onClick(View v) {
				showLoadingDialog("请稍候");
				final int points=seekBar.getProgress();
				if (imglist.size() == 0) {
					Api.get().Points(mContext, points+"", barcodeinfo.code,null,new ApiRequestListener<String>() {

						@Override
						public void onSuccess(String msg, String data) {
							dismissLoadingDialog();
							showCustomToast(msg);
							finish();
						}
						
						@Override
						public void onFailure(String msg) {
							dismissLoadingDialog();
							showCustomToast(msg);
						}

					});
				}else{
					new QiniuUtil(mContext, imglist, new OnQiniuUploadListener() {

						@Override
						public void success(String names) {
						   
							Api.get().Points(mContext, points+"", barcodeinfo.code,names,new ApiRequestListener<String>() {

										@Override
										public void onSuccess(String msg, String data) {
											dismissLoadingDialog();
											showCustomToast(msg);
											finish();
										}
										
										@Override
										public void onFailure(String msg) {
											dismissLoadingDialog();
											showCustomToast(msg);
										}

									});
						}

						@Override
						public void failure() {
							dismissLoadingDialog();
							showShortToast("图片上传失败");
						}
					}).upload();
				}
				
			}
		});
		
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
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
