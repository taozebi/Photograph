package com.ewide.photograph.common.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Taoze on 2018/6/8.
 */

public class MenuButton extends android.support.v7.widget.AppCompatRadioButton{
    private Class clasz;
    public MenuButton(Context context) {
        super(context);
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Class getClasz() {
        return clasz;
    }

    public void setClasz(Class clasz) {
         this.clasz = clasz;
    }
}
