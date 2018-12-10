package com.remotejobs.io.app.highestpaid.view

import android.os.Bundle
import android.view.View
import com.google.android.instantapps.InstantApps
import com.remotejobs.io.app.base.BaseActivity

class HighestActivity : BaseActivity() {

    companion object {
        val HOME = "Comapanies"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            replaceFragment(HighestPaidFragment(), HOME)
        }

        if (InstantApps.isInstantApp(this)) {
            replaceFragment(HighestPaidFragment(), HOME)
            setNavigationVisibility(View.GONE)
        }
    }
}
