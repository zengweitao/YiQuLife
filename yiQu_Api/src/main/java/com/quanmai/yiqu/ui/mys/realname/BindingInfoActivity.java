package com.quanmai.yiqu.ui.mys.realname;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.vo.RealNameMember;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.StringUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * 绑定住户信息
 */
public class BindingInfoActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE_UNBIND = 101; //取消绑定
    private final int REQUEST_CODE_CHECK = 102; //入户申请审核
    private final int REQUEST_CODE_ADD_MEMBER = 103; //添加成员

    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAddress;
    private RelativeLayout rlCheckOfApply; //入户申请
    private RelativeLayout rlQRCode;    //用户二维码
    private LinearLayout llMyMember;
    private Button btnAddMembers;
    private TextView tv_right;

    private String mName; //住户姓名
    private String mPhone; //住户手机号
    private String mAddress; //住户住房地址
    private String usertype; //用户类型 0.无 1.户主 2.成员
    private ArrayList<RealNameMember> membersList; //成员信息列表
    private RealNameMember mCurrentMember; //当前点击成员
    private String housecode;  //住户编码
    private String housecodeX;  //加密过的住户编码

    private Dialog mDeleteDialog; //删除确认弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_info);
        initView();
        init();
        initMember();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("绑定住户");
        findViewById(R.id.iv_back).setOnClickListener(this);

        this.btnAddMembers = (Button) findViewById(R.id.btnAddMembers);
        this.llMyMember = (LinearLayout) findViewById(R.id.llMyMember);
        this.rlCheckOfApply = (RelativeLayout) findViewById(R.id.rlCheckOfApply);
        this.rlQRCode = (RelativeLayout) findViewById(R.id.rlQRCode);
        this.tvAddress = (TextView) findViewById(R.id.tvAddress);
        this.tvPhone = (TextView) findViewById(R.id.tvPhone);
        this.tvName = (TextView) findViewById(R.id.tvName);
        this.tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);

        rlCheckOfApply.setOnClickListener(this);
        rlQRCode.setOnClickListener(this);
        btnAddMembers.setOnClickListener(this);
    }

    private void init() {
        mName = getIntent().getStringExtra("name");
        mPhone = getIntent().getStringExtra("phone");
        mAddress = getIntent().getStringExtra("address");
        usertype = getIntent().getStringExtra("usertype");
        housecode = getIntent().getStringExtra("housecode");
        housecodeX = getIntent().getStringExtra("housecodeX");
        if ("1".equals(usertype)) {
            rlCheckOfApply.setVisibility(View.VISIBLE);
            tv_right.setText("申请解绑");
        } else if ("2".equals(usertype)) {
            btnAddMembers.setVisibility(View.GONE);
            tv_right.setText("退出本户");
        }

        membersList = new ArrayList<>();
        if (getIntent().hasExtra("membersList")) {
            membersList = (ArrayList<RealNameMember>) getIntent().getSerializableExtra("membersList");
        }
        if (membersList.size() == 0) {
            getResidentBindingInfo();
        }

        tvName.setText(mName);
        tvPhone.setText(StringUtil.phoneEncrypt(mPhone));
        if (!TextUtils.isEmpty(mAddress)) {
            tvAddress.setText(mAddress);
        }
    }

    //初始成员列表
    private void initMember() {
        llMyMember.setLayoutTransition(new LayoutTransition()); //添加动画
        if (membersList != null && membersList.size() > 0) {
            for (int i = 0; i < membersList.size(); i++) {
                View childView = LayoutInflater.from(mContext).inflate(R.layout.item_resident_member_info, null);
                TextView tvMemberName = (TextView) childView.findViewById(R.id.tvMemberName);
                TextView tvMemberPhone = (TextView) childView.findViewById(R.id.tvMemberPhone);
                TextView tvMaster = (TextView) childView.findViewById(R.id.tvMaster);
                ImageButton btnDelete = (ImageButton) childView.findViewById(R.id.btnDelete);

                final RealNameMember member = membersList.get(i);
                if ("1".equals(member.type)) { //成员-户主
                    tvMaster.setVisibility(View.VISIBLE);
                } else if ("0".equals(member.type)) { //成员-普通成员
                    tvMaster.setVisibility(View.GONE);
                    if ("1".equals(usertype)) { //用户是户主
                        btnDelete.setVisibility(View.VISIBLE);
                    }
                }
                tvMemberName.setText(member.name);
                tvMemberPhone.setText(StringUtil.phoneEncrypt(member.phone));
                btnDelete.setTag(member);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentMember = (RealNameMember) v.getTag();
                        mDeleteDialog = DialogUtil.getConfirmDialog(mContext, "确认删除", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (v.getId() == R.id.buttonConfirm) {
                                    mDeleteDialog.dismiss();
                                    deleteMember(member.usermemberid, member.phone);
                                } else {
                                    mDeleteDialog.dismiss();
                                }
                            }
                        });
                        mDeleteDialog.show();
                    }
                });
                llMyMember.addView(childView);
            }
        }
    }

    //添加新成员
    private void addMember(ArrayList<RealNameMember> newMemberList) {
        for (final RealNameMember member : newMemberList) { //遍历成员集合
            if (!isContains(member)) {
                //如果新的成员不存在于旧的成员集合
                membersList.add(member); //将新成员添加入集合
                View childView = LayoutInflater.from(mContext).inflate(R.layout.item_resident_member_info, null);
                TextView tvMemberName = (TextView) childView.findViewById(R.id.tvMemberName);
                TextView tvMemberPhone = (TextView) childView.findViewById(R.id.tvMemberPhone);
                TextView tvMaster = (TextView) childView.findViewById(R.id.tvMaster);
                ImageButton btnDelete = (ImageButton) childView.findViewById(R.id.btnDelete);

                if ("1".equals(member.type)) { //成员-户主
                    tvMaster.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.GONE);
                } else if ("0".equals(member.type)) { //成员-普通成员
                    tvMaster.setVisibility(View.GONE);
                    if ("1".equals(usertype)) { //用户是户主
                        btnDelete.setVisibility(View.VISIBLE);
                    }
                }
                tvMemberName.setText(member.name);
                tvMemberPhone.setText(StringUtil.phoneEncrypt(member.phone));
                btnDelete.setTag(member);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentMember = (RealNameMember) v.getTag();
                        mDeleteDialog = DialogUtil.getConfirmDialog(mContext, "确认删除", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (v.getId() == R.id.buttonConfirm) {
                                    mDeleteDialog.dismiss();
                                    deleteMember(member.usermemberid, member.phone);
                                } else {
                                    mDeleteDialog.dismiss();
                                }
                            }
                        });
                        mDeleteDialog.show();
                    }
                });
                llMyMember.addView(childView);
            }
        }
    }

    //成员集合是否包括指定的成员
    private boolean isContains(RealNameMember member) {
        if (member != null) {
            for (int i = 0; i < membersList.size(); i++) { //遍历旧的成员集合
                if (member.compare(membersList.get(i))) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < membersList.size(); i++) {
                if (membersList.get(i) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_UNBIND == requestCode && Activity.RESULT_OK == resultCode) {
            finish();
        } else if (REQUEST_CODE_CHECK == requestCode && Activity.RESULT_OK == resultCode) {
            getResidentBindingInfo();
        } else if (REQUEST_CODE_ADD_MEMBER == requestCode && Activity.RESULT_OK == resultCode) {
            getResidentBindingInfo();
        }
    }

    //获取实名制用户信息
    private void getResidentBindingInfo() {
        UserApi.get().getResidentBindingInfo(mContext, new ApiConfig.ApiRequestListener<Map<String, Object>>() {
            @Override
            public void onSuccess(String msg, Map<String, Object> data) {
                if (data == null || data.size() <= 0) {
                    showCustomToast(msg);
                    return;
                }
                ArrayList<RealNameMember> newMemberList = (ArrayList<RealNameMember>) data.get("membersList");
                addMember(newMemberList);
                housecode = (String) data.get("housecode");
                housecodeX = (String) data.get("housecodeX");
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //删除成员
    private void deleteMember(String usermemberid, String phone) {
        showLoadingDialog();
        UserApi.get().deleteMember(mContext, usermemberid, phone, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                int position = membersList.indexOf(mCurrentMember);
                membersList.remove(position);
                llMyMember.removeViewAt(position);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //用户退出本户，直接解绑
    private void membersExit() {
        showLoadingDialog();
        String householdPhone = ""; //户主电话
        for (RealNameMember member : membersList) {
            if ("1".equals(member.type)) {
                householdPhone = member.phone;
            }
        }
        UserApi.get().userMembersExit(mContext, householdPhone, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(data);
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
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.rlQRCode: { //住户二维码
                Dialog dialog = DialogUtil.getUserQRCodeDialog(mContext, housecode, housecodeX);
                if (dialog != null) {
                    dialog.show();
                }
                break;
            }
            case R.id.rlCheckOfApply: { //入户申请审核
                Intent intent = getIntent();
                intent.setClass(mContext, MembersApplyOfCheckActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CHECK);
                break;
            }
            case R.id.btnAddMembers: { //增加成员
                Intent intent = getIntent();
                intent.putExtra("isAddMembers", true);
                intent.setClass(this, ResidentBindingActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_MEMBER);
                break;
            }
            case R.id.tv_right: {
                if ("1".equals(usertype)) { //申请解绑
                    Intent intent = this.getIntent();
                    intent.setClass(mContext, ResidentUnbindActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_UNBIND);
                } else if ("2".equals(usertype)) { //退出本户
                    membersExit();
                }
                break;
            }
            default:
                break;
        }
    }
}
