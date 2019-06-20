package com.xuefan.livescore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xuefan.livescore.model.packet.StandingsResponse;

public class StandingModel {

    @SerializedName("rank")
    @Expose
    public int rank;

    @SerializedName("team_id")
    @Expose
    public int team_id;

    @SerializedName("teamName")
    @Expose
    public String teamName;

    @SerializedName("logo")
    @Expose
    public String logo;

    @SerializedName("group")
    @Expose
    public String group;

    @SerializedName("forme")
    @Expose
    public String forme;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("all")
    @Expose
    public StandingDetails all;

    @SerializedName("home")
    @Expose
    public StandingDetails home;

    @SerializedName("away")
    @Expose
    public StandingDetails away;

    @SerializedName("goalsDiff")
    @Expose
    public int goalsDiff;

    @SerializedName("points")
    @Expose
    public int points;

    @SerializedName("lastUpdate")
    @Expose
    public String lastUpdate;

    public class StandingDetails {

        @SerializedName("matchsPlayed")
        @Expose
        public int matchsPlayed;

        @SerializedName("win")
        @Expose
        public int win;

        @SerializedName("draw")
        @Expose
        public int draw;

        @SerializedName("lose")
        @Expose
        public int lose;

        @SerializedName("goalsFor")
        @Expose
        public int goalsFor;

        @SerializedName("goalsAgainst")
        @Expose
        public int goalsAgainst;
    }
}
