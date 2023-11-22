package com.bignerdranch.android.cinemaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout

class ChatListScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list_screen)

        val chat1LinearLayout: LinearLayout = findViewById(R.id.chat1LinearLayout)

        chat1LinearLayout.setOnClickListener {
            // Переход к чату
            val intent = Intent(this, ChatScreen::class.java)
            startActivity(intent)
        }

    }
}