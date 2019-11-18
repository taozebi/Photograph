package com.ewide.photograph.common.bluetooth;

/**
 * 蓝牙连接状态监听
 * Created by Taoze on 2018/6/23.
 */

public interface BluetoothConnectChangeListener {

    /*正在连接*/
    public void onConnecting();

    /**
     * 连接成功
     * @param deviceName 设备名称
     * @param deviceAddress 设备地址
     */
    public void onSuccess(String deviceName, String deviceAddress);

    /*连接失败*/
    public void onFailed();
}
