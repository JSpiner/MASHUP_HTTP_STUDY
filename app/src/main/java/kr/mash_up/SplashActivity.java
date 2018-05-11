package kr.mash_up;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private static final int ACTIVITY_START_DELAY_MILLIS = 1000 * 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        initStatusBar();
        startMainActivityWithDelay();
    }

    private void initStatusBar() {
        setWindowLightStatusBarEnable();
        setStatusBarColor(Color.WHITE);
    }

    protected void setStatusBarColor(@ColorInt int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
    }

    protected void setWindowLightStatusBarEnable() {
        View view = findViewById(R.id.root);

        int flags = view.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
    }

    private void startMainActivityWithDelay() {
        Runnable startActivityRunnable = new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        };

        handler.postDelayed(
                startActivityRunnable,
                ACTIVITY_START_DELAY_MILLIS
        );
    }

    private void startMainActivity() {

    }
}
