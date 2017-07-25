package com.quanmai.yiqu.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;

import static com.alibaba.sdk.android.feedback.xblink.config.GlobalConfig.context;

/**
 * Created by yin on 16/4/13.
 */
public class CustomDialog extends Dialog {
    Context mContext;
//    TextView title;
//    TextView message;
//    Button btn_n;
//    Button btn_p;

    public CustomDialog(Context context) {
        super(context);
        mContext = context;
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private DialogInterface.OnClickListener
                positiveButtonClickListener,
                negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context, R.style.MyDialogStyle);
            View layout = inflater.inflate(R.layout.layout_material_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            ((TextView) layout.findViewById(R.id.textViewTitle)).setText(title);
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_p))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_p)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(
                                    dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_p).setVisibility(
                        View.GONE);
            }

            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_n))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_n))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(
                                            dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_n).setVisibility(
                        View.GONE);
            }

            if (message != null) {
                ((TextView) layout.findViewById(
                        R.id.message)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView,
                                new LayoutParams(
                                        LayoutParams.WRAP_CONTENT,
                                        LayoutParams.WRAP_CONTENT));
            }

            dialog.setContentView(layout);


            return dialog;
        }

        /**
         * 创建个人评分提示dialog
         * @return 返回自定义dialog对象
         */
        public CustomDialog create2(){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context, R.style.MyDialogStyle);
            View layout = inflater.inflate(R.layout.layout_personal_grade_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            ((TextView) layout.findViewById(R.id.textViewTitle)).setText(title);
            if (positiveButtonText!=null){
                ((Button) layout.findViewById(R.id.btn_p_grade))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null){
                    ((Button) layout.findViewById(R.id.btn_p_grade)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(
                                    dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            }else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_p).setVisibility(
                        View.GONE);
            }

            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_n_grade))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_n_grade))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(
                                            dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_n_grade).setVisibility(
                        View.GONE);
            }

            if (message != null) {
                ((TextView) layout.findViewById(
                        R.id.message)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView,
                                new LayoutParams(
                                        LayoutParams.WRAP_CONTENT,
                                        LayoutParams.WRAP_CONTENT));
            }

            dialog.setContentView(layout);



            return dialog;
        }

    }

    }

//}


//    public void setPositiveButton(String text,View.OnClickListener listener){
//        btn_p.setText(text);
//        btn_p.setOnClickListener(listener);
//    }
//
//    public void setNegativeButton(String text,View.OnClickListener listener){
//        btn_n.setText(text);
//        btn_n.setOnClickListener(listener);
//    }
//
//    public void addTitle(CharSequence title) {
//        this.title.setText(title);
//    }
//
//    public void addMessage(String message) {
//        this.message.setText(message);
//    }
//}
