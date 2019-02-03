package com.remotejobs.io.app.home.view

import android.os.Bundle
import android.transition.ChangeBounds
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.remotejobs.io.app.R
import com.remotejobs.io.app.companies.view.CompaniesFragment
import com.remotejobs.io.app.highestpaid.view.HighestPaidFragment
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setAnimation()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar.transitionName = "textJobs"

        val fragments = ArrayList<Fragment>()
        fragments.add(HomeFragment())
        fragments.add(HighestPaidFragment())
        fragments.add(CompaniesFragment())

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        val adapter = HomePageAdapter(fragments, supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = fragments.size
    }

    private fun setAnimation() {
        val changeBounds = ChangeBounds()
        changeBounds.duration = 2000
        window.sharedElementExitTransition = changeBounds
    }
}
