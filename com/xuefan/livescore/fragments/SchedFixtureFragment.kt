package com.xuefan.livescore.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import kotlinx.android.synthetic.main.fragment_schedules.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.opengl.ETC1.getWidth





class SchedFixtureFragment: Fragment() {

    private var currentLeagueIdx = -1
    protected var selectedDate = ""
    private var fixturesAdapter = FixturesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_schedules, container, false)
        var datesView = rootView.findViewById<GridLayout>(R.id.schedDatesView)
        var dtFormatter = SimpleDateFormat("yyyy-MM-dd")
        var lastView: TextView? = null
        var width = 0
        for (i in -6 .. 6) {
            var day = i
            var date = Date()
            date.time += day*3600*24*1000
            var dateView = inflater.inflate(R.layout.item_sched_date, datesView, false) as TextView
            dateView.text = dtFormatter.format(date)
            dateView.setOnClickListener {
                selectedDate = (it as TextView).text.toString()
                onDateChanged()
                if (lastView != null) {
                    lastView!!.setBackgroundResource(android.R.color.transparent)
                    lastView!!.setTextColor(resources.getColor(android.R.color.white))
                }
                dateView.setBackgroundResource(R.drawable.ic_round_box)
                dateView.setTextColor(resources.getColor(android.R.color.black))
                lastView = dateView
            }
            if (i == 0) {
                dateView.setBackgroundResource(R.drawable.ic_round_box)
                dateView.setTextColor(resources.getColor(android.R.color.black))
                lastView = dateView
            }
            datesView.addView(dateView)
            width += dateView.width
        }
        var  scrollView = rootView.findViewById<HorizontalScrollView>(R.id.datesScrollView)
        val handler = Handler()
        val mTabSelector = Runnable {
            scrollView.scrollTo((datesView.width/2 - scrollView.width/2).toInt(), 0)
        }
        handler.postDelayed(mTabSelector, 1000)
        return rootView
    }

    public fun setCurrentLeagueIdx(idx: Int) {
        currentLeagueIdx = idx
        forceReload = false
        onDateChanged()
    }

    var forceReload = false
    public fun reload(idx: Int) {
        currentLeagueIdx = idx
        forceReload = true
        onDateChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fixturesAdapter.setContext(this)
        fixturesAdapter.setFixtures(ArrayList<FixtureModel>())
        lstSchedFixtures.adapter = fixturesAdapter
        selectedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        //onDateChanged()
    }

    public fun onDateChanged() {
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
            if (activity!!.isNetworkAvailable()) {
                if (!forceReload && GlobalStorage.getSchedFixtures(selectedDate).count() > 0) {
                    fixturesAdapter.setFixtures(GlobalStorage.getSchedFixtures(selectedDate))
                    return "1"
                }
                val call = APIClient.getInstance().getFixturesByDate(selectedDate) //current 2018-2019 turkish super lig
                try {
                    val response = call.execute()
                    var res = response.body()!!.api.fixtures.filter { GlobalStorage.getLeagueIdxById(it.league_id) != -1 }
                    GlobalStorage.putSchedFixtures(selectedDate, res)
                    fixturesAdapter.setFixtures(GlobalStorage.getSchedFixtures(selectedDate))
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
                fixturesAdapter.showSchedule(true)
                fixturesAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, R.string.msg_no_internet, Toast.LENGTH_LONG).show()
            }
        }
    }
}