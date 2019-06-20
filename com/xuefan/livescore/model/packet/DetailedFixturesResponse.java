package com.xuefan.livescore.model.packet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xuefan.livescore.model.DetailedFixtureModel;

import java.util.ArrayList;

public class DetailedFixturesResponse {

    @SerializedName("api")
    @Expose
    public APIResponse api;

    public class APIResponse {

        @SerializedName("results")
        @Expose
        public int results;

        @SerializedName("fixtures")
        @Expose
        public ArrayList<DetailedFixtureModel> fixtures;

    }

}
