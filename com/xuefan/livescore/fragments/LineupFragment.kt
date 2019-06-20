package com.xuefan.livescore.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.xuefan.livescore.R
import com.xuefan.livescore.model.LineupModel
import com.xuefan.livescore.model.PlayerInfo
import kotlinx.android.synthetic.main.fragment_lineup.*

class LineupFragment: Fragment() {

    private var homeLineup: LineupModel = LineupModel()
    private var awayLineup: LineupModel = LineupModel()

    public fun setLineup(param1: LineupModel, param2: LineupModel) {
        homeLineup = param1
        awayLineup = param2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_lineup, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var homeFormation = layoutHomeLineup.findViewById<TextView>(R.id.txtFormation)
        var awayFormation = layoutAwayLineup.findViewById<TextView>(R.id.txtFormation)

        var homeCoach = layoutHomeLineup.findViewById<TextView>(R.id.txtCoach)
        var awayCoach = layoutAwayLineup.findViewById<TextView>(R.id.txtCoach)

        var homePlayers = layoutHomeLineup.findViewById<ListView>(R.id.lstPlayers)
        var awayPlayers = layoutAwayLineup.findViewById<ListView>(R.id.lstPlayers)

        homeFormation.text = homeLineup.formation
        awayFormation.text = awayLineup.formation

        homeCoach.text = homeLineup.coach
        awayCoach.text = awayLineup.coach

        var homePlayersAdapter = PlayerListAdapter()
        if (homeLineup.startXI != null ) {
            homePlayersAdapter.players = homePlayersAdapter.players.plus(homeLineup.startXI)
            if (homeLineup.substitutes != null ) {
                homePlayersAdapter.players = homePlayersAdapter.players.plus(homeLineup.substitutes)
            }
        }
        homePlayers.adapter = homePlayersAdapter


        var awayPlayersAdapter = PlayerListAdapter()
        if (awayLineup.startXI != null ) {
            awayPlayersAdapter.players = awayPlayersAdapter.players.plus(awayLineup.startXI)
            if (awayLineup.substitutes != null ){
                awayPlayersAdapter.players = awayPlayersAdapter.players.plus(awayLineup.substitutes)
            }
        }
        awayPlayers.adapter = awayPlayersAdapter
    }

    inner class PlayerListAdapter: BaseAdapter() {

        var players: List<PlayerInfo> = ArrayList()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.item_player, parent, false)
            } else {
                view = convertView
            }
            view.findViewById<TextView>(R.id.txtPlayerNo).text = players[position].number
            view.findViewById<TextView>(R.id.txtPlayerName).text = players[position].player
            if (position > 10) {
                view.setBackgroundColor(resources.getColor(R.color.opacLightGreen))
            } else {
                view.setBackgroundColor(resources.getColor(R.color.opacLightBlue))
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
            return players.count()
        }

    }
}