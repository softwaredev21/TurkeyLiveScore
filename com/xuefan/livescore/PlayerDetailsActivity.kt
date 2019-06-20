package com.xuefan.livescore

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.xuefan.livescore.api.ApiService
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.model.PlayerModel
import kotlinx.android.synthetic.main.activity_players.*


class PlayerDetailsActivity : BaseActivity() {

    private var teamId = -1
    private var players: List<PlayerModel>  = ArrayList<PlayerModel>()
    private var playersAdapter = PlayersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_players)

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
        supportActionBar!!.setTitle(R.string.menu_players)

        teamId = intent.getIntExtra("TeamId", -1)
        lstDetailPlayers.adapter = playersAdapter

        if (teamId >= 0) {
            Handler().postDelayed({

                var myTask = MyTask()
                myTask.execute()
            }, 1000)
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class MyTask : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            if (loadingBar != null) {
                loadingBar.visibility = View.VISIBLE
            }
        }

        override fun doInBackground(vararg params: String): String {

            // Check for Internet Connection from the static method of Helper class
            if (isNetworkAvailable()) {
                var p = ApiService.instance.loadPlayers(teamId)
                if (p != null) {
                    players = p
                    return "1"
                }
            }
            return "0"
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            playersAdapter.notifyDataSetChanged()
            if (loadingBar != null) {
                loadingBar.visibility = View.GONE
            }
        }
    }


    private inner class PlayersAdapter: BaseAdapter() {
        var lastView: LinearLayout? = null
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var cvh: CustomViewHolder
            var view: View
            if(convertView == null){
                view = layoutInflater.inflate(R.layout.item_player_details, parent, false)
                cvh = CustomViewHolder(view)
                view.tag = cvh
            }else{
                view = convertView
                cvh = view.tag as CustomViewHolder
            }
            var player = players[position]

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
                var idx = GlobalStorage.defStorage.favoritePlayers.indexOfFirst { player.playerId == it.playerId }
                if (idx >= 0) {
                    cvh.like.setImageResource(R.drawable.ic_star_off)
                    GlobalStorage.defStorage.removeFavoritePlayer(idx)
                } else {
                    cvh.like.setImageResource(R.drawable.ic_star)
                    GlobalStorage.defStorage.addFavoritePlayer(player)
                }
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
            return players.count()
        }

    }

    inner class CustomViewHolder(view : View){
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
