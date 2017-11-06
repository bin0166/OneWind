package com.onewind.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bin on 2017/11/5.
 */

public class Weather {

    public String status;

    public Basic basic;

    public Now now;

    public Update update;

    @SerializedName("lifestyle")
    public List<LifeStyle> lifeStyleList;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
