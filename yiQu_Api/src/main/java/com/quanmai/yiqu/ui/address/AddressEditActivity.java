package com.quanmai.yiqu.ui.address;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.AddressApi;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.vo.AddressInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;

public class AddressEditActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_title,tv_right;
    private AddressInfo mAddressInfo;
    private EditText editText_address_name,editText_address_phone,editText_address_address;
    private TextView textView_address_community;
    boolean isDefault=false;//true为默认地址
    private ImageView imageview_address_default;
    String name=null;
    String phone=null;
    String address=null;
    private LinearLayout linear_address_default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);
        init();
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("编辑地址");
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("添加");
        editText_address_name = (EditText) findViewById(R.id.editText_address_name);
        editText_address_name.setFilters(new InputFilter[]{Utils.getEditTextLength(8)}); //即限定最大输入字符数为20
        editText_address_phone = (EditText) findViewById(R.id.editText_address_phone);
        editText_address_address = (EditText) findViewById(R.id.editText_address_address);
        editText_address_address.setFilters(new InputFilter[]{Utils.getEditTextLength(40)});
        linear_address_default = (LinearLayout) findViewById(R.id.linear_address_default);
        imageview_address_default = (ImageView) findViewById(R.id.imageview_address_default);
        textView_address_community = (TextView) findViewById(R.id.textView_address_community);
        textView_address_community.setText(UserInfo.get().getCommunity());
        if (getIntent().hasExtra("EditOrAdd")&&getIntent().getStringExtra("EditOrAdd").equals("Edit")){
            tv_right.setText("保存");
            mAddressInfo = (AddressInfo) getIntent().getSerializableExtra("AddressInfo");
            Log.e("--地址id","== "+mAddressInfo.id);
            if (mAddressInfo.sortfield==1){
                imageview_address_default.setEnabled(false);
                imageview_address_default.setBackgroundResource(R.drawable.icon_check);
                isDefault=true;
            }
            name=mAddressInfo.name;
            phone=mAddressInfo.phone;
            address=mAddressInfo.address;
            editText_address_name.setText(name);
            editText_address_name.requestFocus();
            editText_address_name.setSelection(editText_address_name.getText().length());
            editText_address_phone.setText(phone);
            editText_address_address.setText(address);
        }else {
            linear_address_default.setVisibility(View.INVISIBLE);
        }
        tv_right.setOnClickListener(this);
        imageview_address_default.setOnClickListener(this);
        editText_address_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                name=editText_address_name.getText().toString();
            }
        });
        editText_address_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                phone=editText_address_phone.getText().toString();
            }
        });
        editText_address_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                address=editText_address_address.getText().toString();
            }
        });

    }

    /**
     * 编辑地址
     */
    public void EditAddress(){
        Log.e("--输入内容、内容","== "+name+" "+phone+" "+address);
        showLoadingDialog("请稍后！");
        AddressApi.get().AddressEdit(mContext, mAddressInfo.id, name, phone,
                    address,
                    new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                        if (isDefault&&mAddressInfo.sortfield!=1){
                            DefaultAddress(mAddressInfo.id);
                        }else {
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
    /**
     * 新增地址
     */
    public void AddAddress(){
        showLoadingDialog("请稍后！");
        AddressApi.get().AddressAdd(mContext, editText_address_name.getText().toString(),
                editText_address_phone.getText().toString(),
                editText_address_address.getText().toString(),
                new ApiConfig.ApiRequestListener<String>() {
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
    /**
     * 设置默认地址
     */
    public void DefaultAddress(int id){
        AddressApi.get().AddressDefault(mContext,id,
                new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                        setResult(0);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_right:
                if(editText_address_name.getText().equals(null)||editText_address_name.getText().equals("")
                        ||editText_address_name.getText().equals("null")){
                    showCustomToast("姓名/电话/地址均不能为空！！！");
                    return;
                }
                if(editText_address_phone.getText().equals(null)||editText_address_phone.getText().equals("")
                        ||editText_address_phone.getText().equals("null")){
                    showCustomToast("姓名/电话/地址均不能为空！！！");
                    return;
                }
                if(editText_address_phone.getText().equals(null)||editText_address_phone.getText().equals("")
                        ||editText_address_phone.getText().equals("null")){
                    showCustomToast("姓名/电话/地址均不能为空！！！");
                    return;
                }
                if (!Utils.isMobileNO(phone)) {
                    showCustomToast("请输入正确的手机号");
                    return;
                }
                if (getIntent().hasExtra("EditOrAdd")&&getIntent().getStringExtra("EditOrAdd").equals("Edit")){
                    EditAddress();
                    return;
                }
                AddAddress();
                break;
            case R.id.imageview_address_default:
                if (isDefault){
                    imageview_address_default.setBackgroundResource(R.drawable.icon_check_no);
                    isDefault=false;
                }else {
                    imageview_address_default.setBackgroundResource(R.drawable.icon_check);
                    isDefault=true;
                }
                break;
        }
    }
}
