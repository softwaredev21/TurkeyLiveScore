package com.xuefan.livescore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LineupModel {

    @SerializedName("formation")
    @Expose
    public String formation;

    @SerializedName("startXI")
    @Expose
    public ArrayList<PlayerInfo> startXI;

    @SerializedName("substitutes")
    @Expose
    public ArrayList<PlayerInfo> substitutes;

    @SerializedName("coach")
    @Expose
    public String coach;

}
