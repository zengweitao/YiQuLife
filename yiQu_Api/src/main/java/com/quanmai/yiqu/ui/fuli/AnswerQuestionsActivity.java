package com.quanmai.yiqu.ui.fuli;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.MessageApi;
import com.quanmai.yiqu.api.vo.PicValidateInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.ui.code.ChangeBagActivity;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 领取福利-答题页面
 */
public class AnswerQuestionsActivity extends BaseActivity {
    private final int QUITE = 101;
    private final int GOAHEAD = 102;
    private final int RIGHT_ANSWER = 103;
    private final int CHANGE_BAG = 104;

    TextView tv_title;
    Button buttonNext;
    GridView gridview;
    ImageView imageViewContent;
    TextView textViewSubject;
    LinearLayout relativeLayoutContent;
    TextView iv_no_data;
    Button buttonSkip;
    TextView textViewChanges;
    TextView textViewTips;

    String url;
    PicValidateAdapter adapter;
    String answer;  //答案
    FuliDialog dialog;
    CommonList<PicValidateInfo> list;
    int count = 1;
    String changes = "0"; //答题机会
    private boolean isShowChangeBag = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                dialog.show("0", "0", changes, false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_questions);
        url = getIntent().getStringExtra("url");
        isShowChangeBag = getIntent().getBooleanExtra("isShowChangeBag", false);
        init();
        showLoadingDialog("请稍候");
        getLuckyBagChange();

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(answer);
            }
        });

        //对话框消失回调
        dialog.setDismissListener(new FuliDialog.DismissListener() {
            @Override
            public void onDismiss(int code) {
                switch (code) {
                    case QUITE: {
                        finish();
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(PersonalFragment.ACTION_NETWORKING_TO_REFRESH_DATA));
                        break;
                    }
                    case GOAHEAD: {
                        getQuestions();
                        break;
                    }
                    case RIGHT_ANSWER: {
                        startActivity(LuckyBagActivity.class);
                        finish();
                        break;
                    }
                    case CHANGE_BAG: {
                        Intent intent = getIntent();
                        intent.setClass(mContext, ChangeBagActivity.class);
                        intent.putExtra("isRefresh", true);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    default:
                        break;
                }
            }
        });


        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQuestions();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getQuestions();
            }
        });

    }

    //初始化
    private void init() {

        buttonSkip = (Button) findViewById(R.id.buttonSkip);
        textViewChanges = (TextView) findViewById(R.id.textViewChanges);
        tv_title = (TextView) findViewById(R.id.tv_title);
        textViewSubject = (TextView) findViewById(R.id.textViewSubject);
        relativeLayoutContent = (LinearLayout) findViewById(R.id.relativeLayoutContent);
        iv_no_data = (TextView) findViewById(R.id.iv_no_data);
        tv_title.setText("领取福利");
        buttonNext = (Button) findViewById(R.id.buttonNext);
        gridview = (GridView) findViewById(R.id.gridview);
        imageViewContent = (ImageView) findViewById(R.id.imageViewContent);
        textViewTips = (TextView) findViewById(R.id.textViewTips);

        dialog = new FuliDialog(mContext);

        if (url != null && url != "") {
            ImageloaderUtil.displayImage(mContext, url, imageViewContent);
        }
    }

    /**
     * 下一题
     */
    private void getQuestions() {
        MessageApi.get().PicValidate(mContext, new ApiConfig.ApiRequestListener<CommonList<PicValidateInfo>>() {

            @Override
            public void onSuccess(String msg, CommonList<PicValidateInfo> data) {
                dismissLoadingDialog();
                list = data;
                if (!TextUtils.isEmpty(msg)) {
                    SpannableStringBuilder spannableString = new SpannableStringBuilder("请选出 " + msg.split(",").length + " 个：" + data.name);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#48c299")), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textViewSubject.setText(spannableString);
                }
                if (data.name.equals("厨余垃圾")) {
                    textViewTips.setText("小提示：厨余垃圾是指居民日常生活及食品加工、饮食服务、单位供餐等活动中产生的垃圾。");
                } else if (data.name.equals("可回收垃圾")) {
                    textViewTips.setText("小提示：可回收垃圾主要包括废纸、塑料、玻璃、金属和布料五大类。");
                } else if (data.name.equals("其他垃圾")) {
                    textViewTips.setText("小提示：其他垃圾主包括砖瓦陶瓷、渣土、卫生间废纸、纸巾等难以回收的废弃物及果壳、尘土。");
                } else if (data.name.equals("有害垃圾")) {
                    textViewTips.setText("小提示：有害垃圾包括废电池、废日光灯管、废水银温度计、过期药品等，这些垃圾需要特殊安全处理。");
                }
//                textViewAnswer.setText("");
                answer = msg;
                adapter = new PicValidateAdapter(mContext, data);
                gridview.setAdapter(adapter);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                relativeLayoutContent.setVisibility(View.GONE);
                iv_no_data.setText("没有数据");
                Utils.showCustomToast(mContext, msg);
            }
        });
    }

    //答案校验
    private void checkAnswer(String answer) {
        String id = adapter.getCheckId();
        List<String> answerList = new ArrayList<>();
        if (answer != null && "" != answer) {
            String[] answers = null;
            answers = answer.split(",");
            for (int i = 0; i < answers.length; i++) {
                answerList.add(answers[i]);
            }
        }
        if (id.equals("")) {
            Utils.showCustomToast(mContext, "请至少选择一张图片");
            return;
        }
        //假如答案正确，则显示√，答案错误，则显示出正确答案，且答错部分用X标明
        //一秒后弹框提示
        if (!answer.equals(id)) {
            if (list.size() > 0) {
                int unCheckCount = 0;
                boolean isCount = true;
                for (int i = 0; i < list.size(); i++) {
                    View mView = gridview.getChildAt(i);
                    ImageView imageView = (ImageView) mView.findViewById(R.id.imageViewBorder);
                    PicValidateInfo info = (PicValidateInfo) imageView.getTag();

                    if (answerList.contains(list.get(i).id)) {
                        imageView.setBackgroundResource(R.drawable.icon_answer_true);
                        if (info.isCheck) {
                            unCheckCount++;
                        }
                    } else if (info.isCheck) {
                        isCount = false;
                        imageView.setBackgroundResource(R.drawable.icon_answer_false);
                    }
                }
//                if (isCount&&unCheckCount>0&&(answer.split(",").length>unCheckCount)){
//                    textViewAnswer.setText("漏选"+(answer.split(",").length-unCheckCount)+"题");
//                }else {
//                    textViewAnswer.setText("");
//                }
            }
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000); //答错题，延迟一秒弹窗
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(1);
                }
            }.start();
        } else {
            for (int i = 0; i < list.size(); i++) {
                View mView = gridview.getChildAt(i);
                ImageView imageView = (ImageView) mView.findViewById(R.id.imageViewBorder);
                if (answerList.contains(list.get(i).id)) {
                    imageView.setBackgroundResource(R.drawable.icon_answer_true);
                }
            }
            if (count == 1) {
                getLastChange();
            }
        }
    }

    private void getLastChange() {
        showLoadingDialog("请稍候");
        MessageApi.get().PicValidateSuccess(mContext,
                new ApiConfig.ApiRequestListener<Map<String, Object>>() {

                    @Override
                    public void onSuccess(String point, Map<String, Object> map) {
                        dismissLoadingDialog();
                        count = 1;
                        changes = (String) map.get("monthlasttimes");
                        String receivetype = (String) map.get("receivetype");
                        dialog.show((String) map.get("singletimes"), receivetype, changes, true, isShowChangeBag);
                        getLuckyBagChange();
                    }

                    @Override
                    public void onFailure(String msg) {
                        count++;
                        showShortToast(msg);
                        dismissLoadingDialog();
                    }
                });
    }

    //获取剩余答题机会
    private void getLuckyBagChange() {
        MessageApi.get().GetAnswerQuestionsChange(mContext, new ApiConfig.ApiRequestListener<String>() {

            @Override
            public void onSuccess(String msg, String data) {
                changes = data;
                textViewChanges.setText("本月福利已领取：" + msg + "次，" + "剩余：" + data + "次");
                getQuestions();
            }

            @Override
            public void onFailure(String msg) {
                Utils.showCustomToast(mContext, msg);
            }
        });
    }
}
