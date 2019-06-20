package com.xuefan.livescore.model.packet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xuefan.livescore.model.StandingModel;

import java.util.ArrayList;

public class StandingsResponse {

    @SerializedName("api")
    @Expose
    public APIResponse api;

    public class APIResponse {

        @SerializedName("results")
        @Expose
        public int results;

        @SerializedName("standings")
        @Expose
        public ArrayList<ArrayList<StandingModel>> standings;

    }

}
