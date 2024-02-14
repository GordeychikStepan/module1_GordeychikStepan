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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SignUpScreen : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val sharedPrefFile = "com.example.sharedPrefFile"
    private lateinit var nameEditText: EditText
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_screen)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://virtserver.swaggerhub.com/Focus.Lvlup/LEVEL_UP_SECURITY/1.0.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        val iHaveAccountButton: Button = findViewById(R.id.iHaveAccountButton)
        iHaveAccountButton.setOnClickListener {
            val intent = Intent(this, SignInScreen::class.java)
            startActivity(intent)
        }

        sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        nameEditText = findViewById(R.id.nameEditText)
        val savedName = sharedPreferences.getString("name", "")
        nameEditText.setText(savedName)

        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            val surnameEditText: EditText = findViewById(R.id.surnameEditText)
            val rePasswordEditText: EditText = findViewById(R.id.re_passwordEditText)
            val emailEditText: EditText = findViewById(R.id.emailEditText)
            val passwordEditText: EditText = findViewById(R.id.passwordEditText)

            if (nameEditText.text.toString().isEmpty() || surnameEditText.text.toString().isEmpty() ||
                emailEditText.text.toString().isEmpty() || passwordEditText.text.toString().isEmpty() ||
                rePasswordEditText.text.toString().isEmpty()) {

                showErrorDialog("Заполните все поля для регистрации.")
                //Toast.makeText(this, "Заполните все поля для ввода.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (passwordEditText.text.toString() != rePasswordEditText.text.toString()){
                showErrorDialog("Пароли не совпадают.")
                //Toast.makeText(this, "Пароли не совпадают.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!isEmailValid(emailEditText.text.toString())) {
                showErrorDialog("Невверный ввод почты.")
                //Toast.makeText(this, "Невверный ввод почты.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registrationData = RegistrationData(
                email = emailEditText.text.toString(),
                password = passwordEditText.text.toString(),
                firstName = nameEditText.text.toString(),
                lastName = surnameEditText.text.toString()
            )

            editor.putString("email", emailEditText.text.toString())

            registerUser(registrationData, editor)
        }
    }

    private fun registerUser(registrationData: RegistrationData, editor: SharedPreferences.Editor) {
        apiService.registerUser(registrationData).enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { registrationResponse ->

                        editor.remove("imagePath")
                        editor.putBoolean("isRegistered", true)
                        editor.apply()

                        val sharedPrefsHelper = SharedPrefsHelper(this@SignUpScreen)
                        sharedPrefsHelper.clearAll()

                        val userId = registrationResponse.data._id
                        Log.d("Registration", "UserID: $userId")
                        saveUserId(userId)
                        showToast("Вы успешно зарегистрировались.")
                        navigateToMainScreen()

                    } ?: run {
                        showErrorDialog("Ошибка получения данных от сервера")
                    }
                } else {
                    handleApiError(response.code())
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
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
            409 -> showErrorDialog("Пользователь с такой эллекстронной почтой уже существует.")
            else -> showErrorDialog("Ошибка запроса. Код: $errorCode")
        }
    }

    private fun saveUserId(accessToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", accessToken)
        editor.apply()
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Regex("[^@]+@[^@]+\\.[^@]+")
        return pattern.matches(email)
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Ошибка")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}