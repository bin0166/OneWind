package com.onewind.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bin on 2017/11/5.
 */

public class Basic {

    @SerializedName("location")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

}
