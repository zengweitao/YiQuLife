package com.quanmai.yiqu.base;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.api.vo.AdvertInfo;
import com.quanmai.yiqu.common.widget.FocusMap;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public abstract class BaseAppPagerAcitivity extends BaseActivity {

	public int mScreenWidth;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		super.onCreate(savedInstanceState);
	}
	public void initAppPager(int mScreenWidth, LinearLayout addlt,
			JSONArray array) throws JSONException {
		addlt.removeAllViews();
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			int type = jsonObject.getInt("type");
			switch (type) {
			case 1:
//				 GetView1(jsonObject, addlt, mScreenWidth);
				break;
			case 2:
				// utils.GetView2or3(jsonObject, addlt, 2);
				break;
			case 3:
				// utils.GetView2or3(jsonObject, addlt, 3);
				break;
			case 4:
				// utils.GetView4(jsonObject, addlt);
				break;
			case 5:
				// utils.GetView5or6(jsonObject, addlt, mScreenWidth, 5);
				break;
			case 6:
				// utils.GetView5or6(jsonObject, addlt, mScreenWidth, 6);
				break;
			// case 7:
			// utils.GetView7(jsonObject, addlt);
			// break;
			case 21:
				// utils.GetView21(jsonObject, addlt, mScreenWidth);
				break;
			case 22:
				GetView22(jsonObject, addlt, mScreenWidth);
				break;
			case 24:
				GetView24(jsonObject, addlt, mScreenWidth);
				break;
			case 25:
				// utils.GetView25(jsonObject, addlt, mScreenWidth,new
				// OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// if(CommonUtil.isFastDoubleClick())
				// {
				// return;
				// }
				// Intent intent = new Intent(mContext, Directory.class);
				// startActivityForResult(intent, 1);
				// }
				// });
				break;

			default:
				break;
			}
		}
	}

	public void GetView22(JSONObject jsonObject, LinearLayout lt,
			int mScreenWidth) throws JSONException {
		RelativeLayout arg1 = new RelativeLayout(mContext);
		// int padding = 0;
		// // DisplayUtil.dip2px(mContext, 3);
		// arg1.setPadding(0, 0, 0, padding);
//		lt.setBackgroundColor(Color.parseColor(jsonObject.getString("background_color")));
		arg1.setLayoutParams(new LayoutParams((int) (jsonObject.getInt("width")
				* mScreenWidth / 640), (int) (jsonObject.getInt("height")
				* mScreenWidth / 640)));
		final JSONArray content = jsonObject.getJSONArray("content");
		JSONObject childJsonObject = null;
		for (int i = 0; i < content.length(); i++) {
			childJsonObject = content.getJSONObject(i);
			ImageView imageView = new ImageView(mContext);
//			// imageView.setImageResource(R.drawable.img_default);
//			imageView.setBackgroundColor(0xffff0000);

			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			int image_x = (int) (childJsonObject.getInt("x") * mScreenWidth / 640);
			int image_y = (int) (childJsonObject.getInt("y") * mScreenWidth / 640);
			RelativeLayout.LayoutParams wmParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			wmParams.setMargins(image_x, image_y, 0, 0);
			wmParams.width = (int) (childJsonObject.getInt("width")
					* mScreenWidth / 640);
			wmParams.height = (int) (childJsonObject.getInt("height")
					* mScreenWidth / 640);
			OnClick(mContext, imageView,
					childJsonObject.getString("link_value"),
					childJsonObject.getString("title"),
					childJsonObject.getInt("link_type"));

			arg1.addView(imageView, wmParams);
			ImageloaderUtil.displayImage(mContext,
					childJsonObject.getString("picurl"), imageView);
		}
		lt.addView(arg1);
	}


	public void GetView24(JSONObject jsonObject, LinearLayout lt,
			int mScreenWidth) throws JSONException {
		final JSONArray content = jsonObject.getJSONArray("content");
		final FocusMap focusMap=new FocusMap(mContext, (int) (jsonObject.getInt("height")
				* mScreenWidth / 640));
		ArrayList<AdvertInfo> advInfos = new ArrayList<AdvertInfo>();
		AdvertInfo info;
		for (int i = 0; i < content.length(); i++) {
			info=new AdvertInfo();

			jsonObject=content.getJSONObject(i);
			info.picurl=jsonObject.getString("picurl");
			info.link_type=jsonObject.getInt("link_type");
			info.link_value=jsonObject.getString("link_value");
			advInfos.add(info);
		}

		focusMap.setAdapter(advInfos);
		/* 设置自动滚动 */
		focusMap.setAutomaticsliding(true);
		focusMap.setOnItemClickListener(new FocusMap.OnItemClickListener() {
			@Override
			public void onItemClick(int position, Object object) {
				OnClick( focusMap.getItem(position).link_value, "", focusMap.getItem(position).link_type);
			}
		});
		lt.addView(focusMap);
	}

//	public void GetView1(JSONObject jsonObject, LinearLayout lt,
//			int mScreenWidth) throws JSONException {
//		final JSONArray content = jsonObject.getJSONArray("content");
//		View arg1 = LayoutInflater.from(mContext).inflate(
//				R.layout.inflater_home_view1, null);
//		View lt1 = arg1.findViewById(R.id.lt1);
//		View lt2 = arg1.findViewById(R.id.lt2);
//		View lt3 = arg1.findViewById(R.id.lt3);
//		ViewGroup.LayoutParams params = new LayoutParams((mScreenWidth) / 3,
//				(mScreenWidth) * 2 / 5);
//		lt1.setLayoutParams(params);
//		lt2.setLayoutParams(params);
//		lt3.setLayoutParams(params);
//		TextView textView;
//		if (content.length() >= 1) {
//			arg1.findViewById(R.id.lt1).setVisibility(View.VISIBLE);
//			ImageloaderUtil.displayImage(mContext,content.getJSONObject(0).getString("picurl"), (ImageView) arg1.findViewById(R.id.img1));
//			textView = (TextView) arg1.findViewById(R.id.text1);
//			String title = content.getJSONObject(0).getString("title");
//			if (!title.equals("")) {
//				textView.setVisibility(View.VISIBLE);
//				textView.setText(title);
//			}
//			((CountDownView) arg1.findViewById(R.id.countdown1))
//					.setTime(content.getJSONObject(0).getLong("countdown"));
//			OnClick(mContext, arg1.findViewById(R.id.lt1), content
//					.getJSONObject(0).getString("link_value"), content
//					.getJSONObject(0).getString("title"), content
//					.getJSONObject(0).getInt("link_type"));
//		}
//		if (content.length() >= 2) {
//			arg1.findViewById(R.id.lt2).setVisibility(View.VISIBLE);
//			ImageloaderUtil.displayImage(mContext,content.getJSONObject(1).getString("picurl"), (ImageView) arg1.findViewById(R.id.img2));
//			textView = (TextView) arg1.findViewById(R.id.text2);
//			String title = content.getJSONObject(1).getString("title");
//			if (!title.equals("")) {
//				textView.setVisibility(View.VISIBLE);
//				textView.setText(title);
//			}
//			((CountDownView) arg1.findViewById(R.id.countdown2))
//					.setTime(content.getJSONObject(1).getLong("countdown"));
//
//			OnClick(mContext, arg1.findViewById(R.id.lt2), content
//					.getJSONObject(1).getString("link_value"), content
//					.getJSONObject(1).getString("title"), content
//					.getJSONObject(1).getInt("link_type"));
//		}
//		if (content.length() >= 3) {
//			arg1.findViewById(R.id.lt3).setVisibility(View.VISIBLE);
//			ImageloaderUtil.displayImage(mContext,content.getJSONObject(2).getString("picurl"), (ImageView) arg1.findViewById(R.id.img3));
//			textView = (TextView) arg1.findViewById(R.id.text3);
//			String title = content.getJSONObject(2).getString("title");
//			if (!title.equals("")) {
//				textView.setVisibility(View.VISIBLE);
//				textView.setText(title);
//			}
//			((CountDownView) arg1.findViewById(R.id.countdown3))
//					.setTime(content.getJSONObject(2).getLong("countdown"));
//			OnClick(mContext, arg1.findViewById(R.id.lt3), content
//					.getJSONObject(2).getString("link_value"), content
//					.getJSONObject(2).getString("title"), content
//					.getJSONObject(2).getInt("link_type"));
//		}
//		lt.addView(arg1);
//	}

	private void OnClick(final String value,
			final String title, final int type) {
		Intent intent;
		switch (type) {
//		case 1:
//			intent = new Intent(mContext, WebActivity.class);
//			intent.putExtra("http_url", value);
//			mContext.startActivity(intent);
//			break;
//		case 2: // 分类列表
//			intent = new Intent(mContext, ChildClassActivity.class);
//			intent.putExtra("cid", value);
//			intent.putExtra("name", title);
//			mContext.startActivity(intent);
//			break;
//		case 3:
//			intent = new Intent(mContext, ProductDetail2.class);
//			intent.putExtra("aid", value);
//			mContext.startActivity(intent);
//			break;
		case 4:// 搜索列表
				// intent = new Intent(mContext, ResListActivity.class);
				// intent.putExtra("cid",
				// Integer.valueOf(value).intValue());
				// intent.putExtra("cat_name", "");
				// mContext.startActivity(intent);
			break;
		case 5:// 店铺列表
				// intent = new Intent(mContext, ShopActvitiy.class);
				// intent.putExtra("shop_id", Integer.valueOf(value)
				// .intValue());
				// mContext.startActivity(intent);
			break;
//		case 6://
//			intent = new Intent(mContext, AppPageActivity.class);
//			intent.putExtra("pageid", value);
//			startActivityForResult(intent, 200);
//			break;
		case 8://
				// intent = new Intent(mContext, GridActivity.class);
				// intent.putExtra("cid",
				// Integer.valueOf(value).intValue());
				// intent.putExtra("cat_name", "");
				// mContext.startActivity(intent);
			break;
//		case 11://
//			intent = new Intent(mContext, TuangouListActivity.class);
//			intent.putExtra("type", 0);
//			mContext.startActivity(intent);
//		break;
//		case 12://
//			intent = new Intent(mContext, TuangouListActivity.class);
//			intent.putExtra("type", 1);
//			mContext.startActivity(intent);
//		break;
//		case 30:
//			ProductListPresenter.AppPagerAdd(mContext, value,
//					new AppPagerChangeReuest() {
//
//						@Override
//						public void onSuccess(String info) {
//							Utils.showCustomToast(mContext, info);
//							setResult(1);
//							finish();
//						}
//
//						@Override
//						public void onFailure(String info) {
//							Utils.showCustomToast(mContext, info);
//						}
//					});
//			break;
//		case 31:
//			ProductListPresenter.AppPagerDelete(mContext, value,
//					new AppPagerChangeReuest() {
//
//						@Override
//						public void onSuccess(String info) {
//							Utils.showCustomToast(mContext, info);
//							setResult(1);
//							finish();
//						}
//
//						@Override
//						public void onFailure(String info) {
//							Utils.showCustomToast(mContext, info);
//						}
//					});
//			break;
		default:

			break;
		}

	}
	public void OnClick(final Context mContext, View view, final String value,
			final String title, final int type) {
		if (value.equals("")) {
			return;
		}
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OnClick(value, title, type);

			}
		});

	}

	protected abstract void getAppPager();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 200 && resultCode == 1) {
			showLoadingDialog("请稍候");
			getAppPager();
		}

	}
}
