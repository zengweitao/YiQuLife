package com.quanmai.yiqu.ui.classifigarbage;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.ClassifyScoreRankingInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.widget.DateSelectionDialog;
import com.quanmai.yiqu.ui.classifigarbage.adapter.ClassifyGarbageScoreRankingAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 垃圾分类得分排名页面
 */
public class ClassifyGarbageScoreRankingActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_back,imageViewLastMonth,imageViewNextMonth;
    private TextView textViewDate,textViewCount,textViewGarbageRecycle,textViewGarbageThrow;
    private PullToRefreshListView listview_score_ranking;
    private ClassifyGarbageScoreRankingAdapter mAdapter;
    private DateSelectionDialog dateSelectionDialog;
    private LinearLayout linearLayoutDate;
    String datetime="";
    int year,month,day;
    private Calendar calendar;
    int isMonthOrDay=0;
    private String systimestr;
    private LinearLayout linear_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_garbage_score_ranking);
        getSystemTime();
        init();
        getScoreRankingDatas(datetime);
    }

    private void init() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        imageViewLastMonth = (ImageView) findViewById(R.id.imageViewLastMonth);
        imageViewNextMonth = (ImageView) findViewById(R.id.imageViewNextMonth);

        textViewGarbageRecycle = (TextView) findViewById(R.id.textViewGarbageRecycle);
        textViewGarbageThrow = (TextView) findViewById(R.id.textViewGarbageThrow);
        //点击事件
        textViewGarbageRecycle.setOnClickListener(this);
        textViewGarbageThrow.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        imageViewLastMonth.setOnClickListener(this);
        imageViewNextMonth.setOnClickListener(this);

        linear_no_data = (LinearLayout) findViewById(R.id.linear_no_data);

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewDate.setText(systimestr.substring(0,4)+"."+systimestr.substring(5,7));
        textViewCount = (TextView) findViewById(R.id.textViewCount);
        textViewCount.setText(UserInfo.get().getCommunity());

        listview_score_ranking = (PullToRefreshListView) findViewById(R.id.listview_score_ranking);
        linear_no_data.setVisibility(View.VISIBLE);
        listview_score_ranking.setEmptyView(linear_no_data);
        //listview_score_ranking.setMode(PullToRefreshBase.Mode.BOTH);
        listview_score_ranking.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isMonthOrDay==0){
                    getScoreRankingDatas(datetime);
                }else {
                    getScoreRankingDatas(systimestr);
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        mAdapter = new ClassifyGarbageScoreRankingAdapter(this);
        listview_score_ranking.setAdapter(mAdapter);
    }

    public void getScoreRankingDatas(String date){
        showLoadingDialog("请稍候");
        GradeApi.get().ClassifyScoreRanking(this, date, new ApiConfig.ApiRequestListener<CommonList<ClassifyScoreRankingInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<ClassifyScoreRankingInfo> data) {
                linear_no_data.setVisibility(View.GONE);
                dismissLoadingDialog();
                showCustomToast(msg);
                listview_score_ranking.onRefreshComplete();
                mAdapter.clear();
                mAdapter.add(data);
            }

            @Override
            public void onFailure(String msg) {
                listview_score_ranking.onRefreshComplete();
                showCustomToast(msg);
                dismissLoadingDialog();
            }
        });
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.textViewGarbageThrow:
                isMonthOrDay=0;
                mAdapter.clear();
                if (month<10){
                    textViewDate.setText(year + ".0" + month);
                    mAdapter.clear();
                    datetime=year+"-0"+month;
                    getScoreRankingDatas(datetime);
                }else {
                    textViewDate.setText(year + "." + month);
                    mAdapter.clear();
                    datetime=year+"-"+month;
                    getScoreRankingDatas(datetime);
                }
                imageViewLastMonth.setVisibility(View.VISIBLE);
                imageViewNextMonth.setVisibility(View.VISIBLE);
                textViewGarbageRecycle.setBackgroundResource(R.drawable.bg_booking_right_fill);
                textViewGarbageRecycle.setTextColor(Color.parseColor("#ffffff"));
                textViewGarbageThrow.setBackgroundResource(R.drawable.bg_booking_left);
                textViewGarbageThrow.setTextColor(getResources().getColor(R.color.theme));
                break;
            case R.id.textViewGarbageRecycle:
                isMonthOrDay=1;
                getSystemTime();
                textViewDate.setText(systimestr.substring(0,4)+"."+systimestr.substring(5,7)
                        +"."+systimestr.substring(8,10));
                imageViewLastMonth.setVisibility(View.GONE);
                imageViewNextMonth.setVisibility(View.GONE);
                textViewGarbageRecycle.setBackgroundResource(R.drawable.bg_booking_right);
                textViewGarbageRecycle.setTextColor(mContext.getResources().getColor(R.color.theme));
                textViewGarbageThrow.setBackgroundResource(R.drawable.bg_booking_left_fill);
                textViewGarbageThrow.setTextColor(Color.parseColor("#ffffff"));
                mAdapter.clear();
                getScoreRankingDatas(systimestr);
                break;
            case R.id.imageViewLastMonth:
                MonthChange(false);
                if (month<10){
                    textViewDate.setText(year + ".0" + month);
                    mAdapter.clear();
                    datetime=year+"-0"+month;
                    getScoreRankingDatas(datetime);
                }else{
                    textViewDate.setText(year + "." + month);
                    mAdapter.clear();
                    datetime=year+"-"+month;
                    getScoreRankingDatas(datetime);
                }
                break;
            case R.id.imageViewNextMonth:
                MonthChange(true);
                if (month<10){
                    textViewDate.setText(year + ".0" + month);
                    mAdapter.clear();
                    datetime=year+"-0"+month;
                    getScoreRankingDatas(datetime);
                }else {
                    textViewDate.setText(year + "." + month);
                    mAdapter.clear();
                    datetime=year+"-"+month;
                    getScoreRankingDatas(datetime);
                }
                break;
        }
    }
}
