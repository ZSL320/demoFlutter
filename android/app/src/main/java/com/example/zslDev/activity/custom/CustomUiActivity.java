package com.example.zslDev.activity.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amap.api.navi.AMapNavi;
import com.example.zslDev.R;
import com.example.zslDev.activity.CustomCarActivity;
import com.example.zslDev.activity.CustomDirectionViewActivity;
import com.example.zslDev.activity.CustomDriveWayViewActivity;
import com.example.zslDev.activity.CustomNextTurnTipViewActivity;
import com.example.zslDev.activity.CustomRouteActivity;
import com.example.zslDev.activity.CustomRouteTextureInAMapNaviViewActivity;
import com.example.zslDev.activity.CustomTrafficButtonViewActivity;
import com.example.zslDev.activity.CustomTrafficProgressBarActivity;
import com.example.zslDev.activity.CustomZoomButtonViewActivity;
import com.example.zslDev.activity.CustomZoomInIntersectionViewActivity;
import com.example.zslDev.activity.NorthModeActivity;
import com.example.zslDev.activity.OverviewModeActivity;

public class CustomUiActivity extends Activity {
    private String[] examples = new String[]

            {"自定义车标", "自定义路线UI", "自定义路线纹理", "自定义路口转向提示", "正北模式", "自定义全览按钮", "自定义指南针", "自定义路况按钮", "自定义放大缩小按钮",
                    "自定义路口放大图", "自定义导航光柱(新版)", "自定义道路选择"
            };

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {//自定义车标
                startActivity(new Intent(CustomUiActivity.this, CustomCarActivity.class));
            } else if (position == 1) {//自定义路线UI
                startActivity(new Intent(CustomUiActivity.this, CustomRouteActivity.class));
            } else if (position == 2) {
                startActivity(new Intent(CustomUiActivity.this, CustomRouteTextureInAMapNaviViewActivity.class));
            } else if (position == 3) {//自定义路口转向提示
                startActivity(new Intent(CustomUiActivity.this, CustomNextTurnTipViewActivity.class));
            } else if (position == 4) {//正北模式
                startActivity(new Intent(CustomUiActivity.this, NorthModeActivity.class));
            } else if (position == 5) {//自定义全览按钮
                startActivity(new Intent(CustomUiActivity.this, OverviewModeActivity.class));
            } else if (position == 6) {//自定义指南针
                startActivity(new Intent(CustomUiActivity.this, CustomDirectionViewActivity.class));
            } else if (position == 7) {//自定义路况按钮
                startActivity(new Intent(CustomUiActivity.this, CustomTrafficButtonViewActivity.class));
            } else if (position == 8) {//自定义放大缩小按钮
                startActivity(new Intent(CustomUiActivity.this, CustomZoomButtonViewActivity.class));
            } else if (position == 9) {//自定义路况放大图
                startActivity(new Intent(CustomUiActivity.this, CustomZoomInIntersectionViewActivity.class));
            } else if (position == 10) {//自定义光柱
                startActivity(new Intent(CustomUiActivity.this, CustomTrafficProgressBarActivity.class));
            } else if (position == 11) {//自定义道路选择
                startActivity(new Intent(CustomUiActivity.this, CustomDriveWayViewActivity.class));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initView();

    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examples));
        setTitle("导航SDK " + AMapNavi.getVersion());
        listView.setOnItemClickListener(mItemClickListener);
    }

}
