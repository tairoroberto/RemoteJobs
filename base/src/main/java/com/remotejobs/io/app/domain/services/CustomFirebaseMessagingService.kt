package com.remotejobs.io.app.domain.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.remotejobs.io.app.R


/**
 * Created by tairo on 9/8/17.
 */
class CustomFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        val intent = Intent(this, getActivityClass("com.remotejobs.io.app.jobs.view.home.SplashActivity"))

        if (remoteMessage?.data?.isEmpty() == false) {
            intent.putExtra("version", remoteMessage.data["version"])
        }

        if (remoteMessage?.notification != null) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body, intent)
        }
    }

    private fun showNotification(title: String?, body: String?, intent: Intent) {

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(this, "android")
                .setContentTitle(title)
                .setContentText(body)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder.setSound(defaultSoundUri)

        val notification = builder.build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }

    @Throws(Exception::class)
    private fun getActivityClass(target: String): Class<*>? {
        val classLoader = this.baseContext.classLoader

        return classLoader.loadClass(target)
    }
}