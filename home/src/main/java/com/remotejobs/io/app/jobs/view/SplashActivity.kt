package com.remotejobs.io.app.jobs.view

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.TextUtils
import com.remotejobs.io.app.BuildConfig
import com.remotejobs.io.app.jobs.R
import com.remotejobs.io.app.utils.extension.launchPlayStore
import com.remotejobs.io.app.view.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val version = intent.getStringExtra("version")
        if (!TextUtils.isEmpty(version) && version != BuildConfig.VERSION_NAME) {
            launchPlayStore()
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
