package com.bignerdranch.android.cinemaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatListScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list_screen)

        val chatListRecyclerView: RecyclerView = findViewById(R.id.chatListRecyclerView)

        val layoutManager = LinearLayoutManager(this)
        chatListRecyclerView.layoutManager = layoutManager

        val chatList = MessagesData.getAllChats()

        val chatListAdapter = ChatListAdapter(chatList)
        chatListRecyclerView.adapter = chatListAdapter

        chatListAdapter.setOnItemClickListener { chatModel ->
            // Переход к чату
            val intent = Intent(this, ChatScreen::class.java)
            intent.putExtra("chatNumber", chatModel.chatNumber)
            startActivity(intent)
        }
    }
}
