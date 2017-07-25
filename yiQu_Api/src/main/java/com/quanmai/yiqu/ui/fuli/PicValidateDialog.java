package com.quanmai.yiqu.ui.fuli;

import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.PicValidateInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;

public class PicValidateDialog extends Dialog implements OnClickListener {

    PicValidateAdapter adapter;
    GridView mGridView;
    BaseActivity mContext;
    TextView tv_title;

    public PicValidateDialog(BaseActivity context) {
        super(context, R.style.Theme_UMDialog);
        mContext = context;
        setContentView(R.layout.dialog_pic_validate);
        setCanceledOnTouchOutside(false);
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // lp.width = LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        onWindowAttributesChanged(lp);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mGridView = (GridView) findViewById(R.id.gridview);

        findViewById(R.id.tv_submit).setOnClickListener(this);
        findViewById(R.id.iv_cancel).setOnClickListener(this);
    }

    String answers = new String();

    public void show(String answers, CommonList<PicValidateInfo> data) {
        this.answers = answers;
        tv_title.setText(data.name);
        adapter = new PicValidateAdapter(mContext, data);
        mGridView.setAdapter(adapter);
        show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                String id = adapter.getCheckId();
                if (id.equals("")) {
                    Utils.showCustomToast(mContext, "请选择图片");
                    return;
                }
                if (!answers.equals(id)) {
                    Utils.showCustomToast(mContext, "选择错误");
                    return;
                }
                mContext.showLoadingDialog("请稍候");
                Api.get().PicValidateSuccess(mContext,
                        new ApiRequestListener<String>() {

                            @Override
                            public void onSuccess(String point, String times) {
                                mContext.dismissLoadingDialog();
                                FuliDialog dialog = new FuliDialog(mContext);
                                dialog.show(point, "0", times, false);
                                dismiss();
                            }

                            @Override
                            public void onFailure(String msg) {
                                mContext.dismissLoadingDialog();
                                Utils.showCustomToast(mContext, msg);
                            }
                        });

                break;
            case R.id.iv_cancel:
                dismiss();
                break;
            default:
                break;
        }

    }

}
