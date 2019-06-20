package com.xuefan.livescore.api

import com.xuefan.livescore.data.GlobalSettings
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.model.FixtureModel
import com.xuefan.livescore.model.LeagueModel
import com.xuefan.livescore.model.PlayerModel
import com.xuefan.livescore.model.StandingModel
import com.xuefan.livescore.model.packet.*
import java.text.SimpleDateFormat
import java.util.*

class ApiService {

    fun loadLeagues(season: Int = 2018){
        val call = APIClient.getInstance().getLeagues(season)
        try {
            val response = call.execute()

            if (response.isSuccessful) {
                var leagues = (response.body() as LeaguesResponse).api.leagues
                for (league in leagues) {
                    if (GlobalSettings.AvailableCountries.find { it == league.country } == null) {
                        continue
                    }
                    for (j in 0 .. GlobalSettings.AvailableLeagueNames.count() - 1) {
                        if (GlobalSettings.AvailableLeagueNames[j] == league.name.replace(" ","")) {
                            GlobalStorage.defStorage.leagues[j] = league
                            break
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadTeams(leagueIndex: Int){
        val call = APIClient.getInstance().getTeams(GlobalStorage.getLeagueId(leagueIndex))
        try {
            val response = call.execute()

            if (response.isSuccessful) {
                var teams = (response.body() as TeamsResponse).api.teams
                for (team in teams) {
                    GlobalStorage.defStorage.teams.put(team.teamId, team)
                }
                //GlobalStorage.defStorage.teams[leagueIndex] = teams
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadPlayers(teamId: Int) : List<PlayerModel>?{
        val call = APIClient.getInstance().getPlayers(teamId)
        try {
            val response = call.execute()

            if (response.isSuccessful) {
                var players = (response.body() as PlayersResponse).api.players
                return players
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getH2HFixtures(leagueIndex: Int, teamLeftId: Int, teamRightId: Int) : List<FixtureModel>?{
        val call = APIClient.getInstance().getH2HFixtures(teamLeftId, teamRightId)
        try {
            val response = call.execute()

            if (response.isSuccessful) {
                var fixtures = (response.body() as FixturesResponse).api.fixtures
                return fixtures
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun loadLeagues(leagueId: Int, onComplete: (LeagueModel?) -> Unit) {
        if (GlobalStorage.leaguesData[leagueId] != null ) {
            onComplete(GlobalStorage.leaguesData[leagueId])
            return;
        }

        Thread(Runnable {
            val call = APIClient.getInstance().getLeagueById(leagueId)

            try {
                val response = call.execute()

                if (response.isSuccessful) {
                    if ((response.body() as LeaguesResponse).api.leagues.count() > 0) {
                        GlobalStorage.leaguesData[leagueId] = (response.body() as LeaguesResponse).api.leagues[0]
                        onComplete(GlobalStorage.leaguesData[leagueId])
                    } else {
                        onComplete(null)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(null)
            }

        }).start()
    }

    fun loadStandings(index: Int = 0){
        val call = APIClient.getInstance()
            .getStandings(GlobalStorage.defStorage.leagues[index].league_id) //current 2018-2019 turkish super lig 467,468
        try {
            val response = call.execute()
            GlobalStorage.defStorage.standings[index] = ArrayList<StandingModel>()
            if (response.isSuccessful && (response.body() as StandingsResponse).api.standings.size > 0) {
                var standings = (response.body() as StandingsResponse).api.standings
                for (st in standings) {
                    GlobalStorage.defStorage.standings[index] = GlobalStorage.defStorage.standings[index].plus(st)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadStandings(index: Int = 0, onComplete: (List<StandingModel>?) -> Unit = {}){
        if (GlobalStorage.defStorage.standings[index] != null) {
            onComplete(GlobalStorage.defStorage.standings[index])
            return
        }

        val call = APIClient.getInstance()
            .getStandings(GlobalStorage.defStorage.leagues[index].league_id) //current 2018-2019 turkish super lig 467,468
        try {
            val response = call.execute()
            if (response.isSuccessful && (response.body() as StandingsResponse).api.standings.size > 0) {
                GlobalStorage.defStorage.standings[index] = (response.body() as StandingsResponse).api.standings[0]
                if (onComplete != null) {
                    onComplete(GlobalStorage.defStorage.standings[index])
                    return
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (onComplete != null) {
            onComplete(null)
        }
    }

    fun loadTeamFixtures(teamId: Int) : List<FixtureModel>?{
        val call = APIClient.getInstance().getTeamFixtures(teamId) //current 2018-2019 turkish super lig
        try {
            val response = call.execute()
            val res = (response.body() as FixturesResponse).api.fixtures
            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun loadFixtures(index: Int = 0) : List<FixtureModel>? {
        if (GlobalStorage.getFixtures(index) != null) {
            return GlobalStorage.getFixtures(index);
        }

        val call = APIClient.getInstance().getFixtures(GlobalStorage.getLeagueId(index))// .getFixturesByDate(formator.format(today)) //current 2018-2019 turkish super lig
        try {
            val response = call.execute()
            GlobalStorage.setFixtures(index, (response.body() as FixturesResponse).api.fixtures)
            return GlobalStorage.getFixtures(index);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }

    companion object {
        var instance = ApiService()
    }
}