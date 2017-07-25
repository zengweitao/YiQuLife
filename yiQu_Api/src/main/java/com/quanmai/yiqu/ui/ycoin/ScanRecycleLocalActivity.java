package com.quanmai.yiqu.ui.ycoin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.UserDetailsByHouseCodeInfo;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.common.zxing.camera.CameraManager;
import com.quanmai.yiqu.common.zxing.decoding.CaptureActivityHandler;
import com.quanmai.yiqu.common.zxing.view.ViewfinderView;
import com.quanmai.yiqu.ui.UserCodeRelevant.UserMessageByUsercodeActivity;
import com.quanmai.yiqu.ui.booking.BookingSecondActivity;
import com.quanmai.yiqu.ui.code.MipcaActivityCapture;

/**
 * 现场回收扫描页面
 */
public class ScanRecycleLocalActivity extends MipcaActivityCapture {
    private static final int REQUEST_CODE = 100;
    private static final int PARSE_BARCODE_SUC = 300;
    private static final int PARSE_BARCODE_FAIL = 303;
    private ProgressDialog mProgress;
    private String photo_path;

    private ViewfinderView viewfinder_view;
    private Dialog mDialog;
    private LinearLayout llayout_inputcode;
    private LinearLayout llayout_openlight;
    private Button button_inputcode;
    private Button button_openlight;
    boolean isClick = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        init();
        initDialog();

        llayout_inputcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.img_inputcode).setBackgroundResource(R.drawable.ic_manual_input_on);
                button_inputcode.setTextColor(Color.parseColor("#48C299"));
                mDialog.show();
            }
        });

        llayout_openlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = !isClick;
                if (isClick){
                    findViewById(R.id.img_openlight).setBackgroundResource(R.drawable.ic_flashlight_on);
                    button_openlight.setTextColor(Color.parseColor("#48C299"));
                }else {
                    findViewById(R.id.img_openlight).setBackgroundResource(R.drawable.ic_flashlight_off);
                    button_openlight.setTextColor(Color.WHITE);
                }
                CameraManager.get().flashHandler();
            }
        });
    }

    private void init() {
        viewfinder_view = (ViewfinderView) findViewById(R.id.viewfinder_view);
        if (getIntent().hasExtra("getUserData")){
            ((TextView) findViewById(R.id.tv_title)).setText("扫码获取用户信息");
        }else {
            ((TextView) findViewById(R.id.tv_title)).setText("扫码回收");
        }
        viewfinder_view.setScanText("请扫描用户的二维码");

        llayout_inputcode = (LinearLayout)findViewById(R.id.llayout_inputcode);
        llayout_openlight = (LinearLayout)findViewById(R.id.llayout_openlight);
        button_inputcode = (Button)findViewById(R.id.button_inputcode);
        button_openlight = (Button)findViewById(R.id.button_openlight);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mProgress.dismiss();
            switch (msg.what) {
                case PARSE_BARCODE_SUC:
                    onResultHandler((Result) msg.obj);
                    break;
                case PARSE_BARCODE_FAIL:
                    Utils.showCustomToast(ScanRecycleLocalActivity.this, (String) msg.obj);
                    break;
            }
        }

    };

    /**
     * 处理扫描结果
     *
     * @param result
     */
    @Override
    public void handleDecode(Result result) {
        onResultHandler(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    // 获取图片
                    Uri selectedImage = data.getData();
                    Cursor cursor = getContentResolver().query(data.getData(),
                            null, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        }
                        cursor.close();
                    } else {
                        photo_path = selectedImage.getPath();
                    }
                    mProgress = new ProgressDialog(ScanRecycleLocalActivity.this);
                    mProgress.setMessage("正在扫描...");
                    mProgress.setCancelable(false);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            if (result != null) {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = result;
                                mHandler.sendMessage(m);
                            } else {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_FAIL;
                                m.obj = "扫描失败";
                                mHandler.sendMessage(m);
                            }
                        }
                    }).start();

                    break;
            }
        }
    }

    private void onResultHandler(Result result) {
        final String resultString;
        String str = StringUtil.lineFeedFilter(result.getText());
        if (str.startsWith("http")) {
            str = str.replace("?", "-");
            String[] res = str.split("-");
            for (int i = 0; i < res.length; i++) {
                if (res[i].contains("code")) {
                    str = res[i].split("=")[1];
                }
            }
        }
        resultString = str;

        if (TextUtils.isEmpty(resultString)) {
            Utils.showCustomToast(ScanRecycleLocalActivity.this, "扫描失败！");
            return;
        }
        if (resultString.trim().length()!=64){
            Toast.makeText(this,"请扫描用户码！",Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessage(R.id.restart_preview);
            //Utils.showCustomToast(this,"请扫描用户码！");
            return;
        }
        if (getIntent().hasExtra("getUserData")) {
            getUserDetails("usercode", resultString);
        }else {
            submit("houseCode", resultString);
        }
    }

    //页面跳转
    private void submit(String inputType, String result) {
        Intent intent = getIntent();
        intent.setClass(mContext, BookingSecondActivity.class);
        intent.putExtra("isRecycleLocal", true);
        intent.putExtra(inputType, result.trim());
        startActivity(intent);
        finish();
    }

    /**
     *获取用户信息
     * @param code
     */
    public void getUserDetails(final String inputType, final String code){
        showLoadingDialog("玩命加载中！");
        UserApi.get().getUserDetailsByHouseCode(this, code, new ApiConfig.ApiRequestListener<UserDetailsByHouseCodeInfo>() {
            @Override
            public void onSuccess(String msg, UserDetailsByHouseCodeInfo data) {
                dismissLoadingDialog();
                if (UserDetailsByHouseCodeInfo.get()!=null) {
                    Intent intent = getIntent();
                    intent.setClass(mContext, UserMessageByUsercodeActivity.class);
                    Bundle mBundle=new Bundle();
                    intent.putExtra(inputType, code.trim());
                    mBundle.putSerializable("UserDetailsByHouseCodeInfo",data);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                handler.sendEmptyMessage(R.id.restart_preview);
            }
        });
    }

    private void initDialog() {
        mDialog = DialogUtil.getPopupConfirmEditTextDialog(mContext, "手动输入", "确定", "取消", "请输入您的手机号码", new DialogUtil.OnDialogClickListener() {
            @Override
            public void onClick(String str) {
                if (TextUtils.isEmpty(str)) {
                    showCustomToast("请输入手机号码");
                } else if (!StringUtil.isPhoneNum(str)) {
                    showCustomToast("请输入正确的手机号码");
                } else {
                    mDialog.dismiss();
                    if (getIntent().hasExtra("getUserData")) {
                        getUserDetails("usercode", str);
                    }else {
                        submit("account", str);
                    }
                }
            }
        });
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                findViewById(R.id.img_inputcode).setBackgroundResource(R.drawable.ic_manual_input_off);
                button_inputcode.setTextColor(Color.WHITE);
            }
        });
    }
}
