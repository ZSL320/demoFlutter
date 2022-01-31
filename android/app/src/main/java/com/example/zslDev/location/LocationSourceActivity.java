package com.example.zslDev.location;

import android.graphics.Color;
import android.location.Location;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.example.zslDev.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationSourceActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {
	private MapView mMapView;//显示地图的视图
	private AMap aMap;//定义AMap 地图对象的操作方法与接口。

	private OnLocationChangedListener mListener;//位置发生变化时的监听

	private AMapLocationClient mapLocationClient;//定位服务类。此类提供单次定位、持续定位、地理围栏、最后位置相关功能。
	private AMapLocationClientOption mapLocationClientOption;//定位参数设置，通过这个类可以对定位的相关参数进行设置
	//在AMapLocationClient进行定位时需要这些参数

	private AMapLocation privLocation;
	//private SportMyView sportMyView;
	private double distance;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			MapsInitializer.updatePrivacyAgree(this,true);
			MapsInitializer.updatePrivacyShow(this,true,true);
		}catch (Exception e){}
		setContentView(R.layout.locationsource_activity);
		mMapView = findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);//必须调用
		init();

	}
	//实例化Amap对象
	public void init() {
		if (aMap == null) {
			aMap = mMapView.getMap();
			setConfigrationAmap();
		}
	}
	//配置Amap对象
	public void setConfigrationAmap() {
		aMap.setLocationSource(LocationSourceActivity.this);//设置定位监听
		aMap.setMyLocationEnabled(true);//设置显示定位层，并可以出发定位
		aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置显示定位按  钮
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);//设置定位类型
	}
	// 必须重写
	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
		mMapView.onSaveInstanceState(outState);
	}
	// 必须重写
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();

	}
	// 必须重写
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
		deactivate();
	}
	// 必须重写
	@Override
	protected void onDestroy() {
		super.onDestroy();

//		mMapView.onDestroy();
		if(mapLocationClient!=null){
			mapLocationClient.onDestroy();
		}
	}
	//激活定位
	@Override
	public void activate(OnLocationChangedListener onLocationChangedListener) {
		System.out.println("已经激活定位-------------activate");
		mListener=onLocationChangedListener;
		if(mapLocationClient==null){
			try {
				mapLocationClient=new AMapLocationClient(LocationSourceActivity.this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mapLocationClientOption=new AMapLocationClientOption();
			mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//设置定位模式为 高精度
			mapLocationClient.setLocationOption(mapLocationClientOption);//设置配置
			mapLocationClient.setLocationListener(this);//设置位置变化监听
			mapLocationClient.startLocation();
		}
	}

	//关闭定位
	@Override
	public void deactivate() {

		mListener=null;
		if(mapLocationClient!=null){
			mapLocationClient.stopLocation();
			mapLocationClient.onDestroy();
		}
		mapLocationClient =null;
		System.out.println("已经关闭定位-------------deactivate");
	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if(mListener!=null&&aMapLocation!=null){
			if(aMapLocation.getErrorCode() == 0){
				//Log.e(TAG, "获取经纬度集合" + privLocation)
				Toast.makeText(LocationSourceActivity.this,aMapLocation.getAddress(),Toast.LENGTH_SHORT).show();
				mListener.onLocationChanged(aMapLocation);
				System.out.println(aMapLocation.getLatitude() + "----" + aMapLocation.getLongitude() + "---------" + aMapLocation.getErrorCode());
				Toast.makeText(LocationSourceActivity.this, aMapLocation.getLatitude() + "----" + aMapLocation.getLongitude() + "---------" + aMapLocation.getErrorCode(),Toast.LENGTH_SHORT).show();
				//增加绘制轨迹功能
				if (aMapLocation.getLocationType() == 1) {

					Location location = null;
//					location.latitude = aMapLocation.getLatitude();
//					location.longitutd = aMapLocation.getLongitude();
					drawLines(aMapLocation);//一边定位一边连线
					distance += distance;
					Toast.makeText(LocationSourceActivity.this, "经纬度"+distance+"KM",Toast.LENGTH_SHORT).show();

					Log.e("DDDDDDDDD", String.valueOf(distance));
					//Log.e(TAG, "获取点的类型" + aMapLocation.getLocationType());
					Log.e("LLLLL", String.valueOf(location.getLongitude()));
					Log.e("LLLLLLLL", String.valueOf(location.getLongitude()));
				}
				//获取定位时间
				java.text.SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date(aMapLocation.getTime());
				df.format(date);
				privLocation = aMapLocation;
			}
		}else{

			Toast.makeText(LocationSourceActivity.this,"定位失败:"+aMapLocation.getErrorCode(),Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 绘制运动路线
	 *
	 * @param curLocation
	 */
	public void drawLines(AMapLocation curLocation) {

		if (null == privLocation) {
			return;
		}
		PolylineOptions options = new PolylineOptions();
		//上一个点的经纬度
		options.add(new LatLng(privLocation.getLatitude(), privLocation.getLongitude()));
		//当前的经纬度
		options.add(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()));
		options.width(10).geodesic(true).color(Color.GREEN);
		aMap.addPolyline(options);
		//距离的计算
		distance = AMapUtils.calculateLineDistance(new LatLng(privLocation.getLatitude(),
				privLocation.getLongitude()), new LatLng(curLocation.getLatitude(),
				curLocation.getLongitude()));

	}


}