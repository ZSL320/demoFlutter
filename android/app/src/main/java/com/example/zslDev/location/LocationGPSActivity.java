package com.example.zslDev.location;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.example.zslDev.R;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationGPSActivity extends CheckPermissionsActivity implements LocationSource, AMapLocationListener {
		public AMapLocationClient client=null;
		private AMapLocationClientOption option=null;
		private MapView mapView;
		private AMap map;
		private TextView tvAdd;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			try {
				MapsInitializer.updatePrivacyAgree(this,true);
				MapsInitializer.updatePrivacyShow(this,true,true);
			}catch (Exception e){}
			setContentView(R.layout.locationsource_activity);
			try {
				initView(savedInstanceState);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void initListener() {
			client.setLocationListener(new AMapLocationListener() {
				@SuppressLint("SetTextI18n")
				@Override
				public void onLocationChanged(AMapLocation aMapLocation) {
					if(aMapLocation!=null){
						if(aMapLocation.getErrorCode()==0){
							aMapLocation.getLocationType();//获取当前结果来源,如网络定位.GPS定位
							double lat=aMapLocation.getLatitude();//获取纬度
							double lon=aMapLocation.getLongitude();//获取经度
							aMapLocation.getAccuracy();//获取精度信息
//                        option.setOnceLocation(true);
							SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date=new Date(aMapLocation.getTime());//定位时间
							LatLng latlon=new LatLng(lat,lon);
							MarkerOptions marker=new MarkerOptions();
							map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon,15));//显示在指定位置
							marker.position(latlon);
							marker.title("当前位置");
							marker.visible(true);
							BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.launch_background));
							marker.icon(bitmapDescriptor);
							map.addMarker(marker);
							tvAdd.setText("当前位置:"+aMapLocation.getSpeed()+"         "+format.format(date));
						}else{
//							Toast.makeText(LocationGPSActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
						}
					}
					map.setMyLocationEnabled(true);
				}
			});
		}

		private void initLocation() {
			option=new AMapLocationClientOption();
			option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			option.setNeedAddress(true);
			option.setInterval(60000);
			client.setLocationOption(option);
			client.startLocation();
		}

		private void initView(Bundle savedInstanceState) throws Exception {
			mapView = ((MapView) findViewById(R.id.map));
			tvAdd = ((TextView) findViewById(R.id.btnNewActivity));
			//此方法必须重写
			mapView.onCreate(savedInstanceState);
			client=new AMapLocationClient(getApplicationContext());
			initListener();
			map=mapView.getMap();
			map.setMapType(AMap.MAP_TYPE_NORMAL);
			initLocation();

		}

		@Override
		protected void onResume() {
			super.onResume();
			mapView.onResume();
		}

		@Override
		protected void onPause() {
			super.onPause();
			mapView.onPause();
		}

		@Override
		protected void onDestroy() {
			super.onDestroy();
			client.onDestroy();
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			mapView.onSaveInstanceState(outState);
		}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {

	}

	@Override
	public void activate(OnLocationChangedListener onLocationChangedListener) {

	}

	@Override
	public void deactivate() {

	}
}