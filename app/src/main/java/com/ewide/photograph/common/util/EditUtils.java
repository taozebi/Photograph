package com.ewide.photograph.common.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * author：xiongwei
 * date： 2019-07-05 14:38
 * describe：
 */
public class EditUtils {

    // editText监听输入小数位数
    public static void setPoint(final EditText editText, final int num) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //控制两位小数“num”即为要控制的位数
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > num) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (num + 1));
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }

                }
                //限制只能输入一次小数点

                if (editText.getText().toString().indexOf(".") >= 0) {
                    if (editText.getText().toString().indexOf(".", editText.getText().toString().indexOf(".") + 1) > 0) {
                        editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                        editText.setSelection(editText.getText().toString().length());
                    }

                }

                //第一次输入为点的时候

                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);

                }

                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


        });
    }
}
