package com.remotejobs.io.app.companies.view

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.View
import com.google.android.instantapps.InstantApps
import com.remotejobs.io.app.R
import com.remotejobs.io.app.utils.extension.ActivityLaunchHelper
import com.remotejobs.io.app.view.BaseActivity


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

        setNavigationListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    ActivityLaunchHelper.launchHome(this)
                }
                R.id.navigation_highestpaid -> {

                }
                R.id.navigation_companies -> {
                    ActivityLaunchHelper.launchCompanies(this)
                }
            }

            false
        })
    }
}
