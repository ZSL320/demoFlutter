package com.example.zslDev.cling;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * 1。扫描蓝牙
 * 2。连接蓝牙；
 * 3。注册设备；(跟着第二部自动执行了）
 * 4。连接设备（有蓝牙通道的基础上）；
 */
public class BSClingManager {
    private static final String TAG = "BSClingManagerActivity";
    private BSClingManagerListener mListener;
    private Context mContext;
    private int mUid;
    private String mCid = "";
    static boolean mbClingSdkReady = false;
    static boolean mbClingRegistered = false;

    private ArrayList<BluetoothDeviceInfo> mArrayListScanResult;
    private boolean mbDeviceConnected = false;
    private static boolean mbRevSos = false;
    private static PERIPHERAL_DEVICE_INFO_CONTEXT mDeviceInfo = null;

    //
    public void addListener(BSClingManagerListener listener) {
        mListener = listener;
    }

    public void scan() {
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
                    Log.i(TAG, "onBleScanUpdated() = "+o);
                    if (o != null) {
                        ArrayList<BluetoothDeviceInfo> arrayList = (ArrayList<BluetoothDeviceInfo>) o;
                        if (mListener != null) {
                            mListener.scanResult(arrayList);
                        }
                    }
                }
            });
        } else {
            Log.i(TAG, "Cling sdk not ready, please try again");
        }
    }

    public void stopScan() {
        // TODO Auto-generated method stub
        if (mbClingSdkReady) {
            ClingSdk.stopScan();
        } else {
            Log.i(TAG, "Cling sdk not ready, please try again");
        }
    }

    /**
     *
     * @param userId
     * @param isActive true=激活；false=反激活
     * @param bleinfo
     */
    public void registerDevice(final int userId, boolean isActive, BluetoothDeviceInfo bleinfo) {
        ClingSdk.stopScan();
        if (isActive) {
//            ClingSdk.deregisterDevice(deregisterDeviceListener);
            ClingSdk.registerDevice(userId, bleinfo.getmBleDevice(), new OnBleListener.OnRegisterDeviceListener() {
                @Override
                public void onRegisterDeviceSucceed() {
                    Log.i(TAG, "onRegisterDeviceSucceed()");
                    mUid = userId;
                    mbClingRegistered = true;
//                    mCid = ClingSdk.getBondClingDeviceName();

                    if (mListener != null) {
                        mListener.registerResult(true, "激活成功", mCid);
                    }
                    Log.d(TAG, "loaddevInfo");
                    ClingSdk.loadDeviceInfo();
//                    updateScanText();
                }

                @Override
                public void onRegisterDeviceFailed(int i, String s) {
                    Log.i(TAG, "onRegisterDeviceFailed() :" + i + ", " + s);
                    if (mListener != null) {
                        mListener.registerResult(false, "激活失败", null);
                    }
                }
            });
        } else {
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
    }

    public void connectDevice(int bondUId, String clingId) {
        Log.i(TAG, "connectDeviceByClingid:uid=" + bondUId + ".cid=" + clingId);
        ClingSdk.connectDeviceByClingid(bondUId, clingId);
    }


    /**
     * 传入Application的context
     * @param context
     */
    public void initSDK(Context context) {
        mContext = context.getApplicationContext();

        //
        Intent intent = new Intent(context, SysNotificationListenerService.class);
        context.startService(intent);

        // this is test appkey and appsecret  HCa3f4b08b799e28af   7978e5e9477d07dd5d7dc79fd1bc00d7
        // user should request your own key on our web page ( http://developers.hicling.com )
        //注：如果激活失败，可能是Android9.0系统，例如华为mate20,对网络请求https的要求问题导致
        ClingSdk.init(mContext, "hcc22b8ea7054cae7", "ed21d715017a4400b1c5bb590b7c7ee8", new OnNetworkListener() {
            @Override
            public void onSucceeded(Object o, Object o1) {
                Log.d(TAG, "Init>>onSucceeded");
                mbClingSdkReady = true;
                ClingSdk.enableDebugMode(true);
                if(ClingSdk.isAccountBondWithCling()){
                    Log.i(TAG,"isAccountBondWithCling:"+ClingSdk.getBondClingDeviceName());
                }
                else {
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
                Log.d(TAG, "onFailed, statusCode is:" + i + ", s :" + s);
            }
        });

        ClingSdk.enableDebugMode(true);
        ClingSdk.start(mContext);
        ClingSdk.setBleDataListener(mBleDataListener);
        ClingSdk.setDeviceConnectListener(mDeviceConnectedListener);
//        ClingSdk.deregisterDevice(deregisterDeviceListener);
    }

    public void unInitSDK() {
        ClingSdk.stop(mContext);
        Intent intent = new Intent(mContext, SysNotificationListenerService.class);
        mContext.stopService(intent);
    }

    public void pause() {
        ClingSdk.onPause(mContext);
    }

    public void resume() {
        ClingSdk.setUserId(mUid);
        ClingSdk.onResume(mContext);
    }

    public void loadData() {
        ClingSdk.loadDeviceData();
    }

    public void getMinData(int day) {
        long timestamp = System.currentTimeMillis() / 1000;

        ArrayList<MinuteData> minuteDatas = ClingSdk.getMinuteData(timestamp - (3600 * 24 * day), timestamp);
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < minuteDatas.size(); i++) {
//            sb.append(minuteDatas.get(i));
//        }

        if (mListener != null) {
            mListener.receiveData(minuteDatas);
        }

    }

    ///----------Cling listener-----------///
    private OnBleListener.OnBleDataListener mBleDataListener = new OnBleListener.OnBleDataListener() {
        @Override
        public void onGotSosMessage() {
            Log.d(TAG, "received sos message");
            if (mListener != null) {
                mListener.logInfo("received sos message");
            }
            mbRevSos = true;
        }

        @Override
        public void onDataSyncingProgress(Object o) {
            Log.d(TAG, "onDataSyncingProgress " + o.toString());
            if (null != mListener) {
                mListener.syncProgress(1, 100);
            }
        }

        @Override
        public void onDataSyncedFromDevice() {
            Log.d(TAG, "data synced");
            if (mListener != null) {
                mListener.logInfo("data synced");
            }

            long timestamp = System.currentTimeMillis() / 1000;

            long startTime = timestamp - 7 * 24 * 3600;
            long endTime = timestamp;
            final TreeSet<DayTotalDataModel> dayTotal = ClingSdk.getDayTotalList(startTime, endTime);
            Log.d(TAG, "startTime : " + startTime + ", endTime : " + endTime + " , dayTotal :" + dayTotal);

            if (null != mListener) {
                mListener.dayTotal(dayTotal);
            }

            TreeSet<TimeActivityModel> sportBubble = ClingSdk.getSportBubble(timestamp - 3600 * 24 * 2, timestamp);
            Log.d(TAG, "sportBubble:" + sportBubble);
            if (sportBubble != null) {
                Iterator<TimeActivityModel> it = sportBubble.iterator();
                while (it.hasNext()) {
                    Log.d(TAG, it.next().toString());
                }
            }

            ArrayList<MinuteData> minuteDatas = ClingSdk.getMinuteData(timestamp - 3600 * 48, timestamp - 3600 * 24);

//            if (null != minuteDatas && minuteDatas.size() > 0) {
//                for (int i = 0; i < minuteDatas.size(); i++) {
//                    Log.d(TAG, minuteDatas.get(i) + "");
//                }
//            }

            ClingSdk.generateSportBubble(minuteDatas);
            ClingSdk.generateSleepBubble(minuteDatas);

            TreeSet<TimeActivityModel> bubbles = ClingSdk.getSportBubble(timestamp - 3600 * 48, timestamp - 3600 * 24);
            Log.d(TAG, "bubble : , sportBubble :" + bubbles);


            int sleepScore = ClingSdk.getSleepScore(minuteDatas, 0, 1, 26);
            Log.d(TAG, "sleep score :" + sleepScore);

            ArrayList<SleepCycle> sleepCycles = ClingSdk.getSleepCycle(minuteDatas, 26);
            Log.d(TAG, "sleepCycle : " + sleepCycles);

            long setStartTime = timestamp - 2 * 24 * 3600;
            TreeSet<DayTotalDataModel> dayTotalSet = ClingSdk.getDayTotalSet(setStartTime, timestamp);
            Log.d(TAG, "dayTotalSet : setStartTime : " + setStartTime + ", endTime : " + timestamp + " , dayTotal :" + dayTotalSet);

            float heartrateLevel = ClingSdk.getHeartratePointWithLevel(0, 0, 30, 13, 0, 0, 0, 0);
            Log.d(TAG, "heartrateLevel: " + heartrateLevel);
            float totalHealthScore = ClingSdk.getTotalsocreWithLevel(0, 13, 0, 0, 0, 0);
            Log.d(TAG, "totalHealthScore: " + totalHealthScore);
        }

        @Override
        public void onDataSyncingMinuteData(Object o) {
            if (o != null) {
                Log.d(TAG, "onDataSyncingMinuteData is: " + o.toString());  //MinuteData
            }
        }

        @Override
        public void onGetDayTotalData(DayTotalDataModel dayTotalDataModel) {
            Log.d(TAG, "dayTotalDataModel : " + dayTotalDataModel.toString());
//            mListener.dayTotal();
        }
    };

    private OnBleListener.OnDeviceConnectedListener mDeviceConnectedListener = new OnBleListener.OnDeviceConnectedListener() {
        @Override
        public void onDeviceConnected() {
            Log.d(TAG, "onDeviceConnected()");
            if (mListener != null) {
                mListener.logInfo("Device Connected");
            }
            mbDeviceConnected = true;
            //马上加载设备信息
            ClingSdk.loadDeviceInfo();
//            updateScanText();
            if (mListener != null) {
                mListener.connectStateChanged(1);
            }
        }

        @Override
        public void onDeviceDisconnected() {
            Log.d(TAG, "onDeviceDisconnected()");
            if (mListener != null) {
                mListener.logInfo("device is disconnected");
            }
            mbDeviceConnected = false;
            if (mListener != null) {
                mListener.connectStateChanged(0);
            }
        }

        @Override
        public void onDeviceInfoReceived(Object o) {
            Log.d(TAG, "onDeviceInfoReceived: o :");
            if (o != null) {
//                updateScanText();
//                if (mListener != null) {
//                    mListener.connectStateChanged(1);
//                }

                mDeviceInfo = (PERIPHERAL_DEVICE_INFO_CONTEXT) o;
                mCid = mDeviceInfo.clingId;
                if (mListener != null) {
                    mListener.deviceInfo(mCid);
                }
                Log.d(TAG, "deviceInfo : " + mDeviceInfo.toString());
                Log.d(TAG, "onDeviceInfoReceived: " + mDeviceInfo.softwareVersion);

                if (mbClingRegistered || ClingSdk.getIsDeviceRegistered(mDeviceInfo, mUid)) {
                    Log.d(TAG, "onDeviceInfoReceived: setconfig");
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

    private OnBleListener.OnDeregisterDeviceListener deregisterDeviceListener = new OnBleListener.OnDeregisterDeviceListener() {
        @Override
        public void onDeregisterDeviceSucceed() {
            Log.d(TAG, "onDeregisterDeviceSucceed()");
            if (mListener != null) {
                mListener.logInfo("Deregister Device Succeed");
            }
//            updateScanText();
            if (mListener != null) {
                mListener.registerResult(false, "设备反激活成功", null);
            }
            mbClingRegistered = false;
            ClingSdk.clearDatabase();
//            Main3Activity.this.finish();
        }

        @Override
        public void onDeregisterDeviceFailed(int i, String s) {
            Log.d(TAG, "onDeregisterDeviceFailed(): " + i + ", " + s);
            if (mListener != null) {
                mListener.registerResult(false, "设备反激活失败", null);
            }
        }
    };

    ///--------------singleton--------------

    //将构造函数私有化
    private BSClingManager() {
    }

    //创建私有实例对象
    private static final BSClingManager s_shared = new BSClingManager();
    //对外提供方法，返回实例对象
    public static BSClingManager sharedInstance() {
        return s_shared;
    }

}
