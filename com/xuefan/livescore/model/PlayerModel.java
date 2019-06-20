package com.xuefan.livescore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class PlayerModel {

    @SerializedName("player_id")
    @Expose
    public int playerId;

    @SerializedName("player_name")
    @Expose
    public String playerName;

    @SerializedName("team_id")
    @Expose
    public int teamId;

    @SerializedName("team_name")
    @Expose
    public String teamName;

    @SerializedName("number")
    @Expose
    public int number;

    @SerializedName("age")
    @Expose
    public int age;

    @SerializedName("position")
    @Expose
    public String position;

    @SerializedName("injured")
    @Expose
    public String injured;

    @SerializedName("rating")
    @Expose
    public String rating;

    @SerializedName("captain")
    @Expose
    public int captain;

    @SerializedName("shots")
    @Expose
    public HashMap<String, String> shots;

    @SerializedName("goals")
    @Expose
    public HashMap<String, String> goals;

    @SerializedName("passes")
    @Expose
    public HashMap<String, String> passes;

    @SerializedName("tackles")
    @Expose
    public HashMap<String, String> tackles;

    @SerializedName("duels")
    @Expose
    public HashMap<String, String> duels;

    @SerializedName("dribbles")
    @Expose
    public HashMap<String, String> dribbles;

    @SerializedName("cards")
    @Expose
    public HashMap<String, String> cards;

    @SerializedName("penalty")
    @Expose
    public HashMap<String, String> penalty;

    @SerializedName("games")
    @Expose
    public HashMap<String, String> games;

    @SerializedName("substitutes")
    @Expose
    public HashMap<String, String> substitutes;
}
