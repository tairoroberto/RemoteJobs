package com.remotejobs.io.app.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.remotejobs.io.app.R
import kotlinx.android.synthetic.main.activity_main.*

open class BaseActivity : AppCompatActivity() {

    private var currentTag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener({ item ->
            var action = "https://trmamobile/remotejobs/home"
            when (item.itemId) {
                R.id.navigation_home -> {
                    action = "https://trmamobile/remotejobs/jobs"
                }
                R.id.navigation_highestpaid -> {
                    action = "https://trmamobile/remotejobs/highestpaid"
                }
                R.id.navigation_companies -> {
                    action = "https://trmamobile/remotejobs/companies"
                }
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(action))
            intent.`package` = packageName
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            startActivity(intent)
            finish()
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
