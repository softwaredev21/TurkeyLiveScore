package com.xuefan.livescore.api

import com.xuefan.livescore.model.packet.*
import retrofit2.Call
import retrofit2.http.*

interface APIRequest {

    @GET("leagues/season/{season}")
    abstract fun getLeagues(@Path("season") season: Int): Call<LeaguesResponse>

    @GET("leagues/country/{country_name}/{season}")
    abstract fun getLeagues(@Path("country_name") countryCode: String, @Path("season") season: Int): Call<LeaguesResponse>

    @GET("leagues/league/{league_id}")
    abstract fun getLeagueById(@Path("league_id") leagueId: Int): Call<LeaguesResponse>

    @GET("teams/league/{league_id}")
    abstract fun getTeams(@Path("league_id") leagueId: Int): Call<TeamsResponse>

    @GET("teams/team/{team_id}")
    abstract fun getTeamById(@Path("team_id") teamId: Int): Call<TeamsResponse>

    @GET("players/team/{team_id}")
    abstract fun getPlayers(@Path("team_id") teamId: Int): Call<PlayersResponse>

    @GET("players/player/{player_id}")
    abstract fun getPlayerById(@Path("player_id") playerId: Int): Call<PlayersResponse>

    @GET("leagueTable/{league_id}")
    abstract fun getStandings(@Path("league_id") leagueId: Int): Call<StandingsResponse>

    @GET("fixtures/league/{league_id}")
    abstract fun getFixtures(@Path("league_id") leagueId: Int): Call<FixturesResponse>

    @GET("fixtures/date/{date}")
    abstract fun getFixturesByDate(@Path("date") date: String): Call<FixturesResponse>

    @GET("fixtures/h2h/{team_id_1}/{team_id_2}")
    abstract fun getH2HFixtures(@Path("team_id_1") teamLeftId: Int, @Path("team_id_2") teamRightId: Int): Call<FixturesResponse>

    @GET("fixtures/id/{fixture_id}")
    abstract fun getFixture(@Path("fixture_id") fixtureId: Int): Call<DetailedFixturesResponse>

    @GET("fixtures/team/{team_id}")
    abstract fun getTeamFixtures(@Path("team_id") teamId: Int): Call<FixturesResponse>

    @GET("fixtures/live")
    abstract fun getLiveFixtures(): Call<FixturesResponse>
}