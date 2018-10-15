package com.remotejobs.io.app.home

import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.requestmatcher.InstrumentedTestRequestMatcherRule
import com.remotejobs.io.app.R
import com.remotejobs.io.app.home.view.HomeActivity
import com.remotejobs.io.app.home.view.HomeFragment
import com.remotejobs.io.app.utils.extension.loadJSONFromAsset
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {
    @Rule
    @JvmField
    var serverRule = InstrumentedTestRequestMatcherRule()

    @get:Rule
    var activityRule = ActivityTestRule(HomeActivity::class.java, true, false)

    val app = InstrumentationRegistry.getTargetContext().applicationContext

    @Test
    fun isDialogRateDisplayed() {
        activityRule.launchActivity(null)
        activityRule.activity.supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment(), "home")
        activityRule.activity.getSharedPreferences("Home", Context.MODE_PRIVATE)?.edit()?.putBoolean("rated", false)?.apply()
        activityRule.activity.getSharedPreferences("Home", Context.MODE_PRIVATE)?.edit()?.putInt("ratedCount", 0)?.apply()
        onView(withText("OK")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldListJobs() {

        val server = MockWebServer()
        server.url("/")
        server.enqueue(MockResponse().setBody(app.loadJSONFromAsset("fixtures/remote-jobs.json")))

        activityRule.launchActivity(null)
        activityRule.activity.supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment(), "home")
        activityRule.activity.getSharedPreferences("Home", Context.MODE_PRIVATE)?.edit()?.putBoolean("rated", false)?.apply()
        activityRule.activity.getSharedPreferences("Home", Context.MODE_PRIVATE)?.edit()?.putInt("ratedCount", 0)?.apply()

        onView(withText("CANCEL")).check(matches(isDisplayed())).perform(click())
        onView(withText("Remote Jobs")).check(matches(isDisplayed()))
    }
}
