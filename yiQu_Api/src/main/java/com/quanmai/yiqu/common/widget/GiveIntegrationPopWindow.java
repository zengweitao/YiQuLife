package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ActivityIntegrationInfo;
import com.quanmai.yiqu.base.CommonList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95138 on 2016/4/28.
 */
public class GiveIntegrationPopWindow extends PopupWindow {

    private View mMenuView;
    ListView listView;
    CommonList<ActivityIntegrationInfo> dataList;
    Context mContext;
    MyAdapter mAdapter;
    TypeSelectedListener mListener;

    public GiveIntegrationPopWindow(Context context ,List list,TypeSelectedListener listener){
        mContext = context;
        mListener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.item_give_integration, null);
        dataList = new CommonList<>();
        if (list!=null&&list.size()>0){
            dataList.addAll(list);
        }

        initView();
        initPopWindow();
    }

    private void initPopWindow() {
        //设置PopupWindow的View
        this.setContentView(mMenuView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.PopupAnim);
        //实例化一个ColorDrawable颜色为半透明
        setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
//        设置PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initView() {
        listView = (ListView)mMenuView.findViewById(R.id.listView);
        mAdapter = new MyAdapter();

        listView.setAdapter(mAdapter);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView==null){
                holder=new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pop_window_integration,null);

                holder.textView = (TextView)convertView.findViewById(R.id.textView);
                holder.linearContent = (LinearLayout)convertView.findViewById(R.id.linearContent);
                holder.textView.setTextColor(Color.parseColor("#575757"));
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.textView.setText(dataList.get(position).activityName);
            final TextView mTextView = holder.textView;
            holder.linearContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTextView.setTextColor(mContext.getResources().getColor(R.color.theme));
                    mListener.selected(dataList.get(position));
                    dismiss();
                }
            });
            return convertView;
        }

        class ViewHolder{
            TextView textView;
            LinearLayout linearContent;
        }
    }

    public interface TypeSelectedListener{
        void selected(ActivityIntegrationInfo info);
    }
}
