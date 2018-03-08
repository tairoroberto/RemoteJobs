package com.remoteok.io.app.view.home

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.remoteok.io.app.R
import com.remoteok.io.app.view.companies.CompaniesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var currentTag = ""

    companion object {
        val HOME = "Home"
        val HIGHEST_PAID = "valHighestPaid"
        val COMPANIES = "valCompanies"
        private val STATISTICS = "valStatistics"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment(), HOME)
        }

        navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment(), HOME)
                    return@OnNavigationItemSelectedListener true
                }
            /*R.id.navigation_highestpaid -> {
                replaceFragment(HighestPaidFragment(), HIGHEST_PAID)
                return@OnNavigationItemSelectedListener true
            }*/
                R.id.navigation_companies -> {
                    replaceFragment(CompaniesFragment(), COMPANIES)
                    return@OnNavigationItemSelectedListener true
                }
            /*R.id.navigation_statistics -> {
                replaceFragment(StatisticsFragment(), STATISTICS)
                return@OnNavigationItemSelectedListener true
            }*/
            }
            false
        })
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        if (tag != currentTag) {
            currentTag = tag
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .commit()
        }
    }
}
