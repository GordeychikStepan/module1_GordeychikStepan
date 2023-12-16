package com.bignerdranch.android.cinemaapp

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
class CreateCollectionEndToEndTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun createCollection_process() {
        // Очистка SharedPreferences с коллекциями
        val sharedPreferences = InstrumentationRegistry.getInstrumentation().targetContext
            .getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().commit()

        // Переход к экрану коллекций и создание новой коллекции
        onView(withId(R.id.navigation_collection)).perform(click())

        onView(withId(R.id.createCollectionButton)).perform(click())
        onView(withText("Создать коллекцию")).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.nameEditText)).perform(typeText("My New Collection"), closeSoftKeyboard())

        // Открытие экрана выбора иконки
        onView(withId(R.id.selectIconButton)).perform(click())

        Thread.sleep(500)

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.ico36)).perform(click())

        // Возврат
        onView(withId(R.id.createButton)).perform(click())

        // Проверка наличия новой коллекции на экране коллекций
        onView(withId(R.id.collectionsRecyclerView))
            .check(matches(hasDescendant(withText("My New Collection"))))
    }
}