package com.ewide.photograph.common.base;

public abstract class BaseModelCallBack<T> {

    protected abstract void onSuccess(T o);

    protected abstract void onFailure(int errorCode, String msg);
}
