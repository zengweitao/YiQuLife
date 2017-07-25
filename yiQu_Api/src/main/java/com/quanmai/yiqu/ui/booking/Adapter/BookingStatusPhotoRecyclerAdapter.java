package com.quanmai.yiqu.ui.booking.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageCompressUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.XCRoundImageViewByXfermode;

import java.util.ArrayList;

/**
 * Created by admin on 2017/6/20.
 */

public class BookingStatusPhotoRecyclerAdapter extends RecyclerView.Adapter<BookingStatusPhotoRecyclerAdapter.MyViewHoder> {
    Context mContext;
    OnCloseClick click;
    ArrayList<String> imglist;
    SpaceItemDecoration mSpaceItemDecoration;

    public BookingStatusPhotoRecyclerAdapter(Context mContext, ArrayList<String> imglist) {
        this.mContext = mContext;
        this.imglist = imglist;
    }

    @Override
    public MyViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHoder holder = new MyViewHoder(LayoutInflater.from(
                mContext).inflate(R.layout.item_gridview_personal_grade, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHoder holder, final int position) {
        holder.imageViewPicture_personal_grade.setType(XCRoundImageViewByXfermode.TYPE_ROUND);
        holder.imageViewPicture_personal_grade.setRoundBorderRadius(0);
        if (imglist.size()>0){
            Log.e("--图片名字","== "+imglist.get(position));
            //https://imgs.eacheart.com:9444/a33034-0-20170710144450.jpg
            ImageloaderUtil.displayImage(mContext,imglist.get(position),holder.imageViewPicture_personal_grade);
        }
    }

    @Override
    public int getItemCount() {
        return imglist.size();
    }

    public void addCloseListener(OnCloseClick closeClick){
        click = closeClick;
    }

    public interface OnCloseClick{
        void changView();
        void onClose(String path);
        void getPhotos();
    }

    class MyViewHoder extends RecyclerView.ViewHolder{
        XCRoundImageViewByXfermode imageViewPicture_personal_grade;
        public MyViewHoder(View itemView) {
            super(itemView);
            imageViewPicture_personal_grade= (XCRoundImageViewByXfermode) itemView.findViewById(R.id.imageViewPicture_personal_grade);
        }
    }

    public SpaceItemDecoration getSpaceItemDecoration(int space){
            return mSpaceItemDecoration=new SpaceItemDecoration(space);
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int space;
        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            //outRect.left = space;
           // outRect.right = space;
            //outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if(parent.getChildPosition(view) <3)
                outRect.right = space;
        }
    }
}
