package com.xuefan.livescore

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.xuefan.livescore.data.GlobalSettings
import com.xuefan.livescore.data.GlobalStorage
import kotlinx.android.synthetic.main.activity_teams.*


class TeamDetailsActivity : BaseActivity() {

    private var currentLigIdx = 0
    private var teamsAdapter = TeamsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val bitmap = (ContextCompat.getDrawable(this, R.drawable.ic_menu_back) as BitmapDrawable).bitmap
        val newdrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap,
            resources.getDimension(R.dimen.dip_20).toInt(), resources.getDimension(R.dimen.dip_20).toInt(), true))
        supportActionBar!!.setHomeAsUpIndicator(newdrawable)
        toolbar.setNavigationOnClickListener {
            this.finish()
        }
        supportActionBar!!.setTitle(R.string.menu_teams)
        var lastView: TextView? = null
        currentLigIdx = intent.getIntExtra("ligIndex", 0)
        for (idx in 0 .. GlobalSettings.AvailableLeagueNames.count()-1 ) {
            var view = layoutInflater.inflate(R.layout.item_sched_date, gridLeagues, false) as TextView
            var leagueNames = intArrayOf(R.string.champion_lig, R.string.europa_lig, R.string.super_lig, R.string.first_lig, R.string.women_cup)
            view.text = getString(leagueNames[idx])
            view.setOnClickListener {
                currentLigIdx = idx
                teamsAdapter.notifyDataSetChanged()
                if (lastView != null) {
                    lastView!!.setBackgroundResource(android.R.color.transparent)
                    lastView!!.setTextColor(resources.getColor(android.R.color.white))
                }
                view.setBackgroundResource(R.drawable.ic_round_box)
                view.setTextColor(resources.getColor(android.R.color.black))
                lastView = view
            }
            if (idx == currentLigIdx) {
                lastView = view
                view.setBackgroundResource(R.drawable.ic_round_box)
                view.setTextColor(resources.getColor(android.R.color.black))
            }
            gridLeagues.addView(view)
        }
        lstTeams.adapter = teamsAdapter
    }

    private inner class TeamsAdapter: BaseAdapter() {
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
            if (GlobalStorage.defStorage == null) {
                return view
            }
            if (currentLigIdx < 0 || currentLigIdx >= GlobalStorage.defStorage.standings.count()) {
                return view
            }
            if (GlobalStorage.defStorage.standings[currentLigIdx].count() <= position) {
                return view
            }
            var tid = GlobalStorage.defStorage.standings[currentLigIdx].get(position).team_id
            if (GlobalStorage.defStorage.teams.get(tid) == null) {
                return view
            }
            var team = GlobalStorage.defStorage.teams.get(tid)!!
            Picasso.get().load(team.logo)
                .placeholder(R.drawable.ic_turkey_superlig)
                .into(cvh.teamLogo)
            cvh.teamName.text = team.teamName
            cvh.teamDetails.text = team.country + " " + team.venueCity
            cvh.teamFound.text = getString(R.string.label_founded) + team.founded
            if (GlobalStorage.defStorage.favoriteTeams.find { it.teamId == team.teamId } != null) {
                cvh.btnLike.setImageResource(R.drawable.ic_star)
            } else {
                cvh.btnLike.setImageResource(R.drawable.ic_star_off)
                cvh.btnLike.setOnClickListener {
                    var idx = GlobalStorage.defStorage.favoriteTeams.indexOfFirst { team.teamId == it.teamId }
                    if (idx >= 0) {
                        cvh.btnLike.setImageResource(R.drawable.ic_star_off)
                        GlobalStorage.defStorage.removeFavoriteTeam(idx)
                    } else {
                        cvh.btnLike.setImageResource(R.drawable.ic_star)
                        GlobalStorage.defStorage.addFavoriteTeam(team)
                    }
                }
            }
            cvh.btnPlayers.setOnClickListener {
                var i = Intent(baseContext, PlayerDetailsActivity::class.java)
                i.putExtra("TeamId", team.teamId)
                startActivityForResult(i, 1)
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
            return GlobalStorage.defStorage.standings[currentLigIdx].count()
        }

    }

    inner class CustomViewHolder(view : View){
        var teamLogo = view.findViewById<ImageView>(R.id.imgTeamDetailsLog)
        var teamName = view.findViewById<TextView>(R.id.txtTeamName)
        var teamDetails = view.findViewById<TextView>(R.id.txtTeamDetails)
        var teamFound = view.findViewById<TextView>(R.id.txtTeamFound)
        var btnLike = view.findViewById<ImageButton>(R.id.btnTeamLike)
        var btnPlayers = view.findViewById<ImageButton>(R.id.btnPlayers)
    }
}
