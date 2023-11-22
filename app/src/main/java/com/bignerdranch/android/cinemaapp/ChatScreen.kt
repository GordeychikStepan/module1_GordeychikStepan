package com.bignerdranch.android.cinemaapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)

        val chatList = createDummyData()
        val chatRecyclerView: RecyclerView = findViewById(R.id.chatRecyclerView)
        val messageEditText: EditText = findViewById(R.id.messageEditText)
        val sendButton: Button = findViewById(R.id.sendButton)

        val layoutManager = LinearLayoutManager(this)
        chatRecyclerView.layoutManager = layoutManager
        val chatAdapter = ChatAdapter(chatList)
        chatRecyclerView.adapter = chatAdapter

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                // Добавить новое сообщение в чат
                val newMessage = MessageModel(messageText, System.currentTimeMillis(), true, "Ваше имя", R.drawable.user_profile)
                chatList[0].messages.add(newMessage)
                chatAdapter.notifyItemChanged(0) // Обновить адаптер
                messageEditText.text.clear() // Очистить поле ввода

                // Скрыть клавиатуру
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(messageEditText.windowToken, 0)
            }
        }
    }

    private fun createDummyData(): MutableList<ChatModel> {
        // Создаем список чатов
        val chatList = mutableListOf<ChatModel>()

        // Чат с группой пользователей
        val groupChat = ChatModel(
            userName = "Группа",
            messages = mutableListOf(
                MessageModel("Привет всем в группе!", System.currentTimeMillis(), false, "Агата петровна", R.drawable.user_profile),
                MessageModel("Как дела?", System.currentTimeMillis() + 1000, true, "Максим потапов", R.drawable.user_profile),
                MessageModel("Давайте встретимся завтра", System.currentTimeMillis() + 2000, false, "Иван Иванов", R.drawable.user_profile)
            )
        )
        chatList.add(groupChat)

        // Возвращаем готовый список чатов
        return chatList
    }
}