package com.quanmai.yiqu.ui.mys.realname;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.vo.RealNameMember;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.RoomInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.ui.common.WebActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * 住户绑定页面
 */
public class ResidentBindingActivity extends BaseActivity implements View.OnClickListener {
    public static String ACTION_ADDRESS_CHOICE = "action_address_choice";

    private EditText editRealName;
    private EditText editPhoneNum;
    private RelativeLayout rlAddressChoice; //地址选择
    private TextView tvAddress; //地址
    private TextView tvDesignated; //指定用户介绍
    private CheckBox checkBoxAgree; //实名认证协议-同意勾选框
    private TextView tvIntroduce; //实名认证协议
    private Button btnBinding;

    private boolean isAddMembers = false; //增加会员标示符
    private String usercompareid; //实名制住户信息表id
    private String strAddress;
    private String strPhoneNum;
    private RoomInfo mRoomInfo; //地址信息实体
    private AddressChoiceBroadcastReceiver mBroadcastReceiver; //广播接收器，用以接收地址选择后的地址信息
    private Intent mIntent;

    private Dialog mBindDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_binding);
        mIntent = getIntent();
        initView();
        init();
    }

    private void initView() {
        this.btnBinding = (Button) findViewById(R.id.btnBinding);
        this.tvIntroduce = (TextView) findViewById(R.id.tvIntroduce);
        this.checkBoxAgree = (CheckBox) findViewById(R.id.checkBoxAgree);
        this.tvDesignated = (TextView) findViewById(R.id.tvDesignated);
        this.rlAddressChoice = (RelativeLayout) findViewById(R.id.rlAddressChoice);
        this.tvAddress = (TextView) findViewById(R.id.tvAddress);
        this.editPhoneNum = (EditText) findViewById(R.id.editPhoneNum);
        this.editRealName = (EditText) findViewById(R.id.editRealName);

        findViewById(R.id.iv_back).setOnClickListener(this);
        tvIntroduce.setOnClickListener(this);
        tvDesignated.setOnClickListener(this);
        btnBinding.setOnClickListener(this);
        rlAddressChoice.setOnClickListener(this);
    }

    private void init() {
        mBroadcastReceiver = new AddressChoiceBroadcastReceiver();
        isAddMembers = getIntent().getBooleanExtra("isAddMembers", false);

        if (isAddMembers) { //增加成员
            ((TextView) findViewById(R.id.tv_title)).setText("添加成员");
            editRealName.setHint("输入成员真实姓名");
            editPhoneNum.setHint("输入成员手机号");
            btnBinding.setText("确认添加");
            rlAddressChoice.setOnClickListener(null);
            findViewById(R.id.viewArrows).setVisibility(View.GONE);
            if (getIntent().hasExtra("address")) {
                tvAddress.setText(getIntent().getStringExtra("address"));
            }
            if (getIntent().hasExtra("usercompareid")) {
                usercompareid = getIntent().getStringExtra("usercompareid");
            }
        } else {
            ((TextView) findViewById(R.id.tv_title)).setText("绑定住户");
            if (!TextUtils.isEmpty(mSession.getUsername())) {
                strPhoneNum = mSession.getUsername();
                editPhoneNum.setText(StringUtil.phoneEncrypt(mSession.getUsername()));
                editPhoneNum.setFocusable(false);
                editPhoneNum.setFocusableInTouchMode(false);
            }
        }

        //设置监听，获取变动后的电话号码
        editPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                strPhoneNum = s.toString();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ACTION_ADDRESS_CHOICE);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.rlAddressChoice: {
//                startActivity(AddressChoiceActivity.class);
                Intent intent = new Intent(mContext, AddressChoiceRoomActivity.class);
                intent.putExtra("AddressTo","ResidentBindingActivity");
                startActivity(intent);
                break;
            }
            case R.id.tvDesignated: {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("http_url", getResources().getString(R.string.url_intro));
                startActivity(intent);
                break;
            }
            case R.id.tvIntroduce: {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("http_url", getResources().getString(R.string.url_protocol));
                startActivity(intent);
                break;
            }
            case R.id.btnBinding: {
                if (TextUtils.isEmpty(editRealName.getText().toString())) {
                    showCustomToast("请输入您的真实姓名");
                    return;
                }

                if (!StringUtil.isRealName(editRealName.getText().toString())) {
                    showCustomToast("仅支持2-10位中文汉字，中间不能包含空格,符号,数字等");
                    return;
                }
                if (!StringUtil.isPhoneNum(strPhoneNum)) {
                    showCustomToast("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(tvAddress.getText().toString())) {
                    showCustomToast("请选择您的住房");
                    return;
                }
                if (!checkBoxAgree.isChecked()) {
                    showCustomToast("请勾选用户协议");
                    return;
                }

                if (isAddMembers) { //户主增加成员
                    addMember(editPhoneNum.getText().toString().trim(), editRealName.getText().toString().trim(), usercompareid);
                } else { //实名制注册
                    residentBinding(editRealName.getText().toString().trim(), mRoomInfo.commcode, mRoomInfo.buildingno,
                            mRoomInfo.buildno, mRoomInfo.room);
                }


                break;
            }

            case R.id.btnApply: { //申请加入本户
                membersApply(mSession.getUsername().toString().trim(), editRealName.getText().toString().trim(), usercompareid);
                break;
            }

            case R.id.btnAppeal: { //户主有误申诉
                Intent intent = new Intent(mContext, ResidentAppealActivity.class);
                intent.putExtra("name", editRealName.getText().toString());
                intent.putExtra("phone", mSession.getUsername());
                intent.putExtra("roominfo", mRoomInfo);
                startActivity(intent);
                mBindDialog.dismiss();
                finish();
                break;
            }
            default:
                break;

        }
    }

    //实名制注册
    private void residentBinding(final String realname, String commcode, String buildingno, String unit, String room) {
        showLoadingDialog();
        UserApi.get().residentBinding(mContext, realname, commcode, buildingno, unit, room, new ApiConfig.ApiRequestListener<Map<String, String>>() {
            @Override
            public void onSuccess(String msg, Map<String, String> data) {
                dismissLoadingDialog();
                if (data == null || data.size() == 0) {
                    return;
                }

                String isexsit = data.get("isexsit");
                if ("0".equals(isexsit)) { //绑定成功
                    showCustomToast(msg);
                    ArrayList<RealNameMember> members = new ArrayList<RealNameMember>();
                    RealNameMember member = new RealNameMember(); //添加户主信息
                    member.name = editRealName.getText().toString();
                    member.phone = mSession.getUsername();
                    member.type = "1";
                    member.imgurl = "";
                    member.usermemberid = "";
                    members.add(member);

                    Intent intent = new Intent(mContext, BindingInfoActivity.class);
                    intent.putExtra("name", editRealName.getText().toString());
                    intent.putExtra("phone", mSession.getUsername());
                    intent.putExtra("address", data.get("address"));
                    intent.putExtra("usertype", "1"); //户主类型
                    intent.putExtra("usercompareid", data.get("usercompareid"));
                    intent.putExtra("membersList", members); //成员列表
                    intent.setClass(mContext, BindingInfoActivity.class);
                    startActivity(intent);
                    finish();
                } else { //绑定失败
                    String hostname = data.get("hostname");
                    String phone = data.get("phone");
                    usercompareid = data.get("usercompareid");
                    mIntent.putExtra("name", data.get("hostname"));
                    mIntent.putExtra("phone", data.get("phone"));
                    mIntent.putExtra("address", data.get("address"));
                    mBindDialog = DialogUtil.getBindingFailDialog(mContext, hostname, phone, ResidentBindingActivity.this);
                    mBindDialog.show();
                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //添加成员
    private void addMember(String phone, String name, String usercompareid) {
        showLoadingDialog();
        UserApi.get().addMember(mContext, phone, name, usercompareid, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(data);
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //用户申请入户
    private void membersApply(final String phone, final String name, String usercompareid) {
        UserApi.get().membersApply(mContext, phone, name, usercompareid, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                showCustomToast(data);
                if (mBindDialog.isShowing()) {
                    mBindDialog.dismiss();
                }
                Intent intent = getIntent();
                intent.setClass(mContext, UndeterminedActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("address", mRoomInfo.toString());
                intent.putExtra("type", UndeterminedActivity.TYPE_APPLY);
                intent.putExtra("status", "（户主审核中）");
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
                if (mBindDialog.isShowing()) {
                    mBindDialog.dismiss();
                }
            }
        });
    }

    //本地广播接收器，接收用户选择的住址信息
    private class AddressChoiceBroadcastReceiver extends android.content.BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_ADDRESS_CHOICE.equals(intent.getAction())) {
                if (intent.hasExtra("roomInfo")) {
                    mRoomInfo = (RoomInfo) intent.getSerializableExtra("roomInfo");
                    strAddress = mRoomInfo.toString();
                    tvAddress.setText(strAddress);
                }
            }
        }
    }
}
