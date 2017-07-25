package com.quanmai.yiqu.ui.mys.realname;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.AddressRoomInfo;
import com.quanmai.yiqu.api.vo.CommunityInfo;
import com.quanmai.yiqu.api.vo.RoomInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.ui.mys.realname.adapter.AddressChoiceRidgepoleAdapter;
import com.quanmai.yiqu.ui.mys.realname.adapter.AddressChoiceRoomAdapter;
import com.quanmai.yiqu.ui.mys.realname.adapter.AddressChoiceUnitAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddressChoiceRoomActivity extends BaseActivity implements View.OnClickListener,
        AddressChoiceRidgepoleAdapter.setOnRidgepoleItemClick,AddressChoiceUnitAdapter.setOnUnitItemClick,
        AddressChoiceRoomAdapter.setOnRoomItemClick{

    private TextView tv_title;
    private ImageView iv_back;
    private EditText edtSearchContent;
    private ImageView imgClear;
    private Button btnSearch;
    CommunityInfo communityInfo;
    private PullToRefreshListView pullToRefreshChoiceRoom;
    private LinearLayout llridgepole;
    private LinearLayout llunit;
    private LinearLayout llroom;
    private AddressChoiceRidgepoleAdapter mAdapter01;
    private AddressChoiceUnitAdapter mAdapter02;
    private AddressChoiceRoomAdapter mAdapter03;
    List<AddressRoomInfo.CommunityBean.BuildingListBean> ridgepoleList;
    List<AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean> unitList;
    List<AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean.RoomListBean> roomList;
    AddressRoomInfo info;
    private TextView tvridgepole;
    private ImageView ivridgepole;
    private TextView tvunit;
    private ImageView ivunit;
    private TextView tvroom;
    private ImageView ivroom;
    private TextView tv_right;
    String commcode;
    String commname;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_choice_room);
        initView();
        init();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择地址");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_right = (TextView) findViewById(R.id.tv_right);
        imgClear = (ImageView) findViewById(R.id.imgClear);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        llridgepole = (LinearLayout) findViewById(R.id.llridgepole);
        llunit = (LinearLayout) findViewById(R.id.llunit);
        llroom = (LinearLayout) findViewById(R.id.llroom);
        edtSearchContent = (EditText) findViewById(R.id.edtSearchContent);
        pullToRefreshChoiceRoom = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_choice_room);
        tvridgepole = (TextView) findViewById(R.id.tvridgepole);
        ivridgepole = (ImageView) findViewById(R.id.ivridgepole);
        tvunit = (TextView) findViewById(R.id.tvunit);
        ivunit = (ImageView) findViewById(R.id.ivunit);
        tvroom = (TextView) findViewById(R.id.tvroom);
        ivroom = (ImageView) findViewById(R.id.ivroom);

        iv_back.setOnClickListener(this);
        imgClear.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        llridgepole.setOnClickListener(this);
        llunit.setOnClickListener(null);
        llroom.setOnClickListener(null);
        tv_right.setOnClickListener(null);
    }

    private void init() {
        ridgepoleList=new ArrayList<>();
        unitList=new ArrayList<>();
        roomList=new ArrayList<>();
        if (getIntent().hasExtra("communityInfo")){
            communityInfo = (CommunityInfo) getIntent().getSerializableExtra("communityInfo");
            commcode=communityInfo.commcode;
            commname=communityInfo.commname;
            city=communityInfo.city;
        }
        if (getIntent().hasExtra("AddressTo")&&getIntent().getStringExtra("AddressTo").equals("ResidentBindingActivity")){
            commcode= UserInfo.get().commcode;
            commname= UserInfo.get().community;
            city="";
        }
        tv_right.setText(commname);
        mAdapter01 = new AddressChoiceRidgepoleAdapter(mContext, this);
        pullToRefreshChoiceRoom.setAdapter(mAdapter01);
        mAdapter02 = new AddressChoiceUnitAdapter(mContext, this);
        mAdapter03 = new AddressChoiceRoomAdapter(mContext, this);
        getAddressRoom(commcode);
    }

    /**
     * 获取指定小区的住户列表
     * @param commcode
     */
    public void getAddressRoom(String commcode){
        showLoadingDialog("请稍后...");
        UserApi.get().getUserAddress(mContext, commcode, new ApiConfig.ApiRequestListener<AddressRoomInfo>() {
            @Override
            public void onSuccess(String msg, AddressRoomInfo data) {
                dismissLoadingDialog();
                    info=data;
                    mAdapter01.add(data.getCommunity().getBuildingList());
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });

    }

    /**
     * 通过模糊查询搜索住户
     * @param search
     */
    public void getSearchRoom(String search){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:

                break;
            case R.id.imgClear:
                edtSearchContent.setText("");
                break;
            case R.id.btnSearch:
                if (edtSearchContent.getText().toString().trim().length()==0){
                    showCustomToast("请输入搜索内容！");
                    return;
                }
                getSearchRoom(edtSearchContent.getText().toString());
                break;
            case R.id.llridgepole:
                llunit.setOnClickListener(null);
                llroom.setOnClickListener(null);
                ivridgepole.setBackgroundResource(R.drawable.ic_green_bottom);
                tvridgepole.setText("栋");
                ivunit.setBackgroundResource(R.drawable.ic_gray_right);
                tvunit.setText("单元");
                ivroom.setBackgroundResource(R.drawable.ic_gray_right);
                mAdapter01.clear();
                pullToRefreshChoiceRoom.setAdapter(mAdapter01);
                mAdapter01.add(info.getCommunity().getBuildingList());
                break;
            case R.id.llunit:
                llunit.setOnClickListener(this);
                llroom.setOnClickListener(null);
                ivridgepole.setBackgroundResource(R.drawable.ic_green_right);
                ivunit.setBackgroundResource(R.drawable.ic_green_bottom);
                ivroom.setBackgroundResource(R.drawable.ic_gray_right);
                tvunit.setText("单元");
                mAdapter02.clear();
                pullToRefreshChoiceRoom.setAdapter(mAdapter02);
                mAdapter02.add(unitList);
                break;
            case R.id.llroom:
                ivridgepole.setBackgroundResource(R.drawable.ic_green_right);
                ivunit.setBackgroundResource(R.drawable.ic_green_right);
                ivroom.setBackgroundResource(R.drawable.ic_green_bottom);
                mAdapter03.clear();
                pullToRefreshChoiceRoom.setAdapter(mAdapter03);
                mAdapter03.add(roomList);
                break;
        }
    }

    @Override
    public void OnRidgepoleItemClick(View v, int poestion, List<AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean> list) {
        llunit.setOnClickListener(this);
        ivridgepole.setBackgroundResource(R.drawable.ic_green_right);
        ivunit.setBackgroundResource(R.drawable.ic_green_bottom);
        ivroom.setBackgroundResource(R.drawable.ic_gray_right);
        tvridgepole.setText(info.getCommunity().getBuildingList().get(poestion).getBuildingNo());
        unitList.clear();
        unitList.addAll(list);
        mAdapter02.clear();
        pullToRefreshChoiceRoom.setAdapter(mAdapter02);
        mAdapter02.add(unitList);
        buildingno=info.getCommunity().getBuildingList().get(poestion).getBuildingNo();
    }

    @Override
    public void OnUnitItemClick(View v, int poestion, List<AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean.RoomListBean> list) {
        llroom.setOnClickListener(this);
        ivridgepole.setBackgroundResource(R.drawable.ic_green_right);
        ivunit.setBackgroundResource(R.drawable.ic_green_right);
        ivroom.setBackgroundResource(R.drawable.ic_green_bottom);
        tvunit.setText(unitList.get(poestion).getUnitNo());
        roomList.clear();
        roomList.addAll(list);
        mAdapter03.clear();
        pullToRefreshChoiceRoom.setAdapter(mAdapter03);
        mAdapter03.add(roomList);
        buildno=unitList.get(poestion).getUnitNo();
    }

    String buildingno=null;
    String buildno=null;
    @Override
    public void OnRoomItemClick(View v, int poestion ,AddressRoomInfo.CommunityBean.BuildingListBean.UnitListBean.RoomListBean roomBean) {
        Intent intent = getIntent();
        intent.setAction(ResidentBindingActivity.ACTION_ADDRESS_CHOICE);
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.city = city;
        roomInfo.commname = commname;
        roomInfo.commcode = commcode;
        roomInfo.buildingno = buildingno+"栋";
        roomInfo.buildno = buildno+"单元";
        roomInfo.room = roomBean.getRoomNo()+"室";
        roomInfo.name = roomBean.getName();
        roomInfo.phone = roomBean.getMtel();
        roomInfo.usercompareid = roomBean.getUsercompareid();
        intent.putExtra("roomInfo", roomInfo);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        intent.putExtra("finish", "finish");
        setResult(RESULT_OK, intent);
        finish();
    }
}
