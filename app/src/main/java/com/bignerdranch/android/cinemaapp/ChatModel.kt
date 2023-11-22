package com.bignerdranch.android.cinemaapp

data class ChatModel(
    val userName: String,
    val messages: MutableList<MessageModel>
)
