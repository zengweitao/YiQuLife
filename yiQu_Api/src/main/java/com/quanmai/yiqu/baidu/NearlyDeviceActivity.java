package com.quanmai.yiqu.baidu;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.base.BaseFragmentActivity;
import com.quanmai.yiqu.base.CommonList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class NearlyDeviceActivity extends BaseFragmentActivity implements
		OnMarkerClickListener, OnMapClickListener {
	private BaiduMap mBaiduMap = null;
	private TextView tvNoData;
	private CommonList<DeviceBean> mDatas;
	private TextView tvMyLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearly_device);
		init();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void init() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.tv_title)).setText("周边设备");
		mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager()
				.findFragmentById(R.id.map))).getBaiduMap();
		mBaiduMap.setOnMarkerClickListener(this);
		mBaiduMap.setOnMapClickListener(this);
		tvNoData = (TextView) findViewById(R.id.no_datas);
		tvMyLocation = (TextView) findViewById(R.id.my_location);
		getDataFromNet();
	}

	private void getDataFromNet() {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍候");
		Api.get().NearbyDevices(mContext,
				new ApiRequestListener<CommonList<DeviceBean>>() {

					@Override
					public void onSuccess(String msg,
							CommonList<DeviceBean> data) {
						// TODO Auto-generated method stub
						mDatas = data;
						if (data == null || data.size() == 0) {
							tvNoData.setVisibility(View.VISIBLE);
							tvMyLocation.setVisibility(View.GONE);
						} else {
							tvNoData.setVisibility(View.GONE);
							tvMyLocation.setVisibility(View.VISIBLE);
							tvMyLocation.setText("我的位置："
									+ (mSession.getLocation_x() + "")
											.substring(0, 6)
									+ ", "
									+ (mSession.getLocation_y() + "")
											.substring(0, 6));
							showDevicesOnMap();
						}
						dismissLoadingDialog();
					}

					@Override
					public void onFailure(String msg) {
						showCustomToast(msg);
						tvNoData.setVisibility(View.VISIBLE);
						dismissLoadingDialog();
					}
				});
	}

	/**
	 * 在地图上显示坐标
	 */
	protected void showDevicesOnMap() {
		mBaiduMap.clear();
		for (int i = 0; i < mDatas.size(); i++) {
			LatLng latLng = new LatLng(mDatas.get(i).getLng(), mDatas.get(i)
					.getLat());
			BitmapDescriptor pic = BitmapDescriptorFactory
					.fromResource(R.drawable.location);
			MarkerOptions overlayOptions = new MarkerOptions().position(latLng)
					.icon(pic).zIndex(5);
			Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
			Bundle bundle = new Bundle();
			bundle.putString(
					"location",
					mDatas.get(i).getLocation().split(",")[0].substring(0, 5)
							+ ", "
							+ mDatas.get(i).getLocation().split(",")[1]
									.substring(0, 5));
			bundle.putString("name", mDatas.get(i).getName());
			bundle.putString("address", mDatas.get(i).getAddress());
			marker.setExtraInfo(bundle);
		}
		// 加入自己的位置
		LatLng latLng = new LatLng(mSession.getLocation_y(),
				mSession.getLocation_x());
		BitmapDescriptor myPic = BitmapDescriptorFactory
				.fromResource(R.drawable.my_location);
		MarkerOptions options = new MarkerOptions().position(latLng)
				.icon(myPic).zIndex(5);
		mBaiduMap.addOverlay(options);

		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(17).build()));
		if (mDatas.size() > 0) {
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.animateMapStatus(u);
		}
	}

	@Override
	public void onMapClick(LatLng arg0) {
		mBaiduMap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		try {
			showLocation(arg0);
		} catch (Exception e) {
			showMyLocation(arg0);
		}
		return false;
	}

	/**
	 * 显示我的位置气泡
	 * 
	 * @param marker
	 */
	private void showMyLocation(Marker marker) {
		// TODO Auto-generated method stub
		View popView = LayoutInflater.from(mContext).inflate(
				R.layout.pop_text_view, null);
		InfoWindow mInfoWindow = new InfoWindow(popView, marker.getPosition(),
				-70);
		mBaiduMap.showInfoWindow(mInfoWindow);
	}

	/**
	 * 显示气泡
	 * 
	 * @param marker
	 */
	private void showLocation(Marker marker) {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.device_location_window, null);
		((TextView) popView.findViewById(R.id.device_name)).setText(marker
				.getExtraInfo().getString("name"));
		((TextView) popView.findViewById(R.id.device_address)).setText("地址："
				+ marker.getExtraInfo().getString("address"));
		((TextView) popView.findViewById(R.id.device_location)).setText("设备坐标："
				+ marker.getExtraInfo().getString("location"));
		InfoWindow mInfoWindow = new InfoWindow(popView, marker.getPosition(),
				-10);
		mBaiduMap.showInfoWindow(mInfoWindow);
	}
}
