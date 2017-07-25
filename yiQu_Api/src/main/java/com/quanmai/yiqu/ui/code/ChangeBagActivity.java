package com.quanmai.yiqu.ui.code;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.MessageApi;
import com.quanmai.yiqu.api.UserInfoApi;
import com.quanmai.yiqu.api.vo.AdInfo;
import com.quanmai.yiqu.api.vo.BagInfo;
import com.quanmai.yiqu.api.vo.BannerInfo;
import com.quanmai.yiqu.api.vo.QuestionInfo;
import com.quanmai.yiqu.api.vo.ScorePayInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.XCRoundImageView;
import com.quanmai.yiqu.ui.adapter.LocalImageHolderView;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.quanmai.yiqu.ui.fuli.GainIntegralActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 换袋第一步
 *
 * @author kiwi
 */
public class ChangeBagActivity extends BaseActivity implements OnClickListener {
    ConvenientBanner banner;
    ImageView imageViewBanner;
    XCRoundImageView imageViewHead;
    TextView tvName, tvLuckBag, tvScore, tvConsume;
    ImageView imgCut, imgAdd;
    Button btnLevel;
    TextView tvBagNum;
    CheckBox checkboxFedFirst; //优先消费福袋选择按钮

    int currentCount;   //当前取袋数
    int maxCount;       //数量,每天最多可兑换数量
    int score;          //所需积分,多少积分兑换一个物品
    int point;          //用户可用积分
    int fednum;         //用户可用福袋
    int surplusPoint;   //用户剩余积分
    int surplusFedNum;  //用户剩余福袋
    int singlepoint;    //单个福袋对应积分

    BagInfo bagInfo;

    String terminalno; //终端号
    List<String> urls;
    List<AdInfo> infos;
    boolean isRefresh; //是否需要重新获取用户积分数据

    private Dialog lackOfIntegralDialog; //积分不足弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bag);
        ((TextView) findViewById(R.id.tv_title)).setText("领取环保袋");
        initView();
        init();
        getAdvert();
    }

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
                    Intent intent = new Intent(ChangeBagActivity.this, WebActivity.class);
                    intent.putExtra("http_url", infos.get(position).adver_url);
                    MobclickAgent.onEvent(mContext, infos.get(position).adver_alias); //友盟自定义事件统计
                    startActivity(intent);
                }
            }
        });
    }

    private void initView() {
        tvName = (TextView) findViewById(R.id.textViewName);
        tvLuckBag = (TextView) findViewById(R.id.textViewLuckBag);
        tvScore = (TextView) findViewById(R.id.textViewScore);
        tvConsume = (TextView) findViewById(R.id.textViewConsume);
        tvBagNum = (TextView) findViewById(R.id.textViewBagNum);
        imgCut = (ImageView) findViewById(R.id.imageViewCut);
        imgAdd = (ImageView) findViewById(R.id.imageViewAdd);
        banner = (ConvenientBanner) findViewById(R.id.banner);
        imageViewBanner = (ImageView) findViewById(R.id.imageViewRule);
        imageViewBanner.setOnClickListener(this);
        btnLevel = (Button) findViewById(R.id.buttonLevel);
        imageViewHead = (XCRoundImageView) findViewById(R.id.imageViewHeadPortrait);
        imageViewHead.setImageResource(R.drawable.default_header);
        imageViewHead.setBorderWidth(Utils.dp2px(mContext, 3));
        imageViewHead.setBorderColor(0x19000000);
        checkboxFedFirst = (CheckBox) findViewById(R.id.checkboxFedFirst);

        findViewById(R.id.imageViewCut).setOnClickListener(this);
        findViewById(R.id.imageViewAdd).setOnClickListener(this);
        findViewById(R.id.buttonConfirm).setOnClickListener(this);
    }

    private void init() {
        urls = new ArrayList<>();
        bagInfo = (BagInfo) getIntent().getSerializableExtra("baginfo");
        terminalno = getIntent().getStringExtra("terminalno");
        isRefresh = getIntent().getBooleanExtra("isRefresh", false);
        tvBagNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int c) {
                if (s.length() == 0) {
                    currentCount = 0;
                    tvBagNum.setText(currentCount + "");
                } else {
                    currentCount = Integer.valueOf(s.toString());
                    reload();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        checkboxFedFirst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                reload();
            }
        });

        if (isRefresh) { //重新获取用户积分
            getPointAndFed();
        } else if (bagInfo != null) {
//            maxCount = bagInfo.type == 1 ? bagInfo.count : 1; //出袋数量不可选时，最大兑换数量为1
            maxCount = bagInfo.count;
            score = bagInfo.score;
            point = bagInfo.point;
            singlepoint = Integer.parseInt(bagInfo.singlepoint);
            fednum = bagInfo.fednum;
            surplusPoint = bagInfo.point;
            showLoadingDialog();
            if (fednum == 0) {
                checkboxFedFirst.setChecked(false);
            }
            if (point == 0 && fednum == 0) {
                showLackOfIntegralDialog();
            }
            initData();
        }
    }

    private void initData() {
        tvLuckBag.setText("福袋数：" + fednum);
        tvScore.setText("积分数： " + point);
        currentCount = 1;
        tvBagNum.setText(currentCount + "");
        StringBuffer stringBuffer = new StringBuffer();
        //"每领取一个袋子将消耗一个福袋或" : "每领取一卷袋子将消耗" + score / singlepoint + "个福袋或"
        //score + "积分，当优先消费福袋时，福袋数不足将会自动扣除积分"
        stringBuffer.append(BagInfo.TYPE_BAG.equals(bagInfo.adscription) ?
                "每领取一个袋子将消耗一个福袋或10积分" : "每领取一卷袋子将消耗对应个数的福袋或积分");
        stringBuffer.append( "，当优先消费福袋时，福袋数不足将会自动扣除积分");
        tvConsume.setText(stringBuffer.toString());
        getUserDetail();
    }

    //积分不足弹窗（可跳转到答题页面）
    private void showLackOfIntegralDialog() {
        lackOfIntegralDialog = DialogUtil.getPopupConfirmDialog2(mContext, "积分不足", "赚积分", "取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnConfirm: {
                        lackOfIntegralDialog.dismiss();
                        getQuestion();
                        break;
                    }
                    case R.id.btnCancel: {
                        lackOfIntegralDialog.dismiss();
                        finish();
                        break;
                    }
                }
            }
        });
        lackOfIntegralDialog.show();
    }

    /**
     * 获取广告答题信息
     */
    private void getQuestion() {
        showLoadingDialog();
        MessageApi.get().getAdAnswerQuestionInfo(mContext, new ApiConfig.ApiRequestListener<QuestionInfo>() {
            @Override
            public void onSuccess(String isexist, QuestionInfo data) {
                dismissLoadingDialog();
                if ("0".equals(isexist)) {
                    showCustomToast("今天答题已完");
                    return;
                }

                Intent intent = new Intent(mContext, GainIntegralActivity.class);
                intent.putExtra("questionInfo", data);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast("今天答题已完");
                finish();
            }
        });
    }

    //更新福袋、积分
    private void reload() {
        if (currentCount <= 0) {
            imgCut.setEnabled(false);
        } else {
            imgCut.setEnabled(true);
        }
        if (currentCount < maxCount) {
            imgAdd.setEnabled(true);
        }

        //默认优先使用福袋
        if (checkboxFedFirst.isChecked()) {
            switch (bagInfo.adscription) {
                case BagInfo.TYPE_BAG: { //普通发袋机
                    if (currentCount < fednum) {//1、取袋数少于用户福袋数
                        tvLuckBag.setText("福袋数：" + (fednum - currentCount));
                        tvScore.setText("积分数：" + point);
                        surplusFedNum = fednum - currentCount;
                        surplusPoint = bagInfo.point;
                    } else if (currentCount >= fednum && (currentCount - fednum) * score <= point) {//2、取袋数多于用户福袋数，同时用户积分充足
                        tvLuckBag.setText("福袋数：" + 0);
                        tvScore.setText("积分数：" + (point - (currentCount - fednum) * score));
                        surplusFedNum = 0;
                        surplusPoint = (point - (currentCount - fednum) * score);
                    } else {
                        imgAdd.setEnabled(false);
                        tvBagNum.setText((currentCount - 1) + "");
                        showShortToast("积分不足");
                    }
                    break;
                }
                case BagInfo.TYPE_SMART_BAG: { //智能发袋机
                    if (currentCount * score <= fednum * singlepoint) { //1、取袋所需积分<用户福袋数对应积分
                        surplusFedNum = (fednum * singlepoint - currentCount * score) / singlepoint;
                        tvLuckBag.setText("福袋数：" + surplusFedNum);
                        tvScore.setText("积分数：" + point);
                        surplusPoint = bagInfo.point;
                    } else if (currentCount * score > fednum * singlepoint
                            && (currentCount * score - fednum * singlepoint) <= point) { //2、取袋数多于用户福袋数，同时用户积分充足
                        surplusFedNum = 0;
                        surplusPoint = (point - (currentCount * score - fednum * singlepoint));
                        tvLuckBag.setText("福袋数：" + surplusFedNum);
                        tvScore.setText("积分数：" + surplusPoint);
                    } else {
                        imgAdd.setEnabled(false);
                        tvBagNum.setText((currentCount - 1) + "");
                        showShortToast("积分不足");
                    }
                    break;
                }
            }
        }
        //仅使用积分
        else {
            surplusFedNum = fednum; //剩余福袋数不变
            if (currentCount * score <= point) { //用户积分充足
                tvLuckBag.setText("福袋数：" + fednum);
                tvScore.setText("积分数：" + (point - currentCount * score));
                surplusPoint = point - currentCount * score; //剩余积分
            } else {
                imgAdd.setEnabled(false);
                showShortToast("积分不足");
                tvBagNum.setText(point / (currentCount * score) + "");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (bagInfo == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.imageViewAdd: {
                if (currentCount < maxCount) {
                    currentCount++;
                    tvBagNum.setText(currentCount + "");
                } else { //设置单次领取环保袋最大数量
                    showShortToast("单次最多可领取" + maxCount + "份环保袋");
                }
                break;
            }
            case R.id.imageViewCut: {
                if (currentCount > 0) {
                    currentCount--;
                    tvBagNum.setText(currentCount + "");
                }
                break;
            }
            case R.id.buttonConfirm: {
                String consumetype = "0";
                if (checkboxFedFirst.isChecked() && fednum != 0) { //“优先消费福袋”按钮选中，且用户福袋数不为零
                    consumetype = "0";
                } else {
                    consumetype = "1";
                }
                pay(consumetype);
                break;
            }
            case R.id.imageViewRule: {
                if (infos != null && infos.size() > 0) {
                    Intent intent = new Intent(ChangeBagActivity.this, WebActivity.class);
                    intent.putExtra("http_url", infos.get(0).adver_url);
                    MobclickAgent.onEvent(mContext, infos.get(0).adver_alias); //友盟自定义事件统计
                    startActivity(intent);
                }
                break;
            }
            default:
                break;
        }

    }

    /**
     * 积分换购垃圾袋
     */
    private void pay(String consumetype) {
        if (currentCount <= 0) {
            showCustomToast("请选择环保袋数量");
            return;
        }
        showLoadingDialog();
        IntegralApi.get().ScorePay(mContext, terminalno, currentCount, "1", consumetype,
                new ApiRequestListener<ScorePayInfo>() {

                    @Override
                    public void onSuccess(String msg, ScorePayInfo data) {
                        dismissLoadingDialog();
                        bagInfo.img = data.successimg;
                        bagInfo.balancePoints = data.balancePoints;
                        Intent intent = new Intent(mContext, ChangeBagSecondStepActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("baginfo", bagInfo);
                        intent.putExtra("surplusPoint", surplusPoint);
                        intent.putExtra("surplusFedNum", surplusFedNum);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                    }
                });

    }

    /**
     * 获取广告列表
     */
    private void getAdvert() {
        GoodsApi.get().GetAvd(this, "4", new ApiConfig.ApiRequestListener<BannerInfo>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(String msg, BannerInfo data) {
                infos = new ArrayList<AdInfo>();
                infos.addAll(data.adList);
                for (int i = 0; i < data.adList.size(); i++) {
                    urls.add(data.adList.get(i).adver_img);
                }
                if (urls.size() > 0) {
                    if (urls.size() == 1) {
                        imageViewBanner.setVisibility(View.VISIBLE);
                        banner.setVisibility(View.GONE);
                        ImageloaderUtil.displayImage(ChangeBagActivity.this, urls.get(0), imageViewBanner);
                        return;
                    } else {
                        imageViewBanner.setBackground(null);
                        imageViewBanner.setVisibility(View.GONE);
                        banner.setVisibility(View.VISIBLE);
                        initBanner();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
//                showShortToast(msg);
            }
        });
    }

    /**
     * 获取用户详细信息
     */
    public void getUserDetail() {
        UserInfoApi.get().UserDetail(mContext, new ApiConfig.ApiRequestListener<UserInfo>() {
            @Override
            public void onSuccess(String msg, UserInfo data) {
                dismissLoadingDialog();
                ImageloaderUtil.displayImage(mContext, UserInfo.get().getFace(), imageViewHead);
                tvName.setText(UserInfo.get().getAlias());
                btnLevel.setText(UserInfo.get().vipInfo.level_name);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
//                showCustomToast(msg);
            }
        });
    }

    /**
     * 获取用户积分和红包信息
     */
    public void getPointAndFed() {
        showLoadingDialog();
        IntegralApi.get().QRCode(mContext, terminalno,
                new ApiRequestListener<BagInfo>() {
                    @Override
                    public void onSuccess(String msg, BagInfo data) {
                        dismissLoadingDialog();
                        UserInfo.get().setIsbind("1");
                        mSession.setBind(true);
                        bagInfo = data;
                        maxCount = bagInfo.count;
                        score = bagInfo.score;
                        point = bagInfo.point;
                        fednum = bagInfo.fednum;
                        surplusPoint = bagInfo.point;
                        initData();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                    }
                }

        );
    }
}
