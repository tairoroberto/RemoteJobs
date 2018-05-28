package com.remotejobs.io.app.view

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.remotejobs.io.app.R
import com.remotejobs.io.app.utils.extension.ActivityLaunchHelper
import com.remotejobs.io.app.utils.extension.ActivityLaunchHelper.Companion.companiesIntent
import com.remotejobs.io.app.utils.extension.ActivityLaunchHelper.Companion.highestpaidIntent
import com.remotejobs.io.app.utils.extension.ActivityLaunchHelper.Companion.homeIntent
import kotlinx.android.synthetic.main.activity_main.*

open class BaseActivity : AppCompatActivity() {

    private var currentTag = ""

    companion object {
        val HOME = "home"
        val HIGHEST_PAID = "highest_paid"
        val COMPANIES = "companies"
        val STATISTICS = "statistics"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigationListener()
    }

    fun setNavigationListener(){
        navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    ActivityLaunchHelper.launchActivity(this, homeIntent(this), null, HOME)
                }
                R.id.navigation_highestpaid -> {
                    ActivityLaunchHelper.launchActivity(this, highestpaidIntent(this), null, HIGHEST_PAID)
                }
                R.id.navigation_companies -> {
                    ActivityLaunchHelper.launchActivity(this, companiesIntent(this), null, COMPANIES)
                }
            }

            false
        })
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        if (tag != currentTag) {
            currentTag = tag
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .commit()
        }
    }

    fun addFragment(fragment: Fragment, tag: String) {
        if (tag != currentTag) {
            currentTag = tag
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .commit()
        }
    }

    fun setNavigationVisibility(visibility: Int) {
        navigation.visibility = visibility
    }
}