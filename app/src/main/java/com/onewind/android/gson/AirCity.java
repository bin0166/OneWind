package com.onewind.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bin on 2017/11/5.
 */

public class AirCity {

    public String aqi;

    public String main;

    public String co;

    public String no2;

    public String o3;

    public String pm10;

    public String pm25;

    @SerializedName("pub_time")
    public String pubTime;

    public String qlty;

    public String so2;

}
