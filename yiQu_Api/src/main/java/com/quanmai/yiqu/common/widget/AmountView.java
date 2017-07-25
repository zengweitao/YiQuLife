package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;

/**
 * Created by 95138 on 2016/11/11.
 */

public class AmountView extends LinearLayout implements View.OnClickListener{

    private int amount = -1; //购买数量
    ImageView ivMin;
    TextView tvCount;
    ImageView ivAdd;
    int maxCount = 99; //默认最大数值

    OnAmountChangeListener mListener;

    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.amount_view_item,this);
        ivMin = (ImageView)findViewById(R.id.ivMin);
        tvCount = (TextView)findViewById(R.id.tvCount);
        ivAdd = (ImageView)findViewById(R.id.ivAdd);

        ivMin.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        tvCount.setText(amount+"");
    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

    public void setMaxCount(int max) {
        maxCount = max;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivMin:{
                if (amount >= 0) {
                    amount--;
                    tvCount.setText(amount + "");
                }
                break;
            }
            case R.id.ivAdd:{
                if (amount < maxCount) {
                    amount++;
                    tvCount.setText(amount + "");
                }
                break;
            }
            default:break;
        }

        if (amount>=0){
            ivMin.setVisibility(VISIBLE);
            tvCount.setVisibility(VISIBLE);
        }else {
            ivMin.setVisibility(GONE);
            tvCount.setVisibility(GONE);
        }

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

    public interface OnAmountChangeListener {
        void onAmountChange(View view, int amount);
    }

}
