package com.quanmai.yiqu.ui.FEDRelevant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.HandworkApi;
import com.quanmai.yiqu.api.vo.MachineDataInfo;
import com.quanmai.yiqu.api.vo.MachineDataQueryInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.ui.FEDRelevant.adapter.FEDAddBagListViewAdapter;
import com.quanmai.yiqu.ui.mys.handwork.QRCodeInputActivity;
import com.quanmai.yiqu.ui.mys.handwork.ScanningActivity;
import com.quanmai.yiqu.ui.views.CustomListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FEDPutAwayActivity extends BaseActivity implements View.OnClickListener{

    private TextView textview_1_trough_num,textview_2_trough_num,textview_3_trough_num,textview_4_trough_num;
    private CustomListView listview_1_trough,listview_2_trough,listview_3_trough,listview_4_trough;
    private Button button_add_bag_01,button_add_bag_02,button_add_bag_03,button_add_bag_04;

    List<String> data1;
    List<String> data2;
    List<String> data3;
    List<String> data4;
    private FEDAddBagListViewAdapter mFEDAddBagListViewAdapter1,mFEDAddBagListViewAdapter2,mFEDAddBagListViewAdapter3,mFEDAddBagListViewAdapter4;
    private TextView tv_title;
    private static final int REQUEST_BAGCODE = 110;
    private int channelCode=0;
    private String machineCode;
    private PullToRefreshScrollView pull_scrollview_putaway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fedput_away);
        machineCode = getIntent().getStringExtra("usercode");
        initData();
        init();
        showView((MachineDataInfo)getIntent().getSerializableExtra("MachineDataInfo"));
    }

    private void initData() {
        data1=new ArrayList<String>(12);
        data2=new ArrayList<String>(12);
        data3=new ArrayList<String>(12);
        data4=new ArrayList<String>(12);
        for (int i=0;i<12;i++){
            data1.add("0");
            data2.add("0");
            data3.add("0");
            data4.add("0");
        }
        for (int i=0;i<data2.size();i++){
            Log.e("-- DDDDDDD","--"+data1.size());
        }
        mFEDAddBagListViewAdapter1 = new FEDAddBagListViewAdapter(mContext);
        mFEDAddBagListViewAdapter2 = new FEDAddBagListViewAdapter(mContext);
        mFEDAddBagListViewAdapter3 = new FEDAddBagListViewAdapter(mContext);
        mFEDAddBagListViewAdapter4 = new FEDAddBagListViewAdapter(mContext);
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("设备库存总数0");

        textview_1_trough_num = (TextView) findViewById(R.id.textview_1_trough_num);
        textview_2_trough_num = (TextView) findViewById(R.id.textview_2_trough_num);
        textview_3_trough_num = (TextView) findViewById(R.id.textview_3_trough_num);
        textview_4_trough_num = (TextView) findViewById(R.id.textview_4_trough_num);

        listview_1_trough = (CustomListView) findViewById(R.id.listview_1_trough);
        listview_1_trough.setAdapter(mFEDAddBagListViewAdapter1);
        mFEDAddBagListViewAdapter1.clear();
        mFEDAddBagListViewAdapter1.add(data1);

        listview_2_trough = (CustomListView) findViewById(R.id.listview_2_trough);
        listview_2_trough.setAdapter(mFEDAddBagListViewAdapter2);
        mFEDAddBagListViewAdapter2.clear();
        mFEDAddBagListViewAdapter2.add(data2);

        listview_3_trough = (CustomListView) findViewById(R.id.listview_3_trough);
        listview_3_trough.setAdapter(mFEDAddBagListViewAdapter3);
        mFEDAddBagListViewAdapter3.clear();
        mFEDAddBagListViewAdapter3.add(data3);

        listview_4_trough = (CustomListView) findViewById(R.id.listview_4_trough);
        listview_4_trough.setAdapter(mFEDAddBagListViewAdapter4);
        mFEDAddBagListViewAdapter4.clear();
        mFEDAddBagListViewAdapter4.add(data4);

        button_add_bag_01 = (Button) findViewById(R.id.button_add_bag_01);
        button_add_bag_02 = (Button) findViewById(R.id.button_add_bag_02);
        button_add_bag_03 = (Button) findViewById(R.id.button_add_bag_03);
        button_add_bag_04 = (Button) findViewById(R.id.button_add_bag_04);
        button_add_bag_01.setOnClickListener(this);
        button_add_bag_02.setOnClickListener(this);
        button_add_bag_03.setOnClickListener(this);
        button_add_bag_04.setOnClickListener(this);

        pull_scrollview_putaway = (PullToRefreshScrollView)findViewById(R.id.pull_scrollview_putaway);
        pull_scrollview_putaway.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getDataByMachineCode();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    /**
     * 扫描机器码时获取机器库存信息
     */
    public void getDataByMachineCode(){
        showLoadingDialog("玩命加载中...");
        HandworkApi.get().getDataByMachineCode(mContext, machineCode, new ApiConfig.ApiRequestListener<MachineDataInfo>() {
            @Override
            public void onSuccess(String msg, MachineDataInfo data) {
                dismissLoadingDialog();
                if (data!=null){
                    showView(data);
                }
                pull_scrollview_putaway.onRefreshComplete();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                pull_scrollview_putaway.onRefreshComplete();
            }
        });
    }

    /**
     * 填充页面
     */
    public void showView(MachineDataInfo data){
        tv_title.setText("设备库存总数"+data.getTotalNum());
        textview_1_trough_num.setText(data.getFirstChannelNum().size()+"");
        textview_2_trough_num.setText(data.getSecondChannelNum().size()+"");
        textview_3_trough_num.setText(data.getThirdChannelNum().size()+"");
        textview_4_trough_num.setText(data.getFourthChannelNum().size()+"");
        addDataToChannel(data.getFirstChannelNum().size(),data.getSecondChannelNum().size(),
                data.getThirdChannelNum().size(),data.getFourthChannelNum().size(),
                data.getFirstChannelNum(),data.getSecondChannelNum(),data.getThirdChannelNum(),data.getFourthChannelNum());
        if (data.getFirstChannelNum().size()>=12){
            button_add_bag_01.setClickable(false);
            button_add_bag_01.setBackground(getResources().getDrawable(R.drawable.btn_bg_cannotclick));
            button_add_bag_01.setText("已满");
            button_add_bag_01.setTextColor(Color.parseColor("#48c299"));
        }
        if (data.getSecondChannelNum().size()>=12){
            button_add_bag_02.setClickable(false);
            button_add_bag_02.setBackground(getResources().getDrawable(R.drawable.btn_bg_cannotclick));
            button_add_bag_02.setText("已满");
            button_add_bag_02.setTextColor(Color.parseColor("#48c299"));
        }
        if (data.getThirdChannelNum().size()>=12){
            button_add_bag_03.setClickable(false);
            button_add_bag_03.setBackground(getResources().getDrawable(R.drawable.btn_bg_cannotclick));
            button_add_bag_03.setText("已满");
            button_add_bag_03.setTextColor(Color.parseColor("#48c299"));
        }
        if (data.getFourthChannelNum().size()>=12){
            button_add_bag_04.setClickable(false);
            button_add_bag_04.setBackground(getResources().getDrawable(R.drawable.btn_bg_cannotclick));
            button_add_bag_04.setText("已满");
            button_add_bag_04.setTextColor(Color.parseColor("#48c299"));
        }
    }


    /**
     * 向后台提交添加袋子数据
     * @param terminalno
     * @param barcode
     * @param channel
     */
    public void putAwaySupplies(String terminalno,String barcode,String channel){
        showLoadingDialog("请稍后！");
        HandworkApi.get().submitDataToMachineCode(mContext, terminalno, barcode, channel, new ApiConfig.ApiRequestListener<MachineDataInfo>() {
            @Override
            public void onSuccess(String msg, MachineDataInfo data) {
                dismissLoadingDialog();
                showCustomToast(msg);
                if (data!=null){
                    tv_title.setText("设备库存总数"+data.getTotalNum());
                    textview_1_trough_num.setText(data.getFirstChannelNum().size()+"");
                    textview_2_trough_num.setText(data.getSecondChannelNum().size()+"");
                    textview_3_trough_num.setText(data.getThirdChannelNum().size()+"");
                    textview_4_trough_num.setText(data.getFourthChannelNum().size()+"");
                    addDataToChannel(data.getFirstChannelNum().size(),data.getSecondChannelNum().size(),
                            data.getThirdChannelNum().size(),data.getFourthChannelNum().size(),
                            data.getFirstChannelNum(),data.getSecondChannelNum(),
                            data.getThirdChannelNum(),data.getFourthChannelNum());
                    if (data.getFirstChannelNum().size()>=12){
                        button_add_bag_01.setClickable(false);
                        button_add_bag_01.setBackground(getResources().getDrawable(R.drawable.btn_bg_cannotclick));
                        button_add_bag_01.setText("已满");
                        button_add_bag_01.setTextColor(Color.parseColor("#48c299"));
                    }
                    if (data.getSecondChannelNum().size()>=12){
                        button_add_bag_02.setClickable(false);
                        button_add_bag_02.setBackground(getResources().getDrawable(R.drawable.btn_bg_cannotclick));
                        button_add_bag_02.setText("已满");
                        button_add_bag_02.setTextColor(Color.parseColor("#48c299"));
                    }
                    if (data.getThirdChannelNum().size()>=12){
                        button_add_bag_03.setClickable(false);
                        button_add_bag_03.setBackground(getResources().getDrawable(R.drawable.btn_bg_cannotclick));
                        button_add_bag_03.setText("已满");
                        button_add_bag_03.setTextColor(Color.parseColor("#48c299"));
                    }
                    if (data.getFourthChannelNum().size()>=12){
                        button_add_bag_04.setClickable(false);
                        button_add_bag_04.setBackground(getResources().getDrawable(R.drawable.btn_bg_cannotclick));
                        button_add_bag_04.setText("已满");
                        button_add_bag_04.setTextColor(Color.parseColor("#48c299"));
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

    public void addDataToChannel(int firstChannelNum,int secondChannelNum,int thirdChannelNum,int fourthChannelNum
                                    ,List<String> firstChannelList,List<String> secondChannelList,List<String> thirdChannelList,List<String> fourthChannelList){
        int first = 0,second=0,third=0,fourth=0;
        first=(firstChannelNum<13)?firstChannelNum:12;
        second=(secondChannelNum<13)?secondChannelNum:12;
        third=(thirdChannelNum<13)?thirdChannelNum:12;
        fourth=(fourthChannelNum<13)?fourthChannelNum:12;
        for (int i=0;i<first;i++){
            data1.set(data1.size()-i-1,firstChannelList.get(i));
        }
        mFEDAddBagListViewAdapter1.clear();
        mFEDAddBagListViewAdapter1.add(data1);
        for (int i=0;i<second;i++){
            data2.set(data2.size()-i-1,secondChannelList.get(i));
        }
        mFEDAddBagListViewAdapter2.clear();
        mFEDAddBagListViewAdapter2.add(data2);
        for (int i=0;i<third;i++){
            data3.set(data3.size()-i-1,thirdChannelList.get(i));
        }
        mFEDAddBagListViewAdapter3.clear();
        mFEDAddBagListViewAdapter3.add(data3);
        for (int i=0;i<fourth;i++){
            data4.set(data4.size()-i-1,fourthChannelList.get(i));
        }
        mFEDAddBagListViewAdapter4.clear();
        mFEDAddBagListViewAdapter4.add(data4);
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, ScanningActivity.class);
        switch (v.getId()){
            case R.id.button_add_bag_01:
                channelCode=1;
                intent.putExtra("type",2);
                startActivityForResult(intent,REQUEST_BAGCODE);
                break;
            case R.id.button_add_bag_02:
                channelCode=2;
                intent.putExtra("type",2);
                startActivityForResult(intent,REQUEST_BAGCODE);
                break;
            case R.id.button_add_bag_03:
                channelCode=3;
                intent.putExtra("type",2);
                startActivityForResult(intent,REQUEST_BAGCODE);
                break;
            case R.id.button_add_bag_04:
                channelCode=4;
                intent.putExtra("type",2);
                startActivityForResult(intent,REQUEST_BAGCODE);
                break;
        }
    }

    String bagCodes;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 根据上面发送过去的请求吗来区别
        Log.e("-- 扫码结束了","-"+requestCode+"-"+resultCode);
        if (data.getStringExtra("strResult").equals("")||data.getStringExtra("strResult").isEmpty()||data.getStringExtra("strResult").length()==0){
            return;
        }
        switch (channelCode) {
            case 1:
                bagCodes=regroupStrResult(data.getStringExtra("strResult"));
                putAwaySupplies(machineCode,bagCodes,channelCode+"");
                break;
            case 2:
                bagCodes=regroupStrResult(data.getStringExtra("strResult"));
                putAwaySupplies(machineCode,bagCodes,channelCode+"");
                break;
            case 3:
                bagCodes=regroupStrResult(data.getStringExtra("strResult"));
                putAwaySupplies(machineCode,bagCodes,channelCode+"");
                break;
            case 4:
                bagCodes=regroupStrResult(data.getStringExtra("strResult"));
                putAwaySupplies(machineCode,bagCodes,channelCode+"");
                break;
            default:
                break;
        }
    }

    /**
     * 将单个袋子码添加29个 组装成范围码  范围码重组
     * @param strResult
     * @return
     */
    String startcode;
    String endcode;
    public String  regroupStrResult(String strResult){
        if (!strResult.contains("-")) { //只包含起始编码，默认每卷垃圾袋30个
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
        return startcode+"-"+endcode;
    }

    /**
     * 获取袋子类型
     * @param strResult
     * @return
     */
    String bagtype;
    public String getBagType(String strResult){
        if (startcode.length() != QRCodeInputActivity.BAG_LENGTH_TYPE_1) {
            return bagtype = startcode.substring(1, 2);
        }
        return null;
    }
}
