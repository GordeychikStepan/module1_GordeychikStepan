package com.bignerdranch.android.cinemaapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInScreenTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<SignInScreen> = ActivityScenarioRule(SignInScreen::class.java)

    @Test
    fun testSuccessfulLogin() {
        onView(withId(R.id.emailEditText)).perform(clearText(), typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.signInButton)).perform(click())

        Thread.sleep(3000)

        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testLoginWithEmptyFields() {
        onView(withId(R.id.signInButton)).perform(click())

        onView(withText("Заполните все поля")).check(matches(isDisplayed()))
    }

    @Test
    fun testInvalidEmailFormat() {
        onView(withId(R.id.emailEditText)).perform(clearText(), typeText("invalidemail"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.signInButton)).perform(click())

        onView(withText("Неверный формат email")).check(matches(isDisplayed()))
    }
}