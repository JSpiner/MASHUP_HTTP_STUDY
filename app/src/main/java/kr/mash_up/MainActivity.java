package kr.mash_up;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    protected void setStatusBarColor(@ColorRes int colorResId) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(
                ContextCompat.getColor(
                        getBaseContext(),
                        colorResId
                )
        );
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
            setErrorState();
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
                        onRequestFailure(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("TAG", "status : " + response.code());
                        switch (response.code()) {
                            case 200:
                                onRequestSuccess(response);
                                break;
                            case 500:
                                onRequestFailure(new RuntimeException("Network 에러"));
                                break;
                            default:
                                onRequestFailure(new RuntimeException("Network 에러"));
                        }
                    }
                }
        );

    }

    private void onRequestFailure(Exception error) {
        error.printStackTrace();
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        setErrorState();
                    }
                }
        );
    }

    private void onRequestSuccess(Response response) throws IOException {
        String rawText = response.body().string();
        Log.i("TAG", "treXt : " + rawText);
        WeatherInfo weatherInfo = new Gson().fromJson(
                rawText,
                WeatherInfo.class
        );

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onWeatherInfoResponse(weatherInfo);
            }
        });
    }

    private void onWeatherInfoResponse(WeatherInfo weatherInfo) {
        double dust = weatherInfo.weather.dust;

        if (dust >= 60) {
            setBadWeatherState(dust);
        }
        else if (dust >= 30) {
            setSoSoWeatherState(dust);
        }
        else {
            setNormalWeatherState(dust);
        }
    }

    private double getCurrentLat() {
        return FAKE_LOCATION_LAT;
    }

    private double getCurrentLng() {
        return FAKE_LOCATION_LNG;
    }

    private void setBadWeatherState(double dust) {
        setRootBackgroundColor(R.color.colorBad);
        setStatusBarColor(R.color.colorBadDark);
        setDustDisplayText("미세먼지 최악");
        setStatusFaceImage(R.drawable.face_angry);
        setDustValue(dust);
        setLastUpdateDate();
    }

    private void setSoSoWeatherState(double dust) {
        setRootBackgroundColor(R.color.colorSoSo);
        setStatusBarColor(R.color.colorSoSoDark);
        setDustDisplayText("미세먼지 나쁨");
        setStatusFaceImage(R.drawable.face_soso);
        setDustValue(dust);
        setLastUpdateDate();
    }

    private void setNormalWeatherState(double dust) {
        setRootBackgroundColor(R.color.colorNormal);
        setStatusBarColor(R.color.colorNormalDark);
        setDustDisplayText("미세먼지 보통");
        setStatusFaceImage(R.drawable.face_happy);
        setDustValue(dust);
        setLastUpdateDate();
    }

    private void setErrorState() {
        setRootBackgroundColor(R.color.colorError);
        setStatusBarColor(R.color.colorErrorDark);
        setDustDisplayText("통신 에러");
        setStatusFaceImage(R.drawable.face_down);
        setDustValue(0);
        setLastUpdateDate();
    }

    private void setRootBackgroundColor(@ColorRes int colorResId) {
        findViewById(R.id.root).setBackgroundColor(
                ContextCompat.getColor(
                        getBaseContext(),
                        colorResId
                )
        );
    }

    private void setDustDisplayText(String text) {
        ((TextView) findViewById(R.id.dust_display)).setText(text);
    }

    private void setStatusFaceImage(@DrawableRes int drawableResId) {
        ((ImageView) findViewById(R.id.dust_display_image)).setImageResource(drawableResId);
    }

    private void setDustValue(double dust) {
        ((TextView) findViewById(R.id.dust)).setText(String.valueOf(dust));
    }

    private void setLastUpdateDate() {
        Date nowDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("마지막 업데이트 : yyyy-MM-dd HH:mm:ss");
        ((TextView) findViewById(R.id.date)).setText(
                format.format(nowDate)
        );
    }


}
