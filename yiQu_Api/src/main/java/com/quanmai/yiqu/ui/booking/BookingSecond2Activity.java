package com.quanmai.yiqu.ui.booking;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.AddressApi;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.BookingApi;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.vo.AddressInfo;
import com.quanmai.yiqu.api.vo.AwardRecordInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.QiniuUtil;
import com.quanmai.yiqu.common.widget.CameraProtectActivity;
import com.quanmai.yiqu.common.widget.DateSelectionDialog;
import com.quanmai.yiqu.ui.address.AddressManageActivity;
import com.quanmai.yiqu.ui.booking.Adapter.BookingGoodsRecyclerAdapter;
import com.quanmai.yiqu.ui.booking.Adapter.BookingPhotoRecyclerAdapter;
import com.quanmai.yiqu.ui.grade.adapter.GridViewPersonalGradeAdapter;
import com.quanmai.yiqu.ui.views.CustomFlowLayout;
import com.quanmai.yiqu.ui.views.TagAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingSecond2Activity extends BaseActivity implements View.OnClickListener {

    //private CustomFlowLayout mColorFlowTagLayout;
    //private CustomFlowLayout mSizeFlowTagLayout;
    private CustomFlowLayout mMobileFlowTagLayout;
    //private TagAdapter<String> mSizeTagAdapter;
    //private TagAdapter<String> mColorTagAdapter;
    private TagAdapter<String> mMobileTagAdapter;
    StringBuilder sb;
    private TextView textView_order_name, textView_order_phone, textView_order_address, textView_order_date, textView_order_time, textView_order_mr;
    private RecyclerView recyclerview_order_photo, recyclerview_order_goods;
    private Button button_order_affirm;
    private RelativeLayout relative_order_date, relative_order_time;
    LinearLayout relative_order_userMessage;
    private DateSelectionDialog dateSelectionDialog;
    private final int TAKE_PHOTO = 201;  //选择照片
    private final int TAKE_PICTURE = 202;//拍照
    private final int GET_ADDRESS = 203;//拍照

    ArrayList<String> imglist;
    CommonList<AwardRecordInfo> goodslist;
    private BookingPhotoRecyclerAdapter mBookingPhotoRecyclerAdapter;
    private int year;
    private int month;
    private int day;
    private String week;
    private TextView tv_title;
    AddressInfo mAddressInfo = null;
    private BookingGoodsRecyclerAdapter mBookingGoodsRecyclerAdapter;
    LinearLayout linear_show_price;
    private String datenow;
    private String[] dateantime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_second2);
        getDates();
        init();
        initView();
        initMobileData();
        getAddressList(-1);
        getGiftList();
        mBookingPhotoRecyclerAdapter.addCloseListener(new BookingPhotoRecyclerAdapter.OnCloseClick() {
            @Override
            public void changView() {
            }

            @Override
            public void onClose(String path) {
            }

            @Override
            public void getPhotos() {
                Intent intent = new Intent(getApplication(), CameraProtectActivity.class);
                //imglist.clear();
                intent.putStringArrayListExtra("imgList", imglist);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });
    }

    private void init() {
        imglist = new ArrayList<String>();
        goodslist = new CommonList<>();
        imglist.add(null);
        sb = new StringBuilder();

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("预约");

        mMobileFlowTagLayout = (CustomFlowLayout) findViewById(R.id.mobile_flow_layout);
        textView_order_name = (TextView) findViewById(R.id.textView_order_name);
        textView_order_phone = (TextView) findViewById(R.id.textView_order_phone);
        textView_order_address = (TextView) findViewById(R.id.textView_order_address);
        textView_order_mr = (TextView) findViewById(R.id.textView_order_mr);

        textView_order_date = (TextView) findViewById(R.id.textView_order_date);
        textView_order_time = (TextView) findViewById(R.id.textView_order_time);

        recyclerview_order_photo = (RecyclerView) findViewById(R.id.recyclerview_order_photo);
        recyclerview_order_goods = (RecyclerView) findViewById(R.id.recyclerview_order_goods);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerview_order_photo.setLayoutManager(layoutManager1);
        recyclerview_order_goods.setLayoutManager(layoutManager2);
        //设置为垂直布局，这也是默认的
        layoutManager1.setOrientation(OrientationHelper.HORIZONTAL);
        layoutManager2.setOrientation(OrientationHelper.HORIZONTAL);
        //设置增加或删除条目的动画
        recyclerview_order_photo.setItemAnimator(new DefaultItemAnimator());
        recyclerview_order_goods.setItemAnimator(new DefaultItemAnimator());

        linear_show_price = (LinearLayout) findViewById(R.id.linear_show_price);
        button_order_affirm = (Button) findViewById(R.id.button_order_affirm);

        relative_order_userMessage = (LinearLayout) findViewById(R.id.relative_order_userMessage);
        relative_order_date = (RelativeLayout) findViewById(R.id.relative_order_date);
        relative_order_time = (RelativeLayout) findViewById(R.id.relative_order_time);

        linear_show_price.setOnClickListener(this);
        button_order_affirm.setOnClickListener(this);
        relative_order_userMessage.setOnClickListener(this);
        relative_order_date.setOnClickListener(this);
        relative_order_time.setOnClickListener(this);
    }

    private void initView() {
        addGridViewAdapter();
        mMobileTagAdapter = new TagAdapter<>(this);
        mMobileFlowTagLayout.setTagCheckedMode(CustomFlowLayout.FLOW_TAG_CHECKED_MULTI);
        mMobileFlowTagLayout.setAdapter(mMobileTagAdapter);
        mMobileFlowTagLayout.setOnTagSelectListener(new CustomFlowLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(CustomFlowLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    if (sb != null && sb.length() > 0) {
                        sb.delete(0, sb.length());
                    } else {
                        sb = new StringBuilder(); //多选标签选中的字符拼接
                    }
                    for (int i = 0; i < selectedList.size(); i++) {
                        sb.append(parent.getAdapter().getItem(selectedList.get(i)));
                        if (i < selectedList.size() - 1) sb.append(",");
                    }
//                    Snackbar.make(parent, sb.toString(), Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                }
            }
        });
        mBookingGoodsRecyclerAdapter = new BookingGoodsRecyclerAdapter(this, goodslist);
        recyclerview_order_goods.addItemDecoration(mBookingGoodsRecyclerAdapter.getSpaceItemDecoration(30));
        recyclerview_order_goods.setAdapter(mBookingGoodsRecyclerAdapter);
    }

    /**
     * 日期选择弹框
     */
    public void chooseBookingDate() {
        dateSelectionDialog = new DateSelectionDialog(this, year, month, day, "date",
                new DateSelectionDialog.OnStringDateTimeSetListener() {
                    @Override
                    public void onDateTimeSet(String strdate) {
                        textView_order_date.setText(strdate);
                    }
                });
        dateSelectionDialog.show();
    }

    /**
     * 时间段选择弹框
     */
    public void chooseBookingtime() {
        dateSelectionDialog = new DateSelectionDialog(mContext, "time",
                new DateSelectionDialog.OnStringDateTimeSetListener() {
                    @Override
                    public void onDateTimeSet(String strdate) {
                        textView_order_time.setText(strdate);
                    }
                });
        dateSelectionDialog.show();
    }

    /**
     * 流式布局标签添加数据
     */
    private void initMobileData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("有害垃圾");
        dataSource.add("瓶子");
        dataSource.add("纸类");
        dataSource.add("金属类");
        dataSource.add("家用电器");
        dataSource.add("其他");
        mMobileTagAdapter.onlyAddAll(dataSource);
    }

    /**
     * 添加适配器
     */
    public void addGridViewAdapter() {
        mBookingPhotoRecyclerAdapter = new BookingPhotoRecyclerAdapter(this, imglist);
        recyclerview_order_photo.addItemDecoration(mBookingPhotoRecyclerAdapter.getSpaceItemDecoration(30));
        recyclerview_order_photo.setAdapter(mBookingPhotoRecyclerAdapter);
    }

    /**
     * 刷新适配器
     */
    public void notifyGridViewAdapter() {
        if (mBookingPhotoRecyclerAdapter != null) {
            mBookingPhotoRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public void getDates() {
        datenow = DateUtil.formatToString(new Date(System.currentTimeMillis()), "yyyy-MM-dd");
        dateantime = datenow.split("-");
        year = Integer.parseInt(dateantime[0].trim());
        month = Integer.parseInt(dateantime[1].trim());
        day = Integer.parseInt(dateantime[2].trim());
        week = DateSelectionDialog.getWeek(year + "-" + month + "-" + day, "yyyy-MM-dd");
    }

    public String getDataString() {
        String data;
        data = year + "-" + textView_order_date.getText().toString().trim().substring(0, 2) + "-"
                + textView_order_date.getText().toString().trim().substring(3, 5);
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case TAKE_PICTURE: {
                if (resultCode == RESULT_OK) {
                    if (imglist.size() == 0) {
                        imglist.add(intent.getStringExtra(CameraProtectActivity.IMAGE_PATH));
                        imglist.add(null);
                    } else {
                        imglist.set(imglist.size() - 1, intent.getStringExtra(CameraProtectActivity.IMAGE_PATH));
                        if (imglist.size() < 3) {
                            imglist.add(null);
                        }
                    }
                    notifyGridViewAdapter();
                }
                break;
            }
            case GET_ADDRESS:
                if (intent != null) {
                    if (intent.hasExtra("checkaddress") && intent.getSerializableExtra("checkaddress") != null) {
                        textView_order_name.setText(((AddressInfo) intent.getSerializableExtra("checkaddress")).name);
                        textView_order_phone.setText(((AddressInfo) intent.getSerializableExtra("checkaddress")).phone);
                        textView_order_address.setText(((AddressInfo) intent.getSerializableExtra("checkaddress")).address);
                        if (((AddressInfo) intent.getSerializableExtra("checkaddress")).sortfield == 1) {
                            textView_order_mr.setVisibility(View.VISIBLE);
                        } else {
                            textView_order_mr.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取地址列表
     *
     * @param page
     */
    public void getAddressList(int page) {
        showLoadingDialog("数据加载中...");
        AddressApi.get().AddressList(mContext, page, new ApiConfig.ApiRequestListener<CommonList<AddressInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<AddressInfo> data) {
                dismissLoadingDialog();
                if (data != null && data.size() != 0) {
                    mAddressInfo = data.get(0);
                    textView_order_name.setText(mAddressInfo.name);
                    textView_order_phone.setText(mAddressInfo.phone);
                    textView_order_address.setText(mAddressInfo.address);
                    if (mAddressInfo.sortfield == 1) {
                        textView_order_mr.setVisibility(View.VISIBLE);
                    } else {
                        textView_order_mr.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //获取礼物清单
    private void getGiftList() {
        showLoadingDialog();
        IntegralApi.get().getGiftList(mContext, 0, new ApiConfig.ApiRequestListener<CommonList<AwardRecordInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<AwardRecordInfo> data) {
                dismissLoadingDialog();
                if (data.size() == 0) {
                    return;
                }
                mBookingGoodsRecyclerAdapter.add(data);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //确认预约
    private void SimpleBooking(final String publisher, final String rangeDate,
                               final String rangeTime, final String address, final String mobile, final String description) {
        if (imglist.contains(null)) {
            imglist.remove(null);
        }
        if (imglist == null || imglist.size() <= 0) {
            showCustomToast("请拍摄上传废品照片！");
            return;
        }
        showLoadingDialog();
        new QiniuUtil(mContext, imglist, new QiniuUtil.OnQiniuUploadListener() {
            @Override
            public void success(String names) {

                BookingApi.get().SimpleBooking(mContext, publisher, rangeDate, rangeTime, address, mobile, description, names, new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String orderId) {
                        dismissLoadingDialog();
                        Intent intent = new Intent(BookingSecond2Activity.this, BookingStatusActivity.class);
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

            @Override
            public void failure() {
                dismissLoadingDialog();
                showShortToast("图片上传失败");
            }
        }).upload();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_order_userMessage://选择修改地址
                Intent intent = new Intent(this, AddressManageActivity.class);
                startActivityForResult(intent, GET_ADDRESS);
                break;
            case R.id.relative_order_date://选择回收日期
                chooseBookingDate();
                break;
            case R.id.relative_order_time://选择回收时间段
                chooseBookingtime();
                break;
            case R.id.linear_show_price://查看回收单价
                startActivity(new Intent(mContext, ShowParbagePriceActivity.class));
                break;
            case R.id.button_order_affirm://确认预约
                if (textView_order_name.getText().equals("") || textView_order_name.getText().equals(null)) {
                    showCustomToast("发布人不能为空！");
                    return;
                }
                if (textView_order_date.getText().equals("选择日期") || textView_order_date.getText().equals("") ||
                        textView_order_date.getText().equals(null)) {
                    showCustomToast("请选择回收日期！");
                    return;
                }
                if (textView_order_time.getText().equals("选择时段") || textView_order_time.getText().equals("") ||
                        textView_order_time.getText().equals(null)) {
                    showCustomToast("请选择回收时间段！");
                    return;
                }
                if (textView_order_address.getText().equals("") || textView_order_address.getText().equals(null)) {
                    showCustomToast("请选择地址！");
                    return;
                }
                if (sb.equals("") || sb.equals(null) || sb.equals("null")) {
                    showCustomToast("请选择废品标签！");
                    return;
                }
                SimpleBooking(textView_order_name.getText().toString(), getDataString().toString(), textView_order_time.getText().toString(),
                        textView_order_address.getText().toString(), textView_order_phone.getText().toString(), sb.toString());
                break;
        }
    }
}
