package com.ewide.photograph.common.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;
import java.util.Map;

/**
 * 支持多行多选的View
 * Created by Taoze on 2018/8/2.
 */

public class MultipleChoiceWidget implements View.OnClickListener {

    private Context mContext;

    private List<Map<String,Object>> mDatas;
    private LayoutInflater mInflater;

    public MultipleChoiceWidget(Context mContext, List<Map<String, Object>> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void onClick(View v) {

    }

    interface MultipleChoiceListener{
        void onCancal();
        void onConfirm(List<Map<String,Object>> mapList);
    }
}
