package kr.mash_up;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final double FAKE_LOCATION_LAT = 37.5036826;
    private static final double FAKE_LOCATION_LNG = 127.042665;

    private static final String API_URL = "http://52.78.84.8:5000/dust";

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
                loadWeatherInfo();
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
        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = HttpUrl.parse(API_URL).newBuilder()
                .addQueryParameter("lat", String.valueOf(lat))
                .addQueryParameter("lng", String.valueOf(lng))
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Log.i("TAG", "fail : " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("TAG", "body : " + response.body().string());
                    }
                }
        );

    }

    private double getCurrentLat() {
        return FAKE_LOCATION_LAT;
    }

    private double getCurrentLng() {
        return FAKE_LOCATION_LNG;
    }

}
