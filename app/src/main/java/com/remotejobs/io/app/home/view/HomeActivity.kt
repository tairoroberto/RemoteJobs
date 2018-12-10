package com.remotejobs.io.app.home.view

import android.os.Bundle
import android.view.View.GONE
import com.google.android.instantapps.InstantApps
import com.remotejobs.io.app.base.BaseActivity
import com.remotejobs.io.app.home.view.HomeFragment.Companion.SEARCH_PARAM

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.extras?.getString(SEARCH_PARAM) != null) {
            val fragment = HomeFragment()
            val bundle = Bundle()
            bundle.putString(SEARCH_PARAM, intent?.extras?.getString(SEARCH_PARAM))
            fragment.arguments = bundle

            replaceFragment(fragment, HOME_SEARCH)
        } else {

            if (savedInstanceState == null) {
                replaceFragment(HomeFragment(), HOME)
            }

            if (InstantApps.isInstantApp(this)) {
                replaceFragment(HomeFragment(), HOME)
                setNavigationVisibility(GONE)
            }
        }
    }
}
