package com.xuefan.livescore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerInfo {

    @SerializedName("number")
    @Expose
    public String number;

    @SerializedName("player")
    @Expose
    public String player;
}
