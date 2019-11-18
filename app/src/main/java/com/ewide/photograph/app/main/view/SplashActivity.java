package com.ewide.photograph.app.main.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 闪屏
 * Created by Taoze on 2018/4/9.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.getInt("flag") == 1) {
            String token = bundle.getString("token");
        }


        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
