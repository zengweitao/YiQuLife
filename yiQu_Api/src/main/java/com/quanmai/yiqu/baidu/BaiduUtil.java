package com.quanmai.yiqu.baidu;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.quanmai.yiqu.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
/**
 * 百度定位工具类
 * @author lix
 *start开始定位
 *stop停止定位
 */
public class BaiduUtil {
	private LocationClient mLocationClient = null;
	public BaiduUtil(Context context) {
		mLocationClient = new LocationClient(context);
	}
	private static BaiduUtil mInstance;
	
	public static BaiduUtil get(Context context) {
		if (mInstance == null) {
			mInstance = new BaiduUtil(context);
		}
		return mInstance;
	}
	
	public void start(onLocationCompleteListener Listener) {
		isFirstLoc=true;
		if (Listener != null) {
			Listener.onStart();
		}
		setLocOption();
		mLocationClient.registerLocationListener(new MyLocationListenner(
				Listener));
		mLocationClient.start();
	}
	private void setLocOption() {
		// 设置定位参数包括：定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。eg：
		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);
//		option.setAddrType("all");// 返回的定位结果包含地址信息
//		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
//		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
//		mLocationClient.setLocOption(option);
		option.setIsNeedAddress(true);
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		mLocationClient.setLocOption(option);
	}

	boolean isFirstLoc=true;
	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	private class MyLocationListenner implements BDLocationListener {
		onLocationCompleteListener locationCompleteListener;

		public MyLocationListenner(onLocationCompleteListener Listener) {
			locationCompleteListener = Listener;
		}
		@Override
		public void onReceiveLocation(BDLocation location) {
//			System.out.println("onReceiveLocation--------------------------------------------");
//			Log.e("q", location.getLongitude() + "||" + location.getLatitude());
//			Log.e("q", "getCity="+location.getCity());
//			Log.e("q", "getProvince="+location.getProvince());
			if (isFirstLoc) {
				isFirstLoc = false;
			if (locationCompleteListener != null) {
				if (location == null) {
					locationCompleteListener.onError("坐标获取失败");
				}else {
				locationCompleteListener.onLocationSuccess(location
						.getLongitude(),location.getLatitude(),location.getCity());
				}
			}else {
				locationCompleteListener.onError("坐标获取失败");
			}
			mLocationClient.stop();
			}
		}
	
	}
	
	/**
	 * 开始百度导航
	 * 
	 * @param view
	 */
//	public static void startBaiduNavi(Context mContext,LatLng latLng) {
//		NaviPara para = new NaviPara();
//		para.startPoint = new LatLng(Session.get(mContext).getLocation_y(),
//				Session.get(mContext).getLocation_x());
//		para.startName = "从这里开始";
////		para.endPoint = new LatLng(info.getJindu(), info.getWeidu());
//		para.endPoint = latLng;
//		para.endName = "到这里结束";
//
//		try {
//
//			BaiduMapNavigation.openBaiduMapNavi(para, mContext);
//
//		} catch (BaiduMapAppNotSupportNaviException e) {
//			e.printStackTrace();
//			showImgDialog(mContext);
//		}
//	}


	private static void showImgDialog(final Context mContext) {
		final Dialog getImgDialog = new Dialog(mContext);
			getImgDialog.getWindow()
					.setBackgroundDrawable(new ColorDrawable(0));
			getImgDialog.setCanceledOnTouchOutside(true);
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_common, null);
			TextView message = (TextView) view.findViewById(R.id.textViewTitle);
			message.setText("您尚未安装百度地图app或app版本过低，点击确认安装？");
			getImgDialog.setContentView(view);
			getImgDialog.findViewById(R.id.buttonConfirm).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							getImgDialog.cancel();
//							BaiduMapNavigation.getLatestBaiduMapApp(mContext);
						}
					});
			getImgDialog.findViewById(R.id.buttonShare).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							getImgDialog.cancel();
						}

					});
			Window dialogWindow = getImgDialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = LayoutParams.MATCH_PARENT;
			dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
			dialogWindow.setAttributes(lp);
		
		getImgDialog.show();
	}

	public interface onLocationCompleteListener {
		void onStart();
		
		void onError(String info);

		void onLocationSuccess(double x,double y,String city);
	}
}
