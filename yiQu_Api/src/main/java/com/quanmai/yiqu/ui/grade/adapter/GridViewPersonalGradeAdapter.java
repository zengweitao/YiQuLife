package com.quanmai.yiqu.ui.grade.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.photopicker.utils.BaseImageLoader;
import com.quanmai.yiqu.common.photopicker.utils.OtherUtils;
import com.quanmai.yiqu.common.util.ImageCompressUtil;
import com.quanmai.yiqu.common.widget.XCRoundImageViewByXfermode;

import java.util.ArrayList;

/**
 * Created by Chasing-Li on 2017/4/20.
 */
public class GridViewPersonalGradeAdapter extends BaseAdapter {
    Context mContext;
    OnCloseClick click;
    ArrayList<String> imglist;
    private ViewHolder viewHolder;

    public GridViewPersonalGradeAdapter(Context mContext, ArrayList<String> imglist) {
        this.mContext = mContext;
        this.imglist = imglist;
    }

    @Override
    public int getCount() {
        return imglist.size();
    }

    @Override
    public String getItem(int position) {
        return imglist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String path = getItem(position);
        if (convertView==null){
            convertView=View.inflate(mContext, R.layout.item_gridview_personal_grade,null);
            viewHolder = new ViewHolder();
            viewHolder.imageViewPicture_personal_grade= (XCRoundImageViewByXfermode) convertView.findViewById(R.id.imageViewPicture_personal_grade);
            viewHolder.imageview_personal_grade_delete= (ImageView) convertView.findViewById(R.id.imageview_personal_grade_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.imageViewPicture_personal_grade.setType(XCRoundImageViewByXfermode.TYPE_ROUND);
        viewHolder.imageViewPicture_personal_grade.setRoundBorderRadius(10);
        if (path==null){
            viewHolder.imageViewPicture_personal_grade.setImageBitmap(null);
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.icon_picture_add);
            viewHolder.imageViewPicture_personal_grade.setImageBitmap(bitmap);
            viewHolder.imageview_personal_grade_delete.setImageDrawable(null);
            viewHolder.imageViewPicture_personal_grade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imglist.size()>0){
                        if (imglist.get(imglist.size()-1)==null){
                            click.getPhotos();
                        }
                    }else {
                        Utils.showCustomToast(mContext,"最多只拍摄3张");
                    }
                }
            });
        }else{
            viewHolder.imageViewPicture_personal_grade.setImageBitmap(ImageCompressUtil.compressByQuality(ImageCompressUtil.compressBySize(imglist.get(position), 400, 400), 200));
            viewHolder.imageview_personal_grade_delete.setImageResource(R.drawable.icon_close_green);
            viewHolder.imageview_personal_grade_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imglist.remove(position);
                    if(!imglist.contains(null)){
                        imglist.add(null);
                    }
                    if (imglist.size()<1){
                        imglist.clear();
                        click.changView();
                    }
                    notifyDataSetChanged();
                }
            });
        }
        for (int i=0,count = 0;i<imglist.size();i++){
            if (imglist.get(i)==null){
                count++;
            }
            if (count==imglist.size()){
                click.changView();
                imglist.clear();
            }
        }

        /*viewHolder.imageview_personal_grade_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imglist.remove(position);
                notifyDataSetChanged();
                if (imglist.size()!=0){
                    gridView_personal_grade.setVisibility(View.VISIBLE);
                    gridView_personal_grade.setNumColumns(imglist.size());
                    if (imglist.size()>1){
                        gridView_personal_grade.setLayoutParams(new LinearLayout.LayoutParams((OtherUtils.dip2px(mContext,88)+10)*imglist.size(),OtherUtils.dip2px(mContext,88)));
                    }else {
                        gridView_personal_grade.setLayoutParams(new LinearLayout.LayoutParams((OtherUtils.dip2px(mContext,88))*imglist.size(),OtherUtils.dip2px(mContext,88)));
                    }
                    if (imglist.size()==3||imglist.size()>3){
                        relativeLayoutAddPicture_personal_grade.setVisibility(View.GONE);
                    }else{
                        relativeLayoutAddPicture_personal_grade.setVisibility(View.VISIBLE);
                    }
                }else{
                    gridView_personal_grade.setVisibility(View.GONE);
                }
            }
        });*/
        return convertView;
    }
    public void addCloseListener(OnCloseClick closeClick){
        click = closeClick;
    }

    public interface OnCloseClick{
        void changView();
        void onClose(String path);
        void getPhotos();
    }
}
class ViewHolder{
    XCRoundImageViewByXfermode imageViewPicture_personal_grade;
    ImageView imageview_personal_grade_delete;
}
