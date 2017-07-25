package com.quanmai.yiqu.ui.mys.record;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.CheckInApi;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.mys.record.view.QManHourAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 签到记录
 */
public class SignRecordActivity extends BaseActivity implements OnClickListener {

    QManHourAdapter manHourAdapter;
    PullToRefreshListView mListView;
    TextView textViewYear, textViewMyScore, textViewContinuation, textViewTotal;
    TextView textViewRule1, textViewRule2;

    private int mYearNum, mMonthNum;
    List<String> httpUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_record);

        init();
        getSignRecord();
    }

    private void init() {
        ((TextView) findViewById(R.id.tv_title)).setText("签到记录");
        mListView = (PullToRefreshListView) findViewById(R.id.list);
        manHourAdapter = new QManHourAdapter(mContext, null);

        Calendar calendar = Calendar.getInstance();
        mYearNum = calendar.get(Calendar.YEAR);       //Calendar.YEAR是属性
        mMonthNum = calendar.get(Calendar.MONTH) + 1;

        View viewHeader = View.inflate(this, R.layout.view_sign_record_header, null);
        textViewRule1 = (TextView) viewHeader.findViewById(R.id.textViewRule1);
        textViewRule2 = (TextView) viewHeader.findViewById(R.id.textViewRule2);
        textViewYear = (TextView) viewHeader.findViewById(R.id.textViewYear);
        textViewYear.setText(mYearNum + "." + mMonthNum);
        textViewMyScore = (TextView) viewHeader.findViewById(R.id.textViewMyScore);
        textViewMyScore.setText("我的积分 " + String.valueOf(UserInfo.get().getScore()));
        textViewContinuation = (TextView) viewHeader.findViewById(R.id.textViewContinuation);
        textViewTotal = (TextView) viewHeader.findViewById(R.id.textViewTotal);
        viewHeader.findViewById(R.id.imageViewLastMonth).setOnClickListener(this);
        viewHeader.findViewById(R.id.imageViewNextMonth).setOnClickListener(this);

        mListView.getRefreshableView().addHeaderView(viewHeader);
        mListView.setAdapter(manHourAdapter);
        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    /**
     * 获取签到记录
     */
    private void getSignRecord() {
        showLoadingDialog("请稍候");
        manHourAdapter.clear();
        CheckInApi.get().SignRecord(mContext, mYearNum, mMonthNum, new ApiRequestListener<ManHour>() {
            @Override
            public void onSuccess(String msg, ManHour data) {
                dismissLoadingDialog();
                String[] msgs = msg.split("#");
                textViewContinuation.setText(msgs[1] + "天");
                textViewTotal.setText(msgs[2] + "天");
                textViewRule1.setText("1.每日签到可获得" + msgs[3] + "积分");
                textViewRule2.setText("2.连续签到3日可额外获得" + msgs[3] + "积分");

                ArrayList<ManHour> newList = new ArrayList<ManHour>();
                if (data != null) {
                    newList.add(data);
                } else {
                    newList.add(new ManHour(mYearNum, mMonthNum));
                }
                manHourAdapter.clear();
                manHourAdapter.add(newList);
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
                dismissLoadingDialog();
                ArrayList<ManHour> newList = new ArrayList<ManHour>();
                newList.add(new ManHour(mYearNum, mMonthNum));
                manHourAdapter.clear();
                manHourAdapter.add(newList);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewLastMonth: {
                if (mMonthNum == 1) {
                    mYearNum--;
                    mMonthNum = 12;
                } else {
                    mMonthNum--;
                }
                textViewYear.setText(mYearNum + "." + mMonthNum);
                getSignRecord();
                break;
            }
            case R.id.imageViewNextMonth: {
                if (mMonthNum == 12) {
                    mYearNum++;
                    mMonthNum = 1;
                } else {
                    mMonthNum++;
                }
                textViewYear.setText(mYearNum + "." + mMonthNum);
                getSignRecord();
                break;
            }
            case R.id.imageViewRule: {
                if (httpUrl.size() > 0) {
                    Intent intent = new Intent(SignRecordActivity.this, WebActivity.class);
                    intent.putExtra("http_url", httpUrl.get(0));
                    startActivity(intent);
                }
                break;
            }
            default:
                break;
        }
    }

}
