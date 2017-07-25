package com.quanmai.yiqu.ui.integration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.vo.ActivityIntegrationInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.widget.GiveIntegrationPopWindow;
/**
 * 赠送益币和积分页面
 */
public class GiveIntegrationActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_title;
    RelativeLayout relativeGiveType;
    TextView textViewType;
    EditText editTextIntegarls;
    Button buttonMakeQRCodes;
    TextView textViewPoints,textViewTotalPoints;
    LinearLayout linearIntegration;

    GiveIntegrationPopWindow popupWindow;
    CommonList<ActivityIntegrationInfo> mList;
    String activityId;
    String points;
    int limitPoints=0;
    int lastPoints = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_integration);

        init();

        editTextIntegarls.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())){
                    return;
                }else {
                    if (getIntent().getStringExtra("type").equals("ycoin")){

                    }else if (getIntent().getStringExtra("type").equals("integral")){
                        int points = Integer.parseInt(s.toString());
                        if (limitPoints!=0){
                            if (points>limitPoints){
                                editTextIntegarls.setText(limitPoints+"");
                                showCustomToast("最多只能赠送"+limitPoints+"积分");
                                return;
                            }
                        }

                        if (lastPoints>-1){
                            if (points>lastPoints){
                                editTextIntegarls.setText(lastPoints+"");
                                showCustomToast("活动积分不够啦");
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        relativeGiveType = (RelativeLayout)findViewById(R.id.relativeGiveType);
        textViewType = (TextView)findViewById(R.id.textViewType);
        editTextIntegarls = (EditText)findViewById(R.id.editTextIntegarls);
        buttonMakeQRCodes = (Button)findViewById(R.id.buttonMakeQRCodes);
        textViewPoints = (TextView)findViewById(R.id.textViewPoints);
        textViewTotalPoints = (TextView)findViewById(R.id.textViewTotalPoints);
        linearIntegration = (LinearLayout)findViewById(R.id.linearIntegration);

        if (getIntent().getStringExtra("type").equals("ycoin")){
            if (getIntent().getStringExtra("giftType").equals("1")){
                tv_title.setText("赠送益币");
                editTextIntegarls.setHint("输入益币数");
                buttonMakeQRCodes.setText("点击赠送");
            }else if (getIntent().getStringExtra("giftType").equals("2")){
                tv_title.setText("赠送积分");
                editTextIntegarls.setHint("输入积分数");
                buttonMakeQRCodes.setText("点击赠送");
            }else if (getIntent().getStringExtra("giftType").equals("3")){
                tv_title.setText("赠送福袋");
                editTextIntegarls.setHint("输入福袋数");
                buttonMakeQRCodes.setText("点击赠送");
            }
        }else if (getIntent().getStringExtra("type").equals("integral")){
            relativeGiveType.setVisibility(View.VISIBLE);
            tv_title.setText("赠送积分");
            editTextIntegarls.setHint("输入积分数");
            buttonMakeQRCodes.setText("生成二维码");
        }

        relativeGiveType.setOnClickListener(this);
        buttonMakeQRCodes.setOnClickListener(this);
        mList = new CommonList<>();
    }

    //获取积分类型
    private void getIntegrationGiveType(){
        IntegralApi.get().getIntegrationGiveType(mContext, new ApiConfig.ApiRequestListener<CommonList<ActivityIntegrationInfo>>() {
            @Override
            public void onSuccess(String msg,CommonList<ActivityIntegrationInfo> data) {
                mList.clear();
                mList.addAll(data);
                linearIntegration.setVisibility(View.VISIBLE);
                showWindow(mList);
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }
    //生成二维码
    private void makeQRCodes(String activityId,String points){
        IntegralApi.get().makeQRCode(mContext, activityId, points, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                if (!TextUtils.isEmpty(data)){
                    Intent intent = new Intent(GiveIntegrationActivity.this,ShowQRCodeActivity.class);
                    intent.putExtra("qrcode",data);
                    intent.putExtra("id",msg);

                    linearIntegration.setVerticalGravity(View.GONE);
                    textViewType.setText("");
                    editTextIntegarls.setText("");
                    textViewType.setHint("选择积分赠送类型");
                    editTextIntegarls.setHint("输入积分数");
                    textViewPoints.setText("0");
                    textViewTotalPoints.setText("0");
                    startActivity(intent);

                }
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    private void showWindow(CommonList<ActivityIntegrationInfo> infos){
        popupWindow = new GiveIntegrationPopWindow(mContext, infos, new GiveIntegrationPopWindow.TypeSelectedListener() {
            @Override
            public void selected(ActivityIntegrationInfo info) {
                textViewType.setText(info.activityType);
                textViewTotalPoints.setText(info.totalPoint);


                activityId = info.activityId;
                points = info.defaultPoint;
                lastPoints =(int)Double.parseDouble(info.totalPoint)-(int)Double.parseDouble(info.point);
                limitPoints = (int)Double.parseDouble(info.limitPoint);

                textViewPoints.setText(info.point);

                if ((int)Double.parseDouble(info.defaultPoint)>lastPoints){
                    editTextIntegarls.setText(info.point);
                }else {
                    editTextIntegarls.setText(info.defaultPoint);
                }

                //光标后移
                Editable etext = editTextIntegarls.getText();
                Selection.setSelection(etext, etext.length());
            }
        });

        popupWindow.showAsDropDown(relativeGiveType);
    }

    /**
     * 赠送益币、积分、福袋方法
     */
    public void giveYcoin(){
        showLoadingDialog("请稍后...！");
        String phone;
        if (getIntent().getStringExtra("phone").equals("")||getIntent().getStringExtra("phone").equals("null")||
                getIntent().getStringExtra("phone").equals(null)||getIntent().getStringExtra("phone")==null){
            phone=getIntent().getStringExtra("houseCode");
        }else {
            phone=getIntent().getStringExtra("phone");
        }
    IntegralApi.get().getGiveYcoin(mContext, phone, editTextIntegarls.getText().toString(),getIntent().getStringExtra("giftType"), new ApiConfig.ApiRequestListener<String>() {
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
            editTextIntegarls.setText("");
        }
    });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relativeGiveType:{
                getIntegrationGiveType();
                break;
            }
            case R.id.buttonMakeQRCodes:{
                if(getIntent().getStringExtra("type").equals("ycoin")){
                    if (TextUtils.isEmpty(editTextIntegarls.getText().toString())){
                        return;
                    }
                    giveYcoin();
                }else if (getIntent().getStringExtra("type").equals("integral")){
                    points = editTextIntegarls.getText().toString();
                    if (TextUtils.isEmpty(textViewType.getText().toString())){
                        showCustomToast("请选择活动类型");
                        return;
                    }

                    if (Integer.parseInt(points)>0){
                        makeQRCodes(activityId,points);
                    }else {
                        showCustomToast("请填写相应积分");
                    }
                }
                break;
            }

        }
    }
}
