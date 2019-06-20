package com.xuefan.livescore

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.xuefan.livescore.api.ApiService
import com.xuefan.livescore.data.GlobalSettings
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.data.TinyDB
import kotlinx.android.synthetic.main.activity_splash.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.CountDownLatch


class SplashActivity : BaseActivity() {


    private lateinit var myTask: MyTask
    private var countdownLatch : CountDownLatch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Toast.makeText(baseContext, task.result!!.token, Toast.LENGTH_SHORT).show()
                }
            })

        Handler().postDelayed({

            myTask = MyTask()
            myTask.execute()
        }, 1000)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class MyTask : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        var completeCount = 0
        override fun doInBackground(vararg params: String): String {

            // Check for Internet Connection from the static method of Helper class
            if (this@SplashActivity.isNetworkAvailable()) {
                var db = TinyDB(this@SplashActivity)
                if (!GlobalStorage.loadStorage()) {
                    GlobalStorage.create()
                    for (idx in 0 .. GlobalSettings.TopicNames.count()-1) {
                        subscribe(GlobalSettings.TopicNames[idx])
                    }
                }
                ApiService.instance.loadLeagues(2019)
                ApiService.instance.loadLeagues(2018)
                for (idx in 0 .. GlobalSettings.AvailableLeagueNames.count()-1) {
                    ApiService.instance.loadTeams(idx)
                    ApiService.instance.loadStandings(idx)
                }
                return "1"
            } else {
                return "0"
            }
        }

        fun subscribe(topic: String) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        completeCount = completeCount + 1
                    }
                }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            if (result.equals("0", ignoreCase = true)) {
                progressBar.visibility = View.GONE

            } else {
                restartActivity(MainActivity::class.java)
            }
        }

    }

    public fun testUrl() {
        val myURL = URL("https://api-football-v1.p.rapidapi.com/v2/fixtures/date/2019-06-13")
        val myURLConnection = myURL.openConnection() as HttpURLConnection
        myURLConnection.setRequestMethod("GET")
        myURLConnection.setRequestProperty("Accept", "application/json")
        myURLConnection.setRequestProperty("X-RapidAPI-Key", "e2c7c0490emsh46b61d7ad61453bp19c3e6jsn5653a6da513b")
        myURLConnection.setDoInput(true)
        myURLConnection.setDoOutput(false)
        val res = myURLConnection.responseCode
        val inputStream = myURLConnection.inputStream
        val str = String(inputStream.readBytes())
        println(str)
    }
}
