package com.bignerdranch.android.cinemaapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

// загрузочный экран
@SuppressLint("CustomSplashScreen")
class LaunchScreenActivity : AppCompatActivity() {
    private val splashDelay: Long = 2000
    private val sharedPrefFile = "com.example.sharedPrefFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen)

        Handler().postDelayed({

            val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
            val isRegistered = sharedPreferences.getBoolean("isRegistered", false)

            val intent = if (isRegistered) {
                Intent(this@LaunchScreenActivity, SignInScreen::class.java)
            } else {
                Intent(this@LaunchScreenActivity, SignUpScreen::class.java)
            }

            startActivity(intent)
            finish()
        }, splashDelay)
    }
}