package com.onewind.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.onewind.android.gson.Forecast;
import com.onewind.android.gson.LifeStyle;
import com.onewind.android.gson.Weather;
import com.onewind.android.util.HttpUtil;
import com.onewind.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView flText;

    private TextView tmpText;

    private TextView condText;

    private ImageView condImageView;

    private TextView windDirText;

    private TextView windScText;

    private TextView windSpdText;

    private LinearLayout forecastLayout;

    private LinearLayout lifeStyleLayout;

    private ImageView bingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        flText = (TextView) findViewById(R.id.fl_text);
        tmpText = (TextView) findViewById(R.id.tmp_text);
        condText = (TextView) findViewById(R.id.cond_text);
        condImageView = (ImageView) findViewById(R.id.cond_image_view);
        windDirText = (TextView) findViewById(R.id.wind_dir_text);
        windScText = (TextView) findViewById(R.id.wind_sc_text);
        windSpdText = (TextView) findViewById(R.id.wind_spd_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        lifeStyleLayout = (LinearLayout) findViewById(R.id.lifestyle_layout);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if(weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        String bingPic = prefs.getString("bing_pic", null);
        if(bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {

        String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + weatherId +
                "&key=be9a9a4db2a44ac38a9d57c370d5d433";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                   @Override
                    public void run() {
                       Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                               Toast.LENGTH_SHORT).show();
                   }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                   @Override
                    public void run() {
                       if(weather != null && "ok".equals(weather.status)) {
                           SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                   WeatherActivity.this
                           ).edit();
                           editor.putString("weather", responseText);
                           editor.apply();
                           showWeatherInfo(weather);
                       } else {
                           Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT)
                                   .show();
                       }
                   }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 处理并展示Weather实体类的数据
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.update.updateTime;
        String fl = weather.now.fl;
        String tmp = weather.now.tmp;
        String condCode = weather.now.condCode;
        String imageCode = "https://cdn.heweather.com/cond_icon/" + condCode + ".png";
        String condTxt = weather.now.condTxt;
        String windDir = weather.now.windDir;
        String windSc = weather.now.windSc;
        String windSpd = weather.now.windSpd;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        flText.setText(fl);
        tmpText.setText(tmp);
        condText.setText(condTxt);
        Glide.with(this).load(imageCode).into(condImageView);
        windDirText.setText(windDir);
        windScText.setText(windSc);
        windSpdText.setText(windSpd);
        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                    forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            TextView condDText = (TextView) view.findViewById(R.id.cond_text);
            ImageView condImageView = (ImageView) view.findViewById(R.id.cond_image_view);
            TextView windDirText = (TextView) view.findViewById(R.id.wind_dir_text);
            TextView windScText = (TextView) view.findViewById(R.id.wind_sc_text);
            TextView windSpdText = (TextView) view.findViewById(R.id.wind_spd_text);
            TextView uvIndexText = (TextView) view.findViewById(R.id.uv_index_text);
            TextView visText = (TextView) view.findViewById(R.id.vis_text);
            dateText.setText(forecast.date);
            maxText.setText(forecast.tmpMax);
            minText.setText(forecast.tmpMin);
            condDText.setText(forecast.condTxtD);
            String condImageCode = "https://cdn.heweather.com/cond_icon/" + forecast.condCodeD + ".png";
            Glide.with(this).load(condImageCode).into(condImageView);
            windDirText.setText(forecast.windDir);
            windScText.setText(forecast.windSc);
            windSpdText.setText(forecast.windSpd);
            uvIndexText.setText(forecast.uvIndex);
            visText.setText(forecast.vis);
            forecastLayout.addView(view);

            lifeStyleLayout.removeAllViews();
            for(LifeStyle lifeStyle : weather.lifeStyleList) {
                View lifeView = LayoutInflater.from(this).inflate(R.layout.lifestyle_item,
                        lifeStyleLayout, false);
                TextView lifeTypeText = (TextView) lifeView.findViewById(R.id.lifestyle_type_text);
                TextView lifeBrfText = (TextView) lifeView.findViewById(R.id.lifestyle_brf_text);
                TextView lifeTxtText = (TextView) lifeView.findViewById(R.id.lifestyle_txt_text);
                String type = null;
                switch(lifeStyle.type) {
                    case "comf":
                        type = "舒适度";
                        break;
                    case "cw":
                        type = "洗车指数";
                        break;
                    case "drsg":
                        type = "穿衣指数";
                        break;
                    case "flu":
                        type = "感冒指数";
                        break;
                    case "sport":
                        type = "运动指数";
                        break;
                    case "trav":
                        type = "旅游指数";
                        break;
                    case "uv":
                        type = "紫外线指数";
                        break;
                    case "air" :
                        type = "空气质量";
                        break;
                    default:
                }
                lifeTypeText.setText(type);
                lifeBrfText.setText(lifeStyle.brf);
                lifeTxtText.setText(lifeStyle.txt);
                lifeStyleLayout.addView(lifeView);
            }
        }

    }

    /**
     * 加载必应每日一图
     */
    public void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                        WeatherActivity.this
                ).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                   @Override
                    public void run() {
                       Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                   }
                });
            }
        });
    }
}
