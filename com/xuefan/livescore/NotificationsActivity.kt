package com.xuefan.livescore

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.messaging.FirebaseMessaging
import com.xuefan.livescore.api.ApiService
import com.xuefan.livescore.data.GlobalSettings
import com.xuefan.livescore.data.GlobalStorage
import com.xuefan.livescore.model.PlayerModel
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.activity_players.*


class NotificationsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val bitmap = (ContextCompat.getDrawable(this, R.drawable.ic_menu_back) as BitmapDrawable).bitmap
        val newdrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap,
            resources.getDimension(R.dimen.dip_20).toInt(), resources.getDimension(R.dimen.dip_20).toInt(), true))
        supportActionBar!!.setHomeAsUpIndicator(newdrawable)
        toolbar.setNavigationOnClickListener {
            this.finish()
        }
        supportActionBar!!.setTitle(R.string.menu_notification)

        btnApply.setOnClickListener {
            for (idx in 0 .. checkBoxIds.count()-1) {
                if (findViewById<CheckBox>(checkBoxIds[idx]).isChecked) {
                    subscribe(GlobalSettings.TopicNames[idx])
                } else {
                    unsubscribe(GlobalSettings.TopicNames[idx])
                }
                GlobalStorage.defStorage.notifications[idx] = findViewById<CheckBox>(checkBoxIds[idx]).isChecked
            }
            GlobalStorage.saveStorage()
        }
        btnCancel.setOnClickListener {
            for (idx in 0 .. checkBoxIds.count()-1) {
                findViewById<CheckBox>(checkBoxIds[idx]).isChecked = true
            }
        }
        for (idx in 0 .. checkBoxIds.count()-1) {
            findViewById<CheckBox>(checkBoxIds[idx]).isChecked = GlobalStorage.defStorage.notifications[idx]
        }
    }

    private var completeCount = 0
    private var checkBoxIds = intArrayOf(R.id.chkReminder, R.id.chkFirstHalf, R.id.chkSecondHalf, R.id.chkGoal, R.id.chkMatchResult)
    fun unsubscribe(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completeCount = completeCount + 1
                }
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
}
