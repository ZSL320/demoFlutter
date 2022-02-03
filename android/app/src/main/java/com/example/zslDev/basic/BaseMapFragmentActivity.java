package com.example.zslDev.basic;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.SupportMapFragment;
import com.example.zslDev.R;

public class BaseMapFragmentActivity extends FragmentActivity {
	private AMap mMap;
	private MapView mapView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basemap_fragment_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		setUpMapIfNeeded();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap =mapView.getMap();
		}
	}

}
