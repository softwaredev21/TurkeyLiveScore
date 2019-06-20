package com.xuefan.livescore.data;

import android.util.Log;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xuefan.livescore.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalStorage {

    public static String DBName = "GlobalStorage";
    public static GlobalStorage defStorage = null;
    public static Map<Integer, LeagueModel> leaguesData = new HashMap<>();

    public static boolean loadStorage() {
        Object obj = TinyDB.getDB().getObject(DBName, GlobalStorage.class);
        try {
            if (obj == null) {
                return false;
            } else {
                defStorage = (GlobalStorage) obj;
                return true;
            }
        }catch (Exception ex) {
            //ex.printStackTrace();
            Log.d(DBName, "loading db failed");
            return false;
        }
    }

    public static void create() {
        defStorage = new GlobalStorage();
        for(int i=0; i<defStorage.notifications.length; i++) {
            defStorage.notifications[i] = true;
        }
    }

    public static void saveStorage() {
        TinyDB.getDB().putObject(DBName, defStorage);
    }

    public static List<StandingModel> getStandings(int leagueIdx) {
        return defStorage.standings[leagueIdx];
    }

    public static void setStandings(int leagueIdx, List<StandingModel> standings) {
        defStorage.standings[leagueIdx] = standings;
    }

    public static List<FixtureModel> getFixtures(int leagueIdx) {
        if (leagueIdx >= 0 && leagueIdx < GlobalSettings.AvailableLeagues.length)
            return defStorage.fixtures[leagueIdx];
        return null;
    }

    public static void setFixtures(int leagueIdx, List<FixtureModel> fixtures) {
        if (leagueIdx >= 0 && leagueIdx < GlobalSettings.AvailableLeagues.length)
            defStorage.fixtures[leagueIdx] = fixtures;
    }

    public static List<FixtureModel> getSchedFixtures(String date) {
        if (defStorage.schedFixtures.get(date) != null) {
            return defStorage.schedFixtures.get(date);
        }
        return new ArrayList<>();
    }

    public static void putSchedFixtures(String date, List<FixtureModel> fixtures) {
        defStorage.schedFixtures.put(date, fixtures);
    }

    public static int getLeagueId(int index) {
        return defStorage.leagues[index].league_id;
    }

    public static String getLeagueNameById(int id) {
        for (int i=0; i<defStorage.leagues.length; i++ ) {
            if(id == defStorage.leagues[i].league_id) {
                return defStorage.leagues[i].name;
            }
        }
        return "";
    }

    public static int getLeagueIdxById(int id) {
        for (int i=0; i<defStorage.leagues.length; i++ ) {
            if(id == defStorage.leagues[i].league_id) {
                return i;
            }
        }
        return -1;
    }

    public static List<String> getTeamNames(int ligIndex) {
        if (defStorage.standings[ligIndex] != null) {
            List<String> res = new ArrayList<String>();
            res.add(" *** ");
            for (int i=0; i<defStorage.standings[ligIndex].size(); i++) {
                res.add(defStorage.standings[ligIndex].get(i).teamName);
            }
            return res;
        }
        return new ArrayList<String>();
    }

    public static int getTeamId(int ligIdx, int position) {
        return defStorage.standings[ligIdx].get(position).team_id;
    }

    public LeagueModel[] leagues = new LeagueModel[GlobalSettings.AvailableLeagueNames.length];

    @SerializedName("NotificationSetting")
    @Expose
    public boolean[] notifications = new boolean[GlobalSettings.TopicNames.length];

    public Map<Integer, TeamModel> teams = new HashMap<Integer, TeamModel>();

    public List<StandingModel>[] standings = new ArrayList[GlobalSettings.AvailableLeagueNames.length];

    public List<FixtureModel>[] fixtures = new ArrayList[GlobalSettings.AvailableLeagueNames.length];;

    public Map<String, List<FixtureModel>> schedFixtures = new HashMap<>();

    @SerializedName("favTeams")
    @Expose
    private List<TeamModel> favoriteTeams = new ArrayList<>();

    @SerializedName("favPlayers")
    @Expose
    private List<PlayerModel> favoritePlayers = new ArrayList<>();

    @SerializedName("favLeagues")
    @Expose
    private List<LeagueModel> favoriteLeagues = new ArrayList<>();

    public List<TeamModel> getFavoriteTeams() {
        return favoriteTeams;
    }

    public List<LeagueModel> getFavoriteLeagues() {
        return favoriteLeagues;
    }

    public void addFavoriteTeam(TeamModel team) {
        favoriteTeams.add(team);
        GlobalStorage.saveStorage();
    }

    public void removeFavoriteTeam(int idx) {
        favoriteTeams.remove(idx);
        GlobalStorage.saveStorage();
    }

    public void setFavoriteTeams(List<TeamModel> favoriteTeams) {
        this.favoriteTeams = favoriteTeams;
    }

    public List<PlayerModel> getFavoritePlayers() {
        return favoritePlayers;
    }

    public void addFavoritePlayer(PlayerModel player) {
        favoritePlayers.add(player);
        GlobalStorage.saveStorage();
    }

    public void removeFavoritePlayer(int idx) {
        favoritePlayers.remove(idx);
        GlobalStorage.saveStorage();
    }

    public void setFavoritePlayers(List<PlayerModel> favoritePlayers) {
        this.favoritePlayers = favoritePlayers;
    }

    public boolean isFavoriteLeague(int idx) {
        for (int i=0; i<favoriteLeagues.size(); i++) {
            if (favoriteLeagues.get(i).league_id == leagues[idx].league_id) {
                return true;
            }
        }
        return false;
    }

    public void setFavoriteLeague(int idx, boolean like) {
        if (like) {
            this.favoriteLeagues.add(leagues[idx]);
        } else {
            for (int i=0; i<favoriteLeagues.size(); i++) {
                if (favoriteLeagues.get(i).league_id == leagues[idx].league_id) {
                    favoriteLeagues.remove(i);
                    break;
                }
            }
        }
        GlobalStorage.saveStorage();
    }

    public boolean isFavoriteLeagueById(int id) {
        for (int i=0; i<favoriteLeagues.size(); i++) {
            if (favoriteLeagues.get(i).league_id == id) {
                return true;
            }
        }
        return false;
    }

    public String getFavoriteLeagueNameById(int id) {
        for (int i=0; i<favoriteLeagues.size(); i++) {
            if (favoriteLeagues.get(i).league_id == id) {
                return favoriteLeagues.get(i).name;
            }
        }
        return "";
    }
}
