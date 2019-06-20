package com.xuefan.livescore.model.packet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xuefan.livescore.model.PlayerModel;

import java.util.ArrayList;

public class PlayersResponse {

    @SerializedName("api")
    @Expose
    public APIResponse api;

    public class APIResponse {

        @SerializedName("results")
        @Expose
        public int results;

        @SerializedName("players")
        @Expose
        public ArrayList<PlayerModel> players;
    }
}
