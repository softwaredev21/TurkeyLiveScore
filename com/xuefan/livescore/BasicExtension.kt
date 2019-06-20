package com.xuefan.livescore

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*


fun Activity.isNetworkAvailable() : Boolean{
    val connectivity = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    if(connectivity != null){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val networks = connectivity.allNetworks
            var networkInfo: NetworkInfo

            for (mNetwork in networks) {

                networkInfo = connectivity.getNetworkInfo(mNetwork)

                if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }

        } else {
            val info = connectivity.allNetworkInfo

            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
    }

    return false
}

fun String.convertUtc2Local() : String {
    var dt1 = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
    dt1.timeZone = TimeZone.getTimeZone("UTC")
    var dt = dt1.parse(this.substring(0,20))
    dt1 = SimpleDateFormat("MMM d yyyy, hh:mm a")
    dt1.timeZone =  TimeZone.getDefault()
    var dtstr = dt1.format(dt)
    return dtstr
}