package com.xuefan.livescore.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xuefan.livescore.api.APIClient
import com.xuefan.livescore.api.ApiService
import com.xuefan.livescore.isNetworkAvailable
import com.xuefan.livescore.data.GlobalSettings
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.R
import com.xuefan.livescore.adapters.FixturesAdapter
import com.xuefan.livescore.model.FixtureModel
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.fragment_fixtures.*
import kotlinx.android.synthetic.main.fragment_live_fixtures.*
import java.util.*
import kotlin.collections.ArrayList



class LiveFixtureFragment: Fragment() {

    private var currentLeagueIdx = -1
    private var fixturesAdapter = FixturesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_live_fixtures, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fixturesAdapter.setContext(this)
        fixturesAdapter.setFixtures(ArrayList<FixtureModel>())
        lstLiveFixtures.adapter = fixturesAdapter
        fixturesAdapter.showLive(true)
    }

    public fun reload(leagueIdx: Int) {
        currentLeagueIdx = leagueIdx
        Handler().postDelayed({

            var myTask = MyTask()
            myTask.execute()
        }, 1000)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class MyTask : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            if (activity != null) {
                activity!!.progressBar.visibility = View.VISIBLE
            }
        }

        override fun doInBackground(vararg params: String): String {

            // Check for Internet Connection from the static method of Helper class
            //if (activity!!.isNetworkAvailable()) {
                val call = APIClient.getInstance().getLiveFixtures() //current 2018-2019 turkish super lig
                try {
                    val response = call.execute()
                    var res = response.body()!!.api.fixtures.filter { GlobalStorage.getLeagueIdxById(it.league_id) != -1 }
                    fixturesAdapter.setFixtures(res)
                    return "1"
                } catch (e: Exception) {
                    e.printStackTrace()
                    return "0"
                }
            //} else {
            //}
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            if (activity != null) {
                activity!!.progressBar.visibility = View.GONE
            }
            if (result.equals("1")) {
                fixturesAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, R.string.msg_no_internet, Toast.LENGTH_LONG).show()
            }
        }
    }
}