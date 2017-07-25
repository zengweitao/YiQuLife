package com.quanmai.yiqu.ui.fuli;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;

public class FuliDialog extends Dialog implements View.OnClickListener {

    Context mContext;
    TextView textViewTitle; //标题
    TextView textViewPoints, textViewChanges;
    Button buttonOK;
    ImageView imageViewAnswer;
    LinearLayout linearWrongAnswer;
    Button buttonChangeBag; //取袋按钮
    Button buttonQuite, buttonGoAhead;
    DismissListener listener;
    private final int QUITE = 101;
    private final int GOAHEAD = 102;
    private final int RIGHT_ANSWER = 103;
    private final int CHANGE_BAG = 104;

    public FuliDialog(final Context context) {
        super(context, R.style.MyDialogStyle);
        this.mContext = context;
        setContentView(R.layout.dialog_fuli);
        init();
        initWindow();
    }

    private void initWindow() {
        setCanceledOnTouchOutside(false);
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        onWindowAttributesChanged(lp);
    }

    private void init() {
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewPoints = (TextView) findViewById(R.id.textViewPoints);
        textViewChanges = (TextView) findViewById(R.id.textViewChanges);
        buttonOK = (Button) findViewById(R.id.buttonOK);
        imageViewAnswer = (ImageView) findViewById(R.id.imageViewAnswer);
        linearWrongAnswer = (LinearLayout) findViewById(R.id.linearWrongAnswer);
        buttonQuite = (Button) findViewById(R.id.buttonQuite);
        buttonGoAhead = (Button) findViewById(R.id.buttonGoAhead);
        buttonChangeBag = (Button) findViewById(R.id.buttonChangeBag);

        buttonOK.setOnClickListener(this);
        buttonQuite.setOnClickListener(this);
        buttonGoAhead.setOnClickListener(this);
        buttonChangeBag.setOnClickListener(this);
    }

    /**
     * 对话框内容构造
     *
     * @param point         本次可领取福利（福袋或积分）个数
     * @param receivetype   福利类型 0. 积分 1. 福袋
     * @param times         本月剩余领取次数
     * @param isAnswerRight 答案是否正确
     */
    public void show(String point, String receivetype, String times, boolean isAnswerRight) {
        String receiveName = "0".equals(receivetype) ? "积分" : "个福袋";
        if (isAnswerRight) {
            imageViewAnswer.setBackgroundResource(R.drawable.bg_answer_right);
            textViewPoints.setVisibility(View.VISIBLE);
            textViewTitle.setText("回答正确");
            if (point.equals("0") && times.equals("0")) {
                textViewPoints.setText("本次获得" + point + receiveName);
                textViewChanges.setText("你的福利已领完，暂无奖励");
            } else if (times.equals("0")) {
                textViewPoints.setText("本次获得" + point + receiveName);
                textViewChanges.setText("本月福利领取机会已用完");
            } else {
                textViewPoints.setText("本次获得" + point + receiveName);
                textViewChanges.setText("本月还有" + times + "次答题得福利机会");
            }
        } else if (point.equals("0")) {
            imageViewAnswer.setBackgroundResource(R.drawable.bg_answer_wrong);
            textViewTitle.setText("回答错误");
            textViewPoints.setVisibility(View.GONE);
            textViewChanges.setText("本月还有" + times + "次答题得福利机会");
            buttonChangeBag.setVisibility(View.GONE);
        }
        show();
    }

    public void show(String point, String receivetype, String times, boolean isAnswerRight, boolean isShowChangeBag) {
        if (isShowChangeBag) {
            buttonChangeBag.setVisibility(View.VISIBLE);
        }
        show(point, receivetype, times, isAnswerRight);
    }

    public void setDismissListener(DismissListener dismissListener) {
        if (dismissListener != null) {
            listener = dismissListener;
        }
    }

    public interface DismissListener {
        void onDismiss(int code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOK: {
                listener.onDismiss(RIGHT_ANSWER);
                dismiss();
                break;
            }
            case R.id.buttonQuite: {
                listener.onDismiss(QUITE);
                dismiss();
                break;
            }
            case R.id.buttonGoAhead: {
                dismiss();
                listener.onDismiss(GOAHEAD);
                break;
            }
            case R.id.buttonChangeBag: {
                dismiss();
                listener.onDismiss(CHANGE_BAG);
            }
            default:
                break;
        }
    }
}
