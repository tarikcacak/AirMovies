package com.example.airmovies.data.movie

data class MovieWatchlist(
    val movieId: String?,
    val posterPath: String?,
    val title: String?,
    val voteAverage: String?
) {
    constructor(): this("","", "", "")
}