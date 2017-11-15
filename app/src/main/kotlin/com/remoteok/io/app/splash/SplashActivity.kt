package com.remoteok.io.app.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.remoteok.io.app.home.view.MainActivity
import com.crashlytics.android.Crashlytics
import com.remoteok.io.app.R
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash)

        try {
            val path = Uri.parse("android.resource://" + packageName + "/" + +R.raw.bg)
            videoView.setVideoURI(path)

            videoView.setOnCompletionListener { jump() }
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
