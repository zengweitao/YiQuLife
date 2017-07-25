package com.quanmai.yiqu.ui.classifigarbage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ClassifyGameRankingInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.XCRoundImageView;

/**
 * Created by Chasing-Li on 2017/5/15. 游戏竞赛页面排名adapter
 */

public class ClassifyGameCompetitionRankingAdapter extends BaseAdapter {

    Context mContext;
    CommonList<ClassifyGameRankingInfo.ContestRankListInfo> mData;

    public ClassifyGameCompetitionRankingAdapter(Context mContext) {
        this.mContext = mContext;
        mData=new CommonList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_classify_game_competition_ranking, null);
            holder.textview_ranking_game= (TextView) convertView.findViewById(R.id.textview_ranking_game);
            holder.textview_username= (TextView) convertView.findViewById(R.id.textview_username);
            holder.textview_score_game= (TextView) convertView.findViewById(R.id.textview_score_game);
            holder.imageView_user_game= (XCRoundImageView) convertView.findViewById(R.id.imageView_user_game);
            holder.relative_classify_game_ranking_item= (RelativeLayout) convertView.findViewById(R.id.relative_classify_game_ranking_item);
            convertView.setTag(holder);
        }else {
            //convertView.setBackgroundColor(Color.WHITE);
            holder = (ViewHolder) convertView.getTag();
        }
        holder.relative_classify_game_ranking_item.setBackgroundColor(Color.WHITE);
       // holder.imageView_user_game.setImageResource(R.drawable.default_header);
        if (mData!=null){
            holder.textview_ranking_game.setText(mData.get(position).getRank());
            holder.imageView_user_game.setBorderWidth(Utils.dp2px(mContext, 0));
            holder.imageView_user_game.setBorderColor(Color.WHITE);
            if (holder.imageView_user_game.getTag() == null || !holder.imageView_user_game.getTag().equals(mData.get(position).getImgUrl())) {
                holder.imageView_user_game.setTag(mData.get(position).getImgUrl());
                if (mData.get(position).getImgUrl().equals("")||mData.get(position).getImgUrl()==null||mData.get(position).getImgUrl().equals("null")){
                    holder.imageView_user_game.setImageResource(R.drawable.default_header);
                }else {
                    ImageloaderUtil.displayImage(mContext, mData.get(position).getImgUrl(), holder.imageView_user_game,
                            ImageloaderUtil.getDisplayImageOptions(R.drawable.default_header,R.drawable.default_header,R.drawable.default_header));
                }
            }else if(holder.imageView_user_game.getTag().equals(mData.get(position).getImgUrl())){
                if (mData.get(position).getImgUrl().equals("")||mData.get(position).getImgUrl()==null||mData.get(position).getImgUrl().equals("null")){
                    holder.imageView_user_game.setImageResource(R.drawable.default_header);
                }else {
                    ImageloaderUtil.displayImage(mContext, mData.get(position).getImgUrl(), holder.imageView_user_game,
                            ImageloaderUtil.getDisplayImageOptions(R.drawable.default_header,R.drawable.default_header,R.drawable.default_header));
                }
            }
            holder.textview_username.setText(mData.get(position).getAccount());
            holder.textview_score_game.setText(mData.get(position).getMaxScore());
            int color = mContext.getResources().getColor(R.color.theme);
            if (mData.get(position).getIsCurrent().equals("1")){
                holder.relative_classify_game_ranking_item.setBackgroundColor(color);
            }
        }

        return convertView;
    }
    class ViewHolder{
        TextView textview_ranking_game; //排名
        XCRoundImageView imageView_user_game; //头像
        TextView textview_username;
        TextView textview_score_game;
        RelativeLayout relative_classify_game_ranking_item;
    }

    public void clear(){
        notifyDataSetChanged();
        mData.clear();
    }

    public void add(CommonList<ClassifyGameRankingInfo.ContestRankListInfo> data){
        mData.addAll(data);
        notifyDataSetChanged();

    }
}
