package com.bignerdranch.android.cinemaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter (private val messageList: List<MessageModel>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutRes = if (viewType == SENT_BY_ME) R.layout.item_message_sent else R.layout.item_message_received
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].isSentByMe) SENT_BY_ME else RECEIVED
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
        private val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)

        fun bind(message: MessageModel) {
            messageTextView.text = message.text
            timestampTextView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp))
            profileImageView.setImageResource(message.profilePhoto)
            userNameTextView.text = message.userName
        }
    }

    companion object {
        const val SENT_BY_ME = 1
        const val RECEIVED = 2
    }
}