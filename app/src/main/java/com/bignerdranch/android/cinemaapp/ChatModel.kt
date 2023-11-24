package com.bignerdranch.android.cinemaapp

data class ChatModel(
    val chatNumber: Int,
    val chatName: String,
    val imageResId: Int,
    val messages: MutableList<MessageModel>
) {
    fun getLastMessageTextWithSender(): String {
        val lastMessage = messages.lastOrNull()
        return if (lastMessage != null) {
            "${lastMessage.userName}: ${lastMessage.text}"
        } else {
            ""
        }
    }
}

