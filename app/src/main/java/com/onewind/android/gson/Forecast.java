package com.onewind.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bin on 2017/11/5.
 */

public class Forecast {

    public String date;

    @SerializedName("tmp_max")
    public String tmpMax;

    @SerializedName("tmp_min")
    public String tmpMin;

    @SerializedName("cond_code_d")
    public String condCodeD;

    @SerializedName("code_code_n")
    public String condCodeN;

    @SerializedName("cond_txt_d")
    public String condTxtD;

    @SerializedName("cond_txt_n")
    public String condTxtN;

    @SerializedName("wind_dir")
    public String windDir;

    @SerializedName("wind_sc")
    public String windSc;

    @SerializedName("wind_spd")
    public String windSpd;

    @SerializedName("uv_index")
    public String uvIndex;

    public String vis;

}
