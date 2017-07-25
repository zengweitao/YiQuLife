package com.quanmai.yiqu.ui.mys.realname;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.ChooseRoomInfo;
import com.quanmai.yiqu.api.vo.RoomInfo;
import com.quanmai.yiqu.api.vo.SearchUserDataInfo;
import com.quanmai.yiqu.api.vo.UnitInfo;
import com.quanmai.yiqu.common.LoadingDialog;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.widget.SystemBarTintManager;
import com.quanmai.yiqu.ui.mys.realname.adapter.AddressChoiceRoomListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2016/7/8.
 * 住址选择页面--选择单元和房号
 */
public class AddressChoiceFourthMainActivity extends ExpandableListActivity implements View.OnClickListener {
    private PullToRefreshExpandableListView expandableListView_chooseroom;

    private String mCity; //城市名
    private String mCommName; //小区名
    private String mCommcode; //小区编码
    private String mBuildcode; //楼栋号
    private List<UnitInfo> mUnitInfos = new ArrayList<>();
    private EditText edtSearchContent;
    private ImageView imgClear;
    private ImageView btnSearch;
    private FrameLayout flSearch;

    private boolean isFuzzySearch = false; //是否显示模糊搜索框
    private ExpandableListView expandableListView;
    private AddressChoiceRoomListAdapter mAddressChoiceRoomListAdapter;

    LoadingDialog mLoadingDialog;
    Context mContext;
    Session mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mLoadingDialog = new LoadingDialog(this, "请稍候");
        mLoadingDialog.setCanceledOnTouchOutside(true);
        mContext = this;
        mSession = Session.get(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager manager = new SystemBarTintManager(this);
            manager.setStatusBarTintEnabled(true);
            manager.setStatusBarTintColor(getResources().getColor(R.color.theme));
        }
        setContentView(R.layout.activity_address_choose_room);
        initView();
        init();
        initFuzzySearch();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("选择住房");
        findViewById(R.id.iv_back).setOnClickListener(this);

        expandableListView_chooseroom = (PullToRefreshExpandableListView) findViewById(R.id.expandableListView_chooseroom);
        expandableListView = expandableListView_chooseroom.getRefreshableView();
        findViewById(R.id.rlSearch).setVisibility(View.GONE);
        edtSearchContent = (EditText) findViewById(R.id.edtSearchContent);
        edtSearchContent.setOnClickListener(this);
        imgClear = (ImageView) findViewById(R.id.imgClear);
        imgClear.setOnClickListener(this);
        btnSearch = (ImageView) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        flSearch = (FrameLayout) findViewById(R.id.flSearch);
        flSearch.setOnClickListener(this);
    }

    private void init() {
        mAddressChoiceRoomListAdapter = new AddressChoiceRoomListAdapter(mContext);
        mCity = getIntent().getStringExtra("city");
        mCommName = getIntent().getStringExtra("commname");
        mCommcode = getIntent().getStringExtra("commcode");
        mBuildcode = getIntent().getStringExtra("buildcode");

        expandableListView.setAdapter(mAddressChoiceRoomListAdapter);
        getUnitAndRoom(mCommcode, mBuildcode);
        expandableListView_chooseroom.setMode(PullToRefreshBase.Mode.DISABLED);
        expandableListView_chooseroom.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                getUnitAndRoom(mCommcode, mBuildcode);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {}
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = getIntent();
                intent.setAction(ResidentBindingActivity.ACTION_ADDRESS_CHOICE);
                RoomInfo roomInfo = new RoomInfo();
                roomInfo.city = mCity;
                roomInfo.commname = mCommName;
                roomInfo.commcode = mCommcode;
                roomInfo.buildingno = mBuildcode;
                roomInfo.buildno = mAddressChoiceRoomListAdapter.getGroup(groupPosition).toString();
                roomInfo.room = mAddressChoiceRoomListAdapter.getChild(groupPosition,childPosition).toString();
                intent.putExtra("roomInfo", roomInfo);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                intent.putExtra("finish", "finish");
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
        });
       /* expandableListView_chooseroom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                intent.setAction(ResidentBindingActivity.ACTION_ADDRESS_CHOICE);
                RoomInfo roomInfo = (RoomInfo) mListAdapter.getItem(position - 1);
                roomInfo.city = mCity;
                roomInfo.commname = mCommName;
                roomInfo.commcode = mCommcode;
                roomInfo.buildingno = mBuildcode;
                intent.putExtra("roomInfo", roomInfo);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                intent.putExtra("finish", "finish");
                setResult(RESULT_OK, intent);
                finish();
            }
        });*/
        expandableListView_chooseroom.setRefreshing();
    }

    private void initFuzzySearch() {
        isFuzzySearch = getIntent().getBooleanExtra("isFuzzySearch", false);
        if (isFuzzySearch) {
            flSearch.setVisibility(View.VISIBLE);
        }
        edtSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH: {
                        fuzzySearch(mBuildcode, mCommcode);
                        break;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 实名制获取单元楼和房间号
     *
     * @param commcode
     * @param buildcode
     */
    private void getUnitAndRoom(String commcode, String buildcode) {
        UserApi.get().getUnitAndRoom(mContext, commcode, buildcode, new ApiConfig.ApiRequestListener<ChooseRoomInfo>() {
            @Override
            public void onSuccess(String msg, ChooseRoomInfo data) {
                expandableListView_chooseroom.onRefreshComplete();
                if (data == null || data.getArList().size() == 0) {
                    Utils.showCustomToast(mContext, msg);
                    return;
                }
                mAddressChoiceRoomListAdapter.addAll(data.getArList());
                //设置二级菜单默认打开
                int groupCount = expandableListView.getCount();
                for (int i=0; i<groupCount; i++) {
                    expandableListView.expandGroup(i);
                };
            }

            @Override
            public void onFailure(String msg) {
                expandableListView_chooseroom.onRefreshComplete();
                Utils.showCustomToast(mContext, msg);
            }
        });

    }

    //指定用户关键字模糊搜索（单元号、姓名、手机号等）
    private void fuzzySearch(String buildcode, String commcode) {
        // validate
        String edtSearchContentString = edtSearchContent.getText().toString().trim();
        if (TextUtils.isEmpty(edtSearchContentString)) {
            Utils.showCustomToast(mContext, "请输入关键字搜索");
            return;
        }

        showLoadingDialog();

        UserApi.get().membersFuzzySearch(mContext, buildcode, commcode, edtSearchContentString, new ApiConfig.ApiRequestListener<SearchUserDataInfo>() {
            public void onSuccess(String msg, SearchUserDataInfo data) {
                dismissLoadingDialog();
                if (data == null) {
                    showCustomToast(msg);
                    return;
                }
                List<ChooseRoomInfo.ArListBean> listdata=new ArrayList<ChooseRoomInfo.ArListBean>();

                for (int i=0;i<data.getArList().size();i++){
                    ChooseRoomInfo.ArListBean arListBean=new ChooseRoomInfo.ArListBean();
                    arListBean.setBuildno(data.getArList().get(i).getBuildno());
                    List<String> strRoom=new ArrayList<String>();
                    for (int j=0;j<data.getArList().get(i).getRoomList().size();j++){
                        strRoom.add(data.getArList().get(i).getRoomList().get(j).getRoom());
                    }
                    arListBean.setRoomList(strRoom);
                    listdata.add(arListBean);
                }
                mAddressChoiceRoomListAdapter.clear();
                mAddressChoiceRoomListAdapter.addAll(listdata);
                //设置二级菜单默认打开
                int groupCount = expandableListView.getCount();
                for (int i=0; i<groupCount; i++) {
                    expandableListView.expandGroup(i);
                };
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
            case R.id.btnSearch: {
                fuzzySearch(mBuildcode, mCommcode);
                break;
            }
            case R.id.imgClear:{
                edtSearchContent.setText(null);
            }
            default:
                break;
        }
    }
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams wlp = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            wlp.flags |= bits;
        } else {
            wlp.flags &= ~bits;
        }
        win.setAttributes(wlp);
    }
    public void showLoadingDialog() {
        if (mLoadingDialog == null) return;

        if (mLoadingDialog.isShowing()) return;

        mLoadingDialog.show();
    }
    public void showLoadingDialog(String text) {
        if (mLoadingDialog == null) return;

        if (mLoadingDialog.isShowing()) return;

        if (text != null) mLoadingDialog.setLoadingText(text);

        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
    public void showCustomToast(String text) {
        Utils.showCustomToast(mContext, text);
    }
}
