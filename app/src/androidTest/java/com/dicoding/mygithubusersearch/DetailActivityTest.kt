package com.dicoding.mygithubusersearch

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.mygithubusersearch.ui.main.MainActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testTapFirstItemInRecyclerView() {
        onView(withId(R.id.rvUser)).check(matches(isDisplayed()))
        Thread.sleep(4000)
        onView(withId(R.id.rvUser)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        Thread.sleep(4000)
        onView(withId(R.id.detail)).check(matches(isDisplayed()))
        onView(withId(R.id.tvLogin)).check(matches(withText("user")))
    }
}