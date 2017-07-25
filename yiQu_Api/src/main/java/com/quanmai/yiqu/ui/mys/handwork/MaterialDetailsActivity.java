package com.quanmai.yiqu.ui.mys.handwork;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.HandworkApi;
import com.quanmai.yiqu.api.vo.BagRepertoryInfo;
import com.quanmai.yiqu.api.vo.GrantBagDetailsInfo;
import com.quanmai.yiqu.api.vo.MaterialOrUserDetailsInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.mys.handwork.adapter.MaterialDetailsNotGiveAdapter;
import com.quanmai.yiqu.ui.mys.handwork.adapter.MaterialDetailsPutAwayAdapter;
import com.quanmai.yiqu.ui.mys.handwork.adapter.MaterialDetailsPutWareHouseAdapter;
import com.quanmai.yiqu.ui.views.CustomListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 物料详情页面
 */
public class MaterialDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private CustomListView listView;

    private String commcode = "";
    private Map<String, Object> mMap;
    private MaterialDetailsPutWareHouseAdapter mAdapter1;
    private MaterialDetailsPutAwayAdapter mAdapter2;
    private MaterialDetailsNotGiveAdapter mAdapter3;
    private TextView tvCommname;
    private TextView tvAddress;
    private TextView tvFrequency;
    private TextView tvTotalOut;
    private TextView tvTotalRemain;
    private LinearLayout llRepertory, Linear_putbags, Linear_giveout, Linear_notgiveout;
    String statu = "1";//请求状态参数：1-入库明细，2-已发明细，3-未发明细
    CommonList<MaterialOrUserDetailsInfo.UsercompareListBean> Uinfos;
    CommonList<MaterialOrUserDetailsInfo.InventoryListBean> Iinfos;
    int pages = 1;
    ImageView image_material01, image_material02, image_material03;
    private TextView textViewDate;
    private ImageView imageViewLastMonth;
    private ImageView imageViewNextMonth;

    int year;
    int month;
    private String datetime;
    private Calendar calendar;
    private int day;
    private String systimestr;
    private PullToRefreshScrollView pullScrollviewMaterial;
    private TextView tvputwarehouse;
    private TextView tvputaway;
    private TextView tvnotgive;
    View headerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_details);
        getSystemTime();
        initView();
        init();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("物料详情");
        pullScrollviewMaterial = (PullToRefreshScrollView) findViewById(R.id.pull_scrollview_material);
        listView = (CustomListView) findViewById(R.id.listView);
        getHeader(statu);
        mAdapter1 = new MaterialDetailsPutWareHouseAdapter(mContext);
        mAdapter2 = new MaterialDetailsPutAwayAdapter(mContext);
        mAdapter3 = new MaterialDetailsNotGiveAdapter(mContext);

        llRepertory = (LinearLayout) findViewById(R.id.llRepertory);
        Linear_putbags = (LinearLayout) findViewById(R.id.Linear_putbags);
        Linear_giveout = (LinearLayout) findViewById(R.id.Linear_giveout);
        Linear_notgiveout = (LinearLayout) findViewById(R.id.Linear_notgiveout);
        tvCommname = (TextView) findViewById(R.id.tvCommname);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvTotalOut = (TextView) findViewById(R.id.tvTotalOut);
        tvFrequency = (TextView) findViewById(R.id.tvFrequency);
        tvTotalRemain = (TextView) findViewById(R.id.tvTotalRemain);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewDate.setText(systimestr.substring(0,4)+"."+systimestr.substring(5,7));

        image_material01 = (ImageView) findViewById(R.id.image_material01);
        image_material02 = (ImageView) findViewById(R.id.image_material02);
        image_material03 = (ImageView) findViewById(R.id.image_material03);
        imageViewLastMonth = (ImageView) findViewById(R.id.imageViewLastMonth);
        imageViewNextMonth = (ImageView) findViewById(R.id.imageViewNextMonth);

        tvputwarehouse = (TextView) findViewById(R.id.tvputwarehouse);
        tvputaway = (TextView) findViewById(R.id.tvputaway);
        tvnotgive = (TextView) findViewById(R.id.tvnotgive);

        Linear_putbags.setOnClickListener(this);
        Linear_giveout.setOnClickListener(this);
        Linear_notgiveout.setOnClickListener(this);
        imageViewLastMonth.setOnClickListener(this);
        imageViewNextMonth.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    private void init() {
        pullScrollviewMaterial.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        if (getIntent().hasExtra("commname")) {
            tvCommname.setText(getIntent().getStringExtra("commname"));
            mAdapter3.setCommname(getIntent().getStringExtra("commname"));
        }
        if (getIntent().hasExtra("address")) {
            tvAddress.setText(getIntent().getStringExtra("address"));
        }
        if (getIntent().hasExtra("commcode")) {
            commcode = getIntent().getStringExtra("commcode");
        }
        if (getIntent().hasExtra("garList")) {
            refreshBagRepertory((List<BagRepertoryInfo>) getIntent().getSerializableExtra("garList"));
        }
        /*if (getIntent().hasExtra("repertoryDetails")) {
            SerializableMap serializableMap = (SerializableMap) getIntent().getSerializableExtra("repertoryDetails");
            mMap = serializableMap.getMap();
            if (mMap == null) {
                getRepertoryDetails(commcode);
            } else {
                refreshData(mMap);
            }
        } else {
            getRepertoryDetails(commcode);
        }*/
        listView.setAdapter(mAdapter1);
        getMaterialDetails(commcode, statu, datetime, 0);
        pullScrollviewMaterial.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pages = 1;
                getMaterialDetails(commcode, statu, datetime, 0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getMaterialDetails(commcode, statu, datetime, pages);
            }
        });
    }

    public void getHeader(String statu){
        if (statu.equals("1")){
            headerView = LayoutInflater.from(mContext).inflate(R.layout.item_material_putwarehouse, null);
        }else if (statu.equals("2")){
            headerView = LayoutInflater.from(mContext).inflate(R.layout.item_material_putaway, null);
        }else if (statu.equals("3")){
            headerView = LayoutInflater.from(mContext).inflate(R.layout.item_material_notgive, null);
        }
        listView.addHeaderView(headerView);
    }

    private void refreshData(Map<String, Object> map) {
        tvFrequency.setText((String) map.get("issuetype"));
        tvTotalOut.setText((String) map.get("totalout"));
        tvTotalRemain.setText((String) map.get("totalremain"));
        refreshBagRepertory((List<BagRepertoryInfo>) map.get("garList"));
        if (map.containsKey("inventList")) {
            List<GrantBagDetailsInfo> list = (List<GrantBagDetailsInfo>) map.get("inventList");
            if (list == null || list.size() != 0) {
                //mAdapter.addAll(list);
            }
        }
    }

    //刷新垃圾袋库存信息
    private void refreshBagRepertory(List<BagRepertoryInfo> list) {
        if (list == null || list.size() == 0) {
            return;
        }
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
            llRepertory.addView(itemView);
        }
    }

    /**
     * 获取系统时间，年月日2017-04-03  2017-4-3
     */
    public void getSystemTime(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        systimestr = formatter.format(curDate);
    }

    public void MonthChange(boolean next){
        if(next==false){
            if (month==1){
                year=year-1;
                month=12;
            }else{
                month=month-1;
            }
        }else{
            if (month==12){
                year=year+1;
                month=1;
            }else{
                month=month+1;
            }
        }
        if (month<10){
            textViewDate.setText(year + ".0" + month);
            datetime = year+"-0"+month;
        }else{
            textViewDate.setText(year + "." + month);
            datetime=year+"-"+month;
        }
    }

    //获取库存详情
    private void getRepertoryDetails(String commcode) {
        showLoadingDialog();
        HandworkApi.get().getRepertoryDetails(mContext, commcode, new ApiConfig.ApiRequestListener<Map<String, Object>>() {
            @Override
            public void onSuccess(String msg, Map<String, Object> data) {
                dismissLoadingDialog();
                if (data == null || data.size() == 0) {
                    return;
                }
                refreshData(data);
                pullScrollviewMaterial.onRefreshComplete();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                pullScrollviewMaterial.onRefreshComplete();
                Log.i("获取库存详情", msg);
            }
        });
    }

    //获取库存详情
    private void getMaterialDetails(String commcode, final String status, String date, final int page) {
        showLoadingDialog();
        HandworkApi.get().getMaterialDetails(mContext, status, commcode, date, page + "", new ApiConfig.ApiRequestListener<JSONObject>() {
            @Override
            public void onSuccess(String msg, JSONObject jsons) {
                dismissLoadingDialog();
                if (jsons.equals("") || jsons.length() == 0) {
                    return;
                }
                try {
                    if (jsons.has("usercompareList")) {
                        CommonList<MaterialOrUserDetailsInfo.UsercompareListBean> infoList = new CommonList();
                        if (jsons.has("totalPage")) {
                            infoList.max_page = jsons.optInt("totalPage");
                        }
                        if (status.equals("3") && page == 0) {
                            mAdapter3.clear();
                        }
                        if (infoList.max_page <= page + 1) {
                            pullScrollviewMaterial.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        } else {
                            pullScrollviewMaterial.setMode(PullToRefreshBase.Mode.BOTH);
                        }
                        for (int i = 0; i < jsons.optJSONArray("usercompareList").length(); i++) {
                            MaterialOrUserDetailsInfo.UsercompareListBean info = new Gson().fromJson(jsons.optJSONArray("usercompareList").get(i).toString(), MaterialOrUserDetailsInfo.UsercompareListBean.class);
                            infoList.add(info);
                        }
                        if (infoList == null || infoList.size() != 0) {
                            mAdapter3.addAll(infoList);
                        }
                    } else if (jsons.has("inventoryList")) {
                        CommonList<MaterialOrUserDetailsInfo.InventoryListBean> infoList = new CommonList();
                        if (jsons.has("totalPage")) {
                            infoList.max_page = jsons.optInt("totalPage");
                        }
                        if (status.equals("1") && page == 0) {
                            mAdapter1.clear();
                        } else if (status.equals("2") && page == 0) {
                            mAdapter2.clear();
                        }
                        if (infoList.max_page <= page + 1) {
                            pullScrollviewMaterial.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        } else {
                            pullScrollviewMaterial.setMode(PullToRefreshBase.Mode.BOTH);
                        }
                        for (int i = 0; i < jsons.optJSONArray("inventoryList").length(); i++) {
                            MaterialOrUserDetailsInfo.InventoryListBean info = new Gson().fromJson(jsons.optJSONArray("inventoryList").get(i).toString(), MaterialOrUserDetailsInfo.InventoryListBean.class);
                            infoList.add(info);
                        }
                        if (infoList == null || infoList.size() != 0) {
                            if (status.equals("1")) {
                                mAdapter1.addAll(infoList);
                            } else if (status.equals("2")) {
                                mAdapter2.addAll(infoList);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pages++;
                pullScrollviewMaterial.onRefreshComplete();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                pullScrollviewMaterial.onRefreshComplete();
                Log.i("获取库存详情", msg);
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
            case R.id.Linear_putbags: {
                pages = 1;
                statu = "1";
                listView.removeHeaderView(headerView);
                getHeader(statu);
                image_material01.setImageResource(R.drawable.icon_putwarehouse);
                image_material02.setImageResource(R.drawable.icon_putaway_no);
                image_material03.setImageResource(R.drawable.icon_notgive_no);
                tvputwarehouse.setTextColor(Color.parseColor("#6cbf9c"));
                tvputaway.setTextColor(Color.parseColor("#666666"));
                tvnotgive.setTextColor(Color.parseColor("#666666"));
                listView.setAdapter(mAdapter1);
                mAdapter1.clear();
                getMaterialDetails(commcode, statu, datetime, 0);
                break;
            }
            case R.id.Linear_giveout: {
                pages = 1;
                statu = "2";
                listView.removeHeaderView(headerView);
                getHeader(statu);
                image_material01.setImageResource(R.drawable.icon_putwarehouse_no);
                image_material02.setImageResource(R.drawable.icon_putaway);
                image_material03.setImageResource(R.drawable.icon_notgive_no);
                tvputwarehouse.setTextColor(Color.parseColor("#666666"));
                tvputaway.setTextColor(Color.parseColor("#6cbf9c"));
                tvnotgive.setTextColor(Color.parseColor("#666666"));
                listView.setAdapter(mAdapter2);
                mAdapter2.clear();
                getMaterialDetails(commcode, statu, datetime, 0);
                break;
            }
            case R.id.Linear_notgiveout: {
                pages = 1;
                statu = "3";
                listView.removeHeaderView(headerView);
                getHeader(statu);
                image_material01.setImageResource(R.drawable.icon_putwarehouse_no);
                image_material02.setImageResource(R.drawable.icon_putaway_no);
                image_material03.setImageResource(R.drawable.icon_notgive);
                tvputwarehouse.setTextColor(Color.parseColor("#666666"));
                tvputaway.setTextColor(Color.parseColor("#666666"));
                tvnotgive.setTextColor(Color.parseColor("#6cbf9c"));
                listView.setAdapter(mAdapter3);
                mAdapter3.clear();
                getMaterialDetails(commcode, statu, datetime, 0);
                break;
            }
            case R.id.imageViewLastMonth:
                MonthChange(false);
                mAdapter1.clear();
                mAdapter2.clear();
                mAdapter3.clear();
                getMaterialDetails(commcode, statu, datetime, 0);
                break;
            case R.id.imageViewNextMonth:
                MonthChange(true);
                mAdapter1.clear();
                mAdapter2.clear();
                mAdapter3.clear();
                getMaterialDetails(commcode, statu, datetime, 0);
                break;
            default:
                break;
        }
    }
}
