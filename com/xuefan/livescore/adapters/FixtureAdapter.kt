package com.xuefan.livescore.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.xuefan.livescore.DetailsActivity
import com.xuefan.livescore.model.FixtureModel
import java.text.SimpleDateFormat
import com.xuefan.livescore.api.ApiService
import com.xuefan.livescore.R
import com.xuefan.livescore.convertUtc2Local
import com.xuefan.livescore.data.GlobalSettings
import com.xuefan.livescore.data.GlobalStorage
import java.security.acl.Group
import java.util.*
import kotlin.collections.ArrayList


open class FixturesAdapter: BaseAdapter() {

    private var leagueCount = 0
    private var fixtures: HashMap<Int, List<FixtureModel>> = HashMap<Int, List<FixtureModel>>()
    private var context: Fragment? = null
    private var showSchedule = false
    private var isLiveFixture = false
    var layoutInflater : LayoutInflater? = null

    public fun setContext(param: Fragment) {
        context = param
    }

    public fun setFixtures(param: List<FixtureModel>) {
        var fix = param.sortedWith(compareBy { -it.event_timestamp })
        fixtures.clear()
        for (fixture in fix) {
            if (GlobalStorage.getLeagueIdxById(fixture.league_id) < 0) {
                continue
            }
            if (fixtures[fixture.league_id] == null) {
                fixtures[fixture.league_id] = ArrayList<FixtureModel>()
            }
            fixtures[fixture.league_id] = fixtures[fixture.league_id]!!.plus(fixture)
        }
    }

    public fun showLive(param: Boolean) {
        isLiveFixture = param
    }

    public fun showSchedule(showSched: Boolean) {
        showSchedule = showSched
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        var cc = 0
        for (fix in fixtures) {
            cc = cc + fix.value.count() + 1
        }
        return cc
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if(layoutInflater == null)
            layoutInflater = context!!.layoutInflater
        val view : View

        var cc = 0
        var lastCc = 0
        var leagueID = -1
        var realPos = -2
        for (fix in fixtures) {
            leagueID = fix.key
            if (cc == position) {
                realPos = -1
            } else if (cc > position) {
                realPos = position - lastCc - 1
                break;
            }
            lastCc = cc
            cc += fix.value.count() + 1
        }
        if (realPos == -2) {
            realPos = position - lastCc - 1
        }

        if (realPos < 0) {
            var cvh: GroupViewHolder
            if(convertView == null || !GroupViewHolder::class.java.isInstance(convertView.tag)){
                view = layoutInflater!!.inflate(R.layout.item_league_group, parent, false)
                cvh = GroupViewHolder(view)
                view.tag = cvh
            }else{
                view = convertView
                cvh = view.tag as GroupViewHolder
            }
            cvh.leagueName.text = GlobalStorage.getLeagueNameById(leagueID)
            return view
        }

        val cvh : CustomViewHolder

        if(convertView == null || !CustomViewHolder::class.java.isInstance(convertView.tag) ){
            view = layoutInflater!!.inflate(R.layout.item_fixture, parent, false)
            cvh = CustomViewHolder(view)
            view.tag = cvh
        }else{
            view = convertView
            cvh = view.tag as CustomViewHolder
        }

        if (position%2 == 0) {
            view.setBackgroundColor(context!!.resources.getColor(R.color.opacLightBlue))
        } else {
            view.setBackgroundColor(context!!.resources.getColor(R.color.opacLightGreen))
        }
        if(fixtures[leagueID] == null || fixtures[leagueID]!!.count() <= realPos) {
            return view
        }
        val fixture = fixtures[leagueID]!![realPos]
        try {
            var assetFilePath = "turkey/superlig/" + fixture.homeTeam.team_name.substring(0,3) + ".png"
            var assetInputStream = context!!.activity!!.assets.open(assetFilePath)
            val drawable = Drawable.createFromStream(assetInputStream, null)
            cvh.homeLogo.setImageDrawable(drawable)
        }catch (ex: Exception){
            cvh.homeLogo.setImageResource(R.drawable.ic_turkey_superlig)
            Picasso.get().load(fixture.homeTeam.logo)
                .placeholder(R.drawable.ic_turkey_superlig)
                .into(cvh.homeLogo)
        }
        try {
            var assetFilePath = "turkey/superlig/" + fixture.awayTeam.team_name.substring(0,3) + ".png"
            var assetInputStream = context!!.activity!!.assets.open(assetFilePath)
            val drawable = Drawable.createFromStream(assetInputStream, null)
            cvh.awayLogo.setImageDrawable(drawable)
        }catch (ex: Exception){
            cvh.awayLogo.setImageResource(com.xuefan.livescore.R.drawable.ic_turkey_superlig)
            Picasso.get().load(fixture.awayTeam.logo)
                .placeholder(com.xuefan.livescore.R.drawable.ic_turkey_superlig)
                .into(cvh.awayLogo)
        }
        if (!isLiveFixture || !showSchedule) {
            if (fixture.statusShort == "TBD" || fixture.statusShort == "NS") {
                cvh.fixtureGoals.text = "- : -"
                cvh.fixtureStatus.visibility = View.VISIBLE
                cvh.fixtureStatus.text = fixture.status
            } else {
                cvh.fixtureGoals.text = "" + fixture.goalsHomeTeam + " : " + fixture.goalsAwayTeam
            }
        } else{
            cvh.fixtureMinutes.text = fixture.status
            //cvh.fixtureGoals.text = "- : -"
            //cvh.fixtureGoals.text = "leagueid: " + fixture.league_id;
        }

        cvh.fixtureMinutes.text = fixture.event_date.convertUtc2Local()
        cvh.homeName.text = fixture.homeTeam.team_name
        cvh.awayName.text = fixture.awayTeam.team_name
        view.setOnClickListener {
            var intent = Intent(context!!.activity, DetailsActivity::class.java)
            intent.putExtra("fixtureId", fixture.fixture_id)
            context!!.activity!!.startActivity(intent)
        }
        if (!isLiveFixture) {
            cvh.fixtureElapsed.visibility = View.GONE
            cvh.fixtureStatus.visibility = View.GONE
        } else {
            cvh.fixtureElapsed.visibility = View.VISIBLE
            cvh.fixtureElapsed.text = "" + fixture.elapsed + " minutes past"
            cvh.fixtureStatus.visibility = View.VISIBLE
            cvh.fixtureStatus.text = fixture.status
        }
        return view
    }

    inner class CustomViewHolder(view : View){
        var homeLogo = view.findViewById<ImageView>(R.id.imgHomeTeamLogo)
        val homeName = view.findViewById<TextView>(R.id.txtHomeTeamName)
        var awayLogo = view.findViewById<ImageView>(R.id.imgAwayTeamLogo)
        val awayName = view.findViewById<TextView>(R.id.txtAwayTeamName)
        var fixtureMinutes = view.findViewById<TextView>(R.id.txtFixtureMinutes)
        var fixtureElapsed = view.findViewById<TextView>(R.id.txtFixtureElapsed)
        val fixtureGoals = view.findViewById<TextView>(R.id.txtFixtureGoals)
        val fixtureStatus = view.findViewById<TextView>(R.id.txtFixtureStatus)
    }

    inner class GroupViewHolder(view: View) {
        var leagueName = view.findViewById<TextView>(R.id.txtLeagueGroupName)
    }
}