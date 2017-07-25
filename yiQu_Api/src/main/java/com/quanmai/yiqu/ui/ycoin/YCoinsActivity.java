package com.quanmai.yiqu.ui.ycoin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.qrcode.encoder.QRCode;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.BitmapUtils;
import com.quanmai.yiqu.common.util.StringUtil;

/**
 *我的益币页面
 * */
public class YCoinsActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_title;
    TextView textViewYCoins;
    ImageView imageViewQRCode;
    TextView textViewSerialNumber;
    TextView textViewUseRule;
    TextView textViewYCoinsRecord,textViewGiftRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ycoins);

        initView();
    }

    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("我的益币");
        textViewYCoins = (TextView)findViewById(R.id.textViewYCoins);
        imageViewQRCode = (ImageView) findViewById(R.id.imageViewQRCode);
        textViewSerialNumber = (TextView)findViewById(R.id.textViewSerialNumber);
        textViewUseRule = (TextView)findViewById(R.id.textViewUseRule);
        textViewYCoinsRecord = (TextView)findViewById(R.id.textViewYCoinsRecord);
        textViewGiftRecord = (TextView)findViewById(R.id.textViewGiftRecord);

        textViewYCoinsRecord.setOnClickListener(this);
        textViewGiftRecord.setOnClickListener(this);

        if (UserInfo.get()!=null&& !TextUtils.isEmpty(UserInfo.get().getHouseCodeX())){
            Bitmap bitmap = BitmapUtils.create2DCode(UserInfo.get().getHouseCodeX(), Utils.dp2px(this,225),Utils.dp2px(this,225));
            if (bitmap!=null){
                imageViewQRCode.setImageBitmap(bitmap);
            }

            textViewSerialNumber.setText(UserInfo.get().getHouseCode());
        }
        textViewYCoins.setText(UserInfo.get().getAmount()+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewYCoinsRecord:{
                Intent intent = new Intent(this,FetchRecordActivity.class);
                intent.putExtra("type","YCoin");
                startActivity(intent);
                break;
            }

            case R.id.textViewGiftRecord:{
                Intent intent = new Intent(this,FetchRecordActivity.class);
                intent.putExtra("type","Award");
                startActivity(intent);
                break;
            }
        }
    }
}
