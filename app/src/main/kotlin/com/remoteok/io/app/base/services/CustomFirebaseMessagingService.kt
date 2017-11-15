package com.remoteok.io.app.base.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import com.remoteok.io.app.splash.SplashActivity

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.remoteok.io.app.R

/**
 * Created by tairo on 9/8/17.
 */
class CustomFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage?.data?.isNotEmpty() == true) {

            val parametroScreen = remoteMessage.data["screen"]

            if (!TextUtils.isEmpty(parametroScreen)) {
                showNotification("screen", parametroScreen)
            }
        }

        if (remoteMessage?.notification != null) {
            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, SplashActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder.setSound(defaultSoundUri)

        val notification = builder.build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
}