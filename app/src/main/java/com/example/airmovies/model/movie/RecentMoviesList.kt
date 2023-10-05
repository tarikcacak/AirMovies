package com.example.airmovies.model.movie


import com.google.gson.annotations.SerializedName

data class RecentMoviesList(
    @SerializedName("results")
    val results: List<RecentMoviesResult>
)