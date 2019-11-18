package com.ewide.photograph.common.base;

import android.content.Context;

/**
 * Presenter基类
 * Created by Taoze on 2018/9/12.
 */

public abstract class BasePresenter {
    protected Context mContext;

    public BasePresenter(Context context) {
        this.mContext = context;
    }
}
