package com.xuefan.livescore

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.fragments.*
import kotlinx.android.synthetic.main.content_main.*
import android.view.WindowManager
import android.view.Window.FEATURE_NO_TITLE
import android.os.Build
import android.text.Html


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    val REQUEST_TEAMS = 1
    val REQUEST_NOTIFICATIONS = 2

    val tabIds = intArrayOf(R.id.tabSchedule,R.id.tabLivescore,R.id.tabStandings,  R.id.tabFixtures, R.id.tabFavorites);
    val imgTabIds = intArrayOf(R.id.imgTabSchedule, R.id.imgTabLiveScore,R.id.imgTabStandings, R.id.imgTabFixtures, R.id.imgTabFavorites);
    val txtTabIds = intArrayOf(R.id.txtTabSchedule, R.id.txtTabLiveScore,R.id.txtTabStandings, R.id.txtTabFixtures, R.id.txtTabFavorites);
    var ligNames = intArrayOf(
        R.string.champion_lig,
        R.string.europa_lig,
        R.string.super_lig,
        R.string.first_lig,
        R.string.women_cup
    )

    private var currentLeagueIndex = 0
    private var currentTeamId: Int = -1
    private var optionMenu: Menu? = null

    private var schedFixtureFragment: SchedFixtureFragment? = null
    private var liveFixtureFragment: LiveFixtureFragment? = null
    private var fixtureFragment: FixtureFragment? = null
    private var standingsFragment: StandingFragment? = null
    private var favoritesFragment: FavoritesFragment? = null
    private var selectedFragment = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = Html.fromHtml("<small>" + getString(R.string.app_title) + "</small>")

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupTabs()
        progressBar.visibility = View.GONE
        navView.setNavigationItemSelectedListener(this)

        //loadLiveFixtureFragment(false)
        loadSchedFixtureFragment(false)
    }

    private fun setupTabs() {
        for (id in tabIds) {
            var tab = findViewById<View>(id)
            tab.setOnClickListener {
                onTabClicked(it.id)
            }
        }
    }

    public fun selectTeam(team: Int) {
        currentTeamId = team
    }

    public fun refreshFragments() {
        //supportActionBar!!.setTitle(getString(ligNames[currentLeagueIndex]))
        if (selectedFragment == 3) {
            onTabClicked(R.id.tabFixtures)
        } else {
            onTabClicked(R.id.tabStandings)
        }
    }

    public fun onTabClicked(tabId: Int) {
        var i = 0
        if (progressBar != null) {
            progressBar.visibility = View.INVISIBLE
        }
        for(id in tabIds) {
            if(id == tabId) {
                findViewById<ImageView>(imgTabIds[i]).setColorFilter(resources.getColor(android.R.color.holo_orange_light))
                findViewById<TextView>(txtTabIds[i]).setTextColor(resources.getColor(android.R.color.holo_orange_light))
            } else {
                findViewById<ImageView>(imgTabIds[i]).setColorFilter(resources.getColor(android.R.color.white))
                findViewById<TextView>(txtTabIds[i]).setTextColor(resources.getColor(android.R.color.white))
            }
            i = i + 1
        }
        if (tabId == R.id.tabLivescore) {
            supportActionBar!!.setTitle(R.string.menu_score)
            loadLiveFixtureFragment()
            selectedFragment = 1
        } else if (tabId == R.id.tabSchedule) {
            supportActionBar!!.setTitle(R.string.menu_schedule)
            loadSchedFixtureFragment(true)
            selectedFragment = 0
        } else if (tabId == R.id.tabFixtures) {
            supportActionBar!!.title = Html.fromHtml("<small>" + getString(R.string.menu_fixtures) + " - " + getString(ligNames[currentLeagueIndex]) + "</small>")
            loadFixtureFragment(currentTeamId)
            selectedFragment = 3
        } else if (tabId == R.id.tabStandings) {
            supportActionBar!!.title = Html.fromHtml("<small>" + getString(R.string.menu_standing) + " - " + getString(ligNames[currentLeagueIndex]) + "</small>")
            loadStandingFragment();
            selectedFragment = 2
        } else if (tabId == R.id.tabFavorites) {
            supportActionBar!!.setTitle(R.string.menu_favorites)
            loadFavoritesFragment();
            selectedFragment = 4
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        if (!GlobalStorage.defStorage.isFavoriteLeague(currentLeagueIndex)) {
            menu.getItem(0).setIcon(R.drawable.ic_star_off)
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_star)
        }
        optionMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh) {
            if (selectedFragment == 1 && liveFixtureFragment != null) {
                liveFixtureFragment!!.reload(currentLeagueIndex)
            }
            if (selectedFragment == 0 && schedFixtureFragment != null) {
                schedFixtureFragment!!.reload(currentLeagueIndex)
            }
        } else if (item.itemId == R.id.menu_like) {
            GlobalStorage.defStorage.setFavoriteLeague(currentLeagueIndex, !GlobalStorage.defStorage.isFavoriteLeague(currentLeagueIndex))
            updateMenu()
            if (selectedFragment == 4) {
                runOnUiThread {
                    favoritesFragment!!.refresh()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getLeagueName(): String{
        if (currentLeagueIndex == 0) {
            return resources.getString(R.string.super_lig)
        } else if (currentLeagueIndex == 1) {
            return resources.getString(R.string.first_lig)
        } else if (currentLeagueIndex == 2) {
            return resources.getString(R.string.second_lig)
        }
        return ""
    }

    fun getSubTitle(): String{
        if (selectedFragment == 1) {
            return resources.getString(R.string.menu_standing)
        } else if (selectedFragment == 2) {
            return resources.getString(R.string.menu_fixtures)
        }
        return resources.getString(R.string.app_title)
    }

    fun loadFixtureFragment(teamId: Int) {
        fixtureFragment = FixtureFragment()
        if (teamId >= 0) {
            fixtureFragment!!.setLeagueIndex(currentLeagueIndex, teamId)
        } else {
            fixtureFragment!!.setLeagueIndex(currentLeagueIndex)
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, fixtureFragment!!, fixtureFragment!!.javaClass.simpleName)
            .commit()
    }

    fun loadFavoritesFragment() {
        if (favoritesFragment == null) {
            favoritesFragment = FavoritesFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, favoritesFragment!!, favoritesFragment!!.javaClass.simpleName)
            .commit()
        favoritesFragment!!.refresh()
    }

    private fun loadSchedFixtureFragment(replace: Boolean = false) {
        if (schedFixtureFragment == null) {
            schedFixtureFragment = SchedFixtureFragment()
        }
        schedFixtureFragment!!.setCurrentLeagueIdx(currentLeagueIndex)
        if (replace) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, schedFixtureFragment!!, schedFixtureFragment!!.javaClass.simpleName)
                .commit()
        }else {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, schedFixtureFragment!!, schedFixtureFragment!!.javaClass.simpleName)
                .commit()
        }
    }

    private fun loadLiveFixtureFragment() {
        if (liveFixtureFragment == null) {
            liveFixtureFragment = LiveFixtureFragment()
        }
        liveFixtureFragment!!.reload(currentLeagueIndex)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, liveFixtureFragment!!, liveFixtureFragment!!.javaClass.simpleName)
            .commit()
    }

    private fun loadStandingFragment() {
        if (standingsFragment == null) {
            standingsFragment = StandingFragment()
        } else {
            standingsFragment!!.reload(currentLeagueIndex)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, standingsFragment!!, standingsFragment!!.javaClass.simpleName)
            .commit()
    }

    fun updateMenu() {
        if (optionMenu != null) {
            if (!GlobalStorage.defStorage.isFavoriteLeague(currentLeagueIndex)) {
                optionMenu!!.getItem(0).setIcon(R.drawable.ic_star_off)
            } else {
                optionMenu!!.getItem(0).setIcon(R.drawable.ic_star)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_champion_league -> {
                currentLeagueIndex = 0
                refreshFragments()
                updateMenu()
            }
            R.id.nav_europa_league -> {
                currentLeagueIndex = 1
                refreshFragments()
                updateMenu()
            }
            R.id.nav_turkey_super_league -> {
                currentLeagueIndex = 2
                refreshFragments()
                updateMenu()
            }
            R.id.nav_turkey_lig_first -> {
                currentLeagueIndex = 3
                refreshFragments()
                updateMenu()
            }
            R.id.nav_turkey_lig_second -> {
                currentLeagueIndex = 4
                refreshFragments()
                updateMenu()

            }
            R.id.nav_teams -> {
                var intent = Intent(this, TeamDetailsActivity::class.java)
                intent.putExtra("ligIndex", currentLeagueIndex)
                //startActivity(intent)
                startActivityForResult(intent, REQUEST_TEAMS)
            }
            R.id.nav_notification -> {
                var intent = Intent(this, NotificationsActivity::class.java)
                startActivityForResult(intent, REQUEST_NOTIFICATIONS)

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TEAMS) {
            if (selectedFragment == 4) {
                favoritesFragment!!.refresh()
            }
        }
        if (requestCode == REQUEST_NOTIFICATIONS) {

        }
    }
}
