package com.quanmai.yiqu.ui.selectpic;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.Utils;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class GridAdapter extends CommonAdapter<String>{

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public List<String> mSelectedImage;
	SelectPicActivity c;
	ImageLoader iImageLoader = ImageLoader.getInstance(4, ImageLoader.Type.LIFO);
	/**
	 * 文件夹路径
	 */
	private String mDirPath;
	
	public GridAdapter(Context context, SelectPicActivity c, List<String> mDatas, int itemLayoutId,
			String dirPath, List<String> mSelectedImage){
		
		super(context, mDatas, itemLayoutId);
		this.c = c;
		this.mDirPath = dirPath;
		this.mSelectedImage = mSelectedImage;
	}

	@Override
	public void convert(final ViewHolder helper, final String item){
		
		//设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		//设置no_selected
		helper.setImageResource(R.id.id_item_select,
				R.drawable.picture_unselected);

		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);

		File imageFile = new File(mDirPath + "/" + item);
		// 显示图片
			Glide.with(c)
					.load(imageFile)
					.placeholder(R.drawable.icon_nopic)
					.override(150,150)
					.centerCrop()
					.into(mImageView);
//		Picasso.with(c)
//		.load(imageFile)
//		.placeholder(R.drawable.icon_nopic)
//
//		//.error(R.drawable.default_error)
//		.resize(100, 100)
//		.centerInside()
//		.into(mImageView);

//		iImageLoader.loadImage(mDirPath + "/" + item, mImageView);
		mImageView.setColorFilter(null);
		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener(){
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v){
					if (mSelectedImage.contains(mDirPath + "/" + item)){//取消选择
						mSelect.setImageResource(R.drawable.picture_unselected);
//						mImageView.setColorFilter(null);
						c.removeView(mDirPath + "/" + item);
					} else{//选择
						if(c.selectedList.size()<c.max){
							mSelect.setImageResource(R.drawable.pictures_selected);
//							mImageView.setColorFilter(Color.parseColor("#77000000"));
							Log.e("mark", "path:    "+mDirPath + "/" + item);
							c.addView(mDirPath + "/" + item);
						}else{
							Utils.showCustomToast(c, "只能设置"+c.max+"张图片");
						}
					}
			}
		});

		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item)){
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}else {
			mImageView.setColorFilter(null);
		}
	}
	

}
