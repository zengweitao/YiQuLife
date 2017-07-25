package com.quanmai.yiqu.ui.booking.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageCompressUtil;
import com.quanmai.yiqu.common.widget.XCRoundImageViewByXfermode;
import com.quanmai.yiqu.ui.booking.BookingSecond2Activity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/6/20.
 */

public class BookingPhotoRecyclerAdapter extends RecyclerView.Adapter<BookingPhotoRecyclerAdapter.MyViewHoder> {
    Context mContext;
    OnCloseClick click;
    ArrayList<String> imglist;
    SpaceItemDecoration mSpaceItemDecoration;

    public BookingPhotoRecyclerAdapter(Context mContext, ArrayList<String> imglist) {
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
        holder.imageViewPicture_personal_grade.setRoundBorderRadius(5);
        if (imglist.get(position)==null){
            holder.imageViewPicture_personal_grade.setImageBitmap(null);
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.pic_photo);
            holder.imageViewPicture_personal_grade.setImageBitmap(bitmap);
            holder.imageview_personal_grade_delete.setImageDrawable(null);
            holder.imageViewPicture_personal_grade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imglist.size()>0){
                        if (imglist.contains(null)){
                            click.getPhotos();
                        }
                    }else if(imglist.size()>3){
                        Utils.showCustomToast(mContext,"最多只拍摄3张");
                    }
                }
            });
        }else{
            holder.imageViewPicture_personal_grade.setImageBitmap(ImageCompressUtil.compressByQuality(ImageCompressUtil.compressBySize(imglist.get(position), 400, 400), 200));
            holder.imageview_personal_grade_delete.setImageResource(R.drawable.icon_close_green);
            holder.imageview_personal_grade_delete.setOnClickListener(new View.OnClickListener() {
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
        ImageView imageview_personal_grade_delete;
        public MyViewHoder(View itemView) {
            super(itemView);
            imageViewPicture_personal_grade= (XCRoundImageViewByXfermode) itemView.findViewById(R.id.imageViewPicture_personal_grade);
            imageview_personal_grade_delete= (ImageView) itemView.findViewById(R.id.imageview_personal_grade_delete);
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
