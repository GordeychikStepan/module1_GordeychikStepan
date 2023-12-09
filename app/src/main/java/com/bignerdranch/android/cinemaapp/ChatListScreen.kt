package com.bignerdranch.android.cinemaapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ChatListScreen : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var chatListAdapter: ChatListAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "com.example.sharedPrefFile"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list_screen)

        val chatListRecyclerView: RecyclerView = findViewById(R.id.chatListRecyclerView)
        val backButton: ImageView = findViewById(R.id.backImage)

        val layoutManager = LinearLayoutManager(this)
        chatListRecyclerView.layoutManager = layoutManager

        // инициализация адаптера с обработчиком нажатий
        chatListAdapter = ChatListAdapter(emptyList()) { group ->
            val intent = Intent(this, ChatScreen::class.java)
            intent.putExtra("groupId", group.id)
            startActivity(intent)
        }

        chatListRecyclerView.adapter = chatListAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://virtserver.swaggerhub.com/focus.lvlup2021/LEVEL_UP_MOBILE/1.0.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        loadGroups(accessToken)


        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_out_right, R.anim.fade_out)
        }
    }

    private fun loadGroups(accessToken: String?) {
        if (accessToken != null) {
            apiService.getGroup(accessToken).enqueue(object : Callback<GroupResponse> {
                override fun onResponse(call: Call<GroupResponse>, response: Response<GroupResponse>) {
                    if (response.isSuccessful) {
                        val groupResponse = response.body()
                        groupResponse?.let {
                            if (it.success) {
                                val groups = listOf(it.group)
                                chatListAdapter.updateData(groups)
                            }
                        }
                    } else {
                        handleApiError(response.code())
                    }
                }

                override fun onFailure(call: Call<GroupResponse>, t: Throwable) {
                    handleApiFailure(t)
                }
            })
        }
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
            401 -> showErrorDialog("Пользователь не является участником.")
            404 -> showErrorDialog("Группа не найдена.")
            else -> showErrorDialog("Ошибка запроса. Код: $errorCode")
        }
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
