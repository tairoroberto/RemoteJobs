package com.remotejobs.io.app.jobs.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.View.GONE
import com.google.android.instantapps.InstantApps
import com.remotejobs.io.app.R
import com.remotejobs.io.app.utils.extension.ActivityLaunchHelper
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

        handleIntent(intent)

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData = intent.data
        if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {
            val recipeId = appLinkData.lastPathSegment
            val appData = Uri.parse("content://com.recipe_app/recipe/").buildUpon()
                    .appendPath(recipeId).build()
            //showRecipe(appData)
        }
    }
}
