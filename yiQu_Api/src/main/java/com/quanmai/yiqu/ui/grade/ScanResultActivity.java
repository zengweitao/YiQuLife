package com.quanmai.yiqu.ui.grade;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.BarCodeInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.photopicker.PhotoPickerActivity;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageCompressUtil;
import com.quanmai.yiqu.common.util.PhotoUtil;
import com.quanmai.yiqu.common.util.QiniuUtil;
import com.quanmai.yiqu.common.widget.CameraProtectActivity;
import com.quanmai.yiqu.common.widget.CustomDialog;

import java.util.ArrayList;

/**
 * 扫描结果-打分界面
 */
public class ScanResultActivity extends BaseActivity implements View.OnClickListener {

    RadioGroup radioGroup1, radioGroup2;
    TextView textViewDeviceNumber, textViewTakeDate, textViewAddress;
    RelativeLayout relativeLayoutAddPicture;
    Button buttonSubmit,button_label_01,button_label_02,button_label_03,button_label_04;
    TextView tv_title;
    //    ImageView imageViewPicture;
    LinearLayout linearLayoutPhoto,linearLayoutContent;

    BarCodeInfo barcodeinfo;
    int mScore = 0;
    final int FLAG_PIC = 3;
    ArrayList<String> imglist;
    Dialog chooseDialog;
    private final int TAKE_PHOTO = 201;  //选择照片
    private final int TAKE_PICTURE = 202;//拍照
    private CustomDialog mCustomDialog;
    private EditText edittext_comment;
    String str_comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        barcodeinfo = (BarCodeInfo) getIntent().getSerializableExtra("barcodeinfo");

        init();
        initDialog();
        initRadioGroup(-1);
    }

    //初始化view
    private void init() {
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        textViewDeviceNumber = (TextView) findViewById(R.id.textViewDeviceNumber);
        textViewTakeDate = (TextView) findViewById(R.id.textViewTakeDate);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        relativeLayoutAddPicture = (RelativeLayout) findViewById(R.id.relativeLayoutAddPicture);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        tv_title = (TextView) findViewById(R.id.tv_title);
//        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        linearLayoutPhoto = (LinearLayout) findViewById(R.id.linearLayoutPhoto);
        linearLayoutContent = (LinearLayout) findViewById(R.id.linearLayoutContent);

        button_label_01= (Button) findViewById(R.id.button_label_01);
        button_label_02= (Button) findViewById(R.id.button_label_02);
        button_label_03= (Button) findViewById(R.id.button_label_03);
        button_label_04= (Button) findViewById(R.id.button_label_04);
        edittext_comment = (EditText) findViewById(R.id.edittext_comment);
        edittext_comment.setSelection(edittext_comment.getText().length());

        button_label_01.setOnClickListener(this);
        button_label_02.setOnClickListener(this);
        button_label_03.setOnClickListener(this);
        button_label_04.setOnClickListener(this);

        relativeLayoutAddPicture.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        tv_title.setText("扫描结果");
        if (barcodeinfo != null) {
            textViewDeviceNumber.setText(barcodeinfo.terminalno);
            textViewTakeDate.setText(barcodeinfo.time);
            textViewAddress.setText(barcodeinfo.address);
        }
        imglist = new ArrayList<String>();
    }

    private void initDialog(){
        mCustomDialog = new CustomDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否上传图片凭证？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCustomDialog.dismiss();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCustomDialog.dismiss();
                        showLoadingDialog("请稍候");
                        GradeApi.get().Points(mContext, mScore + "", barcodeinfo.code, null,edittext_comment.getText().toString(), new ApiConfig.ApiRequestListener<String>() {

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
                })
                .create();
    }

    //星星初始化
    private void initRadioGroup(int temp) {

        for (int j = 0; j < radioGroup2.getChildCount(); j++) {
            final int tempI = j + 5;
            RadioButton radioButton = (RadioButton) radioGroup2.getChildAt(j);
            if (temp > 0 && temp >= 5) {
                if (tempI <= temp) {
                    radioButton.setChecked(true);
                }
            }
            setStar(radioButton, tempI, radioButton.isChecked());
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioGroup1.clearCheck();
                    radioGroup2.clearCheck();
                    mScore = tempI + 1;
                    initRadioGroup(tempI);
                }
            });
        }

        for (int i = 0; i < radioGroup1.getChildCount(); i++) {
            final int tempI = i;
            RadioButton radioButton = (RadioButton) radioGroup1.getChildAt(i);
            if (temp >= 0) {
                if (tempI <= temp) {
                    radioButton.setChecked(true);
                }
            }
            setStar(radioButton, tempI, radioButton.isChecked());
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioGroup2.clearCheck();
                    radioGroup1.clearCheck();
                    mScore = tempI + 1;
                    initRadioGroup(tempI);
                }
            });
        }


    }

    //星星selected
    private void setStar(RadioButton view, int i, boolean isChecked) {
        switch (i) {
            case 0: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.one_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_one);
                }
                break;
            }

            case 1: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.two_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_two);
                }
                break;
            }

            case 2: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.three_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_three);
                }
                break;
            }

            case 3: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.four_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_four);
                }
                break;
            }
            case 4: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.five_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_five);
                }
                break;
            }
            case 5: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.six_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_six);
                }
                break;
            }
            case 6: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.seven_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_seven);
                }
                break;
            }
            case 7: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.eight_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_eight);
                }
                break;
            }
            case 8: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.nine_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_nine);
                }
                break;
            }
            case 9: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.ten_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_ten);
                }
                break;
            }
            default:
                break;
        }
    }

    private void addPicture(ArrayList<String> list){
        if (list.size()<0){
            return;
        }
        linearLayoutContent.removeAllViews();
        for (int i=0;i<list.size();i++){
            final String url = list.get(i);
            final View mPictureView = LayoutInflater.from(this).inflate(R.layout.scan_result_photo_item,null);

            ImageView imageViewPicture = (ImageView) mPictureView.findViewById(R.id.imageViewPicture);
            ImageView imageViewDeleteButton = (ImageView)mPictureView.findViewById(R.id.imageViewDeleteButton);

            imageViewPicture.setImageBitmap(ImageCompressUtil.compressByQuality(ImageCompressUtil.compressBySize(url, 400, 400), 200));

            imageViewDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imglist.remove(url);
                    linearLayoutContent.removeView(mPictureView);
                    relativeLayoutAddPicture.setVisibility(View.VISIBLE);
                }
            });

            linearLayoutContent.addView(mPictureView);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //添加图片
            case R.id.relativeLayoutAddPicture: {
                chooseHeadImg();
                break;
            }

            case R.id.btn_local_photo: {
                Intent intent = new Intent(this, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, false);
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
                startActivityForResult(intent, TAKE_PHOTO);
                chooseDialog.cancel();
                break;
            }
            //相册挑选
            case R.id.btn_take_photos: {
                if (PhotoUtil.isSdcardExisting()) {
                    Intent intent = new Intent(this, CameraProtectActivity.class);
                    intent.putStringArrayListExtra("imgList", imglist);
                    startActivityForResult(intent, TAKE_PICTURE);
                } else {
                    showCustomToast("请插入SD卡");
                }
                chooseDialog.cancel();
                break;
            }
            //取消
            case R.id.btn_cancel: {
                chooseDialog.cancel();
                break;
            }
            //提交
            case R.id.buttonSubmit: {
                if (edittext_comment.getText().length()>50){
                    showShortToast("评论字数请不要超过50字！");
                    return;
                }
                submit();
                break;
            }
            case R.id.button_label_01: {
                str_comments=edittext_comment.getText().toString()+button_label_01.getText().toString();
                edittext_comment.setText(str_comments);
                edittext_comment.setSelection(edittext_comment.getText().length());
                break;
            }
            case R.id.button_label_02: {
                str_comments=edittext_comment.getText().toString()+button_label_02.getText().toString();
                edittext_comment.setText(str_comments);
                edittext_comment.setSelection(edittext_comment.getText().length());
                break;
            }
            case R.id.button_label_03: {
                str_comments=edittext_comment.getText().toString()+button_label_03.getText().toString();
                edittext_comment.setText(str_comments);
                edittext_comment.setSelection(edittext_comment.getText().length());
                break;
            }
            case R.id.button_label_04: {
                str_comments=edittext_comment.getText().toString()+button_label_04.getText().toString();
                edittext_comment.setText(str_comments);
                edittext_comment.setSelection(edittext_comment.getText().length());
                break;
            }
            default:
                break;
        }
    }

    //添加照片
    public void chooseHeadImg() {
        if (chooseDialog == null) {
            chooseDialog = new Dialog(mContext, R.style.MyDialogStyle);
            chooseDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(0));
            chooseDialog.setCanceledOnTouchOutside(true);
            chooseDialog.setContentView(R.layout.dialog_choose_head_img);
            chooseDialog.findViewById(R.id.btn_local_photo).setOnClickListener(this);
            chooseDialog.findViewById(R.id.btn_take_photos).setOnClickListener(this);
            chooseDialog.findViewById(R.id.btn_cancel).setOnClickListener(this);

            Window window = chooseDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            window.setAttributes(params);
        }
        chooseDialog.show();
    }

    /*
    * 提交当前打分
    * */
    public void submit() {
        DialogUtil.showConfirmDialog(mContext, "是否确定当前打分为最终分值", new DialogUtil.OnDialogSelectId() {

            @Override
            public void onClick(View v) {
                if (imglist.size() == 0) {
                    mCustomDialog.show();
                } else {
                    showLoadingDialog("请稍候");
                    new QiniuUtil(mContext, imglist, new QiniuUtil.OnQiniuUploadListener() {

                        @Override
                        public void success(String names) {


                            //Log.e("-- edittext_comment","-- "+edittext_comment.getText());
                            GradeApi.get().Points(mContext, mScore + "", barcodeinfo.code, names,edittext_comment.getText().toString(), new ApiConfig.ApiRequestListener<String>() {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case TAKE_PHOTO: {
                if (resultCode == RESULT_OK) {
//                    imglist.clear();
                    imglist.addAll(intent.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT));

                    addPicture(imglist);

                    if (linearLayoutContent.getChildCount()>=3){
                        relativeLayoutAddPicture.setVisibility(View.GONE);
                    }
//                    linearLayoutPhoto.setVisibility(View.GONE);
//                    imageViewPicture.setImageBitmap(ImageCompressUtil.compressByQuality(ImageCompressUtil.compressBySize(imglist.get(0), 400, 400), 200));
                }
                break;
            }
            case TAKE_PICTURE: {
                if (resultCode == RESULT_OK) {
//                    imageViewPicture.setVisibility(View.VISIBLE);
//                    linearLayoutPhoto.setVisibility(View.GONE);
//                    imglist.clear();
                    imglist.add(intent.getStringExtra(CameraProtectActivity.IMAGE_PATH));
//                    imageViewPicture.setImageBitmap(ImageCompressUtil.compressByQuality(ImageCompressUtil.compressBySize(imglist.get(0), 400, 400), 200));

                    addPicture(imglist);

                    if (linearLayoutContent.getChildCount()>=3){
                        relativeLayoutAddPicture.setVisibility(View.GONE);
                    }
                }
                break;
            }
            default:
                break;
        }
    }
}
