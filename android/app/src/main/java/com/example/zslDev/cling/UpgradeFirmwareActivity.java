package com.example.zslDev.cling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hicling.clingsdk.ClingSdk;
import com.hicling.clingsdk.devicemodel.PERIPHERAL_DEVICE_INFO_CONTEXT;
import com.hicling.clingsdk.listener.OnBleListener;
import com.hicling.clingsdk.listener.OnNetworkListener;
import com.hicling.clingsdk.model.ClingFirmwareInfoModel;
import com.example.zslDev.R;
import java.util.Locale;



/**
 * Created by Elisa on 17/10/10.
 */

public class UpgradeFirmwareActivity extends Activity {
    private final static String TAG = UpgradeFirmwareActivity.class.getSimpleName();

    private String mstrClingid = null;
    private String mstrVersion = null;

    private TextView txtvTitle;
    private ProgressBar pbar;
    private TextView txtvProgress;
    private static double mdUpgradingProgress;
    private final static int msg_Upgrading_Progresss = 1000;
    public final static String BUNDLE_DEVICE_INFO_VERSION = "Main3Activity.BUNDLE_DEVICE_INFO_VERSION";
    public final static String BUNDLE_DEVICE_INFO_CLINGID = "Main3Activity.BUNDLE_DEVICE_INFO_CLINGID";
    //   Attention Please: This Activity is just for Demo, the Firmware bin file is on HiCling Server
    //   Actually Customized device should have its Firmware bin file on its own Server not HiCling Server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upgrade);

        ClingSdk.setUserId(5115557);

        Intent intent = getIntent();
        if (intent != null && (getIntent().getExtras()) != null) {
            mstrClingid = intent.getStringExtra(BUNDLE_DEVICE_INFO_CLINGID);
            mstrVersion = intent.getStringExtra(BUNDLE_DEVICE_INFO_VERSION);
        }

        Log.i(TAG, "mstrClingid is: " + mstrClingid + " ,mstrVersion is: " + mstrVersion);

        Button btnRequestFirmware = (Button) findViewById(R.id.request_firmware_btn);
        btnRequestFirmware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, "userid : " + ClingData.getInstance().getUserId());
                getFirmwareVersion();
            }
        });


        Button btnUpgrade = (Button) findViewById(R.id.firmware_upgrading_btn);
        btnUpgrade.setEnabled(false);
        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, "userid : " + ClingData.getInstance().getUserId());

                ClingSdk.upgradeFirmware(new OnBleListener.OnUpgradeFirmwareListener() {
                    @Override
                    public void onFirmwareSpaceNotEnough() {
                        Log.i(TAG, "onFirmwareSpaceNotEnough");
                    }

                    @Override
                    public void onFirmwareDownloadFailed(int i, String s) {
                        Log.i(TAG, "onFirmwareDownloadFailed: " + i + ", " + s);
                    }

                    @Override
                    public void onFirmwareDownloadProgress(Object o) {
//                        Log.i(TAG, "onFirmwareDownloadProgress " + o.toString());
                    }

                    @Override
                    public void onFirmwareDownloadSucceeded() {
                        Log.i(TAG, "onFirmwareDownloadSucceeded ");
                    }

                    @Override
                    public void onFirmwareUpgradeProgress(Object o) {
                        Log.i(TAG, "onFirmwareUpgradeProgress " + o);
                        if (o != null) {
                            mdUpgradingProgress = Double.parseDouble(o.toString());
                            mMsgHandler.sendMessage(mMsgHandler.obtainMessage(msg_Upgrading_Progresss));
                        }
                    }

                    @Override
                    public void onFirmwareUpgradeSucceeded() {
                        Log.i(TAG, "onFirmwareUpgradeSucceeded");
                    }

                    @Override
                    public void onFirmwareUpgradeFailed(int i, String s) {
                        Log.i(TAG, "onFirmwareUpgradeFailed: " + i + " , " + s);
                        showToast("upgrading failed " + s);
                    }
                });
            }
        });

        txtvProgress = (TextView) findViewById(R.id.firmware_progress_txt);
        txtvTitle = (TextView) findViewById(R.id.firmware_indicator_txt);
        pbar = (ProgressBar) findViewById(R.id.firmware_progress_bar);
        pbar.setMax(100);

    }


    private void getFirmwareVersion() {
        PERIPHERAL_DEVICE_INFO_CONTEXT pdic = new PERIPHERAL_DEVICE_INFO_CONTEXT();
        pdic.clingId = mstrClingid;
        pdic.softwareVersion = mstrVersion;
        Log.i(TAG, "firmware version is: " + mstrVersion);
        ClingSdk.requestFirmwareUpgradeInfo(pdic, new OnNetworkListener() {
            @Override
            public void onSucceeded(final Object o, final Object o1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (o != null) {
//                            boolean bRel = (Boolean) o;
                            Button btnUpgrade = (Button) findViewById(R.id.firmware_upgrading_btn);
                            btnUpgrade.setEnabled(true);
//                            if (bRel) {
//                                Log.i(TAG, "there is new firmware");
//                                btnUpgrade.setEnabled(true);
//                            } else {
//                                btnUpgrade.setEnabled(false);
//                                Log.i(TAG, "now is new firmware");
//                                showToast("now is new firmware");
//                            }

                            if (o1 != null) {
                                ClingFirmwareInfoModel firmwareInfo = (ClingFirmwareInfoModel) o1;
                                Log.i(TAG, "firmwareInfo is: " + firmwareInfo.toString());
                            }
                        }

                    }
                });

            }

            @Override
            public void onFailed(int i, String s) {
                Log.i(TAG, "onFailed, statusCode is:" + i + ", msg is: " + s);
            }
        });
    }


    private Handler mMsgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

//			Log.i(TAG, "got handler msg: " + msg.what);
            switch (msg.what) {
                case msg_Upgrading_Progresss:
                    showFirmwareUpgradingProgress(mdUpgradingProgress);
                    break;
            }
        }
    };


    private void showFirmwareUpgradingProgress(double progress) {
        txtvTitle.setText("Upgrading");
        txtvProgress.setText(String.format(Locale.US, "%.1f%%", progress * 100));
        pbar.setProgress((int) (progress * 100));
    }


    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UpgradeFirmwareActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
