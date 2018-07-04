package com.remotejobs.io.app.splash.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.messaging.FirebaseMessaging
import com.remotejobs.io.app.BuildConfig
import com.remotejobs.io.app.splash.R
import com.remotejobs.io.app.utils.extension.ActivityLaunchHelper
import com.remotejobs.io.app.utils.extension.launchPlayStore
import com.remotejobs.io.app.view.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseMessaging.getInstance().subscribeToTopic("remotejobs")

        val version = intent.getStringExtra("version")
        if (!TextUtils.isEmpty(version) && version != BuildConfig.VERSION_NAME) {
            launchPlayStore()
            finish()
        }

        lottieAnimationView.setOnClickListener { jump() }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    jump()
                }
            }
        }, 3000)
    }

    private fun jump() {
        if (isFinishing) {
            return
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("${ActivityLaunchHelper.URL_BASE}/home"))
                .addCategory(Intent.CATEGORY_DEFAULT)
                .addCategory(Intent.CATEGORY_BROWSABLE)
        intent.`package` = packageName
        startActivity(intent)
        finish()
    }
}
