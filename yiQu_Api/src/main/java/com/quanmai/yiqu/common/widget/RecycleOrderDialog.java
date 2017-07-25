package com.quanmai.yiqu.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.RecycleGarbageInfo;
import com.quanmai.yiqu.api.vo.RecycleOrderInfo;

import java.util.List;

/**
 * Created by zhanjinj on 16/4/26.
 */
public class RecycleOrderDialog extends Dialog {

    Context mContext;
    LinearLayout llRecycleInfo;
    TextView tvGainPoint;

    public RecycleOrderDialog(Context context, String title, int position, RecycleOrderInfo recycleOrderInfo, String point,
                              View.OnClickListener onClickListener) {
        super(context, R.style.MyDialogStyle);
        this.mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_recycle_order, null);
        ((TextView) view.findViewById(R.id.tvTitle)).setText(title);
        llRecycleInfo = (LinearLayout) view.findViewById(R.id.llRecycleInfo);
        tvGainPoint = (TextView) view.findViewById(R.id.tvGainPoint);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);

        btnCancel.setTag(position);
        btnConfirm.setTag(position);

        btnCancel.setText("修改数量");
        btnConfirm.setText("确认回收");
        if (Integer.parseInt(point) >= 1000) {
            tvGainPoint.setText("对方将获得1000积分");
        } else {
            tvGainPoint.setText("对方将获得" + point + "益币");
        }

        btnCancel.setOnClickListener(onClickListener);
        btnConfirm.setOnClickListener(onClickListener);
        btnClose.setOnClickListener(onClickListener);

        if (recycleOrderInfo != null && recycleOrderInfo.recycleDetails.size() > 0) {
            for (int i = 0; i < recycleOrderInfo.recycleDetails.size(); i++) {
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_recycle_order_dialog, null);
                ((TextView) itemView.findViewById(R.id.tvRecycleType)).setText(recycleOrderInfo.recycleDetails.get(i).garbage);
                ((TextView) itemView.findViewById(R.id.tvRecycleQuantity)).setText(recycleOrderInfo.recycleDetails.get(i).quantity + "个");
                ((TextView) itemView.findViewById(R.id.tvRecyclePoint)).setText("+" + (Integer.parseInt(recycleOrderInfo.recycleDetails.get(i).point) >= 1000 ?
                        1000 : recycleOrderInfo.recycleDetails.get(i).point));
                llRecycleInfo.addView(itemView);
            }
        }

        setContentView(view);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setWindowAnimations(R.style.PopupAnim);
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }


    public void updateRecycleDetails(List<RecycleGarbageInfo> recycleDetails) {
        if (recycleDetails != null && recycleDetails.size() > 0) {
            llRecycleInfo.removeAllViews();
            for (int i = 0; i < recycleDetails.size(); i++) {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_order_dialog, null);
                ((TextView) itemView.findViewById(R.id.tvRecycleType)).setText(recycleDetails.get(i).garbage);
                ((TextView) itemView.findViewById(R.id.tvRecycleQuantity)).setText(recycleDetails.get(i).quantity + "个");
                ((TextView) itemView.findViewById(R.id.tvRecyclePoint)).setText("+" + (Integer.parseInt(recycleDetails.get(i).point) >= 1000 ?
                        1000 : recycleDetails.get(i).point));
                llRecycleInfo.addView(itemView);
            }
        }
    }

    public void updatePoint(int point) {
        tvGainPoint.setText("对方将获得" + point + "益币");
    }
}
