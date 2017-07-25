package com.quanmai.yiqu.ui.mys.handwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.HandworkApi;
import com.quanmai.yiqu.api.vo.CommunityInfo;
import com.quanmai.yiqu.api.vo.MaterialOrUserDetailsInfo;
import com.quanmai.yiqu.api.vo.RoomInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.ui.mys.realname.AddressChoiceRoomActivity;
import com.quanmai.yiqu.ui.mys.realname.AddressChoiceThirdActivity;

import java.util.Map;

import static com.quanmai.yiqu.ui.mys.realname.ResidentBindingActivity.ACTION_ADDRESS_CHOICE;

/**
 * 发放环保袋页面
 */
public class GrantBagActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;
    private TextView tvGrantCode;
    private TextView tvGrantType;
    private TextView tvGrantNum;
    private ImageView imgQRCode;
    private TextView tvAddress;
    private TextView tvGrantCommunity;
    private TextView tvGrantFrequency;
    private TextView tvGrantNumEachTime;
    private LinearLayout llAddressChoose;
    private EditText editName;
    private EditText editPhoneNum;
    private Button btnConfirm;

    private String startcode = "";      //垃圾袋起始编码
    private String endcode = "";        //垃圾袋截至编码
    private String bagtype = "1";       //垃圾袋类型:1.厨余垃圾 2其他垃圾 3.有害垃圾 4.可回收 5.通用 6.通用（带收紧带）,默认是厨余垃圾
    private String commname;            //小区名
    private String commcode;            //小区编码
    private RoomInfo mRoomInfo;         //房号实体
    private AddressChoiceBroadcastReceiver mBroadcastReceiver; //广播接收器，用以接收地址选择后的地址信息
    private String strResult = "";      //袋子二维码（1.起始编码-截至编码，多个垃圾袋 2.只包含起始编码，默认30个垃圾袋）
    private Map<String, Object> mMap;   //发袋信息
    MaterialOrUserDetailsInfo.UsercompareListBean info;
    String usercompareid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant_bag);
        initView();
        init();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tvGrantCode = (TextView) findViewById(R.id.tvGrantCode);
        tvGrantType = (TextView) findViewById(R.id.tvGrantType);
        tvGrantNum = (TextView) findViewById(R.id.tvGrantNum);
        imgQRCode = (ImageView) findViewById(R.id.imgQRCode);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvGrantCommunity = (TextView) findViewById(R.id.tvGrantCommunity);
        tvGrantFrequency = (TextView) findViewById(R.id.tvGrantFrequency);
        tvGrantNumEachTime = (TextView) findViewById(R.id.tvGrantNumEachTime);
        llAddressChoose = (LinearLayout) findViewById(R.id.llAddressChoose);
        editName = (EditText) findViewById(R.id.editName);
        editPhoneNum = (EditText) findViewById(R.id.editPhoneNum);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        iv_back.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        imgQRCode.setOnClickListener(this);
        llAddressChoose.setOnClickListener(this);
    }

    private void init() {
        tv_title.setText("发放环保袋");
        mBroadcastReceiver = new AddressChoiceBroadcastReceiver();
        if (getIntent().hasExtra("strResult")) {
            strResult = getIntent().getStringExtra("strResult");
        }
        if (getIntent().hasExtra("commname")) {
            commname = getIntent().getStringExtra("commname");
        }
        if (getIntent().hasExtra("Commname")) {
            commname = getIntent().getStringExtra("Commname");
        }
        if (getIntent().hasExtra("commcode")) {
            commcode = getIntent().getStringExtra("commcode");
        }
        if (getIntent().hasExtra("UsercompareListBean")){
            info= (MaterialOrUserDetailsInfo.UsercompareListBean) getIntent().getSerializableExtra("UsercompareListBean");
            usercompareid=info.getId()+"";
        }
        if (info!=null){
            fillUserMsg(commname,info);
        }
        //切割二维码
        if (!strResult.contains("-")&&strResult.length()<30) { //只包含起始编码，默认每卷垃圾袋30个
            startcode = strResult;
            endcode = String.valueOf(Long.valueOf(StringUtil.numberFilter(strResult)) + 29);
        } else { //起始编码-截至编码
            String[] strings = strResult.trim().split("-");
            if (strings.length > 0) {
                startcode = strings[0].trim();
            }
            if (strings.length > 1) {
                endcode = strings[1].trim();
            }
        }
        if (startcode.length() != QRCodeInputActivity.BAG_LENGTH_TYPE_1) {
            bagtype = startcode.substring(1, 2);
        }
        getGrantBagInfo(startcode + "-" + endcode);
    }

    public void fillUserMsg(String commname,MaterialOrUserDetailsInfo.UsercompareListBean info){
        tvAddress.setText(commname+info.getHoursenum());
        editName.setText(info.getName());
        editPhoneNum.setText(info.getMtel());
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

    //获取指定户下的用户信息
    private void getRealNameUserInfo(String commcode, String buildingno, String unit, String room) {
        HandworkApi.get().realNameUserInfo(mContext, commcode, buildingno, unit, room, new ApiConfig.ApiRequestListener<Map<String, Object>>() {
            @Override
            public void onSuccess(String msg, Map<String, Object> data) {
                if (data == null || data.size() == 0) {
                    return;
                }
                editName.setText((String) data.get("receiveman"));
                editPhoneNum.setText((String) data.get("receivemtle"));
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    //发袋信息
    private void getGrantBagInfo(String barcode) {
        showLoadingDialog();
        HandworkApi.get().getGrantBagInfo(mContext, barcode, new ApiConfig.ApiRequestListener<Map<String, Object>>() {
            @Override
            public void onSuccess(String msg, Map<String, Object> data) {
                dismissLoadingDialog();
                if (data == null || data.size() == 0) {
                    return;
                }
                mMap = data;
                commcode = (String) mMap.get("commcode");
                commname = (String) mMap.get("commname");
                tvGrantCode.setText("发放编码：" + (String) data.get("barcode"));
                String strBagType = "";
                bagtype = (String) data.get("bagtype");
                switch (bagtype) {//垃圾袋类型:（1.厨余垃圾 2其他垃圾 3.有害垃圾 4.可回收）
                    case "1": {
                        strBagType = "厨余垃圾";
                        break;
                    }
                    case "2": {
                        strBagType = "其他垃圾";
                        break;
                    }
                    case "3": {
                        strBagType = "有害垃圾";
                        break;
                    }
                    case "4": {
                        strBagType = "可回收垃圾";
                        break;
                    }
                    default:
                        bagtype = "1";
                        strBagType = "厨余垃圾";
                        break;
                }
                tvGrantType.setText("袋子类型：" + strBagType);
                tvGrantNum.setText("发放数量：" + (String) data.get("bagnum"));
                tvGrantCommunity.setText("发放小区：" + (String) data.get("commname"));
                tvGrantNumEachTime.setText("每次发放：" + (String) data.get("issuenums") + "只");
                if ("1".equals((String) data.get("issuetype"))) {
                    tvGrantFrequency.setText("发放频率：按月发放");
                } else {
                    tvGrantFrequency.setText("发放频率：按季度发放");
                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
            }
        });
    }

    //发袋信息提交
    private void submitGrantBagInfo(String usercompareid, String phone, String realname, String bagnum,
                                    String bagtype, String issuetype, RoomInfo roomInfo) {
        showLoadingDialog();
        HandworkApi.get().submitGrantBagInfo(mContext, usercompareid, phone, realname, startcode, endcode,
                bagnum, bagtype, issuetype,
                (String) mMap.get("commcode"), (String) mMap.get("commname"),
                roomInfo.buildingno, roomInfo.buildno, roomInfo.room, new ApiConfig.ApiRequestListener<String>() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.btnConfirm: { //提交
                submit();
                break;
            }
            case R.id.imgQRCode: { //重扫二维码
                Intent intent = getIntent();
                intent.setClass(mContext, ScanningActivity.class);
                intent.putExtra("type", ScanningActivity.TYPE_GRANT_BAG);
                startActivity(intent);
                break;
            }
            case R.id.llAddressChoose: {
//                Intent intent = new Intent(mContext, AddressChoiceThirdActivity.class);
                Intent intent = new Intent(mContext, AddressChoiceRoomActivity.class);
                CommunityInfo communityInfo = new CommunityInfo();
                communityInfo.commcode = commcode;
                communityInfo.commname = commname;
                intent.putExtra("communityInfo", communityInfo);
                intent.putExtra("isFuzzySearch", true);
                startActivity(intent);
                break;
            }

        }
    }

    private void submit() {
        String tvAddressString = tvAddress.getText().toString().trim();
        if (TextUtils.isEmpty(tvAddressString)) {
            showCustomToast("请选择领取住房");
            return;
        }

        String editPhoneNumString = editPhoneNum.getText().toString().trim();
//        if (TextUtils.isEmpty(editPhoneNumString)) {
//            return;
//        }

        String editNameString = editName.getText().toString().trim();
//        if (TextUtils.isEmpty(editNameString)) {
//        showCustomToast("输入领取人姓名");
//            return;
//        }

        submitGrantBagInfo(usercompareid,editPhoneNumString, editNameString,
                (String) mMap.get("bagnum"), bagtype, (String) mMap.get("issuetype"),
                mRoomInfo);
    }

    //本地广播接收器，接收用户选择的住址信息
    private class AddressChoiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_ADDRESS_CHOICE.equals(intent.getAction())) {
                if (intent.hasExtra("roomInfo")) {
                    mRoomInfo = (RoomInfo) intent.getSerializableExtra("roomInfo");
                    usercompareid=mRoomInfo.usercompareid;
                    tvAddress.setText(mRoomInfo.buildingno + mRoomInfo.buildno + mRoomInfo.room);
                    editName.setText(mRoomInfo.name);
                    editPhoneNum.setText(mRoomInfo.phone);
                    getRealNameUserInfo(commcode, mRoomInfo.buildingno, mRoomInfo.buildno, mRoomInfo.room);
                }
            }
        }
    }
}
