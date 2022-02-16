package com.example.zslDev;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;
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
import com.example.zslDev.cling.Main3Activity;
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
import com.example.zslDev.view.FeatureView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.example.zslDev.MainActivity.mainActivity;
//import com.demo.app.poisearch.PoiAroundSearchActivity;
//import com.demo.app.poisearch.PoiKeywordSearchActivity;
//import com.demo.app.route.RouteActivity;
//import com.demo.app.basic.BaseMapFragmentActivity;

public class MyPlatformView  implements PlatformView {

    private View mNativeView;
    private TextView mTvCount;
    private Button mBtnAdd;
    private int mAndroidButtonClickedNumber = 0;
    private MethodChannel mChannel;
    private Button button;
    private Button buttonAndroidFlutter;
    private ListView listView;
    private int count=0;
    public MyPlatformView(Context context, MethodChannel channel) {
        mChannel = channel;
        //高德地图listview:
//        try {
//            MapsInitializer.updatePrivacyAgree(context,true);
//            MapsInitializer.updatePrivacyShow(context,true,true);
//        }catch (Exception e){}
//        mNativeView = LayoutInflater.from(context).inflate(R.layout.my_list_view,null,false);
//        listView=mNativeView.findViewById(R.id.lv);
//        listView.setAdapter(new CustomArrayAdapter(
//                context, Arrays.asList(demos)));
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                mainActivity.startActivity(new Intent(mainActivity,Arrays.asList(demos).get(i).activityClass));
//            }
//        });
        mNativeView=LayoutInflater.from(context).inflate(R.layout.activity_main_demo,null);
        button=(Button)mNativeView.findViewById(R.id.btn_add);
        mTvCount=(TextView)mNativeView.findViewById(R.id.tv_count);
        mTvCount.setText("Flutter的按钮被点击数量：0");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                Map<String,String>map =new HashMap<>();
                map.put("AndroidButtonClickedNumber",String.valueOf(count));
                mChannel.invokeMethod("addAndroidButtonAndNoticeFlutter",map);
            }
        });

    }


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

    private class CustomArrayAdapter extends BaseAdapter {
        private  Context context;
        private List<DemoDetails> data;
        public CustomArrayAdapter(Context context, List<DemoDetails> data) {
           this.context=context;
           this.data=data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder viewHolder=new MyViewHolder();
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.my_text_view,parent,false);
                viewHolder.demoDetails=convertView.findViewById(R.id.tv);
                convertView.setTag(viewHolder);
            }else{
                viewHolder=(MyViewHolder)convertView.getTag();
            }
            viewHolder.demoDetails.setText(data.get(position).titleId);
           return convertView;
        }
    }
    private final class MyViewHolder{
        TextView demoDetails;
    }
    private static final DemoDetails[] demos = {
            new DemoDetails(R.string.myDemoCling, R.string.ClingWatch,
                    Main3Activity.class),
            new DemoDetails(R.string.myDemoListMap, R.string.myListMapCompat,
                    IndexActivity.class),
            new DemoDetails(R.string.myDemo, R.string.myDemoFinish,
                    MainActivityDemo.class),
            new DemoDetails(R.string.location_map, R.string.location_data,
                    LocationDataActivity.class),
            new DemoDetails(R.string.basic_map, R.string.basic_description,
                    BasicMapActivity.class),
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
    /**
     * 返回嵌入到Flutter页面中的安卓原生view
     * @return
     */
    @Override
    public View getView() {
        return mNativeView;
    }

    /**
     * Flutter的按钮被点击数量+1
     * 安卓原生会显示Flutter的按钮被点击的数量
     */
    public void showFlutterButtonClickedNumber(String number){
        mTvCount.setText("Flutter的按钮被点击数量："+number);
    }
    @Override
    public void onFlutterViewAttached(@NonNull View flutterView) { }
    @Override
    public void onFlutterViewDetached() { }
    @Override
    public void dispose() { }
}
