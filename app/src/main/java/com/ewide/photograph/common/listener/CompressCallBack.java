package com.ewide.photograph.common.listener;

import java.io.File;

/**
 * author：ZhouJian
 * date：2018/10/17 16:25
 * describe：文件压缩的回调
 */
public interface CompressCallBack {
    //开始压缩
    void comPressStart();
   //压缩成功
    void comPressSuccess(File file);
    //压缩失败
    void comPressFiled(Throwable e);
}
