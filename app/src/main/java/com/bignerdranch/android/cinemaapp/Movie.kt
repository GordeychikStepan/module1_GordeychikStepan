package com.bignerdranch.android.cinemaapp

data class Movie(
    val kinopoiskId: Int,
    val nameRu: String,
    val nameEn: String,
    val year: Int,
    val posterUrl: String,
    val posterUrlPreview: String,
    val countries: List<Country>,
    val genres: List<Genre>,
    val duration: Int,
    val premiereRu: String
)

data class Country(
    val country: String
)

data class Genre(
    val genre: String
)
