package com.bignerdranch.android.cinemaapp

data class MessageModel(
    val text: String,
    val timestamp: Long,
    val isSentByMe: Boolean,
    val userName: String,
    val profilePhoto: Int
)
