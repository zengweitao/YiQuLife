package com.quanmai.yiqu.ui.classifigarbage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.ClassifyGameApi;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.vo.AdInfo;
import com.quanmai.yiqu.api.vo.BannerInfo;
import com.quanmai.yiqu.api.vo.ClassifyGameMessageInfo;
import com.quanmai.yiqu.api.vo.ClassifyGameRankingInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.XCRoundImageView;
import com.quanmai.yiqu.ui.adapter.LocalImageHolderView;
import com.quanmai.yiqu.ui.classifigarbage.adapter.ClassifyGameCompetitionRankingAdapter;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.login.LoginActivity;
import com.quanmai.yiqu.ui.views.CostomScrollView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 垃圾分类游戏及竞赛排名类
 */
public class ClassifyGameCompetitionActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private ImageView iv_back, imageViewRule_classify_game;
    private PullToRefreshListView listView_classify_game_ranking;
    private ConvenientBanner banner_classify_game;
    private Button button_classify_game_start;
    private XCRoundImageView imageViewHeadPortrait_game;
    private TextView textview_ranking_user;
    private TextView textview_score, textview_competition_message, textview_game_msg, textViewshoucount;
    List<AdInfo> infos;
    List<String> urls;
    CommonList<ClassifyGameRankingInfo.ContestRankListInfo> datas;
    private LinearLayout linear_no_data, linear_firstuser;
    private ClassifyGameCompetitionRankingAdapter rankingAdapter;
    private ScrollView scrollview_game_msg;
    private Button image_rank_add, image_rank_minus;

    int current = 0; //第几次排名
    int count = 1; //共有几次排名信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_game_competition);
        showLoadingDialog("请稍后！");
        init();
        getAdvert();
        if (!UserInfo.get().getIsbind().equals("1") && !UserInfo.get().getIsbind().equals("2")) {
            textview_competition_message.setText(R.string.str_game_notbind);
        } else {
            getClassifyGameMessageData();
            image_rank_add.setVisibility(View.INVISIBLE);
            getClassifyGameRankingData(0 + "");
        }
    }

    /**
     * 初始化控件
     */
    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("垃圾分类知识竞赛");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        listView_classify_game_ranking = (PullToRefreshListView) findViewById(R.id.listView_classify_game_ranking);

        //head
        View mHeader = View.inflate(this, R.layout.classify_game_competition_head_item, null);
        banner_classify_game = (ConvenientBanner) mHeader.findViewById(R.id.banner_classify_game);
        imageViewRule_classify_game = (ImageView) mHeader.findViewById(R.id.imageViewRule_classify_game);
        button_classify_game_start = (Button) mHeader.findViewById(R.id.button_classify_game_start);
        imageViewHeadPortrait_game = (XCRoundImageView) mHeader.findViewById(R.id.imageViewHeadPortrait_game);
        //imageViewHeadPortrait_game.setImageResource(R.drawable.default_header);
        imageViewHeadPortrait_game.setBorderWidth(Utils.dp2px(mContext, 0));
        imageViewHeadPortrait_game.setBorderColor(Color.WHITE);
        textview_ranking_user = (TextView) mHeader.findViewById(R.id.textview_ranking_user);
        textview_score = (TextView) mHeader.findViewById(R.id.textview_score);
        textview_competition_message = (TextView) mHeader.findViewById(R.id.textview_competition_message);
        scrollview_game_msg = (ScrollView) mHeader.findViewById(R.id.scrollview_game_msg);
        textview_competition_message.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    listView_classify_game_ranking.requestDisallowInterceptTouchEvent(false);
                    scrollview_game_msg.requestDisallowInterceptTouchEvent(false);
                } else {
                    listView_classify_game_ranking.requestDisallowInterceptTouchEvent(true);
                    scrollview_game_msg.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        textview_game_msg = (TextView) mHeader.findViewById(R.id.textview_game_msg);
        textview_game_msg.setText("相关信息");

        image_rank_add = (Button) mHeader.findViewById(R.id.image_rank_add);
        image_rank_minus = (Button) mHeader.findViewById(R.id.image_rank_minus);
        textViewshoucount = (TextView) mHeader.findViewById(R.id.textViewshoucount);

        linear_no_data = (LinearLayout) mHeader.findViewById(R.id.linear_no_data);
        linear_firstuser = (LinearLayout) mHeader.findViewById(R.id.linear_firstuser);

        listView_classify_game_ranking.getRefreshableView().addHeaderView(mHeader);
        listView_classify_game_ranking.setMode(PullToRefreshBase.Mode.PULL_FROM_START);


        iv_back.setOnClickListener(this);
        button_classify_game_start.setOnClickListener(this);
        image_rank_add.setOnClickListener(this);
        image_rank_minus.setOnClickListener(this);

        urls = new ArrayList<>();
        rankingAdapter = new ClassifyGameCompetitionRankingAdapter(this);
        listView_classify_game_ranking.setAdapter(rankingAdapter);

        listView_classify_game_ranking.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                rankingAdapter.clear();
                current = 0;
                getAdvert();
                if (!UserInfo.get().getIsbind().equals("0")) {
                    getClassifyGameMessageData();
                    image_rank_add.setVisibility(View.INVISIBLE);
                    image_rank_minus.setVisibility(View.VISIBLE);
                    getClassifyGameRankingData(0 + "");
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

    }

    //初始化banner
    private void initBanner() {
        banner_classify_game.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new LocalImageHolderView();
            }
        }, urls)
                .setPageIndicator(new int[]{R.drawable.icon_gray_dot, R.drawable.icon_green_dot})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(3000)
                .notifyDataSetChanged();

        banner_classify_game.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (infos != null && infos.size() > 0) {
                    if (!TextUtils.isEmpty(infos.get(position).adver_url)) {
                        Intent intent = new Intent(ClassifyGameCompetitionActivity.this, WebActivity.class);
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
        GoodsApi.get().GetAvd(this, "20", new ApiConfig.ApiRequestListener<BannerInfo>() {
            @Override
            public void onSuccess(String msg, BannerInfo data) {
                dismissLoadingDialog();
                infos = new ArrayList<>();
                infos.addAll(data.adList);
                for (int i = 0; i < data.adList.size(); i++) {
                    urls.add(data.adList.get(i).adver_img);
                }
                if (urls.size() > 0) {
                    if (urls.size() == 1) {
                        imageViewRule_classify_game.setVisibility(View.VISIBLE);
                        banner_classify_game.setVisibility(View.GONE);
                        ImageloaderUtil.displayImage(ClassifyGameCompetitionActivity.this, urls.get(0), imageViewRule_classify_game);
                    } else {
                        imageViewRule_classify_game.setBackground(null);
                        imageViewRule_classify_game.setVisibility(View.GONE);
                        banner_classify_game.setVisibility(View.VISIBLE);
                        initBanner();
                    }
                } else {
                    imageViewRule_classify_game.setVisibility(View.VISIBLE);
                    imageViewRule_classify_game.setImageResource(R.drawable.banner_game_header);
                    banner_classify_game.setVisibility(View.GONE);
                }
                listView_classify_game_ranking.onRefreshComplete();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                //showShortToast(msg);
                imageViewRule_classify_game.setVisibility(View.VISIBLE);
                imageViewRule_classify_game.setImageResource(R.drawable.banner_game_header);
                banner_classify_game.setVisibility(View.GONE);
                listView_classify_game_ranking.onRefreshComplete();
            }
        });
    }


    /**
     * 获取游戏竞赛排名信息
     */
    public void getClassifyGameRankingData(String current) {
        ClassifyGameApi.get().getClassifyGameCompeititonRanking(this, current, new ApiConfig.ApiRequestListener<ClassifyGameRankingInfo>() {
            @Override
            public void onSuccess(String msg, ClassifyGameRankingInfo data) {
                dismissLoadingDialog();
                CommonList<ClassifyGameRankingInfo.ContestRankListInfo> datas = new CommonList<>();
                if (data != null) {
                    count = data.getCount();
                    rankingAdapter.clear();
                    if (data.getContestRankList() != null && data.getContestRankList().size() > 0) {
                        linear_no_data.setVisibility(View.GONE);
                        linear_firstuser.setVisibility(View.VISIBLE);
                        for (int i = 1; i < data.getContestRankList().size(); i++) {
                            datas.add(data.getContestRankList().get(i));
                        }
                        rankingAdapter.add(datas);
                        if (!data.getContestRankList().get(0).getImgUrl().equals("")) {
                            ImageloaderUtil.displayImage(mContext, data.getContestRankList().get(0).getImgUrl(),
                                    imageViewHeadPortrait_game,
                                    ImageloaderUtil.getDisplayImageOptions(R.drawable.default_header, R.drawable.default_header, R.drawable.default_header));
                        } else {
                            imageViewHeadPortrait_game.setImageResource(R.drawable.default_header);
                        }
                        textview_ranking_user.setText(data.getContestRankList().get(0).getAccount());
                        textview_score.setText(data.getContestRankList().get(0).getMaxScore());
                    } else {
                        linear_firstuser.setVisibility(View.GONE);
                        linear_no_data.setVisibility(View.VISIBLE);
                    }
                }
                listView_classify_game_ranking.onRefreshComplete();

            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                linear_firstuser.setVisibility(View.GONE);
                linear_no_data.setVisibility(View.VISIBLE);
                listView_classify_game_ranking.onRefreshComplete();
            }
        });
    }

    /**
     * 获取游戏竞赛信息
     */
    public void getClassifyGameMessageData() {
        ClassifyGameApi.get().getClassifyGameCompetitionMessage(this, new ApiConfig.ApiRequestListener<ClassifyGameMessageInfo>() {
            @Override
            public void onSuccess(String msg, ClassifyGameMessageInfo data) {
                dismissLoadingDialog();
                if (data != null) {
                    textview_game_msg.setText("相关信息(" + data.getGameStatusDes() + ")");
                    String str = null;
                    if (UserInfo.get().isGameContest.equals("1")) {
                        if (data.getStatus().equals("1")) {
                            str = "开放小区：" + data.getCommname() + "\n开放时间：" + data.getStartTime() +
                                    "至" + data.getEndTime() + "\n游戏介绍：" + data.getDescription();
                        } else {
                            str = "\t\t本小区近期没有游戏竞赛相关活动！";
                        }

                    } else {
                        str = "\t\t该小区暂未开通游戏竞赛活动！";
                    }

                    textview_competition_message.setText(str);
                }
                listView_classify_game_ranking.onRefreshComplete();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                //showCustomToast(msg);
                textview_competition_message.setText("\t\t没有您的游戏竞赛相关信息！");
                listView_classify_game_ranking.onRefreshComplete();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.button_classify_game_start:
                if (Session.get(this).isLogin()) {
                    startActivity(new Intent(this, ClassifyGameActivity.class));
                } else {
                    Utils.showCustomToast(this, "未登录，请登录后再试");
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.image_rank_add:
                image_rank_minus.setVisibility(View.VISIBLE);
                current--;
                getClassifyGameRankingData(current + "");
                if (current == 0) {
                    image_rank_add.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.image_rank_minus:
                image_rank_add.setVisibility(View.VISIBLE);
                current++;
                getClassifyGameRankingData(current + "");
                if (current == count - 1) {
                    image_rank_minus.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}
