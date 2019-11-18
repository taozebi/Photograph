package com.ewide.photograph.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * 可以图片文字居中的RadioButton
 *
 * 新的view要设置android:button="@null",然后设置一个android:drawableStart="@drawable/radiobutton_selector"
 * Created by Taoze on 2018/7/27.
 */

public class CenterRadioButton extends AppCompatRadioButton {
    private static final String TAG = CenterRadioButton.class.getSimpleName();

    public CenterRadioButton(Context context) {
        super(context);
    }

    public CenterRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = drawables[0];
        int gravity = getGravity();
        int left = 0;
        if (gravity == Gravity.CENTER) {
            left = ((int) (getWidth() - drawable.getIntrinsicWidth() - getPaint().measureText(getText().toString()))
                    / 2);
        }
        drawable.setBounds(left-5, 0, left + drawable.getIntrinsicWidth()-5, drawable.getIntrinsicHeight());
    }
}

