package com.example.zslDev.cling;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.hicling.clingsdk.ClingSdk;
import com.hicling.clingsdk.bleservice.BluetoothDeviceInfo;
import com.hicling.clingsdk.devicemodel.PERIPHERAL_DEVICE_INFO_CONTEXT;
import com.hicling.clingsdk.devicemodel.PERIPHERAL_GOGPS_UPSTREAMDATA;
import com.hicling.clingsdk.devicemodel.PERIPHERAL_GOGPS_UP_STREAM_TRACK_INFO_V2;
import com.hicling.clingsdk.devicemodel.PERIPHERAL_USER_PROFILE;
import com.hicling.clingsdk.listener.OnBleListener;
import com.hicling.clingsdk.listener.OnGetGPSDataListener;
import com.hicling.clingsdk.listener.OnNetworkListener;
import com.hicling.clingsdk.model.DayTotalDataModel;
import com.hicling.clingsdk.model.MinuteData;
import com.hicling.clingsdk.model.SleepCycle;
import com.hicling.clingsdk.model.TimeActivityModel;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import com.example.zslDev.R;


public class Main3Activity extends Activity {

    private final static String TAG = Main3Activity.class.getSimpleName();

    public final static String BUNDLE_DEVICE_INFO_VERSION = "Main3Activity.BUNDLE_DEVICE_INFO_VERSION";
    public final static String BUNDLE_DEVICE_INFO_CLINGID = "Main3Activity.BUNDLE_DEVICE_INFO_CLINGID";


    static boolean mbClingSdkReady = false;
    static boolean mbClingRegistered = false;

    private static ListView mListViewScanResult;
    private ArrayList<BluetoothDeviceInfo> mArrayListScanResult;
    private boolean mbDeviceConnected = false;
    private static boolean mbRevSos = false;
    private static PERIPHERAL_DEVICE_INFO_CONTEXT mDeviceInfo = null;


    //    private String clingid = "HIGG00009056648915E4";
//    private String clingid = "HICPB9098533709FCCDF";
    private String clingid = "HICETETH073B974BC5D8";
//    private String clingid = "HICL0000D1169E90BAD8";

    private int nUserid = 5115557;
//    private int nUserid = 87635;

    private TextView tvClarData;
    private Button btnStartScan;
    private Button btnGetMinData;
    private EditText edDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        tvClarData = (TextView) findViewById(R.id.txtv_cal_data);
        btnStartScan = (Button) findViewById(R.id.txtv_startScan);
        btnGetMinData = (Button) findViewById(R.id.btn_get_minute_data);
        edDay = (EditText) findViewById(R.id.et_begin_time);

        Intent intent = new Intent(this, SysNotificationListenerService.class);
        startService(intent);

//        String mac = "D4:36:39:72:C8:56";

        // this is test appkey and appsecret  HCa3f4b08b799e28af   7978e5e9477d07dd5d7dc79fd1bc00d7
        // user should request your own key on our web page ( http://developers.hicling.com )
        //注：如果激活失败，可能是Android9.0系统，例如华为mate20,对网络请求https的要求问题导致
        ClingSdk.init(getApplicationContext(), "HCa3f4b08b799e28af", "7978e5e9477d07dd5d7dc79fd1bc00d7", new OnNetworkListener() {
            @Override
            public void onSucceeded(Object o, Object o1) {
                Log.i(TAG, "onSucceeded");
                mbClingSdkReady = true;
                ClingSdk.enableDebugMode(true);
                if(ClingSdk.isAccountBondWithCling()){
                    Log.i(TAG,"isAccountBondWithCling:"+ClingSdk.getBondClingDeviceName());
                }
                else{
                    Log.i(TAG, "Account Not Bonded");
                }




                ClingSdk.onGetGPSData(new OnGetGPSDataListener() {
                    @Override
                    public void onGetGPSData(PERIPHERAL_GOGPS_UPSTREAMDATA gps) {
                        Log.d(TAG, "onGetGPSData: gps : " + gps);
                    }

                    @Override
                    public void onGetGpsTotal(PERIPHERAL_GOGPS_UP_STREAM_TRACK_INFO_V2 total) {
                        Log.d(TAG, "onGetGpsTotal: " + total.toString());
                    }
                });
            }

            @Override
            public void onFailed(int i, String s) {
                Log.i(TAG, "onFailed, statusCode is:" + i + ", s :" + s);
            }
        });


        ClingSdk.enableDebugMode(true);
        ClingSdk.start(getApplicationContext());
        ClingSdk.setBleDataListener(mBleDataListener);
        ClingSdk.setDeviceConnectListener(mDeviceConnectedListener);
        ClingSdk.deregisterDevice(deregisterDeviceListener);

//        requestPermission();//如果后面需要对申请的权限做集中处理，可以在这个函数中做
//        If you need to do centralized processing for the applied permission later, you can do it in requestPermission()

        Button btnRegister = (Button) findViewById(R.id.txtv_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbClingSdkReady) {
                    ClingSdk.deregisterDevice(deregisterDeviceListener);
//                    ClingSdk.deregisterDevice(new OnBleListener.OnDeregisterDeviceListener() {
//
//                        @Override
//                        public void onDeregisterDeviceSucceed() {
//                            Log.i(TAG, "onDeregisterDeviceSucceed()");
//                            showToast("Deregister Device Succeed");
//                            updateScanText();
//                            mbClingRegistered = false;
//                            ClingSdk.clearDatabase();
//                        }
//
//                        @Override
//                        public void onDeregisterDeviceFailed(int i, String s) {
//                            Log.i(TAG, "onDeregisterDeviceFailed(): " + i + ", " + s);
//                        }
//                    });
                }
            }
        });


        btnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btnStartScan");
                if (mbClingSdkReady && !ClingSdk.isAccountBondWithCling()) {
                    ClingSdk.setClingDeviceType(ClingSdk.CLING_DEVICE_TYPE_ALL);
//						ClingSdk.setClingDeviceType ( ClingSdk.CLING_DEVICE_TYPE_WATCH_GOGPS );
                }

                if (mbClingSdkReady) {
                    ClingSdk.stopScan();
                    ClingSdk.startScan(10 * 60 * 1000, new OnBleListener.OnScanDeviceListener() { // 10 minutes
                        @Override
                        public void onBleScanUpdated(Object o) {
                            //蓝牙连接成功后，不会再扫描
                            //After Bluetooth connected successfully, stop scan
                            Log.i(TAG, "onBleScanUpdated()");
                            if (o != null) {
                                ArrayList<BluetoothDeviceInfo> arrayList = (ArrayList<BluetoothDeviceInfo>) o;
                                updateScanResultView(arrayList);
                            }
                        }
                    });

//                    if ( mbDeviceConnected ) {
//                        showDownloadPage ();
//                    }
                } else {
                    Log.i(TAG, "Cling sdk not ready, please try again");
                }
            }
        });

        Button btnStopScan = (Button) findViewById(R.id.txtv_stopScan);
        btnStopScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mbClingSdkReady) {
                    ClingSdk.stopScan();
                } else {
                    Log.i(TAG, "Cling sdk not ready, please try again");
                }
            }
        });

        mListViewScanResult = (ListView) findViewById(R.id.listview_scanResult);
        mListViewScanResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListAdapter adapter = mListViewScanResult.getAdapter();
                Object obj = adapter.getItem(position);
                if (obj != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> mapItem = (Map<String, Object>) obj;
                    String devname = (String) mapItem.get("DEVNAME");
//                    btnStartScan.setText(devname);
                    if (mArrayListScanResult != null) {
                        for (final BluetoothDeviceInfo bleinfo : mArrayListScanResult) {
                            if (bleinfo.getmBleDevice().getName().equals(devname)) {
                                ClingSdk.stopScan();
                                ClingSdk.registerDevice(nUserid, bleinfo.getmBleDevice(), new OnBleListener.OnRegisterDeviceListener() {
                                    @Override
                                    public void onRegisterDeviceSucceed() {
                                        Log.i(TAG, "onRegisterDeviceSucceed()");
//                                            if(mbDeviceConnected) {s
//                                                showDownloadPage();
//                                            }
                                        mbClingRegistered = true;
                                        updateScanText();


                                    }

                                    @Override
                                    public void onRegisterDeviceFailed(int i, String s) {
                                        Log.i(TAG, "onRegisterDeviceFailed() :" + i + ", " + s);
                                    }
                                });
                                break;
                            }
                        }
                    }


                }
            }
        });

        Button btnBle = (Button) findViewById(R.id.txtv_bleSetting);
        btnBle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbDeviceConnected) {
                    Intent intent = new Intent(Main3Activity.this, BleActivity.class);
                    startActivity(intent);
                } else {
                    showToast("ble is not connected");
                }
            }
        });

        Button btnUpgrade = (Button) findViewById(R.id.txtv_upgrade);
        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, "userid : //" + ClingData.getInstance().getUserId());
                if (mbDeviceConnected) {
                    Intent intent = new Intent(Main3Activity.this, UpgradeFirmwareActivity.class);
                    intent.putExtra(BUNDLE_DEVICE_INFO_VERSION, mDeviceInfo.softwareVersion);
                    intent.putExtra(BUNDLE_DEVICE_INFO_CLINGID, mDeviceInfo.clingId);
                    startActivity(intent);
                } else {
                    showToast("ble is not connected");
                }
            }
        });

        Button btnConnect = (Button) findViewById(R.id.txtv_bleconnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String clingid = "HICNF5EC4EC8DE5D7128";
//                ClingSdk.connectDeviceByClingid(87635, clingid);


                Log.i(TAG, "connectDeviceByClingid");
                //
                ClingSdk.connectDeviceByClingid(nUserid, clingid);
            }
        });


        Button btnLoadDeviceData = (Button) findViewById(R.id.txtv_loadDeviceData);
        btnLoadDeviceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClingSdk.loadDeviceData();
            }
        });


        Button btnUpdateClockFace = findViewById(R.id.btn_update_clock_face);
        btnUpdateClockFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start download clock face.");
                downloadClockFace();
            }
        });

        btnGetMinData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!TextUtils.isEmpty(edDay.getText())) {
                int day = Integer.parseInt(edDay.getText().toString());

                long timestamp = System.currentTimeMillis() / 1000;

                ArrayList<MinuteData> minuteDatas = ClingSdk.getMinuteData(timestamp - (3600 * 24 * day), timestamp);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < minuteDatas.size(); i++) {
                    sb.append(minuteDatas.get(i));
                }



                File file = new File(Environment.getExternalStorageDirectory(), "minutedata.txt");
                file.setWritable(true);
                file.setReadable(true);
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(file);
                    outStream.write(sb.toString().getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

//                Intent intent = new Intent(Main3Activity.this, MinuteDataActivity.class);
//                intent.putExtra("day", day);
//                Main3Activity.this.startActivity(intent);
//            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ClingSdk.setUserId(nUserid);
//        Log.i(TAG, "userid : " + ClingData.getInstance().getUserId());
        ClingSdk.onResume(getApplicationContext());
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        ClingSdk.onPause(getApplicationContext());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ClingSdk.stop(getApplicationContext());
        Log.i(TAG, "onDestroy()");
        super.onDestroy();

        Intent intent = new Intent(this, SysNotificationListenerService.class);
        stopService(intent);
    }


//    private void checkAppid() {
//        String mac = "D4:36:39:72:C8:56";
//        String clingId = "HIGG00009056648915E4";
//        ClingSdk.checkSdkAppId(clingId, "HCa3f4b08b799e28af", null, null, new OnNetworkListener() {
//            @Override
//            public void onSucceeded(Object o, Object o1) {
//                Log.i(TAG, "onSucceeded");
//            }
//
//            @Override
//            public void onFailed(int i, String s) {
//                Log.i(TAG, "onFailed, statusCode is:" + i);
//            }
//        });
//    }


    private void updateScanResultView(final ArrayList<BluetoothDeviceInfo> arrlistDevices) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListViewScanResult != null && arrlistDevices != null) {
                    mArrayListScanResult = arrlistDevices;
                    ArrayList<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
                    for (BluetoothDeviceInfo bleinfo : arrlistDevices) {
                        if(bleinfo.getmBleDevice().getName().contains("ETETH")){
                            Log.i(TAG, String.format("device: %s, rssi:%d", bleinfo.getmBleDevice().getName(), bleinfo.getmRssi()));
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("DEVNAME", bleinfo.getmBleDevice().getName());
                            map.put("RSSI", String.valueOf(bleinfo.getmRssi()) + "db");
                            contents.add(map);
                        }

                    }
                    SimpleAdapter adapter = new SimpleAdapter(Main3Activity.this, contents, R.layout.list_scan_item, new String[]{"DEVNAME", "RSSI"}, new int[]{R.id.listitem_device_name, R.id.listitem_rssi});
                    mListViewScanResult.setAdapter(adapter);
                }
            }
        });
    }

    private void updateScanText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ClingSdk.isAccountBondWithCling()) {
                    String clingId = ClingSdk.getBondClingDeviceName();

                    int clingType = ClingSdk.getClingDeviceType();
                    String strPrefix = "U";
                    switch (clingType) {
                        case ClingSdk.CLING_DEVICE_TYPE_WATCH_1:
                            strPrefix = "W";
                            break;
                        case ClingSdk.CLING_DEVICE_TYPE_BAND_1:
                            strPrefix = "B1";
                            break;
                        case ClingSdk.CLING_DEVICE_TYPE_BAND_2:
                            strPrefix = "B2";
                            break;
                        case ClingSdk.CLING_DEVICE_TYPE_BAND_3:
                            strPrefix = "B3";
                            break;
                        case ClingSdk.CLING_DEVICE_TYPE_BAND_PACE:
                            strPrefix = "PA";
                            break;
                        case ClingSdk.CLING_DEVICE_TYPE_BAND3_COLOR://CLING AURA
                            strPrefix = "AU";
                            break;
                        case ClingSdk.CLING_DEVICE_TYPE_WATCH_GOGPS://CLING LEAP
                            strPrefix = "GO";
                            break;
                        case ClingSdk.CLING_DEVICE_TYPE_THERMO://CLING THERMO
                            strPrefix = "TH";
                            break;
                        case ClingSdk.CLING_DEVICE_TYPE_ETETH07://ETETH07
                            strPrefix = "E7";
                            break;
                    }

                    btnStartScan.setText(strPrefix + " " + clingId);
                } else {
                    btnStartScan.setText("Start Scan");
                }
            }
        });
    }


    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Main3Activity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private OnBleListener.OnDeregisterDeviceListener deregisterDeviceListener = new OnBleListener.OnDeregisterDeviceListener() {
        @Override
        public void onDeregisterDeviceSucceed() {
            Log.i(TAG, "onDeregisterDeviceSucceed()");
            showToast("Deregister Device Succeed");
            updateScanText();
            mbClingRegistered = false;
            ClingSdk.clearDatabase();
//            Main3Activity.this.finish();
        }

        @Override
        public void onDeregisterDeviceFailed(int i, String s) {
            Log.i(TAG, "onDeregisterDeviceFailed(): " + i + ", " + s);
        }
    };


    private OnBleListener.OnBleDataListener mBleDataListener = new OnBleListener.OnBleDataListener() {
        @Override
        public void onGotSosMessage() {
            Log.i(TAG, "received sos message");
            showToast("received sos message");
            mbRevSos = true;
        }

        @Override
        public void onDataSyncingProgress(Object o) {
            Log.i(TAG, "onDataSyncingProgress " + o.toString());

        }

        @Override
        public void onDataSyncedFromDevice() {
            Log.i(TAG, "data synced");
            showToast("data synced");

            long timestamp = System.currentTimeMillis() / 1000;

            long startTime = timestamp - 7 * 24 * 3600;
            long endTime = timestamp;
            final TreeSet<DayTotalDataModel> dayTotal = ClingSdk.getDayTotalList(startTime, endTime);
            Log.i(TAG, "startTime : " + startTime + ", endTime : " + endTime + " , dayTotal :" + dayTotal);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != dayTotal) {
                        Iterator<DayTotalDataModel> it = dayTotal.iterator();
                        while (it.hasNext()) {
                            DayTotalDataModel model = it.next();
                            tvClarData.setText("Sport Calories:" + model.mCaloriesSport);
                        }
                    }
                }
            });

            TreeSet<TimeActivityModel> sportBubble = ClingSdk.getSportBubble(timestamp - 3600 * 24 * 2, timestamp);
            Log.i(TAG, "sportBubble:" + sportBubble);
            if (sportBubble != null) {
                Iterator<TimeActivityModel> it = sportBubble.iterator();
                while (it.hasNext()) {
                    Log.i(TAG, it.next().toString());
                }
            }

            ArrayList<MinuteData> minuteDatas = ClingSdk.getMinuteData(timestamp - 3600 * 48, timestamp - 3600 * 24);

//            if (null != minuteDatas && minuteDatas.size() > 0) {
//                for (int i = 0; i < minuteDatas.size(); i++) {
//                    Log.i(TAG, minuteDatas.get(i) + "");
//                }
//            }

            ClingSdk.generateSportBubble(minuteDatas);
            ClingSdk.generateSleepBubble(minuteDatas);

            TreeSet<TimeActivityModel> bubbles = ClingSdk.getSportBubble(timestamp - 3600 * 48, timestamp - 3600 * 24);
            Log.i(TAG, "bubble : , sportBubble :" + bubbles);


            int sleepScore = ClingSdk.getSleepScore(minuteDatas, 0, 1, 26);
            Log.i(TAG, "sleep score :" + sleepScore);

            ArrayList<SleepCycle> sleepCycles = ClingSdk.getSleepCycle(minuteDatas, 26);
            Log.i(TAG, "sleepCycle : " + sleepCycles);

            long setStartTime = timestamp - 2 * 24 * 3600;
            TreeSet<DayTotalDataModel> dayTotalSet = ClingSdk.getDayTotalSet(setStartTime, timestamp);
            Log.i(TAG, "dayTotalSet : setStartTime : " + setStartTime + ", endTime : " + timestamp + " , dayTotal :" + dayTotalSet);

            float heartrateLevel = ClingSdk.getHeartratePointWithLevel(0, 0, 30, 13, 0, 0, 0, 0);
            Log.i(TAG, "heartrateLevel: " + heartrateLevel);
            float totalHealthScore = ClingSdk.getTotalsocreWithLevel(0, 13, 0, 0, 0, 0);
            Log.i(TAG, "totalHealthScore: " + totalHealthScore);

        }

        @Override
        public void onDataSyncingMinuteData(Object o) {
            if (o != null) {
                Log.i(TAG, "onDataSyncingMinuteData is: " + o.toString());  //MinuteData
            }
        }

        @Override
        public void onGetDayTotalData(DayTotalDataModel dayTotalDataModel) {
            Log.i(TAG, "dayTotalDataModel : " + dayTotalDataModel.toString());

        }
    };


    private OnBleListener.OnDeviceConnectedListener mDeviceConnectedListener = new OnBleListener.OnDeviceConnectedListener() {
        @Override
        public void onDeviceConnected() {
            Log.i(TAG, "onDeviceConnected()");
            showToast("Device Connected");
            mbDeviceConnected = true;
            updateScanText();


        }

        @Override
        public void onDeviceDisconnected() {
            Log.i(TAG, "onDeviceDisconnected()");
            showToast("device is disconnected");
            mbDeviceConnected = false;
        }

        @Override
        public void onDeviceInfoReceived(Object o) {
            Log.d(TAG, "onDeviceInfoReceived: o :" + o);
            if (o != null) {
                updateScanText();
                mDeviceInfo = (PERIPHERAL_DEVICE_INFO_CONTEXT) o;
                Log.i(TAG, "deviceInfo : " + mDeviceInfo.toString());
                Log.i(TAG, "onDeviceInfoReceived: " + mDeviceInfo.softwareVersion);

                if (mbClingRegistered || ClingSdk.getIsDeviceRegistered(mDeviceInfo, nUserid)) {
                    Log.i(TAG, "onDeviceInfoReceived: setconfig");
                    PERIPHERAL_USER_PROFILE pup = new PERIPHERAL_USER_PROFILE();
                    pup.height_in_cm = 170;
                    pup.weight_in_kg = 60;
                    pup.units_type = pup.PERIPHERAL_PROFILE_METRICS_INTERNATIONAL;
                    pup.nickname_len = 11;
                    pup.nickname = "Cling Cling";
                    pup.clock_orientation = pup.PERIPHERAL_PROFILE_CLOCK_ORIENTATION_HORIZONTAL;

                    int weekday = 0;
                    //Monday：0，Tuesday：1，Wednesday：2，Thursday: 3，Friday：,4，Saturday：5,Sunday：6
//                    weekday |= 1 << 0;//Monday
//                    weekday |= 1 << 1;//Tuesday
//                    weekday |= 1 << 2;//Wednesday
//                    weekday |= 1 << 3;//Thursday
                    weekday |= 1 << 4;//Friday

                    weekday |= (1 << 7);//enable
//                    weekday &= ~(1 << 7);//unable
                    pup.sleep_alarm_day_of_week = weekday;

                    pup.bed_hr = 14;
                    pup.bed_min = 40;
                    pup.wakeup_hr = 10;
                    pup.wakeup_min = 0;
//                    pup.screen_display_option = pup.PERIPHERAL_PROFILE_SCREEN_DISPLAY_OPTION_STEP | pup.PERIPHERAL_PROFILE_SCREEN_DISPLAY_OPTION_DISTANCE |
//                            pup.PERIPHERAL_PROFILE_SCREEN_DISPLAY_OPTION_CALORIES;
                    pup.touch_virbration = pup.PERIPHERAL_PROFILE_TOUCH_VIRBRATION_ON;
                    pup.daily_goal = 100;
                    pup.training_display_option = pup.PERIPHERAL_PROFILE_TRAINING_DISPLAY_OPTION_CADENCE | pup.PERIPHERAL_PROFILE_TRAINING_DISPLAY_OPTION_CALORIES;
                    pup.age = 20;
                    pup.sex = 1;
                    pup.pace_alarm_zone = pup.PERIPHERAL_PROFILE_PACE_ALARM_DEFAULT_VALUE;
                    pup.app_setting = pup.CLING_USER_PROFILE_APP_SETTING_DISTANCE_NORMALIZATION;
                    pup.caloryDisplayType = pup.CALORIES_DISPLAY_TYPE_ALL;
                    pup.healthinfolevel = 1;
                    ClingSdk.updateDeviceUserCfg(pup);
                }

            }
        }
    };

    private void requestPermission() {
        String[] permissionsUnderOreo = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WAKE_LOCK,
        };

        ActivityCompat.requestPermissions(this, permissionsUnderOreo, 1);
    }

    private void downloadClockFace() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    inputStream = httpDownloadFile(clockfaceurl);

                    if (inputStream == null) {
                        return;
                    }

                    byte[] clockFaceFile = inputStreamToByte(inputStream);
                    Log.d(TAG, " clock face length : " + clockFaceFile.length);

                    if (clockFaceFile != null && clockFaceFile.length > 0) {
                        updateClockFace(clockFaceFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    //Attention Please: please confirm the correct clockface url according to your hicling device
    //the next two clock face urls match CLING LEAP(HICL, ClingSdk.CLING_DEVICE_TYPE_WATCH_GOGPS)
//    private String clockfaceurl = "http://file.hicling.com/flies/sys/FACES/HICL/000/63b1470177ed4bc8ad01f29f0b6bfb54.bin";
//    private String clockfaceurl = "http://file.hicling.com/flies/sys/FACES/HICL/000/63b1470177ed4bc8ad01f29f0b6bfb54.bin";

    //the next two clock face urls match CLING AURA(HICA, ClingSdk.CLING_DEVICE_TYPE_BAND3_COLOR)
//    private String clockfaceurl = "http://file.hicling.com/flies/sys/FACES/HICA/000/7792f2adba334f5b80834defd11d241f.bin";
//    private String clockfaceurl = "http://file.hicling.com/flies/sys/FACES/HICA/000/c7cf65c03d004297a5a2f7beb4671ce2.bin";

    //the next two clock face urls match CLING THERMO(HICTMO, ClingSdk.CLING_DEVICE_TYPE_THERMO)
    private String clockfaceurl = "http://file.hicling.com/flies/sys/FACES/HICTMO/00/53796131975a4fe9b92615f8fb377a5f.bin";
//    private String clockfaceurl = "http://file.hicling.com/flies/sys/FACES/HICTMO/00/c892f8eada1a4baa88a5d47df997d4e2.bin";

    private void updateClockFace(byte[] clockFaceFile) {
        OnBleListener.OnBleFildDownloadListener listener = new OnBleListener.OnBleFildDownloadListener() {
            @Override
            public void onFileDownloadProgress(Object o) {
                Log.d(TAG, "onFileDownloadProgress: "+(double)o);
            }

            @Override
            public void onFileDownloadSucceeded() {
                Log.d(TAG, "onFileDownloadSucceeded: ");
//                ClingSdk.updateClockFace(clockFaceFile, clockFaceFile.length, 0, this);
            }

            @Override
            public void onFileDownloadFailed(int i, String s) {
                Log.d(TAG, "onFileDownloadFailed: ");
            }

            @Override
            public void onFileDownloadBleDisconnected() {
                Log.d(TAG, "onFileDownloadBleDisconnected: ");
            }

            @Override
            public void onFileDownloadBleConnected() {
                Log.d(TAG, "onFileDownloadBleConnected: ");
            }
        };

        ClingSdk.updateClockFace(clockFaceFile, clockFaceFile.length, 0, listener);
    }

    public InputStream httpDownloadFile(String urlStr) throws IOException, MalformedURLException {
        InputStream is = null;
        URL url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        is = urlConn.getInputStream();
        return is;
    }

    public byte[] inputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        is.close();
        return imgdata;
    }


}
