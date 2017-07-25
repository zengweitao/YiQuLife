package com.quanmai.yiqu.ui.classifigarbage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GarbageSortingApi;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.vo.AdInfo;
import com.quanmai.yiqu.api.vo.AdvertInfo;
import com.quanmai.yiqu.api.vo.BannerInfo;
import com.quanmai.yiqu.api.vo.GarbageClassifyInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.CustomDialog;
import com.quanmai.yiqu.common.widget.DraggableGridViewPager;
import com.quanmai.yiqu.ui.adapter.GarbageSortingListAdapter;
import com.quanmai.yiqu.ui.adapter.LocalImageHolderView;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.fuli.LuckyBagActivity;
import com.quanmai.yiqu.ui.grade.PersonalGradesActivity;
import com.quanmai.yiqu.ui.login.LoginActivity;
import com.quanmai.yiqu.ui.recycle.RecycleScoreRecordActivity;
import com.quanmai.yiqu.ui.transaction.DraggableGridViewAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 垃圾分类页面
 */
public class GarbageSortingActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    PullToRefreshListView listView;
    TextView tv_title;
    int[] mAdvertIconList;
    String[] mAdvertNameList;
    private List<AdvertInfo> mAdvertInfos = new ArrayList<>(); //中部icon菜单列表
    private LinearLayout llPoint;
    private ImageView[] imgPoints = null;

    //head
    ConvenientBanner banner;
    RelativeLayout relativeDrop,relativeAnswer, relativeGame;
    TextView textViewMoreDuo;
    ImageView imageViewRule;

    List<AdInfo> infos;
    List<String> urls;
    GarbageSortingListAdapter mAdapter;
    private DraggableGridViewPager draggable_grid_view_pager_classifi;
    private DraggableGridViewAdapter mDraggableGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_sorting);

        init();
        getAdvert();
        getGarbageClassify();
    }

    private void init() {
        mAdvertIconList = new int[]{R.drawable.icon_drop_garbage, R.drawable.icon_answer_for_fuli, R.drawable.icon_interesting_game,
                R.drawable.icon_day_score,R.drawable.icon_classify_score, R.drawable.icon_classify_score_ranking};
        mAdvertNameList = new String[]{"垃圾投放", "答题福利", "趣味游戏","每日一袋", "分类得分",
                "得分排名"};

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("垃圾分类");
        listView = (PullToRefreshListView) findViewById(R.id.listView);
        //head
        View mHeader = View.inflate(this, R.layout.garbage_sorting_head_item, null);
        banner = (ConvenientBanner) mHeader.findViewById(R.id.banner);
        imageViewRule = (ImageView) mHeader.findViewById(R.id.imageViewRule);
        draggable_grid_view_pager_classifi = (DraggableGridViewPager) mHeader.findViewById(R.id.draggable_grid_view_pager_classifi);
        initGridView(draggable_grid_view_pager_classifi);
        if (mAdvertIconList.length>8){
            initPoints(llPoint, (int)Math.ceil(mAdvertIconList.length/8));
        }

        /*relativeAnswer = (RelativeLayout) mHeader.findViewById(R.id.relativeAnswer);
        relativeDrop = (RelativeLayout) mHeader.findViewById(R.id.relativeDrop);
        relativeGame = (RelativeLayout) mHeader.findViewById(R.id.relativeGame);*/
        textViewMoreDuo = (TextView) mHeader.findViewById(R.id.textViewMoreDuo);

        listView.getRefreshableView().addHeaderView(mHeader);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);

        /*relativeAnswer.setOnClickListener(this);
        relativeDrop.setOnClickListener(this);
        relativeGame.setOnClickListener(this);*/
        textViewMoreDuo.setOnClickListener(this);
        imageViewRule.setOnClickListener(this);

        urls = new ArrayList<>();
        mAdapter = new GarbageSortingListAdapter(this);
        listView.setAdapter(mAdapter);
    }

    //初始化中间Icon列表指示器
    private void initPoints(LinearLayout llPoint, int num) {
        if (llPoint == null || num == 0) {
            return;
        }
        imgPoints = new ImageView[num];
        for (int i = 0; i < imgPoints.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.dp2px(mContext, 5), Utils.dp2px(mContext, 5));
            params.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.ic_pointer_checked);
            } else {
                imageView.setBackgroundResource(R.drawable.ic_pointer_unchecked);
            }
            imgPoints[i] = imageView;
            llPoint.addView(imageView);
        }
    }

    /**
     * 初始化中间Icon列表
     */
    private void initGridView(DraggableGridViewPager gridViewPager) {
        gridViewPager.setOnItemClickListener(this);
        for (int i = 0; i < mAdvertIconList.length; i++) {
            AdvertInfo advertInfo = new AdvertInfo();
            advertInfo.picId = mAdvertIconList[i];
            if (i < mAdvertNameList.length) {
                advertInfo.name = mAdvertNameList[i];
            }
            mAdvertInfos.add(advertInfo);
        }
        mDraggableGridViewAdapter = new DraggableGridViewAdapter(this, mAdvertInfos);
        draggable_grid_view_pager_classifi.setAdapter(mDraggableGridViewAdapter);
        draggable_grid_view_pager_classifi.setOnPageChangeListener(new DraggableGridViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (imgPoints != null) {
                    for (int i = 0; i < imgPoints.length; i++) {
                        imgPoints[position % imgPoints.length].setBackgroundResource(R.drawable.ic_pointer_checked);
                        if (position % imgPoints.length != i) {
                            imgPoints[i].setBackgroundResource(R.drawable.ic_pointer_unchecked);
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //初始化banner
    private void initBanner() {
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new LocalImageHolderView();
            }
        }, urls)
                .setPageIndicator(new int[]{R.drawable.icon_gray_dot, R.drawable.icon_green_dot})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(3000)
                .notifyDataSetChanged();

        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (infos != null && infos.size() > 0) {
                    if (!TextUtils.isEmpty(infos.get(position).adver_url)) {
                        Intent intent = new Intent(GarbageSortingActivity.this, WebActivity.class);
                        MobclickAgent.onEvent(mContext, infos.get(position).adver_alias); //友盟自定义事件统计
                        intent.putExtra("http_url", infos.get(position).adver_url);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * 获取广告列表
     */
    private void getAdvert() {
        showLoadingDialog("请稍候");
        GoodsApi.get().GetAvd(this, "7", new ApiConfig.ApiRequestListener<BannerInfo>() {
            @Override
            public void onSuccess(String msg, BannerInfo data) {
                dismissLoadingDialog();
                infos = new ArrayList<AdInfo>();
                infos.addAll(data.adList);
                for (int i = 0; i < data.adList.size(); i++) {
                    urls.add(data.adList.get(i).adver_img);
                }
                if (urls.size() > 0) {
                    if (urls.size() == 1) {
                        imageViewRule.setVisibility(View.VISIBLE);
                        banner.setVisibility(View.GONE);
                        ImageloaderUtil.displayImage(GarbageSortingActivity.this, urls.get(0), imageViewRule);
                    } else {
                        imageViewRule.setBackground(null);
                        imageViewRule.setVisibility(View.GONE);
                        banner.setVisibility(View.VISIBLE);
                        initBanner();
                    }
                }

            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showShortToast(msg);
            }
        });
    }

    /**
     * 获取垃圾分类知识列表
     */
    private void getGarbageClassify() {
        GarbageSortingApi.get().ArticleList(mContext, new ApiConfig.ApiRequestListener<CommonList<GarbageClassifyInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<GarbageClassifyInfo> data) {
                mAdapter.clear();
                mAdapter.add(data);
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //垃圾投放
            case R.id.textViewMoreDuo: {
                Intent intent = new Intent(mContext, ClassifyKnowledgeActivity.class);
                intent.putExtra("http_url", mAdapter.getItem(0).url);
                mContext.startActivity(intent);
                break;
            }
            case R.id.imageViewRule: {
                if (infos != null && infos.size() > 0) {
                    if (!TextUtils.isEmpty(infos.get(0).adver_url)) {
                        Intent intent = new Intent(GarbageSortingActivity.this, WebActivity.class);
                        intent.putExtra("http_url", infos.get(0).adver_url);
                        MobclickAgent.onEvent(mContext, infos.get(0).adver_alias); //友盟自定义事件统计
                        startActivity(intent);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (mAdvertNameList[position]){
            case "垃圾投放":
                if (Session.get(this).isLogin()) {
                    startActivity(new Intent(this, DropGarbageActivity.class));
                } else {
                    Utils.showCustomToast(this, "未登录，请登录后再试");
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case "答题福利":
                if (Session.get(this).isLogin()) {
                    startActivity(new Intent(this, LuckyBagActivity.class));
                } else {
                    Utils.showCustomToast(this, "未登录，请登录后再试");
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case "趣味游戏":
                if (Session.get(this).isLogin()) {
                    //startActivity(new Intent(this, ClassifyGameActivity.class));
                    startActivity(new Intent(this, ClassifyGameCompetitionActivity.class));
                } else {
                    Utils.showCustomToast(this, "未登录，请登录后再试");
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case "每日一袋":
                if (Session.get(getApplication()).isLogin()) {

                    if (UserInfo.get().getIsbind().equals("0")) {
                        CustomDialog dialog = new CustomDialog.Builder(this)
                                .setTitle("提示")
                                .setMessage("请先绑定小区设备！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                        dialog.show();
                        return;
                    }

                    if (UserInfo.get().isScored.equals("0")) {
                        showCustomToast("您所在的小区暂时还没有开通评分功能！");
                        return;
                    }

                    CustomDialog dialog = new CustomDialog.Builder(mContext)
                            .setTitle("个人评分细则")
                            .setMessage(getString(R.string.str_personal_grade))
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(mContext, PersonalGradesActivity.class));
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create2();
                    dialog.show();

                    //startActivity(ClassifyGameActivity.class);
                } else {
                    Utils.showCustomToast(getApplication(), "未登录，请登录后再试");
                    startActivity(LoginActivity.class);
                }
                break;
            case "分类得分":
                if (Session.get(this).isLogin()) {
                    startActivity(new Intent(this, RecycleScoreRecordActivity.class));
                } else {
                    Utils.showCustomToast(this, "未登录，请登录后再试");
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case "得分排名":
                if (!Session.get(this).isLogin()) {
                    Utils.showCustomToast(this, "未登录，请登录后再试");
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                if (UserInfo.get().getIsbind().equals("0")) {
                    CustomDialog dialog = new CustomDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("请先绑定小区设备！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                    return;
                }

                /*if (UserInfo.get().isScored.equals("0")) {
                    showCustomToast("您所在的小区暂时还没有开通评分功能！");
                    return;
                }*/

                //startActivity(new Intent(this, ClassifyGrabgePankingDateChooseActivity.class));
                startActivity(new Intent(this, ClassifyGarbageScoreRankingActivity.class));
                //showCustomToast("“垃圾分类得分排名”功能待增加！");

                break;
           /* case 5:

                break;
            case 6:

                break;
            case 7:

                break;*/
        }
    }
}
