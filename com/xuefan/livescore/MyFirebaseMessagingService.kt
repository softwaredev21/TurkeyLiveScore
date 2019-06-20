package com.xuefan.livescore

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.PendingIntent
import android.widget.RemoteViews
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.app.NotificationChannel
import android.graphics.Color
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.data.TinyDB
import java.text.SimpleDateFormat
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    inner class MsgData(msg: RemoteMessage) {
        var type = msg.data.getValue("type")
        var label = msg.data.getValue("label")
        var hteam = msg.data.getValue("hteam")
        var ateam = msg.data.getValue("ateam")
        var score = msg.data.getValue("score")
        var desc = msg.data.getValue("desc")
        var league = msg.data.getValue("league")
        var time = msg.data.getValue("time")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        try {
            if (remoteMessage!!.getData().count() > 0) {
                var msg = MsgData(remoteMessage!!)
                var data = remoteMessage!!.data
                if (!GlobalStorage.defStorage.notifications[msg.type.toInt()]) {
                    return
                }
                var db = TinyDB(this)
                if (!GlobalStorage.loadStorage()) {
                    return
                }
                var leagueName = ""
                if (!GlobalStorage.defStorage.isFavoriteLeagueById(msg.league.toInt())) {
                    return
                } else {
                    leagueName = GlobalStorage.defStorage.getFavoriteLeagueNameById(msg.league.toInt())
                }
                if (GlobalStorage.defStorage.favoriteTeams.find { it.teamId == msg.hteam.toInt() || it.teamId == msg.ateam.toInt() } == null) {
                    return
                }

                val `when` = System.currentTimeMillis()
                val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                var contentViewBig = RemoteViews(packageName, R.layout.item_notification_big)
                var contentViewSmall = RemoteViews(packageName, R.layout.item_notification)
                var time = msg.time.convertUtc2Local()
                contentViewSmall.setTextViewText(R.id.txtNotificationType, msg.label)
                contentViewSmall.setTextViewText(R.id.txtScore, msg.score)
                contentViewSmall.setTextViewText(R.id.txtDescription, msg.desc)
                contentViewSmall.setTextViewText(R.id.txtScoreTime, time)
                contentViewSmall.setTextViewText(R.id.txtLeagueName, leagueName)
                contentViewBig.setTextViewText(R.id.txtNotificationType, msg.label)
                contentViewBig.setTextViewText(R.id.txtScore, msg.score)
                contentViewBig.setTextViewText(R.id.txtDescription, msg.desc)
                contentViewBig.setTextViewText(R.id.txtScoreTime, time)
                contentViewBig.setTextViewText(R.id.txtLeagueName, leagueName)
                val notificationIntent = Intent(applicationContext, SplashActivity::class.java)
                val contentIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)

                val CHANNEL_ID = remoteMessage.data.getValue("type").toString()
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    val name = "my_channel"
                    val Description = "This is my channel"
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
                    mChannel.description = Description
                    mChannel.enableLights(true)
                    mChannel.lightColor = Color.RED
                    mChannel.enableVibration(true)
                    mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                    mChannel.setShowBadge(false)
                    mNotificationManager.createNotificationChannel(mChannel)
                }
                val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_goal)
                    .setCustomContentView(contentViewBig)
                    .setCustomBigContentView(contentViewBig)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setWhen(`when`)

                mNotificationManager.notify(1, notificationBuilder.build())
            }
        } catch (t: Throwable) {
            Log.d("MYFCMLIST", "Error parsing FCM message", t)
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        //Toast.makeText(this, token, Toast.LENGTH_SHORT).show()

    }
}