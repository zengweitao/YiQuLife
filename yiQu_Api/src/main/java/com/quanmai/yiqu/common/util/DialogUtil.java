package com.quanmai.yiqu.common.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.DialogCommunityInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesMapInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.Adapter.DialogCommunityAdapter;
import com.quanmai.yiqu.common.widget.CircularImageView;
import com.quanmai.yiqu.ui.booking.Adapter.ListViewShoppingCarAdapter;

import java.util.List;
import java.util.Map;

public class DialogUtil {

    public interface OnDialogSelectId {
        void onClick(View v);
    }

    /**
     * 取消订单的Dialog
     *
     * @param context
     * @param onDialogSelectId
     * @return
     */
    public static Dialog showDeleteDialog(Context context, String msg,
                                          final OnDialogSelectId onDialogSelectId) {
        final Dialog dlg = getDialog(context, msg, "是",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogSelectId.onClick(null);
                        dialog.dismiss();
                    }

                }, "否", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dlg.show();

        return dlg;
    }

    /**
     * 确认的Dialog
     *
     * @param context
     * @param onDialogSelectId
     * @return
     */
    public static Dialog showConfirmDialog(final Context context, String msg,
                                           final OnDialogSelectId onDialogSelectId) {
        final Dialog dlg = getDialog(context, msg, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogSelectId.onClick(null);
                        dialog.dismiss();
                    }

                }, "取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dlg.show();

        return dlg;
    }

    /**
     * 确认删除的Dialog
     *
     * @param context
     * @param onDialogSelectId
     * @return
     */
    public static Dialog showDelDialog(final Activity context,
                                       final OnDialogSelectId onDialogSelectId) {
        final Dialog dialog = getDialog(context, "是否删除？", "是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogSelectId.onClick(null);
                dialog.dismiss();
            }

        }, "否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        dialog.show();
        return dialog;
    }

    /**
     * 拨打电话选择弹出
     *
     * @param context
     * @param content
     * @param onClickListener
     * @return
     */
    public static Dialog getCallDialog(Context context, String content, OnClickListener onClickListener) {
        return getPopupConfirmDialog(context, "是否拨打电话", content, "拨打", "取消", onClickListener);
    }

    /**
     * 选择头像（从本地相册选取或直接拍照）
     */
    public static Dialog chooseHeadImgDialog(final Context context, final OnClickListener onClickListener) {
        Dialog headImgDialog = new Dialog(context, R.style.MyDialogStyle);
        headImgDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(0));
        headImgDialog.setCanceledOnTouchOutside(true);
        headImgDialog.setContentView(R.layout.dialog_choose_head_img);

        headImgDialog.findViewById(R.id.btn_local_photo).setOnClickListener(onClickListener);
        headImgDialog.findViewById(R.id.btn_take_photos).setOnClickListener(onClickListener);
        headImgDialog.findViewById(R.id.btn_cancel).setOnClickListener(onClickListener);

        Window window = headImgDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        window.setAttributes(params);

        return headImgDialog;
    }

    /**
     * 选择性别的Dialog
     */
    public static Dialog chooseSexDialog(final Context context, final OnClickListener onClickListener) {
        Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_choose_sex);
        dialog.findViewById(R.id.buttonConfirm).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.buttonShare).setOnClickListener(onClickListener);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        window.setAttributes(params);
        return dialog;
    }

    public static Dialog getPhoneDialog(final Context context, String name, final String phone) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_phone,
                null);
        dialog.setContentView(view);
        ((TextView) view.findViewById(R.id.tv_name)).setText(name);
        ((TextView) view.findViewById(R.id.tv_phone)).setText(phone);
        view.findViewById(R.id.tv_cancel).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        view.findViewById(R.id.tv_call).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                .parse("tel:" + phone));
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });
        // btn2.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // if (listener2 != null) {
        // listener2.onClick(dialog, 0);
        // }
        //
        // }
        // });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        // dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        dialogWindow.setAttributes(lp);
        return dialog;
    }

    public static Dialog getDialog(final Context context, String title,
                                   String confirm, final DialogInterface.OnClickListener listener1,
                                   String cancel, final DialogInterface.OnClickListener listener2) {
        final Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_common, null);
        TextView message = (TextView) view.findViewById(R.id.textViewTitle);
        message.setText(title);
        TextView btn1 = (TextView) view.findViewById(R.id.buttonConfirm);
        TextView btn2 = (TextView) view.findViewById(R.id.buttonCancel);
        dialog.setContentView(view);
        btn1.setText(confirm);
        btn2.setText(cancel);
        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener1 != null) {
                    listener1.onClick(dialog, v.getId());
                }

            }
        });
        btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener2 != null) {
                    listener2.onClick(dialog, v.getId());
                }

            }
        });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.mDialogAnimation); //设置弹出动画
        return dialog;
    }

    /**
     * 确定/取消 弹窗
     *
     * @param context
     * @param title
     * @param onClickListener
     * @return
     */
    public static Dialog getConfirmDialog(Context context, String title, OnClickListener onClickListener) {
        return getConfirmDialog(context, title, "确定", "取消", onClickListener);
    }

    /**
     * 确定/取消 弹窗
     *
     * @param context
     * @param title
     * @param confirm         确定按键内容
     * @param cancel          取消按键内容
     * @param onClickListener
     * @return
     */
    public static Dialog getConfirmDialog(Context context, String title,
                                          String confirm, String cancel, OnClickListener onClickListener) {
        Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.textViewTitle);
        TextView btnConfirm = (TextView) view.findViewById(R.id.buttonConfirm);
        TextView btnCancel = (TextView) view.findViewById(R.id.buttonCancel);

        tvTitle.setText(title);
        btnConfirm.setText(confirm);
        btnCancel.setText(cancel);
        dialog.setContentView(view);

        btnConfirm.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.mDialogAnimation); //设置弹出动画
        return dialog;
    }

    /**
     * 通用对话框（样式1，中部弹出）
     *
     * @param context
     * @param title
     * @param confirm
     * @param cancel
     * @param onClickListener
     * @return
     */
    public static Dialog getPopupConfirmDialog(Context context, String title, String content,
                                               String confirm, String cancel, OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_popup, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) view.findViewById(R.id.tvPhone);
        TextView btnConfirm = (TextView) view.findViewById(R.id.buttonConfirm);
        TextView btnCancel = (TextView) view.findViewById(R.id.buttonCancel);


        tvTitle.setText(title);
        tvContent.setText(content);
        btnConfirm.setText(confirm);
        btnCancel.setText(cancel);
        dialog.setContentView(view);

        btnConfirm.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.PopupAnim); //设置弹出动画
        return dialog;
    }

    /**
     * 通用对话框（样式1，中部弹出,有输入框）
     *
     * @param context
     * @param title
     * @param confirm
     * @param cancel
     * @return
     */
    public static Dialog getPopupConfirmEditTextDialog(Context context, String title,
                                                       String confirm, String cancel, final OnDialogClickListener listener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_popup, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) view.findViewById(R.id.tvPhone);
        final TextView btnConfirm = (TextView) view.findViewById(R.id.buttonConfirm);
        TextView btnCancel = (TextView) view.findViewById(R.id.buttonCancel);
        final EditText editText = (EditText) view.findViewById(R.id.editText);

        tvContent.setVisibility(View.GONE);
        editText.setVisibility(View.VISIBLE);

        tvTitle.setText(title);
        btnConfirm.setText(confirm);
        btnCancel.setText(cancel);
        dialog.setContentView(view);

        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(editText.getText().toString());
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.PopupAnim); //设置弹出动画
        return dialog;
    }

    /**
     * 通用对话框（样式1，中部弹出,有输入框,文字提醒）
     *
     * @param context
     * @param title
     * @param confirm
     * @param cancel
     * @param hint
     * @return
     */
    public static Dialog getPopupConfirmEditTextDialog(Context context, String title,
                                                       String confirm, String cancel, String hint, final OnDialogClickListener listener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_popup, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) view.findViewById(R.id.tvPhone);
        final TextView btnConfirm = (TextView) view.findViewById(R.id.buttonConfirm);
        TextView btnCancel = (TextView) view.findViewById(R.id.buttonCancel);
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        editText.setHint(hint);

        tvContent.setVisibility(View.GONE);
        editText.setVisibility(View.VISIBLE);

        tvTitle.setText(title);
        btnConfirm.setText(confirm);
        btnCancel.setText(cancel);
        dialog.setContentView(view);

        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(editText.getText().toString());
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.PopupAnim); //设置弹出动画
        return dialog;
    }

    public interface OnDialogClickListener {
        void onClick(String str);
    }

    /**
     * 通用对话框2（中部弹出）
     *
     * @param context
     * @param content
     * @param confirm
     * @param cancel
     * @param onClickListener
     * @return
     */
    public static Dialog getPopupConfirmDialog2(Context context, String content, String confirm, String cancel, OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_popup2, null);
        TextView tvContent = (TextView) view.findViewById(R.id.tvExplain);
        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);

        tvContent.setText(content);
        btnConfirm.setText(confirm);
        btnCancel.setText(cancel);
        dialog.setContentView(view);

        btnConfirm.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.PopupAnim); //设置弹出动画
        return dialog;
    }

    /**
     * 选择扫描结果对话框（中部弹出）
     *
     * @param context
     * @param content
     * @param onClickListener
     * @return
     */
    public static String tab_jurisdiction=null;
    public static Dialog getPopupChooseDialog(Context context, String content, int whichcode, String usertype, OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_scan_result, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        Button give_present = (Button) view.findViewById(R.id.give_present);
        Button scene_recycle = (Button) view.findViewById(R.id.scene_recycle);
        Button get_user_details = (Button) view.findViewById(R.id.get_user_details);

        Button inspection_grade = (Button) view.findViewById(R.id.inspection_grade);
        Button sand_bag_of_hand = (Button) view.findViewById(R.id.sand_bag_of_hand);
        Button bag_put_in_storage = (Button) view.findViewById(R.id.bag_put_in_storage);

        LinearLayout linear_usercode= (LinearLayout) view.findViewById(R.id.linear_usercode);
        LinearLayout linear_bagcode= (LinearLayout) view.findViewById(R.id.linear_bagcode);

        if (whichcode==1){//扫描袋子码
            linear_usercode.setVisibility(View.GONE);
            linear_bagcode.setVisibility(View.VISIBLE);
            if (usertype.contains(UserInfo.USER_ALL)){
                inspection_grade.setVisibility(View.VISIBLE);
                sand_bag_of_hand.setVisibility(View.VISIBLE);
                bag_put_in_storage.setVisibility(View.VISIBLE);
            }
            if (usertype.contains(UserInfo.USER_INSPECTION)){
                tab_jurisdiction=UserInfo.USER_INSPECTION;
                inspection_grade.setVisibility(View.VISIBLE);
            }
            if (usertype.contains(UserInfo.USER_HANDWORK)){
                tab_jurisdiction=UserInfo.USER_HANDWORK;
                sand_bag_of_hand.setVisibility(View.VISIBLE);
            }
            if (usertype.contains(UserInfo.USER_WAREHOUSE)){
                tab_jurisdiction=UserInfo.USER_WAREHOUSE;
                bag_put_in_storage.setVisibility(View.VISIBLE);
            }
        }else if (whichcode==2){//扫描用户码
            linear_bagcode.setVisibility(View.GONE);
            linear_usercode.setVisibility(View.VISIBLE);
            if (usertype.contains(UserInfo.USER_ALL)){
                give_present.setVisibility(View.VISIBLE);
                scene_recycle.setVisibility(View.VISIBLE);
                get_user_details.setVisibility(View.VISIBLE);
            }
            if (usertype.contains(UserInfo.USER_GIFT_GIVING)){
                tab_jurisdiction=UserInfo.USER_GIFT_GIVING;
                give_present.setVisibility(View.VISIBLE);
                get_user_details.setVisibility(View.VISIBLE);
            }
            if (usertype.contains(UserInfo.USER_RECYCLE)){
                tab_jurisdiction=UserInfo.USER_RECYCLE;
                scene_recycle.setVisibility(View.VISIBLE);
            }
            if (usertype.contains(UserInfo.USER_HANDWORK)){
                if (usertype.contains(UserInfo.USER_HANDWORK)){
                    tab_jurisdiction=UserInfo.USER_HANDWORK;
                }
                get_user_details.setVisibility(View.VISIBLE);
            }
        }else if (whichcode==0){//手动输入是弹出
            linear_bagcode.setVisibility(View.VISIBLE);
            linear_usercode.setVisibility(View.VISIBLE);
            if (usertype.contains(UserInfo.USER_ALL)){
                inspection_grade.setVisibility(View.VISIBLE);
                sand_bag_of_hand.setVisibility(View.VISIBLE);
                bag_put_in_storage.setVisibility(View.VISIBLE);
                give_present.setVisibility(View.VISIBLE);
                scene_recycle.setVisibility(View.VISIBLE);
                get_user_details.setVisibility(View.VISIBLE);
            }
        }else if (whichcode==4){
            linear_usercode.setVisibility(View.GONE);
            linear_bagcode.setVisibility(View.VISIBLE);
            if (usertype.contains(UserInfo.USER_ALL)){
                sand_bag_of_hand.setVisibility(View.VISIBLE);
                bag_put_in_storage.setVisibility(View.VISIBLE);
            }
            if (usertype.contains(UserInfo.USER_HANDWORK)){
                tab_jurisdiction=UserInfo.USER_HANDWORK;
                sand_bag_of_hand.setVisibility(View.VISIBLE);
            }
            if (usertype.contains(UserInfo.USER_WAREHOUSE)){
                tab_jurisdiction=UserInfo.USER_WAREHOUSE;
                bag_put_in_storage.setVisibility(View.VISIBLE);
            }
        }
        tvTitle.setText(content);
        dialog.setContentView(view);

        give_present.setOnClickListener(onClickListener);
        scene_recycle.setOnClickListener(onClickListener);
        get_user_details.setOnClickListener(onClickListener);
        inspection_grade.setOnClickListener(onClickListener);
        sand_bag_of_hand.setOnClickListener(onClickListener);
        bag_put_in_storage.setOnClickListener(onClickListener);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.PopupAnim); //设置弹出动画
        return dialog;
    }

    /**
     * 选择小区对话框（中部弹出）
     *
     * @param context
     * @param title
     * @param onClickListener
     * @return
     */
    public static Dialog getChooseCommunityDialog(Context context, String title , List<DialogCommunityInfo.CommListBean> infos, final OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_community_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        ListView listview_community= (ListView) view.findViewById(R.id.listview_community);
        DialogCommunityAdapter mAdapter = new DialogCommunityAdapter(context, new DialogCommunityAdapter.setOnCommunityClick() {
            @Override
            public void OnCommunityClick(View v) {
                onClickListener.onClick(v);
            }
        });
        listview_community.setAdapter(mAdapter);
        mAdapter.add(infos);



        tvTitle.setText(title);
        dialog.setContentView(view);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.PopupAnim); //设置弹出动画
        return dialog;
    }

    /**
     * 选择扫描结果对话框（中部弹出）当扫描户码时选择
     *
     * @param context
     * @param content
     * @param onClickListener
     * @return
     */
    public static Dialog getPopupChooseHouseCodeDialog(Context context, String content, OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_scan_house_code, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        Button button_get_bag = (Button) view.findViewById(R.id.button_get_bag);
        Button button_putaway_bag = (Button) view.findViewById(R.id.button_putaway_bag);

        tvTitle.setText(content);
        dialog.setContentView(view);

        button_get_bag.setOnClickListener(onClickListener);
        button_putaway_bag.setOnClickListener(onClickListener);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.PopupAnim); //设置弹出动画
        return dialog;
    }
    /**
     * 选择输入类型对话框（中部弹出）
     *
     * @param context
     * @param content
     * @param onClickListener
     * @return
     */
    public static Dialog getPopupInPutDialog(Context context, String content, String usertype, OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_scan_result, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        Button give_present = (Button) view.findViewById(R.id.give_present);
        Button scene_recycle = (Button) view.findViewById(R.id.scene_recycle);
        Button get_user_details = (Button) view.findViewById(R.id.get_user_details);

        Button inspection_grade = (Button) view.findViewById(R.id.inspection_grade);
        Button sand_bag_of_hand = (Button) view.findViewById(R.id.sand_bag_of_hand);
        Button bag_put_in_storage = (Button) view.findViewById(R.id.bag_put_in_storage);
        Button get_bag = (Button) view.findViewById(R.id.get_bag);

        LinearLayout linear_usercode= (LinearLayout) view.findViewById(R.id.linear_usercode);
        LinearLayout linear_bagcode= (LinearLayout) view.findViewById(R.id.linear_bagcode);


        linear_bagcode.setVisibility(View.VISIBLE);
        linear_usercode.setVisibility(View.VISIBLE);
        get_bag.setVisibility(View.VISIBLE);
        if (usertype.contains(UserInfo.USER_ALL)){
            inspection_grade.setVisibility(View.VISIBLE);
            sand_bag_of_hand.setVisibility(View.VISIBLE);
            bag_put_in_storage.setVisibility(View.VISIBLE);
            give_present.setVisibility(View.VISIBLE);
            scene_recycle.setVisibility(View.VISIBLE);
            get_user_details.setVisibility(View.VISIBLE);
        }
        if (usertype.contains(UserInfo.USER_INSPECTION)){
            inspection_grade.setVisibility(View.VISIBLE);
        }
        if (usertype.contains(UserInfo.USER_HANDWORK)){
            sand_bag_of_hand.setVisibility(View.VISIBLE);
        }
        if (usertype.contains(UserInfo.USER_WAREHOUSE)){
            bag_put_in_storage.setVisibility(View.VISIBLE);
        }
        if (usertype.contains(UserInfo.USER_GIFT_GIVING)){
            give_present.setVisibility(View.VISIBLE);
        }
        if (usertype.contains(UserInfo.USER_RECYCLE)){
            scene_recycle.setVisibility(View.VISIBLE);
        }
        if (usertype.contains(UserInfo.USER_HANDWORK)||usertype.contains(UserInfo.USER_GIFT_GIVING)){
            give_present.setVisibility(View.VISIBLE);
        }

        tvTitle.setText(content);
        dialog.setContentView(view);


        give_present.setOnClickListener(onClickListener);
        scene_recycle.setOnClickListener(onClickListener);
        get_user_details.setOnClickListener(onClickListener);
        inspection_grade.setOnClickListener(onClickListener);
        sand_bag_of_hand.setOnClickListener(onClickListener);
        bag_put_in_storage.setOnClickListener(onClickListener);
        get_bag.setOnClickListener(onClickListener);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.PopupAnim); //设置弹出动画
        return dialog;
    }


    /**
     * 二维码Dialog
     *
     * @param context
     */
    public static void showCodeDialog(final Context context, final String string, final OnDialogSelectId alertSelectId) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(context, R.layout.dialog_code, null);
        TextView tv_code = (TextView) view.findViewById(R.id.tv_code);
        tv_code.setText(string);
        TextView btn1 = (TextView) view.findViewById(R.id.buttonConfirm);
        dialog.setContentView(view);
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                alertSelectId.onClick(v);
            }
        });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        dialogWindow.setAttributes(lp);
        dialog.show();
    }


    private static long exitTime = 0;

    /**
     * 退出Toast
     *
     * @param context
     * @param onDialogSelectId
     */
    public static void showExitToast(Context context, OnDialogSelectId onDialogSelectId) {
        Toast toast = Toast.makeText(context, "再按一次退出益趣生活", Toast.LENGTH_SHORT);
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast.show();
            exitTime = System.currentTimeMillis();
        } else {
            toast.cancel();
            onDialogSelectId.onClick(null);
        }
    }

    /**
     * 回收预约弹窗
     *
     * @param context
     * @param title
     * @param date
     * @param address
     * @param onClickListener
     * @return
     */
    public static Dialog getAppointmentDialog(Context context, String title, String date, String address,
                                              OnClickListener onClickListener) {
        Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_appointment_order, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvRecycleDate = (TextView) view.findViewById(R.id.tvRecycleDate);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);

        tvTitle.setText(title);
        tvRecycleDate.setText(date);
        tvAddress.setText(address);
        btnCancel.setText("忽略");
        btnConfirm.setText("接单");

        btnCancel.setOnClickListener(onClickListener);
        btnConfirm.setOnClickListener(onClickListener);

        dialog.setContentView(view);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(R.style.PopupAnim);
        return dialog;
    }

    /**
     * 功能选择弹窗
     *
     * @param context
     * @param button1Text
     * @param button2Text
     * @param onClickListener
     * @return
     */
    public static Dialog getSelectDialog(Context context, String button1Text, String button2Text, OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_two, null);
        Button btnOne = (Button) view.findViewById(R.id.buttonOne);
        Button btnTwo = (Button) view.findViewById(R.id.buttonTwo);
        Button btnCancel = (Button) view.findViewById(R.id.buttonCancel);

        btnOne.setText(button1Text);
        btnTwo.setText(button2Text);

        btnOne.setOnClickListener(onClickListener);
        btnTwo.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        lp.width = LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.mDialogAnimation);
        return dialog;
    }

    /**
     * 周边优惠（未登陆/未绑定）提示弹窗
     *
     * @param context
     * @param strRemind
     * @param strConfirm
     * @param onClickListener
     * @return
     */
    public static Dialog getNotLoginRemindDialog(Context context, String strRemind, String strConfirm, OnClickListener onClickListener) {
        Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_not_login_remind, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) view.findViewById(R.id.tvExplain);
        RelativeLayout rlClose = (RelativeLayout) view.findViewById(R.id.rlClose);
        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);

        tvContent.setText(StringUtil.stringNullFilter(strRemind));
        btnConfirm.setText(strConfirm);
        btnConfirm.setOnClickListener(onClickListener);
        rlClose.setOnClickListener(onClickListener);

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(R.style.PopupAnim);

        return dialog;
    }

    /**
     * 住户绑定失败弹窗
     *
     * @param context
     * @param name            用户名
     * @param phone           用户电话
     * @param onClickListener
     * @return
     */
    public static Dialog getBindingFailDialog(Context context, String name, String phone, OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_binding_fail, null);
        dialog.setContentView(view);

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnAppeal = (Button) view.findViewById(R.id.btnAppeal);
        Button btnApply = (Button) view.findViewById(R.id.btnApply);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);

        tvName.setText("户主：" + StringUtil.nameEncrypt(name));
        tvPhone.setText("手机：" + StringUtil.phoneEncrypt(phone));

        btnAppeal.setOnClickListener(onClickListener);
        btnApply.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(R.style.PopupAnim);

        return dialog;
    }

    /**
     * app更新弹窗
     *
     * @param context
     * @param description
     * @param onClickListener
     * @return
     */
    public static Dialog getUpdateDialog(Context context, String updatemode, String description, OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
        dialog.setContentView(view);

        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        LinearLayout includeBtnGroup = (LinearLayout) view.findViewById(R.id.includeBtnGroup);
        Button btnCancel = (Button) includeBtnGroup.findViewById(R.id.btnCancel);
        Button btnConfirm = (Button) includeBtnGroup.findViewById(R.id.btnConfirm);
        Button btnUpdateNow = (Button) view.findViewById(R.id.btnUpdateNow);

        if ("0".equals(updatemode)) {
            includeBtnGroup.setVisibility(View.GONE);
            btnUpdateNow.setVisibility(View.VISIBLE);
        }

        tvDescription.setText(StringUtil.stringNullFilter(description).replace("\\n", "\n"));
        btnCancel.setText("暂不更新");
        btnConfirm.setText("更新");

        btnConfirm.setOnClickListener(onClickListener);
        btnUpdateNow.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(R.style.PopupAnim);

        return dialog;
    }

    /**
     * 答题赚积分对话窗
     *
     * @param context
     * @param drawable
     * @param isRight
     * @param point
     * @param onClickListener
     * @return
     */
    public static Dialog getGainIntegral(Context context, Drawable drawable, Boolean isRight, int point, OnClickListener onClickListener) {
        Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_gain_integral, null);
        CircularImageView imgAdvertisement = (CircularImageView) mView.findViewById(R.id.imgAdvertisement);
        ImageButton btnClose = (ImageButton) mView.findViewById(R.id.btnClose);
        Button btnAgain = (Button) mView.findViewById(R.id.btnAgain);
        LinearLayout llRight = (LinearLayout) mView.findViewById(R.id.llRight);
        Button btnChangeBag = (Button) mView.findViewById(R.id.btnChangeBag);
        Button btnContinue = (Button) mView.findViewById(R.id.btnContinue);
        TextView tvContent = (TextView) mView.findViewById(R.id.tvExplain);
        TextView tvResult = (TextView) mView.findViewById(R.id.tvResult);

        if (isRight) {
            llRight.setVisibility(View.VISIBLE);
            btnAgain.setVisibility(View.GONE);
            tvResult.setText("回答正确");
            tvContent.setText("您已获得 " + point + " 积分");
        } else {
            llRight.setVisibility(View.GONE);
            btnAgain.setVisibility(View.VISIBLE);
            tvResult.setText("回答错误");
            tvContent.setText("可以重新答题哦");
        }

        if (drawable != null) { //设置广告图片
            imgAdvertisement.setImageDrawable(drawable);
        }

        btnContinue.setOnClickListener(onClickListener);
        btnChangeBag.setOnClickListener(onClickListener);
        btnAgain.setOnClickListener(onClickListener);
        btnClose.setOnClickListener(onClickListener);

        dialog.setContentView(mView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.PopupAnim);
        window.setGravity(Gravity.CENTER);

        return dialog;
    }

    /**
     * 住户编码二维码弹窗
     *
     * @param context
     * @param str     住户编码
     * @param strX    加密过的住户编码
     * @return
     */
    public static Dialog getUserQRCodeDialog(Context context, String str, String strX) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        if (TextUtils.isEmpty(strX)) {
            Utils.showCustomToast(context, "获取二维码失败，请稍候再试");
            return null;
        }

        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_user_qr_code, null);
        TextView tvUserQRCode = (TextView) mView.findViewById(R.id.tvUserQRCode);
        tvUserQRCode.setText(StringUtil.stringFilter(str));

        ImageView imgQRCode = (ImageView) mView.findViewById(R.id.imgQRCode);
        Bitmap bitmap = BitmapUtils.create2DCode(strX, 300, 300);
        imgQRCode.setBackground(new BitmapDrawable(bitmap));

        Button btnConfirm = (Button) mView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog.setContentView(mView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.PopupAnim);
        window.setGravity(Gravity.CENTER);

        return dialog;
    }

    /**
     * 现场回收和预约回收购物车弹框
     *
     * @param context
     * @param onClickListener
     * @return
     *//*
    public static Dialog getShoppingCarDialog(Context context, Map<String, RecycleGarbagesMapInfo> garbageMap, OnClickListener onClickListener) {
        Dialog dialog = new Dialog(context, R.style.MyDialogStyleShopping);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_shopping_car, null);
        LinearLayout linear_shoppingcar_delete = (LinearLayout) view.findViewById(R.id.linear_shoppingcar_delete);
        ListView listview_shoppingcar = (ListView) view.findViewById(R.id.listview_shoppingcar);
        ListViewShoppingCarAdapter mListViewShoppingCarAdapter=new ListViewShoppingCarAdapter(context);
        listview_shoppingcar.setAdapter(mListViewShoppingCarAdapter);
        mListViewShoppingCarAdapter.add(garbageMap);
        dialog.setContentView(view);

        linear_shoppingcar_delete.setOnClickListener(onClickListener);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 100; // 新位置Y坐标
        lp.width = LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.mDialogAnimation); //设置弹出动画
        return dialog;
    }*/

}



