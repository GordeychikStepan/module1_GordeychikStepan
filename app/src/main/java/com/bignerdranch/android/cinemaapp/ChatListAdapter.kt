package com.bignerdranch.android.cinemaapp

import android.annotation.SuppressLint
import android.graphics.*
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ChatListAdapter(
    private var groups: List<GroupResponse.Group>,
    private val onGroupClick: (GroupResponse.Group) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newGroups: List<GroupResponse.Group>) {
        groups = newGroups
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_list, parent, false)
        return ChatViewHolder(view, onGroupClick) // Передаем onGroupClick в ViewHolder
    }
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount(): Int = groups.size

    class ChatViewHolder(itemView: View, private val onGroupClick: (GroupResponse.Group) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private val chatNameTextView: TextView = itemView.findViewById(R.id.chatNameTextView)
        private val lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessageTextView)
        private val chatImageView: ImageView = itemView.findViewById(R.id.chatImageView)

        fun bind(group: GroupResponse.Group) {
            chatNameTextView.text = group.name
            setLastMessage(group.posts.lastOrNull())
            setChatImage(group)

            itemView.setOnClickListener { this.onGroupClick(group) }
        }

        private fun setLastMessage(lastPost: GroupResponse.Post?) {
            lastMessageTextView.maxLines = 2
            if (lastPost != null) {
                val username = lastPost.username + ": "
                val message = lastPost.caption
                val fullText = username + message

                val spannableString = SpannableString(fullText).apply {
                    setSpan(
                        ForegroundColorSpan(Color.parseColor("#AFAFAF")),
                        0,
                        username.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    setSpan(
                        ForegroundColorSpan(Color.WHITE),
                        username.length,
                        fullText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                lastMessageTextView.text = spannableString
            } else {
                lastMessageTextView.text = "Нет сообщений"
            }
        }

        private fun setChatImage(group: GroupResponse.Group) {
            val lastPostImageUrl = group.posts.lastOrNull()?.urlMedia
            if (!lastPostImageUrl.isNullOrEmpty()) {

                Glide.with(itemView.context)
                    .load(lastPostImageUrl)
                    .placeholder(R.drawable.ellipse_to_chat)
                    .error(createAbbreviationBitmap(group.name))
                    .into(chatImageView)

            } else {
                chatImageView.setImageBitmap(createAbbreviationBitmap(group.name))
            }
        }

        private fun createAbbreviationBitmap(groupName: String): Bitmap {
            val abbreviation = getAbbreviation(groupName)

            val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ellipse_to_chat)
            drawable?.setBounds(0, 0, canvas.width, canvas.height)
            drawable?.draw(canvas)

            val paint = Paint().apply {
                isAntiAlias = true
                color = Color.WHITE
                textSize = 180f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
            }

            val textBounds = Rect()
            paint.getTextBounds(abbreviation, 0, abbreviation.length, textBounds)
            val x = canvas.width / 2f
            val y = canvas.height / 2f + textBounds.height() / 2f

            canvas.drawText(abbreviation, x, y, paint)

            return bitmap
        }


        private fun getAbbreviation(text: String): String {
            val words = text.split(" ")
            return if (words.size > 1) {
                (words[0].take(1) + words[1].take(1)).toUpperCase()
            } else {
                text.take(2).toUpperCase()
            }
        }

    }

}
