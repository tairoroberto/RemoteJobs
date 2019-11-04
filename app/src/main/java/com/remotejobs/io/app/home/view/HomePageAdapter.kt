package com.remotejobs.io.app.home.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class HomePageAdapter(private var fragments: List<Fragment>, supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun update(fragments: List<Fragment>){
        this.fragments = fragments
        notifyDataSetChanged()
    }
}