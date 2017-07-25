package com.quanmai.yiqu.ui.mys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.UserInfoApi;
import com.quanmai.yiqu.api.vo.Member;
import com.quanmai.yiqu.api.vo.SignData;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.SignInADPopupWindow;
import com.quanmai.yiqu.common.widget.SignInPopupWindow;
import com.quanmai.yiqu.common.widget.XCRoundImageView;
import com.quanmai.yiqu.ui.code.MipcaActivityCapture;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;
import com.quanmai.yiqu.ui.integration.IntegralDetailsActivity;
import com.quanmai.yiqu.ui.publish.PublishUnusedActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/**
 * 会员详情
 */
public class VipDetailActivity extends BaseActivity implements OnClickListener {

    XCRoundImageView imageViewHead;
    ImageView imageViewSex;
    TextView textViewName, textViewGrowthValue, textViewScore;

    TextView textViewTotalIntegral, textViewLevelRemain;
    ProgressBar progressBarIntegral;
    Button buttonSignIn, buttonLevel;
    LinearLayout llLevelPrivilege;
    RadioGroup radioGroupLevel;
    TextView tv_right;
    SignInPopupWindow popupWindow;
    SignInADPopupWindow mSignInADPopupWindow; //签到成功弹窗（广告）

    static int[] LEVELS = new int[]{0, 1000, 2000, 4000, 6000, 8000}; //1～6级，各等级最小积分
    CommonList<Member> mMembers;   //会员等级实体类列表
    private TextView tvExplanationSign;
    private TextView tvExplanationPublish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_detail);
        initView();

        init();
        getGrowIntegral();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeadData();
        getVipIntroduce();
    }

    private void init() {
        ((TextView) findViewById(R.id.tv_title)).setText("会员详情");
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("积分详情");
        imageViewHead = (XCRoundImageView) findViewById(R.id.imageViewHeadPortrait);
        imageViewHead.setImageResource(R.drawable.default_header);
        imageViewHead.setBorderWidth(Utils.dp2px(mContext, 3));
        imageViewHead.setBorderColor(0x4BFFFFFF);
        buttonLevel = (Button) findViewById(R.id.tvLevel);
        imageViewSex = (ImageView) findViewById(R.id.imageViewSex);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewGrowthValue = (TextView) findViewById(R.id.textViewLeft);
        textViewScore = (TextView) findViewById(R.id.textViewRight);

        textViewTotalIntegral = (TextView) findViewById(R.id.textViewTotalIntegral);
        textViewLevelRemain = (TextView) findViewById(R.id.textViewLevelRemain);
        progressBarIntegral = (ProgressBar) findViewById(R.id.progressBarIntegral);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        llLevelPrivilege = (LinearLayout) findViewById(R.id.linearLayoutLevelPrivilege);
        radioGroupLevel = (RadioGroup) findViewById(R.id.radioGroupLevel);


        textViewScore.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        findViewById(R.id.buttonSignIn).setOnClickListener(this);
        findViewById(R.id.buttonPublish).setOnClickListener(this);
        findViewById(R.id.buttonFetchPag).setOnClickListener(this);
        findViewById(R.id.relativeLayoutKnowLevel).setOnClickListener(this);
    }

    //初始化头部数据
    private void initHeadData() {
        if (imageViewHead.getTag() == null || !imageViewHead.getTag().equals(UserInfo.get().face)) {
            imageViewHead.setTag(UserInfo.get().face);
            ImageloaderUtil.displayImage(mContext, UserInfo.get().face, imageViewHead);
        }

        if (TextUtils.isEmpty(UserInfo.get().alias)) {
            textViewName.setText("未设置");
        } else {
            textViewName.setText(UserInfo.get().alias);
        }
        if (UserInfo.get().vipInfo.level_name.contains("LV")){
            buttonLevel.setVisibility(View.VISIBLE);
            buttonLevel.setText(UserInfo.get().vipInfo.level_name);
        }
        switch (UserInfo.get().getSex()) {
            case 0: { //未设置
                imageViewSex.setVisibility(View.GONE);
                break;
            }
            case 1: { //男
                imageViewSex.setVisibility(View.VISIBLE);
                imageViewSex.setBackgroundResource(R.drawable.ic_sex_man);
                break;
            }
            case 2: { //女
                imageViewSex.setVisibility(View.VISIBLE);
                imageViewSex.setBackgroundResource(R.drawable.ic_sex_women);
                break;
            }
            default:
                break;
        }
    }

    //初始化等级数据
    private void initLevelData() {
        int growthValue = UserInfo.get().growthvalue;
        String strGrowthValue = String.valueOf(growthValue);
        int needGrowthValue; //距离下一等级所需积分
        int currentLevel = 1;

        for (int i = 0; i < LEVELS.length; i++) {
            if (growthValue > LEVELS[i]) {
                continue;
            } else if (growthValue <= LEVELS[i]) {
                currentLevel = i - 1 + 1;
                break;
            }
        }

        //成长值超过最高等级积分，设为最高等级
        if (growthValue >= LEVELS[LEVELS.length - 1]) {
            textViewTotalIntegral.setText("成长值已满");
            textViewLevelRemain.setText("恭喜您等级已满");
            textViewTotalIntegral.setTextColor(getResources().getColor(R.color.theme));
            textViewLevelRemain.setTextColor(getResources().getColor(R.color.theme));
            currentLevel = LEVELS.length;
        } else {
            needGrowthValue = (LEVELS[currentLevel] - growthValue) > 0 ? LEVELS[currentLevel] - growthValue : 0; //成长值超过最高等级积分，需要成长值为“0”
            String strNeedGrowthValue = String.valueOf(needGrowthValue);

            //更改局部字体颜色
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(strGrowthValue + "/" + (currentLevel >= 6 ? 0 : LEVELS[currentLevel]));
            strBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#48c299")), 0, 0 + strGrowthValue.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            textViewTotalIntegral.setText(strBuilder);

            strBuilder = new SpannableStringBuilder("升级至Lv" + (currentLevel >= 6 ? 6 : (currentLevel + 1)) + "还需" + strNeedGrowthValue + "成长值");
            strBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#48c299")), 8, 8 + strNeedGrowthValue.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            textViewLevelRemain.setText(strBuilder);
        }


        textViewGrowthValue.setText("成长值：" + String.valueOf(UserInfo.get().growthvalue));
        textViewScore.setText("积分：" + String.valueOf(UserInfo.get().score));

        //进度条设置
        float currentProgress = 0f;
        if (growthValue >= LEVELS[LEVELS.length - 1]) {
            //成长值超过最高等级积分，进度条填充全部长度
            currentProgress = progressBarIntegral.getMax();
        } else {
            //成长值未超过最高等级积分
            for (int i = 0; i < LEVELS.length - 1; i++) {
                if (growthValue >= LEVELS[i]) {
                    //当前积分 / * 进度条最大值 * (区间宽度/ 5)/下一个等级积分;
                    currentProgress = growthValue * progressBarIntegral.getMax() * (i + 1) / 5 / LEVELS[i + 1];
                }
            }
        }
        progressBarIntegral.setProgress((int) currentProgress);

        //当前等级图标
        Button currentBtn = (Button) radioGroupLevel.getChildAt((currentLevel - 1) * 2);
        if (currentBtn != null) {
            currentBtn.setTextColor(Color.WHITE);
            currentBtn.setBackgroundResource(R.drawable.bg_button_orange);
        }

        //签到
        if (UserInfo.get().sign == 1) {
            buttonSignIn.setText("已签到");
            buttonSignIn.setTextColor(Color.WHITE);
            buttonSignIn.setBackgroundResource(R.drawable.bg_btn_gray);
            buttonSignIn.setClickable(false);
        } else {
            buttonSignIn.setText("去签到");
            buttonSignIn.setTextColor(getResources().getColor(R.color.theme));
            buttonSignIn.setBackgroundResource(R.drawable.bg_btn_green_stroke);
        }

        //等级特权
        if (!UserInfo.get().level_introducton.equals("")) {
            String[] introductions = UserInfo.get().level_introducton.split("\r\n");
            if (introductions.length > 0) {
                llLevelPrivilege.removeAllViews();
                for (int i = 0; i < introductions.length; i++) {
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View view = inflater.inflate(R.layout.view_level_introduce, null);
                    TextView tvLevelPrivilege = (TextView) view.findViewById(R.id.textViewLevelIntroduce);
                    tvLevelPrivilege.setText(introductions[i]);
                    llLevelPrivilege.addView(view);
                }
            }
        }

    }

    //获取用户详细信息
    private void getUserDetail() {
        UserInfoApi.get().getUserHome(mContext, new ApiConfig.ApiRequestListener<UserInfo>() {
            @Override
            public void onSuccess(String msg, UserInfo data) {
                dismissLoadingDialog();
                initHeadData();
                initLevelData();

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(PersonalFragment.ACTION_REFRESH_PAGE));
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //获取积分规则和了解特权介绍
    private void getVipIntroduce() {
        showLoadingDialog("请稍候");
        IntegralApi.get().getVipIntroduce(mContext, new ApiConfig.ApiRequestListener<CommonList<Member>>() {
            @Override
            public void onSuccess(String msg, CommonList<Member> data) {
                dismissLoadingDialog();
                if (data == null || data.size() == 0) {
                    Log.i("VipDetailActivity--->", "会员等级说明获取失败");
                    return;
                }

                mMembers = data;
                getIntent().putExtra("members", mMembers);
                for (int i = 0; i < data.size(); i++) {
                    Member member = data.get(i);
                    LEVELS[i] = member.lowpoint;
                }
                initLevelData();
            }

            @Override
            public void onFailure(String msg) {
                showShortToast(msg);
                dismissLoadingDialog();
            }
        });
    }

    //积分成长说明
    private void getGrowIntegral() {
        IntegralApi.get().getGrowIntegral(mContext, "app", new ApiConfig.ApiRequestListener<Map<String, String>>() {
            @Override
            public void onSuccess(String msg, Map<String, String> map) {
                if (map == null) {
                    return;
                }
                if (map.get("每日签到") != null) {
                    tvExplanationSign.setText(map.get("每日签到"));
                    tvExplanationSign.setVisibility(View.VISIBLE);
                }
                if (map.get("发布闲置物品") != null) {
                    tvExplanationPublish.setText(map.get("发布闲置物品"));
                    tvExplanationPublish.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private void showPicDialog(String signpoint, String signDay, String score, String level) {
        popupWindow = new SignInPopupWindow(this);
        popupWindow.setData(signpoint, signDay, score, level);
        popupWindow.showAtLocation(imageViewHead, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignIn: {
                if (UserInfo.get().sign != 1) {
                    showLoadingDialog("请稍候");
                    Api.get().Sign(this, new ApiConfig.ApiRequestListener<SignData>() {
                        @Override
                        public void onSuccess(String msg, final SignData data) {
                            dismissLoadingDialog();
                            UserInfo.get().setSign(1);
//                            showPicDialog(data.signpoint, data.signdays, data.score, data.vipInfo.level_name);
                            mSignInADPopupWindow = (SignInADPopupWindow) SignInADPopupWindow.showOnCenter(VipDetailActivity.this, imageViewHead, data.adInfo.adver_img, new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MobclickAgent.onEvent(mContext, "advertisement"); //友盟自定义事件统计——签到广告
                                    mSignInADPopupWindow.dismiss();
                                    Intent intent = new Intent(mContext, WebActivity.class);
                                    intent.putExtra("http_url", data.adInfo.adver_url);
                                    startActivity(intent);
                                }
                            });
                            getUserDetail();
                        }

                        @Override
                        public void onFailure(String msg) {
                            dismissLoadingDialog();
                        }
                    });
                }
                break;
            }

            case R.id.buttonPublish: {
                startActivity(PublishUnusedActivity.class);
                break;
            }
            case R.id.buttonFetchPag: {
                startActivity(MipcaActivityCapture.class);
                break;
            }
            case R.id.relativeLayoutKnowLevel: {
                Intent intent = getIntent();
                intent.setClass(VipDetailActivity.this, VipIntroduceActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_right: {
                Intent intent = new Intent(mContext, IntegralDetailsActivity.class);
                intent.putExtra("point_type",2);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    private void initView() {
        tvExplanationSign = (TextView) findViewById(R.id.tvExplanationSign);
        tvExplanationPublish = (TextView) findViewById(R.id.tvExplanationPublish);
    }
}
