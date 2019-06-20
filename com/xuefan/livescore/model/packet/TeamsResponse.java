package com.xuefan.livescore.model.packet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xuefan.livescore.model.TeamModel;

import java.util.ArrayList;

public class TeamsResponse {

    @SerializedName("api")
    @Expose
    public APIResponse api;

    public class APIResponse {

        @SerializedName("results")
        @Expose
        public int results;

        @SerializedName("teams")
        @Expose
        public ArrayList<TeamModel> teams;
    }
}
