package com.quanmai.yiqu.ui.mys.realname.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.vo.RealNameMember;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.common.LoadingDialog;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.common.widget.XCRoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2016/7/6.
 * 审核（入户申请） 适配器
 */
public class MembersApplyOfCheckAdapter extends BaseAdapter implements View.OnClickListener {
    Context mContext;
    List<RealNameMember> mList; //申请用户列表
    Dialog mDialogRefuse, mDialogAgree; //拒绝、同意弹窗
    RealNameMember mCurrentMember; //当前点击用户
    private LoadingDialog mLoadingDialog;


    public MembersApplyOfCheckAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
        mLoadingDialog = new LoadingDialog(mContext, "请稍候");
        initDialog();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_application_audit, null);
            viewHolder = new ViewHolder();
            viewHolder.ivHead = (XCRoundImageView) convertView.findViewById(R.id.ivHead);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
            viewHolder.btnRefuse = (Button) convertView.findViewById(R.id.btnRefuse);
            viewHolder.btnAgree = (Button) convertView.findViewById(R.id.btnAgree);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RealNameMember member = mList.get(position);

        ImageloaderUtil.displayImage(mContext, member.imgurl, viewHolder.ivHead);
        viewHolder.tvName.setText(member.name);
        viewHolder.tvPhone.setText(StringUtil.phoneEncrypt(member.phone));
        viewHolder.btnRefuse.setTag(member);
        viewHolder.btnAgree.setTag(member);
        viewHolder.btnRefuse.setOnClickListener(this);
        viewHolder.btnAgree.setOnClickListener(this);
        return convertView;
    }

    public void add(List<RealNameMember> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clean() {
        mList.clear();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        XCRoundImageView ivHead;
        TextView tvName;
        TextView tvPhone;
        Button btnRefuse;
        Button btnAgree;
    }

    private void initDialog() {
        mDialogRefuse = DialogUtil.getConfirmDialog(mContext, "是否拒绝申请？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonConfirm: {
                        if (mDialogRefuse.isShowing()) {
                            mDialogRefuse.dismiss();
                        }
                        checkMembersApply(mCurrentMember.usermemberid, mCurrentMember.phone, "0"); //拒绝申请
                        break;
                    }
                    case R.id.buttonCancel: {
                        if (mDialogRefuse.isShowing()) {
                            mDialogRefuse.dismiss();
                        }
                        break;
                    }
                }
            }
        });

        mDialogAgree = DialogUtil.getConfirmDialog(mContext, "是否同意申请？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonConfirm: {
                        if (mDialogAgree.isShowing()) {
                            mDialogAgree.dismiss();
                        }
                        checkMembersApply(mCurrentMember.usermemberid, mCurrentMember.phone, "1"); //同意申请
                        break;
                    }
                    case R.id.buttonCancel: {
                        if (mDialogAgree.isShowing()) {
                            mDialogAgree.dismiss();
                        }
                        break;
                    }
                }
            }
        });
    }

    /**
     * 成员申请审核
     *
     * @param usercompareid
     * @param checktype     审核标识 0.不通过 1.通过
     */
    private void checkMembersApply(String usercompareid, String phone, String checktype) {
        mLoadingDialog.show();
        UserApi.get().checkMembersApply(mContext, usercompareid, phone, checktype, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                mLoadingDialog.dismiss();
                Utils.showCustomToast(mContext, data);
                mList.remove(mCurrentMember);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                mLoadingDialog.dismiss();
                Utils.showCustomToast(mContext, msg);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRefuse: {
                mCurrentMember = (RealNameMember) v.getTag();
                mDialogRefuse.show();
                break;
            }
            case R.id.btnAgree: {
                mCurrentMember = (RealNameMember) v.getTag();
                mDialogAgree.show();
                break;
            }
        }
    }
}
