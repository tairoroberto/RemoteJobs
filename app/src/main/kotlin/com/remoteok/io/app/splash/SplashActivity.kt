package com.remoteok.io.app.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import com.remoteok.io.app.home.view.MainActivity
import com.crashlytics.android.Crashlytics
import com.remoteok.io.app.R
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
import java.util.*
import android.util.DisplayMetrics


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()

        Fabric.with(fabric)

        setContentView(R.layout.activity_splash)

        try {
            val path = Uri.parse("android.resource://" + packageName + "/" + +R.raw.bg)
            videoView.setVideoURI(path)

            videoView.setOnCompletionListener { jump() }

            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            val params = ConstraintLayout.LayoutParams(metrics.widthPixels, metrics.heightPixels)
            params.bottomToBottom = content.id
            params.endToEnd = content.id
            params.startToStart = content.id
            params.topToBottom = imageView.id
            params.verticalBias = 1.0f
            videoView.layoutParams = params

            videoView.start()
        } catch (e: Exception) {
            jump()
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    startActivity<MainActivity>()
                    finish()
                }
            }
        }, 2500)
    }

    private fun jump() {
        if (isFinishing)
            return

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
