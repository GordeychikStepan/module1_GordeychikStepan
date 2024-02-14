package com.bignerdranch.android.cinemaapp

data class MessageModel(
    val text: String,
    val date: String,
    val isSentByMe: Boolean,
    val userName: String,
    var profilePhoto: String? = null,
    val isDateMessage: Boolean = false
)
