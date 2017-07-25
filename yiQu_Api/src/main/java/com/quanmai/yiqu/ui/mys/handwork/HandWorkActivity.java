package com.quanmai.yiqu.ui.mys.handwork;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.HandworkApi;
import com.quanmai.yiqu.api.vo.BagRepertoryInfo;
import com.quanmai.yiqu.api.vo.DialogCommunityInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.widget.SerializableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 手工发袋界面
 */
public class HandWorkActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tvCommname;            //小区名
    private TextView tvAddress;             //地址
    private ImageView imgGrantBag;          //发放环保袋组件
    private ImageView imgWarehouse;         //物料入库组件
    private RelativeLayout rlMaterialTitle,rlMaterialContent; //物料入库组件
    private LinearLayout llMaterialDetails,llMaterial; //物料详情
    private TextView tv_title;

    private String commname;
    private String address;
    private String commcode;
    private Map<String, Object> mMap;               //垃圾袋库存详情
    private CommonList<BagRepertoryInfo> garList;   //垃圾袋库存列表
    private TextView btnChangeCommname;
    List<DialogCommunityInfo.CommListBean> infos;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_work);
        initView();
        init();
        getCommunityList();
        getDialog();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("手工发袋");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tvCommname = (TextView) findViewById(R.id.tvCommname);
        btnChangeCommname = (TextView) findViewById(R.id.btnChangeCommname);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        imgGrantBag = (ImageView) findViewById(R.id.imgGrantBag);
        imgWarehouse = (ImageView) findViewById(R.id.imgWarehouse);
        rlMaterialTitle = (RelativeLayout) findViewById(R.id.rlMaterialTitle);
        rlMaterialContent = (RelativeLayout) findViewById(R.id.rlMaterialContent);
        llMaterialDetails = (LinearLayout) findViewById(R.id.llMaterialDetails);
        llMaterial = (LinearLayout) findViewById(R.id.llMaterial);

        iv_back.setOnClickListener(this);
        imgGrantBag.setOnClickListener(this);
        imgWarehouse.setOnClickListener(this);
        llMaterial.setOnClickListener(this);
        btnChangeCommname.setOnClickListener(this);
    }

    private void init() {
        infos = new ArrayList<>();
        getRepertoryInfo();
    }

    //获取库存信息
    private void getRepertoryInfo() {
        showLoadingDialog();
        HandworkApi.get().getRepertoryInfo(mContext, new ApiConfig.ApiRequestListener<Map<String, Object>>() {
            @Override
            public void onSuccess(String msg, Map<String, Object> data) {
                dismissLoadingDialog();
                if (data == null) {
                    return;
                }
                commname = (String) data.get("commname");
                address = (String) data.get("address");
                commcode = (String) data.get("commcode");
                tvCommname.setText((String) data.get("commname"));
                tvAddress.setText((String) data.get("address"));
                garList = (CommonList<BagRepertoryInfo>) data.get("garList");
                refreshBagRepertory(garList);
                getRepertoryDetails((String) data.get("commcode"));
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                Log.i("获取库存信息", msg);
            }
        });
    }

    //刷新垃圾袋库存信息
    private void refreshBagRepertory(List<BagRepertoryInfo> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        rlMaterialTitle.setVisibility(View.VISIBLE);
        rlMaterialContent.setVisibility(View.VISIBLE);
        for (BagRepertoryInfo info : list) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_bag_repertory, null);
            TextView tvBagType = (TextView) itemView.findViewById(R.id.tvBagType);
            TextView tvBagNum = (TextView) itemView.findViewById(R.id.tvBagNum);
            ImageView imgRound= (ImageView) itemView.findViewById(R.id.imgRound);
            if (info.bagtype.contains("厨余")){
                imgRound.setImageResource(R.drawable.dor_cy);
            }else if (info.bagtype.contains("其他")){
                imgRound.setImageResource(R.drawable.dor_qt);
            }else if (info.bagtype.contains("有害")){
                imgRound.setImageResource(R.drawable.dor_yh);
            }else if (info.bagtype.contains("可回收")){
                imgRound.setImageResource(R.drawable.dor_khs);
            }else if (info.bagtype.contains("通用")){
                imgRound.setImageResource(R.drawable.dor_khs);
            }else if (info.bagtype.contains("带收紧带")){
                imgRound.setImageResource(R.drawable.dor_khs);
            }
            tvBagNum.setText(info.bagnums + "只");
            tvBagType.setText(info.bagtype);
            llMaterialDetails.addView(itemView);
        }
    }

    //获取库存详情
    private void getRepertoryDetails(String commcode) {
        HandworkApi.get().getRepertoryDetails(mContext, commcode, new ApiConfig.ApiRequestListener<Map<String, Object>>() {
            @Override
            public void onSuccess(String msg, Map<String, Object> data) {
                dismissLoadingDialog();
                if (data == null) {
                    return;
                }
                mMap = data;
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                Log.i("获取库存详情", msg);
            }
        });
    }

    /**
     * 获取绑定过小区列表
     */
    public void getCommunityList(){
        HandworkApi.get().getCommunityList(mContext, UserInfo.get().getUsertype(), new ApiConfig.ApiRequestListener<DialogCommunityInfo>() {
            @Override
            public void onSuccess(String msg, DialogCommunityInfo data) {
                showCustomToast(msg);
                if (data!=null){
                    infos.addAll(data.getCommList());
                }
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    public void getDialog(){
        mDialog = DialogUtil.getChooseCommunityDialog(mContext, "选择小区", infos, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.dismiss();
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
            case R.id.btnChangeCommname: {
                mDialog.show();
                break;
            }
            case R.id.imgGrantBag: {
                if (!UserInfo.get().usertype.contains(UserInfo.USER_HANDWORK) &&
                        !UserInfo.get().usertype.contains(UserInfo.USER_ALL)){
                    showCustomToast("没有垃圾袋发放权限");
                    return;
                }
                Intent intent = new Intent(mContext, ScanningActivity.class);
                intent.putExtra("commname", commname);
                intent.putExtra("commcode", commcode);
                intent.putExtra("type", ScanningActivity.TYPE_GRANT_BAG);
                startActivity(intent);
                break;
            }
            case R.id.imgWarehouse: {
                if (!UserInfo.get().usertype.contains(UserInfo.USER_WAREHOUSE)&&
                        !UserInfo.get().usertype.contains(UserInfo.USER_ALL)){
                    showCustomToast("没有物料入库权限");
                    return;
                }
                Intent intent = new Intent(mContext, ScanningActivity.class);
                intent.putExtra("commname", commname);
                intent.putExtra("commcode", commcode);
                intent.putExtra("whichjump","");
                intent.putExtra("type", ScanningActivity.TYPE_WAREHOUSE);
                startActivity(intent);
                break;
            }
            case R.id.llMaterial: {
                if (garList == null || garList.size() == 0) {
                    return;
                }
                Intent intent = new Intent(mContext, MaterialDetailsActivity.class);
                intent.putExtra("commname", commname);
                intent.putExtra("commcode", commcode);
                intent.putExtra("address", address);
                intent.putExtra("garList", garList);
                intent.putExtra("whichjump","");
                SerializableMap serializableMap = new SerializableMap();
                serializableMap.setMap(mMap);
                intent.putExtra("repertoryDetails", serializableMap);
                startActivity(intent);
                break;
            }

            default:
                break;
        }
    }
}
