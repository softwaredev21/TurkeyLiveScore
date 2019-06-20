package com.xuefan.livescore.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.xuefan.livescore.data.GlobalSettings
import com.xuefan.livescore.R
import kotlinx.android.synthetic.main.fragment_leagues.*

class LeaguesFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_leagues, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var leaguesAdatper = LeaguesAdapter()
        lstLeagues.adapter = leaguesAdatper
        leaguesAdatper.notifyDataSetChanged()
    }

    private inner class LeaguesAdapter: BaseAdapter() {
        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return GlobalSettings.AvailableLeagues.count()
        }

        var layoutInflater : LayoutInflater? = null

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            if(layoutInflater == null)
                layoutInflater = getLayoutInflater()
            val view : View
            val cvh : CustomViewHolder

            if(convertView == null){
                view = layoutInflater!!.inflate(R.layout.item_league, parent, false)
                cvh = CustomViewHolder(view)
                view.tag = cvh
            }else{
                view = convertView
                cvh = view.tag as CustomViewHolder
            }

            val league = GlobalSettings.AvailableLeagues[position]
            //cvh.nameView.text = league.league_name
            return view
        }


        inner class CustomViewHolder(view : View){
            val nameView = view.findViewById<TextView>(R.id.txtLeagueName)
        }
    }
}