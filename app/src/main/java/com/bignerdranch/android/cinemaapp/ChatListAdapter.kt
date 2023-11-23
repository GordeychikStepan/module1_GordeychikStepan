package com.bignerdranch.android.cinemaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatListAdapter(private val chatList: List<ChatModel>) :
    RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    private var itemClickListener: ((ChatModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_list, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatModel = chatList[position]
        holder.bind(chatModel)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chatImageView: ImageView = itemView.findViewById(R.id.chatImageView)
        private val chatNameTextView: TextView = itemView.findViewById(R.id.chatNameTextView)
        private val lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessageTextView)

        init {
            itemView.setOnClickListener {
                itemClickListener?.invoke(chatList[adapterPosition])
            }
        }

        fun bind(chatModel: ChatModel) {
            // Здесь устанавливайте данные в соответствии с вашим макетом
            chatImageView.setImageResource(chatModel.imageResId)
            chatNameTextView.text = chatModel.chatName
            lastMessageTextView.text = chatModel.getLastMessageTextWithSender()
        }
    }

    fun setOnItemClickListener(listener: (ChatModel) -> Unit) {
        itemClickListener = listener
    }
}
