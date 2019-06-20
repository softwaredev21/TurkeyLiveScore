package com.xuefan.livescore.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import com.xuefan.livescore.MainActivity
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.R
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment: Fragment() {

    protected var currentLeagueIdx = 0
    protected var teamLeft = -1
    protected var teamRight = -1
    private var leaguesAdapter = FavoriteLeaguesAdapter()
    private var listAdapter = FavoriteTeamsAdapter()
    private var playersAdapter = PlayersAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_favorites, container, false)

        var tabLeagues = rootView.findViewById<View>(R.id.tabFavoriteLeagues)
        var tabTeams = rootView.findViewById<View>(R.id.tabFavoriteTeams)
        var tabPlayers = rootView.findViewById<View>(R.id.tabFavoritePlayers)
        tabTeams.setOnClickListener {
            tabTeams.setBackgroundColor(activity!!.resources.getColor(android.R.color.holo_green_dark))
            tabPlayers.setBackgroundColor(activity!!.resources.getColor(R.color.opacLightGreen))
            tabLeagues.setBackgroundColor(activity!!.resources.getColor(R.color.opacLightGreen))
            lstFavorites.adapter = listAdapter
            listAdapter.notifyDataSetChanged()

        }
        tabPlayers.setOnClickListener {
            tabPlayers.setBackgroundColor(activity!!.resources.getColor(android.R.color.holo_green_dark))
            tabTeams.setBackgroundColor(activity!!.resources.getColor(R.color.opacLightGreen))
            tabLeagues.setBackgroundColor(activity!!.resources.getColor(R.color.opacLightGreen))
            lstFavorites.adapter = playersAdapter
            playersAdapter.notifyDataSetChanged()
        }
        tabLeagues.setOnClickListener {
            tabLeagues.setBackgroundColor(activity!!.resources.getColor(android.R.color.holo_green_dark))
            tabPlayers.setBackgroundColor(activity!!.resources.getColor(R.color.opacLightGreen))
            tabTeams.setBackgroundColor(activity!!.resources.getColor(R.color.opacLightGreen))
            lstFavorites.adapter = leaguesAdapter
            leaguesAdapter.notifyDataSetChanged()
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lstFavorites.adapter = leaguesAdapter
    }

    public fun refresh() {
        listAdapter.notifyDataSetChanged()
        playersAdapter.notifyDataSetChanged()
        leaguesAdapter.notifyDataSetChanged()
    }

    inner class FavoriteLeaguesAdapter: BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var cvh: CustomViewHolder
            var view: View
            if(convertView == null){
                view = layoutInflater.inflate(R.layout.item_teamdetails, parent, false)
                cvh = CustomViewHolder(view)
                view.tag = cvh
            }else{
                view = convertView
                cvh = view.tag as CustomViewHolder
            }
            var league = GlobalStorage.defStorage.favoriteLeagues[position]
            Picasso.get().load(league.logo)
                .placeholder(R.drawable.ic_turkey_superlig)
                .into(cvh.teamLogo)
            cvh.teamName.text = league.name
            cvh.teamDetails.text = league.country
            cvh.teamFound.text = league.season_start + " - " + league.season_end
            view.findViewById<View>(R.id.btnPlayers).visibility = View.GONE
            return view
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return GlobalStorage.defStorage.favoriteLeagues.count()
        }

    }

    inner class FavoriteTeamsAdapter: BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var cvh: CustomViewHolder
            var view: View
            if(convertView == null){
                view = layoutInflater.inflate(R.layout.item_teamdetails, parent, false)
                cvh = CustomViewHolder(view)
                view.tag = cvh
            }else{
                view = convertView
                cvh = view.tag as CustomViewHolder
            }
            var team = GlobalStorage.defStorage.favoriteTeams[position]
            Picasso.get().load(team.logo)
                .placeholder(R.drawable.ic_turkey_superlig)
                .into(cvh.teamLogo)
            cvh.teamName.text = team.teamName
            cvh.teamDetails.text = team.country + " " + team.venueCity
            cvh.teamFound.text = getString(R.string.label_founded) + team.founded
            view.setOnClickListener {
                (activity!! as MainActivity).selectTeam(team.teamId)
                (activity!! as MainActivity).onTabClicked(R.id.tabFixtures)
            }
            cvh.btnLike.setOnClickListener {
                GlobalStorage.defStorage.removeFavoriteTeam(GlobalStorage.defStorage.favoriteTeams.indexOfFirst { it.teamId == team.teamId })
                listAdapter.notifyDataSetChanged()
            }
            return view
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return GlobalStorage.defStorage.favoriteTeams.count()
        }

    }

    private inner class PlayersAdapter: BaseAdapter() {
        var lastView: LinearLayout? = null
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var cvh: CustomPlayerViewHolder
            var view: View
            if(convertView == null){
                view = layoutInflater.inflate(R.layout.item_player_details, parent, false)
                cvh = CustomPlayerViewHolder(view)
                view.tag = cvh
            }else{
                view = convertView
                cvh = view.tag as CustomPlayerViewHolder
            }
            var player = GlobalStorage.defStorage.favoritePlayers[position]

            cvh.name.text = player.playerName
            cvh.age.text = player.age.toString()
            cvh.no.text = player.number.toString()
            if (player.rating != null) {
                cvh.rating.rating = player.rating.toFloat()/2
            } else {
                cvh.rating.rating = 0f
            }
            if (player.injured == "True") {
                cvh.injured.text = resources.getString(R.string.injuered)
            } else {
                cvh.injured.text = ""
            }
            cvh.like.setOnClickListener {
                GlobalStorage.defStorage.addFavoritePlayer(player)
                cvh.like.setImageResource(R.drawable.ic_star)
            }
            if (GlobalStorage.defStorage.favoritePlayers.find { it.playerId == player.playerId } != null) {
                cvh.like.setImageResource(R.drawable.ic_star)
            } else {
                cvh.like.setImageResource(R.drawable.ic_star_off)
            }
            cvh.id = player.playerId
            cvh.goals.text = player.goals.get("total")
            cvh.assists.text = player.goals.get("assists")
            cvh.pos.text = player.position

            cvh.shots.text = player.shots.get("total")
            cvh.shotson.text = player.shots.get("on")

            cvh.passes.text = player.passes.get("total")
            cvh.acc.text = player.passes.get("accuracy")

            cvh.tackles.text = player.tackles.get("total")
            cvh.blocks.text = player.tackles.get("blocks")

            cvh.duels.text = player.duels.get("total")
            cvh.wins.text = player.duels.get("win")

            cvh.dribbles.text = player.dribbles.get("attempts")
            cvh.dribblesuccess.text = player.dribbles.get("success")

            cvh.yellows.text = player.cards.get("yellow")
            cvh.yreds.text = player.cards.get("yellowred")
            cvh.reds.text = player.cards.get("red")

            cvh.panelty.text = player.penalty.get("success")
            cvh.missed.text = player.penalty.get("missed")
            cvh.saved.text = player.penalty.get("saved")

            cvh.games.text = player.games.get("appearences")
            cvh.minutes.text = player.games.get("minutes_played")
            cvh.lineups.text = player.games.get("lineups")

            cvh.subsits.text = player.substitutes.get("in")
            cvh.subsitsout.text = player.substitutes.get("out")
            cvh.subsitsbench.text = player.substitutes.get("bench")
            view.setOnClickListener {
                if (lastView != null) {
                    lastView!!.visibility = View.GONE
                }
                cvh.details.visibility = View.VISIBLE
                lastView = cvh.details
            }
            cvh.like.setOnClickListener {
                GlobalStorage.defStorage.removeFavoritePlayer(GlobalStorage.defStorage.favoritePlayers.indexOfFirst { it.playerId == player.playerId })
                playersAdapter.notifyDataSetChanged()
            }
            cvh.details.visibility = View.GONE
            return view
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return GlobalStorage.defStorage.favoritePlayers.count()
        }

    }

    inner class CustomViewHolder(view : View){
        var teamLogo = view.findViewById<ImageView>(R.id.imgTeamDetailsLog)
        var teamName = view.findViewById<TextView>(R.id.txtTeamName)
        var teamDetails = view.findViewById<TextView>(R.id.txtTeamDetails)
        var teamFound = view.findViewById<TextView>(R.id.txtTeamFound)
        var btnLike = view.findViewById<ImageButton>(R.id.btnTeamLike)
    }

    inner class CustomPlayerViewHolder(view : View){
        var id = 0
        var name = view.findViewById<TextView>(R.id.txtPlayerName)
        var no = view.findViewById<TextView>(R.id.txtPlayerNo)
        var age = view.findViewById<TextView>(R.id.txtPlayerAge)
        var injured = view.findViewById<TextView>(R.id.txtPlayerInjured)
        var rating = view.findViewById<RatingBar>(R.id.playerRatingBar)
        var pos = view.findViewById<TextView>(R.id.txtPlayerPosition)
        var details = view.findViewById<LinearLayout>(R.id.layoutPlayerDetails)
        var like = view.findViewById<ImageButton>(R.id.btnLike)

        var goals = view.findViewById<TextView>(R.id.txtPlayerGoals)
        var assists = view.findViewById<TextView>(R.id.txtPlayerGoalAssists)

        var shots = view.findViewById<TextView>(R.id.txtPlayerShot)
        var shotson = view.findViewById<TextView>(R.id.txtPlayerShotOn)

        var passes = view.findViewById<TextView>(R.id.txtPlayerPasses)
        var acc = view.findViewById<TextView>(R.id.txtPlayerPassesAccuracy)

        var tackles = view.findViewById<TextView>(R.id.txtPlayerTackles)
        var blocks = view.findViewById<TextView>(R.id.txtPlayerBlocks)

        var duels = view.findViewById<TextView>(R.id.txtPlayerDuels)
        var wins = view.findViewById<TextView>(R.id.txtPlayerDuelsWon)

        var dribbles = view.findViewById<TextView>(R.id.txtPlayerDribbles)
        var dribblesuccess = view.findViewById<TextView>(R.id.txtPlayerDribblesSuccess)

        var yellows = view.findViewById<TextView>(R.id.txtPlayerCards)
        var yreds = view.findViewById<TextView>(R.id.txtPlayerYReds)
        var reds = view.findViewById<TextView>(R.id.txtPlayerReds)

        var panelty = view.findViewById<TextView>(R.id.txtPlayerPanelty)
        var missed = view.findViewById<TextView>(R.id.txtPlayerPaneltyMissed)
        var saved = view.findViewById<TextView>(R.id.txtPlayerPaneltySaved)

        var games = view.findViewById<TextView>(R.id.txtPlayerGames)
        var minutes = view.findViewById<TextView>(R.id.txtPlayerGameMinutes)
        var lineups = view.findViewById<TextView>(R.id.txtPlayerGameLineups)

        var subsits = view.findViewById<TextView>(R.id.txtPlayerSubstitutes)
        var subsitsout = view.findViewById<TextView>(R.id.txtPlayerSubstitutesOut)
        var subsitsbench = view.findViewById<TextView>(R.id.txtPlayerSubstitutesBench)
    }
}
