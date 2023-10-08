package com.example.airmovies.data.tv

data class TvWatchlist(
    val tvId: String?,
    val posterPath: String?,
    val title: String?,
    val voteAverage: String?
) {
    constructor(): this("", "", "", "")
}