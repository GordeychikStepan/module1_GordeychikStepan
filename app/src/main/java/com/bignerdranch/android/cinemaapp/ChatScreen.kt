package com.bignerdranch.android.cinemaapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ChatScreen : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var chatAdapter: MessageAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatTitleTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "com.example.sharedPrefFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        chatTitleTextView = findViewById(R.id.chatTitleTextView)
        val messageEditText: EditText = findViewById(R.id.messageEditText)
        val sendButton: Button = findViewById(R.id.sendButton)
        val backButton: ImageView = findViewById(R.id.backImage)

        sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://virtserver.swaggerhub.com/focus.lvlup2021/LEVEL_UP_MOBILE/1.0.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        val groupId = intent.getStringExtra("groupId") ?: return finish()
        loadChat(groupId)

        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_out_right, R.anim.fade_out)
        }

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                val currentDate = getCurrentTimeString()
                val accessToken = sharedPreferences.getString("accessToken", null) ?: return@setOnClickListener
                val name = sharedPreferences.getString("name", "You") ?: "You"
                val imagePath = sharedPreferences.getString("imagePath", null)

                val newPostData = NewPostData(
                    userId = accessToken,
                    urlMedia = "",
                    content = messageText
                )

                apiService.newPost(newPostData).enqueue(object : Callback<NewPostResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(call: Call<NewPostResponse>, response: Response<NewPostResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {

                            val newMessage = MessageModel(
                                text = messageText,
                                date = currentDate,
                                isSentByMe = true,
                                userName = name,
                                profilePhoto = imagePath
                            )

                            val messages = chatAdapter.messageList.toMutableList()
                            messages.add(newMessage)
                            chatAdapter.messageList = messages
                            chatAdapter.notifyDataSetChanged()
                            messageEditText.text.clear()
                            chatRecyclerView.scrollToPosition(messages.size - 1)

                        } else {
                            handleApiErrorSend(response.code())
                        }
                    }

                    override fun onFailure(call: Call<NewPostResponse>, t: Throwable) {
                        handleApiFailure(t)
                    }
                })
            }
        }

    }

    private fun loadChat(groupId: String) {
        apiService.getGroup(groupId).enqueue(object : Callback<GroupResponse> {
            override fun onResponse(call: Call<GroupResponse>, response: Response<GroupResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chatTitleTextView.text = it.group.name
                        chatAdapter = MessageAdapter(it.group.posts.map { post ->
                            MessageModel(post.caption, post.publishDate, false, post.username, post.avatar)
                        })
                        chatRecyclerView.adapter = chatAdapter
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
    private fun handleApiErrorSend(errorCode: Int) {
        when (errorCode) {
            404 -> showErrorDialog("Пользователь не найден. Фатальная ошибка.")
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

    private fun getCurrentTimeString(): String {
        val dateFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'S", Locale.ENGLISH)
        return dateFormat.format(Date())
    }
}
