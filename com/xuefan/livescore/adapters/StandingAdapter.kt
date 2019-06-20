package com.xuefan.livescore.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.xuefan.livescore.MainActivity
import com.xuefan.livescore.R
import com.xuefan.livescore.model.StandingModel

class StandingAdapter: BaseAdapter() {
    private var context: Fragment? = null
    private var standings: List<StandingModel> = ArrayList<StandingModel>()

    public fun setContext(param: Fragment) {
        context = param
    }

    public fun setStandings(param: List<StandingModel>) {
        standings = param
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return standings.count()
    }

    var layoutInflater : LayoutInflater? = null

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if(layoutInflater == null)
            layoutInflater = context!!.getLayoutInflater()
        val view : View
        val cvh : CustomViewHolder

        if(convertView == null){
            view = layoutInflater!!.inflate(R.layout.item_standing, parent, false)
            cvh = CustomViewHolder(view)
            view.tag = cvh
        }else{
            view = convertView
            cvh = view.tag as CustomViewHolder
        }

        val standing = standings[position]
        try {
            var assetFilePath = "turkey/superlig/" + standing.teamName.substring(0,3) + ".png"
            var assetInputStream = context!!.activity!!.assets.open(assetFilePath)
            val drawable = Drawable.createFromStream(assetInputStream, null)
            cvh.logoView.setImageDrawable(drawable)
        }catch (ex: Exception){
            cvh.logoView.setImageResource(R.drawable.ic_turkey_superlig)
            Picasso.get().load(standing.logo)
                .placeholder(R.drawable.ic_turkey_superlig)
                .into(cvh.logoView)
        }
        cvh.nameView.text = standing.teamName
        cvh.rankView.text = standing.rank.toString()
        cvh.pointsView.text = standing.points.toString()

        cvh.allWinView.text = standing.all.win.toString()
        cvh.allDrawView.text = standing.all.draw.toString()
        cvh.allLoseView.text = standing.all.lose.toString()
        cvh.allPlayedView.text = standing.all.matchsPlayed.toString()
        cvh.allGFView.text = standing.all.goalsFor.toString()
        cvh.allGAView.text = standing.all.goalsAgainst.toString()

        cvh.homeWinView.text = standing.home.win.toString()
        cvh.homeDrawView.text = standing.home.draw.toString()
        cvh.homeLoseView.text = standing.home.lose.toString()
        cvh.homePlayedView.text = standing.home.matchsPlayed.toString()
        cvh.homeGFView.text = standing.home.goalsFor.toString()
        cvh.homeGAView.text = standing.home.goalsAgainst.toString()

        cvh.awayWinView.text = standing.away.win.toString()
        cvh.awayDrawView.text = standing.away.draw.toString()
        cvh.awayLoseView.text = standing.away.lose.toString()
        cvh.awayPlayedView.text = standing.away.matchsPlayed.toString()
        cvh.awayGFView.text = standing.away.goalsFor.toString()
        cvh.awayGAView.text = standing.away.goalsAgainst.toString()

        view.setOnClickListener {
            (context!!.activity!! as MainActivity).selectTeam(standing.team_id)
            (context!!.activity!! as MainActivity).onTabClicked(R.id.tabFixtures)
        }
        return view
    }


    inner class CustomViewHolder(view : View){
        var logoView = view.findViewById<ImageView>(R.id.imgTeamLogo)
        val nameView = view.findViewById<TextView>(R.id.txtTeamName)
        var rankView = view.findViewById<TextView>(R.id.txtRank)
        var pointsView = view.findViewById<TextView>(R.id.txtPoints)

        val allWinView = view.findViewById<TextView>(R.id.txtAllWin)
        val homeWinView = view.findViewById<TextView>(R.id.txtHomeWin)
        val awayWinView = view.findViewById<TextView>(R.id.txtAwayWin)

        val allLoseView = view.findViewById<TextView>(R.id.txtAllLose)
        val homeLoseView = view.findViewById<TextView>(R.id.txtHomeLose)
        val awayLoseView = view.findViewById<TextView>(R.id.txtAwayLose)

        val allDrawView = view.findViewById<TextView>(R.id.txtAllDraw)
        val homeDrawView = view.findViewById<TextView>(R.id.txtHomeDraw)
        val awayDrawView = view.findViewById<TextView>(R.id.txtAwayDraw)

        val allPlayedView = view.findViewById<TextView>(R.id.txtAllPlayed)
        val homePlayedView = view.findViewById<TextView>(R.id.txtHomePlayed)
        val awayPlayedView = view.findViewById<TextView>(R.id.txtAwayPlayed)

        val allGFView = view.findViewById<TextView>(R.id.txtAllGoal)
        val homeGFView = view.findViewById<TextView>(R.id.txtHomeGoal)
        val awayGFView = view.findViewById<TextView>(R.id.txtAwayGoal)

        val allGAView = view.findViewById<TextView>(R.id.txtAllGoalAgainst)
        val homeGAView = view.findViewById<TextView>(R.id.txtHomeGoalAgainst)
        val awayGAView = view.findViewById<TextView>(R.id.txtAwayGoalAgainst)
    }
}