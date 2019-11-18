package com.ewide.photograph.app.global;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.ewide.photograph.R;
import com.ewide.photograph.common.base.BaseApplication;
import com.ewide.photograph.common.widget.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * author：Taozebi
 * date：2018/11/16 16:37
 * describe：App主程序
 */

public class MRApplication extends BaseApplication {

    public static final String TAG = "zebit";
    public static final String TAG_HTTP = "tag_http";
    private static final boolean DEBUG = false;


    private static MRApplication instance;


    private List<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        activities.add(activity);
    }


    public void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public Activity getTopActivity() {
        return activities.get(activities.size() - 1);
    }

    public static MRApplication getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSomething();
        instance = this;
    }

    private void initSomething() {
        intDatabasePath();
    }


    private void intDatabasePath() {
        Constants.MOBILEDBPATH = "/data/data/" + getPackageName() + "/databases/";
        Log.d(MRApplication.TAG, "init intDatabasePath : " + Constants.MOBILEDBPATH);
    }


    public void exit() {
        // 销毁Activity
        for (Activity act : activities) {
            act.finish();
        }
        System.exit(0);
    }


    public boolean toExit(final Activity activity, boolean showDialog) {
        if (showDialog) {
            new AlertDialog(activity)
                    .builder()
                    .setTitle("退出程序")
                    .setMsg("退出程序将无法查看应用相关信息，确定退出？")
                    .setPositiveButton("退出", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            exit();
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
        } else {
            exit();
        }
        return true;
    }

    public boolean toRestart(final Activity activity) {
        new AlertDialog(activity)
                .builder()
                .setTitle(getString(R.string.tip))
                .setMsg(getString(R.string.tip_need_restart))
                .setPositiveButton(getString(R.string.confirm), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 销毁Activity
                        for (Activity act : activities) {
                            act.finish();
                        }
                    }
                })
                .show();
        return true;
    }

    /**
     * 弹出系统对话框
     *
     * @param activity
     * @param title           标题
     * @param message         内容
     * @param confirm         确定
     * @param confirmLintener
     */
    public void showDialog(final Activity activity, String title, String message, String confirm, View.OnClickListener confirmLintener) {
        new AlertDialog(activity)
                .builder()
                .setCancelable(false)
                .setTitle(title)
                .setMsg(message)
                .setPositiveButton(confirm, confirmLintener)
                .show();
    }

    /**
     * 弹出系统对话框
     *
     * @param activity
     * @param title           标题
     * @param message         内容
     * @param confirm         确定
     * @param cancel          取消
     * @param confirmLintener
     * @param cancalListener
     */
    public void showDialog(final Activity activity, String title, String message, String confirm, String cancel, View.OnClickListener confirmLintener, View.OnClickListener cancalListener) {
        new AlertDialog(activity)
                .builder()
                .setCancelable(false)
                .setTitle(title)
                .setMsg(message)
                .setPositiveButton(confirm, confirmLintener)
                .setNegativeButton(cancel, cancalListener)
                .show();
    }

}
