package com.quanmai.yiqu.ui.mys.setting;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.UserInfoApi;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.PhotoUtil;
import com.quanmai.yiqu.common.util.QiniuUtil;
import com.quanmai.yiqu.common.util.QiniuUtil.OnQiniuUploadListener;
import com.quanmai.yiqu.common.widget.DateSelectionDialog;
import com.quanmai.yiqu.common.widget.DateSelectionDialog.OnDateTimeSetListener;
import com.quanmai.yiqu.common.widget.XCRoundImageView;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人信息设置
 */
public class PersonalInfoSettingActivity extends BaseActivity implements
        OnClickListener {
    private final int REQUEST_CODE_Nickname = 101;
    private final int REQUEST_CODE_LOCAL_PHOTO = 102;
    private final int REQUEST_CODE_TAKE_PHOTOS = 103;
    private final int REQUEST_CODE_CUT_PHOTO = 104;

    private TextView tv_nickname, tv_sex, tv_birth;
    private XCRoundImageView iv_head;
    private Dialog chooseSexDialog, chooseHeadImgDialog;
    private DateSelectionDialog dateSelectionDialog;
    private String mAlias, mSex, mBirth;
    private String imgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        ((TextView) findViewById(R.id.tv_title)).setText("个人资料设置");

        init();
    }

    private void init() {
        iv_head = (XCRoundImageView) findViewById(R.id.iv_head);
        iv_head.setImageResource(R.drawable.default_header);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_birth = (TextView) findViewById(R.id.tv_birth);

        findViewById(R.id.relative_head_img).setOnClickListener(this);
        findViewById(R.id.linear_nickname).setOnClickListener(this);
        findViewById(R.id.linear_choose_sex).setOnClickListener(this);
        findViewById(R.id.linear_choose_birth).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        getUserDetail();
    }

    /**
     * 选择头像（从本地相册选取或直接拍照）
     */
    private void chooseHeadImg() {
        if (chooseHeadImgDialog == null) {
            chooseHeadImgDialog = DialogUtil.chooseHeadImgDialog(this, this);
        }
        chooseHeadImgDialog.show();
    }

    /**
     * 选择性别的Dialog
     */
    private void chooseSex() {
        if (chooseSexDialog == null) {
            chooseSexDialog = DialogUtil.chooseSexDialog(this, this);
        }
        chooseSexDialog.show();
    }

    /**
     * 选择生日的Dialog
     */
    private void chooseBirth() {
        if (dateSelectionDialog == null) {
            dateSelectionDialog = new DateSelectionDialog(mContext,
                    new OnDateTimeSetListener() {

                        @Override
                        public void onDateTimeSet(int year, int monthOfYear,
                                                  int day) {
                            tv_birth.setText(year + "." + monthOfYear);
                        }

                    });
        }
        dateSelectionDialog.show();
    }

    private void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/png");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CODE_CUT_PHOTO);
    }

    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            imgName = getImageUri().getPath();
            bitmap = PhotoUtil.toSmall(bitmap, imgName);
            iv_head.setImageBitmap(bitmap);
        }
    }

    private Uri getImageUri() {
        File file = new File(Environment.getExternalStorageDirectory()
                + "/yiqu");
        if (!file.exists()) {
            file.mkdir();
        }
        return Uri.fromFile(new File(file, "head.png"));
    }

    public void getUserDetail() {
        showLoadingDialog("请稍候");

        UserInfoApi.get().getUserHome(mContext, new ApiConfig.ApiRequestListener<UserInfo>() {
            @Override
            public void onSuccess(String msg, UserInfo data) {
                dismissLoadingDialog();

                ImageloaderUtil.displayImage(mContext, UserInfo.get().face, iv_head);
                tv_nickname.setText(UserInfo.get().alias);

                switch (UserInfo.get().sex) {
                    case 0:
                        tv_sex.setText(null);
                        break;
                    case 1:
                        tv_sex.setText("男");
                        break;
                    case 2:
                        tv_sex.setText("女");
                        break;
                    default:
                        break;
                }
                String[] birthArray = UserInfo.get().birth.split("-");
                if (birthArray.length >= 2) {
                    tv_birth.setText(birthArray[0] + "." + birthArray[1]);
                } else {
                    tv_birth.setText(UserInfo.get().birth);
                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    private void submit() {
        mAlias = tv_nickname.getText().toString().trim();
        mSex = tv_sex.getText().toString().trim();
        mBirth = tv_birth.getText().toString().trim();

        if (mAlias.equals("")) {
            showCustomToast("请设置您的昵称");
            return;
        }
        if (mSex.equals("")) {
            showCustomToast("请选择性别");
            return;
        } else if (mSex.equals("男")) {
            mSex = "1";
        } else if (mSex.equals("女")) {
            mSex = "2";
        }
        if (mBirth.equals("")) {
            showCustomToast("请选择生日");
            return;
        }
        if (imgName != null && !"".equals(imgName)) {
            showLoadingDialog("请稍候");
            List<String> list = new ArrayList<String>();
            list.add(getImageUri().getPath());
            new QiniuUtil(this, list, new OnQiniuUploadListener() {
                @Override
                public void success(String names) {
                    UserInfoApi.get().UserSetting(mContext, names, mAlias, mSex, mBirth,
                            new ApiRequestListener<String>() {

                                @Override
                                public void onSuccess(String msg, String data) {
                                    LocalBroadcastManager.getInstance(mContext)
                                            .sendBroadcast(new Intent(PersonalFragment.ACTION_NETWORKING_TO_REFRESH_DATA));

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
                    showCustomToast("图片上传失败");
                    dismissLoadingDialog();
                }
            }).upload();
        } else {
            showLoadingDialog();
            Api.get().UserSetting(mContext, UserInfo.get().face, mAlias, mSex, mBirth,
                    new ApiRequestListener<String>() {

                        @Override
                        public void onSuccess(String msg, String data) {
                            LocalBroadcastManager.getInstance(PersonalInfoSettingActivity.this).
                                    sendBroadcast(new Intent(PersonalFragment.ACTION_NETWORKING_TO_REFRESH_DATA));
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

    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.relative_head_img:
                chooseHeadImg();
                break;
            case R.id.btn_local_photo:
                intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_CODE_LOCAL_PHOTO);
                chooseHeadImgDialog.cancel();
                break;
            case R.id.btn_take_photos:
                if (PhotoUtil.isSdcardExisting()) {
                    intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTOS);
                } else {
                    showCustomToast("请插入SD卡");
                }
                chooseHeadImgDialog.cancel();
                break;
            case R.id.btn_cancel:
                chooseHeadImgDialog.cancel();
                break;
            case R.id.linear_nickname:
                startActivityForResult(NicknameActivity.class, REQUEST_CODE_Nickname);
                break;
            case R.id.linear_choose_sex:
                chooseSex();
                break;
            case R.id.buttonConfirm:
                tv_sex.setText("男");
                chooseSexDialog.cancel();
                break;
            case R.id.buttonShare:
                tv_sex.setText("女");
                chooseSexDialog.cancel();
                break;
            case R.id.linear_choose_birth:
                chooseBirth();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_Nickname:
                if (resultCode == 1) {
                    tv_nickname.setText(data.getStringExtra("nickname"));
                }
                break;

            case REQUEST_CODE_LOCAL_PHOTO: // 相册
                if (resultCode != -1) {
                    return;
                }
                resizeImage(data.getData());
                break;

            case REQUEST_CODE_TAKE_PHOTOS:
                if (resultCode != -1) {
                    return;
                }
                if (PhotoUtil.isSdcardExisting()) {
                    resizeImage(getImageUri());
                } else {
                    showCustomToast("未找到存储卡，无法存储照片");
                }
                break;

            case REQUEST_CODE_CUT_PHOTO: // 裁剪
                if (resultCode != -1) {
                    return;
                }
                if (data != null) {
                    showResizeImage(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
