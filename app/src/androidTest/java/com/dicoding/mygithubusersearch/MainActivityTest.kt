package com.dicoding.mygithubusersearch

import android.view.KeyEvent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.mygithubusersearch.ui.main.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    private fun selectPopupMenuItem(menuItem: String) {
        onView(withId(R.id.ibWidget)).perform(click())
        onView(withText(menuItem))
            .inRoot(isPlatformPopup())
            .perform(click())
    }

    @Test
    fun testFavoriteMenuItem() {
        selectPopupMenuItem("Favorite Users")
        onView(withId(R.id.main_favorite))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testSettingMenuItem() {
        selectPopupMenuItem("Settings")
        onView(withId(R.id.main_setting))
            .check(matches(isDisplayed()))
    }
}