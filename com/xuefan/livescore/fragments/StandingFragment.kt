package com.xuefan.livescore.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.R
import com.xuefan.livescore.adapters.StandingAdapter
import com.xuefan.livescore.api.APIClient
import com.xuefan.livescore.isNetworkAvailable
import com.xuefan.livescore.model.StandingModel
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.fragment_leagues.*

class StandingFragment: Fragment() {

    private var currentLeagueIdx = 0
    private var standingAdapter = StandingAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_leagues, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        standingAdapter.setContext(this)
        lstLeagues.adapter = standingAdapter
        reload(currentLeagueIdx)
    }

    public fun reload(leagueIdx: Int) {
        currentLeagueIdx = leagueIdx
        Handler().postDelayed({

            var myTask = MyTask()
            myTask.execute()
        }, 100)
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

            if (GlobalStorage.getStandings(currentLeagueIdx) != null) {
                standingAdapter.setStandings(GlobalStorage.getStandings(currentLeagueIdx))
                return "1"
            }
            if (activity!!.isNetworkAvailable()) {
                val call = APIClient.getInstance().getStandings(GlobalStorage.defStorage.leagues[currentLeagueIdx].league_id) //current 2018-2019 turkish super lig
                try {
                    val response = call.execute()
                    var res = response.body()!!.api.standings
                    var resAll: List<StandingModel> = ArrayList<StandingModel>()
                    for (r in res) {
                        resAll += r
                    }
                    standingAdapter.setStandings(resAll)
                    GlobalStorage.setStandings(currentLeagueIdx, resAll)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return "1"
            } else {
                return "0"
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            if (activity != null) {
                activity!!.progressBar.visibility = View.GONE
            }
            if (result.equals("1")) {
                standingAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, R.string.msg_no_internet, Toast.LENGTH_LONG).show()
            }
        }
    }

}