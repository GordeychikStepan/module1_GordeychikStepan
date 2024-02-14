package com.bignerdranch.android.cinemaapp

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
class SignUpScreenTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(SignUpScreen::class.java)

    @Test
    fun signUpScreenElementsAreVisible() {
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.surnameEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()))

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.re_passwordEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))
        onView(withId(R.id.iHaveAccountButton)).check(matches(isDisplayed()))
    }

    @Test
    fun checkEmptyFieldsValidation() {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.registerButton)).perform(click())

        onView(withText("Заполните все поля для регистрации.")).check(matches(isDisplayed()))
    }

    @Test
    fun checkEmptyNameValidation() {
        onView(withId(R.id.nameEditText)).perform(clearText())
        onView(withId(R.id.surnameEditText)).perform(clearText(), typeText("Gordeychik"), closeSoftKeyboard())
        onView(withId(R.id.emailEditText)).perform(clearText(), typeText("test@example.com"), closeSoftKeyboard())

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.re_passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())
        onView(withText("Заполните все поля для регистрации.")).check(matches(isDisplayed()))
    }

    @Test
    fun checkEmptySurnameValidation() {
        onView(withId(R.id.nameEditText)).perform(clearText(), typeText("Stepan"), closeSoftKeyboard())
        onView(withId(R.id.surnameEditText)).perform(clearText())
        onView(withId(R.id.emailEditText)).perform(clearText(), typeText("test@example.com"), closeSoftKeyboard())

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.re_passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())
        onView(withText("Заполните все поля для регистрации.")).check(matches(isDisplayed()))
    }

    @Test
    fun checkEmptyEmailValidation() {
        onView(withId(R.id.nameEditText)).perform(clearText(), typeText("Stepan"), closeSoftKeyboard())
        onView(withId(R.id.surnameEditText)).perform(clearText(), typeText("Gordeychik"), closeSoftKeyboard())
        onView(withId(R.id.emailEditText)).perform(clearText())

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.re_passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())
        onView(withText("Заполните все поля для регистрации.")).check(matches(isDisplayed()))
    }

    @Test
    fun checkEmptyPasswordValidation() {
        onView(withId(R.id.nameEditText)).perform(clearText(), typeText("Stepan"), closeSoftKeyboard())
        onView(withId(R.id.surnameEditText)).perform(clearText(), typeText("Gordeychik"), closeSoftKeyboard())
        onView(withId(R.id.emailEditText)).perform(clearText(), typeText("test@example.com"), closeSoftKeyboard())

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.passwordEditText)).perform(clearText())
        onView(withId(R.id.re_passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())
        onView(withText("Заполните все поля для регистрации.")).check(matches(isDisplayed()))
    }

    @Test
    fun checkEmptyRePasswordValidation() {
        onView(withId(R.id.nameEditText)).perform(clearText(), typeText("Stepan"), closeSoftKeyboard())
        onView(withId(R.id.surnameEditText)).perform(clearText(), typeText("Gordeychik"), closeSoftKeyboard())
        onView(withId(R.id.emailEditText)).perform(clearText(), typeText("test@example.com"), closeSoftKeyboard())

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.re_passwordEditText)).perform(clearText())

        onView(withId(R.id.registerButton)).perform(click())
        onView(withText("Заполните все поля для регистрации.")).check(matches(isDisplayed()))
    }

    @Test
    fun checkInvalidEmailValidation() {
        onView(withId(R.id.nameEditText)).perform(clearText(), typeText("Stepan"), closeSoftKeyboard())
        onView(withId(R.id.surnameEditText)).perform(clearText(), typeText("Gordeychik"), closeSoftKeyboard())
        onView(withId(R.id.emailEditText)).perform(clearText(), typeText("invalidemail"), closeSoftKeyboard())

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.re_passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())
        onView(withText("Невверный ввод почты.")).check(matches(isDisplayed()))
    }

    @Test
    fun checkMismatchedPasswordsValidation() {
        onView(withId(R.id.nameEditText)).perform(clearText(), typeText("Stepan"), closeSoftKeyboard())
        onView(withId(R.id.surnameEditText)).perform(clearText(), typeText("Gordeychik"), closeSoftKeyboard())
        onView(withId(R.id.emailEditText)).perform(clearText(), typeText("test@example.com"), closeSoftKeyboard())

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.re_passwordEditText)).perform(clearText(), typeText("different"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())
        onView(withText("Пароли не совпадают.")).check(matches(isDisplayed()))
    }

    @Test
    fun testSuccessfulRegistration() {
        onView(withId(R.id.nameEditText)).perform(clearText(), typeText("Stepan"), closeSoftKeyboard())
        onView(withId(R.id.surnameEditText)).perform(clearText(), typeText("Gordeychik"), closeSoftKeyboard())
        onView(withId(R.id.emailEditText)).perform(clearText(), typeText("test@example.com"), closeSoftKeyboard())

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.swipe(100, 800, 100, 100, 10)

        Thread.sleep(500)

        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.re_passwordEditText)).perform(clearText(), typeText("password"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        Thread.sleep(3000)

        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
    }
}