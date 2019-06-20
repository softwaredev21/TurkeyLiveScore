package com.xuefan.livescore

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.xuefan.livescore.api.APIClient
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.fragments.EventsFragment
import com.xuefan.livescore.fragments.LineupFragment
import com.xuefan.livescore.fragments.StatisticsFragment
import com.xuefan.livescore.model.DetailedFixtureModel
import com.xuefan.livescore.model.packet.DetailedFixturesResponse
import kotlinx.android.synthetic.main.activity_details.*
import java.text.SimpleDateFormat


class DetailsActivity : BaseActivity() {

    var fixtureId: Int = -1
    var detailedFixture: DetailedFixtureModel =
        DetailedFixtureModel()
    var lineupFragment = LineupFragment()
    var eventsFragment = EventsFragment()
    var statisticsFragment = StatisticsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

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

        fixtureId = intent.getIntExtra("fixtureId", -1)
        if (fixtureId>0) {
            Handler().postDelayed({

                var myTask = MyTask()
                myTask.execute()
            }, 1000)
            initFragments()
        } else {
            Toast.makeText(this, "can't find fixture", Toast.LENGTH_SHORT)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.details, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

    inner class CustomViewHolder(view : View){
        var homeLogo = view.findViewById<ImageView>(R.id.imgHomeTeamLogo)
        val homeName = view.findViewById<TextView>(R.id.txtHomeTeamName)
        var awayLogo = view.findViewById<ImageView>(R.id.imgAwayTeamLogo)
        val awayName = view.findViewById<TextView>(R.id.txtAwayTeamName)
        var fixtureMinutes = view.findViewById<TextView>(R.id.txtFixtureMinutes)
        val fixtureGoals = view.findViewById<TextView>(R.id.txtFixtureGoals)
    }

    fun initFragments() {
        val lineupColor = resources.getColor(R.color.opacLightGray1)
        val eventsColor = resources.getColor(R.color.opacLightGray2)
        val statisticsColor = resources.getColor(R.color.opacLightGray3)
        btnLineup.setOnClickListener {
            btnLineup.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
            btnEvents.setBackgroundColor(eventsColor)
            btnStatistics.setBackgroundColor(statisticsColor)
            supportFragmentManager.beginTransaction().replace(R.id.container, lineupFragment, lineupFragment.javaClass.simpleName)
                .commit()
        }

        btnEvents.setOnClickListener {
            btnEvents.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
            btnLineup.setBackgroundColor(lineupColor)
            btnStatistics.setBackgroundColor(statisticsColor)
            supportFragmentManager.beginTransaction().replace(R.id.container, eventsFragment, eventsFragment.javaClass.simpleName)
                .commit()
        }

        btnStatistics.setOnClickListener {
            btnStatistics.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
            btnLineup.setBackgroundColor(lineupColor)
            btnEvents.setBackgroundColor(eventsColor)
            supportFragmentManager.beginTransaction().replace(R.id.container, statisticsFragment, statisticsFragment.javaClass.simpleName)
                .commit()
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class MyTask : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            loadingBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String): String {

            // Check for Internet Connection from the static method of Helper class
            if (isNetworkAvailable()) {
                loadDetailedFixture()
                return "1"
            } else {
                return "0"
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            loadingBar.visibility = View.GONE
            layoutMask.visibility = View.GONE
            if (detailedFixture.lineups != null) {
                var key0 = detailedFixture.lineups.keys.toList()[0]
                var key1 = detailedFixture.lineups.keys.toList()[1]
                lineupFragment.setLineup(detailedFixture.lineups.getValue(key0), detailedFixture.lineups.getValue(key1))
            }
            if (detailedFixture.statistics != null) {
                statisticsFragment.setStats(detailedFixture.statistics)
            }
            if (detailedFixture.events != null) {
                eventsFragment.setEvents(detailedFixture.events)
            }

            supportFragmentManager.beginTransaction().add(R.id.container, lineupFragment, lineupFragment.javaClass.simpleName)
                .commit()
            val view = findViewById<View>(R.id.layoutFixture)
            val cvh = CustomViewHolder(view)
            val fixture = detailedFixture
            try {
                var assetFilePath = "turkey/superlig/" + fixture.homeTeam.team_name.substring(0,3) + ".png"
                var assetInputStream = assets.open(assetFilePath)
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
                var assetInputStream = assets.open(assetFilePath)
                val drawable = Drawable.createFromStream(assetInputStream, null)
                cvh.awayLogo.setImageDrawable(drawable)
            }catch (ex: Exception){
                cvh.awayLogo.setImageResource(R.drawable.ic_turkey_superlig)
                Picasso.get().load(fixture.awayTeam.logo)
                    .placeholder(R.drawable.ic_turkey_superlig)
                    .into(cvh.awayLogo)
            }
            cvh.fixtureGoals.text = "" + fixture.goalsHomeTeam + " : " + fixture.goalsAwayTeam
            var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            var dt = simpleDateFormat.parse(fixture.event_date.substring(0, 20).replace("T", " "))
            simpleDateFormat = SimpleDateFormat("MMM d, yyyy h:m")
            cvh.fixtureMinutes.text = simpleDateFormat.format(dt)
            cvh.homeName.text = fixture.homeTeam.team_name
            cvh.awayName.text = fixture.awayTeam.team_name
        }

        fun loadDetailedFixture() {
            val call = APIClient.getInstance().getFixture(fixtureId) //current 2018-2019 turkish super lig
            try {
                val response = call.execute()
                detailedFixture = (response.body() as DetailedFixturesResponse).api.fixtures[0]
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}
