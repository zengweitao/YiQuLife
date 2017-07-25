package com.quanmai.yiqu.ui.Around;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.AroundApi;
import com.quanmai.yiqu.api.vo.ShopClassInfo;
import com.quanmai.yiqu.api.vo.ShopInfo;
import com.quanmai.yiqu.api.vo.ShopListInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.Around.adapter.CouponCatalogAdapter;
import com.quanmai.yiqu.ui.Around.adapter.CouponShopListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看所有优惠券（全网/周边）界面
 */
public class AllCouponActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title;
    private ImageView iv_right; //搜索
    private ImageView iv_back;
    private RecyclerView recyclerViewCatalog; //左侧优惠券分类目录
    private RecyclerView recyclerViewContent; //右侧商家列表

    private String couponType = "0"; //优惠券类型，“0”,全网商家类型，“1”，周边商家类型

    CouponCatalogAdapter mCatalogAdapter;           //优惠券分类目录适配器
    List<CouponShopListAdapter> mShopListAdapters;  //优惠券商家适配器列表

    List<ShopClassInfo> mClassInfoList; //优惠券分类信息集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_coupon);
        initView();
        init();
    }

    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setOnClickListener(this);
        recyclerViewCatalog = (RecyclerView) findViewById(R.id.recyclerViewCatalog);
        recyclerViewContent = (RecyclerView) findViewById(R.id.recyclerViewContent);

        //线性（纵向）布局
        LinearLayoutManager lmCatalog = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerViewCatalog.setLayoutManager(lmCatalog);
        //网格布局
        GridLayoutManager gmContent = new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
        recyclerViewContent.setLayoutManager(gmContent);
    }

    public void init() {
        if (getIntent().hasExtra("couponType")) {
            couponType = getIntent().getStringExtra("couponType");
        }
        tv_title.setText(couponType.equals(ShopListInfo.typeBusiness) ? "全网优惠券" : "周边优惠券"); //标题
        mCatalogAdapter = new CouponCatalogAdapter(mContext);
        mShopListAdapters = new ArrayList<>();
        recyclerViewCatalog.setAdapter(mCatalogAdapter);
        //分类目录列表设置item点击监听
        mCatalogAdapter.setOnItemClickListener(new CouponCatalogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //遍历，重置item选中标示符
                for (int i = 0; i < mClassInfoList.size(); i++) {
                    if (i == position) {
                        mClassInfoList.get(i).isCheck = true;
                    } else {
                        mClassInfoList.get(i).isCheck = false;
                    }
                }
                //刷新item（选中状态）
                mCatalogAdapter.refresh(mClassInfoList);

                if (mShopListAdapters.size() >= position) {
                    recyclerViewContent.setAdapter(mShopListAdapters.get(position)); //设置商家信息适配器
                    //点击item对应适配器无数据，重新请求
                    if (mShopListAdapters.get(position).getItemCount() == 0) {
                        getShopInfoList(position, mClassInfoList.get(position).id);
                    }
                }
            }
        });

        getShopClassList();
    }

    /**
     * 获取商户分类列表
     */
    private void getShopClassList() {
        showLoadingDialog();
        AroundApi.getInstance().getShopClassList(mContext, new ApiConfig.ApiRequestListener<List<ShopClassInfo>>() {
            @Override
            public void onSuccess(String msg, List<ShopClassInfo> data) {
                dismissLoadingDialog();
                if (data == null) {
                    return;
                }

                if (data.size() > 0) { //返回分类数量>0
                    int size = data.size();
                    while (size-- > 0) {
                        mShopListAdapters.add(new CouponShopListAdapter(mContext));
                    }

                    data.get(0).isCheck = true; //默认选中第一个item
                    recyclerViewContent.setAdapter(mShopListAdapters.get(0));
                    mCatalogAdapter.clear();
                    mCatalogAdapter.add(data);
                    mClassInfoList = data;
                    getShopInfoList(0, data.get(0).id);
                }
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    /**
     * 获取商户信息列表
     *
     * @param position item位置
     * @param shopType 商户类型参数（可不传）
     */
    private void getShopInfoList(final int position, final String shopType) {
        AroundApi.getInstance().getShopInfoList(mContext, shopType, new ApiConfig.ApiRequestListener<ShopListInfo>() {
            @Override
            public void onSuccess(String msg, ShopListInfo shopListInfo) {
                if (shopListInfo == null) return;

                if (shopListInfo.data.size() > 0) {  //内容不为空
                    for (int i = 0; i < shopListInfo.data.size(); i++) {
                        List<ShopListInfo.Data> data = shopListInfo.data;
                        CommonList<ShopInfo> infoList = data.get(i).infoList; //商户列表
                        if (data.get(i).type.equals(couponType)) {
                            mShopListAdapters.get(position).add(infoList);
                        }
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
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
            //搜索
            case R.id.iv_right: {
                startActivity(NewsSearchActivity.class);
                break;
            }
            default:
                break;
        }
    }
}
