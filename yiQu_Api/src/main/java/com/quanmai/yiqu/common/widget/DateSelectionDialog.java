package com.quanmai.yiqu.common.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.common.view.wheelview.ArrayWheelAdapter;
import com.common.view.wheelview.NumericWheelAdapter;
import com.common.view.wheelview.OnWheelChangedListener;
import com.common.view.wheelview.WheelView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.ui.booking.BookingSecond2Activity;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class DateSelectionDialog extends Dialog implements
        android.view.View.OnClickListener {
    private static int START_YEAR = 1900, END_YEAR = 2100;
    private WheelView wv_year, wv_month, wv_day;
    private Button mBtnButton1;// 底部按钮1
    private TextView mHtvTitle;// 标题
    String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    String[] months_little = {"4", "6", "9", "11"};
    final List<String> list_big, list_little;
    private WheelView date_string;
    String[] datas = {"","","","","","",""};//设置预约日期
    String[] times = {"全天","8:00~12:00","14:00~18:00"};//设置预约时段
    String way;

    public static void getDialog(Context context, String title, int year,
                                 int month, int day, OnDateTimeSetListener callBack) {
        DateSelectionDialog mBaseDialog;
        if (year == 0 || month == 0) {
            mBaseDialog = new DateSelectionDialog(context, callBack);
        } else {
            mBaseDialog = new DateSelectionDialog(context, year, month, day, callBack);
        }
        mBaseDialog.setTitle(title);
        mBaseDialog.setCancelable(true);
        mBaseDialog.setCanceledOnTouchOutside(true);
        mBaseDialog.show();
        // return mBaseDialog;
    }

    // public static void getDialog(Context context, String title,
    // OnDateTimeSetListener callBack) {
    // DatePickerDialog mBaseDialog = new DatePickerDialog(context, callBack);
    // mBaseDialog.setTitle(title);
    // mBaseDialog.setCancelable(true);
    // mBaseDialog.setCanceledOnTouchOutside(true);
    // mBaseDialog.show();
    // // return mBaseDialog;
    // }

    /**
     * 年月日日期选择器
     * @param context
     * @param callBack
     */
    public DateSelectionDialog(Context context, OnDateTimeSetListener callBack) {
        super(context, R.style.DataSheet);
        setContentView(R.layout.dialog_date);
        Calendar mCalendar = Calendar.getInstance();
        mCallBack = callBack;
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        init(year, month, day - 1);
        initEvents();
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window w = getWindow();
        w.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = w.getAttributes();
        w.setWindowAnimations(R.style.mDialogAnimation); // 设置窗口弹出动画
        lp.gravity = Gravity.BOTTOM;
        onWindowAttributesChanged(lp);
    }

    public DateSelectionDialog(Context context, int year, int month, int day,
                               OnDateTimeSetListener callBack) {
        super(context, R.style.DataSheet);
        setContentView(R.layout.dialog_date);
        mCallBack = callBack;
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        init(year, month - 1, day - 1);
        initEvents();
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    /**
     * 预约日期
     * @param context
     * @param year
     * @param month
     * @param day
     * @param date
     * @param callBack
     */
    public DateSelectionDialog(Context context, int year, int month, int day, String date,
                               OnStringDateTimeSetListener callBack) {
        super(context, R.style.DataSheet);
        setContentView(R.layout.dialog_date_string);
        way=date;
        mCallBackStr = callBack;
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        initString(year, month, day);
        initEvents();
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window w = getWindow();
        w.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = w.getAttributes();
        w.setWindowAnimations(R.style.mDialogAnimation); // 设置窗口弹出动画
        lp.gravity = Gravity.BOTTOM;
        onWindowAttributesChanged(lp);
    }

    /**
     * 预约时段
     * @param context
     * @param time
     * @param callBack
     */
    public DateSelectionDialog(Context context, String time,
                               OnStringDateTimeSetListener callBack) {
        super(context, R.style.DataSheet);
        setContentView(R.layout.dialog_date_string);
        way=time;
        mCallBackStr = callBack;
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        mBtnButton1 = (Button) findViewById(R.id.dialog_generic_btn_button1);
        date_string = (WheelView) findViewById(R.id.date_string);
        date_string.setAdapter(new ArrayWheelAdapter<String>(times, times.length));
        date_string.setCyclic(false);
        initEvents();
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window w = getWindow();
        w.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = w.getAttributes();
        w.setWindowAnimations(R.style.mDialogAnimation); // 设置窗口弹出动画
        lp.gravity = Gravity.BOTTOM;
        onWindowAttributesChanged(lp);
    }

    private void init(int year, int month, int day) {
        mBtnButton1 = (Button) findViewById(R.id.dialog_generic_btn_button1);
        // 年
        wv_year = (WheelView) findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
        // 月
        wv_month = (WheelView) findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(month);
        wv_day = (WheelView) findViewById(R.id.day);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
            else
                wv_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
        }
        wv_day.setCyclic(true);
        wv_day.setLabel("日");
        wv_day.setCurrentItem(day);

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big
                        .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                            .getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);
    }

    public void initString(int year, int month, int day) {
        mBtnButton1 = (Button) findViewById(R.id.dialog_generic_btn_button1);
        date_string = (WheelView) findViewById(R.id.date_string);
        // 判断大小月及是否闰年,用来确定"日"的数据
        datas[0] = StringUtil.getDaTaAdd0(month+"",2) + "月" + StringUtil.getDaTaAdd0(day+"",2) + "日 "+getWeek(year+"-"+month+"-"+day,"yyyy-MM-dd");
        for (int i = 1; i < 7; i++) {
            if (list_big.contains(String.valueOf(month))) {
                day = day + 1;
                if (day > 31) {
                    day = 1;
                    month += 1;
                }
                datas[i] = StringUtil.getDaTaAdd0(month+"",2) + "月" + StringUtil.getDaTaAdd0(day+"",2) + "日 "+getWeek(year+"-"+month+"-"+day,"yyyy-MM-dd");
            } else if (list_little.contains(String.valueOf(month))) {
                day = day + 1;
                if (day > 30) {
                    day = 1;
                    month += 1;
                }
                datas[i]=StringUtil.getDaTaAdd0(month+"",2) + "月" + StringUtil.getDaTaAdd0(day+"",2) + "日 "+getWeek(year+"-"+month+"-"+day,"yyyy-MM-dd");
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    day = day + 1;
                    if (day > 29) {
                        day = 1;
                        month += 1;
                    }
                    datas[i]=StringUtil.getDaTaAdd0(month+"",2) + "月" + StringUtil.getDaTaAdd0(day+"",2) + "日 "+getWeek(year+"-"+month+"-"+day,"yyyy-MM-dd");
                } else {
                    day = day + 1;
                    if (day > 28) {
                        day = 1;
                        month += 1;
                    }
                    datas[i]=StringUtil.getDaTaAdd0(month+"",2) + "月" + StringUtil.getDaTaAdd0(day+"",2) + "日 "+getWeek(year+"-"+month+"-"+day,"yyyy-MM-dd");
                }
            }
        }
        date_string.setAdapter(new ArrayWheelAdapter<String>(datas, 7));
        date_string.setCyclic(false);
        //date_string.setCurrentItem();
    }

    private void initEvents() {
        mBtnButton1.setOnClickListener(this);
    }

    @Override
    public void setTitle(CharSequence text) {
        if (text != null) {
            mHtvTitle.setText(text);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_generic_btn_button1:
                if (mCallBack != null) {
                    mCallBack.onDateTimeSet(wv_year.getCurrentItem() + START_YEAR,
                            wv_month.getCurrentItem() + 1,
                            wv_day.getCurrentItem() + 1);
                }
                if (mCallBackStr !=null){
                    if (way.equals("date")){
                        mCallBackStr.onDateTimeSet(datas[date_string.getCurrentItem()]);
                    }else if (way.equals("time")){
                        mCallBackStr.onDateTimeSet(times[date_string.getCurrentItem()]);
                    }

                }
                if (isShowing()) {
                    dismiss();
                }
                break;

        }
    }

    public static String getWeek(String date,String formatstr){
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat(formatstr);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "周日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "周一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "周二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "周三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "周四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "周五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "周六";
        }
        return Week;
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    public interface OnDateTimeSetListener {
        void onDateTimeSet(int year, int monthOfYear, int day);
    }
    public interface OnStringDateTimeSetListener {
        void onDateTimeSet(String strdate);
    }
    private  OnDateTimeSetListener mCallBack;
    private  OnStringDateTimeSetListener mCallBackStr;

}
