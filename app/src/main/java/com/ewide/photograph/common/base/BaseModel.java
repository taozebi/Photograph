package com.ewide.photograph.common.base;

import android.content.Context;

public abstract class BaseModel {

    protected int timeout = 60 * 1000 * 10;
    protected long sendTime = 0;
    protected long useTime = 0;
    private String mMsgCode;
    protected Context mContext;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public BaseModel(Context context) {
        this.mContext = context;
    }


}
