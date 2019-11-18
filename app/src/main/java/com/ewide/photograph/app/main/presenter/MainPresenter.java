package com.ewide.photograph.app.main.presenter;


import android.content.Context;

import com.ewide.photograph.app.main.model.MainCallback;
import com.ewide.photograph.app.main.model.MainModel;
import com.ewide.photograph.common.base.BasePresenter;

/**
 * author：Taozebi
 * date：2018/11/16 15:09
 * describe：用户登录表示层
 */

public class MainPresenter extends BasePresenter {
    private MainModel mainModel;

    public MainPresenter(Context context) {
        super(context);
        mainModel = new MainModel(context);
    }

    public void getProjectListData(MainCallback callback){
        mainModel.getProjectListData(callback);
    }

}
