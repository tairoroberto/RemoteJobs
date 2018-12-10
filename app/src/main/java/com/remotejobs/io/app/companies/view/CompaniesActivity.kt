package com.remotejobs.io.app.companies.view

import android.os.Bundle
import android.view.View
import com.google.android.instantapps.InstantApps
import com.remotejobs.io.app.base.BaseActivity

class CompaniesActivity : BaseActivity() {

    companion object {
        val HOME = "Comapanies"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            replaceFragment(CompaniesFragment(), HOME)
        }

        if (InstantApps.isInstantApp(this)) {
            replaceFragment(CompaniesFragment(), HOME)
            setNavigationVisibility(View.GONE)
        }
    }
}
