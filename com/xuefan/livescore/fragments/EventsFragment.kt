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
import kotlinx.android.synthetic.main.fragment_events.*

class EventsFragment: Fragment() {

    private var events = ArrayList<DetailedFixtureModel.EventInfo>()

    public fun setEvents(param: ArrayList<DetailedFixtureModel.EventInfo>) {
        events = param
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_events, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var adapter = EventListAdapter()
        lstEventsView.adapter = adapter
    }

    inner class EventListAdapter: BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.item_events, parent, false)
            } else {
                view = convertView
            }
            if (position > 0) {
                view.findViewById<TextView>(R.id.txtTimeElapsed).text = "" + events[position-1]!!.elapsed + "m"
                view.findViewById<TextView>(R.id.txtTeamName).text = events[position-1]!!.teamName
                view.findViewById<TextView>(R.id.txtPlayerName).text = events[position-1]!!.player
                view.findViewById<TextView>(R.id.txtEventType).text = events[position-1]!!.type
                view.findViewById<TextView>(R.id.txtEventDetail).text = events[position-1]!!.detail
            } else {
                view.findViewById<TextView>(R.id.txtTimeElapsed).text = resources.getString(R.string.label_elapsed)
                view.findViewById<TextView>(R.id.txtTeamName).text = resources.getString(R.string.label_teamname)
                view.findViewById<TextView>(R.id.txtPlayerName).text = resources.getString(R.string.label_player)
                view.findViewById<TextView>(R.id.txtEventType).text = resources.getString(R.string.label_eventtype)
                view.findViewById<TextView>(R.id.txtEventDetail).text = resources.getString(R.string.label_eventdetail)
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
            return events.count() + 1
        }

    }
}