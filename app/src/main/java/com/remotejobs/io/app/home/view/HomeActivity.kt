package com.remotejobs.io.app.home.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.ChangeClipBounds
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.remotejobs.io.app.R
import com.remotejobs.io.app.categories.view.CategoriesFragment
import com.remotejobs.io.app.companies.view.CompaniesFragment
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setAnimation()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val fragments = ArrayList<Fragment>()
        fragments.add(HomeFragment())
        fragments.add(CategoriesFragment())
        fragments.add(CompaniesFragment())

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        var adapter = HomePageAdapter(fragments, supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = fragments.size

        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                viewPager.setCurrentItem(0, true)
                val newText = editable.toString()

                if (newText.length >= 3) {
                    fragments[0] = HomeFragment.newInstance(newText)
                    adapter = HomePageAdapter(fragments, supportFragmentManager)
                    viewPager.adapter = adapter
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = 0
        }
    }

    private fun setAnimation() {
        val changeBounds = ChangeClipBounds()
        changeBounds.duration = 2000
        window.sharedElementExitTransition = changeBounds
    }
}
