package kr.mash_up;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
        startMainActivityWithDelay();
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}
