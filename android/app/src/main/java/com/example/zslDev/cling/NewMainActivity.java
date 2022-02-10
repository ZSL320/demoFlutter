package com.example.zslDev.cling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.zslDev.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

public class NewMainActivity extends Activity {

    private final static String TAG = NewMainActivity.class.getSimpleName();

    public final static String BUNDLE_DEVICE_INFO_VERSION = "Main3Activity.BUNDLE_DEVICE_INFO_VERSION";
    public final static String BUNDLE_DEVICE_INFO_CLINGID = "Main3Activity.BUNDLE_DEVICE_INFO_CLINGID";

    private static ListView mListViewScanResult;
    private ArrayList<BluetoothDeviceInfo> mArrayListScanResult;
    private boolean mbDeviceConnected = false;
    private static boolean mbRevSos = false;
    private static PERIPHERAL_DEVICE_INFO_CONTEXT mDeviceInfo = null;

    private String mClingId;
    private int nUserid;

    private TextView tvClarData;
    private Button btnStartScan;
    private Button btnGetMinData;
    private EditText edDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //生成一个永久的
        initUserIdAndClingId();

        tvClarData = (TextView) findViewById(R.id.txtv_cal_data);
        btnStartScan = (Button) findViewById(R.id.txtv_startScan);
        btnGetMinData = (Button) findViewById(R.id.btn_get_minute_data);
        edDay = (EditText) findViewById(R.id.et_begin_time);

        BSClingManager.sharedInstance().addListener(new BSClingManagerListener() {
            @Override
            public void registerResult(boolean result, String msg, String clingId) {
                Log.d(TAG, "registerResult:result="+result+".msg=" + msg + ".cid=" + clingId);
                mClingId = clingId;
            }

            @Override
            public void connectStateChanged(int state) {
                mbDeviceConnected = state == 1;
                Log.d(TAG, "connectStateChanged:state="+state);
            }

            @Override
            public void receiveData(ArrayList<MinuteData> minuteData) {

            }

            @Override
            public void dayTotal(TreeSet<DayTotalDataModel> dayData) {

            }

            @Override
            public void syncProgress(long current, long total) {

            }

            @Override
            public void syncFinished() {

            }

            @Override
            public void logInfo(String log) {
                Log.d(TAG, log);
            }

            @Override
            public void scanResult(ArrayList<BluetoothDeviceInfo> arrayList) {
                updateScanResultView(arrayList);
            }

            @Override
            public void deviceInfo(String cid) {
                mClingId = cid;
                SharedPreferences sharedPreferences = NewMainActivity.this.getSharedPreferences("bsble_cling_data", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("bscling_clingId", mClingId).apply();
            }
        });

        //初始化
        BSClingManager.sharedInstance().initSDK(this);

//        requestPermission();//如果后面需要对申请的权限做集中处理，可以在这个函数中做
//        If you need to do centralized processing for the applied permission later, you can do it in requestPermission()

        Button btnRegister = (Button) findViewById(R.id.txtv_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BSClingManager.sharedInstance().registerDevice(nUserid, false, null);
            }
        });

        btnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btnStartScan 11");
                BSClingManager.sharedInstance().scan();
            }
        });

        Button btnStopScan = (Button) findViewById(R.id.txtv_stopScan);
        btnStopScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BSClingManager.sharedInstance().stopScan();
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
                                BSClingManager.sharedInstance().registerDevice(nUserid, true, bleinfo);
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
                    Intent intent = new Intent(NewMainActivity.this, BleActivity.class);
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
                    Intent intent = new Intent(NewMainActivity.this, UpgradeFirmwareActivity.class);
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
                if (nUserid > 0 && mClingId.length() > 0) {
                    BSClingManager.sharedInstance().connectDevice(nUserid, mClingId);
                } else {
                    Log.d(TAG, "Cling或者cid为空，无法直接连接");
                }
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

                BSClingManager.sharedInstance().getMinData(day);
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
        BSClingManager.sharedInstance().resume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        BSClingManager.sharedInstance().pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        BSClingManager.sharedInstance().unInitSDK();
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    private void updateScanResultView(final ArrayList<BluetoothDeviceInfo> arrlistDevices) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListViewScanResult != null && arrlistDevices != null) {
                    mArrayListScanResult = arrlistDevices;
                    ArrayList<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
                    for (BluetoothDeviceInfo bleinfo : arrlistDevices) {
                        String name = bleinfo.getmBleDevice().getName();
                        if(name.contains("AURA") || bleinfo.getmBleDevice().getName().contains("ETETH")){
                            Log.i(TAG, String.format("device: %s, rssi:%d", bleinfo.getmBleDevice().getName(), bleinfo.getmRssi()));
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("DEVNAME", bleinfo.getmBleDevice().getName());
                            map.put("RSSI", String.valueOf(bleinfo.getmRssi()) + "db");
                            contents.add(map);
                        }
                    }
                    SimpleAdapter adapter = new SimpleAdapter(NewMainActivity.this, contents, R.layout.list_scan_item, new String[]{"DEVNAME", "RSSI"}, new int[]{R.id.listitem_device_name, R.id.listitem_rssi});
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
                Toast.makeText(NewMainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

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


    ///--------------private method--------------///
    void initUserIdAndClingId() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("bsble_cling_data", Context.MODE_PRIVATE);
        nUserid = sharedPreferences.getInt("bscling_userId", 0);
        if (nUserid == 0) {
            nUserid = new Random().nextInt(100000) + 100;
            sharedPreferences.edit().putInt("bscling_userId", nUserid).apply();
        }
        if (mClingId == null) {
            mClingId = sharedPreferences.getString("bscling_clingId", "");
        }
    }

}
