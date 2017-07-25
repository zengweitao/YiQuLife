package com.quanmai.yiqu.ui.grade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.PointPenaltyInfo;
import com.quanmai.yiqu.common.widget.CheckableRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 清运评分适配器
 * Created by James on 2016/11/10.
 */

public class GradeCleanAdapter extends BaseAdapter {
    private Context mContext;
    private List<PointPenaltyInfo> mList;
//    private HashMap<Integer, Boolean> mCheckedMap;  //存放item选中的结果

    public GradeCleanAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
//        this.mCheckedMap = new HashMap<>();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grade_clean, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvContent.setText(mList.get(position).getCheckname());
        holder.rlItem.setTag(R.id.tag_position, position);
        holder.rlItem.setTag(R.id.tag_tv_content, holder.tvContent);
        holder.rlItem.setTag(R.id.tag_img_checked, holder.imgChecked);

        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag(R.id.tag_position);
                TextView textView = (TextView) v.getTag(R.id.tag_tv_content);
                ImageView imageView = (ImageView) v.getTag(R.id.tag_img_checked);
                if (!mList.get(position).getCheck()) { //未选中状态
                    v.setBackgroundResource(R.drawable.bg_theme_color_radius_2dp);
                    textView.setTextColor(mContext.getResources().getColor(R.color.white));
                    imageView.setVisibility(View.VISIBLE);
                    mList.get(position).setCheck(true);
                } else {                           //选中状态
                    v.setBackgroundResource(R.drawable.bg_gray_stroke_1px_radius_2dp);
                    textView.setTextColor(mContext.getResources().getColor(R.color.text_color_979797));
                    imageView.setVisibility(View.VISIBLE);
                    mList.get(position).setCheck(false);
                }
            }
        });
        return convertView;
    }


    public void add(List<PointPenaltyInfo> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mList.addAll(list);
//        for (int i = 0; i < mList.size(); i++) { //初始化，未选中
//            mCheckedMap.put(i, false);
//        }
        notifyDataSetChanged();
    }

    public void clean() {
        mList.clear();
        notifyDataSetChanged();
    }

    //获取选择的Map
//    public HashMap<Integer, Boolean> getIsSelected() {
//        return mCheckedMap;
//    }

    //返回扣分项id和扣分值集合
    public List<String[]> getScoreList() {
        List<String[]> scoreList = new ArrayList<>();
        for (PointPenaltyInfo info : mList) {
            String[] checkMap = new String[2];
            if (info.getCheck()) {      //选中，扣分
                checkMap[0] = info.getId();
                checkMap[1] = info.getScore();
                scoreList.add(checkMap);
            }
        }
        return scoreList;
    }

    class ViewHolder {
        public View rootView;
        public TextView tvContent;
        public ImageView imgChecked;
        public CheckableRelativeLayout rlItem;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvContent = (TextView) rootView.findViewById(R.id.tvContent);
            this.imgChecked = (ImageView) rootView.findViewById(R.id.imgChecked);
            this.rlItem = (CheckableRelativeLayout) rootView.findViewById(R.id.rlItem);
        }
    }
}
