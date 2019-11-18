package com.ewide.photograph.common.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 在ScrollView中可以使ScrollView滑动的ListView
 * Created by Taoze on 2018/4/23.
 */

public class TListView extends ListView{
    public TListView(Context context) {
        super(context);
    }

    public TListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设计一个较大的值和AT_MOST模式
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);//再调用原方法测量
    }
}
