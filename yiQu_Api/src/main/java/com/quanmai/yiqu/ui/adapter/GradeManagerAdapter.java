package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.quanmai.yiqu.api.vo.InspectionRecordInfo;
import com.quanmai.yiqu.ui.grade.ScoreDetailActivity;
import com.quanmai.yiqu.ui.mys.record.view.QMonthCellDescriptor;
import com.quanmai.yiqu.ui.mys.record.view.QMonthDescriptor;
import com.quanmai.yiqu.ui.mys.record.view.QMonthView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by zhanjinj on 16/3/24.
 * 评分管理适配器
 */
public class GradeManagerAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<InspectionRecordInfo> mList;
    View.OnClickListener clickListener;

    public GradeManagerAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<InspectionRecordInfo>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InspectionRecordInfo info = mList.get(position);

        QMonthView monthView = QMonthView.create(parent, inflater, null);
        Calendar monthCounter = getFirstDayOfMonth(info.year, info.month);

        Date date = monthCounter.getTime();
        SimpleDateFormat monthNameFormat = new SimpleDateFormat("MMMM yyyy",
                Locale.getDefault());
        QMonthDescriptor descriptor = new QMonthDescriptor(
                monthCounter.get(MONTH), monthCounter.get(YEAR), date,
                monthNameFormat.format(date));

        monthView.setOnClickListener(cellOnClickListener);
        monthView.init(descriptor,
                getMonthCells(descriptor, monthCounter, info.days), true,
                null, null, info.days.size());

        if (clickListener != null) {
            monthView.setOnTitleClickListener(clickListener);
        }

        return monthView;
    }

    public void clear() {
        mList.clear();
    }

    public void addAll(List<InspectionRecordInfo> list) {
        clear();
        for (int i = 0; i < list.size(); i++) {
            this.mList.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    public void add(List<InspectionRecordInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            this.mList.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    public static Calendar getFirstDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c;
    }

    List<List<QMonthCellDescriptor>> getMonthCells(QMonthDescriptor month,
                                                   Calendar startCal, HashMap<Integer, String> days) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(startCal.getTime());
        List<List<QMonthCellDescriptor>> cells = new ArrayList<List<QMonthCellDescriptor>>();
        cal.set(DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(DAY_OF_WEEK) - 1;
        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
        if (offset > 0) {
            offset -= 7;
        }
        cal.add(Calendar.DATE, offset);
        while ((cal.get(MONTH) < month.getMonth() + 1 || cal.get(YEAR) < month
                .getYear()) && cal.get(YEAR) <= month.getYear()) {
            // Utils.E("Building week row starting at "+cal.getTime());
            List<QMonthCellDescriptor> weekCells = new ArrayList<QMonthCellDescriptor>();
            cells.add(weekCells);
            for (int c = 0; c < 7; c++) {
                Date date = cal.getTime();
                boolean isCurrentMonth = cal.get(MONTH) == month.getMonth();
                boolean isToday = false;
                int value = cal.get(DAY_OF_MONTH);
                // Utils.E("Building week row starting at "+cal.getTime());
                String manhour = new String();
                // if(isCurrentMonth)
                // {
                if (isCurrentMonth && days.get(value) != null) {
                    manhour = days.get(value);
                }
                // }
                QMonthCellDescriptor.RangeState rangeState = QMonthCellDescriptor.RangeState.NONE;
                weekCells.add(new QMonthCellDescriptor(date, isCurrentMonth,
                        false, false, isToday, false, value, manhour,
                        rangeState));
                cal.add(DATE, 1);
            }
        }
        return cells;
    }

    View.OnClickListener cellOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            QMonthCellDescriptor cellDescriptor = (QMonthCellDescriptor) v.getTag();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String datetime = format.format(cellDescriptor.getDate());
            Intent intent = new Intent(mContext, ScoreDetailActivity.class);
            intent.putExtra("datetime", datetime);
            mContext.startActivity(intent);
        }
    };
}
