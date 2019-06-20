package com.xuefan.livescore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FixtureModel {

    @SerializedName("fixture_id")
    @Expose
    public int fixture_id;

    @SerializedName("league_id")
    @Expose
    public int league_id;

    @SerializedName("event_date")
    @Expose
    public String event_date;

    @SerializedName("event_timestamp")
    @Expose
    public long event_timestamp;

    @SerializedName("firstHalfStart")
    @Expose
    public long firstHalfStart;

    @SerializedName("secondHalfStart")
    @Expose
    public long secondHalfStart;

    @SerializedName("round")
    @Expose
    public String round;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("statusShort")
    @Expose
    public String statusShort;

    @SerializedName("elapsed")
    @Expose
    public int elapsed;

    @SerializedName("venue")
    @Expose
    public String venue;

    @SerializedName("referee")
    @Expose
    public String referee;

    @SerializedName("homeTeam")
    @Expose
    public TeamDetails homeTeam;

    @SerializedName("awayTeam")
    @Expose
    public TeamDetails awayTeam;

    @SerializedName("score")
    @Expose
    public ScoreDetails score;

    @SerializedName("goalsHomeTeam")
    @Expose
    public int goalsHomeTeam;

    @SerializedName("goalsAwayTeam")
    @Expose
    public int goalsAwayTeam;

    public class TeamDetails {
        @SerializedName("team_id")
        @Expose
        public int team_id;

        @SerializedName("team_name")
        @Expose
        public String team_name;

        @SerializedName("logo")
        @Expose
        public String logo;
    }

    public class ScoreDetails {
        @SerializedName("halftime")
        @Expose
        public String halftime;

        @SerializedName("fulltime")
        @Expose
        public String fulltime;

        @SerializedName("extratime")
        @Expose
        public String extratime;

        @SerializedName("penalty")
        @Expose
        public String penalty;
    }
}
