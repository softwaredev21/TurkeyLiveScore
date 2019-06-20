package com.xuefan.livescore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeagueModel {

    @SerializedName("league_id")
    @Expose
    public int league_id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("country_code")
    @Expose
    public String country_code;

    @SerializedName("season")
    @Expose
    public int season;

    @SerializedName("season_start")
    @Expose
    public String season_start;

    @SerializedName("season_end")
    @Expose
    public String season_end;

    @SerializedName("logo")
    @Expose
    public String logo;

    @SerializedName("flag")
    @Expose
    public String flag;

    @SerializedName("standings")
    @Expose
    public int standings;

    @SerializedName("is_current")
    @Expose
    public int is_current;
}
