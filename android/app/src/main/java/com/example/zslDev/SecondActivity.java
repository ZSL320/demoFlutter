package com.example.zslDev;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapsInitializer;
import com.example.zslDev.activity.IndexActivity;
import com.example.zslDev.basic.BaseMapFragmentActivity;
import com.example.zslDev.basic.BasicMapActivity;
import com.example.zslDev.basic.CameraActivity;
import com.example.zslDev.basic.EventsActivity;
import com.example.zslDev.basic.HeatMapActivity;
import com.example.zslDev.basic.LayersActivity;
//import com.demo.app.basic.MapOptionActivity;
import com.example.zslDev.basic.OpenglActivity;
import com.example.zslDev.basic.PoiClickActivity;
import com.example.zslDev.basic.ScreenShotActivity;
import com.example.zslDev.basic.UiSettingsActivity;
import com.example.zslDev.busline.BuslineActivity;
//import com.demo.app.district.DistrictActivity;
//import com.demo.app.geocoder.GeocoderActivity;
import com.example.zslDev.location.LocationGPSActivity;
import com.example.zslDev.location.LocationModeSourceActivity;
import com.example.zslDev.location.LocationNetworkActivity;
import com.example.zslDev.location.LocationSensorSourceActivity;
import com.example.zslDev.location.LocationSourceActivity;
import com.example.zslDev.locationData.LocationDataActivity;
import com.example.zslDev.offlinemap.OfflineMapActivity;
import com.example.zslDev.overlay.ArcActivity;
import com.example.zslDev.overlay.CircleActivity;
import com.example.zslDev.overlay.GroundOverlayActivity;
import com.example.zslDev.overlay.MarkerActivity;
import com.example.zslDev.overlay.NavigateArrowOverlayActivity;
import com.example.zslDev.overlay.PolygonActivity;
import com.example.zslDev.overlay.PolylineActivity;
import com.example.zslDev.poisearch.PoiAroundSearchActivity;
import com.example.zslDev.poisearch.PoiKeywordSearchActivity;
import com.example.zslDev.poisearch.PoiSearchActivity;
import com.example.zslDev.util.CheckPermissionsActivity;
import com.example.zslDev.view.FeatureView;

import java.util.ArrayList;
import java.util.List;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformViewFactory;
//import com.demo.app.poisearch.PoiAroundSearchActivity;
//import com.demo.app.poisearch.PoiKeywordSearchActivity;
//import com.demo.app.route.RouteActivity;
//import com.demo.app.basic.BaseMapFragmentActivity;

/**
 * AMapV2地图demo总汇
 */
public final class SecondActivity extends ListActivity {
    private static class DemoDetails {
        private final int titleId;
        private final int descriptionId;
        private final Class<? extends android.app.Activity> activityClass;

        public DemoDetails(int titleId, int descriptionId,
                           Class<? extends android.app.Activity> activityClass) {
            super();
            this.titleId = titleId;
            this.descriptionId = descriptionId;
            this.activityClass = activityClass;
        }
    }

    private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {
        public CustomArrayAdapter(Context context, DemoDetails[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }
            DemoDetails demo = getItem(position);
            featureView.setTitleId(demo.titleId);
            featureView.setDescriptionId(demo.descriptionId);
            return featureView;
        }
    }

    private static final DemoDetails[] demos = {
            new DemoDetails(R.string.myDemoListMap, R.string.myListMapCompat,
                    IndexActivity.class),
            new DemoDetails(R.string.myDemo, R.string.myDemoFinish,
                    MainActivityDemo.class),
            new DemoDetails(R.string.location_map, R.string.location_data,
                    LocationDataActivity.class),
            new DemoDetails(R.string.basic_map, R.string.basic_description,
                    BasicMapActivity.class),
            new DemoDetails(R.string.base_fragment_map,
                    R.string.base_fragment_description,
                    BaseMapFragmentActivity.class),
            new DemoDetails(R.string.camera_demo, R.string.camera_description,
                    CameraActivity.class),
            new DemoDetails(R.string.events_demo, R.string.events_description,
                    EventsActivity.class),
            new DemoDetails(R.string.layers_demo, R.string.layers_description,
                    LayersActivity.class),
            new DemoDetails(R.string.heatmap_demo,
                    R.string.heatmap_description, HeatMapActivity.class),
            new DemoDetails(R.string.poiclick_demo,
                    R.string.poiclick_description, PoiClickActivity.class),
//            new DemoDetails(R.string.mapOption_demo,
//                    R.string.mapOption_description, MapOptionActivity.class),
            new DemoDetails(R.string.screenshot_demo,
                    R.string.screenshot_description, ScreenShotActivity.class),
            new DemoDetails(R.string.opengl_demo, R.string.opengl_description,
                    OpenglActivity.class),
            new DemoDetails(R.string.uisettings_demo,
                    R.string.uisettings_description, UiSettingsActivity.class),
            new DemoDetails(R.string.polyline_demo,
                    R.string.polyline_description, PolylineActivity.class),
            new DemoDetails(R.string.polygon_demo,
                    R.string.polygon_description, PolygonActivity.class),
            new DemoDetails(R.string.circle_demo, R.string.circle_description,
                    CircleActivity.class),
            new DemoDetails(R.string.marker_demo, R.string.marker_description,
                    MarkerActivity.class),
            new DemoDetails(R.string.arc_demo, R.string.arc_description,
                    ArcActivity.class),
            new DemoDetails(R.string.groundoverlay_demo,
                    R.string.groundoverlay_description,
                    GroundOverlayActivity.class),
            new DemoDetails(R.string.navigatearrow_demo,
                    R.string.navigatearrow_description,
                    NavigateArrowOverlayActivity.class),
//            new DemoDetails(R.string.geocoder_demo,
//                    R.string.geocoder_description, GeocoderActivity.class),
            new DemoDetails(R.string.locationsource_demo,
                    R.string.locationsource_description,
                    LocationSourceActivity.class),
            new DemoDetails(R.string.locationmodesource_demo,
                    R.string.locationmodesource_description,
                    LocationModeSourceActivity.class),
            new DemoDetails(R.string.locationSensorGPS_demo,
                    R.string.locationSensorGPS_demo_description,
                    LocationSensorSourceActivity.class),
            new DemoDetails(R.string.locationGPS_demo,
                    R.string.locationGPS_description, LocationGPSActivity.class),
            new DemoDetails(R.string.locationNetwork_demo,
                    R.string.locationNetwork_description,
                    LocationNetworkActivity.class),
            new DemoDetails(R.string.poikeywordsearch_demo,
                    R.string.poikeywordsearch_description,
                    PoiKeywordSearchActivity.class),
            new DemoDetails(R.string.poiaroundsearch_demo,
                    R.string.poiaroundsearch_description,
                    PoiAroundSearchActivity.class),
            new DemoDetails(R.string.busline_demo,
                    R.string.busline_description, BuslineActivity.class),
//            new DemoDetails(R.string.route_demo, R.string.route_description,
//                    RouteActivity.class),
            new DemoDetails(R.string.offlinemap_demo,
                    R.string.offlinemap_description, OfflineMapActivity.class),
            new DemoDetails(R.string.poi_demo,
                    R.string.demo_set, PoiSearchActivity.class),
//            new DemoDetails(R.string.district_demo,
//                    R.string.district_description, DistrictActivity.class)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            MapsInitializer.updatePrivacyAgree(this,true);
            MapsInitializer.updatePrivacyShow(this,true,true);
        }catch (Exception e){}
        setContentView(R.layout.main_activity);
        setTitle("3D地图Demo" + AMap.getVersion());
        ListAdapter adapter = new CustomArrayAdapter(
                this.getApplicationContext(), demos);
        setListAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        System.exit(0);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        DemoDetails demo = (DemoDetails) getListAdapter().getItem(position);
        startActivity(new Intent(this.getApplicationContext(),
                demo.activityClass));
    }

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
    }

    /**
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击设置权限打开所需权限");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
