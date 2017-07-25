package com.quanmai.yiqu.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;

public class AddRecycleAddressActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_title;
    TextView tv_right;
    TextView textViewCommunity;
    TextView textViewNotMyCommunity;
    EditText edittextName;
    EditText edittextPhone;
    EditText edittextDetailedAddress;
    ImageView imageViewClearName,imageViewClearPhone,imageViewClearAddress;

    private final int FINISH_ADD_ADDRESS = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recycle_address);

        init();

        edittextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    if (imageViewClearName.getVisibility()!=View.VISIBLE){
                        imageViewClearName.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (imageViewClearName.getVisibility()==View.VISIBLE){
                        imageViewClearName.setVisibility(View.GONE);
                    }
                }

                if (s.length()>10){
                    Utils.showCustomToast(mContext,"姓名不能超过10个字");
                    edittextName.setText(s.subSequence(0,10));
                    Selection.setSelection(edittextName.getText(), edittextName.getText().length());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edittextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    if (imageViewClearPhone.getVisibility()!=View.VISIBLE){
                        imageViewClearPhone.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (imageViewClearPhone.getVisibility()==View.VISIBLE){
                        imageViewClearPhone.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edittextDetailedAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    if (imageViewClearAddress.getVisibility()!=View.VISIBLE){
                        imageViewClearAddress.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (imageViewClearAddress.getVisibility()==View.VISIBLE){
                        imageViewClearAddress.setVisibility(View.GONE);
                    }
                }

                if (s.length()>50){
                    Utils.showCustomToast(mContext,"地址不能超过50个字");
                    edittextDetailedAddress.setText(s.subSequence(0,50));
                    Selection.setSelection(edittextDetailedAddress.getText(), edittextDetailedAddress.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    //初始化
    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_right = (TextView)findViewById(R.id.tv_right);
        textViewCommunity = (TextView)findViewById(R.id.textViewCommunity);
        textViewNotMyCommunity = (TextView)findViewById(R.id.textViewNotMyCommunity);
        edittextName = (EditText)findViewById(R.id.edittextName);
        edittextPhone = (EditText)findViewById(R.id.edittextPhone);
        edittextDetailedAddress = (EditText)findViewById(R.id.edittextDetailedAddress);
        imageViewClearName = (ImageView)findViewById(R.id.imageViewClearName);
        imageViewClearPhone = (ImageView)findViewById(R.id.imageViewClearPhone);
        imageViewClearAddress = (ImageView)findViewById(R.id.imageViewClearAddress);

        tv_title.setText("设置回收地址");
        tv_right.setText("确定");

        tv_right.setOnClickListener(this);
        imageViewClearName.setOnClickListener(this);
        imageViewClearPhone.setOnClickListener(this);
        imageViewClearName.setOnClickListener(this);
        imageViewClearAddress.setOnClickListener(this);
        textViewNotMyCommunity.setOnClickListener(this);
        if (!TextUtils.isEmpty(mSession.getBookingCommunity())){
            textViewCommunity.setText(mSession.getBookingCommunity());
        }

        if (!TextUtils.isEmpty(mSession.getBookingDetailAddress())){
            edittextName.setText(mSession.getBookingName());
            edittextPhone.setText(mSession.getBookingPhone());
            edittextDetailedAddress.setText(mSession.getBookingDetailAddress());

            imageViewClearName.setVisibility(View.VISIBLE);
            imageViewClearPhone.setVisibility(View.VISIBLE);
            imageViewClearAddress.setVisibility(View.VISIBLE);
            Selection.setSelection(edittextName.getText(), edittextName.getText().length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //确认
            case R.id.tv_right:{
                comfirm();
                break;
            }
            //不是我所在的小区
            case R.id.textViewNotMyCommunity:{
                startActivity(new Intent(this,NotMyCommunityActivity.class));
                break;
            }
            case R.id.imageViewClearName:{
                edittextName.setText("");
                edittextName.setHint("输入姓名");
                break;
            }
            case R.id.imageViewClearPhone:{
                edittextPhone.setText("");
                edittextPhone.setHint("输入电话");
                break;
            }
            case R.id.imageViewClearAddress:{
                edittextDetailedAddress.setText("");
                edittextDetailedAddress.setHint("输入楼栋、楼层、门牌号");
                break;
            }
            default:
                break;
        }
    }

    private void comfirm(){
        final String name = edittextName.getText().toString().trim();
        final String phone = edittextPhone.getText().toString().trim();
        final String address = edittextDetailedAddress.getText().toString();
        if (TextUtils.isEmpty(name)){
            showCustomToast("姓名不能为空");
            return;
        }

        if (TextUtils.isEmpty(phone)){
            showCustomToast("手机号码不能为空");
            return;
        }

        if (!Utils.isMobileNO(phone)){
            showCustomToast("请输入正确的手机号");
            return;
        }

        if (TextUtils.isEmpty(address)){
            showCustomToast("详细地址不能为空");
            return;
        }

        mSession.addAddress(name,phone,mSession.getBookingCommunity(),address);
        startActivity(BookingSecondActivity.class);
        finish();
    }


}
