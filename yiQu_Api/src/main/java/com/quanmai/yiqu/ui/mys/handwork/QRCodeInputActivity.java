package com.quanmai.yiqu.ui.mys.handwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;

import static com.quanmai.yiqu.R.id.iv_back;
import static com.quanmai.yiqu.ui.mys.handwork.ScanningActivity.TYPE_GRANT_BAG;
import static com.quanmai.yiqu.ui.mys.handwork.ScanningActivity.TYPE_WAREHOUSE;
import static com.quanmai.yiqu.ui.mys.handwork.ScanningActivity.TYPE_PUTAWAY;

/**
 * 二维码手动输入界面
 */
public class QRCodeInputActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private EditText edtStartCode;
    private EditText edtEndCode;
    private EditText edtBagType;
    private Button btnConfirm;
    private int type;                           //扫描类型
    private Spinner spinnerBagType;
//    private int[] arrBagType = {1, 2, 3, 4, 5, 6};    //垃圾袋类型:（//垃圾袋类型:1.厨余垃圾 2其他垃圾 3.有害垃圾 4.可回收 5.通用 6.通用（带收紧带）,默认是厨余垃圾）
    private int[] arrBagType = {1, 2};    //垃圾袋类型:（//垃圾袋类型:1.厨余垃圾 2其他垃圾 默认是厨余垃圾）
//    private String[] strBagType = {"厨余垃圾", "其他垃圾", "有害垃圾", "可回收", "通用", "通用（带收紧带）"};
    private String[] strBagType = {"厨余垃圾", "其他垃圾"};
    private int intBagType = 1;                 //当前选择垃圾袋类型
    public static int BAG_LENGTH_TYPE_1 = 12;  //垃圾袋编码规则12位-12位”
    public static int BAG_LENGTH_TYPE_2 = 14;  //垃圾袋编码规则14位-14位”
    public static int BAG_LENGTH_TYPE_3 = 11;  //垃圾袋编码规则11位-11位”
    private RelativeLayout relative_choose_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_input);
        initView();
        init();
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("手动输入");
        edtStartCode = (EditText) findViewById(R.id.edtStartCode);
        edtEndCode = (EditText) findViewById(R.id.edtEndCode);
        edtBagType = (EditText) findViewById(R.id.edtBagType);
        relative_choose_type = (RelativeLayout) findViewById(R.id.relative_choose_type);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        switch (type){
            case TYPE_WAREHOUSE:
                relative_choose_type.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void init() {
        spinnerBagType = (Spinner) findViewById(R.id.spinnerBagType);

        spinnerBagType.setSelection(0);
        spinnerBagType.setEnabled(false);
        //将可选内容与ArrayAdapter连接起来
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, R.array.bag_type, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strBagType);
        //自定义下拉列表的风格
        adapter.setDropDownViewResource(R.layout.item_spinner);
        spinnerBagType.setAdapter(adapter);

        spinnerBagType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intBagType = arrBagType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtStartCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < BAG_LENGTH_TYPE_3) {
                    spinnerBagType.setSelection(0);
                    spinnerBagType.setEnabled(false);
                    InputFilter[] inputFilter = {new InputFilter.LengthFilter(BAG_LENGTH_TYPE_2)};
                    edtEndCode.setFilters(inputFilter);
                } else {
                    spinnerBagType.setEnabled(true);
                }
            }
        });
    }

    private void submit() {
        // validate
        String edtStartCodeString = edtStartCode.getText().toString().trim();
        if (TextUtils.isEmpty(edtStartCodeString)) {
            showCustomToast("请输入起始编号");
            return;
        }
        String edtEndCodeString = edtEndCode.getText().toString().trim(); //允许截至编号为空
        if (edtStartCode.getText().toString().trim().length()==11){
            if (TextUtils.isEmpty(edtEndCodeString)) {
            showCustomToast("请输入截至编号");
            return;
            }
        }

//        if (TextUtils.isEmpty(edtEndCodeString)) {
//            showCustomToast("请输入截至编号");
//            return;
//        }

        if (!TextUtils.isEmpty(edtEndCodeString)) {
            if (edtEndCodeString.length() != edtStartCodeString.length()) {
                showCustomToast("请检查输入编号，起始编号和截至编号长度须一致");
                return;
            }

            if ((Long.decode(edtEndCodeString) - Long.decode(edtStartCodeString)) <= 0) {
                showCustomToast("截至编号须大于起始编号");
                return;
            }
        }
        String strResult = TextUtils.isEmpty(edtEndCodeString) ? edtStartCodeString : (edtStartCodeString + "-" + edtEndCodeString);

        // TODO validate success, do something
        switch (type) {
            case TYPE_GRANT_BAG: {
                Intent intent = getIntent();
                intent.setClass(mContext, GrantBagActivity.class);
                intent.putExtra("strResult", strResult.trim());
                intent.putExtra("intBagType", intBagType);
                startActivity(intent);
                finish();
                break;
            }
            case TYPE_WAREHOUSE: {
                Intent intent = getIntent();
                intent.setClass(mContext, MaterialWarehouseActivity.class);
                intent.putExtra("strResult", strResult.trim());
                intent.putExtra("intBagType", intBagType);
                startActivity(intent);
                finish();
                break;
            }
            case TYPE_PUTAWAY: {
                Intent intent = getIntent();
                intent.setClass(mContext, GrantBagActivity.class);
                intent.putExtra("strResult", strResult.trim());
                intent.putExtra("intBagType", intBagType);
                startActivity(intent);
                finish();
                break;
            }
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.btnConfirm: {
                submit();
                break;
            }
        }
    }
}
