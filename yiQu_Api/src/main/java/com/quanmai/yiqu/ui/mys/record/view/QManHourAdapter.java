package com.quanmai.yiqu.ui.mys.record.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.quanmai.yiqu.ui.mys.record.ManHour;

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

public class QManHourAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<ManHour> list;
    final QMonthView.Listener listener;
    OnClickListener clickListener;

    public QManHourAdapter(Context context, QMonthView.Listener listener) {
        this.list = new ArrayList<ManHour>();
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ManHour getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ManHour info = getItem(position);
        QMonthView monthView = QMonthView.create(parent, inflater, listener);
        Calendar monthCounter = getfMonth(info.year, info.month);

        Date date = monthCounter.getTime();
        SimpleDateFormat monthNameFormat = new SimpleDateFormat("MMMM yyyy",
                Locale.getDefault());
        QMonthDescriptor descriptor = new QMonthDescriptor(
                monthCounter.get(MONTH), monthCounter.get(YEAR), date,
                monthNameFormat.format(date));
        monthView.init(descriptor,
                getMonthCells(descriptor, monthCounter, info.days), false,
                null, null, info.days.size());
        if (clickListener != null) {
            monthView.setOnTitleClickListener(clickListener);
        }
        if (position != 0) {
            if (getItem(position).year == getItem(position - 1).year) {
                monthView.hideYear();
            } else {
                monthView.showYear();
            }
        } else {
            monthView.showYear();
        }
        return monthView;
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
                if (isCurrentMonth && days.get(value) != null) {
                    manhour = days.get(value);
                }
                QMonthCellDescriptor.RangeState rangeState = QMonthCellDescriptor.RangeState.NONE;
                weekCells.add(new QMonthCellDescriptor(date, isCurrentMonth,
                        false, false, isToday, false, value, manhour,
                        rangeState));
                cal.add(DATE, 1);
            }
        }
        return cells;
    }

    public void clear() {
        list.clear();
    }

    public void add(ArrayList<ManHour> arrayList) {

        for (int i = 0; i < arrayList.size(); i++) {
            list.add(arrayList.get(i));
        }
        notifyDataSetChanged();
    }

    public static Calendar getfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c;
    }
}
