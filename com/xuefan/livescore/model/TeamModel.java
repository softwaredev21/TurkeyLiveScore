package com.xuefan.livescore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamModel {

    @SerializedName("team_id")
    @Expose
    public int teamId;

    @SerializedName("name")
    @Expose
    public String teamName;

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("logo")
    @Expose
    public String logo;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("founded")
    @Expose
    public int founded;

    @SerializedName("venue_name")
    @Expose
    public String venueName;

    @SerializedName("venue_surface")
    @Expose
    public String venueSurface;

    @SerializedName("venue_address")
    @Expose
    public String venueAddress;

    @SerializedName("venue_city")
    @Expose
    public String venueCity;

    @SerializedName("venue_capacity")
    @Expose
    public String venueCapacity;
}
