package com.quanmai.yiqu.ui.booking;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.BookingApi;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.vo.AppointedUserInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbageListInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesMapInfo;
import com.quanmai.yiqu.api.vo.RecycleOrderInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.ShoppingCar.BookingShopCartDialog;
import com.quanmai.yiqu.common.ShoppingCar.imp.ShopCartImp;
import com.quanmai.yiqu.common.ShoppingCar.model.ShopCart;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.adapter.BookingCountLeftAdapter;
import com.quanmai.yiqu.ui.adapter.StatisticsAdapter;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;
import com.quanmai.yiqu.ui.ycoin.RecycleLocalSuccessActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookingSecondActivity extends BaseActivity implements View.OnClickListener,
        ShopCartImp, BookingShopCartDialog.ShopCartDialogImp, BookingShopCartDialog.ShopCartDialogOnClick {
    private Boolean isRecycleLocal = false; //是否现场回收
    private String houseCode = "";  //扫描——用户号
    private String account = "";    //扫描——手动输入手机号

    ImageView iv_back;
    TextView tv_title;
    TextView textViewName;      //用户名
    TextView textViewPhone;     //用户手机号
    TextView textViewAddress;   //用户住址

    RelativeLayout relative_shopping_car;
    TextView textViewPredictIntegral; //益币数总计
    TextView textViewConfirmBooking; //点击确认回收

    ListView listViewLeft, listViewRight;

    SelectedTimeDialog.SelectedTimeDialogListener selectedTimeDialogListener;//监听器
    private final int FINISH_ADD_ADDRESS = 201;//添加地址
    StatisticsAdapter mAdapter;
    List<RecycleGarbagesInfo> garbageList;   //将要回收的垃圾
    Map<RecycleGarbagesInfo, Double> garbageMap;   //key 回收的垃圾 value 回收的数量
    String rangeDate; //预约日期
    String rangeTime; //预约时段
    CommonList<RecycleGarbageListInfo> mData;

    BookingCountLeftAdapter mLeftAdapter;
    private ImageView image_dor_shopingcar;
    private LinearLayout linear_bottm;
    private ShopCart shopCart;
    private RecycleOrderInfo mRecycleOrderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_second);
        //获取上个界面的传值
        if (getIntent().hasExtra("isRecycleLocal")) {
            isRecycleLocal = getIntent().getBooleanExtra("isRecycleLocal", false);
        }
        if (isRecycleLocal == false) {
            mRecycleOrderInfo = (RecycleOrderInfo) getIntent().getSerializableExtra("RecycleOrderInfo");
        }
        if (getIntent().hasExtra("houseCode")) {
            houseCode = getIntent().getStringExtra("houseCode");
        }
        if (getIntent().hasExtra("account")) {
            account = getIntent().getStringExtra("account");
        }
        init();
        initListLift();
        getGrabageList();
        addListener();
    }

    //view初始化
    private void init() {
        shopCart = new ShopCart();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewPhone = (TextView) findViewById(R.id.textViewPhone);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);

        textViewPredictIntegral = (TextView) findViewById(R.id.textViewPredictIntegral);
        textViewConfirmBooking = (TextView) findViewById(R.id.textViewConfirmBooking);
        textViewConfirmBooking.setText("确认回收");
        relative_shopping_car = (RelativeLayout) findViewById(R.id.relative_shopping_car);
        image_dor_shopingcar = (ImageView) findViewById(R.id.image_dor_shopingcar);
        linear_bottm = (LinearLayout) findViewById(R.id.linear_bottm);

        listViewLeft = (ListView) findViewById(R.id.listViewLeft);
        listViewRight = (ListView) findViewById(R.id.listViewRight);

        textViewConfirmBooking.setOnClickListener(this);
        relative_shopping_car.setOnClickListener(this);
        textViewPredictIntegral.setText("0益币");
        garbageList = new ArrayList<>();
        garbageMap = new HashMap<>();
        if (isRecycleLocal) {
            tv_title.setText("现场回收");
            getAppointedUserInfo(houseCode, account);
        } else {
            tv_title.setText("预约回收");
            initAddress();
        }
    }

    private void initListLift() {
        mLeftAdapter = new BookingCountLeftAdapter(this);
        listViewLeft.setAdapter(mLeftAdapter);
        addDataToView(0);
    }


    private void addListener() {
        listViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mLeftAdapter.setPosition(i);
                addDataToView(i);
                mAdapter.clear();
                for (int j = 0; j < mData.size(); j++) {
                    if (mData.get(j).getTypeName().equals(mLeftAdapter.getItem(i))) {
                        Log.e("--map集合", "== " + garbageMap.size());
                        mAdapter.addRecord(mData.get(j).getGarbageList(), shopCart);
                    }
                }
            }
        });
    }

    /**
     * 向左右两边的listview填充数据
     */
    public void addDataToView(final int i) {
        mAdapter = new StatisticsAdapter(mContext, mLeftAdapter, new StatisticsAdapter.CountListener() {
            @Override
            public void countChange(String addOrminus, RecycleGarbagesInfo recycleGarbagesInfo, int count, String unit, double goodsCount, int postion) {
                if (addOrminus.equals("add")){
                    garbageList.add(recycleGarbagesInfo);
                    garbageMap.put(recycleGarbagesInfo, goodsCount);
                }
                if (addOrminus.equals("min")){
                    garbageList.remove(recycleGarbagesInfo);
                    garbageMap.remove(recycleGarbagesInfo);
                }
                if (addOrminus.equals("writ")){
                    if (garbageMap.containsKey(recycleGarbagesInfo)) {
                            if (goodsCount > 0) {
                                garbageMap.put(recycleGarbagesInfo, goodsCount);
                            } else {
                                garbageList.remove(recycleGarbagesInfo);
                                garbageMap.remove(recycleGarbagesInfo);
                            }
                    } else {
                        garbageList.add(recycleGarbagesInfo);
                        garbageMap.put(recycleGarbagesInfo, goodsCount);
                    }
                }
                shopCart.setMap(garbageMap, garbageList);
                showTotalPrice();
//                Log.e("--有害长度","== "+shopCart.getShoppingTotalPrice());
                if (shopCart.getShoppingTotalPrice()<=0){
                    garbageMap.clear();
                    garbageList.clear();
                    shopCart.clear();
                }
                /*if (garbageMap.size() > 0) {
                    image_dor_shopingcar.setVisibility(View.VISIBLE);
                } else {
                    image_dor_shopingcar.setVisibility(View.INVISIBLE);
                }*/
                Log.e("--garbageMap数量", "== " + garbageMap.size() + "  " + garbageList.size());

                /*totalPoint = 0;
                if (addOrminus.equals("add")) {
                    for (int i = 0; i < garbageMap.size(); i++) {
                        totalPoint = totalPoint + garbageMap.get(garbageList.get(i)) * Integer.parseInt(garbageList.get(i).getPoint());
                    }
                    Log.e("--计算总的益币", "== " + totalPoint);
                }*/
                /*if (totalPoint > 1000) {
                    textViewPredictIntegral.setText(1000 + "益币");
                } else if (totalPoint <= 0) {
                    textViewPredictIntegral.setText(0 + "益币");
                } else {
                    textViewPredictIntegral.setText(totalPoint + "益币");
                }*/

                /*for (int i = 0; i < mData.size(); i++) {
                    for (int j = 0; j < mData.get(i).getGarbageList().size(); j++) {
                        if (garbageMap.containsKey(mData.get(i).getGarbageList().get(j).garbage)) {
                            mData.get(i).getGarbageList().get(j).setQuantity(garbageMap.get(mData.get(i).getGarbageList().get(j).garbage) + "");
                        }
                    }
                }*/
            }
        });
        listViewRight.setAdapter(mAdapter);

    }

    /**
     * 当预约跳转时添加用户信息
     */
    private void initAddress() {
        if (isRecycleLocal == false) {
            textViewName.setText(mRecycleOrderInfo.publisher + "");
            textViewPhone.setText(mRecycleOrderInfo.mobile);
            textViewAddress.setText(mRecycleOrderInfo.address);
        }
    }

    //获取可回收垃圾列表
    private void getGrabageList() {
        showLoadingDialog();
        BookingApi.get().GetGarbageList(mContext, new ApiConfig.ApiRequestListener<CommonList<RecycleGarbageListInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<RecycleGarbageListInfo> data) {
                dismissLoadingDialog();
                mLeftAdapter.add(data);
                mAdapter.addData(data.get(0).getGarbageList());
                mData = data;
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //确认预约
    private void ConfirmBooking(String point,
                                String publisher, String rangeDate,
                                String rangeTime, String address, String mobile, String recycleDetails) {
        showLoadingDialog();
        BookingApi.get().ConfirmBooking(mContext, point, publisher, rangeDate, rangeTime, address, mobile, recycleDetails, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String orderId) {
                dismissLoadingDialog();
                Intent intent = new Intent(BookingSecondActivity.this, BookingStatusActivity.class);
                intent.putExtra("id", orderId);
                startActivity(intent);
                BookingActivity.instance.finish();
                BookingActivity.instance = null;
                finish();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    private void showCart(View view) {
        if (shopCart != null && shopCart.getShoppingAccount() > 0) {
            BookingShopCartDialog dialog = new BookingShopCartDialog(this, shopCart, R.style.cartdialog);
            Window window = dialog.getWindow();
            dialog.setShopCartDialogImp(this);
            dialog.setShopCartDialogOnClick(this);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            params.dimAmount = 0.5f;
            window.setAttributes(params);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FINISH_ADD_ADDRESS) {
                initAddress();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //打开购物车
            case R.id.relative_shopping_car: {
                showCart(v);
                //getShoppinpCarDialog();
                break;
            }
            //确认预约  textViewConfirmBooking
            case R.id.textViewConfirmBooking: {
                if (shopCart.getShoppingTotalPrice() <= 0) {
                    showCustomToast("请选择要回收的垃圾");
                    return;
                }
                if (garbageMap != null && garbageMap.size() > 5) {
                    showCustomToast("回收种类不能超过5种");
                    return;
                }
                if (isRecycleLocal == true) {
                    startRecycleLocal(shopCart.getShoppingTotalPrice(), mSession.getBookingName(), textViewAddress.getText().toString(),
                            textViewPhone.getText().toString(), Utils.toJson(garbageMap, mData), "");
                } else {
                    startRecycleLocal(shopCart.getShoppingTotalPrice(), mRecycleOrderInfo.publisher, mRecycleOrderInfo.address,
                            mRecycleOrderInfo.mobile, Utils.toJson(garbageMap, mData), mRecycleOrderInfo.id);
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * 获取指定用户信息
     *
     * @param houseCode
     * @param tel
     */
    private void getAppointedUserInfo(String houseCode, final String tel) {
        showLoadingDialog();
        IntegralApi.get().appointedUserInfo(mContext, houseCode, tel, new ApiConfig.ApiRequestListener<AppointedUserInfo>() {
            @Override
            public void onSuccess(String msg, AppointedUserInfo data) {
                dismissLoadingDialog();
                if (data == null) {
                    return;
                }
                if (TextUtils.isEmpty(account) && !TextUtils.isEmpty(data.mtel)) {
                    account = data.getMtel();
                }
                textViewName.setText(data.getName());
                textViewAddress.setText(data.getCommname());
                textViewPhone.setText(account);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //现场回收垃圾
    private void startRecycleLocal(final int point, String publisher, String address, String mobile, String recycleDetails, String orderId) {
        if (TextUtils.isEmpty(mobile)) {
            showCustomToast("用户手机号码为空\n\t请通过“手动输入”输入手机号");
            return;
        }

        showLoadingDialog();
        IntegralApi.get().recycleLocal(mContext, point + "", publisher, address, mobile, recycleDetails, orderId, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(PersonalFragment.ACTION_NETWORKING_TO_REFRESH_DATA));

                Intent intent = new Intent(mContext, RecycleLocalSuccessActivity.class);
                intent.putExtra("TotalPoint", point);
                intent.putExtra("GarbageInfoList", (Serializable) getSelectarbageList());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    private List<RecycleGarbagesInfo> getSelectarbageList() {
        List<RecycleGarbagesInfo> infoList = new ArrayList<>();

        for (Map.Entry<RecycleGarbagesInfo, Double> entry : garbageMap.entrySet()) {
            for (int i = 0; i < mData.size(); i++) {
                CommonList<RecycleGarbagesInfo> list = mData.get(i).getGarbageList();
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).garbage.equals(entry.getKey())) {
                        infoList.add(list.get(j));
                    }
                }
            }
        }
        return infoList;
    }

   /* @Override
    public void setOnClick(View v) {
        switch (v.getId()){
            case R.id.linear_shoppingcar_delete:

                break;
        }
    }*/

    @Override
    public void add(View view, int postion) {

    }

    @Override
    public void remove(View view, int postion) {

    }

    public void refresh() {
        garbageMap.clear();
        garbageList.clear();
        shopCart.setMap(garbageMap,garbageList);
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setCount(0);
            for (int j = 0; j < mData.get(i).getGarbageList().size(); j++) {
                mData.get(i).getGarbageList().get(j).setCount(0);
            }
        }
    }

    private void showTotalPrice() {
        if (shopCart != null && shopCart.getShoppingTotalPrice() > 0) {
            image_dor_shopingcar.setVisibility(View.VISIBLE);
            textViewPredictIntegral.setText(shopCart.getShoppingTotalPrice() + "益币");

        } else {
            image_dor_shopingcar.setVisibility(View.INVISIBLE);
            textViewPredictIntegral.setText("0益币");
        }
    }

    @Override
    public void dialogDismiss() {
        mAdapter.addRecord(mData.get(0).getGarbageList(), shopCart);
        mLeftAdapter.setPosition(0);
        mLeftAdapter.addAll(mData);
        refresh();
        shopCart.clear();
        showTotalPrice();
        //mAdapter.notifyDataSetChanged();
        /*shopCart.clear();
        garbageMap.clear();
        refresh();
        textViewPredictIntegral.setText("0益币");
        image_dor_shopingcar.setVisibility(View.INVISIBLE);
        mLeftAdapter.setPosition(0);
        mAdapter.addRecord(mData.get(0).getGarbageList(), shopCart);
        mLeftAdapter.addAll(mData);*/
    }

    @Override
    public void dialogOnClick() {
        if (shopCart.getShoppingTotalPrice() <= 0) {
            showCustomToast("请选择要回收的垃圾");
            return;
        }
        if (garbageMap != null && garbageMap.size() > 5) {
            showCustomToast("回收种类不能超过5种");
            return;
        }
        Log.e("--上传json", "== " + Utils.toJson(garbageMap, mData));
        if (isRecycleLocal == true) {
            startRecycleLocal(shopCart.getShoppingTotalPrice(), mSession.getBookingName(), textViewAddress.getText().toString(),
                    textViewPhone.getText().toString(), Utils.toJson(garbageMap, mData), "");
        } else {
            startRecycleLocal(shopCart.getShoppingTotalPrice(), mRecycleOrderInfo.publisher, mRecycleOrderInfo.address,
                    mRecycleOrderInfo.mobile, Utils.toJson(garbageMap, mData), mRecycleOrderInfo.id);
        }
    }
}
