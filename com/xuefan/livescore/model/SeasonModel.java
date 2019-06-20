package com.xuefan.livescore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeasonModel {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("league_id")
    @Expose
    public int leagueId;

    @SerializedName("is_current_season")
    @Expose
    public boolean isCurrentSeason;

    @SerializedName("current_round_id")
    @Expose
    public int currentRoundId;

    @SerializedName("current_stage_id")
    @Expose
    public int currentStageId;
}
