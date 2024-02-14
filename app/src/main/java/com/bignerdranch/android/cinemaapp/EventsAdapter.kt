package com.bignerdranch.android.cinemaapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter(
    private var events: List<Event>,
    private val onEventClick: (Event) -> Unit,
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount() = events.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateEvents(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }

    inner class EventViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(event: Event) {
            val originalFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.ENGLISH)
            val targetFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
            originalFormat.timeZone = TimeZone.getTimeZone("GMT")

            try {
                val date = originalFormat.parse(event.eventDate)
                val formattedDate = date?.let { targetFormat.format(it) } ?: "Неизвестная дата"
                view.findViewById<TextView>(R.id.textDateEvent).text = formattedDate
            } catch (e: Exception) {
                view.findViewById<TextView>(R.id.textDateEvent).text = "Неверный формат даты"
            }
            view.setOnClickListener { onEventClick(event) }
        }
    }

    fun getItem(position: Int): Event {
        return events[position]
    }
}
