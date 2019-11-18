package com.ewide.photograph.common.bluetooth;

/*
* 蓝牙回调
*  Created by Taoze on 2018/6/23.
*/
public interface BlueToothCallBackListener {

	void onFinish(String respose);

	void onError(Exception e);
}
