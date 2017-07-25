package com.quanmai.yiqu.ui.fuli;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.MessageApi;
import com.quanmai.yiqu.api.vo.Option;
import com.quanmai.yiqu.api.vo.QuestionInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.BitmapUtils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.CheckableRelativeLayout;
import com.quanmai.yiqu.common.widget.CircularImageView;
import com.quanmai.yiqu.ui.code.MipcaActivityCapture;
import com.quanmai.yiqu.ui.common.PicViewActivity;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;
import com.quanmai.yiqu.ui.login.LoginActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 答题赚积分页面
 */
public class GainIntegralActivity extends BaseActivity implements View.OnClickListener {

    private CircularImageView imgAdvertisement;
    private TextView tvDetail; //点击查看详情
    private TextView tvChoiceType; //答题类型（单选、多选）
    private TextView tvQuestion; //答题内容
    private TextView tvExplain; //答题说明，具体获得积分数
    private LinearLayout llChoicePic; //图片答题选择列表
    private LinearLayout llChoiceText; //文字答题选择列表
    private Button btnConfirmAnswer;
    private QuestionInfo mQuestionInfo;

    private Boolean isradio = false; //是否单选
    private Boolean isTextOption = false; //是否文字选项
    private String answer = ""; //答案
    private List<String> answerList; //答案集合
    private String checkedAnswer = ""; //选中答案
    private List<String> checkedAnswerList; //选中答案集合

    private Drawable mAdDrawable;
    private Dialog mDialog; //答题弹窗（答对、打错）
    private Dialog gainIntegralDialog; //答题赚积分--》答题已答完弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gain_integral);
        if (getIntent().hasExtra("questionInfo")) {
            mQuestionInfo = (QuestionInfo) getIntent().getSerializableExtra("questionInfo");
        }
        initView();
        init();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(PersonalFragment.ACTION_NETWORKING_TO_REFRESH_DATA));
        super.onDestroy();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("答题赚积分");
        imgAdvertisement = (CircularImageView) findViewById(R.id.imgAdvertisement);
        tvDetail = (TextView) findViewById(R.id.tvDetail);
        tvChoiceType = (TextView) findViewById(R.id.tvChoiceType);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvExplain = (TextView) findViewById(R.id.tvExplain);
        llChoicePic = (LinearLayout) findViewById(R.id.llChoicePic);
        llChoiceText = (LinearLayout) findViewById(R.id.llChoiceText);
        btnConfirmAnswer = (Button) findViewById(R.id.btnConfirmAnswer);

        tvDetail.setOnClickListener(this);
        btnConfirmAnswer.setOnClickListener(this);
        imgAdvertisement.setOnClickListener(this);
    }

    private void init() {
        if (mQuestionInfo != null) {
            tvQuestion.setText(mQuestionInfo.question); //问题
            tvExplain.setText("答对即送" + mQuestionInfo.points + "积分"); //赠送积分说明
            if (!TextUtils.isEmpty(mQuestionInfo.optiontype)) {
                isTextOption = mQuestionInfo.optiontype.equals("0") ? true : false; //是否文字选项
            }
            if (!TextUtils.isEmpty(mQuestionInfo.isradio)) {
                isradio = mQuestionInfo.isradio.equals("0") ? true : false; //是否单选题
                tvChoiceType.setText(isradio ? "[单选]" : "[多选]");
                btnConfirmAnswer.setVisibility(isradio ? View.GONE : View.VISIBLE);
            }
            if (mQuestionInfo.optionsList != null && mQuestionInfo.optionsList.size() > 0) {
                initOptions(isTextOption, mQuestionInfo.optionsList);
            }

            //答案
            answer = mQuestionInfo.answer;
            answerList = new ArrayList<>();
            checkedAnswerList = new ArrayList<>();
            for (int i = 0; i < answer.length(); i++) {
                answerList.add(answer.substring(i, i + 1));
            }

            ImageloaderUtil.displayImage(mContext, mQuestionInfo.urlicon, imgAdvertisement, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    dismissLoadingDialog();
                    mAdDrawable = ContextCompat.getDrawable(mContext, R.drawable.default_header);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    dismissLoadingDialog();
                    mAdDrawable = new BitmapDrawable(bitmap);
                }
            });
            ImageLoader.getInstance().loadImage(mQuestionInfo.urlAlert, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    mAdDrawable = BitmapUtils.bitmapToDrawable(loadedImage);
                }
            });
            resetUi();
        }
    }

    //重置页面
    private void resetUi() {
        if (isTextOption) { //重置文字选项
            for (int i = 0; i < llChoiceText.getChildCount(); i++) {
                CheckableRelativeLayout child = (CheckableRelativeLayout) llChoiceText.getChildAt(i);
                child.setEnabled(true);
                child.setClickable(true);
                child.setChecked(false);
                child.setBackgroundResource(R.drawable.bg_btn_white_half_round);
                child.findViewById(R.id.tvOptionNo).setBackgroundResource(R.drawable.bg_circle_gray);
                ((TextView) child.findViewById(R.id.tvOptionNo)).setTextColor(ContextCompat.getColor(mContext, R.color.text_color_default));
                ((TextView) child.findViewById(R.id.tvOptionContent)).setTextColor(ContextCompat.getColor(mContext, R.color.text_color_default));
                child.findViewById(R.id.imgRight).setVisibility(View.GONE);
                child.findViewById(R.id.imgWrong).setVisibility(View.GONE);
            }
        } else { //重置图片选项
            for (int i = 0; i < llChoicePic.getChildCount(); i++) {
                CheckableRelativeLayout child = (CheckableRelativeLayout) llChoicePic.getChildAt(i);
                child.setEnabled(true);
                child.setClickable(true);
                child.setChecked(false);
                child.setBackgroundResource(R.drawable.bg_gray_stroke_1px);
                ((TextView) child.findViewById(R.id.tvOptionNo)).setTextColor(ContextCompat.getColor(mContext, R.color.text_color_default));
                child.findViewById(R.id.imgRight).setVisibility(View.GONE);
                child.findViewById(R.id.imgWrong).setVisibility(View.GONE);
            }
        }
        checkedAnswer = "";
        checkedAnswerList.clear();
    }

    //初始化选项
    private void initOptions(Boolean isTextOption, List<Option> optionList) {
        if (isTextOption) { //文字选项
            initTextOptions(optionList);
        } else { //图片选项
            initPicOptions(optionList);
        }
    }

    private void initTextOptions(List<Option> optionList) {
        llChoiceText.setVisibility(View.VISIBLE);
        llChoicePic.setVisibility(View.GONE);
        for (int i = 0; i < llChoiceText.getChildCount(); i++) {
            final CheckableRelativeLayout childView = (CheckableRelativeLayout) llChoiceText.getChildAt(i);
            final TextView tvOptionNo = (TextView) childView.findViewById(R.id.tvOptionNo); //选项
            final TextView tvOptionContent = (TextView) childView.findViewById(R.id.tvOptionContent); //选项内容
            final Option option = optionList.get(i);
            childView.setTag(option);
            tvOptionNo.setText(option.optionno.trim()); //设置选项序号
            tvOptionContent.setText(option.content.trim()); //设置选项内容
            //监听TextView绘制（之前）
            tvOptionContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    int lineCount = tvOptionContent.getLineCount();
                    if (lineCount > 1) {
                        tvOptionContent.setTextSize(16);
                    }
                    tvOptionContent.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });


            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childView.setChecked(!childView.isChecked());
                }
            });
            childView.setOnCheckedChangeListener(new CheckableRelativeLayout.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RelativeLayout relativeLayout, boolean isChecked) {
                    if (isChecked) {
                        tvOptionNo.setTextColor(ContextCompat.getColor(mContext, R.color.theme));
                        relativeLayout.setBackgroundResource(R.drawable.bg_theme_color_stroke_2px_half_round);
                        if (isradio) { //单选题，立刻校验答案
                            checkTheAnswers();
                        }
                    } else {
                        tvOptionNo.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_default));
                        relativeLayout.setBackgroundResource(R.drawable.bg_btn_white_half_round);
                    }
                    Option mOption = (Option) relativeLayout.getTag();
                    mOption.isChecked = isChecked;
                    relativeLayout.setTag(mOption);
                }
            });
        }
    }

    private void initPicOptions(List<Option> optionList) {
        llChoiceText.setVisibility(View.GONE);
        llChoicePic.setVisibility(View.VISIBLE);
        for (int i = 0; i < llChoicePic.getChildCount(); i++) {
            final CheckableRelativeLayout childView = (CheckableRelativeLayout) llChoicePic.getChildAt(i);
            final TextView tvOptionNo = (TextView) childView.findViewById(R.id.tvOptionNo);
            CircularImageView imgOption = (CircularImageView) childView.findViewById(R.id.imgOption);
            final View viewBorder = childView.findViewById(R.id.viewBorder);
            Option option = optionList.get(i);
            childView.setTag(option);
            tvOptionNo.setText(option.optionno); //设置选项序号
            ImageloaderUtil.displayImage(mContext, option.url, imgOption);

            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childView.setChecked(!childView.isChecked());
                }
            });
            childView.setOnCheckedChangeListener(new CheckableRelativeLayout.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RelativeLayout relativeLayout, boolean isChecked) {
                    if (isChecked) {
                        tvOptionNo.setTextColor(ContextCompat.getColor(mContext, R.color.theme));
                        relativeLayout.setBackgroundResource(R.drawable.bg_green_stroke_2px);
                        viewBorder.setVisibility(View.VISIBLE);
                        if (isradio) { //单选题，立刻校验答案
                            checkTheAnswers();
                        }
                    } else {
                        tvOptionNo.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_default));
                        relativeLayout.setBackgroundResource(R.drawable.bg_gray_stroke_1px);
                        viewBorder.setVisibility(View.GONE);
                    }
                    Option mOption = (Option) relativeLayout.getTag();
                    mOption.isChecked = isChecked;
                    relativeLayout.setTag(mOption);
                }
            });
        }
    }

    //校验答案
    private void checkTheAnswers() {
        LinearLayout currentLL = isTextOption ? llChoiceText : llChoicePic;
        int checkedOptionNum = 0; //当前选中选项数目
        for (int i = 0; i < currentLL.getChildCount(); i++) {
            CheckableRelativeLayout child = (CheckableRelativeLayout) currentLL.getChildAt(i);
            if (child.isChecked()) {
                checkedOptionNum++;
            }
        }
        //无选中选项
        if (checkedOptionNum == 0) {
            showCustomToast("请选择");
            return;
        }
        //有选中选项
        if (isTextOption) { //文字选项
            checkTextAnswer();
        } else { //图片选项
            checkPicAnswer();
        }

        for (int i = 0; i < currentLL.getChildCount(); i++) {
            CheckableRelativeLayout child = (CheckableRelativeLayout) currentLL.getChildAt(i);
            child.setEnabled(false);
            child.setClickable(false);
        }

        if (answer.equals(checkedAnswer)) { //回答正确
            answerRight(mQuestionInfo.questionid, String.valueOf(mQuestionInfo.points));
            mDialog = DialogUtil.getGainIntegral(GainIntegralActivity.this, mAdDrawable, true, mQuestionInfo.points, this);
            mDialog.show();
        } else { //回答错误
            mDialog = DialogUtil.getGainIntegral(GainIntegralActivity.this, mAdDrawable, false, mQuestionInfo.points, this);
            //延时1秒弹窗
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialog.show();
                }
            }, 1000); //延时1000毫秒
        }
    }

    private void checkTextAnswer() {
        for (int i = 0; i < llChoiceText.getChildCount(); i++) {
            CheckableRelativeLayout child = (CheckableRelativeLayout) llChoiceText.getChildAt(i);
            TextView tvOptionNo = (TextView) child.findViewById(R.id.tvOptionNo);
            TextView tvOptionContent = (TextView) child.findViewById(R.id.tvOptionContent);
            ImageView imgRight = (ImageView) child.findViewById(R.id.imgRight);
            ImageView imgWrong = (ImageView) child.findViewById(R.id.imgWrong);
            Option option = (Option) child.getTag();

            if (child.isChecked()) { //当前选项被选中
                String currentAnswer = option.optionno; //选中序号
                checkedAnswer += currentAnswer;
                checkedAnswerList.add(currentAnswer);

                if (!answerList.contains(option.optionno)) { //错误答案选项
                    child.setBackgroundResource(R.drawable.bg_btn_ffa39b_half_round);
                    tvOptionNo.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_FFA39B));
                    tvOptionNo.setBackgroundResource(R.drawable.bg_circle_white);
                    tvOptionContent.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    imgWrong.setVisibility(View.VISIBLE);
                } else { //正确答案
                    child.setBackgroundResource(R.drawable.bg_btn_theme_color_half_round);
                    tvOptionNo.setTextColor(ContextCompat.getColor(mContext, R.color.theme));
                    tvOptionNo.setBackgroundResource(R.drawable.bg_circle_white);
                    tvOptionContent.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    imgRight.setVisibility(View.VISIBLE);
                }
            } else { //当前选项未被选中
                if (answerList.contains(option.optionno)) { //正确答案选项
                    child.setBackgroundResource(R.drawable.bg_btn_theme_color_half_round);
                    tvOptionNo.setTextColor(ContextCompat.getColor(mContext, R.color.theme));
                    tvOptionNo.setBackgroundResource(R.drawable.bg_circle_white);
                    tvOptionContent.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    imgRight.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void checkPicAnswer() {
        for (int i = 0; i < llChoicePic.getChildCount(); i++) {
            CheckableRelativeLayout child = (CheckableRelativeLayout) llChoicePic.getChildAt(i);
            TextView tvOptionNo = (TextView) child.findViewById(R.id.tvOptionNo);
            View viewBorder = child.findViewById(R.id.viewBorder);
            ImageView imgRight = (ImageView) child.findViewById(R.id.imgRight);
            ImageView imgWrong = (ImageView) child.findViewById(R.id.imgWrong);
            Option option = (Option) child.getTag();
            viewBorder.setVisibility(View.GONE);

            if (child.isChecked()) { //当前选项被选中
                String currentAnswer = option.optionno; //选中序号
                checkedAnswer += currentAnswer;
                checkedAnswerList.add(currentAnswer);
                if (!answerList.contains(option.optionno)) { //错误答案选项
                    child.setBackgroundResource(R.drawable.bg_btn_ffa39b_radius_4dp);
                    tvOptionNo.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    imgWrong.setVisibility(View.VISIBLE);
                } else { //正确答案
                    child.setBackgroundResource(R.drawable.bg_theme_color_radius_4dp);
                    tvOptionNo.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    imgRight.setVisibility(View.VISIBLE);
                }
            } else { //当前选项未被选中
                if (answerList.contains(option.optionno)) { //正确答案选项
                    child.setBackgroundResource(R.drawable.bg_theme_color_radius_4dp);
                    tvOptionNo.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    imgRight.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 获取广告答题信息
     */
    private void getQuestion() {
        showLoadingDialog();
        MessageApi.get().getAdAnswerQuestionInfo(mContext, new ApiConfig.ApiRequestListener<QuestionInfo>() {
            @Override
            public void onSuccess(String isexist, QuestionInfo data) {
                if ("0".equals(isexist)) {
                    dismissLoadingDialog();
                    finish();
                }
                mQuestionInfo = data;
                init();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                gainIntegralDialog = DialogUtil.getPopupConfirmDialog2(mContext, "今天答题已完成", "知识分类答题", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btnConfirm: {
                                startActivity(LuckyBagActivity.class);
                                gainIntegralDialog.dismiss();
                                finish();
                                break;
                            }
                            case R.id.btnCancel: {
                                gainIntegralDialog.dismiss();
                                finish();
                                break;
                            }
                        }
                    }
                });
                gainIntegralDialog.show();
            }
        });
    }

    /**
     * 回答正确得分
     *
     * @param contentid
     * @param points
     */
    private void answerRight(String contentid, String points) {
        MessageApi.get().adAnswerQuestionGetPoint(mContext, contentid, points, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
//                showCustomToast(msg);
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
            case R.id.btnConfirmAnswer: {
                checkTheAnswers();
                break;
            }
            case R.id.imgAdvertisement: { //答题跳转
                if (mQuestionInfo != null && !TextUtils.isEmpty(mQuestionInfo.adurl)) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra("http_url", mQuestionInfo.adurl);
                    startActivity(intent);
                }
                break;
            }
            case R.id.tvDetail: { //查看详情（大图）
                if (mQuestionInfo != null && !TextUtils.isEmpty(mQuestionInfo.urladpic)) {
                    Intent intent = new Intent(mContext, PicViewActivity.class);
                    intent.putExtra("urlImg", mQuestionInfo.urladpic);
                    startActivity(intent);
                }
                break;
            }
            case R.id.btnContinue: { //继续答题
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                getQuestion();
                break;
            }
            case R.id.btnChangeBag: { //扫码取袋
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                MobclickAgent.onEvent(mContext, "scanCode"); //友盟自定义事件统计——扫码取袋
                if (Session.get(mContext).isLogin()) {
                    startActivity(new Intent(mContext, MipcaActivityCapture.class));
                } else {
                    Utils.showCustomToast(mContext, "未登录，请登录后再试");
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            }
            case R.id.btnAgain: { //重新答题
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                resetUi();
                break;
            }
            case R.id.btnClose: { //关闭
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                finish();
                break;
            }
            default: {
                break;
            }
        }
    }
}
