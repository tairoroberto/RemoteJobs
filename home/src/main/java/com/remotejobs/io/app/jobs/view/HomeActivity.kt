package com.remotejobs.io.app.jobs.view

import android.os.Bundle
import android.view.View.GONE
import com.google.android.instantapps.InstantApps
import com.remotejobs.io.app.view.BaseActivity

class HomeActivity : BaseActivity() {

    companion object {
        val HOME = "Home"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment(), HOME)
        }

        if (InstantApps.isInstantApp(this)) {
            replaceFragment(HomeFragment(), HOME)
            setNavigationVisibility(GONE)
        }
    }
}
