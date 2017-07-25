package com.quanmai.yiqu.ui.code;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.vo.BagInfo;
import com.quanmai.yiqu.api.vo.ScorePayInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.CircularImageView;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;

/**
 * 换袋第二步
 *
 * @author kiwi
 */
public class ChangeBagSecondStepActivity extends BaseActivity implements OnClickListener {

    TextView tvFedNum;          //福袋数
    TextView tvScore;           //积分
    TextView tv_count;           //积分
    TextView tvSurplusFedNum;   //剩余福袋数
    TextView tvSurplusScore;    //剩余积分
    TextView tvFedNumUnit;      //福袋单位（**福袋）
    TextView tvScoreUnit;       //积分单位（**积分）
    TextView tvSurplusFedNumUnit;   //剩余福袋单位（**福袋）
    TextView tvSurplusScoreUnit;    //剩余积分单位（**积分）
    CircularImageView ivPrompt;             //提示图片
    TextView tvTitle;

    private BagInfo bagInfo; //垃圾堆信息
    private int surplusPoint; //用户剩余积分
    private int surplusFedNum; //用户剩余福袋
    private String terminalno;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bag_second_step);
        initView();
        init();
    }

    private void initView() {
        findViewById(R.id.lt_back).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_back).setVisibility(View.GONE);
        findViewById(R.id.btn_confirm).setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("领取环保袋");

        ivPrompt = (CircularImageView) findViewById(R.id.ivPrompt);
        tvFedNum = (TextView) findViewById(R.id.tvFedNum);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tvSurplusFedNum = (TextView) findViewById(R.id.tvSurplusFedNum);
        tvSurplusScore = (TextView) findViewById(R.id.tvSurplusScore);
        tvFedNumUnit = (TextView) findViewById(R.id.tvFedNumUnit);
        tvScoreUnit = (TextView) findViewById(R.id.tvScoreUnit);
        tvSurplusFedNumUnit = (TextView) findViewById(R.id.tvSurplusFedNumUnit);
        tvSurplusScoreUnit = (TextView) findViewById(R.id.tvSurplusScoreUnit);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
    }

    private void init() {
        bagInfo = (BagInfo) getIntent().getSerializableExtra("baginfo");
        surplusPoint = getIntent().getIntExtra("surplusPoint", bagInfo.point);
        surplusFedNum = getIntent().getIntExtra("surplusFedNum", bagInfo.fednum);
        terminalno = getIntent().getStringExtra("terminalno");

        if (getIntent().hasExtra("type")) {
            if (getIntent().getStringExtra("type").equals("changePaper")) {
                tvTitle.setText("领取纸巾");
                pay(1, "2");
            }
        } else if (bagInfo != null) {
//            if (bagInfo.fednum - surplusFedNum > 0) {
//                tvFedNum.setVisibility(View.VISIBLE);
//                tvFedNumUnit.setVisibility(View.VISIBLE);
//                tvFedNum.setText(bagInfo.fednum - surplusFedNum + "");
//                tvSurplusFedNum.setText(surplusFedNum + "");
//            }
//            if (bagInfo.point - surplusPoint > 0) {
//                tvScore.setVisibility(View.VISIBLE);
//                tvScoreUnit.setVisibility(View.VISIBLE);
//                tvScore.setText(bagInfo.point - surplusPoint + "");
//                tvSurplusScore.setText(surplusPoint + "");
//            }

            if (!TextUtils.isEmpty(bagInfo.img)) {
                ImageloaderUtil.displayImage(mContext, bagInfo.img, ivPrompt, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        ivPrompt.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_change_bag_graphic));
                    }
                });
            }
        }

        //调整图片比例
        ivPrompt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                float width = 0.0f;
                float heigh = 0.0f;
                if ((float) ivPrompt.getWidth() / (float) ivPrompt.getHeight() >= 345.00 / 410.00) {
                    width = (float) ivPrompt.getHeight() * 345 / 410;
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) width, ViewGroup.LayoutParams.MATCH_PARENT);
                    ivPrompt.setLayoutParams(layoutParams);
                } else {
                    heigh = (float) ivPrompt.getWidth() * 410 / 345;
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) heigh);
                    ivPrompt.setLayoutParams(layoutParams);
                }

                ivPrompt.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * 积分商品支付(“纸要你”)
     */
    private void pay(int count, String abscription) {
        showLoadingDialog("请稍候");
        IntegralApi.get().ScorePay(mContext, terminalno, count, abscription,
                new ApiConfig.ApiRequestListener<ScorePayInfo>() {

                    @Override
                    public void onSuccess(String msg, ScorePayInfo data) {
                        dismissLoadingDialog();
                        tv_count.setText("取纸成功后每份纸将消耗5积分");
                        /*tvScore.setVisibility(View.VISIBLE);
                        tvScoreUnit.setVisibility(View.VISIBLE);
                        tvScore.setText(bagInfo.score + "");
                        tvSurplusScore.setText(bagInfo.point - bagInfo.score + "");*/
                        ImageloaderUtil.displayImage(mContext, data.successimg, ivPrompt, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                ivPrompt.setBackgroundResource(R.drawable.bg_change_papper);
                            }
                        });
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
            case R.id.btn_confirm: {
                finish();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(PersonalFragment.ACTION_NETWORKING_TO_REFRESH_DATA));
    }
}
