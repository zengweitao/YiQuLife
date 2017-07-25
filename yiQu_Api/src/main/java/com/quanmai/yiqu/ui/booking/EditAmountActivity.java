package com.quanmai.yiqu.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.BookingApi;
import com.quanmai.yiqu.api.vo.RecycleGarbageListInfo;
import com.quanmai.yiqu.api.vo.BookingDetailInfo;
import com.quanmai.yiqu.api.vo.GarbageDetailsInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesMapInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.adapter.BookingCountLeftAdapter;
import com.quanmai.yiqu.ui.adapter.StatisticsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditAmountActivity extends BaseActivity {

    TextView tv_title;
    ListView listView,listViewLeft;
    TextView textViewPredictIntegral;
    TextView textViewConfirm;

    BookingCountLeftAdapter mLeftAdapter;
    StatisticsAdapter mAdapter;
    List<String> garbageList;   //将要回收的垃圾
    Map<RecycleGarbagesInfo, Double> garbageMap;   //key 回收的垃圾 value 回收的数量
    int totalPoint;  //预计能获取的积分数
    BookingDetailInfo mInfo; //预约废品列表
    final int UPDATE_INFO = 201; //更新废品列表;
    String orderId;
    CommonList<RecycleGarbageListInfo> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_amount);

        totalPoint = Integer.parseInt(getIntent().getStringExtra("points"));
        mInfo = (BookingDetailInfo) getIntent().getSerializableExtra("info");
        orderId = getIntent().getStringExtra("orderId");
        init();

        searchRecycleGarbageList();


        listViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mLeftAdapter.setPosition(i);
                mAdapter.clear();
                for (int j=0;j<mData.size();j++){
                    if (mData.get(j).getTypeName().equals(mLeftAdapter.getItem(i))){
                       // mAdapter.addRecord(mData.get(j).getGarbageList(),);
                    }
                }
            }
        });
    }

    private void initData() {
        CommonList<GarbageDetailsInfo> list = mInfo.garbageDetailsInfos;
        for (int i = 0; i < list.size(); i++) {
            garbageList.add(list.get(i).garbage);
            //garbageMap.put(list.get(i).garbage, list.get(i));
        }
        for (int i=0;i<mData.size();i++){
            CommonList<RecycleGarbagesInfo> recycleGarbagesList = mData.get(i).getGarbageList();
            for (int j=0;j<recycleGarbagesList.size();j++){
                RecycleGarbagesInfo recycleGarbagesInfo = recycleGarbagesList.get(j);
                if (garbageMap.containsKey(recycleGarbagesInfo.garbage)){
                    recycleGarbagesInfo.quantity = garbageMap.get(recycleGarbagesInfo.garbage)+"";
                }
            }
        }
        //确认修改
        textViewConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.point = totalPoint + "";
                if (garbageMap.size()>0){
                    if (garbageMap.size()>5){
                        showCustomToast("回收种类不能超过5种");
                    }else {
                        modifyBooking(orderId, mInfo.point, Utils.toJson(garbageMap, mData));
                    }
                }else {
                    Utils.showCustomToast(mContext,"物品不能为空");
                }

            }
        });
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("修改数量");
        listView = (ListView) findViewById(R.id.listView);
        listViewLeft = (ListView) findViewById(R.id.listViewLeft);
        textViewPredictIntegral = (TextView) findViewById(R.id.textViewPredictIntegral);
        textViewConfirm = (TextView) findViewById(R.id.textViewConfirm);
        mLeftAdapter = new BookingCountLeftAdapter(this);

        if (totalPoint>1000){
            textViewPredictIntegral.setText("预计可获得：" + 1000 + "益币");
        }else {
            textViewPredictIntegral.setText("预计可获得：" + totalPoint + "益币");
        }

        garbageList = new ArrayList<>();
        garbageMap = new HashMap<>();
//        mAdapter = new StatisticsAdapter(mContext, new StatisticsAdapter.CountListener() {
//            @Override
//            public void countChange(String addOrminus, String type, int count) {
//                if (garbageList.contains(type)) {
//                    if (addOrminus.equals("+")) {
//                        garbageMap.put(type, garbageMap.get(type) + 1);
//                    } else if (addOrminus.equals("-")) {
//                        if (garbageMap.get(type) - 1 > 0) {
//                            garbageMap.put(type, garbageMap.get(type) - 1);
//                        } else {
//                            garbageList.remove(type);
//                            garbageMap.remove(type);
//                        }
//                    }else if (addOrminus.equals("add")){
//                        garbageMap.put(type,garbageMap.get(type)+1);
//                    }else if (addOrminus.equals("min")){
//                        if (garbageMap.get(type)-1>0){
//                            garbageMap.put(type,garbageMap.get(type)-1);
//                        }else {
//                            garbageList.remove(type);
//                            garbageMap.remove(type);
//                        }
//                    }
//                } else {
//                    garbageList.add(type);
//                    garbageMap.put(type, 1);
//                }
//
//                if (addOrminus.equals("+")) {
//                    totalPoint = totalPoint + count;
//                } else if (addOrminus.equals("-")) {
//                    totalPoint = totalPoint - count;
//                }
//
//                if (addOrminus.equals("add")){
//                    totalPoint = totalPoint+count;
//                }else if (addOrminus.equals("min")){
//                    totalPoint = totalPoint-count;
//                }
//
//                if (totalPoint>1000){
//                    textViewPredictIntegral.setText("预计可获得："+1000+"益币");
//                }else if (totalPoint<=0){
//                    textViewPredictIntegral.setText("预计可获得："+0+"益币");
//                }else {
//                    textViewPredictIntegral.setText("预计可获得："+totalPoint+"益币");
//                }
//            }
//        });


        listViewLeft.setAdapter(mLeftAdapter);

        listView.setAdapter(mAdapter);
    }

    //查询可回收垃圾列表
    private void searchRecycleGarbageList() {
        showLoadingDialog();
        BookingApi.get().GetGarbageList(mContext, new ApiConfig.ApiRequestListener<CommonList<RecycleGarbageListInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<RecycleGarbageListInfo> data) {
                dismissLoadingDialog();
                mLeftAdapter.add(data);
                mData = data;

                initData();
                mAdapter.addData(data.get(0).getGarbageList());
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
            }
        });
    }

    private void modifyBooking(String orderId, String piont, final String recycleDetails) {
        showLoadingDialog();
        BookingApi.get().ModifyBooking(mContext, orderId, piont, recycleDetails, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast("修改成功");
                CommonList<GarbageDetailsInfo> commonList = new CommonList<GarbageDetailsInfo>();
                for (Map.Entry<RecycleGarbagesInfo, Double> entry : garbageMap.entrySet()) {
                    GarbageDetailsInfo tempInfo = new GarbageDetailsInfo(entry.getKey().getGarbage(), entry.getValue() + "");
                    commonList.add(tempInfo);
                }
                mInfo.garbageDetailsInfos = commonList;
//                mInfo.point = totalPoint + "";
                Intent intent = new Intent();
                intent.putExtra("info", mInfo);
                intent.putExtra("point", totalPoint);
                intent.putExtra("recycleDetails", recycleDetails);
                setResult(UPDATE_INFO, intent);

                finish();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

}
