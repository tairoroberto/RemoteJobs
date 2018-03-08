package com.remoteok.ui

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.com.concretesolutions.requestmatcher.InstrumentedTestRequestMatcherRule
import br.com.concretesolutions.requestmatcher.model.HttpMethod
import com.remoteok.ApplicationTest
import com.remoteok.io.app.R
import com.remoteok.io.app.view.home.HomeFragment
import com.remoteok.io.app.view.home.MainActivity
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var serverRule = InstrumentedTestRequestMatcherRule()

    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    val app = InstrumentationRegistry.getTargetContext().applicationContext as ApplicationTest

    @Before
    fun setUp() {
        activityRule.activity.supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment(), "home")
    }

    @Test
    fun sholdListJobs() {
        serverRule.addFixture(200, "remote-jobs.json")
                .ifRequestMatches()
                .methodIs(HttpMethod.GET)
                .pathMatches(CoreMatchers.endsWith("/fetchJobs"))

        activityRule.launchActivity(null)


        onView(withText("Dev")).check(matches(isDisplayed()))
    }
}