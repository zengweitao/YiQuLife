package com.quanmai.yiqu.baidu;
//package com.lemuji.mall.baidu;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
//import com.baidu.mapapi.navi.BaiduMapNavigation;
//import com.baidu.mapapi.navi.NaviPara;
//import com.quanmai.pingyao.R;
//import com.quanmai.pingyao.base.BaseActivity;
//import com.quanmai.pingyao.common.utils.Utils;
//
///**
// * 演示MapView的基本用法
// */
//public class MapActivity extends BaseActivity implements OnClickListener {
//	@SuppressWarnings("unused")
//	private static final String LTAG = MapActivity.class.getSimpleName();
//	private MapView mMapView;
//	private BaiduMap mBaiduMap;
//	LatLng p;
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_map);
//		((TextView) findViewById(R.id.title)).setText(getIntent().getStringExtra("title"));
//		View btn_back = findViewById(R.id.btn_back);
//		btn_back.setVisibility(View.VISIBLE);
//		btn_back.setOnClickListener(this);
//		TextView button = (TextView) findViewById(R.id.btn_title_right);
//		button.setText("导航");
//		button.setOnClickListener(this);
//		mMapView = (MapView) findViewById(R.id.mapview);
//		mMapView.showScaleControl(false);
//		mMapView.removeViewAt(1);
//		mBaiduMap = mMapView.getMap();
//		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
////		Intent intent = getIntent();
//		String jinweidu=getIntent().getStringExtra("jinweidu");
//		String[] array=jinweidu.split(",");  
//		if(array.length==2)
//		{
//			p = new LatLng( Double.valueOf(array[1]),Double.valueOf(array[0]));
//			mBaiduMap.clear();
//			mBaiduMap.addOverlay(new MarkerOptions().position(p).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));
//			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(p));
//		} 
//
//		
//	
////		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
////		mBaiduMap.animateMapStatus(u);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		// activity 暂停时同时暂停地图控件
//		mMapView.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		// activity 恢复时同时恢复地图控件
//		mMapView.onResume();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		// activity 销毁时同时销毁地图控件
//		mMapView.onDestroy();
//	}
//
//	@Override
//	public void onClick(View v) {
//		if (Utils.isFastDoubleClick()) {
//			return;
//		}
//		switch (v.getId()) {
//		case R.id.btn_back:
//			finish();
//			break;
//		case R.id.btn_title_right:
//			startNavi();
//			break;
//		default:
//			break;
//		}
//	}
//	
//	/**
//	 * 开始导航
//	 * 
//	 * @param view
//	 */
//	public void startNavi() {
//		
////		LatLng pt1 = new LatLng(mLat1, mLon1);
////		LatLng pt2 = new LatLng(mLat2, mLon2);
//		NaviPara para = new NaviPara();
//		para.startPoint = new LatLng(mSession.getLocation_y(), mSession.getLocation_x());
//		para.startName = "从这里开始";
//		para.endPoint = p;
//		para.endName = "到这里结束";
//
//		try {
//
//			BaiduMapNavigation.openBaiduMapNavi(para, this);
//
//		} catch (BaiduMapAppNotSupportNaviException e) {
//			e.printStackTrace();
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
//			builder.setTitle("提示");
//			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					BaiduMapNavigation.getLatestBaiduMapApp(mContext);
//					
//				}
//			});
//
//			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			});
//
//			builder.create().show();
//		}
//	}
//	
//
//}
