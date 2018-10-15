package com.remotejobs.io.app.ui

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.requestmatcher.InstrumentedTestRequestMatcherRule
import com.remotejobs.io.app.view.BaseActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var serverRule = InstrumentedTestRequestMatcherRule()

    @get:Rule
    var activityRule = ActivityTestRule(BaseActivity::class.java, true, false)

    val app = InstrumentationRegistry.getTargetContext().applicationContext

    @Test
    fun sholdListJobs() {
        serverRule.addFixture(200, "remote-jobs.json")
                .ifRequestMatches()
                .pathMatches(CoreMatchers.endsWith("/fetchJobs"))

        activityRule.launchActivity(null)


        onView(withText("Dev")).check(matches(isDisplayed()))
    }
}