package com.remotejobs.io.app.jobs.view

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.remotejobs.io.app.BuildConfig
import com.remotejobs.io.app.jobs.R
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

        val animation = videoView.drawable as AnimationDrawable
        animation.start()

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

        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
