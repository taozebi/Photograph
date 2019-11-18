package com.ewide.photograph.app.global;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * author：Taozebi
 * date：2018/11/16 16:43
 * describe：MeterRead service
 */

public class MRService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
