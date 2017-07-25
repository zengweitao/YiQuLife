package com.quanmai.yiqu.ui.mys.handwork;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.MaterialOrUserDetailsInfo;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.common.zxing.camera.CameraManager;
import com.quanmai.yiqu.common.zxing.decoding.CaptureActivityHandler;
import com.quanmai.yiqu.common.zxing.view.ViewfinderView;
import com.quanmai.yiqu.ui.FEDRelevant.FEDPutAwayActivity;
import com.quanmai.yiqu.ui.code.MipcaActivityCapture;

/**
 * 手工发袋-二维码扫描页面
 */
public class ScanningActivity extends MipcaActivityCapture{
    public static final int TYPE_GRANT_BAG = 0;
    public static final int TYPE_WAREHOUSE = 1;
    public static final int TYPE_PUTAWAY = 2;

    private static final int REQUEST_CODE = 100;
    private static final int PARSE_BARCODE_SUC = 300;
    private static final int PARSE_BARCODE_FAIL = 303;
    private ProgressDialog mProgress;
    private String photo_path;
    private int type; //扫描类型

    private ViewfinderView viewfinder_view;
    private LinearLayout llayout_inputcode,llayout_openlight;
    private ImageView img_inputcode,img_openlight;
    private Button button_inputcode,button_openlight;
    private static final int REQUEST_BAGCODE = 110;
    private ImageView iv_back;
    MaterialOrUserDetailsInfo.UsercompareListBean info;
    String mCommname;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        init();
    }

    private void init() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        llayout_inputcode = (LinearLayout) findViewById(R.id.llayout_inputcode);
        llayout_openlight = (LinearLayout) findViewById(R.id.llayout_openlight);
        img_inputcode = (ImageView) findViewById(R.id.img_inputcode);
        img_openlight = (ImageView) findViewById(R.id.img_openlight);
        button_inputcode = (Button) findViewById(R.id.button_inputcode);
        button_openlight = (Button) findViewById(R.id.button_openlight);

        iv_back.setOnClickListener(this);
        llayout_inputcode.setOnClickListener(this);
        llayout_openlight.setOnClickListener(this);

        viewfinder_view = (ViewfinderView) findViewById(R.id.viewfinder_view);
        if (getIntent().hasExtra("UsercompareListBean")){
            info= (MaterialOrUserDetailsInfo.UsercompareListBean) getIntent().getSerializableExtra("UsercompareListBean");
        }
        if (getIntent().hasExtra("Commname")){
            mCommname=getIntent().getStringExtra("Commname");
        }
        if (getIntent().hasExtra("type")){
            type = getIntent().getIntExtra("type", 0);
        }
        switch (type) {
            case TYPE_GRANT_BAG: {
                ((TextView) findViewById(R.id.tv_title)).setText("发放环保袋");
                viewfinder_view.setScanText("请扫描发放袋上的二维码");
                break;
            }
            case TYPE_WAREHOUSE: {
                ((TextView) findViewById(R.id.tv_title)).setText("物料入库");
                viewfinder_view.setScanText("请扫描入库袋上的二维码");
                break;
            }
            case TYPE_PUTAWAY: {
                ((TextView) findViewById(R.id.tv_title)).setText("物料上架");
                viewfinder_view.setScanText("请扫描上架袋上的二维码");
                break;
            }
        }

        /*((TextView) findViewById(R.id.tv_right)).setText("手动输入");
        findViewById(R.id.tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.setClass(mContext,QRCodeInputActivity.class);
                startActivity(intent);
                finish();
//                mDialog = DialogUtil.getPopupConfirmEditTextDialog(mContext, "输入条码", "确定", "取消", new DialogUtil.OnDialogClickListener() {
//                    @Override
//                    public void onClick(String str) {
//                        if (TextUtils.isEmpty(str)) {
//                            showCustomToast("请输入条码");
//                        } else {
//                            switch (type) {
//                                case TYPE_GRANT_BAG: {
//                                    Intent intent = getIntent();
//                                    intent.setClass(mContext, GrantBagActivity.class);
//                                    intent.putExtra("strResult", str);
//                                    startActivity(intent);
//                                    finish();
//                                    break;
//                                }
//                                case TYPE_WAREHOUSE: {
//                                    Intent intent = getIntent();
//                                    intent.setClass(mContext, MaterialWarehouseActivity.class);
//                                    intent.putExtra("strResult", str);
//                                    startActivity(intent);
//                                    finish();
//                                    break;
//                                }
//                                default:
//                                    break;
//                            }
//                        }
//                    }
//                });
//                mDialog.show();
            }
        });*/
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
                    Utils.showCustomToast(ScanningActivity.this, (String) msg.obj);
                    break;
            }
        }

    };

    /**
     * 处理扫描结果
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
                            photo_path = cursor.getString(cursor
                                    .getColumnIndex(MediaStore.Images.Media.DATA));
                        }
                        cursor.close();
                    } else {
                        photo_path = selectedImage.getPath();
                    }
                    mProgress = new ProgressDialog(ScanningActivity.this);
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

    //跳转到下一个页面
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
            Utils.showCustomToast(ScanningActivity.this, "扫描失败！");
            return;
        }
        switch (type) {
            case TYPE_GRANT_BAG: {
                if (resultString.length()>30||resultString.length()==11){
                    handler.sendEmptyMessageAtTime(R.id.restart_preview,4000);
                    showCustomToast("请扫描或手动输入正确的袋子范围码！");
                    return;
                }
                Intent intent = getIntent();
                intent.setClass(mContext, GrantBagActivity.class);
                intent.putExtra("strResult", resultString);
                intent.putExtra("UsercompareListBean", info);
                intent.putExtra("Commname", mCommname);
                startActivity(intent);
                finish();
                break;
            }
            case TYPE_WAREHOUSE: {
                if (resultString.length()>30||resultString.length()==11){
                    handler.sendEmptyMessageAtTime(R.id.restart_preview,4000);
                    showCustomToast("请扫描或手动输入正确的袋子范围码！");
                    return;
                }
                Intent intent = getIntent();
                intent.setClass(mContext, MaterialWarehouseActivity.class);
                intent.putExtra("strResult", resultString);
                startActivity(intent);
                finish();
                break;
            }
            case TYPE_PUTAWAY: {
                if (resultString.length()>30){
                    showCustomToast("请扫描正确的袋子码！");
                    handler.sendEmptyMessage(R.id.restart_preview);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("strResult", resultString);
                this.setResult(REQUEST_BAGCODE, intent);
                finish();
                break;
            }
            default:
                break;
        }

    }

    boolean isClick=true;
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.llayout_inputcode) {
            Intent intent = getIntent();
            intent.setClass(this,QRCodeInputActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
                /*findViewById(R.id.img_inputcode).setBackgroundResource(R.drawable.ic_manual_input_on);
                button_inputcode.setTextColor(Color.parseColor("#48C299"));*/

        } else if (i == R.id.llayout_openlight) {
            if (isClick){
                findViewById(R.id.img_openlight).setBackgroundResource(R.drawable.ic_flashlight_on);
                button_openlight.setTextColor(Color.parseColor("#48C299"));
            }else {
                findViewById(R.id.img_openlight).setBackgroundResource(R.drawable.ic_flashlight_off);
                button_openlight.setTextColor(Color.WHITE);
            }
            isClick=!isClick;
            CameraManager.get().flashHandler();
        }else if(i == R.id.iv_back){
            Intent intent = new Intent();
            intent.putExtra("strResult", "");
            this.setResult(REQUEST_BAGCODE, intent);
            finish();
        }

    }
}
