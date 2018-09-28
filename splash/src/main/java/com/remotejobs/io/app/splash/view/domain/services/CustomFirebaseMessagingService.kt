package com.remotejobs.io.app.splash.view.domain.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.remotejobs.io.app.R
import com.remotejobs.io.app.splash.view.SplashActivity


/**
 * Created by tairo on 9/8/17.
 */
class CustomFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        val intent = Intent(this, SplashActivity::class.java)

        if (remoteMessage?.data?.isEmpty() == false) {
            intent.putExtra("version", remoteMessage.data["version"])
        }

        if (remoteMessage?.notification != null) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body, intent)
        }
    }

    private fun showNotification(title: String?, body: String?, intent: Intent) {

        val mChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel("jobs", "jobs", NotificationManager.IMPORTANCE_DEFAULT)
            "jobs"
        } else {
            "jobs"
        }

        val builder = NotificationCompat.Builder(this, mChannel)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(true)

        val resultIntent = Intent(this, SplashActivity::class.java)

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(SplashActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(resultPendingIntent)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder.setSound(defaultSoundUri)

        val notification = builder.build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
}