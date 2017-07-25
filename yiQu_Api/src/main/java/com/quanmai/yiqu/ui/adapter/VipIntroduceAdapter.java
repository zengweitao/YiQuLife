package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.Member;
import com.quanmai.yiqu.common.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanjinj on 16/3/31.
 */
public class VipIntroduceAdapter extends BaseAdapter {

    Context mContext;
    List<Member> mList;

    public VipIntroduceAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
    }

    public void clear() {
        mList.clear();
    }

    public void addAll(List<Member> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
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
        if (convertView == null) {
            View viewItem = LayoutInflater.from(mContext).inflate(R.layout.item_level_introduction, null);
            convertView = viewItem;
        }

        RelativeLayout relativeLayoutLevel = (RelativeLayout) convertView.findViewById(R.id.relativeLayoutLevel);
        LinearLayout linearLayoutIntroduce = (LinearLayout) convertView.findViewById(R.id.linearLayoutIntroduce);
        ((Button) convertView.findViewById(R.id.tvLevel)).setText("Lv" + mList.get(position).vipcode);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                Utils.dp2px(mContext, 65));

        String[] strIntroduces = mList.get(position).vipprivilege.split("\\r\\n");
        if (strIntroduces.length > 1) {
            itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.dp2px(mContext, strIntroduces.length * 20 + (strIntroduces.length - 1) * 5 + 20));
        }
        relativeLayoutLevel.setLayoutParams(itemParams);

        if (strIntroduces.length > 0) {
            for (int j = 0; j < strIntroduces.length; j++) {
                View viewIntroduce = LayoutInflater.from(mContext).inflate(R.layout.view_level_introduce, null);
                ((TextView) viewIntroduce.findViewById(R.id.textViewLevelIntroduce)).setText(strIntroduces[j]);
                ((TextView) viewIntroduce.findViewById(R.id.textViewLevelIntroduce)).setTextSize(15);
                linearLayoutIntroduce.addView(viewIntroduce);
            }
        }

        return convertView;
    }
}
