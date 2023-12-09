package com.bignerdranch.android.cinemaapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SignInScreen : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val sharedPrefFile = "com.example.sharedPrefFile"
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_screen)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://virtserver.swaggerhub.com/Focus.Lvlup/LEVEL_UP_SECURITY/1.0.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        val registerButton: Button = findViewById(R.id.registerInButton)
        registerButton.setOnClickListener {
            val intent = Intent(this, SignUpScreen::class.java)
            startActivity(intent)
        }

        sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        val savedEmail = sharedPreferences.getString("email", null)
        emailEditText.setText(savedEmail)

        val signInButton: Button = findViewById(R.id.signInButton)
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showErrorDialog("Заполните все поля")
            } else if (!isEmailValid(email)) {
                showErrorDialog("Неверный формат email")
            } else {
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginData = LoginData(email, password)
        apiService.loginUser(loginData).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { signingResponse ->

                        val accessToken = signingResponse.data.accessToken
                        Log.d("SignIn", "Token: $accessToken")
                        saveUserToken(accessToken)
                        navigateToMainScreen()

                    } ?: run {
                        showErrorDialog("Ошибка получения данных от сервера")
                    }
                } else {
                    handleApiError(response.code())
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                handleApiFailure(t)
            }
        })
    }

    private fun handleApiFailure(t: Throwable) {
        if (t is IOException) {
            showToast("Отсутствует подключение к интернету")
        } else {
            showErrorDialog("Не удалось выполнить запрос: ${t.localizedMessage}")
        }
        Log.e("PremieresResponse", "Failed to execute the request", t)
    }

    private fun handleApiError(errorCode: Int) {
        when (errorCode) {
            400 -> showErrorDialog("Пожалуйста, проверьте введенные данные.")
            401 -> showErrorDialog("Ошибка аутентификации.")
            403 -> showErrorDialog("Доступ запрещен. Пожалуйста, подтвердите вашу электронную почту для продолжения.")
            else -> showErrorDialog("Ошибка запроса. Код: $errorCode")
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Regex("[^@]+@[^@]+\\.[^@]+")
        return pattern.matches(email)
    }

    private fun saveUserToken(accessToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", accessToken)
        editor.apply()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Ошибка")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}