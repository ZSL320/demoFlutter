package com.example.zslDev.cling;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hicling.clingsdk.ClingSdk;
import com.hicling.clingsdk.devicemodel.PERIPHERAL_USER_PROFILE;
import com.hicling.clingsdk.devicemodel.PERIPHERAL_USER_REMINDER_CONTEXT;
import com.hicling.clingsdk.devicemodel.PERIPHERAL_WEATHER_DATA;
import com.hicling.clingsdk.devicemodel.PERIPHERAL_WEATHER_TYPE;
import com.hicling.clingsdk.listener.OnBleListener;
import com.hicling.clingsdk.model.DayTotalDataModel;
import com.hicling.clingsdk.model.DeviceConfiguration;
import com.hicling.clingsdk.model.DeviceNotification;

import java.util.ArrayList;
import java.util.Calendar;
import com.example.zslDev.R;
/**
 * Created by Elisa on 17/9/29.
 */

public class BleActivity extends Activity {
    private final static String TAG = BleActivity.class.getSimpleName();

//    private static boolean mbRevSos = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_config);

        ClingSdk.setBleDataListener(mBleDataListener);

        Button btnConfigUserP = (Button) findViewById(R.id.device_userprofile_btn);
        btnConfigUserP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PERIPHERAL_USER_PROFILE pup = new PERIPHERAL_USER_PROFILE();
                pup.screen_display_option = pup.PERIPHERAL_PROFILE_SCREEN_DISPLAY_OPTION_STEP | pup.PERIPHERAL_PROFILE_SCREEN_DISPLAY_OPTION_DISTANCE |
                        pup.PERIPHERAL_PROFILE_SCREEN_DISPLAY_OPTION_CALORIES;
                pup.training_display_option = pup.PERIPHERAL_PROFILE_TRAINING_DISPLAY_OPTION_CADENCE | pup.PERIPHERAL_PROFILE_TRAINING_DISPLAY_OPTION_CALORIES;
                pup.nickname_len = 11;
                pup.nickname = "Cling Cling";
                pup.caloryDisplayType = pup.CALORIES_DISPLAY_TYPE_ACTIVE;
                ClingSdk.updateDeviceUserCfg(pup);
                showToast("config done, see result on Cling device");

                //test
//                ClingSdk.stopScan();
//                ClingSdk.startScan(10 * 60 * 1000, new OnBleListener.OnScanDeviceListener() { // 10 minutes
//                    @Override
//                    public void onBleScanUpdated(Object o) {
//                        //蓝牙连接成功后，不会再扫描
//                        Log.i(TAG, "onBleScanUpdated()");
//                        if (o != null) {
//                            ArrayList<BluetoothDeviceInfo> arrayList = (ArrayList<BluetoothDeviceInfo>) o;
//                            Log.i(TAG, "arrayList : " + arrayList);
//                        }
//                    }
//                });
            }
        });

        Button btnConfigDev = (Button) findViewById(R.id.device_configuration_btn);
        btnConfigDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceConfiguration devCfg = new DeviceConfiguration();
                devCfg.bActFlipWristEn = 0;
                devCfg.bActHoldEn = 1;
                devCfg.bNavShakeWrist = 1;
                devCfg.hrDayInterval = 15;
                devCfg.bIdleAlertEn = 1;
                devCfg.idleAlertInterval = 30;
                devCfg.idleAlertHourStart = 13;
                devCfg.idleAlertHourEnd = 15;
                devCfg.nHrBroadcast = 1;
                ClingSdk.setPerpheralConfiguration(devCfg);

                showToast("config done, see result on Cling device");

            }
        });

        Button btnSmartNoti = (Button) findViewById(R.id.Smart_notification_btn);
        btnSmartNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceNotification devNotification = new DeviceNotification();
                devNotification.incomingcall = 1;
                devNotification.missedcall = 1;
                devNotification.social = 1;
                ClingSdk.setPeripheralNotification(devNotification);
                showToast("notification done");
            }
        });

        Button btnReminder = (Button) findViewById(R.id.Reminder_btn);
        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PERIPHERAL_USER_REMINDER_CONTEXT reminder = new PERIPHERAL_USER_REMINDER_CONTEXT();
                reminder.hour = 19;
                reminder.minute = 46;
                reminder.name = "meeting";
                reminder.isEnable = true;
                reminder.bRepeatDaily = false;

                int weekday = 0;
//                weekday |= 1 << 0;//beep and repeat in monday
                weekday |= 1 << 1;//beep and repeat in tuesday
                weekday |= 1 << 2;//beep and repeat in wednesday
//                weekday |= 1 << 3;//beep and repeat in thursday
                weekday |= 1 << 4;//beep and repeat in friday
//                weekday |= 1 << 5;//beep and repeat in saturday
//                weekday |= 1 << 6;//beep and repeat in sunday
                reminder.weekday = weekday;    //monday：0，tuesday：1，wednesday：2，thursday，3，friday：,4，saturday：5,sunday：6
                Log.i(TAG, "add reminder: " + reminder.toString());
                ClingSdk.addPerpheralReminderInfo(reminder);
                showToast("reminder done, see result on Cling device");
            }
        });

        Button btnWeather = (Button) findViewById(R.id.Weather_btn);
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PERIPHERAL_WEATHER_DATA> arrlistWeather = new ArrayList<PERIPHERAL_WEATHER_DATA>();
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                PERIPHERAL_WEATHER_DATA weather = new PERIPHERAL_WEATHER_DATA();
                weather.day = day;
                weather.month = month + 1;
                weather.temperature_high = 30;
                weather.temperature_low = 22;
                weather.type = PERIPHERAL_WEATHER_TYPE.PERIPHERAL_WEATHER_RAINY;
//                weather.aqi = 47;
//                 some devices require aqi data, others don't,
//                 if aqi is required, device shows the avg temperature, otherwise shows high and low temp
                arrlistWeather.add(weather);
                // more weather forecast...
                // weather = new PERIPHERAL_WEATHER_DATA ();...
                // ...
                // arrlistWeather.add(weather);
                ClingSdk.setPeripheralWeatherInfo(arrlistWeather);
                showToast("weather done, see result on Cling device");
            }
        });

        Button btnLanguage = (Button) findViewById(R.id.Language_btn);
        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // note this configuration does not work on Cling Watch devices.
                // Now only Cling Band devices support language configuration.
                ClingSdk.setPeripheralLanguage(ClingSdk.CLING_DEVICE_LANGUAGE_TYPE_EN);
                showToast("Language changed, see result on Cling band device");
            }
        });

        Button btnSosMsg = (Button) findViewById(R.id.sosMsg_btn);
        if (ClingSdk.getClingDeviceType() == ClingSdk.CLING_DEVICE_TYPE_BAND_1  //UV
                || ClingSdk.getClingDeviceType() == ClingSdk.CLING_DEVICE_TYPE_BAND_2   //Nfc
                || ClingSdk.getClingDeviceType() == ClingSdk.CLING_DEVICE_TYPE_BAND_3   //voc
        ) {
            btnSosMsg.setVisibility(View.VISIBLE);
        } else {
            btnSosMsg.setVisibility(View.GONE);
        }
    }


    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BleActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private OnBleListener.OnBleDataListener mBleDataListener = new OnBleListener.OnBleDataListener() {
        @Override
        public void onGotSosMessage() {
            Log.i(TAG, "received sos message");
            showToast("received sos message");
            Button btnSosMsg = (Button) findViewById(R.id.sosMsg_btn);
            btnSosMsg.setText("Receive SOS MSG");

        }

        @Override
        public void onDataSyncingProgress(Object o) {
            Log.i(TAG, "onDataSyncingProgress " + o.toString());
        }

        @Override
        public void onDataSyncedFromDevice() {
            Log.i(TAG, "data synced");
            showToast("data synced");
        }

        @Override
        public void onDataSyncingMinuteData(Object o) {
            if (o != null) {
                Log.i(TAG, "onDataSyncingMinuteData is: " + o.toString());  //MinuteData
            }
        }

        @Override
        public void onGetDayTotalData(DayTotalDataModel dayTotalDataModel) {

        }
    };


}
