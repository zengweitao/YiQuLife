package com.quanmai.yiqu.ui.code;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.EquipmentApi;
import com.quanmai.yiqu.api.HandworkApi;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.BagInfo;
import com.quanmai.yiqu.api.vo.BarCodeInfo;
import com.quanmai.yiqu.api.vo.MachineDataInfo;
import com.quanmai.yiqu.api.vo.UserDetailsByHouseCodeInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.common.widget.SystemBarTintManager;
import com.quanmai.yiqu.common.zxing.RGBLuminanceSource;
import com.quanmai.yiqu.common.zxing.camera.CameraManager;
import com.quanmai.yiqu.common.zxing.decoding.CaptureActivityHandler;
import com.quanmai.yiqu.common.zxing.decoding.InactivityTimer;
import com.quanmai.yiqu.common.zxing.view.ViewfinderView;
import com.quanmai.yiqu.ui.FEDRelevant.FEDPutAwayActivity;
import com.quanmai.yiqu.ui.UserCodeRelevant.UserMessageByUsercodeActivity;
import com.quanmai.yiqu.ui.booking.BookingSecondActivity;
import com.quanmai.yiqu.ui.common.TwoDCodeShowActivity;
import com.quanmai.yiqu.ui.grade.ScanResultActivity;
import com.quanmai.yiqu.ui.integration.IntegralDetailsActivity;
import com.quanmai.yiqu.ui.mys.handwork.GrantBagActivity;
import com.quanmai.yiqu.ui.mys.handwork.MaterialWarehouseActivity;
import com.quanmai.yiqu.ui.mys.handwork.QRCodeInputActivity;
import com.quanmai.yiqu.ui.ycoin.GiftGivingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Hashtable;

public class MipcaActivityCapture extends BaseActivity implements Callback,OnClickListener {

    public CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    //	private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    private static final int REQUEST_CODE = 100;
    private static final int PARSE_BARCODE_SUC = 300;//解析条码成功
    private static final int PARSE_BARCODE_FAIL = 303; //解析条码失败
    private ProgressDialog mProgress;
    private String photo_path;
    private int type;
    Dialog mDialog,mDialog2,mDialog3,mDialog4,mDialog5;

    private Button button_inputcode;
    private Button button_openlight;
    private LinearLayout llayout_inputcode;
    private LinearLayout llayout_openlight;
    private int whichcode;

    Context context;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        context=MipcaActivityCapture.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager manager = new SystemBarTintManager(this);
            manager.setStatusBarTintEnabled(true);
            manager.setStatusBarTintColor(getResources().getColor(R.color.theme));
        }

        type = getIntent().getIntExtra("type", 0);

        CameraManager.init(getApplication());  //相机初始化
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        ((TextView) findViewById(R.id.tv_title)).setText("扫描二维码");

        button_inputcode = (Button) findViewById(R.id.button_inputcode);
        button_openlight = (Button) findViewById(R.id.button_openlight);
        llayout_inputcode = (LinearLayout) findViewById(R.id.llayout_inputcode);
        llayout_openlight = (LinearLayout) findViewById(R.id.llayout_openlight);

        llayout_inputcode.setOnClickListener(this);
        llayout_openlight.setOnClickListener(this);

        TextView tvRight = (TextView) findViewById(R.id.tv_right);

        if (getIntent().hasExtra("whichjump")) {
            tvRight.setText("相册");
            tvRight.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent innerIntent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(innerIntent, REQUEST_CODE);
                }
            });
            initDialog("请输入设备码！");
        }else {
            initDialog("请输入垃圾袋编码！");
        }

    }

    /**
     * 手动输入弹出框方法
     * @param hint
     */
    private void initDialog(final String hint) {
        //扫码取袋弹出框 输入机器码
        mDialog = DialogUtil.getPopupConfirmEditTextDialog(mContext, hint, "确定", "取消", new DialogUtil.OnDialogClickListener() {
            @Override
            public void onClick(String str) {
                if (TextUtils.isEmpty(str)) {
                    showCustomToast(hint);
                } else {
                    mDialog.dismiss();
                    onResultHandler(str);//手动输入机器码
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

        //巡检评分弹出框  输入垃圾代码
        mDialog2 = DialogUtil.getPopupConfirmEditTextDialog(this, hint, "确定", "取消", new DialogUtil.OnDialogClickListener() {
            @Override
            public void onClick(String str) {
                if (TextUtils.isEmpty(str)) {
                    Utils.showCustomToast(MipcaActivityCapture.this, hint);
                } else {
                    mDialog2.dismiss();
                    takeScore(str);//手动输入袋子码并跳转评分界面
                }
            }
        });
        mDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                findViewById(R.id.img_inputcode).setBackgroundResource(R.drawable.ic_manual_input_off);
                button_inputcode.setTextColor(Color.WHITE);
            }
        });
    }

    /**
     * 根据扫描的结果弹出选择框
     * @param hint
     * @param whichcode

     * @param resultString
     */
    private void initchooseDialog(String hint, int whichcode, final String resultString) {

        mDialog3 = DialogUtil.getPopupChooseDialog(this, hint,whichcode,UserInfo.get().usertype, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.give_present: //赠送礼品
                        mDialog3.dismiss();
                        givepresent(resultString);
                        finish();
                        break;
                    case R.id.scene_recycle: //现场回收
                        mDialog3.dismiss();
                        scenerecycle(resultString);
                        finish();
                        break;
                    case R.id.get_user_details: //获取用户信息
                        mDialog3.dismiss();
                        getUserDetails(resultString);
                        break;
                    case R.id.inspection_grade: //巡检评分
                        mDialog3.dismiss();
                        takeScore(resultString);
                        break;
                    case R.id.sand_bag_of_hand: //手工发袋
                        mDialog3.dismiss();
                        sandbagbyhand(resultString);
                        finish();
                        break;
                    case R.id.bag_put_in_storage: //物料入库
                        mDialog3.dismiss();
                        bagputinstorage(resultString);
                        finish();
                        break;
                }
            }
        });
        mDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                DialogUtil.tab_jurisdiction=null;
                handler.sendEmptyMessage(R.id.restart_preview);
            }
        });

        mDialog5 = DialogUtil.getPopupChooseHouseCodeDialog(this, hint, new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button_get_bag:
                        mDialog5.dismiss();
                        scanMachineCode(resultString);
                        break;
                    case R.id.button_putaway_bag:
                        mDialog5.dismiss();
                        getDataByMachineCode(resultString);
                        break;
                }
            }
        });
        mDialog5.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                    handler.sendEmptyMessage(R.id.restart_preview);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mProgress.dismiss();
            switch (msg.what) {
                case PARSE_BARCODE_SUC:
                    onResultHandler((Result) msg.obj);//解析相册中图片获得的信息
                    break;
                case PARSE_BARCODE_FAIL:
                    Utils.showCustomToast(MipcaActivityCapture.this, (String) msg.obj);
                    break;
            }
        }

    };

    /**
     * 扫描解析从相册选中的图片信息
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    // 获取图片
                    Uri selectedImage = data.getData();
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        }
                        cursor.close();
                    } else {
                        photo_path = selectedImage.getPath();
                    }
                    mProgress = new ProgressDialog(MipcaActivityCapture.this);
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

    /**
     * 扫码图片的方法
     *
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     */
    public void handleDecode(Result result) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();//加声音和振动
        onResultHandler(result);//处理扫描结果跳转
    }

    /**
     * 跳转到下一个页面
     *
     * @param result
     */
    private void onResultHandler(Object result) {
        final String resultString;
        //String str = StringUtil.lineFeedFilter(result.getText());
        String str;
        if (result instanceof Result){
            Result r=(Result)result;
            str = r.getText();
        }else {
            str=(String)result;
        }
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
            Utils.showCustomToast(MipcaActivityCapture.this, "扫描失败！");
            return;
        }
        String strResult = Utils.getDecodeBase64(resultString); //解码Base64字符串
        if (type == 0 && strResult.contains("getPoint")) { //扫码获取积分
            if (UserInfo.get().usertype.contains(UserInfo.USER_RECYCLE)) {
                showCustomToast("您所在的用户类型不支持此功能");
                finish();
            }
            try {
                JSONObject jsonObject = new JSONObject(strResult);
                String strHash = jsonObject.optString("hash");
                strHash = Utils.getMD5(strHash + mSession.getToken());
                String pointRecordId = jsonObject.optString("pointRecordId");
                String activityId = jsonObject.optString("activityId");
                String defaultPoint = jsonObject.optString("defaultPoint");
                getIntegrationGive(strHash, pointRecordId, activityId, defaultPoint);
            } catch (JSONException e) {
                Utils.E("二维码Json数据解析失败");
            }
        } else if (type == 0) {
            if (resultString.trim().length()>=11&&resultString.trim().length()<=14){
                if (UserInfo.get().usertype.contains(UserInfo.USER_ALL)||
                        UserInfo.get().usertype.contains(UserInfo.USER_INSPECTION)){
                    takeScore(resultString);
                }else {
                    Intent intent=new Intent(this, TwoDCodeShowActivity.class);
                    intent.putExtra("resultString",resultString);
                    startActivity(intent);
                    finish();
                    showCustomToast("您没有此类权限！");
                }
                /*if (!UserInfo.get().usertype.contains(UserInfo.USER_ALL)&&!UserInfo.get().usertype.contains(UserInfo.USER_INSPECTION)&&
                        !UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK)&&!UserInfo.get().usertype.contains(UserInfo.USER_WAREHOUSE))
                {
                    Intent intent=new Intent(this, TwoDCodeShowActivity.class);
                    intent.putExtra("resultString",resultString);
                    startActivity(intent);
                    finish();
                    showCustomToast("您没有此类权限！");
                }
                    if (UserInfo.get().usertype.contains(UserInfo.USER_INSPECTION)&&
                            !UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK)&&
                            !UserInfo.get().usertype.contains(UserInfo.USER_WAREHOUSE)){
                        takeScore(resultString);
                        return;
                    }
                    if (!UserInfo.get().usertype.contains(UserInfo.USER_INSPECTION)&&
                            UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK)&&
                            !UserInfo.get().usertype.contains(UserInfo.USER_WAREHOUSE)){
                        sandbagbyhand(resultString);
                        return;
                    }
                    if (!UserInfo.get().usertype.contains(UserInfo.USER_INSPECTION)&&
                            !UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK)&&
                            UserInfo.get().usertype.contains(UserInfo.USER_WAREHOUSE)){
                        bagputinstorage(resultString);
                        return;
                    }
                whichcode=1;
                initchooseDialog("请选择相应操作！",whichcode,resultString);
                mDialog3.show();*/
            }else if ((resultString.trim().startsWith("FED")||resultString.trim().startsWith("ZYN")
                    ||resultString.trim().startsWith("FFJ")||resultString.trim().startsWith("FDJ"))&&(resultString.trim().length()<64)){
                if (UserInfo.get().usertype.contains(UserInfo.USER_ALL)||UserInfo.get().usertype.contains(UserInfo.USER_WAREHOUSE)) {
                    initchooseDialog("请选择相应操作！",-1 ,resultString);
                    mDialog5.show();
                    return;
                }
                    scanMachineCode(resultString);

            }else if (resultString.trim().length()==64){
                if (!UserInfo.get().usertype.contains(UserInfo.USER_ALL)&&!UserInfo.get().usertype.contains(UserInfo.USER_GIFT_GIVING)&&
                        !UserInfo.get().usertype.contains(UserInfo.USER_RECYCLE)&&!UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK))
                {
                    Intent intent=new Intent(this, TwoDCodeShowActivity.class);
                    intent.putExtra("resultString",resultString);
                    startActivity(intent);
                    finish();
                    showCustomToast("您没有此类权限！");
                }
                    if (UserInfo.get().usertype.contains(UserInfo.USER_RECYCLE)&&
                            !UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK)){
                        scenerecycle(resultString);
                        return;
                    }
                    if (!UserInfo.get().usertype.contains(UserInfo.USER_RECYCLE)&&
                            UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK)){
                        getUserDetails(resultString);
                        return;
                    }
                whichcode=2;
                initchooseDialog("请选择相应操作！",whichcode,resultString);
                mDialog3.show();
            }else if (StringUtil.removeAllSpace(resultString.trim()).length()>=23&&StringUtil.removeAllSpace(resultString.trim()).length()<=29&&
            StringUtil.removeAllSpace(resultString.trim()).contains("-")){
                if (!UserInfo.get().usertype.contains(UserInfo.USER_ALL)&&!UserInfo.get().usertype.contains(UserInfo.USER_WAREHOUSE)&&
                        !UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK))
                {
                    Intent intent=new Intent(this, TwoDCodeShowActivity.class);
                    intent.putExtra("resultString",resultString);
                    startActivity(intent);
                    finish();
                    showCustomToast("您没有此类权限！");
                }
                    if (UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK)&&
                            !UserInfo.get().usertype.contains(UserInfo.USER_WAREHOUSE)){
                        sandbagbyhand(resultString);
                        return;
                    }
                    if (!UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK)&&
                            UserInfo.get().usertype.contains(UserInfo.USER_WAREHOUSE)){
                        bagputinstorage(resultString);
                        return;
                    }
                whichcode=4;
                initchooseDialog("请选择相应操作！",whichcode,resultString);
                mDialog3.show();
            }else {
                scanMachineCode(resultString);
                /*Intent intent=new Intent(this, TwoDCodeShowActivity.class);
                intent.putExtra("resultString",resultString);
                startActivity(intent);*/
                finish();
            }

        } else if (type == 1){
            takeScore(resultString.trim());
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, null, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {hasSurface = false;}

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 扫描成功的声音和振动效果
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    boolean isClick=true;
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.llayout_inputcode) {
            if (getIntent().hasExtra("whichjump")) {
                mDialog.show();
            } else {
                mDialog2.show();
            }
            if (mDialog.isShowing()||mDialog2.isShowing()){
                findViewById(R.id.img_inputcode).setBackgroundResource(R.drawable.ic_manual_input_on);
                button_inputcode.setTextColor(Color.parseColor("#48C299"));
            }else {

            }
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

        }
    }

    /**
     *获取用户信息
     * @param code
     */
    public void getUserDetails(final String code){
        showLoadingDialog("玩命加载中！");
        UserApi.get().getUserDetailsByHouseCode(this, code, new ApiConfig.ApiRequestListener<UserDetailsByHouseCodeInfo>() {
            @Override
            public void onSuccess(String msg, UserDetailsByHouseCodeInfo data) {
                dismissLoadingDialog();
                if (UserDetailsByHouseCodeInfo.get()!=null) {
                    Intent intent=new Intent(mContext,UserMessageByUsercodeActivity.class);
                    Bundle mBundle=new Bundle();
                    intent.putExtra("usercode",code);
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

    /**
     * 积分领取
     * @param hash
     * @param pointRecordId
     * @param activityId
     * @param defaultPoint
     */
    private void getIntegrationGive(String hash, String pointRecordId, String activityId, String defaultPoint) {
        showLoadingDialog("积分领取中！");
        IntegralApi.get().getIntegrationGive(mContext, hash, pointRecordId, activityId, defaultPoint, new ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(msg);
                Intent intent = new Intent(mContext, IntegralDetailsActivity.class);
                intent.putExtra("point_type",2);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                finish();
            }
        });
    }
    /**
     * 巡检评分的方法
     * @param resultString
     */
    private void takeScore(final String resultString) {
        showLoadingDialog();
        EquipmentApi.get().BarCode(mContext, resultString, new ApiConfig.ApiRequestListener<BarCodeInfo>() {
            @Override
            public void onSuccess(String msg, BarCodeInfo data) {
                dismissLoadingDialog();
                Intent intent = new Intent(mContext, ScanResultActivity.class);
                Bundle bundle = new Bundle();
                data.code = resultString;
                bundle.putSerializable("barcodeinfo", data);
                intent.putExtras(bundle);
                startActivity(intent);
                mDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                DialogUtil.showCodeDialog(mContext, msg, new DialogUtil.OnDialogSelectId() {

                    @Override
                    public void onClick(View v) {
                        handler.sendEmptyMessage(R.id.restart_preview);
                    }
                });
            }
        });
    }

    /**
     * 扫描机器码领袋子和纸
     * @param resultString
     */
    public void scanMachineCode(final String resultString){
        showLoadingDialog();
        IntegralApi.get().QRCode(context, resultString, new ApiRequestListener<BagInfo>() {
            @Override
            public void onSuccess(String msg, BagInfo data) {
                dismissLoadingDialog();
                UserInfo.get().setIsbind("1");
                mSession.setBind(true);
                switch (data.adscription) {
                    //智能发袋机
                    case BagInfo.TYPE_SMART_BAG: {
                        Intent intent = new Intent();
                        intent.setClass(mContext, ChangeBagActivity.class);
                        intent.putExtra("terminalno", resultString);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("baginfo", data);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    //纸要你
                    case BagInfo.TYPE_PAPER: {
                        Intent intent = new Intent(mContext, ChangeBagSecondStepActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("baginfo", data);
                        intent.putExtra("terminalno", resultString);
                        intent.putExtra("type", "changePaper");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    //默认跳转到普通发袋机
                    default: {
                        Intent intent = new Intent();
                        intent.setClass(mContext, ChangeBagActivity.class);
                        intent.putExtra("terminalno", resultString);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("baginfo", data);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                DialogUtil.showCodeDialog(mContext, msg,
                        new DialogUtil.OnDialogSelectId() {
                            @Override
                            public void onClick(View v) {
                                handler.sendEmptyMessage(R.id.restart_preview);
                            }
                        });
            }
        });
    }

    /**
     * 手工发袋
     * @param resultString
     */
    public void sandbagbyhand(String resultString){
        Intent intent = getIntent();
        intent.setClass(mContext, GrantBagActivity.class);
        intent.putExtra("strResult", resultString);
        startActivity(intent);
        finish();
    }

    /**
     * 物料入库
     * @param resultString
     */
    public void bagputinstorage(String resultString){
        Intent intent = getIntent();
        intent.setClass(mContext, MaterialWarehouseActivity.class);
        intent.putExtra("strResult", resultString);
        startActivity(intent);
        finish();
    }

    /**
     * 赠送礼品
     * @param resultString
     */
    public void givepresent(String resultString){
        Intent intent = new Intent(this,GiftGivingActivity.class);
        intent.putExtra("wherecome","notscan");
        intent.putExtra("code",resultString);
        startActivity(intent);
    }

    /**
     * 现场回收
     * @param resultString
     */
    public void scenerecycle(String resultString){
        Intent intent = new Intent(mContext, BookingSecondActivity.class);
        intent.putExtra("isRecycleLocal", true);
        intent.putExtra("houseCode", resultString.trim());
        startActivity(intent);
    }

    /**
     * 扫描机器码时获取机器库存信息
     */
    public void getDataByMachineCode(final String resultString){
        showLoadingDialog("玩命加载中...");
        HandworkApi.get().getDataByMachineCode(mContext, resultString, new ApiConfig.ApiRequestListener<MachineDataInfo>() {
            @Override
            public void onSuccess(String msg, MachineDataInfo data) {
                dismissLoadingDialog();
                if (data!=null){
                    Intent intent=new Intent(mContext,FEDPutAwayActivity.class);
                    intent.putExtra("usercode",resultString);
                    intent.putExtra("MachineDataInfo",data);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }
}