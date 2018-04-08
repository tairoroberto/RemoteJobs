package com.remoteok.io.app.view.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.DisplayMetrics
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.remoteok.io.app.BuildConfig
import com.remoteok.io.app.R
import com.remoteok.io.app.view.home.MainActivity
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Crashlytics, disabled for debug builds
        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit)

        setContentView(R.layout.activity_splash)

        val version = intent.getStringExtra("version")
        if (!TextUtils.isEmpty(version) && version != BuildConfig.VERSION_NAME) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${applicationContext?.packageName}"))
            startActivity(intent)
            finish()
        }

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
                    jump()
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
