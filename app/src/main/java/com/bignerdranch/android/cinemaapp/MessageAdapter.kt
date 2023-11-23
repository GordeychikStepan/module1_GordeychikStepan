package com.bignerdranch.android.cinemaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val messageList: List<MessageModel>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutRes = when (viewType) {
            NORMAL_RECEIVED -> R.layout.item_message_received
            LAST_RECEIVED -> R.layout.item_message_received_last
            NORMAL_SENT -> R.layout.item_message_sent
            LAST_SENT -> R.layout.item_message_sent_last
            else -> throw IllegalArgumentException("Invalid view type")
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        val viewType = getItemViewType(position)
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val current = messageList[position]
        val previous = if (position > 0) messageList[position - 1] else null
        val next = if (position < messageList.size - 1) messageList[position + 1] else null

        return when {
            current.isSentByMe -> {
                if (previous == null || current.userName != previous.userName) {
                    NORMAL_SENT
                } else {
                    LAST_SENT
                }
            }
            else -> {
                if (previous == null || current.userName != previous.userName) {
                    NORMAL_RECEIVED
                } else {
                    LAST_RECEIVED
                }
            }
        }
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
        private val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)

        fun bind(message: MessageModel) {
            messageTextView.text = message.text
            val formattedTimestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            timestampTextView.text = formattedTimestamp
            profileImageView.setImageResource(message.profilePhoto)
            userNameTextView.text = message.userName
        }
    }

    companion object {
        const val NORMAL_RECEIVED = 1
        const val LAST_RECEIVED = 2
        const val NORMAL_SENT = 3
        const val LAST_SENT = 4
    }
}