package com.quanmai.yiqu.ui.community;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.AdInfo;
import com.quanmai.yiqu.api.vo.BannerInfo;
import com.quanmai.yiqu.api.vo.CommCodeInfo;
import com.quanmai.yiqu.api.vo.CommServiceList;
import com.quanmai.yiqu.api.vo.CommunityAddressInfo;
import com.quanmai.yiqu.api.vo.ServicesInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.ListViewForScrollView;
import com.quanmai.yiqu.common.widget.NoScrollGridView;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.adapter.LocalImageHolderView;
import com.quanmai.yiqu.ui.fragment.UnusedFragment;
import com.quanmai.yiqu.ui.fragment.selectaddress.ViewAddressSelect;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityServiceActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_title,tv_right,textViewNoData;
    RelativeLayout relativeLayoutIllegalSearch,
    relativeLayoutDeliverySearch,relativeLayoutBusSearch
            ,relativeLayoutOrderSearch;
    ConvenientBanner banner;
    ImageView imageViewBanner;
    PopupWindow popupWindow;
    LinearLayout content;
    ViewAddressSelect viewAddressSelect;
    ServicePhoneListAdapter adapter;
    ServiceListAdapter mServicesAdapter;
    ListViewForScrollView listViewPhoneNo;
    NoScrollGridView noScrollGridView;
//    ListViewForScrollView listViewServices;

    String url;
    List<String> urls;
    List<CommunityAddressInfo> parentList;
    Map<String ,List<CommunityAddressInfo>> map ;
    List<AdInfo> infos;

    //网页跳转链接
    private final String ILLEGAL_SEARCH_URL = "http://m.cheshouye.com/api/weizhang/";
    private final String DELIVERY_SEARCH_URL = "http://m.kuaidi100.com/index_all.html";
    private final String BUS_SEARCH_URL = "http://zuoche.com/touch/";
    private final String ORDER_SEARCH_URL = "http://wy.guahao.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        init();
    }

    //初始化
    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_right = (TextView)findViewById(R.id.tv_right);
        textViewNoData = (TextView)findViewById(R.id.textViewNoData);
        tv_right.setOnClickListener(this);
        listViewPhoneNo = (ListViewForScrollView)findViewById(R.id.listViewPhoneNo);
        noScrollGridView = (NoScrollGridView)findViewById(R.id.noScrollGridView);
        banner = (ConvenientBanner)findViewById(R.id.banner);
        imageViewBanner = (ImageView)findViewById(R.id.imageViewRule);
        imageViewBanner.setOnClickListener(this);
        tv_title.setText("社区服务");

        content = (LinearLayout)findViewById(R.id.content);
        urls = new ArrayList<>();
        map = new HashMap<>();

        //地区选择下拉列表
        popupWindow = new PopupWindow(content, FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        ColorDrawable dw = new ColorDrawable(0x90000000);  //背景半透明
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setFocusable(true);
        popupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.setFocusable(false);
                popupWindow.dismiss();

                return true;
            }
        });

        //地区选择后的回调
        viewAddressSelect = new ViewAddressSelect(this, new UnusedFragment.OnSelectListener() {
            @Override
            public void onSelect(CommunityAddressInfo info) {
                tv_right.setText(info.commname);
                mSession.setCommunity(info.commname);
                mSession.setCommunity_code(info.commcode);
                getServiceList(info.commcode);
                getCommunityServiceList(info.commcode);
                popupWindow.dismiss();
            }

            @Override
            public void onSelect(String city) {
                mSession.setCommunity_city(city);
            }
        });
        RelativeLayout bottom = (RelativeLayout)viewAddressSelect.findViewById(R.id.relativeBottom);
        LinearLayout content = (LinearLayout)viewAddressSelect.findViewById(R.id.linearContent);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(viewAddressSelect);

        getAdvert();

        //设置默认选择的小区
        if (!TextUtils.isEmpty(UserInfo.get().getCommunity())){
            tv_right.setText(UserInfo.get().getCommunity());
            getServiceList(UserInfo.get().getCommcode());
            getCommunityServiceList(UserInfo.get().getCommcode());
        }else if (mSession.getCommunity()!=null&&mSession.getCommunity()!=""){
            tv_right.setText(mSession.getCommunity());
            getServiceList(mSession.getCommunity_code());
            getCommunityServiceList(mSession.getCommunity_code());
        } else {
            tv_right.setText("切换社区");
        }
    }

    //初始化banner
    private void initBanner() {
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new LocalImageHolderView();
            }
        },urls)
                .setPageIndicator(new int[]{R.drawable.icon_gray_dot,R.drawable.icon_green_dot})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(3000)
                .notifyDataSetChanged();

        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (infos != null && infos.size() > 0) {
                    if (!TextUtils.isEmpty(infos.get(position).adver_url)) {
                        Intent intent = new Intent(CommunityServiceActivity.this, WebActivity.class);
                        intent.putExtra("http_url", infos.get(position).adver_url);
                        MobclickAgent.onEvent(mContext, infos.get(position).adver_alias); //友盟自定义事件统计
                        startActivity(intent);
                    }
                }
            }
        });
    }


    /**
     * 获取广告列表
     */
    private void getAdvert(){
        showLoadingDialog("请稍候");
        GoodsApi.get().GetAvd(this, "3", new ApiConfig.ApiRequestListener<BannerInfo>() {
            @Override
            public void onSuccess(String msg, BannerInfo data) {
                dismissLoadingDialog();
                infos = new ArrayList<AdInfo>();
                infos.addAll(data.adList);
                for (int i = 0; i < data.adList.size(); i++) {
                    urls.add(data.adList.get(i).adver_img);
                }
                if (urls.size() > 0) {
                    if (urls.size()==1){
                        imageViewBanner.setVisibility(View.VISIBLE);
                        banner.setVisibility(View.GONE);
                        ImageloaderUtil.displayImage(CommunityServiceActivity.this, urls.get(0), imageViewBanner);

                    }else {
                        imageViewBanner.setBackground(null);
                        imageViewBanner.setVisibility(View.GONE);
                        banner.setVisibility(View.VISIBLE);
                        initBanner();
                    }
                }

                getCommunityList();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showShortToast(msg);
            }
        });
    }

    /**
     * 小区服务列表
     */
    private void getServiceList(String commcode){
        UserApi.get().GetCommunityServiceList(mContext, commcode, new ApiConfig.ApiRequestListener<CommServiceList>() {
            @Override
            public void onSuccess(String msg, CommServiceList data) {
                dismissLoadingDialog();
//                textViewNoData.setVisibility(View.GONE);
                if (adapter!=null){
                    adapter.clear();
                }
                listViewPhoneNo.setVisibility(View.VISIBLE);
                adapter = new ServicePhoneListAdapter(mContext, data.CommServiceList);
                listViewPhoneNo.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                listViewPhoneNo.setVisibility(View.GONE);
                if (adapter!=null){
                    adapter.clear();
                }
//                textViewNoData.setVisibility(View.VISIBLE);
            }
        });
    }
    /**
     * 获取城市地区列表
     * */
    private void getCommunityList() {
        UserApi.get().GetCommunityList(mContext, "", new ApiConfig.ApiRequestListener<CommCodeInfo>() {
            @Override
            public void onSuccess(String msg, CommCodeInfo data) {
                parentList= new ArrayList<CommunityAddressInfo>();
                parentList.addAll(data.commCodeList);
                for (int i = 0; i < parentList.size(); i++) {
                    map.put(parentList.get(i).commcode, null);
                    if (i == 0) {
                        getCommunityList(parentList.get(0).commcode);
                    }
                }
                viewAddressSelect.setCity(parentList);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
//                showCustomToast(msg);
            }
        });
    }
    /**
     * 根据城市获取小区列表
     * */
    private void getCommunityList(final String city) {
        UserApi.get().GetCommunityList(mContext, city, new ApiConfig.ApiRequestListener<CommCodeInfo>() {
            @Override
            public void onSuccess(String msg, CommCodeInfo data) {
                if (data!=null){
                    if (!TextUtils.isEmpty(UserInfo.get().getCommunity())){
                        tv_right.setText(UserInfo.get().getCommunity());
                        getServiceList(UserInfo.get().getCommcode());
                    }else if (mSession.getCommunity()!=null&&mSession.getCommunity()!=""){
                        tv_right.setText(mSession.getCommunity());
                        getServiceList(mSession.getCommunity_code());
                    } else {
                        tv_right.setText(data.commCodeList.get(0).commname);
                        mSession.setCommunity(data.commCodeList.get(0).commname);
                        mSession.setCommunity_code(data.commCodeList.get(0).commcode);

                        map.put(city, data.commCodeList);
                        viewAddressSelect.setData(map);
                        getServiceList(map.get(city).get(0).commcode);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
//                showCustomToast(msg);
            }
        });
    }

    /**
     * 小区便民服务列表
     */
    private void getCommunityServiceList(String areacode){
        UserApi.get().GetCommunityService(mContext,areacode, new ApiConfig.ApiRequestListener<CommonList<ServicesInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<ServicesInfo> data) {
                mServicesAdapter = new ServiceListAdapter(mContext,data);
                noScrollGridView.setAdapter(mServicesAdapter);
//                listViewServices.setAdapter(mServicesAdapter);
            }

            @Override
            public void onFailure(String msg) {
//                showCustomToast(msg);
                if (mServicesAdapter!=null){
                    mServicesAdapter.clear();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,ServiceSearchActivity.class);
        switch (v.getId()){
            //违章查询
//            case R.id.relativeLayoutIllegalSearch:{
//                intent.putExtra("weburl",ILLEGAL_SEARCH_URL);
//                intent.putExtra("title","违章查询");
//                startActivity(intent);
//                break;
//            }
//            //快递查询
//            case R.id.relativeLayoutDeliverySearch:{
//                intent.putExtra("weburl",DELIVERY_SEARCH_URL);
//                intent.putExtra("title","快递查询");
//                startActivity(intent);
//                break;
//            }
//            //公交查询
//            case R.id.relativeLayoutBusSearch:{
//                intent.putExtra("weburl",BUS_SEARCH_URL);
//                intent.putExtra("title","公交查询");
//                startActivity(intent);
//                break;
//            }
//            //预约挂号
//            case R.id.relativeLayoutOrderSearch:{
//                intent.putExtra("weburl",ORDER_SEARCH_URL);
//                intent.putExtra("title","预约挂号");
//                startActivity(intent);
//                break;
//            }
            //切换小区
            case R.id.tv_right:{
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else{
                    if (!TextUtils.isEmpty(mSession.getCommunity_city())){
                        viewAddressSelect.setDefaultCity(mSession.getCommunity_city());
                    }
                    popupWindow.showAsDropDown(tv_right);
                }
                break;
            }
            //banner网页跳转
            case R.id.imageViewRule:{
                if (infos != null && infos.size() > 0) {
                    if (!TextUtils.isEmpty(infos.get(0).adver_url)){
                        intent = new Intent(CommunityServiceActivity.this, WebActivity.class);
                        intent.putExtra("http_url", infos.get(0).adver_url);
                        MobclickAgent.onEvent(mContext, infos.get(0).adver_alias); //友盟自定义事件统计
                        startActivity(intent);
                    }
                }
                break;
            }
            default:break;
        }
    }
}
