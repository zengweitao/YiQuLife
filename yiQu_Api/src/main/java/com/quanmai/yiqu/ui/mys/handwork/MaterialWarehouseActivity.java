package com.quanmai.yiqu.ui.mys.handwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.HandworkApi;
import com.quanmai.yiqu.api.vo.CompanyInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.StringUtil;

import java.util.List;

/**
 * 物料入库页面
 */
public class MaterialWarehouseActivity extends BaseActivity implements View.OnClickListener {
    public static int REQUEST_CODE_UNIT = 101;

    private ImageView iv_back;
    private TextView tv_title;
    private TextView tvUnit;
    private LinearLayout llUnit;
    private EditText edtName;
    private EditText editPhoneNum;
    private Button btnConfirm;

    private ImageView imgQRCode;
    private TextView tvMaterialCode;
    private TextView tvMaterialType;
    private TextView tvMaterialNum;
    private TextView tvReceiver;
    private TextView tvReceiveCommunity;

    private String strResult = "";  //袋子二维码（1.起始编码-截至编码，多个垃圾袋 2.只包含起始编码，默认30个垃圾袋）
    private String commname;        //小区名
    private String commcode;        //小区编码
    private String startcode = "";  //垃圾袋起始编码（单个时为垃圾袋编码）
    private String endcode = "";    //垃圾袋截至编码
    private String bagtype = "1";   //垃圾袋类型:1.厨余垃圾 2其他垃圾 3.有害垃圾 4.可回收 5.通用 6.通用（带收紧带）,默认是厨余垃圾
    private Long bagnum;            //发放数量
    private CompanyInfo mCompanyInfo;
    private String[] strBagTypes = {"厨余垃圾", "其他垃圾", "有害垃圾", "可回收", "通用", "通用（带收紧带）"};
    private int intBagType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_warehouse);
        initView();
        init();
        getUnitListInfo();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("物料入库");
        tvUnit = (TextView) findViewById(R.id.tvUnit);
        llUnit = (LinearLayout) findViewById(R.id.llUnit);
        edtName = (EditText) findViewById(R.id.edtName);
        editPhoneNum = (EditText) findViewById(R.id.editPhoneNum);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        imgQRCode = (ImageView) findViewById(R.id.imgQRCode);

        iv_back.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        llUnit.setOnClickListener(this);
        imgQRCode.setOnClickListener(this);
        tvMaterialCode = (TextView) findViewById(R.id.tvMaterialCode);
        tvMaterialType = (TextView) findViewById(R.id.tvMaterialType);
        tvMaterialNum = (TextView) findViewById(R.id.tvMaterialNum);
        tvReceiver = (TextView) findViewById(R.id.tvReceiver);
        tvReceiveCommunity = (TextView) findViewById(R.id.tvReceiveCommunity);
    }

    private void init() {
        if (getIntent().hasExtra("strResult")) {
            strResult = getIntent().getStringExtra("strResult");
        }
        if (getIntent().hasExtra("commname")) {
            commname = getIntent().getStringExtra("commname");
        }else {
            commname = UserInfo.get().community;
        }
        if (getIntent().hasExtra("commcode")) {
            commcode = getIntent().getStringExtra("commcode");
        }else {
            commcode = UserInfo.get().commcode;
        }

        //切割二维码
        if (!strResult.contains("-")) { //只包含起始编码
            startcode = strResult;
            endcode = String.valueOf(Long.valueOf(StringUtil.numberFilter(strResult))+29);
            bagnum = (long) 30;
        } else { //起始编码-截至编码
            String[] strings = strResult.trim().split("-");
            if (strings.length > 0) {
                startcode = strings[0].trim();
            }
            if (strings.length > 1) {
                endcode = strings[1].trim();
            }
            bagnum = Long.decode(StringUtil.numberFilter(endcode)) -
                    Long.decode(StringUtil.numberFilter(startcode)) + 1;
        }

        if (startcode.length() == QRCodeInputActivity.BAG_LENGTH_TYPE_2) {
            bagtype = startcode.substring(1, 2);
        }

        tvMaterialCode.setText("物料编码：" + startcode);
        tvMaterialNum.setText("物料数量：" + bagnum);

        String strBagType = "";
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
            case "5": {
                strBagType = "通用";
                break;
            }
            case "6": {
                strBagType = "通用【带收紧带】";
                break;
            }
            default: {
                bagtype = "1";
                strBagType = "厨余垃圾";
            }
        }
        if (getIntent().hasExtra("intBagType")) {
            intBagType = getIntent().getIntExtra("intBagType",0);
            bagtype = intBagType+"";
            if (intBagType>0){
                strBagType = strBagTypes[intBagType-1];
            }
        }
        tvMaterialType.setText("袋子类型：" + strBagType);
        tvReceiveCommunity.setText("接收小区：" + commname);
        tvReceiver.setText("接收人：" + UserInfo.get().username);
    }

    //库存信息提交
    private void submitRepertoryInfo(String phone, String realname, String barcode,
                                     String commcode, String commname,
                                     String company) {
        showLoadingDialog();
        HandworkApi.get().submitRepertoryInfo(mContext, phone, realname, startcode, endcode,
                String.valueOf(bagnum), bagtype, commcode, commname, company, new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                        finish();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_UNIT && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("companyInfo")) {
                mCompanyInfo = (CompanyInfo) data.getSerializableExtra("companyInfo");
                tvUnit.setText(mCompanyInfo.companyname);
                edtName.setText(mCompanyInfo.deliveryman);
                editPhoneNum.setText(mCompanyInfo.deliverymtel);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.btnConfirm: { //确认入库
                submit();
                break;
            }
            case R.id.imgQRCode: { //重扫
                Intent intent = getIntent();
                intent.setClass(mContext, ScanningActivity.class);
                intent.putExtra("type", ScanningActivity.TYPE_WAREHOUSE);
                startActivity(intent);
                break;
            }
            case R.id.llUnit: { //选择送货单位
                Intent intent = new Intent(mContext, UnitChooseActivity.class);
                startActivityForResult(intent, REQUEST_CODE_UNIT);
                break;
            }
            default:
                break;
        }
    }

    private void submit() {
        // validate
        String edtNameString = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(edtNameString)) {
            showCustomToast("请输入送货员姓名");
            return;
        }

        String edtPhoneNumString = editPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(edtPhoneNumString)) {
            showCustomToast("请输入送货员手机号码");
            return;
        }

        String tvUnitString = tvUnit.getText().toString().trim();
        if (TextUtils.isEmpty(tvUnitString)) {
            showCustomToast("请选择送货单位");
            return;
        }

        // TODO validate success, do something
        submitRepertoryInfo(edtPhoneNumString, edtNameString, strResult, commcode, commname, tvUnitString);
    }

    //获取送货单位列表信息
    private void getUnitListInfo() {
        showLoadingDialog();
        HandworkApi.get().unitListInfo(mContext, new ApiConfig.ApiRequestListener<List<CompanyInfo>>() {
            @Override
            public void onSuccess(String msg, List<CompanyInfo> data) {
                dismissLoadingDialog();
                if (data == null || data.size() == 0) {
                    return;
                }
                tvUnit.setText(data.get(0).companyname);
                edtName.setText(data.get(0).deliveryman);
                editPhoneNum.setText(data.get(0).deliverymtel);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }
}
