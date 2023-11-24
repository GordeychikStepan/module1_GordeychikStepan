package com.bignerdranch.android.cinemaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImageView: ImageView = itemView.findViewById(R.id.moviePosterImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.movieTitleTextView)
        val overviewTextView: TextView = itemView.findViewById(R.id.movieOverviewTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        // Загрузка изображения с использованием Glide
        Glide.with(holder.itemView.context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.movie) // Замените placeholder своим изображением
            .into(holder.posterImageView)

        holder.titleTextView.text = movie.nameRu
        holder.overviewTextView.text = movie.nameEn
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}