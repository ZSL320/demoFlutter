package com.example.zslDev.cling;

import com.hicling.clingsdk.bleservice.BluetoothDeviceInfo;
import com.hicling.clingsdk.model.DayTotalDataModel;
import com.hicling.clingsdk.model.MinuteData;

import java.util.ArrayList;
import java.util.TreeSet;

public interface BSClingManagerListener {

    //激活设备结果
    void registerResult(boolean result, String msg, String clingId);
    //连接状态变化
    void connectStateChanged(int state);

    //获取最近几天的数据的回调
    void receiveData(ArrayList<MinuteData> minuteData);

    //连接后汇总的数据, 调用loadData获得？
    void dayTotal(TreeSet<DayTotalDataModel> dayData);

    //调用loadData接口产生的同步进度
    void syncProgress(long current, long total);

    //完成数据同步
    void syncFinished();

    void logInfo(String log);

    void scanResult(ArrayList<BluetoothDeviceInfo> arrayList);

    void deviceInfo(String cid);
}
