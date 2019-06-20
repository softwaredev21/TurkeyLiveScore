package com.xuefan.livescore.model.packet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xuefan.livescore.model.LeagueModel;

import java.util.ArrayList;

public class LeaguesResponse {

    @SerializedName("api")
    @Expose
    public APIResponse api;

    public class APIResponse {

        @SerializedName("results")
        @Expose
        public int results;

        @SerializedName("leagues")
        @Expose
        public ArrayList<LeagueModel> leagues;

    }

}
