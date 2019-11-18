package com.ewide.photograph.app.global;

import com.ewide.photograph.common.util.ThreadPoolUtils;

import java.util.List;

/**
 * author：Taozebi
 * date：2018/12/12 16:59
 * describe：线程池管理
 */

public class ThreadManager {

    private static ThreadManager instance;
    private ThreadPoolUtils threadPoolUtils;

    private ThreadManager() {
        threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.FixedThread,5);
    }

    /**
     * 获取线程管理实例
     * @return ThreadManager instance
     */
    public static ThreadManager getInstance(){
        if(instance == null){
            instance = new ThreadManager();
        }
        return instance;
    }

    public void execute(Runnable runnable){
        threadPoolUtils.execute(runnable);
    }

    public void execute(List<Runnable> runnableList){
        threadPoolUtils.execute(runnableList);
    }
}
