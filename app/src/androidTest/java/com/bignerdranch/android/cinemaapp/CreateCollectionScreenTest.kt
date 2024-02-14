package com.bignerdranch.android.cinemaapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateCollectionScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // Проверка видимости элементов на экране создания коллекции
    @Test
    fun createCollectionScreenViewsAreVisible() {
        // Переход к экрану создания коллекции
        onView(withId(R.id.navigation_collection)).perform(click())
        onView(withId(R.id.createCollectionButton)).perform(click())
        onView(withText("Создать коллекцию")).perform(click())

        // Проверка видимости элементов на экране создания коллекции
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.selectIconButton)).check(matches(isDisplayed()))
        onView(withId(R.id.createButton)).check(matches(isDisplayed()))
    }
}