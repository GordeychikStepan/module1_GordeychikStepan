package com.bignerdranch.android.cinemaapp

import java.util.*

data class MessageModel(
    val text: String,
    val date: String,
    val isSentByMe: Boolean,
    val userName: String,
    val profilePhoto: Int,
    val isDateMessage: Boolean = false
)
