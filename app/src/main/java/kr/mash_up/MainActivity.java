package kr.mash_up;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final double FAKE_LOCATION_LAT = 37.5036826;
    private static final double FAKE_LOCATION_LNG = 127.042665;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initViews();

        loadWeatherInfo();
    }

    private void initViews() {
        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void loadWeatherInfo() {
        try {
            requestWeatherInfo(
                    getCurrentLat(),
                    getCurrentLng()
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void requestWeatherInfo(double lat, double lng) throws Exception {

    }

    private double getCurrentLat() {
        return FAKE_LOCATION_LAT;
    }

    private double getCurrentLng() {
        return FAKE_LOCATION_LNG;
    }

}
