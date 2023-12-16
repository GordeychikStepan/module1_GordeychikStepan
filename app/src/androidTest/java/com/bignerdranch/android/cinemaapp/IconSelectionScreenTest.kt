package com.bignerdranch.android.cinemaapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IconSelectionScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // Проверка видимости иконок
    @Test
    fun iconSelectionScreenViewsAreVisible() {
        // Переход к экрану выбора иконки
        onView(withId(R.id.navigation_collection)).perform(click())
        onView(withId(R.id.createCollectionButton)).perform(click())
        onView(withText("Создать коллекцию")).perform(click())

        Thread.sleep(500)

        onView(withId(R.id.selectIconButton)).perform(click())

        Thread.sleep(500)

        // Проверка видимости иконок (пример для первых нескольких иконок)
        onView(withId(R.id.ico1)).check(matches(isDisplayed()))
        onView(withId(R.id.ico2)).check(matches(isDisplayed()))
        onView(withId(R.id.ico3)).check(matches(isDisplayed()))
        onView(withId(R.id.ico4)).check(matches(isDisplayed()))

        onView(withId(R.id.ico5)).check(matches(isDisplayed()))
        onView(withId(R.id.ico6)).check(matches(isDisplayed()))
        onView(withId(R.id.ico7)).check(matches(isDisplayed()))
        onView(withId(R.id.ico8)).check(matches(isDisplayed()))

        onView(withId(R.id.ico9)).check(matches(isDisplayed()))
        onView(withId(R.id.ico10)).check(matches(isDisplayed()))
        onView(withId(R.id.ico11)).check(matches(isDisplayed()))
        onView(withId(R.id.ico12)).check(matches(isDisplayed()))

        onView(withId(R.id.ico13)).check(matches(isDisplayed()))
        onView(withId(R.id.ico14)).check(matches(isDisplayed()))
        onView(withId(R.id.ico15)).check(matches(isDisplayed()))
        onView(withId(R.id.ico16)).check(matches(isDisplayed()))

        onView(withId(R.id.ico17)).check(matches(isDisplayed()))
        onView(withId(R.id.ico18)).check(matches(isDisplayed()))
        onView(withId(R.id.ico19)).check(matches(isDisplayed()))
        onView(withId(R.id.ico20)).check(matches(isDisplayed()))

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.ico21)).check(matches(isDisplayed()))
        onView(withId(R.id.ico22)).check(matches(isDisplayed()))
        onView(withId(R.id.ico23)).check(matches(isDisplayed()))
        onView(withId(R.id.ico24)).check(matches(isDisplayed()))

        onView(withId(R.id.ico25)).check(matches(isDisplayed()))
        onView(withId(R.id.ico26)).check(matches(isDisplayed()))
        onView(withId(R.id.ico27)).check(matches(isDisplayed()))
        onView(withId(R.id.ico28)).check(matches(isDisplayed()))

        onView(withId(R.id.ico29)).check(matches(isDisplayed()))
        onView(withId(R.id.ico30)).check(matches(isDisplayed()))
        onView(withId(R.id.ico31)).check(matches(isDisplayed()))
        onView(withId(R.id.ico32)).check(matches(isDisplayed()))

        onView(withId(R.id.ico33)).check(matches(isDisplayed()))
        onView(withId(R.id.ico34)).check(matches(isDisplayed()))
        onView(withId(R.id.ico35)).check(matches(isDisplayed()))
        onView(withId(R.id.ico36)).check(matches(isDisplayed()))

    }
}