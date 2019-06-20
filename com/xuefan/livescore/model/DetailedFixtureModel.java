package com.xuefan.livescore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailedFixtureModel extends FixtureModel {

    @SerializedName("lineups")
    @Expose
    public HashMap<String, LineupModel> lineups;

    @SerializedName("statistics")
    @Expose
    public HashMap<String, StatInfo> statistics;

    @SerializedName("events")
    @Expose
    public ArrayList<EventInfo> events;

    public class StatInfo {

        @SerializedName("home")
        @Expose
        public String home;

        @SerializedName("away")
        @Expose
        public String away;
    }


    public class EventInfo {

        @SerializedName("elapsed")
        @Expose
        public int elapsed;

        @SerializedName("team_id")
        @Expose
        public String team_id;

        @SerializedName("teamName")
        @Expose
        public String teamName;

        @SerializedName("player")
        @Expose
        public String player;

        @SerializedName("type")
        @Expose
        public String type;

        @SerializedName("detail")
        @Expose
        public String detail;
    }
}
