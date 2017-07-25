package com.quanmai.yiqu.ui.booking;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.quanmai.yiqu.R;

/**
 * Created by 95138 on 2016/4/19.
 */
public class SelectedTimeDialog extends Dialog implements View.OnClickListener{
    Context mContext;
    SelectedTimeDialogListener mListener; //监听

    public SelectedTimeDialog(Context context) {
        super(context);
        initDialog(context);
    }

    public SelectedTimeDialog(Context context, int theme) {
        super(context, theme);
        initDialog(context);
    }

    protected SelectedTimeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog(context);
    }

    private void initDialog(Context context){
        mContext = context;
        setContentView(R.layout.item_select_time_dialog);

        findViewById(R.id.textViewHoleDay).setOnClickListener(this);
        findViewById(R.id.textViewMorning).setOnClickListener(this);
        findViewById(R.id.textViewAfternoon).setOnClickListener(this);
        findViewById(R.id.textViewCancel).setOnClickListener(this);

        Window w = getWindow();
        w.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        w.setWindowAnimations(R.style.mDialogAnimation); //设置弹出动画

        WindowManager.LayoutParams params = w.getAttributes();
        params.gravity = Gravity.BOTTOM;
        onWindowAttributesChanged(params);
    }

    public void addListener(SelectedTimeDialogListener listener){
        if (listener!=null){
            mListener = listener;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //全天
            case R.id.textViewHoleDay:{
                if (mListener==null){
                    return;
                }
                mListener.onItemSelected("全天");
                dismiss();
                break;
            }
            //早上时段
            case R.id.textViewMorning:{
                if (mListener==null){
                    return;
                }
                mListener.onItemSelected("8:00~12:00");
                dismiss();
                break;
            }
            //下午时段
            case R.id.textViewAfternoon:{
                if (mListener==null){
                    return;
                }
                mListener.onItemSelected("14:00~18:00");
                dismiss();
                break;
            }
            //取消
            case R.id.textViewCancel:{
                dismiss();
                break;
            }
            default:
                break;
        }
    }
    public interface SelectedTimeDialogListener{
        void onItemSelected(String time);
    }
}
