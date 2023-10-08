package com.example.airmovies.data.tv

data class TvFirebase(
    val tv: ArrayList<TvWatchlist>
) {
    constructor(): this(ArrayList())
}