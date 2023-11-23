package com.bignerdranch.android.cinemaapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ChatScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)

        // Получить chatNumber из Intent
        val chatNumber = intent.getIntExtra("chatNumber", -1)

        // Если chatNumber не был передан, завершить активность
        if (chatNumber == -1) {
            finish()
            return
        }

        val chatList = mutableListOf<ChatModel>()
        val chatModel = MessagesData.getChatByNumber(chatNumber)
        chatList.add(chatModel)

        val chatRecyclerView: RecyclerView = findViewById(R.id.chatRecyclerView)
        val messageEditText: EditText = findViewById(R.id.messageEditText)
        val sendButton: Button = findViewById(R.id.sendButton)
        val chatTitleTextView: TextView = findViewById(R.id.chatTitleTextView)

        // Установка названия чата в TextView
        chatTitleTextView.text = chatModel.chatName

        val layoutManager = LinearLayoutManager(this)
        chatRecyclerView.layoutManager = layoutManager
        val chatAdapter = ChatAdapter(chatList)
        chatRecyclerView.adapter = chatAdapter

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                // Добавить новое сообщение в чат
                val currentDate = getCurrentTimeString()
                val newMessage = MessageModel(messageText, currentDate, true, "Ваше имя", R.drawable.user_profile)
                chatModel.messages.add(newMessage)
                chatAdapter.notifyItemChanged(0) // Обновить адаптер
                messageEditText.text.clear() // Очистить поле ввода

                // Скрыть клавиатуру
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(messageEditText.windowToken, 0)

                chatRecyclerView.smoothScrollToPosition(chatModel.messages.size - 1)
            }
        }
    }

    private fun getCurrentTimeString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
