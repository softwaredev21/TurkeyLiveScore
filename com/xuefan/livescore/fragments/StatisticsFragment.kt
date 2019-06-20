package com.xuefan.livescore.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.xuefan.livescore.R
import com.xuefan.livescore.model.DetailedFixtureModel
import kotlinx.android.synthetic.main.fragment_statistics.*

class StatisticsFragment: Fragment() {

    private var stats = HashMap<String, DetailedFixtureModel.StatInfo>()

    public fun setStats(param: HashMap<String, DetailedFixtureModel.StatInfo>) {
        stats = param
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_statistics, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var adapter = StatsAdapter()
        lstView.adapter = adapter
    }

    inner class StatsAdapter: BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.item_statistics, parent, false)
            } else {
                view = convertView
            }
            if (position > 0) {
                var key = stats.keys.toList()[position-1]
                view.findViewById<TextView>(R.id.txtStatKey).text = key
                view.findViewById<TextView>(R.id.txtHomeValue).text = stats.get(key)!!.home
                view.findViewById<TextView>(R.id.txtAwayValue).text = stats.get(key)!!.away
            } else {
                view.findViewById<TextView>(R.id.txtStatKey).text = resources.getString(R.string.stats_attr)
                view.findViewById<TextView>(R.id.txtHomeValue).text = resources.getString(R.string.label_home)
                view.findViewById<TextView>(R.id.txtAwayValue).text = resources.getString(R.string.label_away)
            }
            if (position == 0) {
                view.setBackgroundColor(resources.getColor(R.color.opacLightGreen))
            } else {
                view.setBackgroundColor(resources.getColor(R.color.opacLightBlue))
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
            return stats.count() + 1
        }

    }
}