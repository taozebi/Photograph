package com.ewide.photograph.common.log;

/**
 * @Description:主要功能:
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil.klog.base
 * @author: AbrahamCaiJin
 * @date: 2017年05月16日 16:55
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */

import android.util.Log;


/**
 * Created by zhaokaiqiang on 15/11/18.
 */
public class BaseLog {

    private static final int MAX_LENGTH = 4000;

    public static void printDefault(int type, String tag, String msg) {

        int index = 0;
        int length = msg.length();
        int countOfSub = length / MAX_LENGTH;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + MAX_LENGTH);
                printSub(type, tag, sub);
                index += MAX_LENGTH;
            }
            printSub(type, tag, msg.substring(index, length));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case LogUtils.V:
                Log.v(tag, sub);
                break;
            case LogUtils.D:
                Log.d(tag, sub);
                break;
            case LogUtils.I:
                Log.i(tag, sub);
                break;
            case LogUtils.W:
                Log.w(tag, sub);
                break;
            case LogUtils.E:
                Log.e(tag, sub);
//                writeFileData(sub);
                break;
            case LogUtils.A:
                Log.wtf(tag, sub);
                break;
        }
    }

}
