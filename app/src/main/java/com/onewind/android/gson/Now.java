package com.onewind.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bin on 2017/11/5.
 */

public class Now {


    public String tmp;

    public String fl;

    @SerializedName("cond_code")
    public String condCode;

    @SerializedName("cond_txt")
    public String condTxt;

    @SerializedName("wind_dir")
    public String windDir;

    @SerializedName("wind_sc")
    public String windSc;

    @SerializedName("wind_spd")
    public String windSpd;

    public String vis;

}
