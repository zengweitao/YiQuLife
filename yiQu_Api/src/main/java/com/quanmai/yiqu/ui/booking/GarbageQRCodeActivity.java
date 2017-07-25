package com.quanmai.yiqu.ui.booking;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.BitmapUtils;

import java.lang.ref.WeakReference;

/**
 * 垃圾袋二维码生成
 */
public class GarbageQRCodeActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvThrowType;
    private ImageView imgQRCode;
    private Button btnConfirm;
    private TextView tv_title;
    private TextView textViewDescription;
    LinearLayout linearLayoutMore;
    ImageView imgQRCode1,imgQRCode2,imgQRCode3,imgQRCode4,imgQRCode5,imgQRCode6,imgQRCode7;

    private String strCode = "";  //垃圾袋编码
    private String garbagName="";
    private String mType="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_qrcode);
        initView();
        init();
        showLoadingDialog();
    }

    private void initView() {
        tv_title = ((TextView) findViewById(R.id.tv_title));
        textViewDescription = ((TextView) findViewById(R.id.textViewDescription));

        tvThrowType = (TextView) findViewById(R.id.tvThrowType);
        imgQRCode = (ImageView) findViewById(R.id.imgQRCode);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        linearLayoutMore = (LinearLayout)findViewById(R.id.linearLayoutMore);
        imgQRCode1 = (ImageView) findViewById(R.id.imgQRCode1);
        imgQRCode2 = (ImageView) findViewById(R.id.imgQRCode2);
        imgQRCode3 = (ImageView) findViewById(R.id.imgQRCode3);
        imgQRCode4 = (ImageView) findViewById(R.id.imgQRCode4);
        imgQRCode5 = (ImageView) findViewById(R.id.imgQRCode5);
        imgQRCode6 = (ImageView) findViewById(R.id.imgQRCode6);
        imgQRCode7 = (ImageView) findViewById(R.id.imgQRCode7);

        btnConfirm.setOnClickListener(this);
    }

    private void init() {
        if (getIntent().hasExtra("strCode")) {
            strCode = getIntent().getStringExtra("strCode");
        }
        if (getIntent().hasExtra("type")){
            mType = getIntent().getStringExtra("type");
//            imgQRCode.setVisibility(View.GONE);
//            linearLayoutMore.setVisibility(View.VISIBLE);
            tv_title.setText("垃圾投放");
            textViewDescription.setText("请将二维码对准机器的二维码扫描窗口，待扫码成功机器打开投放入口将垃圾投入后点击此页面下方的“完成投放”完成投放步骤");
        }else {
            tv_title.setText("废品投放");
            textViewDescription.setText("请将二维码对准机器的二维码扫描窗口，待扫码成功机器打开投放入口将废品投入后点击此页面下方的“完成投放”完成投放步骤");
        }
        BitmapAsyncTask bitmapAsyncTask = new BitmapAsyncTask(imgQRCode, 350, 350);
        bitmapAsyncTask.execute(strCode);
        if (getIntent().hasExtra("garbagname")){
            garbagName = getIntent().getStringExtra("garbagname");
            tvThrowType.setText("投放类型："+garbagName);
        }
    }

    private class BitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private WeakReference<ImageView> imgWeakReference;
        private int width;
        private int height;

        public BitmapAsyncTask(ImageView imageView, int width, int height) {
            this.imgWeakReference = new WeakReference<ImageView>(imageView);
            this.width = width;
            this.height = height;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = BitmapUtils.create2DCode(params[0], width, height);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
//            if (TextUtils.isEmpty(mType)){
//                if (bitmap != null && imgWeakReference != null) {
//                    final ImageView imageView = imgWeakReference.get();
//                    if (imageView != null) {
//                        imageView.setImageDrawable(new BitmapDrawable(bitmap));
//                        dismissLoadingDialog();
//                    }
//                }
//            }else {
//
//            }
            if (bitmap!=null){
                imgQRCode1.setImageDrawable(new BitmapDrawable(bitmap));
                imgQRCode2.setImageDrawable(new BitmapDrawable(bitmap));
                imgQRCode3.setImageDrawable(new BitmapDrawable(bitmap));
                imgQRCode4.setImageDrawable(new BitmapDrawable(bitmap));
                imgQRCode5.setImageDrawable(new BitmapDrawable(bitmap));
                imgQRCode6.setImageDrawable(new BitmapDrawable(bitmap));
                imgQRCode7.setImageDrawable(new BitmapDrawable(bitmap));
            }
            dismissLoadingDialog();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
                finish();
                break;
        }
    }
}
