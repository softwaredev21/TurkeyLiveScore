package com.xuefan.livescore.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.R
import com.xuefan.livescore.adapters.FixturesAdapter
import com.xuefan.livescore.api.APIClient
import com.xuefan.livescore.api.ApiService
import com.xuefan.livescore.isNetworkAvailable
import com.xuefan.livescore.model.FixtureModel
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_fixtures.*

class FixtureFragment: Fragment(), AdapterView.OnItemSelectedListener {

    protected var currentLeagueIdx = 0
    protected var teamLeft = 0
    protected var teamRight = 0
    protected var firstLoad = true
    private var fixturesAdapter = FixturesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_fixtures, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fixturesAdapter.setContext(this)
        lstFixtures.adapter = fixturesAdapter
        spinTeamLeft.onItemSelectedListener = this
        spinTeamRight.onItemSelectedListener = this
        spinTeamLeft.adapter = ArrayAdapter<String>(activity, R.layout.item_team_label, GlobalStorage.getTeamNames(currentLeagueIdx))
        spinTeamRight.adapter = ArrayAdapter<String>(activity, R.layout.item_team_label, GlobalStorage.getTeamNames(currentLeagueIdx))
        reload()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (firstLoad) {
            firstLoad = false
            return
        }
        var teamId = -1
        if (position > 0) {
            teamId = GlobalStorage.getTeamId(currentLeagueIdx, position-1)
        }
        if (parent == spinTeamLeft) {
            teamLeft = position
        } else {
            teamRight = position
        }
        reload()
    }

    public fun setLeagueIndex(ligIdx: Int) {
        currentLeagueIdx = ligIdx
        teamLeft = 1
        teamRight = 2
        reload()
    }

    public fun setLeagueIndex(ligIdx: Int, teamId: Int) {
        currentLeagueIdx = ligIdx
        var team = GlobalStorage.defStorage.standings[currentLeagueIdx].indexOfFirst { it.team_id == teamId }
        if (team >= 0) {
            teamLeft = team + 1
        } else {
            teamLeft = 0
        }
        teamRight = 0
    }

    public fun reload() {
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
                var res : List<FixtureModel>? = null
                if (teamLeft == 0 && teamRight > 0) {
                    res = ApiService.instance.loadTeamFixtures(GlobalStorage.getTeamId(currentLeagueIdx, teamRight-1))
                } else if (teamLeft > 0 && teamRight == 0) {
                    res = ApiService.instance.loadTeamFixtures(GlobalStorage.getTeamId(currentLeagueIdx, teamLeft-1))
                } else if (teamLeft == 0 && teamRight == 0) {
                    res = ApiService.instance.loadFixtures(currentLeagueIdx)
                }else {
                    res = ApiService.instance.getH2HFixtures(currentLeagueIdx, GlobalStorage.getTeamId(currentLeagueIdx, teamLeft-1), GlobalStorage.getTeamId(currentLeagueIdx, teamRight-1))
                }
                if (res != null) {
                    fixturesAdapter.setFixtures(res!!)
                    return "1"
                }
            //}
            return "0"
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            if (activity != null) {
                activity!!.progressBar.visibility = View.GONE
            }
            if (result.equals("1")) {
                fixturesAdapter.notifyDataSetChanged()
                if (spinTeamLeft != null) {
                    spinTeamLeft.setSelection(teamLeft)
                }
                if (spinTeamRight != null) {
                    spinTeamRight.setSelection(teamRight)
                }
            } else {
                //Toast.makeText(activity, R.string.msg_no_internet, Toast.LENGTH_LONG).show()
            }
        }
    }

}
