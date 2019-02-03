package com.remotejobs.io.app.splash.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.google.firebase.messaging.FirebaseMessaging
import com.remotejobs.io.app.BuildConfig
import com.remotejobs.io.app.R
import com.remotejobs.io.app.home.view.HomeActivity
import com.remotejobs.io.app.utils.extension.launchPlayStore
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        FirebaseMessaging.getInstance().subscribeToTopic("remotejobs")

        val version = intent.getStringExtra("version")
        if (!TextUtils.isEmpty(version) && version != BuildConfig.VERSION_NAME) {
            launchPlayStore()
            finish()
        }

        textView.setOnClickListener { jump() }

        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    jump()
                }
            }
        }, 3000)
    }

    private fun jump() {
        timer.cancel()
        val contenTransition: Pair<View, String> = Pair.create(content, "content")
        val textJobsTransition: Pair<View, String> = Pair.create(textView, "textJobs")
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, contenTransition, textJobsTransition)

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent, options.toBundle())
        finish()
    }
}
